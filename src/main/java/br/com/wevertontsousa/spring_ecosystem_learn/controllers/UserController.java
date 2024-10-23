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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.ApiResponse;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.FindUserOutput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.PatchUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.SaveUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.UpdateUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.services.UserService;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiStatusCode;

@RestController
@RequestMapping(path = "/api/v1/usuarios")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<FindUserOutput>> save(@RequestBody SaveUserInput request) {
    var output = this.userService.save(request);

    var resposta = new ApiResponse<FindUserOutput>(
      ApiStatusCode.CREATED.getCode(),
      ApiResponseMessage.CREATED.getMessage(),
      output
    );

    var uri = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(output.id())
      .toUri();

    return ResponseEntity
      .status(ApiStatusCode.CREATED.getCode())
      .header("Location", uri.toString())
      .body(resposta);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<FindUserOutput>>> findAll() {
    var output = this.userService.findAll();

    var resposta = new ApiResponse<List<FindUserOutput>>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.OK.getMessage(),
      output
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .body(resposta);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<FindUserOutput>> findById(@PathVariable UUID id) {
    var output = this.userService.findById(id);

    var resposta = new ApiResponse<FindUserOutput>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.OK.getMessage(),
      output
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .body(resposta);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<FindUserOutput>> update(@PathVariable UUID id, @RequestBody UpdateUserInput request) {
    var output = this.userService.update(id, request);

    var resposta = new ApiResponse<FindUserOutput>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.UPDATED.getMessage(),
      output
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .body(resposta);
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<FindUserOutput>> patch(@PathVariable UUID id, @RequestBody PatchUserInput request) {
    var output = this.userService.patch(id, request);
    var resposta = new ApiResponse<FindUserOutput>(
      ApiStatusCode.OK.getCode(),
      ApiResponseMessage.CORRECTED.getMessage(),
      output
    );

    return ResponseEntity
      .status(ApiStatusCode.OK.getCode())
      .body(resposta);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteById(@PathVariable UUID id) {
    this.userService.deleteById(id);

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
