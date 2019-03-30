package br.com.cc.entidade.sinistro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

import br.com.cc.entidade.Produtor;

@Entity
public class Sinistro implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataSinistro;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataConclusao;
	
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataCriacao;
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Produtor produtor = new Produtor();
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private ApoliceVeiculo veiculo = new ApoliceVeiculo();
	
	@ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
	private Oficina oficina = new Oficina();
	
	private Double valorOrcamento;
	private Double franquia;
	
	@Enumerated(EnumType.STRING)
	private StatusSinistro status;
	
	private String sinistro;
	
	private String observacoes;
	
	@OneToMany(mappedBy = "sinistro", cascade=CascadeType.ALL)
	private List<SinistroHistorico> historico = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataSinistro() {
		return dataSinistro;
	}

	public void setDataSinistro(Date dataSinistro) {
		this.dataSinistro = dataSinistro;
	}

	public Produtor getProdutor() {
		return produtor;
	}

	public void setProdutor(Produtor produtor) {
		this.produtor = produtor;
	}

	public ApoliceVeiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(ApoliceVeiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Double getFranquia() {
		return franquia;
	}

	public void setFranquia(Double franquia) {
		this.franquia = franquia;
	}

	public StatusSinistro getStatus() {
		return status;
	}

	public void setStatus(StatusSinistro status) {
		this.status = status;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Double getValorOrcamento() {
		return valorOrcamento;
	}

	public void setValorOrcamento(Double valorOrcamento) {
		this.valorOrcamento = valorOrcamento;
	}

	public String getSinistro() {
		return sinistro;
	}

	public void setSinistro(String sinistro) {
		this.sinistro = sinistro;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public List<SinistroHistorico> getHistorico() {
		return historico;
	}

	public void setHistorico(List<SinistroHistorico> historico) {
		this.historico = historico;
	}

	
	
}
