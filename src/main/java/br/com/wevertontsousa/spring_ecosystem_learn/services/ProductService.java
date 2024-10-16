package br.com.wevertontsousa.spring_ecosystem_learn.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.PatchProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceNotFoundException;
import br.com.wevertontsousa.spring_ecosystem_learn.models.ProductModel;
import br.com.wevertontsousa.spring_ecosystem_learn.persistences.JpaProductRepository;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;

@Service
@Transactional
public class ProductService {

  private final JpaProductRepository jpaProductRepository;

  public ProductService(JpaProductRepository jpaProductRepository) {
    this.jpaProductRepository = jpaProductRepository;
  }

  public ProductDto save(ProductDto input) {
    var id = UUID.randomUUID();
    var createdAt = Instant.now();

    var productModel = ProductModel
      .builder()
      .id(id)
      .name(input.name())
      .description(input.description())
      .price(input.price())
      .imageUrl(input.imageUrl())
      .createdAt(createdAt)
      .build();

    this.jpaProductRepository.save(productModel);

    var output = new ProductDto(
      productModel.getName(),
      productModel.getDescription(),
      productModel.getPrice(),
      productModel.getImageUrl(),
      id,
      createdAt,
      productModel.getUpdatedAt()
    );

    return output;
  }

  public List<ProductDto> findAll() {
    return this.jpaProductRepository
      .findAll()
      .stream()
      .map((productModel) -> new ProductDto(
        productModel.getName(),
        productModel.getDescription(),
        productModel.getPrice(),
        productModel.getImageUrl(),
        productModel.getId(),
        productModel.getCreatedAt(),
        productModel.getUpdatedAt()
      ))
      .toList();
  }

  public ProductDto findById(UUID id) {
    return this.jpaProductRepository
      .findById(id)
      .map((productModel) -> new ProductDto(
        productModel.getName(),
        productModel.getDescription(),
        productModel.getPrice(),
        productModel.getImageUrl(),
        productModel.getId(),
        productModel.getCreatedAt(),
        productModel.getUpdatedAt()
      ))
      .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessage.NOT_FOUND.getMessage()));
  }

  public ProductDto update(UUID id, ProductDto input) {
    var updatedAt = Instant.now();

    var productModel = ProductModel
      .builder()
      .id(id)
      .name(input.name())
      .description(input.description())
      .price(input.price())
      .imageUrl(input.imageUrl())
      .createdAt(input.createdAt().get())
      .updatedAt(updatedAt)
      .build();

    this.jpaProductRepository.save(productModel);

    var output = new ProductDto(
      productModel.getName(),
      productModel.getDescription(),
      productModel.getPrice(),
      productModel.getImageUrl(),
      id,
      productModel.getCreatedAt(),
      productModel.getUpdatedAt()
    );

    return output;
  }

  public ProductDto patch(UUID id, PatchProductDto input) {
    var updatedAt = Instant.now();

    var productModel = this.jpaProductRepository
      .findById(id)
      .map((pM) -> ProductModel.builder()
        .id(pM.getId())
        .name(input.name().orElse(pM.getName()))
        .description(input.description().orElse(pM.getDescription()))
        .price(input.price().orElse(pM.getPrice()))
        .imageUrl(input.imageUrl().orElse(pM.getImageUrl()))
        .createdAt(pM.getCreatedAt())
        .updatedAt(updatedAt)
        .build()
      )
      .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessage.NOT_FOUND.getMessage()));

    this.jpaProductRepository.save(productModel);

    var output = new ProductDto(
      productModel.getName(),
      productModel.getDescription(),
      productModel.getPrice(),
      productModel.getImageUrl(),
      id,
      productModel.getCreatedAt(),
      productModel.getUpdatedAt()
    );

    return output;
  }

  public void deleteById(UUID id) {
    this.jpaProductRepository.deleteById(id);
  }

}
