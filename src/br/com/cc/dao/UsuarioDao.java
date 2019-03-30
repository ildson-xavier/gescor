/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cc.dao;

import br.com.cc.entidade.Perfil;
import br.com.cc.entidade.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

/**
 *
 * @author Usuario
 */
public class UsuarioDao extends GenericDAO<Usuario> implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void adicionarUsuario(Usuario usuario) throws Exception {
        save(usuario);
    }
	
	public void salvarMerge(Usuario usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void atualizarUsuario (Usuario usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void removerUsuario (Usuario usuario) throws Exception {
        remove(usuario);
    }
    
    public List<Usuario> getUsuarios() throws Exception {
        return getList("select us from Usuario us where us");
    }
    
    public List<Usuario> getUsuarios(Usuario usuario) throws Exception {
        return getList("select us from Usuario us where us.id = ?1 or us.idPai = ?2", usuario.getId(), usuario.getId());
    }
    
    public List<Usuario> getUsuariosPorPerfil(Perfil perfil) {
        return getList("select us from Usuario us where us.perfil = ?1", perfil);
    }
    
    public Usuario getUsuario (int id) throws Exception {
        return getPojo(Usuario.class, id);
    }
    
    public Usuario getUsuarioLogin (String login, String senha) throws NoResultException, Exception {
        return getPojo("select us from Usuario us where us.login = ?1 and us.senha = ?2", 
                login, senha);
    }
    
    public Usuario getUsuarioEmail (String email) throws NoResultException, Exception {
    	Usuario us = null;
    	try {
    		us = getPojo("select us from Usuario us where us.email = ?1 ", 
            		email);
    	}catch (PersistenceException e) {
			us = null;
		}
    	
        return us;
    }
    
    public Usuario getUsuarioLogin (String login) throws NoResultException, Exception {
    	Usuario us = null;
    	try {
    		us = getPojo("select us from Usuario us where us.login = ?1 ", 
            		login);
    	}catch (PersistenceException e) {
			us = null;
		}
    	
        return us;
    }
}
