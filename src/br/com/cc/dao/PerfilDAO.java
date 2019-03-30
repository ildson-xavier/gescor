package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Perfil;

public class PerfilDAO extends GenericDAO<Perfil> implements Serializable{

	private static final long serialVersionUID = 1L;

	public Perfil salvarPerfil(Perfil perfil){
		perfil = saveOrUpdate(perfil);
		return perfil;
	}
	
	public Perfil getPerfil(Integer id) {
		return getPojo(Perfil.class, id);
	}
	
	public void remover(Perfil perfil) throws Exception{
		remove(perfil);
	}
	
	public List<Perfil> getPerfis(){
		return getList("select p from Perfil p");
	}
	
	public Perfil getPerfiPorDescricao(String descricao){
		return getPojo("select p from Perfil p where p.descricao = ?1",descricao);
	}
}
