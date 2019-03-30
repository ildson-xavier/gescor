package br.com.cc.entidade.sinistro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "apoliceveiculo")
public class ApoliceVeiculo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Integer codigoSegurado; // SegCod.APO
	
	private Integer codigoApolice; // ApoSeq.APO = IAF
	private Integer apoliceAno; // ApoAno.APO    = IAF
	
	private String seguradora; // PlfDes.PLF    APO.SgaCod = PLF.PlfCod
	
	private String apolice; // ApoNumApoEnd.APO
	private String situacao; // ApoSit.APO [R, A]
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date apoliceDateInicio; // ApoDatIni.APO
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date apoliceDateFim; // ApoDatFin.APO
	
	private String segurado; // IafPflNom.IAF
	private String tipoVeiculo; // IafVeiTpo
	private String placa; // IafVeiPla
	private String cor; // IafVeiCor
	private String renavan; // IafVeiRen
	
	public Integer getCodigoSegurado() {
		return codigoSegurado;
	}
	public void setCodigoSegurado(Integer codigoSegurado) {
		this.codigoSegurado = codigoSegurado;
	}
	public Integer getCodigoApolice() {
		return codigoApolice;
	}
	public void setCodigoApolice(Integer codigoApolice) {
		this.codigoApolice = codigoApolice;
	}
	public Integer getApoliceAno() {
		return apoliceAno;
	}
	public void setApoliceAno(Integer apoliceAno) {
		this.apoliceAno = apoliceAno;
	}
	public String getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}
	public String getApolice() {
		return apolice;
	}
	public void setApolice(String apolice) {
		this.apolice = apolice;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Date getApoliceDateInicio() {
		return apoliceDateInicio;
	}
	public void setApoliceDateInicio(Date apoliceDateInicio) {
		this.apoliceDateInicio = apoliceDateInicio;
	}
	public Date getApoliceDateFim() {
		return apoliceDateFim;
	}
	public void setApoliceDateFim(Date apoliceDateFim) {
		this.apoliceDateFim = apoliceDateFim;
	}
	public String getSegurado() {
		return segurado;
	}
	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}
	public String getTipoVeiculo() {
		return tipoVeiculo;
	}
	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getRenavan() {
		return renavan;
	}
	public void setRenavan(String renavan) {
		this.renavan = renavan;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
}
