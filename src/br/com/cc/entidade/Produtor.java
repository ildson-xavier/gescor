/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cc.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@XmlRootElement(name = "produtor")
public class Produtor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Integer codigo;
	private String segurado;
	private String produtor;
	private String sigla;
	private String apolice;
	private String cpf;

	private String telefone;
	private String celular;
	private String bairro;
	private String cidade;
	private String Uf;
	private String endereco;
	private String numero;
	private String corretor;
	private String cep;
	private String email;
	
	@OneToMany(mappedBy = "segurado", cascade=CascadeType.ALL)
	private List<ApoliceBean> apolices = new ArrayList<>();

	@XmlElement
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@XmlElement
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@XmlElement
	public String getUf() {
		return Uf;
	}

	public void setUf(String uf) {
		Uf = uf;
	}

	@XmlElement
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@XmlElement
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@XmlElement
	public String getApolice() {
		return apolice;
	}

	public void setApolice(String apolice) {
		this.apolice = apolice;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getSegurado() {
		return segurado;
	}

	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}

	@XmlElement
	public String getProdutor() {
		return produtor;
	}

	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}

	@XmlElement
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@XmlElement
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@XmlElement
	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlElement
	public String getCorretor() {
		return corretor;
	}

	public void setCorretor(String corretor) {
		this.corretor = corretor;
	}

	@XmlElement
	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public List<ApoliceBean> getApolices() {
		return apolices;
	}

	public void setApolices(List<ApoliceBean> apolices) {
		this.apolices = apolices;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Produtor [id=" + id + ", codigo=" + codigo + ", segurado=" + segurado + ", produtor=" + produtor
				+ ", sigla=" + sigla + ", apolice=" + apolice + ", cpf=" + cpf + ", telefone=" + telefone + ", celular="
				+ celular + ", bairro=" + bairro + ", cidade=" + cidade + ", Uf=" + Uf + ", endereco=" + endereco
				+ ", numero=" + numero + ", corretor=" + corretor + ", cep=" + cep + ", email=" + email + ", apolices="
				+ apolices + "]";
	}

	
}
