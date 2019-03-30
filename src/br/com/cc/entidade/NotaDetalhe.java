package br.com.cc.entidade;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class NotaDetalhe implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String produtor;
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
	public String getProdutor() {
		return produtor;
	}
	public void setProdutor(String produtor) {
		this.produtor = produtor;
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
		NotaDetalhe other = (NotaDetalhe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
