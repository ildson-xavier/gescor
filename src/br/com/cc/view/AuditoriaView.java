package br.com.cc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cc.dao.AuditoriaDAO;
import br.com.cc.entidade.Auditoria;
import br.com.cc.util.JSFMessageUtil;

@ViewScoped
@ManagedBean(name="auditoriaView")
public class AuditoriaView {

	private AuditoriaDAO auditoriaDAO;
	private List<Auditoria> auditorias;
	
	private Date dtInicio;
	private Date dtFim;
	
	@PostConstruct
	public void init(){
		if (auditoriaDAO == null){
			auditoriaDAO = new AuditoriaDAO();
		}
		
		if (auditorias == null){
			auditorias = new ArrayList<>();
		}
		
	}
	
	public void pesquisar(){
		try {
			if (this.dtInicio != null && this.dtFim != null){
				this.setAuditorias(this.auditoriaDAO.buscarAuditoria(dtInicio, dtFim));
			} else {
				this.setAuditorias(this.auditoriaDAO.buscarAuditoria());
			}
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao buscar log de auditoria", e.getMessage());
		}
		
		
	}
	
	public String auditoria(){
		return "auditoria?faces-redirect=true";
	}

	public List<Auditoria> getAuditorias() {
		return auditorias;
	}

	public void setAuditorias(List<Auditoria> auditorias) {
		this.auditorias = auditorias;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}
	
}
