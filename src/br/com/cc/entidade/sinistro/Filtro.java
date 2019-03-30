package br.com.cc.entidade.sinistro;

import java.io.Serializable;
import java.util.Date;

public class Filtro implements Serializable{

	private static final long serialVersionUID = 1L;
	private Date dataInicio;
	private Date dataFim;
	private String seguradora;
	private String segurado;
	
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public String getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}

	public String getSegurado() {
		return segurado;
	}
	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}
	
	
}
