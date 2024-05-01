package br.com.fiap.pizzaria.domain.service;

import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.OpcionalResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.entity.Opcional;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.entity.Sabor;
import br.com.fiap.pizzaria.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProdutoService implements ServiceDTO<Produto, ProdutoRequest, ProdutoResponse>{
    @Autowired
    private ProdutoRepository repo;

    @Autowired
    private SaborService saborService;
    @Autowired
    private OpcionalService opcionalService;

    @Override
    public Collection<Produto> findAll() {
        return repo.findAll();
    }

    @Override
    public Collection<Produto> findAll(Example<Produto> example) {
        return repo.findAll(example);
    }

    @Override
    public Produto findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Produto save(Produto e) {
        return repo.save(e);
    }

    @Override
    public Produto toEntity(ProdutoRequest dto) {
        Sabor sabor = null;
        if (Objects.nonNull(dto.sabor())){
            sabor = saborService.findById(dto.sabor().id());
        }

        Set<Opcional> opcionais = new HashSet<>();

        dto.opcionais().forEach( opcao ->{
            if (Objects.nonNull(opcao)){
                var op = opcionalService.findById(opcao.id());
                opcionais.add(op);
            }
        });

        return Produto.builder()
                .nome(dto.nome())
                .preco(dto.preco())
                .sabor(sabor)
                .opcionais(opcionais)
                .build();
    }

    @Override
    public ProdutoResponse toResponse(Produto e) {
        var sabor = saborService.toResponse( e.getSabor());

        Set<OpcionalResponse> opcionais = new LinkedHashSet<>();

            e.getOpcionais().forEach( opcao ->{
                if (Objects.nonNull(opcao)){
                    var op = opcionalService.toResponse(opcionalService.findById(opcao.getId()));
                    opcionais.add(op);
                }

            });

        return ProdutoResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .preco(e.getPreco())
                .sabor(sabor)
                .opcionais(opcionais)
                .build();
    }
}
