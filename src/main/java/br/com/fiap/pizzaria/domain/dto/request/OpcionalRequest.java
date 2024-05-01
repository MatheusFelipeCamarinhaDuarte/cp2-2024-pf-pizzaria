package br.com.fiap.pizzaria.domain.dto.request;

import java.math.BigDecimal;

public record OpcionalRequest(
        String nome,
        BigDecimal preco,
        AbstractRequest sabor
) {
}
