package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Seguradora;

public class SeguradoraDAO extends GenericDAO<Seguradora> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public void adicionar(Seguradora seguradora){
		save(seguradora);
	}
	
	public Seguradora getSeguradora(int id){
		return getPojo(Seguradora.class, id);
	}
	
	public List<Seguradora> getSeguradoras(){
		return getList("select s from Seguradora s order by s.nome asc");
	}

}
