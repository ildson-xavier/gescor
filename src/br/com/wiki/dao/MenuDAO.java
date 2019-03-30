package br.com.wiki.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.dao.GenericDAO;
import br.com.wiki.entidade.Menu;

public class MenuDAO extends GenericDAO<Menu> implements Serializable{

	private static final long serialVersionUID = 2228061595083639124L;
	
	public void salvar(Menu menu){
		save(menu);
	}
	
	public List<Menu> listar(){
		return getList("select m from Menu m order by m.nome asc");
	}
	
	public Menu pegarMenu(Long id){
		return getPojo("select m from Menu m where m.id = ?1", id);
	}

}
