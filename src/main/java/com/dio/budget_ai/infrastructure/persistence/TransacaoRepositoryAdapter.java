package com.dio.budget_ai.infrastructure.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dio.budget_ai.domain.model.Transacao;
import com.dio.budget_ai.domain.repository.TransacaoRepository;

@Repository
public class TransacaoRepositoryAdapter
        implements TransacaoRepository {

    private final SpringDataTransacaoRepository repository;

    public TransacaoRepositoryAdapter(
            SpringDataTransacaoRepository repository) {

        this.repository = repository;
    }

    @Override
    public Transacao salvar(Transacao transacao) {

        TransacaoEntity entity = new TransacaoEntity(
                transacao.id(),
                transacao.descricao(),
                transacao.valor(),
                transacao.tipo(),
                transacao.data()
        );

        TransacaoEntity salva = repository.save(entity);

        return converter(salva);
    }

    @Override
    public List<Transacao> listarTodas() {

        return repository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    private Transacao converter(
            TransacaoEntity entity) {

        return new Transacao(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getTipo(),
                entity.getData()
        );
    }
}