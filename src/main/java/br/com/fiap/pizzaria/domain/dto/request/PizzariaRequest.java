package br.com.fiap.pizzaria.domain.dto.request;

import java.util.Collection;

public record PizzariaRequest(
        String nome,
        Collection<AbstractRequest> cardapio

        ){
}
