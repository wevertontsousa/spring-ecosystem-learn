package br.com.wevertontsousa.spring_ecosystem_learn.persistences;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.wevertontsousa.spring_ecosystem_learn.models.ProductModel;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductModel, UUID> {

  @Query(
    "SELECT p"
    + " FROM ProductModel AS p"
    + " WHERE p.price BETWEEN :lessPrice AND :biggerPrice"
  )
  List<ProductModel> findPriceBetween(
    @Param("lessPrice") BigDecimal lessPrice,
    @Param("biggerPrice") BigDecimal biggerPrice
  );

}
