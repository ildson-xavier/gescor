package br.com.cc.auditoria;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.cc.dao.AuditoriaDAO;
import br.com.cc.dao.LancamentosDeletadosDAO;
import br.com.cc.entidade.Auditoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.LancamentosDeletados;
import br.com.cc.entidade.Oculto;
import br.com.cc.entidade.Usuario;
import br.com.cc.util.JSFMessageUtil;
import br.com.cc.util.Util;

public class AuditoriaService {

	private AuditoriaDAO dao;
	private LancamentosDeletadosDAO ldao;

	public AuditoriaService() {
		if (dao == null) {
			dao = new AuditoriaDAO();
		}

		if (ldao == null) {
			ldao = new LancamentosDeletadosDAO();
		}
	}

	public void adicionarLogAuditoria(Lancamentos lancamentos, Lancamentos anterior, Usuario usuario,
			String tipoAlteracao) {

		Locale local = new Locale("pt", "BR");
		Calendar cal = Calendar.getInstance(local);

		try {
			List<String[]> retorno = this.obterDiferenca(lancamentos, anterior);
			for (String[] array : retorno) {

				Auditoria aud = new Auditoria();
				String tipoLncamento = lancamentos.getClassificacao() == 1 ? "Receita" : "Despesa";
				aud.setDataAlteracao(cal.getTime());
				aud.setIdRegistro(lancamentos.getId());
				aud.setPeriodo(lancamentos.getPeriodo());
				aud.setTipoAlteracao(tipoAlteracao);
				aud.setTipoLancamento(tipoLncamento);
				aud.setUsuario(usuario);
				aud.setValorAnterior(array[0]);
				aud.setValorAtual(array[1]);

				aud = dao.adicionarAuditoria(aud);

				LancamentosDeletados ld = this.build(anterior);
				ld.setAuditoria(aud);

				ldao.adicionar(ld);
			}
		} catch (Exception e) {
			JSFMessageUtil.sendAlertMessageToUser("Falha ao tentar salvar log auditoria", e.getMessage());
		}

	}

	public void adicionarLogAuditoria(Lancamentos lancamentos, Usuario usuario, String tipoAlteracao) {
		try {

			Auditoria aud = new Auditoria();
			String tipoLncamento = lancamentos.getClassificacao() == 1 ? "Receita" : "Despesa";
			aud.setDataAlteracao(Calendar.getInstance().getTime());
			aud.setIdRegistro(lancamentos.getId());
			aud.setPeriodo(lancamentos.getPeriodo());
			aud.setTipoAlteracao(tipoAlteracao);
			aud.setTipoLancamento(tipoLncamento);
			aud.setUsuario(usuario);

			aud = dao.adicionarAuditoria(aud);

			LancamentosDeletados ld = this.build(lancamentos);
			ld.setAuditoria(aud);

			ldao.adicionar(ld);

		} catch (Exception e) {
			JSFMessageUtil.sendAlertMessageToUser("Falha ao tentar salvar log auditoria", e.getMessage());
		}

	}

	private List<String[]> obterDiferenca(Lancamentos atual, Lancamentos anterior) throws Exception {
		Class<Lancamentos> classAtual = (Class<Lancamentos>) atual.getClass();
		Class<Lancamentos> classAnterior = (Class<Lancamentos>) anterior.getClass();
		List<String[]> lista = new ArrayList<>();
		List<String[]> novaLista = new ArrayList<>();

		try {
			for (int i = 0; i < classAnterior.getDeclaredFields().length; i++) {
				Field fieldAtual = classAtual.getDeclaredFields()[i];
				Field fieldAnterior = classAnterior.getDeclaredFields()[i];

				fieldAnterior.setAccessible(true);
				fieldAtual.setAccessible(true);

				if (!fieldAnterior.isAnnotationPresent(Oculto.class) && !fieldAtual.isAnnotationPresent(Oculto.class)) {

					if (fieldAnterior.getType() == String.class) {
						String valorAnterior = (String) fieldAnterior.get(anterior);
						String valorAtual = (String) fieldAtual.get(atual);

						if (valorAnterior != null && valorAtual != null) {
							if (!valorAnterior.equals(valorAtual)) {
								String[] retorno = new String[2];
								retorno[0] = valorAnterior;
								retorno[1] = valorAtual;
								lista.add(retorno);
							}
						}
					}

					if (fieldAnterior.getType() == Double.class) {
						Double valorAnterior = (Double) fieldAnterior.get(anterior);
						Double valorAtual = (Double) fieldAtual.get(atual);

						if (valorAnterior != null && valorAtual != null) {

							BigDecimal valorAnt = new BigDecimal(valorAnterior).setScale(2, RoundingMode.HALF_EVEN);
							BigDecimal valorAtu = new BigDecimal(valorAtual).setScale(2, RoundingMode.HALF_EVEN);

							if (Double.compare(valorAnt.doubleValue(), valorAtu.doubleValue()) != 0) {
								String[] retorno = new String[2];
								retorno[0] = String.valueOf(valorAnterior);
								retorno[1] = String.valueOf(valorAtual);
								lista.add(retorno);
							}
						}
					}

					if (fieldAnterior.getType() == Date.class) {
						Date valorAnterior = (Date) fieldAnterior.get(anterior);
						Date valorAtual = (Date) fieldAtual.get(atual);
						if (valorAnterior != null && valorAtual != null) {
							if (valorAnterior.compareTo(valorAtual) != 0) {
								String[] retorno = new String[2];
								retorno[0] = Util.formatarData(valorAnterior);
								retorno[1] = Util.formatarData(valorAtual);
								lista.add(retorno);
							}
						}
					}
				}
			}

			String[] str = null;

			for (int i = 0; i < lista.size(); i++) {
				if (str == null) {
					str = lista.get(i);
					novaLista.add(str);
				} else {
					if (!str[0].equals(lista.get(i)[0])) {
						novaLista.add(str);
						str = lista.get(i);
					}
				}

			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return novaLista;
	}

	private LancamentosDeletados build(Lancamentos lanc) {
		LancamentosDeletados del = new LancamentosDeletados();

		if (lanc.getClassificacao() == 1) {
			del.setComissao(lanc.getComissao());
			del.setComissaoLiquida(lanc.getValor3());
			del.setImposto(lanc.getValor2());
		} else {
			del.setComissao(lanc.getValor1());
			del.setComissaoLiquida(lanc.getComissao());
			del.setImposto(lanc.getValor2());
		}

		del.setDataInclusao(lanc.getDataIncluisao());
		del.setDescricao(lanc.getDescricao());
		del.setParcela(lanc.getParcela());
		del.setPeriodo(lanc.getPeriodo());
		del.setProdutor(lanc.getProdutor());
		del.setSegurado(lanc.getHistorico());
		del.setSeguradora(lanc.getSeguradora());
		del.setIdRef(lanc.getId());
		return del;
	}
}
