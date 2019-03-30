package br.com.cc.apolice.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cc.dao.ApoliceDAO;
import br.com.cc.dao.ProdutorDao;
import br.com.cc.dao.VeiculoDAO;
import br.com.cc.entidade.ApoliceBean;
import br.com.cc.entidade.Corretora;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.VeiculoBean;
import br.com.cc.service.cep.ServicoCep;
import br.com.cc.util.JSFMessageUtil;
import br.com.cc.valida.ValidaCNPJ;
import br.com.cc.valida.ValidaCPF;

@ManagedBean(name = "cadastroApolice")
@ViewScoped
public class CadastroApolice implements Serializable{

	private static final long serialVersionUID = 1L;

	private ApoliceBean apoliceBean;
	private VeiculoBean veiculoBean;
	private Produtor segurado;
	private ServicoCep cep;
	private ProdutorDao produtorDao;
	private VeiculoDAO veiculoDAO;
	private ApoliceDAO apoliceDAO;
	private String nomeSegurado;
	private String placa;
	
	private List<Produtor> segurados;
	private List<VeiculoBean> veiculos;
	
	public CadastroApolice() {
		setApoliceBean(new ApoliceBean());
		setVeiculoBean(new VeiculoBean());
		setSegurado(new Produtor());
		produtorDao = new ProdutorDao();
		segurados = new ArrayList<>();
		veiculos = new ArrayList<>();
		veiculoDAO = new VeiculoDAO();
		apoliceDAO = new ApoliceDAO();
	}
	
	public void cadastrarApolice(){
		System.out.println("cadastrarApolice");

		try {
			this.veiculoBean = this.veiculoDAO.atualizarVeiculo(veiculoBean);
			this.apoliceBean.setVeiculo(veiculoBean);
			this.segurado = this.produtorDao.atualizarProdutor(segurado);
			this.apoliceBean.setSegurado(segurado);
			this.apoliceDAO.atualizarApolice(apoliceBean);
			limpar();
			JSFMessageUtil.sendInfoMessageToUser("Apolice salvar com sucesso.", "");
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao salvar apolice", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void limpar(){
		setApoliceBean(new ApoliceBean());
		setVeiculoBean(new VeiculoBean());
		setSegurado(new Produtor());
	}
	
	public void obterCep() {
		try {
			cep = new ServicoCep(this.segurado.getCep());
			Corretora corr = cep.obterDados();
			this.segurado.setBairro(corr.getBairro());
			this.segurado.setCidade(corr.getCidade());
			this.segurado.setEndereco(corr.getEndereco());
			this.segurado.setUf(corr.getUf());
		} catch (Exception e) {
			JSFMessageUtil.sendAlertMessageToUser("Cep não localizado.", e.getMessage());
		}
	}
	
	public void validarIdentidade() {
		boolean status = false;
		String identidade = this.segurado.getCpf().replace(".", "").replace(".", "").replace(".", "")
				.replace("/", "").replaceAll("-", "");
		if (identidade.length() <= 11) {
			status = ValidaCPF.isCPF(identidade);
			if (status == false) {
				JSFMessageUtil.sendAlertMessageToUser("CPF informado está inválido.", "");
			}
		} else {
			status = ValidaCNPJ.isCNPJ(identidade);
			if (status == false) {
				JSFMessageUtil.sendAlertMessageToUser("CNPJ informado está inválido.", "");
			}
		}
	}
	
	public List<String> autoCompletar(String query) {
		segurados = new ArrayList<Produtor>();
		List<String> results = new ArrayList<String>();
		try {
			segurados = produtorDao.getProdutoresPorSegurado(query);
			for (Produtor p : segurados) {
				results.add(p.getSegurado());
			}
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel completar o nome do segurado " + " " + ex.getMessage());
			ex.printStackTrace();
		}
		return results;
	}
	
	public void obterSegurado() {
		for (Produtor p : segurados) {
			if (p.getSegurado().contains(segurado.getSegurado())) {
				this.segurado = p;
				break;
			}
		}
	}
	
	public List<String> autoCompletarVeiculo(String query) {
		veiculos = new ArrayList<VeiculoBean>();
		List<String> results = new ArrayList<String>();
		try {
			veiculos = veiculoDAO.getVeiculoPorPlacas(query);
			for (VeiculoBean p : veiculos) {
				results.add(p.getPlaca());
			}
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel completar o nome do segurado " + " " + ex.getMessage());
			ex.printStackTrace();
		}
		return results;
	}
	
	public void obterVeiculo() {
		for (VeiculoBean p : veiculos) {
			if (p.getPlaca().contains(p.getPlaca())) {
				this.veiculoBean = p;
				break;
			}
		}
	}

	public ApoliceBean getApoliceBean() {
		return apoliceBean;
	}

	public void setApoliceBean(ApoliceBean apoliceBean) {
		this.apoliceBean = apoliceBean;
	}

	public VeiculoBean getVeiculoBean() {
		return veiculoBean;
	}

	public void setVeiculoBean(VeiculoBean veiculoBean) {
		this.veiculoBean = veiculoBean;
	}

	public Produtor getSegurado() {
		return segurado;
	}

	public void setSegurado(Produtor segurado) {
		this.segurado = segurado;
	}

	public List<Produtor> getSegurados() {
		return segurados;
	}

	public void setSegurados(List<Produtor> segurados) {
		this.segurados = segurados;
	}

	public String getNomeSegurado() {
		return nomeSegurado;
	}

	public void setNomeSegurado(String nomeSegurado) {
		this.nomeSegurado = nomeSegurado;
	}

	public List<VeiculoBean> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<VeiculoBean> veiculos) {
		this.veiculos = veiculos;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
}
