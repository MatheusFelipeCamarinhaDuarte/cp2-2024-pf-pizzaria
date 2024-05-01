package br.com.fiap.pizzaria.domain.resource;

import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface ResourceDTO<Request, Response> {



    ResponseEntity<Response> findById(Long id);

    ResponseEntity<Response> save(Request r);
}
