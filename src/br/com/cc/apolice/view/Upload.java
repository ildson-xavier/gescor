package br.com.cc.apolice.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.cc.apolice.servico.ApoliceService;
import br.com.cc.mail.Email;
import br.com.cc.util.JSFMessageUtil;
import br.com.cc.util.Util;

public abstract class Upload {

	private String diretorio;
	private String caminho;
	private byte[] arquivo;
	private String nome;
	private File file;
	private ApoliceService apoliceService;
	private Properties props;

	public Upload(ApoliceService apoliceService) {
		this.apoliceService = apoliceService;
	}

	public void upload(FileUploadEvent event, String diretorio) throws IOException {
		UploadedFile up = event.getFile();

		this.diretorio = "";
		this.nome = "";
		this.caminho = "";
		this.arquivo = null;

		this.nome = up.getFileName();
		this.caminho = diretorio + renomeArquivo(diretorio + up.getFileName());
		this.diretorio = diretorio;

		System.out.println("Nome: " + up.getFileName());
		System.out.println("Caminho: " + this.caminho);
		System.out.println("Diretorio: " + this.diretorio);
		System.out.println("RealPath:" + getRealPath());
		System.out.println("Up: " + up.getFileName());

		this.arquivo = IOUtils.toByteArray(up.getInputstream());
		System.out.println("Arquivo: " + this.arquivo);

		gravar(this.caminho, this.arquivo);
	}

	public String renomeArquivo(String arquivo) {
		File arquivo1 = new File(arquivo);
		String extensao = ".pdf";
		int posicaoFinal = arquivo.indexOf(extensao);
		
		String nova = arquivo.substring(0, posicaoFinal);
		nova += "_" + Util.extensaoData() + extensao;

		File arquivo2 = new File(nova);
		arquivo1.renameTo(arquivo2);
		return arquivo2.getName();
	}

	public void gravar(String caminho, byte[] arquivo) throws FileNotFoundException, IOException {
		FileOutputStream fos;
		fos = new FileOutputStream(caminho);

		fos.write(arquivo);
		fos.flush();
		fos.close();
	}

	public void semImplementacao(String seguradora) {
		props = new Properties();

		try {
			carregarArquivos();
			
			if (this.file != null) {
				props.load(getClass().getResourceAsStream("/mail.properties"));

				new Email().enviarEmail(props.getProperty("mail.destino").toString(), " ", "Apolice " + seguradora,
						" Implementar nova apolice ", this.caminho);
				apagarArquivoTemporario(this.caminho);

				JSFMessageUtil.sendAlertMessageToUser(
						"A importação da apolice da seguradora " + seguradora
								+ " está em processo de implementação. Em menos de 20 dias já estará pronto e você será avisado(a). "
								+ "Enquanto isso, por favor, uso o cadastro manual de apolices.",
						"");
			} else {
				JSFMessageUtil.sendAlertMessageToUser("Não há arquivo para ser processado.", "");
			}

		} catch (Exception ex) {
			JSFMessageUtil.sendFatalMessageToUser("Falha ao tentar importar a apolice.", "Tente novamente mais tarde");
			ex.printStackTrace();
		}
	}

	public void processarPdf(String seguradora) {
		System.out.println("processarPdf");
		props = new Properties();
		try {
			carregarArquivos();
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Antes de processar o arquivo é preciso fazer upload ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		try {
			props.load(getClass().getResourceAsStream("/mail.properties"));
			if (this.file != null) {

				apoliceService.cadastrarApolice(this.caminho);

				apagarArquivoTemporario(this.caminho + ".tmp");

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Arquivo [" + this.nome + "] processado com sucesso!", " ");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO!",
						"Não há arquivo para ser processado. ");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}

		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Nao foi possivel processar arquivo " + Util.nomeArquivo(this.caminho) + ".   " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			new Email().enviarEmail(props.getProperty("mail.destino").toString(), " ", "Apolice " + seguradora,
					" Exception: " + ex.getMessage(), this.caminho);
			apagarArquivoTemporario(this.caminho);
			ex.printStackTrace();
		}
	}

	public void apagarArquivoTemporario(String path) {
		try {
			File f = new File(path);
			if (!f.delete()) {
				System.out.println("Falha ao deletar arquivo: " + path);
			}
			if (f.isFile()) {
				System.out.println("O arquivo existe: " + path);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void carregarArquivos() throws Exception {
		if (!this.diretorio.equals("")) {
			file = new File(this.diretorio);
		} else {
			throw new Exception("Antes de processar o arquivo é preciso fazer upload!");
		}
	}

	public String getRealPath() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		FacesContext faceContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) faceContext.getExternalContext().getContext();

		return context.getRealPath("/");
	}
}
