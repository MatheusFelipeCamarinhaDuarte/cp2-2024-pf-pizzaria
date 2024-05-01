package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.SaborRequest;
import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import br.com.fiap.pizzaria.domain.entity.Sabor;
import br.com.fiap.pizzaria.domain.service.SaborService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping(value = "/sabor")
public class SaborResource implements ResourceDTO<SaborRequest, SaborResponse> {

    @Autowired
    private SaborService service;


    @GetMapping
    public ResponseEntity<Collection<SaborResponse>> findAll(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "descricao", required = false) String descricao
            ) {

        Sabor sabor = Sabor.builder()
                .nome(nome)
                .descricao(descricao)
                .build();


        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Sabor> example = Example.of(sabor, matcher);

        var encontrados = service.findAll(example);
        var resposta = encontrados.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(resposta);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<SaborResponse> findById(@PathVariable Long id) {
        var encontrado = service.findById(id);
        var resposta = service.toResponse(encontrado);
        return ResponseEntity.ok(resposta);
    }

    @Override
    @Transactional
    @PostMapping
    public ResponseEntity<SaborResponse> save(@RequestBody @Valid SaborRequest r) {
        var entity = service.toEntity(r);
        var saved = service.save(entity);
        var resposta = service.toResponse(saved);


        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(resposta);

    }
}
