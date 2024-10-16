package br.com.wevertontsousa.spring_ecosystem_learn.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.models.ProductModel;
import br.com.wevertontsousa.spring_ecosystem_learn.persistences.JpaProductRepository;

@ExtendWith(value = MockitoExtension.class) // para funcionar `@InjectMocks` e `@Mock`
public class ProductServiceTest {

  @InjectMocks // a classe a ser testada. Criará uma instância real dessa classe
  private ProductService produtoService;

  @Mock // dependência da classe a ser testada. Criará um objeto SIMULADO. Por padrão retorna `null`, precisa do `when`
  private JpaProductRepository jpaProductRepository;

  @DisplayName(value = "Deve retornar uma lista com dois produtos")
  @Test
  public void deveRetornarUmaListaComDoisProducts() {
    // Cenário
    var produtoModel1 = ProductModel
      .builder()
      .id(UUID.randomUUID())
      .name("teste 1")
      .description("descrição teste")
      .price(new BigDecimal("19.99"))
      .imageUrl("/imagens/teste-1.png")
      .createdAt(Instant.now())
      .build();

    var produtoModel2 = ProductModel
      .builder()
      .id(UUID.randomUUID())
      .name("teste 2")
      .description("descrição teste")
      .price(new BigDecimal("30.00"))
      .imageUrl("/imagens/teste-2.png")
      .createdAt(Instant.now())
      .build();

    var produtoModelList = List.of(produtoModel1, produtoModel2);

    when(this.jpaProductRepository.findAll()).thenReturn(produtoModelList);

    // Ação
    var saida = this.produtoService.findAll();

    // Verificação
    assertNotNull(saida);
    assertEquals(2, saida.size());
    verify(this.jpaProductRepository, times(1)).findAll();
    verifyNoMoreInteractions(this.jpaProductRepository);

  }

  @DisplayName(value = "Deve salvar um Product e retorná-lo")
  @Test
  public void deveSalvarUmProductERetornaLo() {
    // Cenário
    var entrada = new ProductDto(
      "x-salada",
      "Alface, tomate, queijo, hambúrguer",
      new BigDecimal("19.99"),
      "uploads/images/products"
    );

    var produtoModel = ProductModel
      .builder()
      .id(UUID.randomUUID())
      .name(entrada.name())
      .description(entrada.description())
      .price(entrada.price())
      .imageUrl(entrada.imageUrl())
      .createdAt(Instant.now())
      .build();

    when(this.jpaProductRepository.save(any(ProductModel.class))).thenReturn(produtoModel);

    // Ação
    var saida = this.produtoService.save(entrada);

    // Verificação
    assertNotNull(saida);
    assertEquals(entrada.name(), saida.name());

    verify(this.jpaProductRepository, times(1)).save(any(ProductModel.class));
    verifyNoMoreInteractions(this.jpaProductRepository);
  }

}
