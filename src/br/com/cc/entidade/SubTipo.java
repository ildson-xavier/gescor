package br.com.cc.entidade;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SubTipo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String descricao;
	private String custo; // 1 fixo; 2 variavel
	private Double percentagem = 0.0;
	private boolean clonar = false;
	
	@ManyToOne()
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Categoria categoria;
	
	@OneToMany(mappedBy = "subTipo", cascade=CascadeType.ALL)
	private List<Lancamentos> lancamentos = new LinkedList<Lancamentos>();
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Grupo grupo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		SubTipo other = (SubTipo) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getCusto() {
		return custo;
	}
	public void setCusto(String tipo) {
		this.custo = tipo;
	}
	public Double getPercentagem() {
		return percentagem;
	}
	public void setPercentagem(Double percentagem) {
		this.percentagem = percentagem;
	}
	public boolean isClonar() {
		return clonar;
	}
	public void setClonar(boolean clonar) {
		this.clonar = clonar;
	}
	
	
}
