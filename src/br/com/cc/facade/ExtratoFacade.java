package br.com.cc.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import br.com.cc.dao.ExtratoDAO;
import br.com.cc.dao.LancamentosDao;
import br.com.cc.dao.SeguradoraExtratoDAO;
import br.com.cc.entidade.Extrato;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SeguradorasExtrato;

public class ExtratoFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private LancamentosDao lancamentosDao;
	private ExtratoDAO extratoDAO;
	private SeguradoraExtratoDAO seguradorasDAO;
	private LancamentosDao dao;

	public ExtratoFacade() {
		lancamentosDao = new LancamentosDao();
		extratoDAO = new ExtratoDAO();
		seguradorasDAO = new SeguradoraExtratoDAO();
		dao = new LancamentosDao();
	}

	public List<Extrato> buscarExtrato() throws Exception {
		return extratoDAO.buscarExtrato();
	}

	public List<SeguradorasExtrato> buscarSeguradorasSistema(Extrato extrato) throws Exception {
		List<SeguradorasExtrato> lista = new ArrayList<>();
		try {
			List<Object[]> objs = dao.buscarSeguradoras(extrato.getPeriodo());
			for (Object[] obj : objs) {
				SeguradorasExtrato extr = new SeguradorasExtrato();
				extr.setPeriodo((String) obj[0]);
				extr.setSeguradora((String) obj[1]);
				extr.setValor((Double) obj[2]);
				lista.add(extr);
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return lista;
	}

	public List<SeguradorasExtrato> buscarSeguradoras(Extrato extrato) throws Exception {
		return seguradorasDAO.buscarExtratoPorPeriodoEProdutor(extrato.getPeriodo());
	}

	public List<Extrato> buscarPorPeriodo(String periodo) throws Exception {
		return extratoDAO.buscarExtratoPorPeriodo(periodo);
	}

	public List<Extrato> buscarPorProdutor(String produtor) throws Exception {
		return extratoDAO.buscarExtratoPorProdutor(produtor);
	}

	public List<Extrato> buscarPorPeriodoProdutor(String periodo, String produtor) throws Exception {
		return extratoDAO.buscarExtratoPorPeriodoEProdutor(periodo, produtor);
	}

	public void atualizarExtrato(Extrato extrato) throws Exception {
		extrato.setVlrAcerto(extrato.getVlrReceberSistema() - extrato.getVlrRetiradaSocio());
		extratoDAO.salvar(extrato);
		atualiazarSaldoAcumulado(extrato);
	}

	public void atualiazarSaldoAcumulado(Extrato extrato) throws Exception {
		List<Extrato> saldos = extratoDAO.buscarExtratoPoProdutorOrder(extrato.getProdutor(),
				extrato.getMesReferencia());
		Double saldo = 0.0;
		for (Extrato extr : saldos) {
			saldo += extr.getVlrAcerto();
			extr.setVlrSaldoAcumulado(saldo);
			extratoDAO.salvar(extr);
		}
	}

	public List<Extrato> listarExtratoPorPeriodo(String periodo) throws Exception {
		List<Extrato> extr = null;
		try {
			extr = extratoDAO.buscarExtratoPorPeriodo(periodo);
		} catch (NoResultException e) {
			extr = null;
		}

		return extr;
	}

	public List<Extrato> listarExtratoPorProdutor(String produtor) throws Exception {
		return extratoDAO.buscarExtratoPorProdutor(produtor);
	}

	public void deletar(Extrato extrato) throws Exception {
		extratoDAO.deletar(extrato);
	}

	public void deletarPorPeriodo(String periodo) throws Exception {
		List<Extrato> delets = extratoDAO.buscarExtratoPorPeriodo(periodo);
		for (Extrato extr : delets) {
			this.deletar(extr);
		}
	}

	public void IncluirExtrato(Extrato extrato) throws Exception {

		try {

			List<Lancamentos> lancamentos = lancamentosDao.getLancamentosPorPeriodo(extrato.getPeriodo());

			if (lancamentos != null && !lancamentos.isEmpty()) {
				List<Extrato> obterExtrato = this.listarExtratoPorPeriodo(extrato.getPeriodo());

				if (obterExtrato == null || obterExtrato.isEmpty()) {
					List<Extrato> extratos = criarExtrato(lancamentos, extrato);

					for (Extrato extr : extratos) {
						extratoDAO.salvar(extr);
					}

					if (extratos.size() > 0) {
						salvarSeguradoraExtrato(extrato);
					}

				} else {
					atualizarSeguradoraExtrato(extrato);
					atualizarValorExtrato(obterExtrato, extrato);
				}
			} else {
				throw new Exception("Não existe lançamentos para o período");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private void atualizarValorExtrato(List<Extrato> extratos, Extrato extrato) throws Exception {
		try {
			for (Extrato ex : extratos) {
				ex.setVlrTotalSeguradorasExtrato(
						ex.getVlrTotalSeguradorasExtrato() + extrato.getVlrTotalSeguradorasExtrato());
				extratoDAO.salvar(ex);
			}
		} catch (Exception e) {
			throw new Exception("Falha ao tentar atualizar valor extrato " + e.getMessage());
		}
	}

	private void salvarSeguradoraExtrato(Extrato extrato) throws Exception {
		SeguradorasExtrato se = new SeguradorasExtrato();
		se.setPeriodo(extrato.getPeriodo());
		se.setSeguradora(extrato.getSeguradora());
		se.setValor(extrato.getVlrTotalSeguradorasExtrato());
		try {
			seguradorasDAO.salvar(se);
		} catch (Exception e) {
			throw new Exception("Falha ao tentar atualizar segurado-extrato " + e.getMessage());
		}
	}

	private void atualizarSeguradoraExtrato(Extrato extrato) throws Exception {
		SeguradorasExtrato seg = null;
		try {
			seg = seguradorasDAO.buscarExtratoPorPeriodoEProdutor(extrato.getPeriodo(), extrato.getSeguradora());
			seg.setValor(seg.getValor() + extrato.getVlrTotalSeguradorasExtrato());

			seguradorasDAO.salvar(seg);
		} catch (NoResultException e) {
			salvarSeguradoraExtrato(extrato);
		}
	}

	private List<Extrato> criarExtrato(List<Lancamentos> lancamentos, Extrato extrato) throws Exception {
		Map<String, Extrato> mapa = new HashMap<>();

		Extrato extr = null;
		Double valor = 0.0;
		try {

			for (Lancamentos lanc : lancamentos) {
				valor += lanc.getComissao();

				extr = new Extrato();
				extr.setProdutor(lanc.getProdutor());
				extr.setPeriodo(extrato.getPeriodo());
				extr.setVlrTotalSeguradorasExtrato(extrato.getVlrTotalSeguradorasExtrato());
				extr.setVlrReceberSistema(lanc.getComissao());
				extr.setVlrAcerto(extr.getVlrReceberSistema());

				String dat[] = extr.getPeriodo().split("/");
				String data = dat[1] + dat[0];

				extr.setMesReferencia(Integer.parseInt(data));

				mapa = this.calcularAReceber(mapa, extr);
			}
			extr = null;
			mapa = this.calcularTotalSeguradorasSistema(mapa, valor);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return new ArrayList<>(mapa.values());
	}

	private Map<String, Extrato> calcularTotalSeguradorasSistema(Map<String, Extrato> mapa, Double valor) {
		Map<String, Extrato> map = mapa;

		Set<String> produtores = map.keySet();
		for (String produtor : produtores) {
			Extrato extr = map.get(produtor);
			extr.setVlrTotalSeguradorasSistema(valor);
		}

		return map;
	}

	private Map<String, Extrato> calcularAReceber(Map<String, Extrato> mapa, Extrato extr) throws Exception {
		Map<String, Extrato> map = mapa;
		if (extr.getProdutor() != null && !extr.getProdutor().equals("")) {

			Extrato extrato = map.get(extr.getProdutor().trim());
			if (extrato != null) {
				extrato.setVlrReceberSistema(extr.getVlrReceberSistema() + extrato.getVlrReceberSistema());
				extrato.setVlrAcerto(extr.getVlrAcerto() + extrato.getVlrAcerto());
				map.put(extrato.getProdutor().trim(), extrato);
			} else {
				map.put(extr.getProdutor().trim(), extr);
			}
		} else {
			throw new Exception("Falta informar o produtor para uma seguradora no periodo de " + extr.getPeriodo());
		}

		return map;
	}

	public static void main(String[] args) throws Exception {
		ExtratoFacade nf = new ExtratoFacade();
		Extrato extr = new Extrato();

		extr.setPeriodo("08/2015");
		extr.setVlrTotalSeguradorasExtrato(36265.78);

		nf.IncluirExtrato(extr);

	}
}
