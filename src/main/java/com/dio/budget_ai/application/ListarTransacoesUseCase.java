package com.dio.budget_ai.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dio.budget_ai.domain.model.Transacao;
import com.dio.budget_ai.domain.repository.TransacaoRepository;

@Service
public class ListarTransacoesUseCase {

    private final TransacaoRepository repository;

    public ListarTransacoesUseCase(
            TransacaoRepository repository
    ) {
        this.repository = repository;
    }

    public List<Transacao> executar() {
        return repository.listarTodas();
    }
}