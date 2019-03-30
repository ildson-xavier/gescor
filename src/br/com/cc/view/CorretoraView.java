package br.com.cc.view;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.cc.dao.CorretoraDao;
import br.com.cc.dao.UsuarioDao;
import br.com.cc.entidade.Corretora;
import br.com.cc.entidade.Usuario;
import br.com.cc.mail.Email;
import br.com.cc.mail.template.Template;
import br.com.cc.service.cep.ServicoCep;
import br.com.cc.util.JSFMessageUtil;
import br.com.cc.valida.ValidaCNPJ;
import br.com.cc.valida.ValidaCPF;
import br.com.gescor.cobranca.util.bopepo.BopepoEmissorBoleto;

@ManagedBean(name = "corretoraView")
@ViewScoped
public class CorretoraView implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UsuarioDao usuarioDao = new UsuarioDao();
	private final CorretoraDao corretoraDao = new CorretoraDao();

	@ManagedProperty(value = "#{usuarioLoginBean}")
	private UsuarioLoginBean usuarioLoginBean;
	private Corretora corretora;
	private Usuario usuario;
	private ServicoCep cep;
	private BopepoEmissorBoleto bopepo;
	private boolean situacao;

	private Properties props;

	private String mensagem;

	public CorretoraView() {
		setCorretora(new Corretora());
		usuario = new Usuario();
		bopepo = new BopepoEmissorBoleto();
		criarMensagem();
	}

	private void criarMensagem() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
		Calendar dataAtual = Calendar.getInstance(new Locale("pt", "BR"));
		Calendar dataPagamento = Calendar.getInstance(new Locale("pt", "BR"));
		dataPagamento.add(Calendar.DAY_OF_MONTH, 5);
		String data = df.format(dataPagamento.getTime());
		int dia = dataAtual.get(Calendar.DAY_OF_MONTH);
		this.mensagem = " Boleto de adesão com vencimento em " + data + ". O vencimento dos boletos mensais serão "
				+ "nos dias " + dia + " de cada mês.";
	}

	public String novaAssinatura() {
		return "novaassinatura.jsf?faces-redirect=true";
	}

	public void salvarCorretora() {
		System.out.println("salvarCorretora");
		props = new Properties();
		try {
			Corretora corr = corretoraDao.adicionarCorretora(corretora);
			usuario = usuarioLoginBean.getUsuarioLogador();
			usuario.setCorretora(corr);
			usuarioDao.salvarMerge(usuario);
			corretora = new Corretora();

			props.load(getClass().getResourceAsStream("/mail.properties"));

			new Email().enviarEmail(corr.getEmailCobranca(), props.getProperty("mail.copia").toString(),
					"Obrigado por assinar o GesCor! Estamos aqui para te ajudar!",
					Template.emailNovaAssinatura(this.usuario.getNome()), null);

			JSFMessageUtil.sendInfoMessageToUser("Assinatura realizada com sucesso!",
					"Vamos enviar um boleto para seu e-mail. Após a confirmação do pagamento, será liberado o acesso ao GESCOR.");
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar salvar os dados da corretora", e.getMessage());
			e.printStackTrace();
		}
	}

	public void obterCep() {
		System.out.println("obterCep");
		try {
			cep = new ServicoCep(this.corretora.getCep());
			Corretora corr = cep.obterDados();
			this.corretora.setBairro(corr.getBairro());
			this.corretora.setCidade(corr.getCidade());
			this.corretora.setEndereco(corr.getEndereco());
			this.corretora.setUf(corr.getUf());
		} catch (Exception e) {
			JSFMessageUtil.sendAlertMessageToUser("Cep não localizado.", e.getMessage());
			e.printStackTrace();
		}
	}

	public void validarIdentidade() {
		System.out.println("validarIdentidade");
		boolean status = false;
		String identidade = this.corretora.getCpfCnpj().replace(".", "").replace(".", "").replace(".", "")
				.replace("/", "").replaceAll("-", "");
		if (identidade.length() <= 11) {
			status = ValidaCPF.isCPF(identidade);
			if (status == false) {
				JSFMessageUtil.sendAlertMessageToUser("CPF informado está inválido.", "");
			}
		} else {
			status = ValidaCNPJ.isCNPJ(identidade);
			if (status == false) {
				JSFMessageUtil.sendAlertMessageToUser("CNPJ informado está inválido.", "");
			}
		}
	}

	private void gerarBoleto() {

	}

	public Corretora getCorretora() {
		return corretora;
	}

	public void setCorretora(Corretora corretora) {
		this.corretora = corretora;
	}

	public UsuarioLoginBean getUsuarioLoginBean() {
		return usuarioLoginBean;
	}

	public void setUsuarioLoginBean(UsuarioLoginBean usuarioLoginBean) {
		this.usuarioLoginBean = usuarioLoginBean;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isSituacao() {
		return situacao;
	}

	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
