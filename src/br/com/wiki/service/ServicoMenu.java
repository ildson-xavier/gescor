package br.com.wiki.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import br.com.wiki.dao.MenuDAO;
import br.com.wiki.entidade.Menu;

@Path("/produtor")
public class ServicoMenu {

	private MenuDAO dao;
	
	@Context
	private HttpServletResponse servletResponse;
	
	@PostConstruct
	private void init(){
		dao = new MenuDAO();
	}
	
	@POST
	@Path("/salvarMenu")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarMenu(Menu menu){
		String msg = "";
		Menu m = new Menu();
		try {			
			m.setNome(menu.getNome());
			dao.salvar(m);
			msg = "Configuracao adicionada com sucesso! ";
		} catch (Exception e) {
			msg = "Erro ao adicionar Configuracao!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@GET
	@Path("/listarMenu")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Menu> listarMenu(){

		List<Menu> lista = new ArrayList<>();
		try {
			lista = dao.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
}
