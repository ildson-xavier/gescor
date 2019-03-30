package br.com.cc.entidade;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;

import br.com.gescor.cobranca.model.Boleto;

@Entity
public class Corretora implements Serializable{

	@Transient
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String razaoSocial;
	private String cpfCnpj;
	private String emailCobranca;
	private String telefone;
	private String cep;
	private String uf;
	private String cidade;
	private String bairro;
	private String endereco;
	private String numero;
	private String complemento;
	@Email(message = "Informe um e-mail válido.")
	private String email;
	
	@OneToMany(mappedBy = "corretora", cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
	private List<Boleto> boletos = new ArrayList<>();
	
	@OneToMany(mappedBy = "corretora", cascade=CascadeType.DETACH, fetch=FetchType.LAZY)
	private List<Usuario> usuarios = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getEmailCobranca() {
		return emailCobranca;
	}
	public void setEmailCobranca(String emailCobranca) {
		this.emailCobranca = emailCobranca;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public List<Boleto> getBoletos() {
		return boletos;
	}
	public void setBoletos(List<Boleto> boletos) {
		this.boletos = boletos;
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Corretora [id=" + id + ", razaoSocial=" + razaoSocial + ", cpfCnpj=" + cpfCnpj + ", emailCobranca="
				+ emailCobranca + ", telefone=" + telefone + ", cep=" + cep + ", uf=" + uf + ", cidade=" + cidade
				+ ", bairro=" + bairro + ", endereco=" + endereco + ", numero=" + numero + ", complemento="
				+ complemento + ", email=" + email + "]";
	}	
	
	
	
}
