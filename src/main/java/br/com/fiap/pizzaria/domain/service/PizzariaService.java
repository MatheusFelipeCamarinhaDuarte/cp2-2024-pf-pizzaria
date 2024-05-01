package br.com.fiap.pizzaria.domain.service;

import br.com.fiap.pizzaria.domain.dto.request.PizzariaRequest;
import br.com.fiap.pizzaria.domain.dto.response.PizzariaResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.entity.Pizzaria;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.repository.PizzariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class PizzariaService implements ServiceDTO<Pizzaria, PizzariaRequest, PizzariaResponse>{
    @Autowired
    private PizzariaRepository repo;
    @Autowired
    private ProdutoService produtoService;

    @Override
    public Collection<Pizzaria> findAll() {
        return repo.findAll();
    }

    @Override
    public Collection<Pizzaria> findAll(Example<Pizzaria> example) {
        return repo.findAll(example);
    }

    @Override
    public Pizzaria findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Pizzaria save(Pizzaria e) {
        return repo.save(e);
    }

    @Override
    public Pizzaria toEntity(PizzariaRequest dto) {

        Set<Produto> cardapio  = new LinkedHashSet<>();

        dto.cardapio().forEach( produto ->{
            if (Objects.nonNull(produto)){
                var pd = produtoService.findById(produto.id());
                cardapio.add(pd);
            }
        });

        return Pizzaria.builder()
                .nome(dto.nome())
                .cardapio(cardapio)
                .build();
    }

    @Override
    public PizzariaResponse toResponse(Pizzaria e) {
        Set<ProdutoResponse> cardapio = new LinkedHashSet<>();
        e.getCardapio().forEach(produto -> {
            if (Objects.nonNull(produto)){
                var pd = produtoService.toResponse(produtoService.findById(produto.getId()));
                cardapio.add(pd);
            }
        });

        return PizzariaResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .cardapio(cardapio)
                .build();
    }
}
