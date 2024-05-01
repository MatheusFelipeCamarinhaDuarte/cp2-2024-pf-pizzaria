package br.com.fiap.pizzaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_PIZZARIA")
public class Pizzaria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PIZZARIA")
    @SequenceGenerator(name = "SQ_PIZZARIA", sequenceName = "SQ_PIZZARIA", allocationSize = 1)
    @Column(name = "ID_PIZZARIA")
    private Long id;

    @Column(name = "NM_PIZZARIA")
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "TB_CARDAPIO",
            joinColumns = {
                    @JoinColumn(
                        name = "PIZZARIA",
                        referencedColumnName = "ID_PIZZARIA",
                        foreignKey = @ForeignKey(
                        name = "FK_PIZZARIA_DO_PRODUTO"
                    )
                )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "PRODUTO",
                            referencedColumnName = "ID_PRODUTO",
                            foreignKey = @ForeignKey(
                                name = "FK_PRODUTO_DA_PIZZARIA"
                            )
                    )
            }

    )
    private Set<Produto> cardapio = new LinkedHashSet<>();

}
