package br.com.cc.dao;

import java.io.Serializable;

import br.com.cc.entidade.sinistro.SinistroHistorico;

public class SinistroHistoricoDAO extends GenericDAO<SinistroHistorico> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public SinistroHistorico salvar(SinistroHistorico historico){
		return super.saveOrUpdate(historico);
	}

}
