package com.dio.budget_ai.infrastructure.ai;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssistenteFinanceiroService {

    private final ChatClient chatClient;
    private final FinanceTools financeTools;

    public AssistenteFinanceiroService(
            ChatClient.Builder builder,
            FinanceTools financeTools) {

        this.chatClient = builder.build();
        this.financeTools = financeTools;
    }

    public String processarMensagem(String mensagem) {

        try {

            return chatClient
                    .prompt()
                    .system("""
                            Você é um assistente financeiro.

                            Sua função é interpretar comandos financeiros
                            da pessoa usuária.

                            Use as ferramentas disponíveis sempre que
                            for necessário criar transações,
                            consultar saldo ou listar transações.

                            Os tipos válidos são:

                            RECEITA
                            DESPESA

                            Responda sempre em português,
                            de forma clara e curta.
                            """)
                    .user(mensagem)
                    .tools(financeTools)
                    .call()
                    .content();

        } catch (Exception e) {

            return processarLocalmente(mensagem);
        }
    }

    private String processarLocalmente(String mensagem) {

        String texto = mensagem.toLowerCase();

        // CONSULTAR SALDO
        if (texto.contains("saldo")) {

            return financeTools.consultarSaldo();
        }

        // LISTAR TRANSAÇÕES
        if (texto.contains("listar")
                || texto.contains("transações")
                || texto.contains("transacoes")) {

            return financeTools
                    .listarTransacoes()
                    .toString();
        }

        // CRIAR DESPESA
        if (texto.contains("despesa")
                || texto.contains("gastei")
                || texto.contains("paguei")) {

            BigDecimal valor = extrairValor(texto);

            if (valor == null) {
                return "Não consegui identificar o valor da despesa.";
            }

            String descricao =
                    extrairDescricao(texto, "despesa");

            return financeTools.criarTransacao(
                    descricao,
                    valor,
                    "DESPESA"
            );
        }

        // CRIAR RECEITA
        if (texto.contains("receita")
                || texto.contains("recebi")
                || texto.contains("ganhei")) {

            BigDecimal valor = extrairValor(texto);

            if (valor == null) {
                return "Não consegui identificar o valor da receita.";
            }

            String descricao =
                    extrairDescricao(texto, "receita");

            return financeTools.criarTransacao(
                    descricao,
                    valor,
                    "RECEITA"
            );
        }

        return """
                O serviço de IA externo está indisponível no momento.

                Com o modo local você pode:
                - criar despesas;
                - criar receitas;
                - consultar saldo;
                - listar transações.
                """;
    }

    // Extrai o primeiro valor numérico da mensagem
    private BigDecimal extrairValor(String texto) {

        Pattern pattern =
                Pattern.compile("(\\d+(?:[.,]\\d{1,2})?)");

        Matcher matcher =
                pattern.matcher(texto);

        if (matcher.find()) {

            String numero =
                    matcher.group(1)
                            .replace(",", ".");

            return new BigDecimal(numero);
        }

        return null;
    }

    // Limpa a mensagem e gera uma descrição simples
    private String extrairDescricao(
            String texto,
            String tipo) {

        String descricao = texto
                .replace("adicione", "")
                .replace("adicionar", "")
                .replace("registre", "")
                .replace("registrar", "")
                .replace("uma", "")
                .replace("um", "")
                .replace(tipo, "")
                .replace("reais", "")
                .replace("real", "")
                .replaceAll("\\d+(?:[.,]\\d{1,2})?", "")
                .replace("com", "")
                .replace("de", "")
                .replaceAll("\\s+", " ")
                .trim();

        if (descricao.isBlank()) {

            return tipo.equals("despesa")
                    ? "Despesa"
                    : "Receita";
        }

        return descricao;
    }
}