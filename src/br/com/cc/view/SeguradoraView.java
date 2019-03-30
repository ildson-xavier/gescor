package br.com.cc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cc.dao.SeguradoraDAO;
import br.com.cc.entidade.Seguradora;

@ViewScoped
@ManagedBean(name="seguradoraView")
public class SeguradoraView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Seguradora seguradoraBean;
	private SeguradoraDAO seguradoraDAO = new SeguradoraDAO();
	private List<Seguradora> seguradoras;
	
	public void adicionar(){
		seguradoraDAO.adicionar(this.seguradoraBean);
	}

	@PostConstruct
	public void ini(){
		seguradoras = new ArrayList<>();
		seguradoraBean = new Seguradora();
		carregarSeguradoras();
	}
	
	private void carregarSeguradoras(){
		if (this.seguradoras.size() == 0){
			this.seguradoras = seguradoraDAO.getSeguradoras();
		}
	}
	
	public List<Seguradora> listarSeguradoras(){
		if (this.seguradoras.size() == 0){
			this.seguradoras = seguradoraDAO.getSeguradoras();
		}
		return this.seguradoras;
	}

	public Seguradora getSeguradoraBean() {
		return seguradoraBean;
	}

	public void setSeguradoraBean(Seguradora seguradoraBean) {
		this.seguradoraBean = seguradoraBean;
	}
	
}
