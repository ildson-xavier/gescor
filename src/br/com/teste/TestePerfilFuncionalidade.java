package br.com.teste;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import br.com.cc.dao.FuncionalidadeDAO;
import br.com.cc.dao.PerfilDAO;
import br.com.cc.dao.UsuarioDao;
import br.com.cc.entidade.Funcionalidade;
import br.com.cc.entidade.Perfil;
import br.com.cc.entidade.Usuario;
import junit.framework.TestCase;

public class TestePerfilFuncionalidade extends TestCase{

	@Test
	public void teste() throws Exception{
		UsuarioDao dao = new UsuarioDao();
		PerfilDAO per = new PerfilDAO();
		FuncionalidadeDAO fun = new FuncionalidadeDAO();
		Usuario usuario = new Usuario();
		Perfil perfil = new Perfil();
		Funcionalidade funcionalidade = new Funcionalidade();
		/*
		usuario.setLogin("ixm");
		usuario.setSenha("123");
		perfil.setDescricao("ADM");

		per.salvarPerfil(perfil);
		usuario.setPerfil(per.getPerfil(1));
		
		dao.salvarMerge(usuario);*/
		funcionalidade.setAtivo(true);
		funcionalidade.setDescricao("Tela de despesas");
		funcionalidade.setPagina("Pagina depesas");
		
		usuario = dao.getUsuario(1);
		usuario.setPerfil(per.getPerfil(1));
		funcionalidade.setPerfil(usuario.getPerfil());
		
		//funcionalidade = fun.salvarFuncionalidade(funcionalidade);
		//System.out.println("Id -> "+funcionalidade.getId() );
		
		List<Funcionalidade> lista = fun.getFuncionalidadesPorPerfil(per.getPerfil(1));
		for (Funcionalidade f : lista){
			System.out.println("ID -> "+f.getId());
		}
		
	}
}
