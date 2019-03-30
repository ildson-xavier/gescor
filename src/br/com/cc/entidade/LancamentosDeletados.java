/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cc.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Usuario
 */
@Entity
public class LancamentosDeletados implements Serializable{

	private static final long serialVersionUID = -9158181728658822268L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String segurado;
    private String produtor;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInclusao;
    private String seguradora;
    private Integer parcela;
    private String descricao;
    private Double comissao;
    private Double imposto; 
    private Double comissaoLiquida;
    private Integer idRef;
    
    @ManyToOne
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
    private Auditoria auditoria;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSegurado() {
		return segurado;
	}
	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}
	public String getProdutor() {
		return produtor;
	}
	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}
	public Date getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}
	public String getSeguradora() {
		return seguradora;
	}
	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}
	public Integer getParcela() {
		return parcela;
	}
	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getComissao() {
		return comissao;
	}
	public void setComissao(Double comissao) {
		this.comissao = comissao;
	}
	public Double getImposto() {
		return imposto;
	}
	public void setImposto(Double imposto) {
		this.imposto = imposto;
	}
	public Double getComissaoLiquida() {
		return comissaoLiquida;
	}
	public void setComissaoLiquida(Double comissaoLiquida) {
		this.comissaoLiquida = comissaoLiquida;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	public Auditoria getAuditoria() {
		return auditoria;
	}
	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}
	public Integer getIdRef() {
		return idRef;
	}
	public void setIdRef(Integer idRef) {
		this.idRef = idRef;
	}    


    
    
}
