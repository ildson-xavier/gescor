package br.com.cc.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.cc.dao.ApoliceVeiculoDAO;
import br.com.cc.dao.OficinaDAO;
import br.com.cc.dao.ProdutorDao;
import br.com.cc.dao.SinistroDAO;
import br.com.cc.dao.SinistroHistoricoDAO;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.sinistro.ApoliceVeiculo;
import br.com.cc.entidade.sinistro.Filtro;
import br.com.cc.entidade.sinistro.Oficina;
import br.com.cc.entidade.sinistro.Sinistro;
import br.com.cc.entidade.sinistro.SinistroHistorico;
import br.com.cc.entidade.sinistro.StatusSinistro;

public class SinistroFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProdutorDao produtorDao;
	private ApoliceVeiculoDAO veiculoDAO;
	private SinistroDAO sinistroDAO;
	private OficinaDAO oficinaDAO;
	private SinistroHistoricoDAO historicoDAO;

	private List<Produtor> produtores;
	private List<Oficina> oficinas;
	private Sinistro sinistro;

	public SinistroFacade() {
		this.produtorDao = new ProdutorDao();
		this.veiculoDAO = new ApoliceVeiculoDAO();
		this.sinistroDAO = new SinistroDAO();
		this.oficinaDAO = new OficinaDAO();
		this.produtores = new ArrayList<>();
		this.oficinas = new ArrayList<>();
		this.setHistoricoDAO(new SinistroHistoricoDAO());
	}

	public Sinistro salvar(Sinistro sinistro) throws Exception {
		Oficina oficina = null;
		Produtor produtor = null;
		ApoliceVeiculo apoliceVeiculo = null;
		
		if (sinistro.getProdutor().getId() == null || sinistro.getProdutor().getId() == 0){
			produtor = this.produtorDao.atualizarProdutor(sinistro.getProdutor());
			System.out.println("Salva produtor");
			sinistro.setProdutor(produtor);
		} else {
			System.out.println("Atualiza produtor "+sinistro.getProdutor().getSegurado());
			this.produtorDao.atualizarProdutor(sinistro.getProdutor());
		}
		
		if (sinistro.getVeiculo().getId() == null || sinistro.getVeiculo().getId() == 0){
			System.out.println("Salva veiculo");
			apoliceVeiculo = this.veiculoDAO.atualizarApoliceVeiculo(sinistro.getVeiculo());
			sinistro.setVeiculo(apoliceVeiculo);
		} else {
			System.out.println("Atualiza veiculo");
			this.veiculoDAO.atualizarApoliceVeiculo(sinistro.getVeiculo());
		}
		
		
		if (sinistro.getOficina().getId() == null || sinistro.getOficina().getId() == 0){
			System.out.println("Salva oficina");
			oficina = this.oficinaDAO.salvar(sinistro.getOficina());
			sinistro.setOficina(oficina);
		} else {
			System.out.println("Atualiza oficina");
			this.oficinaDAO.salvar(sinistro.getOficina());
		}
		
		if (sinistro.getId() != null){
			System.out.println("Salva ou atualiza sinistro");
			this.sinistro = this.sinistroDAO.salvar(sinistro);
		} else {
			sinistro.setDataCriacao(Calendar.getInstance().getTime());
			sinistro.setStatus(StatusSinistro.AGUARDANDO_ANALISE);
			this.sinistro = this.sinistroDAO.salvar(sinistro);
			System.out.println("Salva historico");
			this.salvarHistorico(new SinistroHistorico());
		}
		
		return this.sinistro;
	}
	
	public void salvarHistorico(SinistroHistorico historico)  throws Exception{
		
		if (historico.getSinistro() == null){
			historico.setSinistro(this.sinistro);
			historico.setStatus(StatusSinistro.AGUARDANDO_ANALISE.getDescricao());
			historico.setDescricao("O veículo está pendente de avaliação.");
			historico.setData(Calendar.getInstance().getTime());
		} else {
			historico.setStatus(historico.getSinistro().getStatus().getDescricao());
			historico.setData(historico.getSinistro().getDataCriacao());
		}
		this.historicoDAO.salvar(historico);
		
	}
	
	public void atualizar(Sinistro sinistro) throws Exception{
		if (sinistro.getStatus().getDescricao().equals(StatusSinistro.CONCLUIDO.getDescricao())){
			sinistro.setDataConclusao(Calendar.getInstance().getTime());
		}
		this.sinistroDAO.salvar(sinistro);
	}
	
	public void atualizarSinistro(Sinistro sinistro) throws Exception {
		System.out.println("incluirObservacao "+sinistro.getStatus().getDescricao());
		if (sinistro != null && sinistro.getId() > 0){
			this.sinistroDAO.salvar(sinistro);
		} else {
			throw new Exception("Erro ao tentar atualizar a observação");
		}
		
	}

	public List<Sinistro> sinistros(Filtro filtro) throws Exception {
		return this.sinistroDAO.listarSinistros(filtro);
	}

	public List<Produtor> produtorPorSegurado(String nome) throws Exception {
		return this.produtorDao.getProdutoresPorSegurado(nome);
	}
	
	public List<Oficina> listarOficinaPorNome(String nome) throws Exception {
		return this.oficinaDAO.listarOficinasPorNome(nome);
	}

	public ApoliceVeiculo apoliceVeiculoPorCodigoSegurado(int codigo) throws Exception {
		List<ApoliceVeiculo> lista = this.veiculoDAO.getApoliceVeiculoPorSegurado(codigo);
		if (lista.size() > 0) {
			return lista.get(0);
		}
		return new ApoliceVeiculo();
	}
	
	public List<String> autoCompletarOficina(String query) throws Exception {
		List<String> results = new LinkedList<String>();
			this.oficinas = this.listarOficinaPorNome(query);
			for (Oficina p : this.oficinas) {
				results.add(p.getNome());
			}
		return results; 
	}
	
	public Oficina carregarDadosOficina(String nome) {
		for (Oficina p : this.oficinas) {
			if (p.getNome().contains(nome)) {
				return p;
			}
		}
		return new Oficina();
	}

	public List<String> autoCompletar(String query) throws Exception {
		List<String> results = new LinkedList<String>();
			this.produtores = this.produtorPorSegurado(query);
			for (Produtor p : this.produtores) {
				results.add(p.getSegurado());
			}
		return results; 
	}

	public Produtor carregarDadosSegurado(String segurado) {
		for (Produtor p : this.produtores) {
			if (p.getSegurado().contains(segurado)) {
				return p;
			}
		}
		return new Produtor();
	}
	
	public void deletar(Sinistro sinistro) throws Exception{
		this.sinistroDAO.remover(sinistro);
	}

	public SinistroHistoricoDAO getHistoricoDAO() {
		return historicoDAO;
	}

	public void setHistoricoDAO(SinistroHistoricoDAO historicoDAO) {
		this.historicoDAO = historicoDAO;
	}

}
