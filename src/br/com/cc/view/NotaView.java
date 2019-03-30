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

import br.com.cc.entidade.Nota;
import br.com.cc.facade.NotaFacade;
import br.com.cc.util.Util;

@ViewScoped
@ManagedBean(name="notaView")
public class NotaView extends AbstractMB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf;
	
	private List<Nota> notas;
	private NotaFacade notaFacade;
	
	private Nota lancamentos;
	
	private Double vlrComissao;
	private Double vlrNota;
	private Double vlrExtrato;
	
	private Date dataInclusao;
	private Date dataExtrato;
	private String seguradoraInclusao;
	private String seguradoraExtrato;
	private Double valor;
	private Double valorExtrato;
	
	private Date dataPesquisa = null;
	private String seguradoraPesquisa = "";
	
	private Nota notaBean;
	
	@PostConstruct
	public void init(){
		sdf = new SimpleDateFormat("MM/yyyy");
		notaFacade = new NotaFacade();
		notas  = new ArrayList<>();
		
		vlrComissao = 0.0;
		vlrNota = 0.0;
		setVlrExtrato(0.0);
		
		valor = 0.0;
		setValorExtrato(0.0);
		
		notaBean = new Nota();

	}
	
	public void incluirExtrato(){
		System.out.println("incluirExtrato");
		try {
			this.notaFacade.incluirExtrato(this.dataExtrato, this.seguradoraExtrato, this.valorExtrato);
			this.dataExtrato = null;
			this.seguradoraExtrato = "";
			this.valorExtrato = 0.0;
			this.pesquisarNotas();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Atualizado "+this.seguradoraExtrato+" com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar atualizar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void incluirNota(){
		System.out.println("incluirNota");
		Nota nota = new Nota();

		nota.setPeriodo(sdf.format(this.dataInclusao));
		nota.setSeguradora(this.seguradoraInclusao);
		nota.setVlrNota(this.valor);
		
		this.dataInclusao = null;
		this.seguradoraInclusao = "";
		this.valor = 0.0;
		
		try {
			this.notaFacade.IncluirNota(nota);
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Inserido "+ nota.getSeguradora() +" com sucesso ");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar inserir "+ nota.getSeguradora() +" : "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	private void calcularTotais(){
		this.vlrComissao = 0.0;
		this.vlrNota = 0.0;
		this.vlrExtrato = 0.0;
		if (this.notas.size() > 0){
			for (Nota n : this.notas){
				this.vlrComissao += validarValor(n.getVlrComissao());
				this.vlrNota += validarValor(n.getVlrNota());
				this.vlrExtrato += validarValor(n.getVlrExtrato());
			}
		}
	}
	
	private Double validarValor(Double valor){
		if (valor == null){
			return 0.0;
		}
		return valor;
	}
	
	public void pesquisarNotas(){
		Util.verificaMemoria("ccomissao-pesquisarNotas");
		System.out.println("pesquisarNotas");
		try {
			this.notas = notaFacade.listarNotas(this.dataPesquisa, this.seguradoraPesquisa);
			this.calcularTotais();
			this.dataPesquisa = null;
			this.seguradoraPesquisa = "";
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar pesquisar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void atualizarNota(Nota nota){
		System.out.println("atualizarNota");
		try {
			this.notaFacade.atualizarNota(nota);
			this.pesquisarNotas();
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Atualizado "+nota.getSeguradora()+" com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar atualizar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void deletarNota(Nota nota) {
		System.out.println("deletarNota");
		try {
			this.notaFacade.deletar(nota);
			//this.notas = new ArrayList<>();
			this.notas.remove(nota);
			this.vlrComissao -= nota.getVlrComissao();
			this.vlrNota -= nota.getVlrNota();
			
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Deletado "+nota.getSeguradora()+" com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar atualizar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	public void mostrarObservacao(Nota nota){
		this.notaBean = nota;
		
	}
	
	public void atualizarObservacao(){
		System.out.println("atualizarObservacao");
		try {
			this.notaFacade.incluirObservacao(this.notaBean);
			super.keepDialogOpen();
			super.displayInfoMessageToUser("Observação para "+this.notaBean.getSeguradora()+" atualizada com sucesso");
			super.closeDialog();
		} catch (Exception e) {
			super.keepDialogOpen();
			super.displayErrorMessageToUser("Erro ao tentar atualizar: "+e.getMessage());
			super.closeDialog();
			e.printStackTrace();
		}
	}
	
	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		System.out.println("onEdit");
		try {
			this.notaFacade.atualizarNota((Nota) event.getObject());
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Nota) event.getObject()).getSeguradora());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Nota) event.getObject()).getSeguradora() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Nota) event.getObject()).getSeguradora());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String notaView(){
		return "notaview?faces-redirect=true";
	}
	
	public String dataAgora(){
		System.out.println("Data: "+dataInclusao);
		return "";
	}
	
	public void notaLancamento(){
		System.out.println(lancamentos.getLancamento().size());
	}

	public List<Nota> getNotas() {
		return notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public Nota getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(Nota lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Double getVlrComissao() {
		return vlrComissao;
	}


	public Double getVlrNota() {
		return vlrNota;
	}

	public void setVlrNota(Double vlrNota) {
		this.vlrNota = vlrNota;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	public String getSeguradoraPesquisa() {
		return seguradoraPesquisa;
	}

	public void setSeguradoraPesquisa(String seguradoraPesquisa) {
		this.seguradoraPesquisa = seguradoraPesquisa;
	}

	public Double getValorExtrato() {
		return valorExtrato;
	}

	public void setValorExtrato(Double valorExtrato) {
		this.valorExtrato = valorExtrato;
	}

	public Double getVlrExtrato() {
		return vlrExtrato;
	}

	public void setVlrExtrato(Double vlrExtrato) {
		this.vlrExtrato = vlrExtrato;
	}

	public Nota getNotaBean() {
		return notaBean;
	}

	public void setNotaBean(Nota notaBean) {
		this.notaBean = notaBean;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Date getDataExtrato() {
		return dataExtrato;
	}

	public void setDataExtrato(Date dataExtrato) {
		this.dataExtrato = dataExtrato;
	}

	public String getSeguradoraExtrato() {
		return seguradoraExtrato;
	}

	public void setSeguradoraExtrato(String seguradoraExtrato) {
		this.seguradoraExtrato = seguradoraExtrato;
	}

	public String getSeguradoraInclusao() {
		return seguradoraInclusao;
	}

	public void setSeguradoraInclusao(String seguradoraInclusao) {
		this.seguradoraInclusao = seguradoraInclusao;
	}

}
