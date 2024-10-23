package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

import java.time.Instant;

public record UpdateUserInput(
  String username,
  String password,
  String roles,
  boolean enabled,
  Instant createdAt
) {}
