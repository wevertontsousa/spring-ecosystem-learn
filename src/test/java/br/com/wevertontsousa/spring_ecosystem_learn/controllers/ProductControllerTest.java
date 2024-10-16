package br.com.wevertontsousa.spring_ecosystem_learn.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.PatchProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.services.ProductService;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiStatusCode;

@WebMvcTest(ProductController.class) // Carrega apenas o contexto Spring necessário para o Controller
public class ProductControllerTest {

  @MockBean // Quando se testa uma classe que usará o contexto Spring
  private ProductService produtoService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName(value = "Dado que os data são válidos, deve retornar o Produto criado")
  @Test
  public void shouldReturnTheCreatedProduct() throws Exception {
    // Cenário
    var input = new ProductDto(
      "x-salada",
      "Alface, tomate, queijo, hambúrguer",
      new BigDecimal("19.99"),
      "uploads/images/produtos"
    );

    var id = UUID.randomUUID();

    var output = new ProductDto(
      input.name(),
      input.description(),
      input.price(),
      input.imageUrl(),
      id,
      Instant.now()
    );

    when(this.produtoService.save(input)).thenReturn(output);

    // Ação
    ResultActions resultado = this.mockMvc.perform( // Simula uma requisição HTTP
      post("/api/v1/produtos")
        .accept(MediaType.APPLICATION_JSON) // Diz ao servidor o que espera de volta
        .content(this.objectMapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON) // Diz ao servidor o que está enviando
        .characterEncoding("UTF-8") // Diz ao servidor a codificação que está enviando
    );

    // Verificação
    resultado
      .andExpect(status().isCreated())
      .andExpect(header().string("location", Matchers.containsString("http://api/v1/produtos/" + id)))
      .andExpect(jsonPath("$.code").value(ApiStatusCode.CREATED.getCode()))
      .andExpect(jsonPath("$.title").value(ApiResponseMessage.CREATED.getMessage()))
      .andExpect(jsonPath("$.data").exists())
      .andExpect(jsonPath("$.data.id").value(output.id().get().toString()))
      .andExpect(jsonPath("$.data.name").value(output.name()))
      .andExpect(jsonPath("$.data.description").value(output.description()))
      .andExpect(jsonPath("$.data.price").value(output.price().toPlainString()))
      .andExpect(jsonPath("$.data.createdAt").value(output.createdAt().get().toString()))
      .andExpect(jsonPath("$.data.updatedAt").doesNotExist());

    // Verificação dos mocks
    verify(produtoService, times(1)).save(input);
    verifyNoMoreInteractions(produtoService);
  }

  @DisplayName(value = "Deve retornar uma lista com 2 Produtos")
  @Test
  public void shouldReturnAPopulatedList() throws Exception {
    // Cenário
    var produtoDto1 = new ProductDto(
      "teste 1",
      "descrição teste",
      new BigDecimal("19.99"),
      "/imagens/teste-1.png",
      UUID.randomUUID(),
      Instant.now()
    );

    var produtoDto2 = new ProductDto(
      "teste 2",
      "descrição teste",
      new BigDecimal("30.00"),
      "/imagens/teste-2.png",
      UUID.randomUUID(),
      Instant.now()
    );

    var output = List.of(produtoDto1, produtoDto2);

    when(this.produtoService.findAll()).thenReturn(output);

    // Ação + Verificação
    this.mockMvc.perform(
      get("/api/v1/produtos").accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(header().string("location", Matchers.containsString("http://api/v1/produtos")))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.OK.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.OK.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data").isArray())
    .andExpect(jsonPath("$.data", hasSize(2)));

  // Verificação dos mocks
    verify(produtoService, times(1)).findAll();
    verifyNoMoreInteractions(produtoService);
  }

  @DisplayName(value = "Dado que o ID é válido e existe, deve retornar um Produto")
  @Test
  public void shouldReturnAProduct() throws Exception {
    // Cenário
    var id = UUID.randomUUID();

    var output = new ProductDto(
      "teste 1",
      "descrição teste",
      new BigDecimal("19.99"),
      "/imagens/teste-1.png",
      id,
      Instant.now()
    );

    when(this.produtoService.findById(id)).thenReturn(output);

    // Ação + Verificação
    this.mockMvc.perform(
      get("/api/v1/produtos/" + id)
        .accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(header().string("location", Matchers.containsString("http://api/v1/produtos/" + id)))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.OK.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.OK.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data.id").value(output.id().get().toString()))
    .andExpect(jsonPath("$.data.name").value(output.name()))
    .andExpect(jsonPath("$.data.description").value(output.description()))
    .andExpect(jsonPath("$.data.price").value(output.price().toPlainString()))
    .andExpect(jsonPath("$.data.createdAt").value(output.createdAt().get().toString()))
    .andExpect(jsonPath("$.data.updatedAt").doesNotExist());

  // Verificação dos mocks
    verify(produtoService, times(1)).findById(id);
    verifyNoMoreInteractions(produtoService);
  }

  @DisplayName(value = "Dado que os data são válidos, deve retornar o Produto atualizado")
  @Test
  public void shouldReturnTheUpdatedProduct() throws Exception {
    // Cenário
    var id = UUID.randomUUID();
    var createdAt = Instant.now();
    var updatedAt = Instant.now();

    var input = new ProductDto(
      "x-salada",
      "Alface, tomate, queijo, hambúrguer",
      new BigDecimal("19.99"),
      "uploads/images/produtos",
      id,
      createdAt
    );

    var output = new ProductDto(
      input.name(),
      input.description(),
      input.price(),
      input.imageUrl(),
      id,
      createdAt,
      updatedAt
    );

    when(this.produtoService.update(id, input)).thenReturn(output);

    // Ação + Verificação
    this.mockMvc.perform(
      put("/api/v1/produtos/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(header().string("location", Matchers.containsString("http://api/v1/produtos/" + id)))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.OK.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.UPDATED.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data.id").value(output.id().get().toString()))
    .andExpect(jsonPath("$.data.name").value(output.name()))
    .andExpect(jsonPath("$.data.description").value(output.description()))
    .andExpect(jsonPath("$.data.price").value(output.price().toPlainString()))
    .andExpect(jsonPath("$.data.createdAt").value(output.createdAt().get().toString()))
    .andExpect(jsonPath("$.data.updatedAt").value(output.updatedAt().get().toString()));

  // Verificação dos mocks
    verify(produtoService, times(1)).update(id, input);
    verifyNoMoreInteractions(produtoService);
  }

  @DisplayName(value = "Dado que o novo nome é válido, deve retornar o Produto corrigido")
  @Test
  public void shouldReturnTheCorrectedProduct() throws Exception {
    // Cenário
    var input = new PatchProductDto(
      Optional.of("x-tudo"),
      Optional.empty(),
      Optional.empty(),
      Optional.empty()
    );

    var id = UUID.randomUUID();

    var output = new ProductDto(
      input.name().get(),
      "Descrição Teste",
      new BigDecimal("10.0"),
      "uploads/images/produtos",
      id,
      Instant.now(),
      Instant.now()
    );

    when(this.produtoService.patch(id, input)).thenReturn(output);

    // Ação + Verificação
    this.mockMvc.perform(
      patch("/api/v1/produtos/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(header().string("location", Matchers.containsString("http://api/v1/produtos/" + id)))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.OK.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.CORRECTED.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data.id").value(output.id().get().toString()))
    .andExpect(jsonPath("$.data.name").value(output.name()))
    .andExpect(jsonPath("$.data.description").value(output.description()))
    .andExpect(jsonPath("$.data.price").value(output.price().toPlainString()))
    .andExpect(jsonPath("$.data.createdAt").value(output.createdAt().get().toString()))
    .andExpect(jsonPath("$.data.updatedAt").value(output.updatedAt().get().toString()));

  // Verificação dos mocks
    verify(produtoService, times(1)).patch(id, input);
    verifyNoMoreInteractions(produtoService);
  }

  @DisplayName(value = "Dado que o ID é válido e existe, deve deletar um Produto")
  @Test
  public void shouldDeletAProduct() throws Exception {
    // Cenrário
    var id = UUID.randomUUID();
    doNothing().when(this.produtoService).deleteById(id);

    // Ação + Verificação
    this.mockMvc.perform(
      delete("/api/v1/produtos/" + id).accept(MediaType.APPLICATION_JSON)
    )
    .andExpect(status().isOk())
    .andExpect(header().doesNotExist("location"))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.OK.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.DELETED.getMessage()))
    .andExpect(jsonPath("$.data").doesNotExist());

  // Verificação dos mocks
    verify(produtoService, times(1)).deleteById(id);
    verifyNoMoreInteractions(produtoService);
  }

}
