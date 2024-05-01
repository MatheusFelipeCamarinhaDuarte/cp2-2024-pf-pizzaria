package br.com.fiap.pizzaria.domain.dto.response;

import lombok.Builder;

@Builder
public record SaborResponse(
        Long id,
        String nome,
        String descricao
){
}
