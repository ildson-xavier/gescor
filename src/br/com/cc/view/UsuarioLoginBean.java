package br.com.cc.view;

import java.io.IOException;

import java.io.Serializable;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;

import br.com.cc.dao.PerfilDAO;
import br.com.cc.dao.UsuarioDao;
import br.com.cc.entidade.Funcionalidade;
import br.com.cc.entidade.Perfil;
import br.com.cc.entidade.Usuario;
import br.com.cc.mail.Email;
import br.com.cc.mail.template.Template;
import br.com.cc.service.TemaService;
import br.com.cc.util.JSFMessageUtil;
import br.com.cc.util.Util;

@ManagedBean(name = "usuarioLoginBean")
@SessionScoped
public class UsuarioLoginBean extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private Usuario usuarioLogador;
	private final UsuarioDao usuarioDao = new UsuarioDao();
	private Usuario usuarioBean = new Usuario();

	@org.hibernate.validator.constraints.Email(message = "Informe um e-mail válido")
	private String email;

	private Properties props;

	@ManagedProperty("#{temaService}")
	private TemaService service;

	public String LoginUsuario() {
		Util.verificaMemoria("ccomissao-LoginUsuario");

		try {
			setUsuarioLogador(usuarioDao.getUsuarioLogin(getUsuarioBean().getLogin(), getUsuarioBean().getSenha()));

			System.out.println("Logando no GESCOR...");
			System.out.println("Usuario logado: ");
			System.out.println("Nome.......: " + getUsuarioLogador().getNome());
			System.out.println("Login......: " + getUsuarioLogador().getLogin());
			System.out.println("Hierarquia.: " + getUsuarioLogador().getHierarquia());

			getService().setTemaEscolhido(getUsuarioLogador().getTema());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("logado", getUsuarioLogador());

			return "/ccadmin/graficoview.jsf?faces-redirect=true";

		} catch (NoResultException ex) {
			JSFMessageUtil.sendAlertMessageToUser("Usuário ou senha invalidos.", "Tente novamente.");
			setUsuarioBean(new Usuario());
			System.out.println("Usuário ou senha invalidos." + ex.getMessage());
			//return "/login.jsf";
			return "/ccadmin/graficoview.jsf?faces-redirect=true";
		} catch (Exception e) {
			JSFMessageUtil.sendFatalMessageToUser("Erro ao tentar logar no sistema", "");
			setUsuarioBean(new Usuario());

			new Email().enviarEmail(
					"ildson.moraes@gmail.com", null, "Erro ao tentar logar no sistema. " + "Login: "
							+ getUsuarioBean().getLogin() + "Senha: " + getUsuarioBean().getSenha(),
					e.getMessage(), null);

			System.out.println("Erro ao tentar logar no sistema. " + "Login: " + getUsuarioBean().getLogin() + "Senha: "
					+ getUsuarioBean().getSenha() + e.getMessage());
			//return "/login.jsf";
			return "/ccadmin/graficoview.jsf?faces-redirect=true";
		}
	}

	public String logOut() {

		System.out.println("Deslogando no GESCOR...");
		System.out.println("Usuario deslogado: ");
		System.out.println("Nome.......: " + getUsuarioLogador().getNome());
		System.out.println("Login......: " + getUsuarioLogador().getLogin());
		System.out.println("Hierarquia.: " + getUsuarioLogador().getHierarquia());

		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sessao.invalidate();
		return "/login.jsf?faces-redirect=true";
	}

	public boolean autorizador(String indice) {
		if (getUsuarioLogador() != null && getUsuarioLogador().getPerfil() != null
				&& getUsuarioLogador().getPerfil().getFuncionalidades().size() > 0) {
			for (Funcionalidade f : getUsuarioLogador().getPerfil().getFuncionalidades()) {
				if (f.getAtivo()) {
					if (f.getIndice().equals(indice)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void validarEmail() {
		try {
			this.usuarioBean = usuarioDao.getUsuarioEmail(this.email);
			if (this.usuarioBean.getId() > 0) {
				enviarEmail();
			}

		} catch (NoResultException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "E-mail não encontrado. ",
					"E-mail informado não foi encontrado no cadastrado do sistema");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro!",
					"Não foi possivel localizar o e-mail");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
	}

	public String voltarLogin() {
		return "/login.jsf?faces-redirect=true";
	}

	private void enviarEmail() {
		props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("/mail.properties"));
			String conteudo = "Login: " + usuarioBean.getLogin() + "<br/>" + "Senha: " + usuarioBean.getSenha()
					+ "<br/>";
			new Email().enviarEmail(this.usuarioBean.getEmail().trim(), null, "Recuperação de senha", conteudo, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String salvarNovoUsuario() {
		System.out.println("salvarNovoUsuario");
		props = new Properties();
		try {

			Usuario usuario = usuarioDao.getUsuarioEmail(this.usuarioBean.getEmail());
			Usuario login = usuarioDao.getUsuarioLogin(this.usuarioBean.getLogin());
			if (usuario == null && login == null) {

				props.load(getClass().getResourceAsStream("/mail.properties"));
				PerfilDAO perfilDAO = new PerfilDAO();
				// Salvar como usuario administrador
				Perfil perfil = perfilDAO.getPerfil(1);
				this.usuarioBean.setPerfil(perfil);
				this.usuarioBean.setTema("excite-bike");
				this.usuarioBean.setHierarquia(1);
				this.usuarioDao.salvarMerge(this.usuarioBean);

				new Email().enviarEmail(props.getProperty("mail.destino").toString(), 
						props.getProperty("mail.copia").toString(), "Novo Cliente",
						Template.emailUsuarioNovo(this.usuarioBean.toString()), null);

				return voltarLogin();
			} else {
				if (usuario != null) {
					JSFMessageUtil.sendAlertMessageToUser(
							"O e-mail informado já está cadastrado. Por favor, informe outro e-mail.", "");
				}
				if (login != null) {
					JSFMessageUtil
							.sendAlertMessageToUser("O login informado já existe. Por favor, informe outro login.", "");
				}

			}

		} catch (PersistenceException e) {
			JSFMessageUtil.sendAlertMessageToUser(
					"Ocorreu um problema ao tentar salvar seus dados. Por favor, tente novamente mais tarde", "");
			new Email().enviarEmail(props.getProperty("mail.destino").toString(), 
					props.getProperty("mail.copia").toString(),
					"Erro: Novo Cliente " + Template.emailUsuarioNovo(e.getMessage()), this.usuarioBean.toString(),
					null);
			e.printStackTrace();

		} catch (Exception e) {
			JSFMessageUtil.sendAlertMessageToUser(
					"Ocorreu um problema ao tentar salvar seus dados. Por favor, tente novamente mais tarde", "");
			new Email().enviarEmail(props.getProperty("mail.destino").toString(), 
					props.getProperty("mail.copia").toString(),
					"Erro: Novo Cliente " + Template.emailUsuarioNovo(e.getMessage()), this.usuarioBean.toString(),
					null);
			e.printStackTrace();

		}
		return "/experimente.jsf";
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public TemaService getService() {
		return service;
	}

	public void setService(TemaService service) {
		this.service = service;
	}
}
