package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.sinistro.ApoliceVeiculo;

public class ApoliceVeiculoDAO extends GenericDAO<ApoliceVeiculo> implements Serializable {

	private static final long serialVersionUID = 1L;

	public void adicionarApoliceVeiculo(ApoliceVeiculo produtor) throws Exception {
		save(produtor);
	}

	public ApoliceVeiculo atualizarApoliceVeiculo(ApoliceVeiculo produtor) throws Exception {
		return saveOrUpdate(produtor);
	}

	public void removerApoliceVeiculo(ApoliceVeiculo produtor) throws Exception {
		remove(produtor);
	}

	public ApoliceVeiculo getApoliceVeiculo(int idProdutor) throws Exception {
		return getPojo(ApoliceVeiculo.class, idProdutor);
	}

	public List<ApoliceVeiculo> getApoliceVeiculos() throws Exception {
		return getList("select p from ApoliceVeiculo p");
	}

	public List<ApoliceVeiculo> getApoliceVeiculoPorSegurado(String nome) throws Exception {
		return getList("select p from ApoliceVeiculo p where p.segurado like ?1 ", "%" + nome.toUpperCase() + "%");
	}

	public List<ApoliceVeiculo> getApoliceVeiculoPorApolice(String nome) throws Exception {
		return getList("select p from ApoliceVeiculo p where replace( replace(p.apolice, '.',''), '-','') like ?1 ",
				"%" + nome.toUpperCase() + "%");
	}

	public List<ApoliceVeiculo> getApoliceVeiculoPorSegurado(int codSegurado) throws Exception {
		return getList("select p from ApoliceVeiculo p where p.codigoSegurado = ?1 order by apoliceAno desc",
				codSegurado);
	}

}
