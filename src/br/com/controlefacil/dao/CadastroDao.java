package br.com.controlefacil.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.dao.GenericDAO;
import br.com.controlefacil.model.Cadastro;
import br.com.controlefacil.model.UsuarioControle;

public class CadastroDao extends GenericDAO<Cadastro> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void adicionarCadastro(Cadastro usuario) throws Exception {
        save(usuario);
    }
	
	public void salvarMerge(Cadastro usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void atualizarCadastro (Cadastro usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void removerCadastro (Cadastro usuario) throws Exception {
        remove(usuario);
    }
    
    public List<Cadastro> getCadastroPorUsuario(UsuarioControle us) throws Exception {
        return getList("select us from Cadastro us where us.usuario = ?", us);
    }
}
