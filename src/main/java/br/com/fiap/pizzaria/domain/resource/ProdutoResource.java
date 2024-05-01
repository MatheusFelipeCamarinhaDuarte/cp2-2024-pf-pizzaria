package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.OpcionalRequest;
import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.OpcionalResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.entity.Opcional;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.entity.Sabor;
import br.com.fiap.pizzaria.domain.service.OpcionalService;
import br.com.fiap.pizzaria.domain.service.ProdutoService;
import br.com.fiap.pizzaria.domain.service.SaborService;
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
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoResource implements ResourceDTO<ProdutoRequest, ProdutoResponse>{

    @Autowired
    private ProdutoService service;

    @Autowired
    private OpcionalService opcionalService;

    @GetMapping
    public ResponseEntity<Collection<ProdutoResponse>> findAll(

            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "preco", required = false) BigDecimal preco,
            @RequestParam(name = "saborId", required = false) Long saborId,
            @RequestParam(name = "sabor", required = false) String saborNome,
            @RequestParam(name = "opcionalId",required = false) Long opcionalId,
            @RequestParam(name = "opcional",required = false) String opcionalNome
    ) {

        Opcional opcional = Opcional.builder()
                .id(opcionalId)
                .nome(opcionalNome)
                .build();
        Set<Opcional> opcionals = new LinkedHashSet<>();
        opcionals.add(opcional);

        Sabor sabor = Sabor.builder()
                .id(saborId)
                .nome(saborNome)
                .build();

        Produto produto = Produto.builder()
                .nome(nome)
                .sabor(sabor)
                .preco(preco)
                .opcionais(opcionals)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();

        Example<Produto> example = Example.of(produto, matcher);

        var encontrados = service.findAll(example);
        var resposta = encontrados.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(resposta);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable Long id) {
        var encontrados = service.findById(id);
        var resposta = service.toResponse(encontrados);

        return ResponseEntity.ok(resposta);
    }

    @Override
    @Transactional
    @PostMapping
    public ResponseEntity<ProdutoResponse> save(@RequestBody @Valid ProdutoRequest r) {
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

    @Transactional
    @PostMapping(value = "/{id}/opcionais")
    public ResponseEntity<OpcionalResponse> saveOpcional(@PathVariable Long id, @RequestBody @Valid OpcionalRequest r) {
        Produto produto = service.findById(id);

        Set<Opcional> opcionals = new LinkedHashSet<>();
        if(Objects.nonNull(produto.getOpcionais())) {
            opcionals = produto.getOpcionais();
        }


        if (Objects.isNull(r)){
            return ResponseEntity.badRequest().build();
        }
        var entity = opcionalService.toEntity(r);
        opcionals.add(entity);

        var saved = opcionalService.save(entity);
        var response = opcionalService.toResponse(saved);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(response);

    }

    @GetMapping(value = "/{id}/opcionais")
    public ResponseEntity<Collection<OpcionalResponse>> findOpcional(@PathVariable Long id) {

        var encontrados = service.findById(id);
        Set<Opcional> opcionais = encontrados.getOpcionais();


        var resposta = opcionais.stream().map(opcionalService::toResponse).toList();

        return ResponseEntity.ok(resposta);
    }


}
