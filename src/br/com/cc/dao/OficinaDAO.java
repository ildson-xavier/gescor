package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.sinistro.Oficina;

public class OficinaDAO extends GenericDAO<Oficina> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Oficina salvar(Oficina oficina){
		return super.saveOrUpdate(oficina);
	}
	
	public List<Oficina> listarOficinas(){
		return super.findAll();
	}
	
	public Oficina getOficina(int id){
		return super.getPojo(Oficina.class, id);
	}
	
	public List<Oficina> listarOficinasPorNome(String nome){
		return getList("select p from Oficina p where p.nome like ?1 ",
                "%" + nome.toUpperCase() + "%");
	}

}
