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

@Entity
public class NotaLancamento implements Serializable, Comparable<NotaLancamento>{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date periodo;
	private String seguradora;
	private Double vlrComissaoBruto;
	private Double vlrImposto;
	private Double vlrLiquido;
	
	@ManyToOne()
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Nota nota;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Double getVlrComissaoBruto() {
		return vlrComissaoBruto;
	}
	public void setVlrComissaoBruto(Double vlrComissaoBruto) {
		this.vlrComissaoBruto = vlrComissaoBruto;
	}
	public Double getVlrImposto() {
		return vlrImposto;
	}
	public void setVlrImposto(Double vlrImposto) {
		this.vlrImposto = vlrImposto;
	}
	public Double getVlrLiquido() {
		return vlrLiquido;
	}
	public void setVlrLiquido(Double vlrLiquido) {
		this.vlrLiquido = vlrLiquido;
	}
	public Nota getNota() {
		return nota;
	}
	public void setNota(Nota nota) {
		this.nota = nota;
	}
	@Override
	public int compareTo(NotaLancamento o) {
		if (this.periodo.after(o.getPeriodo())){
			return -1;
		}
		
		if (this.periodo.before(o.getPeriodo())){
			return 1;
		}
		return 0;
	}
	
	
}
