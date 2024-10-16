package br.com.wevertontsousa.spring_ecosystem_learn.controllers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiStatusCode;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class ProductControllerIntegrationTest {

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

    var uuidPattern = "^[0-9a-fA-F-]{36}$";
    var createdAtOrUpdatedAtPattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?Z$";

    // Ação + Verificação
    this.mockMvc.perform(
      post("/api/v1/produtos")
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
    )
    .andExpect(status().isCreated())
    .andExpect(header().string("location", Matchers.containsString("/api/v1/produtos")))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.CREATED.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.CREATED.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data.id").value(matchesPattern(uuidPattern)))
    .andExpect(jsonPath("$.data.name").value(input.name()))
    .andExpect(jsonPath("$.data.description").value(input.description()))
    .andExpect(jsonPath("$.data.price").value(input.price().toPlainString()))
    .andExpect(jsonPath("$.data.createdAt").value(matchesPattern(createdAtOrUpdatedAtPattern)))
    .andExpect(jsonPath("$.data.updatedAt").doesNotExist());
  }

}
