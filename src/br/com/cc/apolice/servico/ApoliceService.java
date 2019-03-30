package br.com.cc.apolice.servico;

import java.io.Serializable;

import javax.persistence.NoResultException;

import br.com.cc.apolice.Appolice;
import br.com.cc.dao.ApoliceDAO;
import br.com.cc.dao.ProdutorDao;
import br.com.cc.dao.VeiculoDAO;
import br.com.cc.entidade.ApoliceBean;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.VeiculoBean;
import br.com.cc.util.Util;

public class ApoliceService implements Serializable{

	private static final long serialVersionUID = 1L;
	//private ApoliceMapfre apoliceMapfre;
	private Appolice appolice;
	private ApoliceDAO apoliceDAO;
	private VeiculoDAO veiculoDAO;
	private ProdutorDao seguradoDao;
	private String path;
	
	public ApoliceService(Appolice appolice) {
		apoliceDAO = new ApoliceDAO();
		veiculoDAO = new VeiculoDAO();
		seguradoDao = new ProdutorDao();
		this.appolice = appolice;
	}
	
	public void cadastrarApolice(String path) throws Exception{
		this.path = path;
		//this.appolice = new ApoliceMapfre(path);
		this.appolice.carregarApolice(path);
		this.appolice.pegarDadosGerais();
		this.appolice.pegarDadosDoSegurado();
		this.appolice.pegarDadosDoVeiculo();
		this.appolice.fechar();
		ApoliceBean apoliceBean = appolice.gerarApolice();
		this.salvarApolice(apoliceBean);
		
	}
	
	public void salvarApolice(ApoliceBean bean) throws Exception{
        if (this.verificarSeApoliceJaExiste(bean) == null){
        	if (this.verificarSeSeguradoJaExiste(bean.getSegurado()) != null){
        		bean.setSegurado(seguradoDao.getProdutorPorCpf2(bean.getSegurado().getCpf()));
        	} else{
        		Produtor segurado = seguradoDao.atualizarProdutor(bean.getSegurado());
        		bean.setSegurado(segurado);
        	}
        	if (this.verificarSeVeiculoJaExiste(bean.getVeiculo()) != null){
        		bean.setVeiculo(veiculoDAO.getVeiculoPorPlaca(bean.getVeiculo().getPlaca()));
        	} else{
        		VeiculoBean veiculo = veiculoDAO.atualizarVeiculo(bean.getVeiculo());
        		bean.setVeiculo(veiculo);
        	}
        	bean.setDataImportacao(Util.dataAtual());
        	bean.setPath(this.path);
        	bean.setArquivo(this.nomePdf());
        	apoliceDAO.atualizarApolice(bean);
        } else {
        	throw new Exception("A apolice já está importada.");
        }
	}
	
	public ApoliceBean verificarSeApoliceJaExiste(ApoliceBean bean) throws Exception{
		ApoliceBean apolice = null;
		try {
			apolice = apoliceDAO.getNumeroApolice(bean.getNumeroApolice());
		} catch (NoResultException e) {
			apolice = null;
		} catch (Exception e) {
			throw new Exception("Falha ao verificar se apolice ja foi importada. "+e.getMessage());
		}
		return apolice;
	}
	
	public Produtor verificarSeSeguradoJaExiste(Produtor segurado) throws Exception{
		Produtor produtor = null;
		try {
			produtor = seguradoDao.getProdutorPorCpf2(segurado.getCpf());
		} catch (NoResultException e) {
			produtor = null;
		} catch (Exception e) {
			throw new Exception("Falha ao verificar se segurado ja existe. "+e.getMessage());
		}
		return produtor;
	}
	
	public VeiculoBean verificarSeVeiculoJaExiste(VeiculoBean bean) throws Exception{
		VeiculoBean veiculo = null;
		try {
			veiculo = veiculoDAO.getVeiculoPorPlaca(bean.getPlaca());
		} catch (NoResultException e) {
			veiculo = null;
		} catch (Exception e) {
			throw new Exception("Falha ao verificar se veiculo ja foi existe. "+e.getMessage());
		}
		return veiculo;
	}
	
	private String nomePdf(){
		String nome = "";
		int posicaoInicial = this.path.indexOf("processados");
		nome = this.path.substring(posicaoInicial + "processados".length() + 1,  this.path.length());
		return nome;
	}
}
