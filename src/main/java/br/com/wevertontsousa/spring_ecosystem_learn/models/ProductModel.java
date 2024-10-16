package br.com.wevertontsousa.spring_ecosystem_learn.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceInvalidException;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder(builderClassName = "ProductBuilder")
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "products")
public class ProductModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Setter(value = AccessLevel.NONE)
  private UUID id;

  @Column(name = "name", length = 30, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @Column(name = "price", precision = 5, scale = 2, nullable = false)
  private BigDecimal price;
  
  @Column(name = "image_url", length = 255, nullable = false, unique = true)
  private String imageUrl;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = true)
  private Instant updatedAt;

  public static class ProductBuilder {
    public ProductModel build() {
      if (
        this.id == null
        || this.name == null
        || this.name.isBlank()
        || this.description == null
        || this.description.isBlank()
        || this.imageUrl == null
        || this.imageUrl.isBlank()
      ) {
        throw new ResourceInvalidException(ApiResponseMessage.INVALID_ARGUMENT.getMessage());
      }

      // TODO: criar regras de negócio mais fortes para os atributos

      return new ProductModel(id, name, description, price, imageUrl, createdAt, updatedAt);
    }
  }

}
