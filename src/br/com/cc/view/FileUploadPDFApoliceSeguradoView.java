/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.cc.entidade.Produtor;
import br.com.cc.pdf.PDFApoliceSegurado;

/**
 *
 * @author Usuario
 */
@ManagedBean
@ViewScoped
public class FileUploadPDFApoliceSeguradoView implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PDFApoliceSegurado pdfps = new PDFApoliceSegurado();
    private List<Produtor> listProdutores = new ArrayList<Produtor>();
    private String diretorio;
    private String caminho;
    private byte[] arquivo;
    private String nome;

    /**
     * Creates a new instance of FileUploadPDFSeguradoProdutorView
     */
    public FileUploadPDFApoliceSeguradoView() {
        //Util.verificaMemoria("ccomissao");
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        if (event != null) {
            try {
                upload(event, "resources\\pdfbase\\");
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload do arquivo ", event.getFile().getFileName() + " realizado com sucesso");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (IOException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro fazer upload do arquivo ", event.getFile().getFileName() + " " + ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

            /**
             *
             */
            processarApolice();

            /**
             *
             */
            //produtorView.adicionarProdutores(listProdutores);
        }
    }
    
    public void processarApolice() {
        try {
            pdfps.lerApoliceSegurado(this.caminho);
            //listProdutores = pdfps.getListProdutor();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "SUCESSO! ", "arquivo [" + this.caminho + "] processado com sucesso.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ERRO! ", "Erro durante o processamento do arquivo [" + this.caminho + "] " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void upload(FileUploadEvent event, String diretorio) throws IOException {
        UploadedFile up = event.getFile();
        
        this.nome = up.getFileName();
        this.caminho = getRealPath() + diretorio + getNome();
        
        System.out.println("Nome: " + this.nome);
        System.out.println("Caminho: " + this.caminho);
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
    
    public List<Produtor> getListProdutores() {
        return listProdutores;
    }
    
    public void setListProdutores(List<Produtor> listProdutores) {
        this.listProdutores = listProdutores;
    }
    
}
