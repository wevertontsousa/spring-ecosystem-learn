package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

public record SaveUserInput(String username, String password, String roles, boolean enabled) {}
