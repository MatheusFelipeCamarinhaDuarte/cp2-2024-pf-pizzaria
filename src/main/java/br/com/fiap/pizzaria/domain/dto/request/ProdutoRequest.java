package br.com.fiap.pizzaria.domain.dto.request;

import java.math.BigDecimal;
import java.util.Collection;

public record ProdutoRequest(
        String nome,
        BigDecimal preco,
        AbstractRequest sabor,
        Collection<AbstractRequest> opcionais
){
}
