package br.com.wevertontsousa.spring_ecosystem_learn.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ApiResponse;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.PatchProductDto;
import br.com.wevertontsousa.spring_ecosystem_learn.services.ProductService;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiStatusCode;

@RestController
@RequestMapping(path = "/api/v1/produtos")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductDto>> save(@RequestBody ProductDto request) {
    var saida = this.productService.save(request);

    var resposta = new ApiResponse<ProductDto>(
      ApiStatusCode.CREATED.getCode(),
      ApiResponseMessage.CREATED.getMessage(),
      saida
    );

    return ResponseEntity
      .status(ApiStatusCode.CREATED.getCode())
      .header("location", "http://api/v1/produtos/" + saida.id().get())
      .body(resposta);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductDto>>> findAll() {
    var saida = this.productService.findAll();

    var resposta = new ApiResponse<List<ProductDto>>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.OK.getMessage(),
      saida
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .header("location", "http://api/v1/produtos")
      .body(resposta);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<ProductDto>> findById(@PathVariable UUID id) {
    var saida = this.productService.findById(id);

    var resposta = new ApiResponse<ProductDto>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.OK.getMessage(),
      saida
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .header("location", "http://api/v1/produtos/" + id)
      .body(resposta);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<ProductDto>> update(@PathVariable UUID id, @RequestBody ProductDto request) {
    var saida = this.productService.update(id, request);

    var resposta = new ApiResponse<ProductDto>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.UPDATED.getMessage(),
      saida
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .header("location", "http://api/v1/produtos/" + id)
      .body(resposta);
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<ProductDto>> patch(@PathVariable UUID id, @RequestBody PatchProductDto request) {
    var saida = this.productService.patch(id, request);
    var resposta = new ApiResponse<ProductDto>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.CORRECTED.getMessage(),
      saida
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .header("location", "http://api/v1/produtos/" + id)
      .body(resposta);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteById(@PathVariable UUID id) {
    this.productService.deleteById(id);

    var resposta = new ApiResponse<Object>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.DELETED.getMessage(),
      null
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .body(resposta);
  }

}
