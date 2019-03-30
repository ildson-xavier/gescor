package br.com.cc.apolice.view;

import java.io.Serializable;
import java.util.Date;

public class ApoliceFiltro implements Serializable{

	private static final long serialVersionUID = 1L;
	private String numeroApolice;
	private String susep;
	private Date inicioVigencia;
	private Date fimVigencia;
	private String segurado;
	private String corretor;
	private String marcaMoldelo;
	private String placa;
	private Integer qtdDias;
	
	public String getNumeroApolice() {
		return numeroApolice;
	}
	public void setNumeroApolice(String numeroApolice) {
		this.numeroApolice = numeroApolice;
	}
	public Date getInicioVigencia() {
		return inicioVigencia;
	}
	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}
	public Date getFimVigencia() {
		return fimVigencia;
	}
	public void setFimVigencia(Date fimVigencia) {
		this.fimVigencia = fimVigencia;
	}
	public String getSegurado() {
		return segurado;
	}
	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}
	public String getCorretor() {
		return corretor;
	}
	public void setCorretor(String corretor) {
		this.corretor = corretor;
	}
	public String getMarcaMoldelo() {
		return marcaMoldelo;
	}
	public void setMarcaMoldelo(String marcaMoldelo) {
		this.marcaMoldelo = marcaMoldelo;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getSusep() {
		return susep;
	}
	public void setSusep(String susep) {
		this.susep = susep;
	}
	public Integer getQtdDias() {
		return qtdDias;
	}
	public void setQtdDias(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}
	
	
}
