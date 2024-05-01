package br.com.fiap.pizzaria.domain.dto.response;


import lombok.Builder;

import java.util.Collection;
@Builder
public record PizzariaResponse(
        Long id,
        String nome,
        Collection<ProdutoResponse> cardapio
){
}
