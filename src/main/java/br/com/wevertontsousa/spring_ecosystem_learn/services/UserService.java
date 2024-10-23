package br.com.wevertontsousa.spring_ecosystem_learn.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.wevertontsousa.spring_ecosystem_learn.dtos.FindUserOutput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.PatchUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.SaveUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.dtos.UpdateUserInput;
import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceInvalidException;
import br.com.wevertontsousa.spring_ecosystem_learn.errors.ResourceNotFoundException;
import br.com.wevertontsousa.spring_ecosystem_learn.models.UserModel;
import br.com.wevertontsousa.spring_ecosystem_learn.persistences.JpaUserRepository;
import br.com.wevertontsousa.spring_ecosystem_learn.system.ApiResponseMessage;

@Service
public class UserService {

  private final JpaUserRepository jpaUserRepository;

  public UserService(JpaUserRepository jpaUserRepository) {
    this.jpaUserRepository = jpaUserRepository;
  }

  public FindUserOutput save(SaveUserInput input) {
    var id = UUID.randomUUID();
    var createdAt = Instant.now();

    var userModel = new UserModel(
      id,
      input.username(),
      input.password(),
      input.roles(),
      input.enabled(),
      createdAt
    );

    try {
      this.jpaUserRepository.save(userModel);

    } catch (DataIntegrityViolationException e) {
      throw new ResourceInvalidException("Usuário já existe");
    }

    var output = new FindUserOutput(
      id,
      userModel.getUsername(),
      userModel.getRoles(),
      userModel.isEnabled(),
      createdAt,
      userModel.getUpdatedAt()
    );

    return output;
  }

  public List<FindUserOutput> findAll() {
    return this.jpaUserRepository
      .findAll()
      .stream()
      .map((userModel) -> new FindUserOutput(
        userModel.getId(),
        userModel.getUsername(),
        userModel.getRoles(),
        userModel.isEnabled(),
        userModel.getCreatedAt(),
        userModel.getUpdatedAt()
      ))
      .toList();
  }

  public FindUserOutput findById(UUID id) {
    return this.jpaUserRepository
      .findById(id)
      .map((userModel) -> new FindUserOutput(
        userModel.getId(),
        userModel.getUsername(),
        userModel.getRoles(),
        userModel.isEnabled(),
        userModel.getCreatedAt(),
        userModel.getUpdatedAt()
      ))
      .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessage.NOT_FOUND.getMessage()));
  }

  public FindUserOutput update(UUID id, UpdateUserInput input) {
    var updatedAt = Instant.now();

    var userModel = new UserModel(
      id,
      input.username(),
      input.password(),
      input.roles(),
      input.enabled(),
      input.createdAt(),
      updatedAt
    );

    this.jpaUserRepository.save(userModel);

    try {
      this.jpaUserRepository.save(userModel);

    } catch (IllegalArgumentException e) {
      throw new ResourceInvalidException("Usuário já existe");
    }

    var output = new FindUserOutput(
      userModel.getId(),
      userModel.getUsername(),
      userModel.getRoles(),
      userModel.isEnabled(),
      userModel.getCreatedAt(),
      userModel.getUpdatedAt()
    );

    return output;
  }

  public FindUserOutput patch(UUID id, PatchUserInput input) {
    var updatedAt = Instant.now();

    var userModel = this.jpaUserRepository
      .findById(id)
      .map((pM) -> new UserModel(
        pM.getId(),
        input.username().orElse(pM.getUsername()),
        input.password().orElse(pM.getPassword()),
        input.roles().orElse(pM.getRoles()),
        input.enabled().orElse(pM.isEnabled()),
        pM.getCreatedAt(),
        updatedAt
      ))
      .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessage.NOT_FOUND.getMessage()));

    this.jpaUserRepository.save(userModel);

    var output = new FindUserOutput(
      id,
      userModel.getUsername(),
      userModel.getRoles(),
      userModel.isEnabled(),
      userModel.getCreatedAt(),
      userModel.getUpdatedAt()
    );

    return output;
  }

  public void deleteById(UUID id) {
    this.jpaUserRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessage.NOT_FOUND.getMessage()));

    this.jpaUserRepository.deleteById(id);
  }

}
