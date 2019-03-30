/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import br.com.cc.dao.LancamentosDao;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.excel.PortoSeguro;
import br.com.cc.mail.Email;
import br.com.cc.mail.EnvioEmail;
import br.com.cc.mail.EnvioEmail2;
import br.com.cc.mail.template.Template;
import br.com.cc.util.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Usuario
 */
@ManagedBean
@ViewScoped
public class FileUploadView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PortoSeguro psExcel = new PortoSeguro();
	private final LancamentosDao lancamentosDao = new LancamentosDao();
	private List<Lancamentos> lancs;
	private final LancamentosView lancvw = new LancamentosView();
	private String diretorio;
	private String[] lstArquivos = new String[100];
	private String caminho;
	private byte[] arquivo;
	private String nome;
	private Date dataReferencia;
	private String seguradora;
	private Double pertencent = 8.70;
	private int flagHabilita = 0;

	private Email email = new Email();

	/**
	 * Creates a new instance of FileUploadView
	 */
	public FileUploadView() {
		// Util.verificaMemoria("ccomissao");
		seguradora = "PortoSeguro";
	}

	public void handleFileUpload(FileUploadEvent event) {
		System.out.println("handleFileUpload");
		if (event != null) {
			try {
				upload(event, "resources/planilhas/");
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
		System.out.println("processarPlanilha");
		try {
			carregarArquivos();
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN«√O! ",
					"Antes de processar o arquivo È preciso fazer upload ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		String path = "";
		try {
			if (lstArquivos.length > 0) {
				for (String arq : lstArquivos) {
					lancs = new LinkedList<Lancamentos>();
					System.out.println("Arquivo: " + arq);
					path = diretorio + arq;
					lancs = psExcel.importarPlanilha(path, seguradora, pertencent);
					lancvw.adicionarLancamentos(lancs);
					apagarArquivoTemporario(path);

					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Arquivo [" + arq + "] processado com sucesso!", " ");
					FacesContext.getCurrentInstance().addMessage(null, message);
				}
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN«√O!",
						"N„o h· arquivo para ser processados ");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (Exception ex) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN«√O! ",
					"Nao foi possivel processar arquivo " + Util.nomeArquivo(path) + ".   " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			new Email().enviarEmail("ildson.moraes@gmail.com", "", seguradora,
					"Exception: " + Template.emailMachadoPaiaro(ex.getMessage()), path);
			apagarArquivoTemporario(path);
			ex.printStackTrace();
		}
	}

	public void upload(FileUploadEvent event, String diretorio) throws IOException {
		System.out.println("upload");
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
		System.out.println("gravar");
		FileOutputStream fos;
		fos = new FileOutputStream(this.caminho);

		fos.write(this.arquivo);
		fos.flush();
		fos.close();
	}

	public void carregarArquivos() throws Exception {
		System.out.println("carregarArquivos");
		System.out.println("Caminho: " + this.caminho);
		if (!this.diretorio.equals("")) {
			File file = new File(this.diretorio);
			lstArquivos = file.list();
		} else {
			throw new Exception("Antes de processar o arquivo È preciso fazer upload!");
		}
	}

	public void apagarArquivoTemporario(String path) {
		File f = new File(path);
		f.delete();
	}

	public String getRealPath() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		FacesContext faceContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) faceContext.getExternalContext().getContext();

		return context.getRealPath("/");
	}

	public void addMessageSeguradora() {
		if (seguradora != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Seguradora: ", "" + seguradora));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN√á√ÉO! ", "Seleciona a seguradora."));
		}
	}

	public void addMessagePercent() {
		if (pertencent != 8.21) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN«√O! ",
							"Ao alterar o valor [" + 8.21 + "] padr√£o da percentagem do imposto sobre o a comiss√£o, "
									+ "o valor do pr√™mio l√≠quido da comiss√£o ser√° alterado. \n" + pertencent));
		}
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
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

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public String getSeguradora() {
		return seguradora;
	}

	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}

	public Double getPertencent() {
		return pertencent;
	}

	public void setPertencent(Double pertencent) {
		this.pertencent = pertencent;
	}

	public String[] getLstArquivos() {
		return lstArquivos;
	}

	public void setLstArquivos(String[] lstArquivos) {
		this.lstArquivos = lstArquivos;
	}

	public int getFlagHabilita() {
		return flagHabilita;
	}

	public void setFlagHabilita(int flagHabilita) {
		this.flagHabilita = flagHabilita;
	}

}
