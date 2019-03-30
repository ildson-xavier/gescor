package br.com.cc.entidade;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Extrato implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String periodo;
	private Integer mesReferencia = 0;
	private Double vlrTotalSeguradorasExtrato = 0.0;
	private Double vlrTotalSeguradorasSistema = 0.0;
	private String produtor;
	private Double vlrRetiradaSocio = 0.0;
	private Double vlrReceberSistema = 0.0;
	private Double vlrAcerto = 0.0;
	private Double vlrSaldoAcumulado = 0.0;
	
	@Transient
	private String seguradora;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Integer getMesReferencia() {
		return mesReferencia;
	}
	public void setMesReferencia(Integer mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	public Double getVlrTotalSeguradorasExtrato() {
		return vlrTotalSeguradorasExtrato;
	}
	public void setVlrTotalSeguradorasExtrato(Double vlrTotalSeguradorasExtrato) {
		this.vlrTotalSeguradorasExtrato = vlrTotalSeguradorasExtrato;
	}
	public Double getVlrTotalSeguradorasSistema() {
		return vlrTotalSeguradorasSistema;
	}
	public void setVlrTotalSeguradorasSistema(Double vlrTotalSeguradorasSistema) {
		this.vlrTotalSeguradorasSistema = vlrTotalSeguradorasSistema;
	}
	public String getProdutor() {
		return produtor;
	}
	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}
	public Double getVlrRetiradaSocio() {
		return vlrRetiradaSocio;
	}
	public void setVlrRetiradaSocio(Double vlrRetiradaSocio) {
		this.vlrRetiradaSocio = vlrRetiradaSocio;
	}
	public Double getVlrReceberSistema() {
		return vlrReceberSistema;
	}
	public void setVlrReceberSistema(Double vlrReceberSistema) {
		this.vlrReceberSistema = vlrReceberSistema;
	}
	public Double getVlrAcerto() {
		return vlrAcerto;
	}
	public void setVlrAcerto(Double vlrAcerto) {
		this.vlrAcerto = vlrAcerto;
	}
	public Double getVlrSaldoAcumulado() {
		return vlrSaldoAcumulado;
	}
	public void setVlrSaldoAcumulado(Double vlrSaldoAcumulado) {
		this.vlrSaldoAcumulado = vlrSaldoAcumulado;
	}
	public String getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}

	
	
}
