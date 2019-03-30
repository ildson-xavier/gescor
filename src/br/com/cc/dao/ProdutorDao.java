/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;

import br.com.cc.entidade.Produtor;

/**
 *
 * @author Usuario
 */
@ApplicationScoped
public class ProdutorDao extends GenericDAO<Produtor> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void adicionarProdutor(Produtor produtor) throws Exception {
        save(produtor);
    }

    public Produtor atualizarProdutor(Produtor produtor) throws Exception {
        return saveOrUpdate(produtor);
    }

    public void removerProdutor(Produtor produtor) throws Exception {
        remove(produtor);
    }

    public Produtor getProdutor(int idProdutor) throws Exception {
        return getPojo(Produtor.class, idProdutor);
    }

    public List<Produtor> getProdutores() throws Exception {
        return getList("select p from Produtor p");
    }
    
    public List<Produtor> getProdutorPorCpf (String cpf) throws Exception {
    	return getList("select p from Produtor p where "
    			+ "p.cpf is not null and "
    			+ "replace(replace(replace(replace(p.cpf, '.',''),'.',''),'-',''),'/','') like ?1 ", 
    			"%" + cpf.replace(".", "").replace(".", "").replace("-", "").replace("/", "") + "%");
    }
    
    public Produtor getProdutorPorCpf2 (String cpf) throws Exception {
    	return getPojo("select p from Produtor p where "
    			+ "p.cpf is not null and "
    			+ "replace(replace(replace(replace(p.cpf, '.',''),'.',''),'-',''),'/','') like ?1 ", 
    			"%" + cpf.replace(".", "").replace(".", "").replace("-", "").replace("/", "") + "%");
    }

    public List<Produtor> getProdutoresPorProdutor(String nome) throws Exception {
        return getList("select p from Produtor p where p.produtor like ?1 ",
                "%" + nome.toUpperCase() + "%");
    }

    public List<Produtor> getProdutoresPorSegurado(String nome) throws Exception {
        return getList("select p from Produtor p where p.segurado like ?1 ",
                "%" + nome.toUpperCase() + "%");
    }

    public List<Produtor> getProdutoresESegurados(String produtor, String segurado) throws Exception {
        return getList("select p from Produtor p where p.segurado like ?1 "
                     + "and p.produtor like ?2 ",
                "%" + segurado.toUpperCase() + "%", "%" + produtor.toUpperCase() + "%");
    }
}
