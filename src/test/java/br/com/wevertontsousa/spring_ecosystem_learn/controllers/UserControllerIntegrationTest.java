package br.com.wevertontsousa.spring_ecosystem_learn.controllers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.SaveUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiStatusCode;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName(value = "Dado que os valores são válidos, deve retornar o Usuário criado")
  @Test
  public void shouldReturnTheCreatedProduct() throws Exception {
    // Cenário
    var input = new SaveUserInput("weverton", "12345", "admin employee user", true);

    var uuidPattern = "^[0-9a-fA-F-]{36}$";
    var createdAtOrUpdatedAtPattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?Z$";

    // Ação + Verificação
    this.mockMvc.perform(
      post("/api/v1/usuarios")
        .accept(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(input))
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("UTF-8")
    )
    .andExpect(status().isCreated())
    .andExpect(header().string("Location", Matchers.containsString("/api/v1/usuarios")))
    .andExpect(jsonPath("$.code").value(ApiStatusCode.CREATED.getCode()))
    .andExpect(jsonPath("$.title").value(ApiResponseMessage.CREATED.getMessage()))
    .andExpect(jsonPath("$.data").exists())
    .andExpect(jsonPath("$.data.password").doesNotExist())
    .andExpect(jsonPath("$.data.id").value(matchesPattern(uuidPattern)))
    .andExpect(jsonPath("$.data.username").value(input.username()))
    .andExpect(jsonPath("$.data.roles").value(input.roles()))
    .andExpect(jsonPath("$.data.enabled").value(input.enabled()))
    .andExpect(jsonPath("$.data.createdAt").value(matchesPattern(createdAtOrUpdatedAtPattern)))
    .andExpect(jsonPath("$.data.updatedAt").doesNotExist());
  }

}
