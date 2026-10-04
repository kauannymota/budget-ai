package com.dio.budget_ai.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTransacaoRepository
        extends JpaRepository<TransacaoEntity, Long> {
}