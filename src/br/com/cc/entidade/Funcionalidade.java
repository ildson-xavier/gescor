package br.com.cc.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Funcionalidade implements Serializable{

	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Boolean ativo;
	private String indice;
	private String descricao;
	private String pagina;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Perfil perfil;
	
	@ManyToOne
	@JoinColumn(name="funcionalidade_pai_id")
	private Funcionalidade funcionalidadePai;
	
	@OneToMany(mappedBy="funcionalidadePai", cascade = CascadeType.ALL)
	private List<Funcionalidade> componentes = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public String getIndice() {
		return indice;
	}
	public void setIndice(String indice) {
		this.indice = indice;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	public Funcionalidade getFuncionalidadePai() {
		return funcionalidadePai;
	}
	public void setFuncionalidadePai(Funcionalidade funcionalidadePai) {
		this.funcionalidadePai = funcionalidadePai;
	}
	public List<Funcionalidade> getComponentes() {
		return componentes;
	}
	public void setComponentes(List<Funcionalidade> componentes) {
		this.componentes = componentes;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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
		Funcionalidade other = (Funcionalidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Funcionalidade [indice=" + indice + "]";
	}
	
	
	
}
