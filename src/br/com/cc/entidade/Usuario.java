package br.com.cc.entidade;

import java.io.Serializable;


import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;


/**
 *
 * @author Usuario
 */
@Entity
public class Usuario implements Serializable{
    
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private String tema;
    private String telefone;
    private Integer hierarquia;
    private Integer idPai;
    
   @Email(message="Informe um e-mail válido")
    private String email;
    
    @OneToOne
    //@JoinColumn(name="perfil_id")
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
    private Perfil perfil;
    
    @OneToMany(mappedBy="usuario", orphanRemoval=true)
	private List<Controle> controles = new LinkedList<Controle>();
    
    @OneToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
    private Corretora corretora;
    
    public boolean isMaster(){
    	return true;
    }
    
	public boolean isAdmin(){
		return true;
	}
	
	public boolean isConsultor(){
		return true;
	}
	
	public boolean NaoTemCorretora(){
		return this.corretora != null ? false : ehPai();
	}
	
	public boolean temCorretora(){
		return this.corretora != null ? true : false;
	}
	
	public boolean ehPai(){
		return this.hierarquia == 1 ? true : false;
	}
    
    public Integer getId() {
        return id;
    }
    

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Controle> getControles() {
		return controles;
	}

	public void setControles(List<Controle> controle) {
		this.controles = controle;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public Corretora getCorretora() {
		return corretora;
	}

	public void setCorretora(Corretora corretora) {
		this.corretora = corretora;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", login=" + login + ", senha=" + senha + ", tema=" + tema
				+ ", telefone=" + telefone + ", email=" + email + ", perfil=" + perfil
				+ ", corretora=" + corretora + "]";
	}

	public Integer getHierarquia() {
		return hierarquia;
	}

	public void setHierarquia(Integer hierarquia) {
		this.hierarquia = hierarquia;
	}

	public Integer getIdPai() {
		return idPai;
	}

	public void setIdPai(Integer idPai) {
		this.idPai = idPai;
	}

}
