package br.com.cc.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "temaService", eager = true)
@ApplicationScoped
public class TemaService implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<String> temas;
	private String temaEscolhido = "excite-bike";

	public List<String> getTemas() {
		return temas;
	}

	public void setTemas(List<String> temas) {
		this.temas = temas;
	}

	public String getTemaEscolhido() {
		
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if( params.containsKey(temaEscolhido) ){
			temaEscolhido = params.get(temaEscolhido);
		}
		
		return temaEscolhido;
	}

	public void setTemaEscolhido(String temaEscolhido) {
		this.temaEscolhido = temaEscolhido;
	}

}
