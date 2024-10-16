package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

import java.math.BigDecimal;
import java.util.Optional;

public record PatchProductDto(
  Optional<String> name,
  Optional<String> description,
  Optional<BigDecimal> price,
  Optional<String> imageUrl
) {}
