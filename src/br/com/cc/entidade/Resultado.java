package br.com.cc.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.cc.util.Util;

@Entity
@Table(name="resultado")
public class Resultado implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="sequencial")
	private Long sequencia;
	
	@Column(name="periodo")
	private String periodo;
	
	@Column(name="produtor")
	private String produtor;
	
	@Column(name="bruto")
	private Double brutoComissao;
	
	@Column(name="liquido")
	private Double liquidoComissao;
	
	@Column(name="despesa")
	private Double despesa;
	
	@Column(name="result_brt")
	private Double resultadoBruto;
	
	@Column(name="result_liq")
	private Double resultadoLiquido;
	
	@Column(name="tipo_comi")
	private Integer tipoComissao;
	
	@Column(name="tipo_des")
	private Integer tipoDespesa;

	public Long getSequencia() {
		return sequencia;
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}

	public String getPeriodo() {
		return Util.getFormatDatePt(periodo);
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Double getBrutoComissao() {
		return brutoComissao;
	}

	public void setBrutoComissao(Double brutoComissao) {
		this.brutoComissao = brutoComissao;
	}

	public Double getLiquidoComissao() {
		return liquidoComissao;
	}

	public void setLiquidoComissao(Double liquidoComissao) {
		this.liquidoComissao = liquidoComissao;
	}

	public Double getDespesa() {
		return despesa;
	}

	public void setDespesa(Double despesa) {
		this.despesa = despesa;
	}

	public Double getResultadoBruto() {
		return resultadoBruto;
	}

	public void setResultadoBruto(Double resultadoBruto) {
		this.resultadoBruto = resultadoBruto;
	}

	public Double getResultadoLiquido() {
		return resultadoLiquido;
	}

	public void setResultadoLiquido(Double resultadoLiquido) {
		this.resultadoLiquido = resultadoLiquido;
	}

	public Integer getTipoComissao() {
		return tipoComissao;
	}

	public void setTipoComissao(Integer tipoComissao) {
		this.tipoComissao = tipoComissao;
	}

	public Integer getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(Integer tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public String getProdutor() {
		return produtor;
	}

	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}
	
	
}
