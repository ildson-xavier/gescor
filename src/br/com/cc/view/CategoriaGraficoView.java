package br.com.cc.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;

import br.com.cc.entidade.Lancamentos;
import br.com.cc.facade.CategoriaFacade;

@ViewScoped
@ManagedBean(name = "grafico")
public class CategoriaGraficoView implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Lancamentos> lancamentosGrafico = new LinkedList<Lancamentos>();
	private List<Lancamentos> lancamentosGraficoData = new LinkedList<Lancamentos>();
	private List<Lancamentos> lancamentosGraficoSub = new LinkedList<Lancamentos>();
	private BarChartModel barModelCategoria;
	private BarChartModel barModelCategoriaData;
	private HorizontalBarChartModel barModelFaturamentoSubTipo;
	private String tipoCategoria = "";
	private String subTipoCategoria = "";
	private Date dataInicial;
	private Date dataFinal;
	private Date dataInicialCat;
	private Date dataFinalCat;
	private String ano = "";
	private Date dt = new Date();
	private Calendar cal = Calendar.getInstance();
	private CategoriaFacade categoriaFacade;
	private Double valorTotal;
	private Long quantidade;
	private Integer anoAtual;
	
	private BarChartModel barModel;

	@PostConstruct
	public void init() {
		cal.setTime(dt);
		setAnoAtual(cal.get(Calendar.YEAR));
		setAno(getAnoAtual().toString());
		
		getGraficoCategoria();
		getFiltroGraficoData();
		//criarGraficoBarrasCusto(ano);
		getFiltroGraficoCategoria();
		getFiltroGraficoCategoriaSub();
	}

	public CategoriaFacade getCategoriaFacade() {
		if (categoriaFacade == null) {
			categoriaFacade = new CategoriaFacade();
		}
		return categoriaFacade;
	}

	/**
	 * Graficos
	 */
	public String getGraficoCategoria() {
		try {
			getCategoriaFacade().criarGraficoCategoria(ano);
			lancamentosGrafico = getCategoriaFacade().getLancamentos();
			barModelCategoria = getCategoriaFacade().getBarModelCategoria();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "graficocategoriaview.jsf";
	}

	public void getFiltroGraficoData() {
		try {
			if (!ano.equals("")) {
				barModelCategoriaData = getCategoriaFacade().criarGraficoData(ano);	
				//criarGraficoBarrasCusto(ano);
				lancamentosGraficoData = getCategoriaFacade().getLancamentos();
			} else {
				barModelCategoriaData = getCategoriaFacade().criarGraficoData(anoAtual.toString());
				//criarGraficoBarrasCusto(anoAtual.toString());
				lancamentosGraficoData = getCategoriaFacade().getLancamentos();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return "";
	}

	public void criarGraficoBarrasCusto(String ano) {
		try {
			setBarModel(categoriaFacade.iniciarBarChartModelCusto(ano));

			getBarModel().setTitle("Relação de custo fixo e variavel");
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
	
	public String getFiltroGraficoCategoria() {
		valorTotal = 0.0;
		quantidade = 0L;
		lancamentosGraficoSub = new ArrayList<>();
		//System.out.println("Data: " + dataInicial + "-- " + dataFinal);
		try {
			if (getDataInicialCat() != null && getDataFinalCat() != null && tipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoria(getDataInicialCat(), getDataFinalCat());
				lancamentosGrafico = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				barModelCategoria = getCategoriaFacade().getBarModelCategoria();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else if (getDataInicialCat() != null && getDataFinalCat() != null && !tipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoria(getDataInicialCat(), getDataFinalCat(), tipoCategoria);
				lancamentosGrafico = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				//System.out.println("lancamentosGrafico > " + lancamentosGrafico.size());
				barModelCategoria = getCategoriaFacade().getBarModelCategoria();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else if (getDataInicialCat() == null && getDataFinalCat() == null && !tipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoria(tipoCategoria);
				lancamentosGrafico = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				barModelCategoria = getCategoriaFacade().getBarModelCategoria();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else {
				//System.out.println("criarGraficoCategoria");
				getCategoriaFacade().criarGraficoCategoria(ano);
				lancamentosGrafico = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				//System.out.println("lancamentosGrafico > " + lancamentosGrafico.size());
				barModelCategoria = getCategoriaFacade().getBarModelCategoria();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getFiltroGraficoCategoriaSub() {
		valorTotal = 0.0;
		quantidade = 0L;
		lancamentosGrafico = new ArrayList<>();
		//System.out.println("Data: " + dataInicial + "-- " + dataFinal);
		try {
			if (getDataInicial() != null && getDataFinal() != null && subTipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoriaSub(getDataInicial(), getDataFinal());
				lancamentosGraficoSub = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				barModelFaturamentoSubTipo = getCategoriaFacade().getBarModelFaturamentoSubTipo();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else if (getDataInicial() != null && getDataFinal() != null && !subTipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoriaSub(getDataInicial(), getDataFinal(), subTipoCategoria);
				lancamentosGraficoSub = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				//System.out.println("lancamentosGrafico > " + lancamentosGrafico.size());
				barModelFaturamentoSubTipo = getCategoriaFacade().getBarModelFaturamentoSubTipo();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else if (getDataInicial() == null && getDataFinal() == null && !subTipoCategoria.equals("")) {
				getCategoriaFacade().criarGraficoCategoriaSub(subTipoCategoria);
				lancamentosGraficoSub = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				barModelFaturamentoSubTipo = getCategoriaFacade().getBarModelFaturamentoSubTipo();
				// setDataInicial(null);
				// setDataFinal(null);
				tipoCategoria = "";
			} else {
				//System.out.println("criarGraficoCategoria");
				getCategoriaFacade().criarGraficoCategoriaSub(ano);
				lancamentosGraficoSub = getCategoriaFacade().getLancamentos();
				calcularValorTotal();
				//System.out.println("lancamentosGrafico > " + lancamentosGrafico.size());
				barModelFaturamentoSubTipo = getCategoriaFacade().getBarModelFaturamentoSubTipo();
				// setDataInicial(null);
				// setDataFinal(null);
				subTipoCategoria = "";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public void calcularValorTotal() {
		if (lancamentosGrafico.size() > 0) {
			for (Lancamentos l : lancamentosGrafico) {
				if (l.getComissao() == null) {
					l.setComissao(0.0);
				}
				if (l.getContador() == null) {
					l.setContador(0L);
				}
				valorTotal += l.getComissao();
				quantidade += l.getContador();
			}
		}
		if (lancamentosGraficoSub.size() > 0) {
			for (Lancamentos l : lancamentosGraficoSub) {
				if (l.getComissao() == null) {
					l.setComissao(0.0);
				}
				if (l.getContador() == null) {
					l.setContador(0L);
				}
				valorTotal += l.getComissao();
				quantidade += l.getContador();
			}
		}
	}

	public List<Lancamentos> getLancamentosGrafico() {
		return lancamentosGrafico;
	}

	public void setLancamentosGrafico(List<Lancamentos> lancamentosGrafico) {
		this.lancamentosGrafico = lancamentosGrafico;
	}

	public BarChartModel getBarModelCategoria() {
		return barModelCategoria;
	}

	public void setBarModelCategoria(BarChartModel barModelCategoria) {
		this.barModelCategoria = barModelCategoria;
	}

	public HorizontalBarChartModel getBarModelFaturamentoSubTipo() {
		return barModelFaturamentoSubTipo;
	}

	public void setBarModelFaturamentoSubTipo(HorizontalBarChartModel barModelFaturamentoSubTipo) {
		this.barModelFaturamentoSubTipo = barModelFaturamentoSubTipo;
	}

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}

	public String getSubTipoCategoria() {
		return subTipoCategoria;
	}

	public void setSubTipoCategoria(String subTipoCategoria) {
		this.subTipoCategoria = subTipoCategoria;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public List<Lancamentos> getLancamentosGraficoSub() {
		return lancamentosGraficoSub;
	}

	public void setLancamentosGraficoSub(List<Lancamentos> lancamentosGraficoSub) {
		this.lancamentosGraficoSub = lancamentosGraficoSub;
	}

	public Date getDataFinalCat() {
		return dataFinalCat;
	}

	public void setDataFinalCat(Date dataFinalCat) {
		this.dataFinalCat = dataFinalCat;
	}

	public Date getDataInicialCat() {
		return dataInicialCat;
	}

	public void setDataInicialCat(Date dataInicialCat) {
		this.dataInicialCat = dataInicialCat;
	}

	public Integer getAnoAtual() {
		return anoAtual;
	}

	public void setAnoAtual(Integer anoAtual) {
		this.anoAtual = anoAtual;
	}

	public BarChartModel getBarModelCategoriaData() {
		return barModelCategoriaData;
	}

	public void setBarModelCategoriaData(BarChartModel barModelCategoriaData) {
		this.barModelCategoriaData = barModelCategoriaData;
	}

	public List<Lancamentos> getLancamentosGraficoData() {
		return lancamentosGraficoData;
	}

	public void setLancamentosGraficoData(List<Lancamentos> lancamentosGraficoData) {
		this.lancamentosGraficoData = lancamentosGraficoData;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

}
