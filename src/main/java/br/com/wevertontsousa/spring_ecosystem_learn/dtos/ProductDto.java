package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record ProductDto(
  String name,
  String description,
  BigDecimal price,
  String imageUrl,
  Optional<UUID> id,
  Optional<Instant> createdAt,
  Optional<Instant> updatedAt
) {

  public ProductDto(String name, String description, BigDecimal price, String imageUrl) {
    this(name, description, price, imageUrl, Optional.empty(), Optional.empty(), Optional.empty());
  };

  public ProductDto(String name, String description, BigDecimal price, String imageUrl, UUID id, Instant createdAt) {
    this(name, description, price, imageUrl, Optional.of(id), Optional.of(createdAt), Optional.empty());
  };

  public ProductDto(String name, String description, BigDecimal price, String imageUrl, UUID id, Instant createdAt, Instant updatedAt) {
    this(name, description, price, imageUrl, Optional.of(id), Optional.of(createdAt), Optional.ofNullable(updatedAt));
  };

}
