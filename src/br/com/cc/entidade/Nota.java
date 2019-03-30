package br.com.cc.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Nota implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String seguradora;
	private String periodo;
	private Double vlrNota;
	private Integer qtqArquivosComissao;
	private Double vlrComissao;
	private Double vlrExtrato;
	private String observacao;
	
	@OneToMany(mappedBy = "nota", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<NotaDetalhe> detalhe = new ArrayList<>();
	
	@OneToMany(mappedBy = "nota", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<NotaLancamento> lancamento = new ArrayList<>();
	
	public void adicionarDetalhe(NotaDetalhe detalhe){
		detalhe.setNota(this);
		this.getDetalhe().add(detalhe);
	}
	
	public void adicionarLancamento(NotaLancamento lancamento){
		lancamento.setNota(this);
		this.getLancamento().add(lancamento);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeguradora() {
		return seguradora;
	}

	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Double getVlrNota() {
		return vlrNota;
	}

	public void setVlrNota(Double vlrNota) {
		this.vlrNota = vlrNota;
	}

	public Integer getQtqArquivosComissao() {
		return qtqArquivosComissao;
	}

	public void setQtqArquivosComissao(Integer qtqArquivosComissao) {
		this.qtqArquivosComissao = qtqArquivosComissao;
	}

	public Double getVlrComissao() {
		return vlrComissao;
	}

	public void setVlrComissao(Double vlrComissao) {
		this.vlrComissao = vlrComissao;
	}

	public List<NotaDetalhe> getDetalhe() {
		return detalhe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nota other = (Nota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<NotaLancamento> getLancamento() {
		return lancamento;
	}

	public void setLancamento(List<NotaLancamento> lancamento) {
		this.lancamento = lancamento;
	}

	public void setDetalhe(List<NotaDetalhe> detalhe) {
		this.detalhe = detalhe;
	}

	public Double getVlrExtrato() {
		return vlrExtrato;
	}

	public void setVlrExtrato(Double vlrExtrato) {
		this.vlrExtrato = vlrExtrato;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
}
