package br.com.cc.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="configuracaoProdutor")
@Entity
public class ConfiguracaoProdutor implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer ultimoId;
	private Integer quantidade;	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataInclusao;
	private String codigoProcessamento;
	
	@XmlElement
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@XmlElement
	public Integer getUltimoId() {
		return ultimoId;
	}
	public void setUltimoId(Integer ultimoId) {
		this.ultimoId = ultimoId;
	}
	
	@XmlElement
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	@XmlElement
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
	@XmlElement
	public String getCodigoProcessamento() {
		return codigoProcessamento;
	}
	public void setCodigoProcessamento(String codigoProcessamento) {
		this.codigoProcessamento = codigoProcessamento;
	}
	
	

}
