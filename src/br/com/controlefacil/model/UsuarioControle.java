package br.com.controlefacil.model;


import java.util.Date;
	
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonAutoDetect
@Entity
@Table(name="usuariocontrole")
public class UsuarioControle implements BaseEntity{

	private static final long serialVersionUID = -6684800602294917284L;
	
	@Id
	@Column(unique=true)
	private Long id;
	@Column(unique=true)
	private String email;
	private String nome;
	private String senha;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="data_nascimento")
	private String dataNascimento;
	
	@Column(name="esta_online")
	private Boolean estaOnline = true;
	
	@Column(name="faz_backup")
	private Boolean fazBackup;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario",fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario",fetch = FetchType.LAZY)
	private List<Cadastro> cadastros;
	
	public UsuarioControle() {
	}
	
	public UsuarioControle(Long codigo, String email, String nome, String senha, 
			       String dataNascimento, Boolean estaOnline, Boolean fazBackup) {
		this.id = codigo;
		this.email = email;
		this.nome = nome;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
		this.estaOnline = estaOnline;
		this.fazBackup = fazBackup;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	//@JsonSerialize(using=JsonDateSerializer.class)
	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Boolean getEstaOnline() {
		return estaOnline;
	}

	public void setEstaOnline(Boolean estaOnline) {
		this.estaOnline = estaOnline;
	}

	public Boolean getFazBackup() {
		return fazBackup;
	}

	public void setFazBackup(Boolean fazBackup) {
		this.fazBackup = fazBackup;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long codigo) {
		this.id = codigo;
	}
	
	
}
