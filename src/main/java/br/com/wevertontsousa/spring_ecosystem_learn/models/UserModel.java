package br.com.wevertontsousa.spring_ecosystem_learn.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users")
public class UserModel {

  @Id
  private UUID id;

  @Column(name = "username", length = 30, nullable = false, unique = true)
  private String username;

  @Column(name = "password", length = 255, nullable = false)
  private String password;

  @Column(name = "roles", length = 20, nullable = false)
  private String roles;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = true)
  private Instant updatedAt;

  public UserModel() {}

  public UserModel(
    UUID id,
    String username,
    String password,
    String roles,
    boolean enabled,
    Instant createdAt,
    Instant updatedAt
  ) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.enabled = enabled;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UserModel(
    UUID id,
    String username,
    String password,
    String roles,
    boolean enabled,
    Instant createdAt
  ) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roles = roles;
    this.enabled = enabled;
    this.createdAt = createdAt;
  }

  public UUID getId() { return this.id; }
  public String getUsername() { return this.username; }
  public String getPassword() { return this.password; }
  public String getRoles() { return this.roles; }
  public boolean isEnabled() { return this.enabled; }
  public Instant getCreatedAt() { return this.createdAt; }
  public Instant getUpdatedAt() { return this.updatedAt; }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

}
