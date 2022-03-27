package br.com.cadastro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cadastro.entity.Fornecedor;


@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>{

	List<Fornecedor> findByCnpj(String cnpj);

	Fornecedor deleteByCnpj(String cnpj);
	
	List<Fornecedor> findAll();
	
	Optional<Fornecedor> findById(Long id);
	
}
