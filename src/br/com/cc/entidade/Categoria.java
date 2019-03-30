package br.com.cc.entidade;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Categoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String tipo;
	private Double valorSum;

	@OneToMany(mappedBy = "categoria", cascade=CascadeType.DETACH)
	private List<SubTipo> subTipo = new LinkedList<SubTipo>();

	@OneToMany(mappedBy = "categoria", cascade=CascadeType.ALL)
	private List<Lancamentos> lancamentos = new LinkedList<Lancamentos>();

	private boolean status; // Ativo Inativo

	public void addSubTipo(SubTipo sub) {
		sub.setCategoria(this);
		getSubTipo().add(sub);
	}

	public void addLancamentos(Lancamentos lanc){
		lanc.setCategoria(this);
		getLancamentos().add(lanc);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<SubTipo> getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(List<SubTipo> subTipo) {
		this.subTipo = subTipo;
	}
	
	public Double getValorSum() {
		return valorSum;
	}

	public void setValorSum(Double valorSum) {
		this.valorSum = valorSum;
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
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return tipo;
	}
	
}
