package br.com.cc.entidade;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Controle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer codSis; // 1 Receitas 2 Despesas 3 Conciliacao
	private String descricao;
	//private String status;	// Liberado Pendente Bloqueado
	@Enumerated(EnumType.STRING)
	private ControleStatus status;
	
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Usuario usuario;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodSis() {
		return codSis;
	}
	public void setCodSis(Integer codSis) {
		this.codSis = codSis;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ControleStatus getStatus() {
		return status;
	}
	public void setStatus(ControleStatus status) {
		this.status = status;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codSis == null) ? 0 : codSis.hashCode());
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
		Controle other = (Controle) obj;
		if (codSis == null) {
			if (other.codSis != null)
				return false;
		} else if (!codSis.equals(other.codSis))
			return false;
		return true;
	}

	
	
}
