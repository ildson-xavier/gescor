package br.com.cc.dao;

import br.com.cc.entidade.LancamentosDeletados;

public class LancamentosDeletadosDAO extends GenericDAO<LancamentosDeletados>{

	public void adicionar(LancamentosDeletados ld){
		saveOrUpdate(ld);
	}
}
