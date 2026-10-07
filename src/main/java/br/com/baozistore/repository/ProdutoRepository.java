package br.com.baozistore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.baozistore.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
