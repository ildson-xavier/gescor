package br.com.cc.apolice.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;

import br.com.cc.dao.ApoliceDAO;
import br.com.cc.entidade.ApoliceBean;
import br.com.cc.util.JSFMessageUtil;

@ManagedBean
@ViewScoped
public class PesquisaApoliceView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<ApoliceBean> apolices = new ArrayList<>();
	private ApoliceDAO dao;
	private ApoliceBean apoliceSelecionada;
	private boolean desabilitado = true;
	private StreamedContent file;
	private ApoliceFiltro filtro;
	private boolean situacao;
	private int index = 0;
	
	public PesquisaApoliceView() {
		dao = new ApoliceDAO();
		filtro = new ApoliceFiltro();
	}
	
	public void pesquisarFiltro(){
		apolices.clear();
		apolices = new ArrayList<>();
		List<ApoliceBean> lista = new ArrayList<>();
		try {
			lista = dao.getApoliceFiltro(filtro);
			apolices = getApoliceQtdDias(lista,filtro);
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao pesquisar apolice", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private List<ApoliceBean> getApoliceQtdDias(List<ApoliceBean> apolices, ApoliceFiltro filtro){
		if (filtro != null && filtro.getQtdDias() != null && filtro.getQtdDias() > 0){
			Calendar cal = Calendar.getInstance();
			Calendar atual = Calendar.getInstance();
			List<ApoliceBean> lista = new ArrayList<>();
			atual.add(Calendar.DATE, filtro.getQtdDias());
			for(ApoliceBean bean : apolices){
				cal.setTime(bean.getDataTermino());
				if (cal.compareTo(atual) == 1) {
				//if (bean.getDataTermino().compareTo(atual.getTime()) < 1){
					lista.add(bean);
				}
			}
			return lista;
		}
		return apolices;
	}
	
	public void buscarApolice(){
		try {
			apolices = dao.getApolices();
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao pesquisar apolice", e.getMessage());
		}
	}
	
	public void atualizarApolice(){
		System.out.println("atualizarApolice");
		try {
			String produtor = apoliceSelecionada.getSegurado().getCorretor();
			apoliceSelecionada.getSegurado().setProdutor(produtor);
			dao.atualizarApolice(this.apoliceSelecionada);
			this.desabilitado = true;
			JSFMessageUtil.sendInfoMessageToUser("Apolice atualizado do com sucesso", this.apoliceSelecionada.getNumeroApolice());
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar atualizar apolice.", e.getMessage());
			this.desabilitado = true;
			e.printStackTrace();
		}
	}
	
	public void removerApolice(){
		System.out.println("removerApolice");
		try {
			dao.removerApolice(apoliceSelecionada);
			apolices.remove(apoliceSelecionada);
			JSFMessageUtil.sendInfoMessageToUser("Apolice deletada do com sucesso", this.apoliceSelecionada.getNumeroApolice());
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar apagar a apolice.", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void habilitar() {
		this.desabilitado = false;
	}
	
	public void onEdit(RowEditEvent event) {
		try {
			dao.atualizarApolice(((ApoliceBean) event.getObject()));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((ApoliceBean) event.getObject()).getNumeroApolice());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((ApoliceBean) event.getObject()).getNumeroApolice() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((ApoliceBean) event.getObject()).getNumeroApolice());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public List<ApoliceBean> getApolices() {
		return apolices;
	}

	public void setApolices(List<ApoliceBean> apolices) {
		this.apolices = apolices;
	}

	public ApoliceBean getApoliceSelecionada() {
		return apoliceSelecionada;
	}

	public void setApoliceSelecionada(ApoliceBean apoliceSelecionada) {
		this.apoliceSelecionada = apoliceSelecionada;
	}

	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	public ApoliceFiltro getFiltro() {
		return filtro;
	}

	public void setFiltro(ApoliceFiltro filtro) {
		this.filtro = filtro;
	}

	public boolean isSituacao() {
		this.index = 1;
		return situacao;
	}

	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
