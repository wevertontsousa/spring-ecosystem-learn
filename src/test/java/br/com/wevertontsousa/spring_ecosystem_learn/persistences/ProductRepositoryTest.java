package br.com.wevertontsousa.spring_ecosystem_learn.persistences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.javafaker.Faker;

import br.com.wevertontsousa.spring_ecosystem_learn.models.ProductModel;

@DataJpaTest // faz com que essa classe seja executa apenas nas camadas de persistência. Evitando a incializaçaõ completa da aplicação.
@ActiveProfiles("test") // usa o arquivo que tiver o sufixo -test; applications-test; para configuração
public class ProductRepositoryTest {

  @Autowired // só funcionado com o Spring rodando. Mas funcionará devido ao `@DataJpaTest`
  private JpaProductRepository jpaProductRepository;

  private Faker faker = new Faker();

  @DisplayName("Dado que o intervalo de Preço é válido e existe, deve retornar uma lista de Produtos")
  @Test
  void shouldReturnAPopulatedList() {
    // Cenário
    var productModel1 = this.instantiateProductModel(1.0, 7.0);
    var productModel2 = this.instantiateProductModel(10.0, 30.0);
    var productModel3 = this.instantiateProductModel(10.0, 30.0);

    // Ação
    this.jpaProductRepository.save(productModel1);
    this.jpaProductRepository.save(productModel2);
    this.jpaProductRepository.save(productModel3);

    var productsModel = this.jpaProductRepository.findPriceBetween(
      new BigDecimal("10.0"),
      new BigDecimal("50.0")
    );

    // Verificação
    assertNotNull(productsModel);
    assertEquals(productsModel.size(), 2);
  }

  @DisplayName("Dado que o intervalo de Preço é válido mas não existe, deve retornar uma lista vazia")
  @Test
  void shouldReturnAEmptyList() {
    // Cenário
    var productModel1 = this.instantiateProductModel(1.0, 7.0);
    var productModel2 = this.instantiateProductModel(10.0, 30.0);
    var productModel3 = this.instantiateProductModel(10.0, 30.0);

    // Ação
    this.jpaProductRepository.save(productModel1);
    this.jpaProductRepository.save(productModel2);
    this.jpaProductRepository.save(productModel3);

    var productsModel = this.jpaProductRepository.findPriceBetween(
      new BigDecimal("40.0"),
      new BigDecimal("100.0")
    );

    // Verificação
    assertTrue(productsModel.isEmpty());
  }

  private ProductModel instantiateProductModel(double lessPrice, double biggerPrice) {
    return ProductModel.builder()
      .id(UUID.randomUUID())
      .name(this.faker.commerce().productName())
      .description(this.faker.lorem().characters(1, 255))
      .price(new BigDecimal(this.faker.commerce().price(lessPrice, biggerPrice)))
      .imageUrl(this.faker.internet().url())
      .createdAt(Instant.now())
      .build();
  }

}
