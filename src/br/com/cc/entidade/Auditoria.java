package br.com.cc.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class Auditoria implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@ManyToOne
	@JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Usuario usuario;
	
	private String tipoAlteracao; // delete ou update
	private String tipoLancamento; // receitas ou despesas
	
	@Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
	
	private Integer idRegistro;
	private String valorAnterior;
	private String valorAtual;
	
	@OneToMany(mappedBy = "auditoria", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<LancamentosDeletados> lancs = new ArrayList<>();
	
	public void addLancs(LancamentosDeletados del){
		this.getLancs().add(del);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTipoAlteracao() {
		return tipoAlteracao;
	}

	public void setTipoAlteracao(String tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getValorAnterior() {
		return valorAnterior;
	}

	public void setValorAnterior(String valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public String getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(String valorAtual) {
		this.valorAtual = valorAtual;
	}

	public Date getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public Integer getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}

	public List<LancamentosDeletados> getLancs() {
		return lancs;
	}

	public void setLancs(List<LancamentosDeletados> lancs) {
		this.lancs = lancs;
	}
	
	
}
