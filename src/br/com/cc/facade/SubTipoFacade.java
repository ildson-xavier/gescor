package br.com.cc.facade;

import java.io.Serializable;

import br.com.cc.dao.SubTipoDAO;
import br.com.cc.entidade.SubTipo;

public class SubTipoFacade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SubTipoDAO tipoDAO = new SubTipoDAO();
	
	public void adicionarSubTipo(SubTipo subTipo) throws Exception{
		tipoDAO.adicionarSubTipo(subTipo);
	}

}
