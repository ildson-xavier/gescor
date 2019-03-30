/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.Produtor;
import br.com.cc.util.JSFMessageUtil;

/**
 *
 * @author Usuario
 */
@ManagedBean(name = "produtorView")
@ViewScoped
public class ProdutorView implements Serializable {

	private static final long serialVersionUID = 1L;
	private Produtor produtorBean = new Produtor();
	private List<Produtor> produtores = new LinkedList<Produtor>();
	private ProdutorDao produtorDao = new ProdutorDao();
	private String nomeProdutor;
	private String nomeSegurado;
	private String cpf;
	private String apolice;
	private boolean situacao;

	/**
	 * Creates a new instance of ProdutorView
	 */
	public ProdutorView() {
		// Util.verificaMemoria("ccomissao");
	}

	public String adicionarProdutor() {
		System.out.println("adicionarProdutor");
		try {
			if (produtorBean.getProdutor().length() == 0 || produtorBean.getSegurado().length() == 0) {

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
						"É necessario informar o nome do produtor e do segurando.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return "produtorview.jsf";
			}
			aumentarFonte(produtorBean);
			produtorDao.adicionarProdutor(produtorBean);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK! ",
					"Produtor [" + produtorBean.getProdutor() + "" + "] e segurado [" + produtorBean.getSegurado()
							+ "] salvos com sucesso.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel salvar o produtor [" + produtorBean.getProdutor() + "" + "] e segurado ["
							+ produtorBean.getSegurado() + "] " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
		produtorBean = new Produtor();
		return "produtorview.jsf";
	}

	public void aumentarFonte(Produtor p) {
		p.setProdutor(produtorBean.getProdutor().toUpperCase());
		p.setSegurado(produtorBean.getSegurado().toUpperCase());
	}

	public void adicionarProdutores(List<Produtor> list) {
		System.out.println("adicionarProdutores");
		if (list.size() > 0) {
			for (Produtor p : list) {
				try {

					if (produtorDao.getProdutoresPorSegurado(p.getSegurado()).size() == 0) {
						produtorDao.adicionarProdutor(p);
					}
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
							"Não foi possivel salvar o produtor [" + p.getProdutor() + "" + "] e segurado ["
									+ p.getSegurado() + "] " + ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					ex.printStackTrace();
				}
			}

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Produtores salvos com sucesso!", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
					"Lista de produtores vazia.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		}

	}

	public void removerProdutor() {
		System.out.println("removerProdutor");
		try {
			produtorDao.removerProdutor(produtorBean);
			produtores.remove(produtorBean);
			JSFMessageUtil.sendInfoMessageToUser("",
					"Produtor [" + produtorBean.getSegurado() + " - " + produtorBean.getProdutor() + "] removido");
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Nao foi possivel remover produtor [" + produtorBean.getProdutor() + "] " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public String pesquisarProdutorPorFiltro() {
		System.out.println("pesquisarProdutorPorFiltro");

		if ((getNomeProdutor() != null && getNomeProdutor().length() > 0)
				&& (getNomeSegurado() != null && getNomeSegurado().length() > 0)) {
			try {
				produtores = produtorDao.getProdutoresESegurados(nomeProdutor, nomeSegurado);
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
						"Não foi possivel listar os produtores" + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}

		} else if (getNomeProdutor() != null && getNomeProdutor().length() > 0) {
			try {
				produtores = produtorDao.getProdutoresPorProdutor(nomeProdutor);
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
						"Não foi possivel listar os produtores" + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}

		} else if (getNomeSegurado() != null && getNomeSegurado().length() > 0) {
			try {
				produtores = produtorDao.getProdutoresPorSegurado(nomeSegurado);
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
						"Não foi possivel listar os segurados" + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}

		} else if (this.cpf != null && this.cpf.length() > 0) {
			try {
				produtores = produtorDao.getProdutorPorCpf(this.cpf);
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
						"Não foi possivel listar os segurados" + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}

		} else {
			try {
				produtores = produtorDao.getProdutores();
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
						"Não foi possivel listar os produtores" + ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}

		}

		return "produtorview.jsf";
	}

//	public void addMessage() {
//        String summary = this.situacao ? "Cpf" : "Cnpf";
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
//    }
	
	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		System.out.println("onEdit");
		try {
			produtorDao.atualizarProdutor(((Produtor) event.getObject()));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado com sucesso ",
					((Produtor) event.getObject()).getSegurado());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar",
					((Produtor) event.getObject()).getSegurado() + " " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelar atualização",
				((Produtor) event.getObject()).getSegurado());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Produtor getProdutorBean() {
		return produtorBean;
	}

	public void setProdutorBean(Produtor produtorBean) {
		this.produtorBean = produtorBean;
	}

	public String getNomeProdutor() {
		return nomeProdutor;
	}

	public void setNomeProdutor(String nomeProdutor) {
		this.nomeProdutor = nomeProdutor;
	}

	public String getNomeSegurado() {
		return nomeSegurado;
	}

	public void setNomeSegurado(String nomeSegurado) {
		this.nomeSegurado = nomeSegurado;
	}

	public List<Produtor> getProdutores() {
		return produtores;
	}

	public void setProdutores(List<Produtor> produtores) {
		this.produtores = produtores;
	}

	public String getApolice() {
		return apolice;
	}

	public void setApolice(String apolice) {
		this.apolice = apolice;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public boolean isSituacao() {
		return situacao;
	}

	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

}
