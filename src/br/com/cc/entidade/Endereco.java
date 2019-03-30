package br.com.cc.entidade;

import com.owlike.genson.annotation.JsonProperty;

public class Endereco {

	 @JsonProperty("uf")
	private String uf;
	 
	 @JsonProperty("cidade")
	private String cidade;
	 
	 @JsonProperty("bairro")
	private String bairro;
	 
	 @JsonProperty("tipo_logradouro")
	private String tipo_logradouro;
	 
	 @JsonProperty("logradouro")
	private String logradouro;
	 
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
	public String getTipo_logradouro() {
		return tipo_logradouro;
	}
	public void setTipo_logradouro(String tipo_logradouro) {
		this.tipo_logradouro = tipo_logradouro;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	@Override
	public String toString() {
		return "Endereco [uf=" + uf + ", cidade=" + cidade + ", bairro=" + bairro + ", tipo_logradouro="
				+ tipo_logradouro + ", logradouro=" + logradouro + "]";
	}
	
	
}
