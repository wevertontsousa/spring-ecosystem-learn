package br.com.wevertontsousa.spring_ecosystem_learn.errors;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) { super(message); }

}
