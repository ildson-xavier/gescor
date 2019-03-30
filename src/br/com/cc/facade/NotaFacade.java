package br.com.cc.facade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cc.dao.LancamentosDao;
import br.com.cc.dao.NotaDAO;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Nota;
import br.com.cc.entidade.NotaDetalhe;
import br.com.cc.entidade.NotaLancamento;
import br.com.cc.util.Util;

public class NotaFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private LancamentosDao lancamentosDao;
	private NotaDAO notaDAO;

	public NotaFacade() {
		lancamentosDao = new LancamentosDao();
		notaDAO = new NotaDAO();
	}

	public void atualizarNota(Nota nota) throws Exception {
		deletar(nota);
		IncluirNota(nota);
	}

	public void incluirExtrato(Date data, String seguradora, Double vlrExtrato) throws Exception {
		Nota nota = notaDAO.buscarNota(Util.formataMesAno(data), seguradora);
		if (nota != null) {
			nota.setVlrExtrato(vlrExtrato);
			notaDAO.salvar(nota);
		} else {
			throw new Exception("Não foi encontrado nota para " + Util.formataMesAno(data) + " e " + seguradora);
		}

	}

	public void incluirObservacao(Nota nota) throws Exception {
		if (nota != null && nota.getId() > 0){
			notaDAO.salvar(nota);
		} else {
			throw new Exception("Erro ao tentar atualizar a observação");
		}
		
	}

	@SuppressWarnings("null")
	public List<Nota> listarNotas(Date data, String seguradora) throws Exception {

		if ((data == null) && (seguradora == null || seguradora.equals(""))) {
			return notaDAO.buscarNotas();

		} else if ((data != null) && (seguradora == null || seguradora.equals(""))) {
			return this.listarNotaPorPeriodo(Util.formataMesAno(data));

		} else if ((data == null) && (seguradora != null || !seguradora.equals(""))) {
			return this.listarNotaPorSeguradora(seguradora);

		} else {
			return this.listarNotaPorSeguradoEPeriodo(seguradora, Util.formataMesAno(data));
		}

	}

	public List<Nota> listarNotaPorPeriodo(String periodo) throws Exception {
		return notaDAO.buscarNotaPorPeriodo(periodo);
	}

	public List<Nota> listarNotaPorSeguradora(String seguradora) throws Exception {
		return notaDAO.buscarNotaPorSeguradora(seguradora);
	}

	public List<Nota> listarNotaPorSeguradoEPeriodo(String seguradora, String periodo) throws Exception {
		return notaDAO.buscarNotaPorPeriodoESeguradora(periodo, seguradora);
	}

	public void deletar(Nota nota) throws Exception {
		notaDAO.deletar(nota);
	}

	/**
	 * Incluir o lancamento de nota
	 * 
	 * @param data
	 * @param seguradora
	 * @throws Exception
	 */
	public void IncluirNota(Nota not) throws Exception {
		List<Lancamentos> lancamentos = lancamentosDao.getLancamentosPorPeriodoESeguradora(not.getPeriodo(),
				not.getSeguradora());

		// Verifica se ja existe o lancamento
		List<Nota> listaDeNotas = this.listarNotaPorSeguradoEPeriodo(not.getSeguradora(), not.getPeriodo());

		if (listaDeNotas.isEmpty()) {
			Nota nota = criarNota(lancamentos, not);

			List<NotaDetalhe> detalhes = criarNotaDetalhe(lancamentos, nota);

			List<NotaLancamento> lan = criarNotaLancamento(lancamentos, nota);
			java.util.Collections.sort(lan);

			nota.setLancamento(lan);
			nota.setDetalhe(detalhes);
			nota.setQtqArquivosComissao(lan.size());

			notaDAO.salvar(nota);
		} else {
			if (listaDeNotas.size() == 1) {
				Nota nota = listaDeNotas.get(0);
				BigDecimal valorSalvo = new BigDecimal(nota.getVlrNota()).setScale(2, RoundingMode.HALF_DOWN);
				BigDecimal valorEntrada = new BigDecimal(not.getVlrNota()).setScale(2, RoundingMode.HALF_DOWN);
				if (valorSalvo.doubleValue() != valorEntrada.doubleValue() ){
					nota.setVlrNota(not.getVlrNota());
					notaDAO.salvar(nota);
				} else {
					throw new Exception(
							"Já existe um lançamento para [" + not.getSeguradora() + "] em [" + not.getPeriodo() + "]");
				}
				
			}
			
		}

	}

	/**
	 * Cria o objeto Nota
	 * 
	 * @param lancamentos
	 * @return
	 */
	private Nota criarNota(List<Lancamentos> lancamentos, Nota not) {
		Double valor = 0.0;
		for (Lancamentos l : lancamentos) {
			valor += l.getComissao();
		}

		Nota nota = new Nota();
		nota.setVlrComissao(valor);
		nota.setPeriodo(not.getPeriodo());
		nota.setVlrNota(not.getVlrNota());
		nota.setSeguradora(not.getSeguradora());
		nota.setVlrExtrato(not.getVlrExtrato());
		nota.setObservacao(not.getObservacao());
		return nota;
	}
	

	/**
	 * Cria a nota detalhe
	 * 
	 * @param lancamentos
	 * @return
	 */
	private List<NotaDetalhe> criarNotaDetalhe(List<Lancamentos> lancamentos, Nota not) {
		Map<String, NotaDetalhe> map = new HashMap<>();

		NotaDetalhe nd;
		for (Lancamentos l : lancamentos) {
			nd = new NotaDetalhe();
			nd.setProdutor(l.getProdutor());
			nd.setVlrComissaoBruto(l.getComissao());
			nd.setVlrImposto(l.getValor2());
			nd.setVlrLiquido(l.getValor3());
			nd.setNota(not);
			map = sumariarDetalhe(map, nd);
		}
		nd = null;
		return new ArrayList<>(map.values());
	}

	/**
	 * Sumaria os valores
	 * 
	 * @param map
	 * @param detalhe
	 * @return
	 */
	private Map<String, NotaDetalhe> sumariarDetalhe(Map<String, NotaDetalhe> map, NotaDetalhe detalhe) {
		Map<String, NotaDetalhe> mapa = map;
		NotaDetalhe nota = mapa.get(detalhe.getProdutor());
		if (nota != null) {
			nota.setVlrComissaoBruto(nota.getVlrComissaoBruto() + detalhe.getVlrComissaoBruto());
			nota.setVlrImposto(nota.getVlrImposto() + detalhe.getVlrImposto());
			nota.setVlrLiquido(nota.getVlrLiquido() + detalhe.getVlrLiquido());

			mapa.put(nota.getProdutor(), nota);
		} else {
			mapa.put(detalhe.getProdutor(), detalhe);
		}

		return mapa;
	}

	/**
	 * Cria nota lancamento
	 * 
	 * @param lancamentos
	 * @return
	 */
	private List<NotaLancamento> criarNotaLancamento(List<Lancamentos> lancamentos, Nota not) {
		Map<Date, NotaLancamento> map = new HashMap<>();

		NotaLancamento nl;
		for (Lancamentos l : lancamentos) {
			nl = new NotaLancamento();
			nl.setPeriodo(l.getPeriodo());
			nl.setSeguradora(l.getSeguradora());
			nl.setVlrComissaoBruto(l.getComissao());
			nl.setVlrImposto(l.getValor2());
			nl.setVlrLiquido(l.getValor3());
			nl.setNota(not);
			map = sumarizarLancamento(map, nl);
		}
		nl = null;
		return new ArrayList<>(map.values());
	}

	/**
	 * Sumariza os lancamentos por periodo
	 * 
	 * @param map
	 * @param lancamento
	 * @return
	 */
	private Map<Date, NotaLancamento> sumarizarLancamento(Map<Date, NotaLancamento> map, NotaLancamento lancamento) {
		Map<Date, NotaLancamento> mapa = map;
		NotaLancamento nota = mapa.get(lancamento.getPeriodo());
		if (nota != null) {
			nota.setVlrComissaoBruto(nota.getVlrComissaoBruto() + lancamento.getVlrComissaoBruto());
			nota.setVlrImposto(nota.getVlrImposto() + lancamento.getVlrImposto());
			nota.setVlrLiquido(nota.getVlrLiquido() + lancamento.getVlrLiquido());

			mapa.put(nota.getPeriodo(), nota);
		} else {
			mapa.put(lancamento.getPeriodo(), lancamento);
		}

		return mapa;
	}

	public static void main(String[] args) throws Exception {
		NotaFacade nf = new NotaFacade();
		Nota nota = new Nota();

		nota.setPeriodo("03/2017");
		nota.setSeguradora("Azul");
		nota.setVlrNota(10000.0);
		nf.IncluirNota(nota);

		// List<Nota> lista = nf.listarNotas();
		// for (Nota n : lista){
		// System.out.println(n.getId() + " > "+ n.getPeriodo());
		// }

		// nf.atualizarNota(lista.get(0));
	}
}
