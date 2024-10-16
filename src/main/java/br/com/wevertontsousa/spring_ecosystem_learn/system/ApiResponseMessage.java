package br.com.wevertontsousa.spring_ecosystem_learn.system;

public enum ApiResponseMessage {
  CREATED("Recurso criado com sucesso"),
  CORRECTED("Recurso parcialmente atualizado com sucesso"),
  DELETED("Recurso deletado com sucesso"),
  UPDATED("Recurso atualizado com sucesso"),
  OK("Recurso encontrado com sucesso"),
  NO_CHANGES("Recurso não modificado"),
  NOT_FOUND("Recurso não encontrado"),
  INVALID_ARGUMENT("Dados incorretos"),
  UNAUTHORIZED("Usuário ou senha incorreto");

  private final String message;

  ApiResponseMessage(String message) { this.message = message; }

  public String getMessage() { return this.message; }

}
