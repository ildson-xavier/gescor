package br.com.cc.entidade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.cc.util.JSFMessageUtil;

@Entity
public class ApoliceBean implements Serializable, Comparable<Date>{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String susep;
	private String numeroApolice;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataVigenciaInicio;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataTermino;
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataImportacao;
	private String path;
	private String arquivo;
	private String seguradora;
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Produtor segurado;
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private VeiculoBean veiculo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSusep() {
		return susep;
	}
	public void setSusep(String susep) {
		this.susep = susep;
	}
	public String getNumeroApolice() {
		return numeroApolice;
	}
	public void setNumeroApolice(String numeroApolice) {
		this.numeroApolice = numeroApolice;
	}
	public Date getDataVigenciaInicio() {
		return dataVigenciaInicio;
	}
	public void setDataVigenciaInicio(Date dataVigenciaInicio) {
		this.dataVigenciaInicio = dataVigenciaInicio;
	}
	public Date getDataTermino() {
		return dataTermino;
	}
	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	public Produtor getSegurado() {
		return segurado;
	}
	public void setSegurado(Produtor segurado) {
		this.segurado = segurado;
	}

	public VeiculoBean getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(VeiculoBean veiculo) {
		this.veiculo = veiculo;
	}
	public Date getDataImportacao() {
		return dataImportacao;
	}
	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Transient
	public StreamedContent download(){
		StreamedContent file = null;
		if (this.path != null || !this.path.equals("")){
			try{
				InputStream stream = new FileInputStream(this.path);
				file = new DefaultStreamedContent(stream, "application/pdf", this.arquivo);
			}catch (FileNotFoundException e) {
				JSFMessageUtil.sendAlertMessageToUser("Não foi possivel fazer o download.", e.getMessage());
			}
			
		}
		return file;
	}
	
	@Transient
	public boolean temArquivo(){
		if (this.arquivo == null || this.arquivo.equals("")){
			return true;
		}
		return false;
	}
	
	public String getArquivo() {
		return arquivo;
	}
	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}
	public String getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}
	@Override
	public int compareTo(Date o) {
		// TODO Auto-generated method stub
		return this.dataTermino.compareTo(o);
	}
	@Override
	public String toString() {
		return "ApoliceBean [id=" + id + ", susep=" + susep + ", numeroApolice=" + numeroApolice
				+ ", dataVigenciaInicio=" + dataVigenciaInicio + ", dataTermino=" + dataTermino + ", dataImportacao="
				+ dataImportacao + ", path=" + path + ", arquivo=" + arquivo + ", seguradora=" + seguradora
				+ ", segurado=" + segurado + ", veiculo=" + veiculo + "]";
	}
	
	

}
