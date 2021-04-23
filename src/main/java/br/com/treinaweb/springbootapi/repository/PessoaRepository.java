package br.com.treinaweb.springbootapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.treinaweb.springbootapi.entity.Pessoa;

/**
 * Esta interface possui métodos para as operações padrão de um CRUD.
 * */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> { }