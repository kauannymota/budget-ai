package com.dio.budget_ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transacao(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        LocalDateTime data
) {
}