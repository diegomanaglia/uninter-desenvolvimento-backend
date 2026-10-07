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

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;

// @RestController = classe que recebe as requisições HTTP e devolve JSON
// @RequestMapping = todas as rotas dessa classe começam com /clientes
@RestController
@RequestMapping("/clientes")
public class ClienteController {

	// @Autowired faz o Spring entregar o repository pronto para usar
	@Autowired
	private ClienteRepository clienteRepository;

	// POST /clientes -> cadastra um cliente novo
	// @RequestBody pega o JSON enviado no corpo da requisição e transforma em um Cliente
	@PostMapping
	public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
		Cliente clienteSalvo = clienteRepository.save(cliente);
		// 201 (Created) é o status certo quando algo novo é criado
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}

	// GET /clientes -> lista todos os clientes
	@GetMapping
	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	// GET /clientes/{id} -> busca um cliente pelo id
	// @PathVariable pega o {id} que vem na URL
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		// o findById devolve um Optional, porque pode ser que não exista cliente com esse id
		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}

		// se não achou, devolve 404 (Not Found) em vez de dar erro
		return ResponseEntity.notFound().build();
	}

	// DELETE /clientes/{id} -> apaga um cliente
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> apagar(@PathVariable Long id) {
		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		clienteRepository.deleteById(id);
		// 204 (No Content) = deu certo, mas não tem nada para devolver
		return ResponseEntity.noContent().build();
	}

	// PUT /clientes/{id} -> atualiza os dados de um cliente que já existe
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dadosNovos) {
		Optional<Cliente> clienteExistente = clienteRepository.findById(id);

		if (clienteExistente.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		// pega o cliente que está no banco e troca só os campos (o id continua o mesmo)
		Cliente cliente = clienteExistente.get();
		cliente.setNome(dadosNovos.getNome());
		cliente.setClienteDesde(dadosNovos.getClienteDesde());

		// o save com um id que já existe faz um UPDATE no banco, e não um INSERT
		return ResponseEntity.ok(clienteRepository.save(cliente));
	}

}
