package br.com.controlefacil.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;

import br.com.cc.dao.GenericDAO;
import br.com.cc.entidade.Usuario;
import br.com.controlefacil.model.UsuarioControle;

public class UsuarioControleDao extends GenericDAO<UsuarioControle> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void adicionarUsuario(UsuarioControle usuario) throws Exception {
        save(usuario);
    }
	
	public void salvarMerge(UsuarioControle usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void atualizarUsuario (UsuarioControle usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void removerUsuario (UsuarioControle usuario) throws Exception {
        remove(usuario);
    }
    
    public List<UsuarioControle> getUsuarios() throws Exception {
        return getList("select us from UsuarioControle us");
    }
    
    public UsuarioControle getUsuario(Long id) throws Exception {
        return getPojo("select us from UsuarioControle us where us.id = ?", id);
    }
    
    public UsuarioControle getUsuarioLogin (String login, String senha) throws NoResultException, Exception {
        return getPojo("select us from UsuarioControle us where us.email = ?1 and us.senha = ?2", 
                login, senha);
    }
}
