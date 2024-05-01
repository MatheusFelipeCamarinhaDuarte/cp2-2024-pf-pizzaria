package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.OpcionalRequest;
import br.com.fiap.pizzaria.domain.dto.request.SaborRequest;
import br.com.fiap.pizzaria.domain.dto.response.OpcionalResponse;
import br.com.fiap.pizzaria.domain.entity.Opcional;
import br.com.fiap.pizzaria.domain.entity.Sabor;
import br.com.fiap.pizzaria.domain.service.OpcionalService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping(value = "/opcional")
public class OpcionalResource implements ResourceDTO<OpcionalRequest, OpcionalResponse> {

    @Autowired
    private OpcionalService service;

    @GetMapping
    public ResponseEntity<Collection<OpcionalResponse>> findAll(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "preco", required = false) BigDecimal preco,
            @RequestParam(name = "saborId", required = false) Long saborId,
            @RequestParam(name = "sabor", required = false) String saborNome
            ) {

        Sabor sabor = Sabor.builder()
                .id(saborId)
                .nome(saborNome)
                .build();

        Opcional opcional = Opcional.builder()
                .nome(nome)
                .preco(preco)
                .sabor(sabor)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Opcional> example = Example.of(opcional, matcher);

        var encontrados = service.findAll(example);
        var resposta = encontrados.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping(value = "/{id}")
    @Override
    public ResponseEntity<OpcionalResponse> findById(@PathVariable Long id) {
        var encontrados = service.findById(id);
        var resposta = service.toResponse(encontrados);

        return ResponseEntity.ok(resposta);
    }

    @Transactional
    @PostMapping
    @Override
    public ResponseEntity<OpcionalResponse> save(@RequestBody @Valid OpcionalRequest r) {
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
