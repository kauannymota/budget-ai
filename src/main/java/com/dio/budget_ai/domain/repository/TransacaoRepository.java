package com.dio.budget_ai.domain.repository;

import java.util.List;

import com.dio.budget_ai.domain.model.Transacao;

public interface TransacaoRepository {

    Transacao salvar(Transacao transacao);

    List<Transacao> listarTodas();
}