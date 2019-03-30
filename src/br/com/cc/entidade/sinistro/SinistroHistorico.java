package br.com.cc.entidade.sinistro;

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
public class SinistroHistorico implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date data;
	private String descricao;
	private String status;
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Sinistro sinistro;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Sinistro getSinistro() {
		return sinistro;
	}
	public void setSinistro(Sinistro sinistro) {
		this.sinistro = sinistro;
	}
	
	
}
