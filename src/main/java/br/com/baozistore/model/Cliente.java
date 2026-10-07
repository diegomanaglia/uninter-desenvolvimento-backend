package br.com.baozistore.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// @Entity avisa o JPA que essa classe vira uma tabela no banco (tabela CLIENTE)
@Entity
public class Cliente {

	// @Id marca a chave primária da tabela
	// @GeneratedValue com IDENTITY deixa o próprio banco gerar o id (1, 2, 3...)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	// data em que a pessoa virou cliente da loja (formato no JSON: "2026-10-07")
	private LocalDate clienteDesde;

	// o JPA precisa de um construtor vazio para conseguir criar o objeto
	public Cliente() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getClienteDesde() {
		return clienteDesde;
	}

	public void setClienteDesde(LocalDate clienteDesde) {
		this.clienteDesde = clienteDesde;
	}

}
