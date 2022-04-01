package br.com.cadastro.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cadastro.entity.Fornecedor;
import br.com.cadastro.models.FornecedorRequest;
import br.com.cadastro.services.FornecedorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@CrossOrigin
@Api(value = "Fornecedores")
@RequestMapping("/fornecedor/v1")
public class FornecedorController {
	

	@Autowired
	private FornecedorService fornecedorService;
	
	private static final Logger logger = LogManager.getLogger(FornecedorController.class);

	@ApiOperation(value = "Inclui Fornecedores")
	@PostMapping(path = "/incluir" , produces = {"application/json"})
	public ResponseEntity<Fornecedor> inclusao(@RequestBody FornecedorRequest fornecedoroRequest, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a Inclusao do fornecedorService");
		return fornecedorService.inclusao(fornecedoroRequest );
	}

	@ApiOperation(value = "Mostra listar Fornecedores")
	@GetMapping(path = "/listarFornecedores" , produces = {"application/json"})
	public ResponseEntity<List<Fornecedor>> consultaFornecedores(@RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a consulta do fornecedorService");
		return fornecedorService.listarFornecedores();
	}
	
	@ApiOperation(value = "Atualiza os Fornecedores")
	@PutMapping(path = "/alterar" , produces = {"application/json"})
	public ResponseEntity<Fornecedor> atualizar(@RequestBody FornecedorRequest depositoRequest, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a Inclusao do fornecedorService");
		return fornecedorService.atualizar(depositoRequest );
	}
	
	@ApiOperation(value = "Consulta os Fornecedores")
	@GetMapping(path = "/consultar" , produces = {"application/json"})
	public ResponseEntity<List<Fornecedor>> consulta(@RequestParam("cnpj") String cnpj, @RequestHeader("authorization") String authorization) {
		logger.info("Iniciando a consulta do fornecedorService");
		return fornecedorService.consultar(cnpj);
	}
	

}
