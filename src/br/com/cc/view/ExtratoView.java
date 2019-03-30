package br.com.cc.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import br.com.cc.entidade.Extrato;
import br.com.cc.entidade.SeguradorasExtrato;
import br.com.cc.facade.ExtratoFacade;

@ViewScoped
@ManagedBean(name="extratoView")
public class ExtratoView extends AbstractMB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf;
	
	private List<Extrato> extratos;
	private ExtratoFacade extratoFacade;
	
	private Double vlrExtrato;
	
	private Extrato extrato;
	private Date periodo;
	private Double vlrSeguradoraExtrato = 0.0;
	private String seguradorasExtrato;
	
	private Date dataPesquisa = null;
	private String produtorPesquisa = "";
	
	private List<SeguradorasExtrato> seguradoras;
	
	private Extrato extratoTotais;
	
	@PostConstruct
	public void init(){
		sdf = new SimpleDateFormat("MM/yyyy");
		extratoFacade = new ExtratoFacade();
		extrato = new Extrato();
		setVlrExtrato(0.0);
		seguradoras = new ArrayList<>();
	}
	
	public void criarExtrato() {
		System.out.println("criarExtrato");
		try {
			if (this.periodo != null){
				this.extrato.setPeriodo(sdf.format(this.periodo));
			}
			this.extrato.setSeguradora(this.seguradorasExtrato);
			this.extrato.setVlrTotalSeguradorasExtrato(vlrSeguradoraExtrato);
			this.extratoFacade.IncluirExtrato(this.extrato);
			this.pesquisarExtratos();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Inserido extrato do período "+ this.extrato.getPeriodo() +" com sucesso ");
			super.closeDialog();
			this.extrato = new Extrato();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Falha ao tentar inserir extrato "+ this.extrato.getPeriodo()  +" : "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void pesquisarExtratos(){
		try {
			if (this.dataPesquisa == null && this.produtorPesquisa.equals("")){
				this.setExtratos(extratoFacade.buscarExtrato());
			} else if (this.dataPesquisa != null && this.produtorPesquisa.equals("")) {
				this.setExtratos(extratoFacade.buscarPorPeriodo(sdf.format(this.dataPesquisa)));
			} else if (this.dataPesquisa != null && !this.produtorPesquisa.equals("")){
				this.setExtratos(extratoFacade.buscarPorPeriodoProdutor(sdf.format(this.dataPesquisa), this.produtorPesquisa));
			} else if (this.dataPesquisa == null && !this.produtorPesquisa.equals("")){
				this.setExtratos(extratoFacade.buscarPorProdutor(this.produtorPesquisa));
			} else {
				this.setExtratos(extratoFacade.buscarExtrato());
			}
			calcularTotais();
			
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar pesquisar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	private void calcularTotais(){
		extratoTotais = new Extrato();
		int periodo = 0;
		Double vlrSegEx = 0.0;
		Double vlrSegSis = 0.0;
		Double vlrRecSis = 0.0;
		Double vlrAcer = 0.0;
		Double vlrRetSoc = 0.0;
		Double vlrSalAcu = 0.0;
		Integer qtdProdutor = 0;
		for (Extrato extr : this.getExtratos()){
			if (periodo != extr.getMesReferencia()){
				vlrSegEx += extr.getVlrTotalSeguradorasExtrato();
				vlrSegSis += extr.getVlrTotalSeguradorasSistema();
			}
			qtdProdutor ++;
			vlrRecSis += extr.getVlrReceberSistema();
			vlrAcer += extr.getVlrAcerto();
			vlrRetSoc += extr.getVlrRetiradaSocio();
			vlrSalAcu += extr.getVlrSaldoAcumulado();
		
			periodo = extr.getMesReferencia().intValue();
		}
		extratoTotais.setVlrTotalSeguradorasExtrato(vlrSegEx);
		extratoTotais.setVlrTotalSeguradorasSistema(vlrSegSis);
		
		extratoTotais.setVlrReceberSistema(vlrRecSis);
		extratoTotais.setVlrAcerto(vlrAcer);
		extratoTotais.setVlrRetiradaSocio(vlrRetSoc);
		extratoTotais.setVlrSaldoAcumulado(vlrSalAcu);
		
		extratoTotais.setId(qtdProdutor);
	}
	
	public void atualizarExtrato(){
		System.out.println("atualizarExtrato");
		try {
			this.extratoFacade.atualizarExtrato(this.extrato);
			calcularTotais();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Atualizado extrato com sucesso ["+this.extrato.getProdutor()+"]");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar pesquisar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void atualizarPeriodo(Extrato extrato){
		System.out.println("atualizarPeriodo");
		try {
			this.extratoFacade.deletarPorPeriodo(extrato.getPeriodo());
			this.extratoFacade.IncluirExtrato(extrato);
			this.pesquisarExtratos();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Atualizado o período "+ extrato.getPeriodo() +" com sucesso ");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Falha ao tentar atualizar período "+ extrato.getPeriodo()  +" : "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void deletarExtrato(Extrato extrato){
		System.out.println("deletarExtrato");
		try {
			this.extratoFacade.deletar(extrato);
			this.extratos.remove(extrato);
			calcularTotais();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Deletado "+extrato.getProdutor()+" com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar deletar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void exibirSeguradoraExtrato(Extrato extrato){
		System.out.println("exibirSeguradoraExtrato");
		seguradoras = new ArrayList<>();
		try {
			seguradoras = this.extratoFacade.buscarSeguradoras(extrato);
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Falha ao buscar seguradoras: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void exibirSeguradoraSistema(Extrato extrato){
		System.out.println("exibirSeguradoraSistema");
		seguradoras = new ArrayList<>();
		try {
			seguradoras = this.extratoFacade.buscarSeguradorasSistema(extrato);
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Falha ao buscar seguradoras: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		try {
			//this.notaFacade.atualizarNota((Nota) event.getObject());
			this.extratoFacade.atualizarExtrato((Extrato) event.getObject());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Extrato) event.getObject()).getProdutor());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Extrato) event.getObject()).getProdutor()+ " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Extrato) event.getObject()).getProdutor());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}	

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	public Double getVlrExtrato() {
		return vlrExtrato;
	}

	public void setVlrExtrato(Double vlrExtrato) {
		this.vlrExtrato = vlrExtrato;
	}

	public List<Extrato> getExtratos() {
		return extratos;
	}

	public void setExtratos(List<Extrato> extratos) {
		this.extratos = extratos;
	}

	public Extrato getExtrato() {
		return extrato;
	}

	public void setExtrato(Extrato extrato) {
		this.extrato = extrato;
	}

	public Date getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date periodo) {
		this.periodo = periodo;
	}

	public String getProdutorPesquisa() {
		return produtorPesquisa;
	}

	public void setProdutorPesquisa(String produtorPesquisa) {
		this.produtorPesquisa = produtorPesquisa;
	}

	public Double getVlrSeguradoraExtrato() {
		return vlrSeguradoraExtrato;
	}

	public void setVlrSeguradoraExtrato(Double vlrSeguradoraExtrato) {
		this.vlrSeguradoraExtrato = vlrSeguradoraExtrato;
	}

	public Extrato getExtratoTotais() {
		return extratoTotais;
	}

	public void setExtratoTotais(Extrato extratoTotais) {
		this.extratoTotais = extratoTotais;
	}

	public String getSeguradorasExtrato() {
		return seguradorasExtrato;
	}

	public void setSeguradorasExtrato(String seguradorasExtrato) {
		this.seguradorasExtrato = seguradorasExtrato;
	}

	public List<SeguradorasExtrato> getSeguradoras() {
		return seguradoras;
	}

	public void setSeguradoras(List<SeguradorasExtrato> seguradoras) {
		this.seguradoras = seguradoras;
	}



}
