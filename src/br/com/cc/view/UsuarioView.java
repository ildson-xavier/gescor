/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabCloseEvent;

import br.com.cc.converter.ConversorPerfil;
import br.com.cc.dao.CorretoraDao;
import br.com.cc.dao.FuncionalidadeDAO;
import br.com.cc.dao.PerfilDAO;
import br.com.cc.dao.UsuarioDao;
import br.com.cc.entidade.Funcionalidade;
import br.com.cc.entidade.Perfil;
import br.com.cc.entidade.Usuario;
import br.com.cc.service.TemaService;
import br.com.cc.util.JSFMessageUtil;

/**
 *
 * @author Usuario
 */
@ManagedBean(name = "usuarioView")
@ViewScoped
public class UsuarioView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Usuario> usuarios = new LinkedList<Usuario>();
	private Usuario usuarioBean = new Usuario();
	private Usuario usuarioLogador;
	@ManagedProperty(value = "#{usuarioLoginBean}")
	private UsuarioLoginBean usuarioLoginBean;
	@ManagedProperty(value = "#{corretoraView}")
	private CorretoraView corretoraView;

	private List<Perfil> perfis = new LinkedList<>();
	private Perfil perfilBean;
	private String descricao;
	private ConversorPerfil conversorPerfil = new ConversorPerfil();

	private UsuarioDao usuarioDao;
	private FuncionalidadeDAO funcionalidadeDAO;
	private PerfilDAO perfilDAO;
	private CorretoraDao corretoraDao;

	private boolean desabilitado = true;
	private static int indexTab;

	private String tab;

	private List<String> temas;
	@ManagedProperty("#{temaService}")
	private TemaService service;
	private String tema;

	@PostConstruct
	public void init() {
		temas = new ArrayList<>();
		temas.add("afternoon");
		temas.add("start");
		temas.add("excite-bike");
		temas.add("black-tie");
		temas.add("blitzer");
		temas.add("bluesky");
		temas.add("bootstrap");
		temas.add("casablanca");
		temas.add("cruze");
		temas.add("cupertino");
		temas.add("dark-hive");
		temas.add("delta");
		temas.add("flick");
		temas.add("glass-x");
		temas.add("humanity");
		temas.add("overcast");
		temas.add("rocket");
		temas.add("sunny");
		temas.add("vader");

		tema = service.getTemaEscolhido();
	}

	public UsuarioView() {
		iniciarObjetos();
	}

	private void iniciarObjetos() {
		if (corretoraDao == null) {
			corretoraDao = new CorretoraDao();
		}
		if (perfilDAO == null) {
			perfilDAO = new PerfilDAO();
		}

		if (usuarioDao == null) {
			usuarioDao = new UsuarioDao();
		}

		if (funcionalidadeDAO == null) {
			funcionalidadeDAO = new FuncionalidadeDAO();
		}
	}

	public boolean isAdmin() {
		return true;
	}

	public boolean isConsulto() {
		return true;
	}

	public String adicionarUsuario() {
		usuarioBean = new Usuario();
		indexTab = 0;
		listarUsuarios();
		return "cadastrarusuario.jsf?faces-redirect=true";
	}

	public String mostrarUsuarioNaoAdm() {
		return "usuario.jsf?faces-redirect=true";
	}

	public void atualizarCorretora() {
		System.out.println("atualizarCorretora");
		try {
			corretoraDao.atualizarCorretora(this.usuarioLoginBean.getUsuarioLogador().getCorretora());
			JSFMessageUtil.sendInfoMessageToUser("Corretora atualizada com sucesso.", "");
			this.desabilitado = true;
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar atualizar o cadastro da corretora.", e.getMessage());
			e.printStackTrace();
		}
	}

	public void atualizarUsuarioNaoAdm() {
		System.out.println("atualizarUsuarioNaoAdm");
		try {
			this.usuarioLoginBean.getUsuarioLogador().setTema(this.tema);
			usuarioDao.atualizarUsuario(this.usuarioLoginBean.getUsuarioLogador());
			this.desabilitado = true;
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Não foi posssivel atualizar o usuario: " + this.usuarioLoginBean.getUsuarioLogador().getNome());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.desabilitado = true;
			e.printStackTrace();
		}
	}

	public void updateTema() {
		System.out.println("Tema: " + this.tema);
		service.setTemaEscolhido(this.tema);
	}

	private boolean semPreencher(String campo) {
		if (campo == null || campo.equals("")) {
			return true;
		}
		return false;
	}

	public String savarUsuario() {
		System.out.println("savarUsuario");
		if (semPreencher(usuarioBean.getNome()) || semPreencher(usuarioBean.getLogin())
				|| semPreencher(usuarioBean.getSenha()) || semPreencher(usuarioBean.getEmail())
				|| usuarioBean.getPerfil() == null) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção. ",
					"Por favor, preencha todos os campos.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			try {
				usuarioBean.setTema(getTema());
				service.setTemaEscolhido(getTema());
				usuarioBean.setHierarquia(2);
				usuarioBean.setIdPai(this.usuarioLoginBean.getUsuarioLogador().getId());
				usuarioDao.adicionarUsuario(usuarioBean);
				usuarios.add(usuarioBean);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, " ",
						"Usuário [" + usuarioBean.getNome() + "] cadastrado com sucesso.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				usuarioBean = new Usuario();
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
						"Não foi posssivel cadastrar o usuario");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				e.printStackTrace();
			}
			listarUsuarios();
			indexTab = 0;
		}

		return "cadastrarusuario";
	}

	public void inicializarPerfis() {
		perfilBean = new Perfil();
		listarUsuarios();
		if (getPerfis().isEmpty()) {
			this.perfis = perfilDAO.getPerfis();
		}
	}

	public String adicionarPerfil() {
		System.out.println("adicionarPerfil");
		Funcionalidade func;
		if (!getDescricao().equals("")) {
			try {
				perfilBean.setDescricao(descricao.toUpperCase());
				// Recupera um perfil ADM
				Perfil adm = perfilDAO.getPerfiPorDescricao("ADMINISTRADOR");

				// Salva o perfil
				Perfil bean = perfilDAO.salvarPerfil(perfilBean);

				// Recupera todas as funcionalidade do perfil ADM
				List<Funcionalidade> lista = funcionalidadeDAO.getFuncionalidadesPorPerfil(adm);

				// Deixa as funcionalidades inativas
				for (Funcionalidade f : lista) {
					func = new Funcionalidade();
					func.setAtivo(false);
					func.setDescricao(f.getDescricao());
					func.setIndice(f.getIndice());
					func.setPagina(f.getPagina());
					func.setPerfil(bean);

					// Atualiza a funcionalidade
					funcionalidadeDAO.salvar(func);
				}

				// Carrega a lista de perfis
				this.perfis = new ArrayList<>();
				inicializarPerfis();
				indexTab = 1;
			} catch (Exception e) {
				JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar adicionar novo perfil.", "");
				e.printStackTrace();
			}

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ", "Informe o perfil");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return "cadastrarusuario";
		}
		return "cadastrarusuario.jsf?faces-redirect=true";
	}

	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		System.out.println("onEdit");
		try {
			System.out.println("Perfil: " + ((Usuario) event.getObject()));
			Usuario us = ((Usuario) event.getObject());
			if (tema != null && !tema.equals("")) {
				us.setTema(tema);
			}

			usuarioDao.atualizarUsuario(us);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Usuario) event.getObject()).getNome());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Usuario) event.getObject()).getNome() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Usuario) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onEditFunc(RowEditEvent event) {
		System.out.println("onEditFunc");
		System.out.println("Perfil: " + ((Funcionalidade) event.getObject()).getDescricao());
		try {
			System.out.println("Perfil: " + ((Funcionalidade) event.getObject()));
			funcionalidadeDAO.salvarFuncionalidade((Funcionalidade) event.getObject());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Funcionalidade) event.getObject()).getPagina());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Funcionalidade) event.getObject()).getPagina() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancelFunc(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Funcionalidade) event.getObject()).getPagina());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void removerUsuario() {
		System.out.println("removerUsuario");
		try {
			usuarioDao.removerUsuario(usuarioBean);
			usuarios.remove(usuarioBean);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, " ",
					"Usuário[" + usuarioBean.getNome() + "] removido com sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			usuarioBean = new Usuario();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Não foi posssivel apagar o usuario [" + usuarioBean.getNome() + "]");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
	}

	public void listarUsuarios() {
		System.out.println("listarUsuarios");
		try {
			// if (usuarios.isEmpty()) {
			usuarios = usuarioDao.getUsuarios(this.usuarioLoginBean.getUsuarioLogador());
			// }
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Não foi posssivel listar os usuarios");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
		// return "/ccadmin/listarusuarios.jsf?faces-redirect=true";
	}

	public void onTabClose(TabCloseEvent event) {
		System.out.println("onTabClose");
		this.tab = event.getTab().getTitle();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Exclusão: ",
				"Confirmar a exclusão do perfil " + event.getTab().getTitle() + "?");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void excluirPerfil() {
		System.out.println("excluirPerfil");
		String usuario = "";
		String descricao = "";
		List<Usuario> usuarios = new ArrayList<>();
		Perfil perfil = perfilDAO.getPerfiPorDescricao(this.tab);
		usuarios = usuarioDao.getUsuariosPorPerfil(perfil);
		if (!usuarios.isEmpty()) {
			for (Usuario us : usuarios) {
				usuario += us.getNome() + "; ";
			}

			usuario = usuario.substring(0, usuario.length() - 2);

			if (usuarios.size() > 1) {
				descricao = "O perfil " + this.tab + " está associado a " + usuarios.size() + " usuarios [" + usuario
						+ "]. ";
			} else {
				descricao = "O perfil " + this.tab + " está associado a " + usuarios.size() + " usuario [" + usuario
						+ "]. ";
			}

			JSFMessageUtil.sendErrorMessageToUser("Atenção!", descricao + "O perfil não poderá ser excluido.");
		} else {
			try {
				// exclui a funcionalidade
				List<Funcionalidade> lista = funcionalidadeDAO.getFuncionalidadesPorPerfil(perfil);
				if (lista != null && !lista.isEmpty()) {
					for (Funcionalidade f : lista) {
						funcionalidadeDAO.remover(f);
					}
				}

				// exclui o perfil
				perfilDAO.remover(perfil);

				// Recarrega a lista de perfis
				this.perfis = perfilDAO.getPerfis();

			} catch (Exception e) {
				JSFMessageUtil.sendFatalMessageToUser("ERRO!", "Não foi possivel excluir o perfil " + perfil);
				e.printStackTrace();
			}
		}

	}

	public void habilitar() {
		this.desabilitado = false;
	}

	public Usuario getUsuarioBean() {
		return usuarioBean;
	}

	public void setUsuarioBean(Usuario usuarioBean) {
		this.usuarioBean = usuarioBean;
	}

	public Usuario getUsuarioLogador() {
		return usuarioLogador;
	}

	public void setUsuarioLogador(Usuario usuarioLogador) {
		this.usuarioLogador = usuarioLogador;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public UsuarioLoginBean getUsuarioLoginBean() {
		return usuarioLoginBean;
	}

	public void setUsuarioLoginBean(UsuarioLoginBean usuarioLoginBean) {
		this.usuarioLoginBean = usuarioLoginBean;
	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public ConversorPerfil getConversorPerfil() {
		return conversorPerfil;
	}

	public void setConversorPerfil(ConversorPerfil conversorPerfil) {
		this.conversorPerfil = conversorPerfil;
	}

	public Perfil getPerfilBean() {
		return perfilBean;
	}

	public void setPerfilBean(Perfil perfilBean) {
		this.perfilBean = perfilBean;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getIndexTab() {
		return indexTab;
	}

	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	public void setIndexTab(int indexTab) {
		this.indexTab = indexTab;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public List<String> getTemas() {
		return temas;
	}

	public void setTemas(List<String> temas) {
		this.temas = temas;
	}

	public TemaService getService() {
		return service;
	}

	public void setService(TemaService service) {
		this.service = service;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public CorretoraView getCorretoraView() {
		return corretoraView;
	}

	public void setCorretoraView(CorretoraView corretoraView) {
		this.corretoraView = corretoraView;
	}

}
