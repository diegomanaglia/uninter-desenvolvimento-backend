package br.com.baozistore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;

// mesmo esquema do ClienteController, só que para produtos
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	// POST /produtos
	@PostMapping
	public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
		Produto produtoSalvo = produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}

	// GET /produtos
	@GetMapping
	public List<Produto> listarTodos() {
		return produtoRepository.findAll();
	}

	// GET /produtos/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);

		if (produto.isPresent()) {
			return ResponseEntity.ok(produto.get());
		}

		return ResponseEntity.notFound().build();
	}

	// DELETE /produtos/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> apagar(@PathVariable Long id) {
		if (!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		produtoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// PUT /produtos/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto dadosNovos) {
		Optional<Produto> produtoExistente = produtoRepository.findById(id);

		if (produtoExistente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Produto produto = produtoExistente.get();
		produto.setNome(dadosNovos.getNome());
		produto.setPreco(dadosNovos.getPreco());
		produto.setEstoque(dadosNovos.getEstoque());

		return ResponseEntity.ok(produtoRepository.save(produto));
	}

}
