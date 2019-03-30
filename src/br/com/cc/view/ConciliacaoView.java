package br.com.cc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.DonutChartModel;

import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Resultado;
import br.com.cc.facade.ConciliacaoFacade;
import br.com.cc.util.Util;

@ViewScoped
@ManagedBean(name = "conciliacao")
public class ConciliacaoView implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{usuarioView}")
	private UsuarioView usuarioView;
	
	private String ano;
	private String anoFiltro;
	private String mesAno;
	private String mesAno2;
	private String mesAno3;
	
	private Double valor;
	private Double receitas;
	private Double despesas;
	private String periodo;

	private Double valorDialog;
	private Double receitasDialog;
	private Double despesasDialog;
	private String periodoDialog;
	private String situacaoDialog;
	
	private Double valorDialogProdutor;
	private Double receitasDialogProdutor;
	private Double despesasDialogProdutor;
	private String periodoDialogProdutor;
	private String situacaoDialogProdutor;
	private DonutChartModel donutModelDialogProdutor;

	private Calendar cal = Calendar.getInstance();
	private Calendar cal2 = Calendar.getInstance();
	private Calendar cal3 = Calendar.getInstance();

	private Date dt = new Date();
	private Integer anoAtual;

	private String produtor;
	
	private String situacao;	
	private DonutChartModel donutModel;
	private DonutChartModel donutModelDialog;	
	private BarChartModel barModel;
	private BarChartModel barModelProdutor;
	private List<Lancamentos> lancamentos = new ArrayList<>();
	private List<Lancamentos> lancamentosProdutor = new ArrayList<>();
	private ConciliacaoFacade facade;
	
	private Double valor2;
	private Double receitas2;
	private Double despesas2;
	private String periodo2;
	private String situacao2;
	private DonutChartModel donutModel2;
	
	private Double valor3;
	private Double receitas3;
	private Double despesas3;
	private String periodo3;
	private String situacao3;
	private DonutChartModel donutModel3;
	
	private Double valorProdutor;
	private Double receitasProdutor;
	private Double despesasProdutor;
	private String periodoProdutor;
	private String situacaoProdutor;
	private DonutChartModel donutModelProdutor;
	
	private Double valorProdutor2;
	private Double receitasProdutor2;
	private Double despesasProdutor2;
	private String periodoProdutor2;
	private String situacaoProdutor2;
	private DonutChartModel donutModelProdutor2;
	
	private Double valorProdutor3;
	private Double receitasProdutor3;
	private Double despesasProdutor3;
	private String periodoProdutor3;
	private String situacaoProdutor3;
	private DonutChartModel donutModelProdutor3;
	
	
	private String nmprodutor;
	private String anoProdutor;

	private Lancamentos lancamentoBean;
	private Lancamentos lancamentoBeanProdutor;
	
	private List<Resultado> resultados = new ArrayList<>();
	
	private Date dataPesquisa = null;
	private Date dataPesquisaAte = null;
	private String nomeProdutor;
	
	private Double valorTotal = 837.98D;
	private double comissaoBruto;
	private double comissaoLiquida;
	private double despesa;
	private double resultadoBruto;
	private double resultadoLiquido;

	@PostConstruct
	public void init() {
		 //ano = "2015";
		mesAno = "";
		mesAno2 = "";
		mesAno3 = "";

		cal.setTime(dt);
		setAnoAtual(cal.get(Calendar.YEAR));
		setAno(getAnoAtual().toString());
		
		cal2.setTime(dt);
		setAnoAtual(cal2.get(Calendar.YEAR));
		setAno(getAnoAtual().toString());
		
		cal3.setTime(dt);
		setAnoAtual(cal3.get(Calendar.YEAR));
		setAno(getAnoAtual().toString());

		// Diminiu um mês 
		cal.add(Calendar.MONTH, -1);
		// Diminiu dois mês
		cal2.add(Calendar.MONTH, -2);
		// Diminiu tres mês
		cal3.add(Calendar.MONTH, -3);

		mesAno = mesAno.concat(String.valueOf(cal.get(Calendar.MONTH) + 1)).concat("/")
				.concat(String.valueOf(cal.get(Calendar.YEAR)));

		mesAno = Util.ajustarData(mesAno);
		
		mesAno2 = mesAno2.concat(String.valueOf(cal2.get(Calendar.MONTH) + 1)).concat("/")
				.concat(String.valueOf(cal2.get(Calendar.YEAR)));

		mesAno2 = Util.ajustarData(mesAno2);
		
		mesAno3 = mesAno3.concat(String.valueOf(cal3.get(Calendar.MONTH) + 1)).concat("/")
				.concat(String.valueOf(cal3.get(Calendar.YEAR)));

		mesAno3 = Util.ajustarData(mesAno3);
		
		
		nmprodutor = "M & P CORRETORA DE SEGUROS";
		anoProdutor = ano;
		anoFiltro = ano;

		gerarGraficos();
		
		//pesquisarResultado();
	}
	
	public boolean isAdmin(String nome){
		return usuarioView.adicionarUsuario().contains(nome);
	}

	public String gerarGraficos() {
		Util.verificaMemoria("ccomissao-gerarGraficos");
		criarGraficoDonut();
		criarGraficoDonut2();
		criarGraficoDonut3();
		criarGraficoBarras(getAno());
		criarGraficoDonutPorProdutor(nmprodutor);
		criarGraficoDonutPorProdutor2(nmprodutor);
		criarGraficoDonutPorProdutor3(nmprodutor);
		criarGraficoBarrasPorProdutor(getAno(), nmprodutor);
		return "conciliacaoview.jsf";
	}

   
	
	public void criarGraficoDonutDialog() {
		facade = new ConciliacaoFacade();
		//System.out.println("MesAno: " + lancamentoBean.getMesAno());
		try {
			setDonutModelDialog(facade.iniciarDonutChartModelDialog(Util.getFormatDateUsa(lancamentoBean.getMesAno())));

			periodoDialog = facade.getDataDialog();
			valorDialog = facade.getValorDialog();
			receitasDialog = facade.getReceitasDialog();
			despesasDialog = facade.getDespesasDialog();

			if (valorDialog > 0) {
				setSituacaoDialog("Lucro");
			} else {
				setSituacaoDialog("Prejuizo");
			}

			//getDonutModelDialog().setTitle("Custom Options");
			getDonutModelDialog().setLegendPosition("e");
			getDonutModelDialog().setSliceMargin(5);
			getDonutModelDialog().setShowDataLabels(true);
			getDonutModelDialog().setDataFormat("value");
			getDonutModelDialog().setShadow(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}

	public void criarGraficoDonut() {
		facade = new ConciliacaoFacade();
		try {
			setDonutModel(facade.iniciarDonutChartModel(mesAno));
			periodo = facade.getData();
			valor = facade.getValor();
			receitas = facade.getReceitas();
			despesas = facade.getDespesas();
			if (valor > 0) {
				setSituacao("Lucro");
			} else {
				setSituacao("Prejuizo");
			}
			//getDonutModel().setTitle("");
			getDonutModel().setLegendPosition("e");
			getDonutModel().setSliceMargin(5);
			getDonutModel().setShowDataLabels(true);
			getDonutModel().setDataFormat("value");
			getDonutModel().setShadow(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}

	public void criarGraficoDonut2() {
		facade = new ConciliacaoFacade();
		try {
			setDonutModel2(facade.iniciarDonutChartModel(mesAno2));

			periodo2 = facade.getData();
			valor2 = facade.getValor();
			receitas2 = facade.getReceitas();
			despesas2 = facade.getDespesas();

			if (valor2 > 0) {
				setSituacao2("Lucro");
			} else {
				setSituacao2("Prejuizo");
			}

			//getDonutModel2().setTitle("Custom Options");
			getDonutModel2().setLegendPosition("e");
			getDonutModel2().setSliceMargin(5);
			getDonutModel2().setShowDataLabels(true);
			getDonutModel2().setDataFormat("value");
			getDonutModel2().setShadow(true);
			getDonutModel2().setFill(true);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}
	
	public void criarGraficoDonut3() {
		facade = new ConciliacaoFacade();
		try {
			setDonutModel3(facade.iniciarDonutChartModel(getMesAno3()));

			periodo3 = facade.getData();
			valor3 = facade.getValor();
			receitas3 = facade.getReceitas();
			despesas3 = facade.getDespesas();

			if (valor3 > 0) {
				setSituacao3("Lucro");
			} else {
				setSituacao3("Prejuizo");
			}

			//getDonutModel2().setTitle("Custom Options");
			getDonutModel3().setLegendPosition("e");
			getDonutModel3().setSliceMargin(5);
			getDonutModel3().setShowDataLabels(true);
			getDonutModel3().setDataFormat("value");
			getDonutModel3().setShadow(true);
			getDonutModel3().setFill(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}
	
	public void criarGraficoBarrasFiltro() {
		criarGraficoBarras(getAnoFiltro());
	}
	
	public void criarGraficoBarras(String ano) {
		try {
			setBarModel(facade.iniciarBarChartModel(ano));
			setLancamentos(facade.getLancamentos());

			getBarModel().setTitle("Relação de receitas e despesas");
			getBarModel().setLegendPosition("ne");
			getBarModel().setAnimate(true);
			getBarModel().setShowPointLabels(true);
			//getBarModel().setLegendPlacement(LegendPlacement.OUTSIDEGRID);

			Axis xAxis = getBarModel().getAxis(AxisType.X);
			xAxis.setLabel("Período");

			Axis yAxis = getBarModel().getAxis(AxisType.Y);
			yAxis.setLabel("Valores");

			xAxis.setTickAngle(-20);
			yAxis.setTickFormat("R$ %1.2f");
			// yAxis.setMin(0);
			// yAxis.setMax(200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}

	
	// ------------- Graficos por produtor ----------------//
	public void criarGraficoDonutPorProdutorFiltroNome(){
		criarGraficoDonutPorProdutor(nmprodutor);
		criarGraficoDonutPorProdutor2(nmprodutor);
		criarGraficoDonutPorProdutor3(nmprodutor);
		criarGraficoBarrasPorProdutorFiltro();
	}
	
	public void criarGraficoDonutPorProdutor(String nome) {
		facade = new ConciliacaoFacade();
		try {
			setDonutModelProdutor(facade.iniciarDonutChartModelPorProdutor(mesAno, nome));
			periodoProdutor = facade.getDataProdutor();
			valorProdutor = facade.getValorProdutor();
			receitasProdutor = facade.getReceitasProdutor();
			despesasProdutor = facade.getDespesasProdutor();
			if (valorProdutor > 0) {
				setSituacaoProdutor("Lucro");
			} else {
				setSituacaoProdutor("Prejuizo");
			}
			//getDonutModel().setTitle("");
			getDonutModelProdutor().setLegendPosition("e");
			getDonutModelProdutor().setSliceMargin(1);
			getDonutModelProdutor().setShowDataLabels(true);
			getDonutModelProdutor().setDataFormat("value");
			getDonutModelProdutor().setShadow(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void criarGraficoDonutPorProdutor2(String nome) {
		facade = new ConciliacaoFacade();
		try {
			setDonutModelProdutor2(facade.iniciarDonutChartModelPorProdutor(mesAno2, nome));
			setPeriodoProdutor2(facade.getDataProdutor());
			valorProdutor2 = facade.getValorProdutor();
			setReceitasProdutor2(facade.getReceitasProdutor());
			despesasProdutor2 = facade.getDespesasProdutor();
			if (valorProdutor2 > 0) {
				setSituacaoProdutor2("Lucro");
			} else {
				setSituacaoProdutor2("Prejuizo");
			}
			//getDonutModel().setTitle("");
			getDonutModelProdutor2().setLegendPosition("e");
			getDonutModelProdutor2().setSliceMargin(1);
			getDonutModelProdutor2().setShowDataLabels(true);
			getDonutModelProdutor2().setDataFormat("value");
			getDonutModelProdutor2().setShadow(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void criarGraficoDonutPorProdutor3(String nome) {
		facade = new ConciliacaoFacade();
		try {
			setDonutModelProdutor3(facade.iniciarDonutChartModelPorProdutor(mesAno3, nome));
			setPeriodoProdutor3(facade.getDataProdutor());
			valorProdutor3 = facade.getValorProdutor();
			receitasProdutor3 = facade.getReceitasProdutor();
			despesasProdutor3 = facade.getDespesasProdutor();
			if (valorProdutor3 > 0) {
				setSituacaoProdutor3("Lucro");
			} else {
				setSituacaoProdutor3("Prejuizo");
			}
			//getDonutModel().setTitle("");
			getDonutModelProdutor3().setLegendPosition("e");
			getDonutModelProdutor3().setSliceMargin(1);
			getDonutModelProdutor3().setShowDataLabels(true);
			getDonutModelProdutor3().setDataFormat("value");
			getDonutModelProdutor3().setShadow(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void criarGraficoDonutDialogProdutor() {
		facade = new ConciliacaoFacade();
		try {
			setDonutModelDialogProdutor(facade.iniciarDonutChartModelDialogProdutor(Util.getFormatDateUsa(lancamentoBeanProdutor.getMesAno()), nmprodutor));

			periodoDialogProdutor = facade.getDataDialogProdutor();
			valorDialogProdutor = facade.getValorDialogProdutor();
			receitasDialogProdutor = facade.getReceitasDialogProdutor();
			despesasDialogProdutor = facade.getDespesasDialogProdutor();

			if (valorDialogProdutor > 0) {
				setSituacaoDialogProdutor("Lucro");
			} else {
				setSituacaoDialogProdutor("Prejuizo");
			}

			//getDonutModelDialogProdutor().setTitle("Custom Options");
			getDonutModelDialogProdutor().setLegendPosition("e");
			getDonutModelDialogProdutor().setSliceMargin(5);
			getDonutModelDialogProdutor().setShowDataLabels(true);
			getDonutModelDialogProdutor().setDataFormat("value");
			getDonutModelDialogProdutor().setShadow(false);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}

	
	public void criarGraficoBarrasPorProdutorFiltro(){
		criarGraficoBarrasPorProdutor(getAnoProdutor(), nmprodutor);
	}
	
	public void criarGraficoBarrasPorProdutor(String ano, String nome) {
		try {
			setBarModelProdutor(facade.iniciarBarChartModelPorProdutor(ano, nome));
			setLancamentosProdutor(facade.getLancamentosProdutor());

			getBarModelProdutor().setTitle("Relação de receitas e despesas");
			getBarModelProdutor().setLegendPosition("ne");
			getBarModelProdutor().setAnimate(true);
			getBarModelProdutor().setShowPointLabels(true);
			//getBarModel().setLegendPlacement(LegendPlacement.OUTSIDEGRID);

			Axis xAxis = getBarModelProdutor().getAxis(AxisType.X);
			xAxis.setLabel("Período");

			Axis yAxis = getBarModelProdutor().getAxis(AxisType.Y);
			yAxis.setLabel("Valores");

			xAxis.setTickAngle(-20);
			yAxis.setTickFormat("R$ %1.2f");
			// yAxis.setMin(0);
			// yAxis.setMax(200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "conciliacaoview.jsf";
	}
	
	public void pesquisarResultado(){
		facade = new ConciliacaoFacade();
		String anoMes = "0";
		String anoMesAte = "0";
		if (dataPesquisa != null && dataPesquisaAte != null){
			Date data = dataPesquisa;
			Date dataAte = dataPesquisaAte;
			
			Locale local = new Locale("pt", "BR");
			Calendar cal = Calendar.getInstance(local);
			Calendar calAte = Calendar.getInstance(local);
			cal.setTime(data);
			calAte.setTime(dataAte);
			
			
			int ano = cal.get(Calendar.YEAR);
			int mes = cal.get(Calendar.MONTH) + 1;
			
			int anoAte = calAte.get(Calendar.YEAR);
			int mesAte = calAte.get(Calendar.MONTH) + 1;
			
			anoMes = String.valueOf(ano);
			String mess = String.valueOf(mes);
			anoMes += mess.length() == 1 ? "0".concat(mess) : mess;
			
			anoMesAte = String.valueOf(anoAte);
			String messs = String.valueOf(mesAte);
			anoMesAte += messs.length() == 1 ? "0".concat(messs) : messs;
		}
		
		try {
			setResultados(facade.getResultadoConciliacaoGeral(Long.parseLong(anoMes), Long.parseLong(anoMesAte), nomeProdutor));
			calcularTotais();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void calcularTotais(){
		if (!resultados.isEmpty()){
			comissaoBruto = 0;
			comissaoLiquida = 0;
			despesa = 0;
			resultadoBruto = 0;
			resultadoLiquido = 0;
			for (Resultado r : resultados){
				comissaoBruto += r.getBrutoComissao();
				comissaoLiquida += r.getLiquidoComissao();
				despesa += r.getDespesa();
				resultadoBruto += r.getResultadoBruto();
				resultadoLiquido += r.getResultadoLiquido();
			}
		}
	}
	
	public void updateProdutor(){
		this.setNmprodutor(this.getNomeProdutor());
	}
	
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Double getReceitas() {
		return receitas;
	}

	public void setReceitas(Double receitas) {
		this.receitas = receitas;
	}

	public Double getDespesas() {
		return despesas;
	}

	public void setDespesas(Double despesas) {
		this.despesas = despesas;
	}

	public String getPeriodoDialog() {
		return periodoDialog;
	}

	public void setPeriodoDialog(String periodoDialog) {
		this.periodoDialog = periodoDialog;
	}

	public Double getDespesasDialog() {
		return despesasDialog;
	}

	public void setDespesasDialog(Double despesasDialog) {
		this.despesasDialog = despesasDialog;
	}

	public Double getReceitasDialog() {
		return receitasDialog;
	}

	public void setReceitasDialog(Double receitasDialog) {
		this.receitasDialog = receitasDialog;
	}

	public Double getValorDialog() {
		return valorDialog;
	}

	public void setValorDialog(Double valorDialog) {
		this.valorDialog = valorDialog;
	}

	public Lancamentos getLancamentoBean() {
		return lancamentoBean;
	}

	public void setLancamentoBean(Lancamentos lancamentoBean) {
		this.lancamentoBean = lancamentoBean;
	}

	public DonutChartModel getDonutModelDialog() {
		return donutModelDialog;
	}

	public void setDonutModelDialog(DonutChartModel donutModelDialog) {
		this.donutModelDialog = donutModelDialog;
	}

	public String getSituacaoDialog() {
		return situacaoDialog;
	}

	public void setSituacaoDialog(String situacaoDialog) {
		this.situacaoDialog = situacaoDialog;
	}

	public Integer getAnoAtual() {
		return anoAtual;
	}

	public void setAnoAtual(Integer anoAtual) {
		this.anoAtual = anoAtual;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getProdutor() {
		return produtor;
	}

	public void setProdutor(String produtor) {
		this.produtor = produtor;
	}

	public Double getValor2() {
		return valor2;
	}

	public void setValor2(Double valor2) {
		this.valor2 = valor2;
	}

	public Double getReceitas2() {
		return receitas2;
	}

	public void setReceitas2(Double receitas2) {
		this.receitas2 = receitas2;
	}

	public Double getDespesas2() {
		return despesas2;
	}

	public void setDespesas2(Double despesas2) {
		this.despesas2 = despesas2;
	}

	public String getPeriodo2() {
		return periodo2;
	}

	public void setPeriodo2(String periodo2) {
		this.periodo2 = periodo2;
	}

	public DonutChartModel getDonutModel2() {
		return donutModel2;
	}

	public void setDonutModel2(DonutChartModel donutModel2) {
		this.donutModel2 = donutModel2;
	}

	public String getMesAno2() {
		return mesAno2;
	}

	public void setMesAno2(String mesAno2) {
		this.mesAno2 = mesAno2;
	}

	public String getSituacao2() {
		return situacao2;
	}

	public void setSituacao2(String situacao2) {
		this.situacao2 = situacao2;
	}

	public Double getValor3() {
		return valor3;
	}

	public void setValor3(Double valor3) {
		this.valor3 = valor3;
	}

	public Double getReceitas3() {
		return receitas3;
	}

	public void setReceitas3(Double receitas3) {
		this.receitas3 = receitas3;
	}

	public Double getDespesas3() {
		return despesas3;
	}

	public void setDespesas3(Double despesas3) {
		this.despesas3 = despesas3;
	}

	public String getPeriodo3() {
		return periodo3;
	}

	public void setPeriodo3(String periodo3) {
		this.periodo3 = periodo3;
	}

	public String getSituacao3() {
		return situacao3;
	}

	public void setSituacao3(String situacao3) {
		this.situacao3 = situacao3;
	}

	public DonutChartModel getDonutModel3() {
		return donutModel3;
	}

	public void setDonutModel3(DonutChartModel donutModel3) {
		this.donutModel3 = donutModel3;
	}

	public String getMesAno3() {
		return mesAno3;
	}

	public void setMesAno3(String mesAno3) {
		this.mesAno3 = mesAno3;
	}

	public String getAnoFiltro() {
		return anoFiltro;
	}

	public void setAnoFiltro(String anoFiltro) {
		this.anoFiltro = anoFiltro;
	}

	public Double getValorProdutor() {
		return valorProdutor;
	}

	public void setValorProdutor(Double valorProdutor) {
		this.valorProdutor = valorProdutor;
	}

	public Double getReceitasProdutor() {
		return receitasProdutor;
	}

	public void setReceitasProdutor(Double receitasProdutor) {
		this.receitasProdutor = receitasProdutor;
	}

	public Double getDespesasProdutor() {
		return despesasProdutor;
	}

	public void setDespesasProdutor(Double despesasProdutor) {
		this.despesasProdutor = despesasProdutor;
	}

	public String getPeriodoProdutor() {
		return periodoProdutor;
	}

	public void setPeriodoProdutor(String periodoProdutor) {
		this.periodoProdutor = periodoProdutor;
	}

	public String getSituacaoProdutor() {
		return situacaoProdutor;
	}

	public void setSituacaoProdutor(String situacaoProdutor) {
		this.situacaoProdutor = situacaoProdutor;
	}

	public DonutChartModel getDonutModelProdutor() {
		return donutModelProdutor;
	}

	public void setDonutModelProdutor(DonutChartModel donutModelProdutor) {
		this.donutModelProdutor = donutModelProdutor;
	}

	public BarChartModel getBarModelProdutor() {
		return barModelProdutor;
	}

	public void setBarModelProdutor(BarChartModel barModelProdutor) {
		this.barModelProdutor = barModelProdutor;
	}

	public List<Lancamentos> getLancamentosProdutor() {
		return lancamentosProdutor;
	}

	public void setLancamentosProdutor(List<Lancamentos> lancamentosProdutor) {
		this.lancamentosProdutor = lancamentosProdutor;
	}

	public String getNmprodutor() {
		return nmprodutor;
	}

	public void setNmprodutor(String nmprodutor) {
		this.nmprodutor = nmprodutor;
	}

	public String getAnoProdutor() {
		return anoProdutor;
	}

	public void setAnoProdutor(String anoProdutor) {
		this.anoProdutor = anoProdutor;
	}

	public String getSituacaoDialogProdutor() {
		return situacaoDialogProdutor;
	}

	public void setSituacaoDialogProdutor(String situacaoDialogProdutor) {
		this.situacaoDialogProdutor = situacaoDialogProdutor;
	}

	public String getPeriodoDialogProdutor() {
		return periodoDialogProdutor;
	}

	public void setPeriodoDialogProdutor(String periodoDialogProdutor) {
		this.periodoDialogProdutor = periodoDialogProdutor;
	}

	public Double getDespesasDialogProdutor() {
		return despesasDialogProdutor;
	}

	public void setDespesasDialogProdutor(Double despesasDialogProdutor) {
		this.despesasDialogProdutor = despesasDialogProdutor;
	}

	public Double getReceitasDialogProdutor() {
		return receitasDialogProdutor;
	}

	public void setReceitasDialogProdutor(Double receitasDialogProdutor) {
		this.receitasDialogProdutor = receitasDialogProdutor;
	}

	public Double getValorDialogProdutor() {
		return valorDialogProdutor;
	}

	public void setValorDialogProdutor(Double valorDialogProdutor) {
		this.valorDialogProdutor = valorDialogProdutor;
	}

	public DonutChartModel getDonutModelDialogProdutor() {
		return donutModelDialogProdutor;
	}

	public void setDonutModelDialogProdutor(DonutChartModel donutModelDialogProdutor) {
		this.donutModelDialogProdutor = donutModelDialogProdutor;
	}

	public Lancamentos getLancamentoBeanProdutor() {
		return lancamentoBeanProdutor;
	}

	public void setLancamentoBeanProdutor(Lancamentos lancamentoBeanProdutor) {
		this.lancamentoBeanProdutor = lancamentoBeanProdutor;
	}

	
	public Double getValorProdutor2() {
		return valorProdutor2;
	}

	public void setValorProdutor2(Double valorProdutor2) {
		this.valorProdutor2 = valorProdutor2;
	}


	public Double getDespesasProdutor2() {
		return despesasProdutor2;
	}

	public void setDespesasProdutor2(Double despesasProdutor2) {
		this.despesasProdutor2 = despesasProdutor2;
	}

	public String getSituacaoProdutor2() {
		return situacaoProdutor2;
	}

	public void setSituacaoProdutor2(String situacaoProdutor2) {
		this.situacaoProdutor2 = situacaoProdutor2;
	}

	public DonutChartModel getDonutModelProdutor2() {
		return donutModelProdutor2;
	}

	public void setDonutModelProdutor2(DonutChartModel donutModelProdutor2) {
		this.donutModelProdutor2 = donutModelProdutor2;
	}

	public Double getValorProdutor3() {
		return valorProdutor3;
	}

	public void setValorProdutor3(Double valorProdutor3) {
		this.valorProdutor3 = valorProdutor3;
	}

	public Double getReceitasProdutor3() {
		return receitasProdutor3;
	}

	public void setReceitasProdutor3(Double receitasProdutor3) {
		this.receitasProdutor3 = receitasProdutor3;
	}

	public Double getDespesasProdutor3() {
		return despesasProdutor3;
	}

	public void setDespesasProdutor3(Double despesasProdutor3) {
		this.despesasProdutor3 = despesasProdutor3;
	}

	public String getPeriodoProdutor3() {
		return periodoProdutor3;
	}

	public void setPeriodoProdutor3(String periodoProdutor3) {
		this.periodoProdutor3 = periodoProdutor3;
	}

	public String getSituacaoProdutor3() {
		return situacaoProdutor3;
	}

	public void setSituacaoProdutor3(String situacaoProdutor3) {
		this.situacaoProdutor3 = situacaoProdutor3;
	}

	public DonutChartModel getDonutModelProdutor3() {
		return donutModelProdutor3;
	}

	public void setDonutModelProdutor3(DonutChartModel donutModelProdutor3) {
		this.donutModelProdutor3 = donutModelProdutor3;
	}

	public String getPeriodoProdutor2() {
		return periodoProdutor2;
	}

	public void setPeriodoProdutor2(String periodoProdutor2) {
		this.periodoProdutor2 = periodoProdutor2;
	}

	public Double getReceitasProdutor2() {
		return receitasProdutor2;
	}

	public void setReceitasProdutor2(Double receitasProdutor2) {
		this.receitasProdutor2 = receitasProdutor2;
	}

	public UsuarioView getUsuarioView() {
		return usuarioView;
	}

	public void setUsuarioView(UsuarioView usuarioView) {
		this.usuarioView = usuarioView;
	}

	public List<Resultado> getResultados() {
		return resultados;
	}

	public void setResultados(List<Resultado> resultados) {
		this.resultados = resultados;
	}

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	public Date getDataPesquisaAte() {
		return dataPesquisaAte;
	}

	public void setDataPesquisaAte(Date dataPesquisaAte) {
		this.dataPesquisaAte = dataPesquisaAte;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getComissaoBruto() {
		return comissaoBruto;
	}

	public void setComissaoBruto(Double comissaoBruto) {
		this.comissaoBruto = comissaoBruto;
	}

	public Double getComissaoLiquida() {
		return comissaoLiquida;
	}

	public void setComissaoLiquida(Double comissaoLiquida) {
		this.comissaoLiquida = comissaoLiquida;
	}

	public Double getDespesa() {
		return despesa;
	}

	public void setDespesa(Double despesa) {
		this.despesa = despesa;
	}

	public Double getResultadoBruto() {
		return resultadoBruto;
	}

	public void setResultadoBruto(Double resultadoBruto) {
		this.resultadoBruto = resultadoBruto;
	}

	public Double getResultadoLiquido() {
		return resultadoLiquido;
	}

	public void setResultadoLiquido(Double resultadoLiquido) {
		this.resultadoLiquido = resultadoLiquido;
	}

	public String getNomeProdutor() {
		return nomeProdutor;
	}

	public void setNomeProdutor(String nomeProdutor) {
		this.nomeProdutor = nomeProdutor;
	}
}
