package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

import java.util.Optional;

public record PatchUserInput(
  Optional<String> username,
  Optional<String> password,
  Optional<String> roles,
  Optional<Boolean> enabled
) {}
