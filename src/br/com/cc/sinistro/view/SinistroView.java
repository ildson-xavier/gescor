package br.com.cc.sinistro.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.sinistro.ApoliceVeiculo;
import br.com.cc.entidade.sinistro.Filtro;
import br.com.cc.entidade.sinistro.Oficina;
import br.com.cc.entidade.sinistro.Sinistro;
import br.com.cc.entidade.sinistro.SinistroHistorico;
import br.com.cc.entidade.sinistro.StatusSinistro;
import br.com.cc.facade.SinistroFacade;
import br.com.cc.view.AbstractMB;

@ManagedBean(name = "sinistroView")
@ViewScoped
public class SinistroView extends AbstractMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SinistroFacade facade;
	
	private List<Sinistro> sinistros;
	
	private Sinistro sinistro;
	private Sinistro sinistroBean;
	private Produtor produtor;
	private ApoliceVeiculo apoliceVeiculo;
	private Oficina oficina;
	private SinistroHistorico historico;
	
	private Boolean editar;
	
	private String nomeSegurado;
	
	private Filtro filtro;
	
	@PostConstruct
	public void init(){
		this.sinistro = new Sinistro();
		this.sinistroBean = new Sinistro();
		this.facade = new SinistroFacade();
		this.produtor = new Produtor();
		this.apoliceVeiculo = new ApoliceVeiculo();
		this.historico = new SinistroHistorico();
		this.setOficina(new Oficina());
		
		this.sinistros = new ArrayList<>();
		this.editar = false;
		this.setFiltro(new Filtro());
	}
	
	public void mostrarObservacao(Sinistro sinistro){
		this.editar = true;
		this.sinistroBean = sinistro;
		
	}
	
	public void detalhe(Sinistro sinistro){
		this.produtor = sinistro.getProdutor();
		this.apoliceVeiculo = sinistro.getVeiculo();
		this.oficina = sinistro.getOficina();
		this.sinistro = sinistro;
		this.editar = true;
		//return "cadastrarsinistroview?faces-redirect=true";
	}
	
	public String cancelar(){
		this.sinistros = new ArrayList<>();
		return "sinistroview?faces-redirect=true";
	}
	
	public void deletarSinistro(Sinistro sinistro){
		try {
			this.facade.deletar(sinistro);
			this.sinistros.remove(sinistro);
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Sinistro do "+this.sinistro.getProdutor().getSegurado()+" deletado com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar deletar: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	public void atualizarHistorico(){
		try {
			this.facade.salvarHistorico(builder(this.historico));
			this.facade.atualizarSinistro(this.sinistroBean);
			this.sinistroBean.getHistorico().add(this.historico);
			this.sinistroBean.setObservacoes("");
			this.historico = new SinistroHistorico();
			this.sinistroBean = new Sinistro();
			this.pesquisar();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Status atualizada com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar atualizar: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	private SinistroHistorico builder(SinistroHistorico historico){
		historico.setSinistro(this.sinistroBean);
		historico.setStatus(this.sinistroBean.getStatus().getDescricao());
		historico.setData(Calendar.getInstance().getTime());
		historico.setDescricao(this.sinistroBean.getObservacoes());
		return historico;
	}
	
	public List<String> autoCompletarSegurado(String query){
		List<String> results = new LinkedList<String>();
		try {
			results = this.facade.autoCompletar(query);
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar autocomplentar: "+e.getMessage());
			super.closeDialog();
		}
		return results;
	}
	
	public List<String> autoCompletarOficina(String query){
		List<String> results = new LinkedList<String>();
		try {
			results = this.facade.autoCompletarOficina(query);
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar autocomplentar: "+e.getMessage());
			super.closeDialog();
		}
		return results;
	}
	
	public void carregarDadosOficina(){
		this.oficina = this.facade.carregarDadosOficina(this.oficina.getNome());
	}
	
	public void carregarDadosSegurado(){
		this.produtor = this.facade.carregarDadosSegurado(this.produtor.getSegurado());
		try {
			this.apoliceVeiculo = this.facade.apoliceVeiculoPorCodigoSegurado(this.produtor.getCodigo());
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar autocomplentar: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	public String novo(){
		inicializar();
		return "cadastrarsinistroview?faces-redirect=true";
	}
	
	private void inicializar(){
		this.produtor = new Produtor();
		this.apoliceVeiculo = new ApoliceVeiculo();
		this.oficina = new Oficina();
		this.sinistro = new Sinistro();
		this.historico = new SinistroHistorico();
		this.editar = false;
	}
	
	public void salvar(){
		this.sinistro.setProdutor(this.produtor);
		this.sinistro.setVeiculo(this.apoliceVeiculo);
		this.sinistro.setOficina(this.oficina);
		this.sinistro.setHistorico(new ArrayList<SinistroHistorico>());
		
		try {
			this.sinistro = this.facade.salvar(sinistro);
			inicializar();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Salvo com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar salvar: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	public void salvarHistorico(){
		try {
			this.historico.setSinistro(this.sinistro);
			this.facade.salvarHistorico(this.historico);
			inicializar();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar salvar: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	public void salvarFull(){
		this.salvar();
		this.salvarHistorico();
	}
	
	public void pesquisar(){
		try {
			this.sinistros = new ArrayList<>();
			this.sinistros = this.facade.sinistros(this.filtro);
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao pesquisar sinistro: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	public List<StatusSinistro> getStatus(){
		return Arrays.asList(StatusSinistro.values());
	}
	
	public void atualizar(Sinistro sinistro){
		try {
			this.facade.atualizar(sinistro);
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Atualizado com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao pesquisar sinistro: "+e.getMessage());
			super.closeDialog();
		}
	}
	
	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		try {
			System.out.println(((Sinistro) event.getObject()).getStatus().getDescricao());
			this.facade.atualizar(((Sinistro) event.getObject()));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Sinistro) event.getObject()).getProdutor().getSegurado());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Sinistro) event.getObject()).getProdutor().getSegurado() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Sinistro) event.getObject()).getProdutor().getSegurado());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public List<Sinistro> getSinistros() {
		return sinistros;
	}
	public void setSinistros(List<Sinistro> sinistros) {
		this.sinistros = sinistros;
	}
	public Sinistro getSinistro() {
		return sinistro;
	}
	public void setSinistro(Sinistro sinistro) {
		this.sinistro = sinistro;
	}

	public String getNomeSegurado() {
		return nomeSegurado;
	}

	public void setNomeSegurado(String nomeSegurado) {
		this.nomeSegurado = nomeSegurado;
	}

	public Produtor getProdutor() {
		return produtor;
	}

	public void setProdutor(Produtor produtor) {
		this.produtor = produtor;
	}

	public ApoliceVeiculo getApoliceVeiculo() {
		return apoliceVeiculo;
	}

	public void setApoliceVeiculo(ApoliceVeiculo apoliceVeiculo) {
		this.apoliceVeiculo = apoliceVeiculo;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	public Sinistro getSinistroBean() {
		return sinistroBean;
	}

	public void setSinistroBean(Sinistro sinistroBean) {
		this.sinistroBean = sinistroBean;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public SinistroHistorico getHistorico() {
		return historico;
	}

	public void setHistorico(SinistroHistorico historico) {
		this.historico = historico;
	}
	
	

}
