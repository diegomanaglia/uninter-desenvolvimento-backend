package br.com.baozistore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.baozistore.model.Cliente;

// Estendendo JpaRepository o Spring já cria sozinho os métodos básicos do banco:
// save, findAll, findById, existsById, deleteById, etc. Não precisa escrever SQL.
// <Cliente, Long> = a entidade e o tipo do id dela
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
