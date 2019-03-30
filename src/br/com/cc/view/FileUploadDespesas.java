package br.com.cc.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.cc.excel.DespesasXls;
import br.com.cc.mail.Email;

@ManagedBean
@ViewScoped
public class FileUploadDespesas implements Serializable {

	private static final long serialVersionUID = 1L;

	private DespesasXls despesasXls = new DespesasXls();
	private String diretorio;
	private String[] lstArquivos = new String[100];
	private String caminho;
	private byte[] arquivo;
	private String nome;
	private int flagHabilita;

	public FileUploadDespesas() {

	}

	public void handleFileUpload(FileUploadEvent event) {
		if (event != null) {
			try {
				upload(event, "resources/despesa/");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload do arquivo ",
						event.getFile().getFileName() + " realizado com sucesso");
				FacesContext.getCurrentInstance().addMessage(null, message);
				flagHabilita = 1;
			} catch (IOException ex) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro fazer upload do arquivo ",
						event.getFile().getFileName() + " " + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
				ex.printStackTrace();
			}
		}
	}

	public void processarPlanilha() {
		try {
			carregarArquivos();
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Antes de processar o arquivo é preciso fazer upload ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		String path = "";
		try {
			if (lstArquivos.length > 0) {
				for (String arq : lstArquivos) {

					System.out.println("Arquivo: " + arq);
					path = diretorio + arq;
					despesasXls.importarDespesas(path);
					apagarArquivoTemporario(path);

					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Arquivo [" + arq + "] processado com sucesso!", " ");
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO!",
						"Não há arquivo para ser processados ");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Nao foi possivel processar planilha excel " + path + "   " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			new Email().enviarEmail("ildson.moraes@gmail.com","", "Despesas", "Exception: " + ex.getMessage(), path);
			apagarArquivoTemporario(path);
			ex.printStackTrace();
		}
	}

	public void upload(FileUploadEvent event, String diretorio) throws IOException {
		UploadedFile up = event.getFile();

		this.diretorio = "";
		this.nome = "";
		this.caminho = "";
		this.arquivo = null;

		this.nome = up.getFileName();
		this.caminho = getRealPath() + diretorio + getNome();
		this.diretorio = getRealPath() + diretorio;

		System.out.println("Nome: " + this.nome);
		System.out.println("Caminho: " + this.caminho);
		System.out.println("Diretorio: " + this.diretorio);
		System.out.println("RealPath:" + getRealPath());
		System.out.println("Up: " + up.getFileName());

		this.arquivo = IOUtils.toByteArray(up.getInputstream());
		System.out.println("Arquivo: " + this.arquivo);
		gravar();
	}

	public void gravar() throws FileNotFoundException, IOException {
		FileOutputStream fos;
		fos = new FileOutputStream(this.caminho);

		fos.write(this.arquivo);
		fos.flush();
		fos.close();
	}

	
	public String getRealPath() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		FacesContext faceContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) faceContext.getExternalContext().getContext();

		return context.getRealPath("/");
	}

	public void carregarArquivos() throws Exception {
		if (!this.diretorio.equals("")) {
			File file = new File(this.diretorio);
			lstArquivos = file.list();
		} else {
			throw new Exception("Antes de processar o arquivo é preciso fazer upload!");
		}
	}

	public void apagarArquivoTemporario(String path) {
		File f = new File(path);
		f.delete();
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}

	public String[] getLstArquivos() {
		return lstArquivos;
	}

	public void setLstArquivos(String[] lstArquivos) {
		this.lstArquivos = lstArquivos;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getFlagHabilita() {
		return flagHabilita;
	}

	public void setFlagHabilita(int flagHabilita) {
		this.flagHabilita = flagHabilita;
	}

}
