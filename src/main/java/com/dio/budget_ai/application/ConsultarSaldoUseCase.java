package com.dio.budget_ai.application;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.dio.budget_ai.domain.model.TipoTransacao;
import com.dio.budget_ai.domain.repository.TransacaoRepository;

@Service
public class ConsultarSaldoUseCase {

    private final TransacaoRepository repository;

    public ConsultarSaldoUseCase(
            TransacaoRepository repository) {

        this.repository = repository;
    }

    public BigDecimal executar() {

        BigDecimal saldo = BigDecimal.ZERO;

        for (var transacao : repository.listarTodas()) {

            if (transacao.tipo() == TipoTransacao.RECEITA) {
                saldo = saldo.add(transacao.valor());
            } else {
                saldo = saldo.subtract(transacao.valor());
            }
        }

        return saldo;
    }
}