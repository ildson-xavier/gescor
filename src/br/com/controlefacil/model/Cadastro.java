package br.com.controlefacil.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cadastro")
public class Cadastro implements BaseEntity {

	private static final long serialVersionUID = 6214533048982323813L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String categoria;
	private String descricao;
	
	@Column(name="esta_online")
	private Boolean estaOnline = true;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario", referencedColumnName = "id")
	private UsuarioControle usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getEstaOnline() {
		return estaOnline;
	}

	public void setEstaOnline(Boolean estaOline) {
		this.estaOnline = estaOline;
	}

	public UsuarioControle getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioControle usuario) {
		this.usuario = usuario;
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
		Cadastro other = (Cadastro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
