package br.com.wiki.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.dao.GenericDAO;
import br.com.wiki.entidade.Menu;
import br.com.wiki.entidade.Publicacao;

public class PublicacaoDAO extends GenericDAO<Publicacao> implements Serializable{

	private static final long serialVersionUID = 2305306393467012231L;

	public void salvar(Publicacao publicacao){
		save(publicacao);
	}
	
	public List<Publicacao> listar(Long id){
		return getList("select p from Publicacao p where p.menu.id = ?1 order by p.data desc", id);
	}
}
