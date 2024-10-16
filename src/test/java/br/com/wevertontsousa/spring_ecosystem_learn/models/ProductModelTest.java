package br.com.wevertontsousa.spring_ecosystem_learn.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceInvalidException;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;

public class ProductModelTest {

  @DisplayName(value = "Dado que os dados são válidos, deve instanciar um ProductModel")
  @Test
  public void shouldInstantiateAProductModel() {
    // Cenário
    var id = UUID.randomUUID();
    var name = "x-salada";
    var description = "Alface, tomate, queijo, hambúrguer";
    var price = new BigDecimal("19.99");
    var imageUrl = "uploads/imagens/produtos";

    // Ação
    var produtoModel = ProductModel
      .builder()
      .id(id)
      .name(name)
      .description(description)
      .price(price)
      .imageUrl(imageUrl)
      .createdAt(Instant.now())
      .build();

    // Verificação
    assertNotNull(produtoModel);
    assertEquals(id, produtoModel.getId());
    assertEquals(name, produtoModel.getName());
    assertEquals(description, produtoModel.getDescription());
    assertEquals(price, produtoModel.getPrice());
    assertEquals(imageUrl, produtoModel.getImageUrl());
  }

  @DisplayName(value = "Dado que o ID é nulo, deve lançar a excecao ResourceInvalidException")
  @Test
  public void shouldThrowExceptionForNullId() {
    // Cenário
    var produtoModel = ProductModel
      .builder()
      .id(null)
      .name("x-salada")
      .description("Alface, tomate, queijo, hambúrguer")
      .price(new BigDecimal("19.99"))
      .imageUrl("uploads/imagens/produtos")
      .createdAt(Instant.now());

    // Ação
    var excecao = assertThrows(
      ResourceInvalidException.class, // exceção esperada
      () -> produtoModel.build() // método ou ação que lançara a exceção
    );

    // Verificação
    assertEquals(excecao.getMessage(), ApiResponseMessage.INVALID_ARGUMENT.getMessage());
  }

  @DisplayName(value = "Dado que o Nome é nulo, deve lançar a excecao ResourceInvalidException")
  @Test
  public void shouldThrowExceptionForNullName() {
    // Cenário
    var produtoModel = ProductModel
      .builder()
      .id(UUID.randomUUID())
      .description("Alface, tomate, queijo, hambúrguer")
      .price(new BigDecimal("19.99"))
      .imageUrl("uploads/imagens/produtos")
      .createdAt(Instant.now());

    // Ação
    var excecao = assertThrows(ResourceInvalidException.class, () -> produtoModel.build());

    // Verificação
    assertEquals(excecao.getMessage(), ApiResponseMessage.INVALID_ARGUMENT.getMessage());
  }

  @DisplayName(value = "Dado que o Nome é uma String vazia, deve lançar a excecao ResourceInvalidException")
  @Test
  public void shouldThrowExceptionForEmptyName() {
    // Cenário
    var produtoModel = ProductModel
      .builder()
      .id(UUID.randomUUID())
      .name(" ")
      .description("Alface, tomate, queijo, hambúrguer")
      .price(new BigDecimal("19.99"))
      .imageUrl("uploads/imagens/produtos")
      .createdAt(Instant.now());

    // Ação
    var excecao = assertThrows(ResourceInvalidException.class, () -> produtoModel.build());

    // Verificação
    assertEquals(excecao.getMessage(), ApiResponseMessage.INVALID_ARGUMENT.getMessage());
  }

  // TODO: criar testes para os atributos: description; price; imageUrl

}
