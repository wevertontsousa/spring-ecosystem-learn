package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

import java.time.Instant;
import java.util.UUID;

public record FindUserOutput(
  UUID id,
  String username,
  String roles,
  boolean enabled,
  Instant createdAt,
  Instant updatedAt
) {}
