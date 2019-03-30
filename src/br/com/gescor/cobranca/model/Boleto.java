package br.com.gescor.cobranca.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import br.com.cc.entidade.Corretora;

@Entity
public class Boleto implements Serializable{

	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataGeracaoBoleto;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataVencimento;
	
	private Integer diasAbertos;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataPagamento;
	
	@Enumerated(EnumType.STRING)
	private Status statusPagamento = Status.GERADO;
	
	private Double valorBoleto;
	
	@OneToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Corretora corretora; // Sacado
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDataGeracaoBoleto() {
		return dataGeracaoBoleto;
	}
	public void setDataGeracaoBoleto(Date dataGeracaoBoleto) {
		this.dataGeracaoBoleto = dataGeracaoBoleto;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Integer getDiasAbertos() {
		return diasAbertos;
	}
	public void setDiasAbertos(Integer diasAbertos) {
		this.diasAbertos = diasAbertos;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public Status getStatusPagamento() {
		return statusPagamento;
	}
	public void setStatusPagamento(Status statusPagamento) {
		this.statusPagamento = statusPagamento;
	}
	public Double getValorBoleto() {
		return valorBoleto;
	}
	public void setValorBoleto(Double valorBoleto) {
		this.valorBoleto = valorBoleto;
	}
	public Corretora getCorretora() {
		return corretora;
	}
	public void setCorretora(Corretora corretora) {
		this.corretora = corretora;
	}
	
	
}
