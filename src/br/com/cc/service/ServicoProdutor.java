package br.com.cc.service;

import java.math.BigDecimal;
import java.util.ArrayList;


import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import br.com.cc.dao.ApoliceVeiculoDAO;
import br.com.cc.dao.ConfiguracaoProdutorDAO;
import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.ConfiguracaoProdutor;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.sinistro.ApoliceVeiculo;
import br.com.cc.util.Util;
import br.com.controlefacil.dao.CadastroDao;
import br.com.controlefacil.dao.LancamentoDao;
import br.com.controlefacil.dao.UsuarioControleDao;
import br.com.controlefacil.model.Cadastro;
import br.com.controlefacil.model.Lancamento;
import br.com.controlefacil.model.UsuarioControle;
import br.com.wiki.dao.MenuDAO;
import br.com.wiki.dao.PublicacaoDAO;
import br.com.wiki.entidade.Menu;
import br.com.wiki.entidade.Publicacao;

@Path("/produtor")
public class ServicoProdutor {

	private ProdutorDao dao;
	private ConfiguracaoProdutorDAO confDao;
	
	private MenuDAO menu;
	private PublicacaoDAO publicacao;
	
	private UsuarioControleDao usc;
	private CadastroDao cas;
	private LancamentoDao lan;
	
	@Context
	private HttpServletResponse servletResponse;
	
	@PostConstruct
	private void init(){
		dao = new ProdutorDao();
		confDao = new ConfiguracaoProdutorDAO();
		menu = new MenuDAO();
		publicacao = new PublicacaoDAO();
		
		usc = new UsuarioControleDao();
		cas = new CadastroDao();
		lan = new LancamentoDao();
	}
	
	private void allowCrossDomainAccess() {
        if (servletResponse != null){
            servletResponse.setHeader("Access-Control-Allow-Origin", "*");
        }
    }
	
	@GET
	@Path("/listar")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Produtor> listarProdutor(){
		List<Produtor> lista = null;
		try {
			lista = dao.getProdutores();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	
	@GET
	@Path("/listaConf")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<ConfiguracaoProdutor> listarConfiguacao(){
		allowCrossDomainAccess();
		List<ConfiguracaoProdutor> lista = new ArrayList<>();
		try {
			lista = confDao.listarConfiguracaoProdutor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	@GET
	@Path("/listar/{id}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Produtor listarProdutorPorId(@PathParam("id") int idProdutor){
		Produtor lista = null;
		
		try {
			lista = dao.getProdutor(idProdutor);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@POST
	@Path("/adicionar")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String addProdutor(Produtor produtor){
		String msg = "";
		Produtor p = new Produtor();
		try {
			
			p.setProdutor(produtor.getProdutor());
			p.setSegurado(produtor.getSegurado());
			p.setApolice(produtor.getApolice());
			p.setCpf(produtor.getCpf());
			
			dao.adicionarProdutor(p);
			msg = "Produtor adicionado com sucesso! "+produtor.toString();
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@POST
	@Path("/adicionarConf")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String addConfiguracao(ConfiguracaoProdutor produtor){
		String msg = "";
		ConfiguracaoProdutor p = new ConfiguracaoProdutor();
		try {
			p.setDataInclusao(Calendar.getInstance().getTime());
			p.setQuantidade(produtor.getQuantidade());
			p.setUltimoId(produtor.getUltimoId());
			
			confDao.adicionarConfiguracao(p);
			msg = "Configuracao adicionada com sucesso! ";
		} catch (Exception e) {
			msg = "Erro ao adicionar Configuracao!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@POST
	@Path("/adicionarLista")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public String addListProdutor(String json){
		String msg = "";
		Produtor p = null;
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++){
				JSONObject js = new JSONObject(array.getJSONObject(i).toString());
				p = new Produtor();
				p.setProdutor(js.getString("corretor"));
				p.setSegurado(js.getString("segurado"));
				p.setCpf(js.getString("cpf"));
				
				p.setCodigo(js.getInt("codigo"));
				p.setBairro(js.getString("bairro"));
				p.setCelular(js.getString("celular"));
				p.setCidade(js.getString("cidade"));
				p.setEndereco(js.getString("endereco"));
				p.setNumero(js.getString("numero"));
				p.setTelefone(js.getString("telefone"));
				p.setUf(js.getString("Uf"));
				p.setCorretor(js.getString("corretor"));
				p.setApolice(js.getString("apolice"));
				
				dao.adicionarProdutor(p);
			}
			
			dao.adicionarProdutor(p);
			msg = "Produtor adicionado com sucesso! ";
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor! "+e.getMessage() + " <> "+json;
			e.printStackTrace();
		}
		return msg;
	}
	
	
	@POST
	@Path("/adicionarListaApoliceVeiculo")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public String addListApoliceVeiculo(String json){
		String msg = "";
		ApoliceVeiculo p = null;
		ApoliceVeiculoDAO dao = new ApoliceVeiculoDAO();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++){
				JSONObject js = new JSONObject(array.getJSONObject(i).toString());
				p = new ApoliceVeiculo();

				p.setApolice(js.getString("apolice"));
				p.setApoliceAno(js.getInt("apoliceAno"));
				p.setCodigoApolice(js.getInt("codigoApolice"));
				p.setCodigoSegurado(js.getInt("codigoSegurado"));
				p.setCor(js.getString("cor"));
				p.setPlaca(js.getString("placa"));
				p.setRenavan(js.getString("renavan"));
				p.setSegurado(js.getString("segurado"));
				p.setSeguradora(js.getString("seguradora"));
				p.setSituacao(js.getString("situacao"));
				p.setTipoVeiculo(js.getString("tipoVeiculo"));
				
				p.setApoliceDateFim(Util.converterDataStrParaDate(js.getString("apoliceDateInicio")));
				p.setApoliceDateInicio(Util.converterDataStrParaDate(js.getString("apoliceDateFim")));
				
				dao.adicionarApoliceVeiculo(p);
			}
			
			dao.adicionarApoliceVeiculo(p);
			msg = "Produtor adicionado com sucesso! ";
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor! "+e.getMessage() + " <> "+json;
			e.printStackTrace();
		}
		return msg;
	}


	
	/*******************************************************/
	/*******************************************************/
	/*******************************************************/
	@POST
	@Path("/salvarMenu")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarMenu(String menu){
		String msg = "";
		Menu m = new Menu();
		try {
			JSONObject json = new JSONObject(menu);
			m.setNome(json.getString("nome"));
			this.menu.salvar(m);
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
			lista = this.menu.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	/*******************************************************/
	/*******************************************************/
	/*******************************************************/
	@POST
	@Path("/salvarPublicacao")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarPublicacao(String publicacao){
		String msg = "";
		Publicacao p = new Publicacao();
		try {
			JSONObject json = new JSONObject(publicacao);
			String idd = json.getJSONObject("menu").get("id").toString();
			p.setMenu(menu.pegarMenu(Long.parseLong(idd)));
			//p.setImagem(json.getString("imagem"));
			p.setTexto(json.getString("texto"));
			p.setTitulo(json.getString("titulo"));
			this.publicacao.salvar(p);
			msg = "Configuracao adicionada com sucesso! ";
		} catch (Exception e) {
			msg = "Erro ao adicionar Configuracao!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@GET
	@Path("/listarPublicacao/{id}")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Publicacao> listarPublicacao(@PathParam("id") Long id){

		List<Publicacao> lista = new ArrayList<>();
		try {
			lista = this.publicacao.listar(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	/**********************************************************
	 * **********************************************************
	 * LISTAR 
	 */
	
	@GET
	@Path("/logarUsarioControle/{email}/{senha}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public UsuarioControle logarUsarioControle(@PathParam("email") String email, @PathParam("senha") String senha){
		UsuarioControle lista = null;
		
		try {
			lista = usc.getUsuarioLogin(email, senha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lista;
	}
	
	@GET
	@Path("/buscarCadastroPorUsuario/{usuario}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Cadastro> buscarCadastroPorUsuario(@PathParam("usuario") String usuario){
		List<Cadastro> lista = new ArrayList<>();
		JSONArray array = new JSONArray();
		JSONObject json = null;
		try {
			lista = cas.getCadastroPorUsuario(usc.getUsuario(Long.parseLong(usuario)));
			for (Cadastro c : lista){
				json = new JSONObject();
				json.put("id", c.getId());
				json.put("categoria", c.getCategoria());
				json.put("descricao", c.getDescricao());
				json.put("esta_online", c.getEstaOnline());
				json.put("usuario", c.getUsuario());
				array.put(json);
			}
			System.out.println(array.toString());
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	
	@GET
	@Path("/buscarLancamentoPorUsuario/{usuario}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Lancamento> buscarLancamentoPorUsuario(@PathParam("usuario") String usuario){
		List<Lancamento> lista = new ArrayList<>();
		JSONArray array = new JSONArray();
		JSONObject json = null;
		try {
			lista = lan.getLancamentoPorUsuario(usc.getUsuario(Long.parseLong(usuario)));
			for (Lancamento c : lista){
				json = new JSONObject();
				json.put("id", c.getId());
				json.put("categoria", c.getCategoria());
				json.put("descricao", c.getDescricao());
				json.put("esta_online", c.getEstaOnline());
				json.put("usuario", c.getUsuario());
				json.put("saldo", c.getSaldo());
				json.put("valor", c.getValor());
				json.put("data_inclusao", c.getDataInclusao());
				json.put("data_lancamento", c.getDataLancamento());
				json.put("deletado", c.getDeletado());
				json.put("e_futuro", c.geteFuturo());
				json.put("mes_ano", c.getMesAno());
				array.put(json);
			}
			System.out.println(array.toString());
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	
	
	@GET
	@Path("/buscarLancamento")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public List<Lancamento> buscarLancamento(){
		List<Lancamento> lista = new ArrayList<>();
		try {
			lista = lan.getLancamentos();

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lista;
	}
	
	/*******************************************************
	 * SALVAR
	 */
	@POST
	@Path("/salvarUsuario")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarUsuario(UsuarioControle usuario){
		String msg = "";
		UsuarioControle p = new UsuarioControle();
		try {
			System.out.println("data: "+usuario.getDataNascimento());
			p.setDataNascimento(usuario.getDataNascimento());
			p.setEmail(usuario.getEmail());
			p.setEstaOnline(usuario.getEstaOnline());
			p.setFazBackup(usuario.getFazBackup());
			p.setNome(usuario.getNome());
			p.setSenha(usuario.getSenha());
			p.setId(usuario.getId());
			
			usc.adicionarUsuario(p);
			//usc.salvarMerge(p);
			msg = "Produtor adicionado com sucesso! "+p.toString();
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@POST
	@Path("/salvarCadastro")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarCadastro(Cadastro cadastro){
		String msg = "";
		Cadastro p = new Cadastro();
		try {
			
			p.setCategoria(cadastro.getCategoria());
			p.setDescricao(cadastro.getDescricao());
			p.setEstaOnline(cadastro.getEstaOnline());
			p.setUsuario(cadastro.getUsuario());
			
			cas.adicionarCadastro(p);
			//usc.salvarMerge(p);
			msg = "Produtor adicionado com sucesso! "+p.toString();
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@POST
	@Path("/salvarLancamento")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String salvarLancamento(Lancamento l){
		String msg = "";
		Lancamento p = new Lancamento();
		try {
			System.out.println("Data >>> "+l.getDataLancamento());
			p.setCategoria(l.getCategoria());
			p.setDataInclusao(l.getDataInclusao());
			p.setDataLancamento(l.getDataLancamento());
			p.setDeletado(l.getDeletado());
			p.setDescricao(l.getDescricao());
			p.seteFuturo(l.geteFuturo());
			p.setEstaOnline(l.getEstaOnline());
			p.setMesAno(l.getMesAno());
			p.setUsuario(l.getUsuario());
			p.setSaldo(l.getSaldo());
			p.setValor(l.getValor());
			
			lan.adicionarLancamento(p);
			//usc.salvarMerge(p);
			msg = "Produtor adicionado com sucesso! "+p.toString();
		} catch (Exception e) {
			msg = "Erro ao adicionar produtor!";
			e.printStackTrace();
		}
		return msg;
	}
	
	@POST
	@Path("/atualizarLancamento")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String atualizarLancamento(String obj){
		String msg = "";
		; 
		try {
			JSONObject json = new JSONObject(obj);
			String usuario = json.get("usuario").toString();
			String data = json.get("dataLancamento").toString();
			String descricao = json.get("descricao").toString();
			Boolean deletado = Boolean.parseBoolean(json.getString("deletado").toString());
			BigDecimal valor = new BigDecimal(json.get("valor").toString());
			BigDecimal saldo = new BigDecimal(json.get("saldo").toString());
			BigDecimal valorAntigo = new BigDecimal(json.get("valorAntigo").toString());
			
			Lancamento lanc = lan.getLancamento(
					Long.parseLong(usuario),
					data,
					descricao,
					valorAntigo);
			
			lanc.setValor(valor);
			lanc.setSaldo(saldo);
			lanc.setDeletado(deletado);
			
			lan.atualizarLancamento(lanc);
		
			msg = "Lancamento atualizado com sucesso! "+lanc.toString();
		} catch (Exception e) {
			msg = "Erro ao Lancamento produtor!";
			e.printStackTrace();
		}
		return msg;
	}
	
}
