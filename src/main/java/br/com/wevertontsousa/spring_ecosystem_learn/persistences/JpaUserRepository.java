package br.com.wevertontsousa.spring_ecosystem_learn.persistences;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wevertontsousa.spring_ecosystem_learn.models.UserModel;

@Repository
public interface JpaUserRepository extends JpaRepository<UserModel, UUID> {}
