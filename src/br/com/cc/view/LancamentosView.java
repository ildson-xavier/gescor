/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.view;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

import br.com.cc.auditoria.AuditoriaService;
import br.com.cc.dao.LancamentosDao;
import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Produtor;
import br.com.cc.util.Util;

/**
 *
 * @author Usuario
 */
@ManagedBean(name = "lancamentosView")
@ViewScoped
public class LancamentosView implements Serializable {

	private static final long serialVersionUID = -3521492063714123204L;

	private LancamentosDao lancamentosDao;
	private ProdutorDao produtorDao;

	private List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();
	private List<Lancamentos> lancamentosSintetico = new ArrayList<Lancamentos>();
	private Lancamentos lacamentoBean = new Lancamentos();
	private List<Produtor> lstProdutores = new ArrayList<Produtor>();
	private List<Produtor> produtors = new ArrayList<Produtor>();
	private Date dtInicial;
	private Date dtFinal;
	private Date dtInicialSintetico;
	private Date dtFinalSintetico;
	private String produtor;
	private String cpf;
	private String seguradora;
	private Double total = 0.0;
	private Double totalLiquido = 0.0;
	private Double percentagem = 8.21;
	private Double imposto = 0.0;
	private Double premioLiquido = 0.0;
	private Double vlrComissao = 0.0;
	private String segurado;
	private Double totalLiquidoAnalitico = 0.0;
	private Double totalComissaoAnalitico = 0.0;
	private Double totalImpostoAnalitico = 0.0;
	private Date dataAtual = new Date();

	private List<Lancamentos> lancamentosFilter = new ArrayList<Lancamentos>();

	private List<Lancamentos> lancamentosInclusao = new ArrayList<>();
	private double[] totalInclusao = new double[3];

	private Map<String, Double> mapPercentual;
	private boolean situacao;

	private boolean tribuitar;
	private Double percentual;

	private boolean tipoPessoa;
	private boolean ativarFiltroData;
	
	@ManagedProperty(value = "#{usuarioLoginBean}")
	private UsuarioLoginBean usuarioLoginBean;
	
	private AuditoriaService auditoriaService;

	/**
	 * Creates a new instance of LancamentosView
	 */
	public LancamentosView() {
		Util.verificaMemoria("ccomissao");

		carregarPercentual();

		if (lancamentosDao == null) {
			lancamentosDao = new LancamentosDao();
		}

		if (produtorDao == null) {
			produtorDao = new ProdutorDao();
		}
	}

	private void carregarPercentual() {
		Properties props = new Properties();
		try {
			props.load(getClass().getResourceAsStream("/percentual.properties"));

			this.mapPercentual = new HashMap<>();
			getMapPercentual().put("2011", Double.parseDouble(props.getProperty("per.2011")));
			getMapPercentual().put("2012", Double.parseDouble(props.getProperty("per.2012")));
			getMapPercentual().put("2013", Double.parseDouble(props.getProperty("per.2013")));
			getMapPercentual().put("2014", Double.parseDouble(props.getProperty("per.2014")));
			getMapPercentual().put("2015", Double.parseDouble(props.getProperty("per.2015")));
			getMapPercentual().put("2016", Double.parseDouble(props.getProperty("per.2016")));
			getMapPercentual().put("2017", Double.parseDouble(props.getProperty("per.2017")));
			getMapPercentual().put("2018", Double.parseDouble(props.getProperty("per.2018")));
			getMapPercentual().put("2019", Double.parseDouble(props.getProperty("per.2019")));
			getMapPercentual().put("2020", Double.parseDouble(props.getProperty("per.2020")));
			getMapPercentual().put("2021", Double.parseDouble(props.getProperty("per.2021")));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean filterByPeriodo(Object value, Object filter, Locale locale) {

		boolean retorno = false;
		String filterText = (filter == null) ? null : filter.toString().trim();

		if (filterText == null || filter.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		try {
			if (Util.validaData((String) filterText)) {
				retorno = Util.comparaDadas((Date) value, (String) filter);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	public void adicionarLancamentos(List<Lancamentos> ls) throws Exception {
		System.out.println("adicionarLancamentos");
		int index = 0;
		if (ls.size() > 0) {
			for (Lancamentos l : ls) {
				// if (!l.equals(l)) {
				// lancamentosDao.adicionarLancamento(l);
				if (adicionarLancamentoComProdutor(l, index) < 0) {
					throw new Exception("Arquivo ja importada!");
					// break;
				}
				index++;
				// }
			}
		} else {
			throw new Exception("Lista com os lancamentos vazia!");
		}
	}

	public int adicionarLancamentoComProdutor(Lancamentos lanc, int index) throws Exception {
		System.out.println("adicionarLancamentoComProdutor");
		boolean vazio = true;
		String nomeProdutor = "";
		int index2 = lanc.getHistorico().length();

		if (index2 >= 25) {
			nomeProdutor = lanc.getHistorico().substring(0, 25);
		} else {
			nomeProdutor = lanc.getHistorico();
		}

		// try {
		lstProdutores = produtorDao.getProdutoresPorSegurado(nomeProdutor.trim());
		for (Produtor p : lstProdutores) {
			lanc.setProdutor(p.getProdutor());
			lanc.setCpf(p.getCpf());
			break;
		}
		if (lstProdutores.isEmpty()) {
			System.out.println("Nao achou o produtor");
		} else {
			System.out.println("Achou o produtor");
		}

		// Verifica se ja existe o lancamento
		if (index == 0) {
			try {
				// System.out.println("Periodo: " + lanc.getPeriodo());
				vazio = lancamentosDao.existeLancamento(lanc.getPeriodo(), lanc.getSeguradora());
				if (vazio == false) {

					throw new Exception("O arquivo de comissao já está importado. " + "Nele existem lançamentos da "
							+ lanc.getSeguradora() + " para " + Util.formatarData(lanc.getPeriodo()));

				}
			} catch (NoResultException ne) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Novo lancamento.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.out.println(ne.getMessage());
			}
		}

		// Verifica a situacao, se eh renovacao ou novo
		if (lancamentosDao.ehLancamentosDescrecenteRenovacao(lanc)) {
			lanc.setSituacao(2); // Renovacao
			System.out.println("Eh renovacao");

		} else {
			System.out.println("Eh novo");
			lanc.setSituacao(1); // Novo
			Calendar calAtual = Calendar.getInstance();
			Calendar calLanc = Calendar.getInstance();

			calAtual.setTime(dataAtual);
			calLanc.setTime(lanc.getPeriodo());

			// Se o ano de comissa, que esta sendo importada, for menor
			// que o ano atual ...
			System.out.println("Ano atual: " + calAtual);
			System.out.println("Ano lanc: " + calLanc);
			if (calAtual.get(Calendar.YEAR) > calLanc.get(Calendar.YEAR)) {
				lancamentosDao.ehLancamentosCrecenteRenovacao(lanc);
			}
		}

		// Insere o lancamento
		lancamentosDao.adicionarLancamento(lanc);

		/*
		 * } catch (Exception ex) { FacesMessage msg = new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
		 * "Não foi possivel salvar lancamento " + lacamentoBean.getHistorico()
		 * + "   " + ex.getMessage());
		 * FacesContext.getCurrentInstance().addMessage(null, msg);
		 * //EnvioEmail.enviarEmail(); ex.printStackTrace(); }
		 */
		return 0;
	}

	public String adicionarLancamento() {
		System.out.println("adicionarLancamento");
		try {
			lacamentoBean.setComissao(Util.novoValor(lacamentoBean.getComissao()));
			calcularPercentualEPremioLiquido(lacamentoBean, percentagem);
			//lacamentoBean.calcularPercentualEPremioLiquido(percentagem);

			lacamentoBean.setProdutor(produtor);
			lacamentoBean.setCpf(cpf);
			lacamentoBean.setHistorico(segurado);
			lacamentoBean.setClassificacao(1);
			lacamentoBean.setDataIncluisao(Calendar.getInstance().getTime());
			aumentarFonte(lacamentoBean);

			if (!lancamentosDao.existeLancamento(lacamentoBean.getHistorico(), lacamentoBean.getPeriodo(),
					lacamentoBean.getSeguradora(), lacamentoBean.getComissao())) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ANTENÇÃO! ",
						"O segurado já está cadastrado para o periodo informado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				lacamentoBean = new Lancamentos();
				return "cadastrarlancamentoview.jsf";
			}

			lancamentosDao.adicionarLancamento(lacamentoBean);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Gravado com sucesso! ",
					"[" + lacamentoBean.getHistorico() + "]");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			lacamentoBean.setComissao(0.0);
			lacamentoBean.setHistorico("");
			lacamentoBean.setProdutor("");
			lacamentoBean.setCpf("");
			lacamentoBean.setParcela(0);
			produtor = "";
			segurado = "";
			tipoPessoa = false;

			setLancamentosInclusao(lancamentosDao.getLancamentosAnaliticoPorPerido(lacamentoBean.getPeriodo(),
					lacamentoBean.getSeguradora()));

			// setLancamentosInclusao(lancamentosDao.getLancamentosAnaliticoPorSeguradoraAtual(lacamentoBean.getSeguradora()));

			setTotalInclusao(totalizarInclusao(getLancamentosInclusao()));

		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel salvar o lancamento " + lacamentoBean.getHistorico() + "   " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}

		return "cadastrarlancamentoview.jsf";
	}

	public void atulizarGrid() throws Exception {
		if (!this.ativarFiltroData) {
			this.lancamentosFilter.clear();
			setLancamentosInclusao(lancamentosDao.getLancamentosAnaliticoPorPerido(lacamentoBean.getPeriodo(),
					lacamentoBean.getSeguradora()));
			setTotalInclusao(totalizarInclusao(getLancamentosInclusao()));
			ativarFiltroData = false;
		} else {
			this.lancamentosFilter.clear();
			setLancamentosInclusao(
					lancamentosDao.getLancamentosAnaliticoPorSeguradoraAtual(lacamentoBean.getSeguradora()));
			setTotalInclusao(totalizarInclusao(getLancamentosInclusao()));
			ativarFiltroData = true;
		}
	}

	private double[] totalizarInclusao(List<Lancamentos> lista) {
		double[] total = new double[3];
		double comissao = 0.0;
		double imposto = 0.0;
		double liquido = 0.0;

		if (lista.size() > 0) {
			for (Lancamentos l : lista) {
				comissao += l.getComissao();
				imposto += l.getValor2();
				liquido += l.getValor3();
			}
		}

		total[0] = comissao;
		total[1] = imposto;
		total[2] = liquido;

		return total;
	}

	public List<String> autoCompletar(String query) {
		System.out.println("autoCompletar");
		produtors = new ArrayList<Produtor>();
		List<String> results = new ArrayList<String>();
		try {
			produtors = produtorDao.getProdutoresPorSegurado(query);
			for (Produtor p : produtors) {
				results.add(p.getSegurado());
			}
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel completar o nome do segurado " + " " + ex.getMessage());
			ex.printStackTrace();
		}
		return results;
	}

	public List<String> autoCompletarProdutor(String query) {
		System.out.println("autoCompletarProdutor");
		List<String> results = new LinkedList<String>();
		produtors = new ArrayList<Produtor>();
		try {
			produtors = produtorDao.getProdutoresPorProdutor(query);
			for (Produtor p : produtors) {
				results.add(p.getProdutor());
				this.cpf = p.getCpf();
			}
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel completar o nome do produtor " + " " + ex.getMessage());
			ex.printStackTrace();
		}
		return results;
	}

	public void obterProdutor() {
		for (Produtor p : produtors) {
			if (p.getSegurado().contains(segurado)) {
				produtor = p.getProdutor();
				this.cpf = p.getCpf();
				break;
			}
		}
	}

	public void removerLancamento() {
		System.out.println("removerLancamento");
		try {
			
			AuditoriaService aud = new AuditoriaService();
			aud.adicionarLogAuditoria(lacamentoBean, getUsuarioLoginBean().getUsuarioLogador(), "Deletado");
			aud = null;
			
			lancamentosDao.removerLancamento(lacamentoBean);
			lancamentos.remove(lacamentoBean);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK! ",
					"Segurado [" + lacamentoBean.getHistorico() + "] removido com sucesso.");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			lacamentoBean = new Lancamentos();

		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO! ",
					"Não foi possivel remover o lancamento " + lacamentoBean.getHistorico() + "   " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	/*
	 * Metodos Analiticos
	 */
	public String pesquisarPorFiltroAnalitico() {
		System.out.println("pesquisarPorFiltroAnalitico");
		lancamentos.clear();
		Util.verificaMemoria("ccomissao-pesquisarPorFiltroAnalitico");
		if (getDtInicial() != null && getDtFinal() != null) {
			if (getLacamentoBean().getProdutor().length() == 0 && getLacamentoBean().getSeguradora().length() == 0
					&& getLacamentoBean().getHistorico().length() == 0) {
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorData(dtInicial, dtFinal);
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca por data ",
							ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca por data " + ex.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() == 0
					&& getLacamentoBean().getSeguradora().length() == 0
					&& getLacamentoBean().getHistorico().length() > 0) {
				System.out.println("getLancamentosAnaliticoPorDataHistorico " + getLacamentoBean().getHistorico());
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataHistorico(dtInicial, dtFinal,
							getLacamentoBean().getHistorico());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception e) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao efeturar busca por data e historico ", e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca por data e historico " + e.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() > 0 && getLacamentoBean().getSeguradora().length() > 0
					&& getLacamentoBean().getHistorico().length() > 0) {

				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataProdutorSeguradoraHistorico(dtInicial,
							dtFinal, getLacamentoBean().getProdutor(), getLacamentoBean().getSeguradora(),
							getLacamentoBean().getHistorico());
					calcularValoresAnaliticos(lancamentos);
				} catch (Exception e) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao efeturar busca por data, produtor, seguradora e historico ", e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println(
							"Erro ao efeturar busca por data, produtor, seguradora e historico " + e.getMessage());
				}

			} else if (getLacamentoBean().getProdutor().length() > 0 && getLacamentoBean().getSeguradora().length() == 0
					&& getLacamentoBean().getHistorico().length() == 0) {
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataProdutor(dtInicial, dtFinal,
							getLacamentoBean().getProdutor());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao efeturar busca por produtor ", ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca por produtor " + ex.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() == 0 && getLacamentoBean().getSeguradora().length() > 0
					&& getLacamentoBean().getHistorico().length() == 0) {
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataSeguradora(dtInicial, dtFinal,
							getLacamentoBean().getSeguradora());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao efeturar busca por data e seguradora", ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca por data e seguradora " + ex.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() > 0 && getLacamentoBean().getSeguradora().length() > 0
					&& getLacamentoBean().getHistorico().length() == 0) {
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataProdutorSeguradora(dtInicial, dtFinal,
							getLacamentoBean().getProdutor(), getLacamentoBean().getSeguradora());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
							ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca " + ex.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() > 0 && getLacamentoBean().getSeguradora().length() == 0
					&& getLacamentoBean().getHistorico().length() > 0) {
				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataProdutorHistorico(dtInicial, dtFinal,
							getLacamentoBean().getProdutor(), getLacamentoBean().getHistorico());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
							ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca " + ex.getMessage());
				}
			} else if (getLacamentoBean().getProdutor().length() == 0 && getLacamentoBean().getSeguradora().length() > 0
					&& getLacamentoBean().getHistorico().length() > 0) {

				try {
					lancamentos = lancamentosDao.getLancamentosAnaliticoPorDataSeguradoraHistorico(dtInicial, dtFinal,
							getLacamentoBean().getSeguradora(), getLacamentoBean().getHistorico());
					calcularValoresAnaliticos(lancamentos);
					return "principalview";
				} catch (Exception ex) {
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
							ex.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msg);
					System.err.println("Erro ao efeturar busca " + ex.getMessage());
				}
			}

		} else if (getLacamentoBean().getHistorico().length() > 0 && getLacamentoBean().getSeguradora().length() > 0
				&& getLacamentoBean().getProdutor().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorSeguradoESeguradoraEProdutor(
						getLacamentoBean().getHistorico(), getLacamentoBean().getProdutor(),
						getLacamentoBean().getSeguradora());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca por seguradora", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.err.println("Erro ao efeturar busca por seguradora" + ex.getMessage());
			}
		} else if (getLacamentoBean().getHistorico().length() > 0 && getLacamentoBean().getSeguradora().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorSeguradoESeguradora(
						getLacamentoBean().getHistorico(), getLacamentoBean().getSeguradora());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca por seguradora", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.err.println("Erro ao efeturar busca por seguradora " + ex.getMessage());
			}
		} else if (getLacamentoBean().getHistorico().length() > 0 && getLacamentoBean().getProdutor().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorSeguradoEProdutor(
						getLacamentoBean().getHistorico(), getLacamentoBean().getProdutor());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";

			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca por seguradora", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.err.println("Erro ao efeturar busca por seguradora " + ex.getMessage());
			}
		} else if (getLacamentoBean().getHistorico().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorSegurado(getLacamentoBean().getHistorico());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca por historico",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.err.println("Erro ao efeturar busca por historico "+ ex.getMessage());
			}

		} else if (getLacamentoBean().getProdutor().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorProdutor(getLacamentoBean().getProdutor());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca por produtor",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				System.err.println("Erro ao efeturar busca por produtor"+ex.getMessage());
			}
		} else if (getLacamentoBean().getSeguradora().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoPorSeguradora(getLacamentoBean().getSeguradora());
				calcularValoresAnaliticos(lancamentos);
				return "principalview";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca por seguradora", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else if (getLacamentoBean().getCpf() == null || getLacamentoBean().getCpf().length() == 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnalitico();
				calcularValoresAnaliticos(lancamentos);
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else if (getLacamentoBean().getCpf().length() > 0) {
			try {
				lancamentos = lancamentosDao.getLancamentosAnaliticoCpf(getLacamentoBean().getCpf());
				calcularValoresAnaliticos(lancamentos);
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else {
			try {
				lancamentos = lancamentosDao.getLancamentosAnalitico();
				calcularValoresAnaliticos(lancamentos);
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		}
		return "principalview";
	}

	public void pesquisarPorCpf() {
		System.out.println("pesquisarPorCpf");
		try {
			lancamentos = lancamentosDao.getLancamentosAnaliticoCpf(getLacamentoBean().getCpf());
			calcularValoresAnaliticos(lancamentos);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca ",
					ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void calcularValoresAnaliticos(List<Lancamentos> list) {
		totalComissaoAnalitico = 0.0;
		totalImpostoAnalitico = 0.0;
		totalLiquidoAnalitico = 0.0;
		for (Lancamentos l : list) {
			if (l.getValor2() == null) {
				l.setValor2(0.0);
			}
			if (l.getValor3() == null) {
				l.setValor3(0.0);
			}
			if (l.getComissao() == null) {
				l.setComissao(0.0);
			}
			totalComissaoAnalitico += l.getComissao();
			totalImpostoAnalitico += l.getValor2();
			totalLiquidoAnalitico += l.getValor3();
		}
	}

	/*
	 * Metodos Sinteticos
	 */
	public String pesquisaPorFiltroSintetico() {
		System.out.println("pesquisaPorFiltroSintetico");
		Util.verificaMemoria("ccomissao-pesquisaPorFiltroSintetico");
		lancamentosSintetico.clear();
		total = 0.0;
		totalLiquido = 0.0;
		if ((getDtInicialSintetico() == null || getDtFinalSintetico() == null)
				&& (produtor == null || produtor.length() == 0) && (seguradora == null || seguradora.length() == 0)) {
			try {
				List<Object[]> objs = lancamentosDao.getLancamentosSintetico();
				// Lancamentos lanc = new Lancamentos();
				for (Object[] obj : objs) {
					Lancamentos lanc = new Lancamentos();
					// lanc.setMesAno((String) obj[0]);
					lanc.setMesAno(Util.getFormatDatePt((String) obj[0]));
					lanc.setProdutor((String) obj[1]);
					lanc.setSeguradora((String) obj[2]);
					lanc.setComissao((Double) obj[3]);
					lanc.setValor3((Double) obj[4]);
					total += lanc.getComissao();
					if (lanc.getValor3() == null) {
						totalLiquido = 0.0;
					} else {
						totalLiquido += lanc.getValor3();
					}
					lancamentosSintetico.add(lanc);
				}
				return "";

			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efeturar busca sintÃ©tica ",
						ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else if ((produtor == null || produtor.length() == 0) && (seguradora == null || seguradora.length() == 0)) {
			try {
				List<Object[]> objs = lancamentosDao.getLancamentosSinteticoPorDataData(getDtInicialSintetico(),
						getDtFinalSintetico());
				// Lancamentos lanc = new Lancamentos();
				for (Object[] obj : objs) {
					Lancamentos lanc = new Lancamentos();
					lanc.setMesAno((String) obj[0]);
					lanc.setProdutor((String) obj[1]);
					lanc.setSeguradora((String) obj[2]);
					lanc.setComissao((Double) obj[3]);
					lanc.setValor3((Double) obj[4]);
					total += lanc.getComissao();
					if (lanc.getValor3() == null) {
						totalLiquido = 0.0;
					} else {
						totalLiquido += lanc.getValor3();
					}
					lancamentosSintetico.add(lanc);
				}
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca sintÃ©tica por data", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else if ((produtor != null && produtor.length() > 0) && (seguradora.length() == 0 || seguradora == null)) {
			try {
				List<Object[]> objs = lancamentosDao.getLancamentosSinteticoPorDataProdutor(getDtInicialSintetico(),
						getDtFinalSintetico(), produtor);
				// Lancamentos lanc = new Lancamentos();
				for (Object[] obj : objs) {
					Lancamentos lanc = new Lancamentos();
					lanc.setMesAno((String) obj[0]);
					lanc.setProdutor((String) obj[1]);
					lanc.setSeguradora((String) obj[2]);
					lanc.setComissao((Double) obj[3]);
					lanc.setValor3((Double) obj[4]);
					total += lanc.getComissao();
					if (lanc.getValor3() == null) {
						totalLiquido = 0.0;
					} else {
						totalLiquido += lanc.getValor3();
					}
					lancamentosSintetico.add(lanc);
				}
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca sintÃ©tica por data e produtor", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else if ((seguradora != null && seguradora.length() > 0) && (produtor == null || produtor.length() == 0)) {
			try {
				List<Object[]> objs = lancamentosDao.getLancamentosSinteticoPorDataSeguradora(getDtInicialSintetico(),
						getDtFinalSintetico(), seguradora);
				// Lancamentos lanc = new Lancamentos();
				for (Object[] obj : objs) {
					Lancamentos lanc = new Lancamentos();
					lanc.setMesAno((String) obj[0]);
					lanc.setProdutor((String) obj[1]);
					lanc.setSeguradora((String) obj[2]);
					lanc.setComissao((Double) obj[3]);
					lanc.setValor3((Double) obj[4]);
					total += lanc.getComissao();
					if (lanc.getValor3() == null) {
						totalLiquido = 0.0;
					} else {
						totalLiquido += lanc.getValor3();
					}
					lancamentosSintetico.add(lanc);
				}
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca sintÃ©tica por data e seguradora", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		} else if ((seguradora.length() > 0 && seguradora != null) && (produtor.length() > 0 && produtor != null)) {
			try {
				List<Object[]> objs = lancamentosDao.getLancamentosSinteticoPorDataSeguradoraProdutor(
						getDtInicialSintetico(), getDtFinalSintetico(), seguradora, produtor);
				// Lancamentos lanc = new Lancamentos();
				for (Object[] obj : objs) {
					Lancamentos lanc = new Lancamentos();
					lanc.setMesAno((String) obj[0]);
					lanc.setProdutor((String) obj[1]);
					lanc.setSeguradora((String) obj[2]);
					lanc.setComissao((Double) obj[3]);
					lanc.setValor3((Double) obj[4]);
					total += lanc.getComissao();
					if (lanc.getValor3() == null) {
						totalLiquido = 0.0;
					} else {
						totalLiquido += lanc.getValor3();
					}
					lancamentosSintetico.add(lanc);
				}
				return "";
			} catch (Exception ex) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro ao efeturar busca sintética por data, seguradora e produtor ", ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				ex.printStackTrace();
			}
		}
		return "";
	}

	public void controlarValores(Integer id, Lancamentos lanc) throws Exception {

		if (Double.compare(Util.arredondar(lanc.getComissao()), Util.arredondar(lanc.getValorBruto())) != 0) {
			lanc.setComissao(Util.novoValor(lanc.getComissao()));
		}
		if (Double.compare(Util.arredondar(lanc.getValor2()), Util.arredondar(lanc.getValorImposto())) != 0) {
			lanc.setValor2(Util.novoValor(lanc.getValor2()));
		}
		if (Double.compare(Util.arredondar(lanc.getValor3()), Util.arredondar(lanc.getValorLiquido())) != 0) {
			lanc.setValor3(Util.novoValor(lanc.getValor3()));
		}
	}

	/*
	 * Metodos dinamicos
	 */
	public void onEdit(RowEditEvent event) {
		try {
			Lancamentos lanca = (Lancamentos) event.getObject();
			controlarValores(lanca.getId(), lanca);
			
			LancamentosDao dao = new LancamentosDao();
			Lancamentos anterior = dao.getLancamento(lanca.getId());
			dao = null;
			
			lancamentosDao.atualizarLancamento(lanca);
			
			AuditoriaService aud = new AuditoriaService();
			aud.adicionarLogAuditoria(lanca, anterior, getUsuarioLoginBean().getUsuarioLogador(), "Atualizacao");
			aud = null;
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado segurado ",
					((Lancamentos) event.getObject()).getHistorico());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception ex) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao atualizar segurado ",
					((Lancamentos) event.getObject()).getHistorico());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancelada atualização do segurado",
				((Lancamentos) event.getObject()).getHistorico());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void alterarPercente(SelectEvent event) {
		Date date = (Date) event.getObject();
		Calendar cal = Calendar.getInstance();
		this.lacamentoBean.setTipoPessoa("PJ");
		cal.setTime(date);
		this.percentagem = this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR))) == null ? 8.21
				: this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR)));
		if (cal.get(Calendar.YEAR) == 2014) {
			if (!this.tipoPessoa) {
				this.percentagem = 14.33;
			}
		}
	}

	public void alterarPercente() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.lacamentoBean.getPeriodo());
		this.lacamentoBean.setTipoPessoa("PJ");
		this.percentagem = this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR))) == null ? 8.21
				: this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR)));
		if (cal.get(Calendar.YEAR) == 2014) {
			if (!this.tipoPessoa) {
				this.percentagem = 14.33;
			}
		}
	}

	public void tributar() {
		System.out.println("Tributar: " + this.tribuitar);

	}

	public void recalc2014() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.lacamentoBean.getPeriodo());

		if (cal.get(Calendar.YEAR) == 2014) {
			if (this.tipoPessoa) {
				this.lacamentoBean.setTipoPessoa("PF");
				this.percentagem = this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR))) == null ? 8.21
						: this.mapPercentual.get(String.valueOf(cal.get(Calendar.YEAR)));
			} else {
				this.percentagem = 14.33;
				this.lacamentoBean.setTipoPessoa("PJ");
			}
		} else {
			if (this.tipoPessoa) {
				this.lacamentoBean.setTipoPessoa("PF");
			} else {
				this.lacamentoBean.setTipoPessoa("PJ");
			}
		}
	}

	public void addMessagePercent() {
		if (percentagem != 8.21) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
							"Ao alterar o valor percentual" + " do imposto, sobre o a comissão, "
									+ "o valor do prémio líquido será igual o valor da comissão."));
		}
	}

	public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
		lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
		lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
	}

	public void aumentarFonte(Lancamentos l) {
		l.setHistorico(l.getHistorico().toUpperCase());
		l.setProdutor(l.getProdutor().toUpperCase());
	}

	/**
	 * Processar o PDF
	 *
	 * @return
	 */
	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.open();
		pdf.setPageSize(PageSize.A3.rotate());
		Paragraph p = new Paragraph();
		Chunk c = new Chunk();
		c.setFont(new Font(FontFactory.getFont("Times-Roman", 7, Font.BOLD)));
		pdf.add(c);

		// Font tamanho = new Font(Font.HELVETICA, 5);
		// pdf.add(new Paragraph("Teste", tamanho));
		// pdf.add(new Chunk("Teste2", tamanho));
		// pdf.add(new Phrase("Teste3",tamanho));
		// pdf.add(new Paragraph("Fudeu", FontFactory.getFont("Tete", 8)));

		// ServletContext servletContext = (ServletContext)
		// FacesContext.getCurrentInstance().getExternalContext().getContext();
		// String logo = servletContext.getRealPath("") + File.separator +
		// "resources" + File.separator + "demo" + File.separator + "images" +
		// File.separator + "prime_logo.png";

		// pdf.add(Image.getInstance(""));
	}

	/**
	 * Formatar excel do sintetico
	 * 
	 * @return
	 */

	public void preProcessarXls(Object documento) {
		HSSFWorkbook planilha = (HSSFWorkbook) documento;
		HSSFSheet folha = planilha.getSheetAt(0);
		HSSFRow novaLinha = folha.createRow((short) 2);
		novaLinha.createCell(0).setCellValue("Teste1");
		novaLinha.createCell(1).setCellValue("Teste1");
		novaLinha.createCell(2).setCellValue("Teste1");
		novaLinha.createCell(3).setCellValue("Teste1");
		novaLinha.createCell(4).setCellValue("Teste1");
	}

	public void posProcessarXls(Object documento) {
		HSSFWorkbook planilha = (HSSFWorkbook) documento;
		HSSFSheet folha = planilha.getSheetAt(0);
		// HSSFRow novaLinha = folha.createRow((short) 3);
		// novaLinha = folha.getRow(3);

		HSSFRow cabecalho = folha.getRow(0);
		HSSFRow novaLinha = folha.createRow((short) 3);
		novaLinha.createCell(0).setCellValue(cabecalho.getCell(0).getStringCellValue());
		novaLinha.createCell(1).setCellValue(cabecalho.getCell(1).getStringCellValue());
		novaLinha.createCell(2).setCellValue(cabecalho.getCell(2).getStringCellValue());
		novaLinha.createCell(3).setCellValue(cabecalho.getCell(3).getStringCellValue());
		novaLinha.createCell(4).setCellValue(cabecalho.getCell(4).getStringCellValue());

		// HSSFRow cabecalho = folha.getRow(0);

		HSSFCellStyle estiloCelula = planilha.createCellStyle();
		// org.apache.poi.ss.usermodel.Font fonteCabecalho =
		// planilha.createFont();

		// fonteCabecalho.setColor(IndexedColors.WHITE.getIndex());
		// // fonteCabecalho.setBoldweight((short)500);
		// fonteCabecalho.setFontHeightInPoints((short) 16);
		//
		// estiloCelula.setFont(fonteCabecalho);
		estiloCelula.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		// estiloCelula.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		// estiloCelula.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		for (int i = 0; i < cabecalho.getPhysicalNumberOfCells(); i++) {
			// cabecalho.getCell(i).setCellStyle(estiloCelula);
			novaLinha.getCell(i).setCellStyle(estiloCelula);
		}
	}

	public void relatorioXls() {
		HashMap parametros = new HashMap();
	}

	public Lancamentos getLacamentoBean() {
		return lacamentoBean;
	}

	public void setLacamentoBean(Lancamentos lacamentoBean) {
		this.lacamentoBean = lacamentoBean;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<Lancamentos> getLancamentosSintetico() {
		return lancamentosSintetico;
	}

	public void setLancamentosSintetico(List<Lancamentos> lancamentosSintetico) {
		this.lancamentosSintetico = lancamentosSintetico;
	}

	public Date getDtInicialSintetico() {
		return dtInicialSintetico;
	}

	public void setDtInicialSintetico(Date dtInicialSintetico) {
		this.dtInicialSintetico = dtInicialSintetico;
	}

	public Date getDtFinalSintetico() {
		return dtFinalSintetico;
	}

	public void setDtFinalSintetico(Date dtFinalSintetico) {
		this.dtFinalSintetico = dtFinalSintetico;
	}

	public Double getTotal() {
		BigDecimal bd = new BigDecimal(total);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		// return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTotalLiquido() {
		BigDecimal bd = new BigDecimal(totalLiquido);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		// return totalLiquido;
	}

	public void atualizarSituacao() {
		lancamentosDao.atualizaSituacaoGeral();
	}

	public void setTotalLiquido(Double totalLiquido) {
		this.totalLiquido = totalLiquido;
	}

	public Double getPercentagem() {
		return percentagem;
	}

	public void setPercentagem(Double percentagem) {
		this.percentagem = percentagem;
	}

	public String getProdutor() {
		return produtor;
	}

	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}

	public String getSeguradora() {
		return seguradora;
	}

	public void setSeguradora(String seguradora) {
		this.seguradora = seguradora;
	}

	public Double getImposto() {
		return imposto;
	}

	public void setImposto(Double imposto) {
		this.imposto = imposto;
	}

	public Double getPremioLiquido() {
		return premioLiquido;
	}

	public void setPremioLiquido(Double premioLiquido) {
		this.premioLiquido = premioLiquido;
	}

	public Double getVlrComissao() {
		return vlrComissao;
	}

	public void setVlrComissao(Double vlrComissao) {
		this.vlrComissao = vlrComissao;
	}

	public String getSegurado() {
		return segurado;
	}

	public void setSegurado(String segurado) {
		this.segurado = segurado;
	}

	public List<Produtor> getProdutors() {
		return produtors;
	}

	public void setProdutors(List<Produtor> produtors) {
		this.produtors = produtors;
	}

	public Double getTotalLiquidoAnalitico() {
		BigDecimal bd = new BigDecimal(totalLiquidoAnalitico);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		// return totalLiquidoAnalitico;
	}

	public void setTotalLiquidoAnalitico(Double totalLiquidoAnalitico) {
		this.totalLiquidoAnalitico = totalLiquidoAnalitico;
	}

	public Double getTotalComissaoAnalitico() {
		BigDecimal bd = new BigDecimal(totalComissaoAnalitico);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		// return totalComissaoAnalitico;
	}

	public void setTotalComissaoAnalitico(Double totalComissaoAnalitico) {
		this.totalComissaoAnalitico = totalComissaoAnalitico;
	}

	public Double getTotalImpostoAnalitico() {
		BigDecimal bd = new BigDecimal(totalImpostoAnalitico);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		// return totalImpostoAnalitico;
	}

	public void setTotalImpostoAnalitico(Double totalImpostoAnalitico) {
		this.totalImpostoAnalitico = totalImpostoAnalitico;
	}

	public Map<String, Double> getMapPercentual() {
		return mapPercentual;
	}

	public void setMapPercentual(Map<String, Double> mapPercentual) {
		this.mapPercentual = mapPercentual;
	}

	public boolean isSituacao() {
		return situacao;
	}

	public void setSituacao(boolean situacao) {
		this.situacao = situacao;
	}

	public Double getPercentual() {
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}

	public boolean isTribuitar() {
		return tribuitar;
	}

	public void setTribuitar(boolean tribuitar) {
		this.tribuitar = tribuitar;
	}

	public List<Lancamentos> getLancamentosInclusao() {
		return lancamentosInclusao;
	}

	public void setLancamentosInclusao(List<Lancamentos> lancamentosInclusao) {
		this.lancamentosInclusao = lancamentosInclusao;
	}

	public double[] getTotalInclusao() {
		return totalInclusao;
	}

	public void setTotalInclusao(double[] totalInclusao) {
		this.totalInclusao = totalInclusao;
	}

	public boolean isTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(boolean tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public boolean isAtivarFiltroData() {
		return ativarFiltroData;
	}

	public void setAtivarFiltroData(boolean ativarFiltroData) {
		this.ativarFiltroData = ativarFiltroData;
	}

	public UsuarioLoginBean getUsuarioLoginBean() {
		return usuarioLoginBean;
	}

	public void setUsuarioLoginBean(UsuarioLoginBean usuarioLoginBean) {
		this.usuarioLoginBean = usuarioLoginBean;
	}

}
