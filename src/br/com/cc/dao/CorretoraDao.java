package br.com.cc.dao;

import java.io.Serializable;

import br.com.cc.entidade.Corretora;

public class CorretoraDao extends GenericDAO<Corretora> implements Serializable{

	private static final long serialVersionUID = 1L;

	public Corretora adicionarCorretora(Corretora corretora) throws Exception{
		return saveOrUpdate(corretora);
	}
	
	public Corretora buscarCorretora(Corretora corretora) {
		return super.getPojo(Corretora.class, corretora.getId());
	}
	
	public Corretora atualizarCorretora(Corretora corretora) throws Exception{
	    return saveOrUpdate(corretora);
	}
}
