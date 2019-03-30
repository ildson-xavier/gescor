/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import br.com.cc.entidade.Lancamentos;
import br.com.cc.mail.Email;
import br.com.cc.pdf.PDFSulAmerica;
import br.com.cc.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

/**
 *
 * @author Usuario
 */
@ManagedBean
@ViewScoped
public class FileUploadPDFSulAmerica implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String seguradora;
    private Double pertencent = 8.70;
    private String diretorio;
    private String caminho;
    private byte[] arquivo;
    private String nome;
    private String[] lstArquivos = new String[100];
    private List<Lancamentos> lancs = new LinkedList<Lancamentos>();
    private PDFSulAmerica sul = new PDFSulAmerica();
    private LancamentosView lancvw = new LancamentosView();
    private int flagHabilita;

    /**
     * Creates a new instance of FileUploadPDFAzul
     */
    public FileUploadPDFSulAmerica() {
        //Util.verificaMemoria("ccomissao");
        seguradora = "SulAmerica";
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (event != null) {
            try {
                upload(event, "resources/pdfsulamerica/");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload do arquivo ", event.getFile().getFileName() + " realizado com sucesso");
                FacesContext.getCurrentInstance().addMessage(null, message);
                flagHabilita = 1;
            } catch (IOException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!",
                        " N�o foi possivel fazer upload do arquivo " + event.getFile().getFileName() + " " + ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, message);
                ex.printStackTrace();
            }
        }
    }

    public void processarPdf() {
        try {
            carregarArquivos();
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN��O! ",
                    "Antes de processar o arquivo � preciso fazer upload ");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }                
        
        String path = "";
        try {
            if (lstArquivos.length > 0) {
                for (String arq : lstArquivos) {
                    System.out.println("Arquivo: " + arq);
                    path = diretorio + arq;
                    lancs = sul.lerPdfSulAmerica(path, seguradora, pertencent);
                    lancvw.adicionarLancamentos(lancs);
                    apagarArquivoTemporario(path);
                    lancs = new ArrayList<Lancamentos>();

                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo [" + arq + "] processado com sucesso!", " ");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN��O!", "N�o h� arquivo para ser processados ");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } catch (Exception ex) {
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATEN��O! ", 
            		"Nao foi possivel processar arquivo " 
            + Util.nomeArquivo(path) + ".   " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            new Email().enviarEmail("ildson.moraes@gmail.com","",seguradora, "Exception: " + ex.getMessage(), path);
            apagarArquivoTemporario(path);
            ex.printStackTrace();
        }
    }

    public void upload(FileUploadEvent event, String diretorio) throws IOException {
        UploadedFile up = event.getFile();

        this.diretorio = "";

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

    public void carregarArquivos() throws Exception {
        if (!this.diretorio.equals("")) {
            File file = new File(this.diretorio);
            lstArquivos = file.list();
        } else {
            throw new Exception("Antes de processar o arquivo é preciso fazer upload!");
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

    public String getRealPath() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        FacesContext faceContext = FacesContext.getCurrentInstance();
        ServletContext context = (ServletContext) faceContext.getExternalContext().getContext();

        return context.getRealPath("/");
    }

    public void addMessagePercent() {
        if (pertencent != 8.21) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "ATEN��O! ", "Ao alterar o valor [" + 8.21 + "] padrão da percentagem do imposto sobre o a comissão, "
                    + "o valor do prêmio líquido da comissão será alterado. \n" + pertencent));
        }
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
