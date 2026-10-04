package com.dio.budget_ai.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dio.budget_ai.domain.model.TipoTransacao;
import com.dio.budget_ai.domain.model.Transacao;
import com.dio.budget_ai.domain.repository.TransacaoRepository;

@Service
public class CriarTransacaoUseCase {

    private final TransacaoRepository repository;

    public CriarTransacaoUseCase(TransacaoRepository repository) {
        this.repository = repository;
    }

    public Transacao executar(
            String descricao,
            BigDecimal valor,
            TipoTransacao tipo
    ) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException(
                    "A descrição da transação é obrigatória."
            );
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor da transação deve ser maior que zero."
            );
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da transação é obrigatório."
            );
        }

        Transacao transacao = new Transacao(
                null,
                descricao,
                valor,
                tipo,
                LocalDateTime.now()
        );

        return repository.salvar(transacao);
    }
}