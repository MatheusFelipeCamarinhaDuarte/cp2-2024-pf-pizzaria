package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record AbstractRequest(
        @NotNull(message = "O campo id não pode ser nulo")
        long id
) {

}
