package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.SeguradorasExtrato;

public class SeguradoraExtratoDAO extends GenericDAO<SeguradorasExtrato> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void salvar(SeguradorasExtrato extrato) throws Exception {
		super.saveOrUpdate(extrato);
	}
	
	public SeguradorasExtrato buscarExtratoPorPeriodoEProdutor(String periodo, String seguradora) throws Exception {
		return super.getPojo("select n from SeguradorasExtrato n where n.periodo = ? and n.seguradora = ?", periodo, seguradora);
	}
	
	public List<SeguradorasExtrato> buscarExtratoPorPeriodoEProdutor(String periodo) throws Exception {
		return super.getList("select n from SeguradorasExtrato n where n.periodo = ?", periodo);
	}
}
