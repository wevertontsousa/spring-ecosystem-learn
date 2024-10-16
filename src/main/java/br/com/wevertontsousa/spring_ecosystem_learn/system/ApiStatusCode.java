package br.com.wevertontsousa.spring_ecosystem_learn.system;

public enum ApiStatusCode {

  OK(200),
  CREATED(201),
  NO_CONTENT(204),
  INVALID_ARGUMENT(400), // Bad request
  UNAUTHORIZED(401), // Usuário ou senha incorretos
  FORBIDDEN(403), // Permissão negada
  NOT_FOUND(404),
  INTERNAL_SERVER_ERROR(500);

  private final int code;

  ApiStatusCode(int code) { this.code = code; }

  public int getCode() { return this.code; }

}
