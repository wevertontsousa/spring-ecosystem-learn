package br.com.wevertontsousa.spring_ecosystem_learn.dtos;

public record ApiResponse<T>(int code, String title, T data) {}
