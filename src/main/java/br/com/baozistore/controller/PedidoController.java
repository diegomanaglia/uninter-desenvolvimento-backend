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

import br.com.baozistore.model.Pedido;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.repository.ProdutoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoRepository pedidoRepository;

	// esses dois são usados só para conferir se o cliente e o produto do pedido existem
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	// POST /pedidos
	// ResponseEntity<?> porque pode devolver o pedido salvo ou uma mensagem de erro
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
		// Como o pedido guarda só os ids (clienteId e produtoId), o banco não impede
		// de salvar um id que não existe. Por isso a conferência é feita aqui antes de salvar.
		if (pedido.getClienteId() == null || !clienteRepository.existsById(pedido.getClienteId())) {
			// 400 (Bad Request) = o problema está nos dados que foram enviados
			return ResponseEntity.badRequest().body("Cliente não encontrado");
		}

		if (pedido.getProdutoId() == null || !produtoRepository.existsById(pedido.getProdutoId())) {
			return ResponseEntity.badRequest().body("Produto não encontrado");
		}

		Pedido pedidoSalvo = pedidoRepository.save(pedido);
		return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
	}

	// GET /pedidos
	@GetMapping
	public List<Pedido> listarTodos() {
		return pedidoRepository.findAll();
	}

	// GET /pedidos/{id}
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);

		if (pedido.isPresent()) {
			return ResponseEntity.ok(pedido.get());
		}

		return ResponseEntity.notFound().build();
	}

	// DELETE /pedidos/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> apagar(@PathVariable Long id) {
		if (!pedidoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		pedidoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// PUT /pedidos/{id}
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Pedido dadosNovos) {
		Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);

		if (pedidoExistente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// mesma conferência do POST: não deixa trocar para um cliente ou produto que não existe
		if (dadosNovos.getClienteId() == null || !clienteRepository.existsById(dadosNovos.getClienteId())) {
			return ResponseEntity.badRequest().body("Cliente não encontrado");
		}

		if (dadosNovos.getProdutoId() == null || !produtoRepository.existsById(dadosNovos.getProdutoId())) {
			return ResponseEntity.badRequest().body("Produto não encontrado");
		}

		Pedido pedido = pedidoExistente.get();
		pedido.setClienteId(dadosNovos.getClienteId());
		pedido.setProdutoId(dadosNovos.getProdutoId());
		pedido.setQuantidade(dadosNovos.getQuantidade());

		return ResponseEntity.ok(pedidoRepository.save(pedido));
	}

}
