package com.dio.budget_ai.infrastructure.ai;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.dio.budget_ai.application.ConsultarSaldoUseCase;
import com.dio.budget_ai.application.CriarTransacaoUseCase;
import com.dio.budget_ai.application.ListarTransacoesUseCase;
import com.dio.budget_ai.domain.model.TipoTransacao;
import com.dio.budget_ai.domain.model.Transacao;

@Component
public class FinanceTools {

    private final CriarTransacaoUseCase criarTransacaoUseCase;
    private final ConsultarSaldoUseCase consultarSaldoUseCase;
    private final ListarTransacoesUseCase listarTransacoesUseCase;

    public FinanceTools(
            CriarTransacaoUseCase criarTransacaoUseCase,
            ConsultarSaldoUseCase consultarSaldoUseCase,
            ListarTransacoesUseCase listarTransacoesUseCase) {

        this.criarTransacaoUseCase = criarTransacaoUseCase;
        this.consultarSaldoUseCase = consultarSaldoUseCase;
        this.listarTransacoesUseCase = listarTransacoesUseCase;
    }

    @Tool(description = "Cria uma nova transação financeira.")
    public String criarTransacao(
            String descricao,
            BigDecimal valor,
            String tipo) {

        TipoTransacao tipoTransacao =
                TipoTransacao.valueOf(tipo.toUpperCase());

        Transacao transacao =
                criarTransacaoUseCase.executar(
                        descricao,
                        valor,
                        tipoTransacao
                );

        return "Transação criada com sucesso. ID: "
                + transacao.id();
    }

    @Tool(description = "Consulta o saldo financeiro atual.")
    public String consultarSaldo() {

        BigDecimal saldo =
                consultarSaldoUseCase.executar();

        return "Saldo atual: R$ " + saldo;
    }

    @Tool(description = "Lista todas as transações financeiras.")
    public List<Transacao> listarTransacoes() {

        return listarTransacoesUseCase.executar();
    }
}