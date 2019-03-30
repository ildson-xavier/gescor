package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Extrato;

public class ExtratoDAO extends GenericDAO<Extrato> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void salvar(Extrato extrato) throws Exception {
		super.saveOrUpdate(extrato);
	}
	
	public List<Extrato> buscarExtrato() throws Exception {
		return super.getList("select n from Extrato n order by n.id  desc ");
	}
	
	public List<Extrato> buscarExtratoPorProdutor(String produtor) throws Exception {
		return super.getList("select n from Extrato n where ucase(n.produtor) like ? order by n.id desc ", "%"+produtor.toUpperCase()+"%");
	}
	
	public List<Extrato> buscarExtratoPorPeriodo(String periodo) throws Exception {
		return super.getList("select n from Extrato n where n.periodo = ? order by n.id desc", periodo);
	}
	
	public List<Extrato> buscarExtratoPorPeriodoEProdutor(String periodo, String produtor) throws Exception {
		return super.getList("select n from Extrato n where n.periodo = ? and ucase(n.produtor) like ? order by n.id desc", periodo, "%"+produtor.toUpperCase()+"%");
	}
	
	public List<Extrato> buscarExtratoPoProdutorOrder(String produtor, int mesReferencia) throws Exception {
		return super.getList("select n from Extrato n where n.produtor = ? and n.id >= ? order by n.mesReferencia asc", produtor, 0);
	}
	
	public void deletar(Extrato extrato) throws Exception{
		super.remove(extrato);
	}

}
