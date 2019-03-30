package br.com.controlefacil.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect
@Entity
@Table(name="lancamento")
public class Lancamento implements BaseEntity{

	private static final long serialVersionUID = 7155336127045954855L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String categoria;
	private String descricao;
	private BigDecimal valor;
	private BigDecimal saldo;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="data_lancamento")
	private String dataLancamento;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="data_inclusao")
	private String dataInclusao;
	
	private Boolean estaOnline = true;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario", referencedColumnName = "id")
	private UsuarioControle usuario;
	
	private Boolean eFuturo;
	
	private Boolean deletado;
	
	private String mesAno;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	//@JsonSerialize(using=JsonDateSerializer.class)
	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
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
		Lancamento other = (Lancamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//@JsonSerialize(using=JsonDateSerializer.class)
	public String getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Boolean geteFuturo() {
		return eFuturo;
	}

	public void seteFuturo(Boolean eFuturo) {
		this.eFuturo = eFuturo;
	}

	public Boolean getDeletado() {
		return deletado;
	}

	public void setDeletado(Boolean deletado) {
		this.deletado = deletado;
	}

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}
	
	
}
