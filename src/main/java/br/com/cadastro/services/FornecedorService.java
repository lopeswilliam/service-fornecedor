package br.com.cadastro.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.cadastro.entity.Fornecedor;
import br.com.cadastro.entity.Endereco;
import br.com.cadastro.models.FornecedorRequest;
import br.com.cadastro.repositories.FornecedorRepository;
import br.com.cadastro.repositories.EnderecoRepository;

@Service
public class FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	private static final Logger logger = LogManager.getLogger(FornecedorService.class);


	/**
	 * @param prodRequest
	 * @return
	 */
	public ResponseEntity<Fornecedor> inclusao(FornecedorRequest fornecedoroRequest) {
		logger.info("Iniciando a chamada da Inclusao de DepositoRequest");

		Fornecedor fornecedor = new Fornecedor();
		fornecedor = addFornecedor(fornecedoroRequest, fornecedor);
		try {
			fornecedor = fornecedorRepository.save(fornecedor);
		} catch (Exception e) {
			logger.error("Ocorreu um erro na persistencia de Dados");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fornecedor);
		}

		if(fornecedor != null) {
			Endereco endereco = new Endereco();
			endereco = addEndereco(fornecedoroRequest, endereco,fornecedor);
			endereco = enderecoRepository.save(endereco);
			fornecedor.setEndereco(endereco);
			return ResponseEntity.ok().body(fornecedor);
		}

		return ResponseEntity.notFound().build();

	}

	private Fornecedor addFornecedor(FornecedorRequest depRequest, Fornecedor fornecedor) {

		if(depRequest.getId() != null) {
			fornecedor.setId(Long.valueOf(depRequest.getId()));;
		}

		fornecedor.setNome(depRequest.getNome());;
		fornecedor.setNomeFantasia(depRequest.getNomeFantasia());;
		fornecedor.setEmail(depRequest.getEmail());
		fornecedor.setCnpj(depRequest.getCnpj());
		fornecedor.setTipoEmpresa(depRequest.getTipoEmpresa());

		Endereco endereco = new Endereco();
		endereco = addEndereco(depRequest, endereco,fornecedor);
		fornecedor.setEndereco(endereco);

		return fornecedor;
	}

	/**
	 * @param codigo
	 * @return
	 */
	public ResponseEntity<List<Fornecedor>> listarFornecedores() {
		logger.info("Iniciando a chamada da Consulta de listarFornecedores");
		List<Fornecedor> depositoList = new ArrayList<>();
		try {

			List<Fornecedor> findAll = this.fornecedorRepository.findAll();

			if(findAll.size() > 0 ) {
				for (Fornecedor deposito : findAll) {

					Optional<Endereco> findById = Optional.ofNullable(enderecoRepository.findByFornecedorid(deposito.getId()));

					if(findById.isPresent()) {
						Endereco endereco = new Endereco();
						endereco = findById.orElse(new Endereco());
						deposito.setEndereco(endereco);
						depositoList.add(deposito);
					}

				}

				if(depositoList.size() > 0 ) {
					return ResponseEntity.ok().body(depositoList);
				}

			}



			if(findAll.size() > 0 ) {
				return ResponseEntity.ok().body(findAll);
			}

		} catch (Exception e) {
			logger.error("Iniciando a chamada da consulta de listarFornecedores");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(depositoList);
		}

		return ResponseEntity.notFound().build();

	}

	public ResponseEntity<Fornecedor> atualizar(FornecedorRequest depoRequest) {
		logger.info("Iniciando a chamada da atualizar Fornecedores");

		List<Fornecedor> findByCnpj = fornecedorRepository.findByCnpj(depoRequest.getCnpj());

		if(findByCnpj.size() > 0) {

			for (Fornecedor fornec : findByCnpj) {

				Fornecedor fornecedor = new Fornecedor();
				fornecedor = atualizarDeposito(depoRequest, fornec);

				try {
					fornecedor = fornecedorRepository.saveAndFlush(fornecedor);
				} catch (Exception e) {
					logger.error("Ocorreu um erro na persistencia de Dados");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fornecedor);
				}

				if(fornecedor != null) {
					Endereco endereco = new Endereco();
					endereco = enderecoRepository.findByFornecedorid(fornecedor.getId());
					endereco = addEndereco(depoRequest, endereco, fornecedor);
					endereco = enderecoRepository.saveAndFlush(endereco);
					fornecedor.setEndereco(endereco);
					return ResponseEntity.ok().body(fornecedor);
				}
			}
		}

		return ResponseEntity.notFound().build();

	}

	private Endereco addEndereco(FornecedorRequest enderecoRequest,Endereco ender, Fornecedor deposito) {
		Endereco endereco = new Endereco();
		endereco.setId(ender.getId());
		endereco.setClienteId(new Long(0));
		endereco.setDepositoid(new Long(0));
		endereco.setFornecedorid(deposito.getId());
		endereco.setRua(enderecoRequest.getEndereco().getRua());
		endereco.setNumero(enderecoRequest.getEndereco().getNumero());
		endereco.setComplemento(enderecoRequest.getEndereco().getComplemento());
		endereco.setBairro(enderecoRequest.getEndereco().getBairro());
		endereco.setCep(enderecoRequest.getEndereco().getCep());
		endereco.setCidade(enderecoRequest.getEndereco().getCidade());
		endereco.setEstado(enderecoRequest.getEndereco().getEstado());
		return endereco;
	}

	/**
	 * @param codigo
	 * @return
	 */
	public ResponseEntity<List<Fornecedor>> consultar(String cnpj) {
		logger.info("Iniciando a chamada da Consulta de Fornecedor");
		List<Fornecedor> depositoList = new ArrayList<>();
		try {

			List<Fornecedor> findAll = this.fornecedorRepository.findByCnpj(cnpj);

			if(findAll.size() > 0 ) {
				for (Fornecedor deposito : findAll) {

					Optional<Endereco> findById = Optional.ofNullable(enderecoRepository.findByFornecedorid(deposito.getId()));

					if(findById.isPresent()) {
						Endereco endereco = new Endereco();
						endereco = findById.orElse(new Endereco());
						deposito.setEndereco(endereco);
						depositoList.add(deposito);
					}

				}

				return ResponseEntity.ok().body(depositoList);

			} 

		} catch (Exception e) {
			logger.error("Iniciando a chamada da consulta de Fornecedor");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(depositoList);
		}

		logger.info("Fim da chamada da consulta de Fornecedor");
		return ResponseEntity.notFound().build();

	}


	private Fornecedor atualizarDeposito(FornecedorRequest depRequest, Fornecedor fornecedor) {

		if(depRequest.getId() != null) {
			fornecedor.setId(Long.valueOf(fornecedor.getId()));;
		}

		fornecedor.setNome(depRequest.getNome());;
		fornecedor.setNomeFantasia(depRequest.getNomeFantasia());;
		fornecedor.setEmail(depRequest.getEmail());
		fornecedor.setCnpj(depRequest.getCnpj());
		fornecedor.setTipoEmpresa(depRequest.getTipoEmpresa());

		return fornecedor;
	}


}
