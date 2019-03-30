package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Funcionalidade;
import br.com.cc.entidade.Perfil;

public class FuncionalidadeDAO extends GenericDAO<Funcionalidade> implements Serializable {

	private static final long serialVersionUID = 1L;

	public void salvar(Funcionalidade funcionalidade){
		save(funcionalidade);
	}
	
	public Funcionalidade salvarFuncionalidade(Funcionalidade funcionalidade){
		funcionalidade = saveOrUpdate(funcionalidade);
		return funcionalidade;
	}
	
	public Funcionalidade getFuncionalidade(Integer id) throws Exception{
		return getPojo(Funcionalidade.class, id);
	}
	
	public void remover(Funcionalidade funcionalidade) throws Exception{
		remove(funcionalidade);
	}
	
	public List<Funcionalidade> getFuncionalidades(){
		return getList("select p from Funcionalidade p");
	}
	
	public List<Funcionalidade> getFuncionalidadesPorPerfil(Perfil perfil){
		return getList("select p from Funcionalidade p where p.perfil = ?1", perfil);
	}
}
