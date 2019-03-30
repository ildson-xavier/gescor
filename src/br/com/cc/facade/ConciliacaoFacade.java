package br.com.cc.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;

import br.com.cc.dao.CategoriaDAO;
import br.com.cc.dao.ConciliacaoDao;
import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Resultado;
import br.com.cc.util.Util;

public class ConciliacaoFacade implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Lancamentos> lancamentos;
	private List<Lancamentos> lancamentosProdutor;
	private Double valor;
	private Double receitas;
	private Double despesas;
	private String data;
	
	private Double valorProdutor;
	private Double receitasProdutor;
	private Double despesasProdutor;
	private String dataProdutor;
	
	private Double valorDialog;
	private Double receitasDialog;
	private Double despesasDialog;
	private String dataDialog;
	
	private Double valorDialogProdutor;
	private Double receitasDialogProdutor;
	private Double despesasDialogProdutor;
	private String dataDialogProdutor;

	private ConciliacaoDao dao = ConciliacaoDao.Criar();
	private CategoriaDAO categoriaDAO = new CategoriaDAO();
	
	
	/**
	 * Grafico semi-circulo por produto
	 * @param mesAno
	 * @return
	 * @throws Exception
	 */
	public DonutChartModel iniciarDonutChartModelPorProdutor(String mesAno, String produtor) throws Exception{
		DonutChartModel model = new DonutChartModel();
		Categoria categoria = categoriaDAO.getCategoriasPorTipo(produtor);
		valorProdutor = 0.0;
		receitasProdutor = 0.0;
		despesasProdutor = 0.0;
		Map<String, Number> circle = new LinkedHashMap<String, Number>();
		List<Object[]> list = dao.getGraficoPorProdutorDonut(mesAno, categoria);
		//System.out.println("Lista Tam: "+list.size());
		
		for (Object[] obj : list){
			Lancamentos lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setComissao((Double) obj[1]);
			lanc.setValor3((Double) obj[2]);
			lanc.setMesAno((String) obj[3]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				valorProdutor += lanc.getComissao();
				receitasProdutor = lanc.getComissao();
				circle.put(lanc.getDescricao(), lanc.getComissao());
			} else {
				lanc.setDescricao("Despesas");
				valorProdutor -= lanc.getValor3();
				despesasProdutor = lanc.getValor3();
				circle.put(lanc.getDescricao(), lanc.getValor3());
			}
			dataProdutor = lanc.getMesAno();			
			//circle.put(lanc.getDescricao(), lanc.getValor3());
		}
		model.addCircle(circle);
		model.setDataFormat("percent");
		model.setShowDataLabels(true);
		return model;
	}

	
	
	/**
	 * Grafico de semi-circulo
	 * @param mesAno
	 * @return
	 * @throws Exception
	 */
	public DonutChartModel iniciarDonutChartModel(String mesAno) throws Exception{
		DonutChartModel model = new DonutChartModel();
		valor = 0.0;
		receitas = 0.0;
		despesas = 0.0;
		Map<String, Number> circle = new LinkedHashMap<String, Number>();
		List<Object[]> list = dao.getGraficoDonut(mesAno);
		//System.out.println("Lista Tam: "+list.size());
		
		for (Object[] obj : list){
			Lancamentos lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setValor3((Double) obj[1]);
			lanc.setMesAno((String) obj[2]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				valor += lanc.getValor3();
				receitas = lanc.getValor3();
			} else {
				lanc.setDescricao("Despesas");
				valor -= lanc.getValor3();
				despesas = lanc.getValor3();
			}
			data = lanc.getMesAno();			
			circle.put(lanc.getDescricao(), lanc.getValor3());
		}
		model.addCircle(circle);
		model.setDataFormat("percent");
		model.setShowDataLabels(true);
		return model;
	}

	/**
	 * Grafico do Dialog
	 * @param mesAno
	 * @return
	 * @throws Exception
	 */
	public DonutChartModel iniciarDonutChartModelDialog(String mesAno) throws Exception{
		DonutChartModel model = new DonutChartModel();
		valorDialog = 0.0;
		receitasDialog = 0.0;
		despesasDialog = 0.0;
		Map<String, Number> circle = new LinkedHashMap<String, Number>();
		List<Object[]> list = dao.getGraficoDonutDialog(mesAno);
		//System.out.println("Lista Tam: "+list.size());
		
		for (Object[] obj : list){
			Lancamentos lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setValor3((Double) obj[1]);
			lanc.setMesAno((String) obj[2]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				valorDialog += lanc.getValor3();
				receitasDialog = lanc.getValor3();
			} else {
				lanc.setDescricao("Despesas");
				valorDialog -= lanc.getValor3();
				despesasDialog = lanc.getValor3();
			}
			dataDialog = lanc.getMesAno();			
			circle.put(lanc.getDescricao(), lanc.getValor3());
		}
		model.addCircle(circle);
		model.setDataFormat("percent");
		model.setShowDataLabels(true);
		return model;
	}

	/**
	 * Grafico Dialog por produtor
	 * @param mesAno
	 * @return
	 * @throws Exception
	 */
	public DonutChartModel iniciarDonutChartModelDialogProdutor(String mesAno, String produtor) throws Exception{
		DonutChartModel model = new DonutChartModel();
		Categoria categoria = categoriaDAO.getCategoriasPorTipo(produtor);
		valorDialogProdutor = 0.0;
		receitasDialogProdutor = 0.0;
		despesasDialogProdutor = 0.0;
		Map<String, Number> circle = new LinkedHashMap<String, Number>();
		List<Object[]> list = dao.getGraficoDonutDialogProduto(mesAno, categoria);
		//System.out.println("Lista Tam: "+list.size());
		
		for (Object[] obj : list){
			Lancamentos lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setComissao((Double) obj[1]);
			lanc.setValor3((Double) obj[2]);
			lanc.setMesAno((String) obj[3]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				valorDialogProdutor += lanc.getComissao();
				receitasDialogProdutor = lanc.getComissao();
				circle.put(lanc.getDescricao(), lanc.getComissao());
			} else {
				lanc.setDescricao("Despesas");
				valorDialogProdutor -= lanc.getValor3();
				despesasDialogProdutor = lanc.getValor3();
				circle.put(lanc.getDescricao(), lanc.getValor3());
			}
			dataDialogProdutor = lanc.getMesAno();			
			//circle.put(lanc.getDescricao(), lanc.getValor3());
		}
		model.addCircle(circle);
		model.setDataFormat("percent");
		model.setShowDataLabels(true);
		return model;
	}
	
	/**
	 * Grafico de barras por produtor
	 * @param ano
	 * @return
	 * @throws Exception
	 */
	public BarChartModel iniciarBarChartModelPorProdutor(String ano, String produtor) throws Exception{
		BarChartModel model = new BarChartModel();
		Categoria categoria = categoriaDAO.getCategoriasPorTipo(produtor);
		//System.out.println("Ano: "+ano);
		List<Object[]> list = dao.getGraficoPorProdutorBarras(ano, categoria);
		//System.out.println("Lista Tam: "+list.size());
		lancamentosProdutor = new ArrayList<>();
		ChartSeries receitas = new ChartSeries();
		ChartSeries despesas = new ChartSeries();
		
		receitas.setLabel("Receitas");
		despesas.setLabel("Despesas");
		for (Object[] obj : list){
			Lancamentos	lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setComissao((Double) obj[1]);
			lanc.setValor3((Double) obj[2]);
			lanc.setMesAno((String) obj[3]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				//receitas.setLabel(lanc.getDescricao());
				receitas.set(lanc.getMesAno(), lanc.getComissao());				
			} else {
				lanc.setDescricao("Despesas");
				//despesas.setLabel(lanc.getDescricao());
				despesas.set(lanc.getMesAno(), lanc.getValor3());				
			}
			lancamentosProdutor.add(lanc);
		}
		model.addSeries(receitas);
		model.addSeries(despesas);
		
		return model;
	}
	
	
	/**
	 * Grafico de barras
	 * @param ano
	 * @return
	 * @throws Exception
	 */
	public BarChartModel iniciarBarChartModel(String ano) throws Exception{
		BarChartModel model = new BarChartModel();
		//System.out.println("Ano: "+ano);
		List<Object[]> list = dao.getGraficoBarras(ano);
		//System.out.println("Lista Tam: "+list.size());
		lancamentos = new ArrayList<>();
		ChartSeries receitas = new ChartSeries();
		ChartSeries despesas = new ChartSeries();
		
		receitas.setLabel("Receitas");
		despesas.setLabel("Despesas");
		int i = 0;
		for (Object[] obj : list){
			Lancamentos	lanc = new Lancamentos();
			lanc.setClassificacao((int) obj[0]);
			lanc.setValor3((Double) obj[1]);
			lanc.setMesAno((String) obj[2]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			if (lanc.getClassificacao() == 1){
				lanc.setDescricao("Receitas");
				//receitas.setLabel(lanc.getDescricao());
				receitas.set(lanc.getMesAno(), lanc.getValor3());				
			} else {
				lanc.setDescricao("Despesas");
				//despesas.setLabel(lanc.getDescricao());
				despesas.set(lanc.getMesAno(), lanc.getValor3());				
			}
			lancamentos.add(lanc);
		}
		model.addSeries(receitas);
		model.addSeries(despesas);
		
		return model;
	}
	
	public List<Resultado> getResultadoConciliacaoGeral(Long anoMes, Long anoMesAte, String produtor) throws Exception{
		return dao.getResultadoConciliacaoGeral(anoMes, anoMesAte, produtor);
	}
	
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public Double getDespesas() {
		return despesas;
	}

	public void setDespesas(Double despesas) {
		this.despesas = despesas;
	}

	public Double getReceitas() {
		return receitas;
	}

	public void setReceitas(Double receitas) {
		this.receitas = receitas;
	}

	public Double getValorDialog() {
		return valorDialog;
	}

	public void setValorDialog(Double valorDialog) {
		this.valorDialog = valorDialog;
	}

	public Double getReceitasDialog() {
		return receitasDialog;
	}

	public void setReceitasDialog(Double receitasDialog) {
		this.receitasDialog = receitasDialog;
	}

	public Double getDespesasDialog() {
		return despesasDialog;
	}

	public void setDespesasDialog(Double despesasDialog) {
		this.despesasDialog = despesasDialog;
	}

	public String getDataDialog() {
		return dataDialog;
	}

	public void setDataDialog(String dataDialog) {
		this.dataDialog = dataDialog;
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



	public String getDataProdutor() {
		return dataProdutor;
	}



	public void setDataProdutor(String dataProdutor) {
		this.dataProdutor = dataProdutor;
	}



	public List<Lancamentos> getLancamentosProdutor() {
		return lancamentosProdutor;
	}



	public void setLancamentosProdutor(List<Lancamentos> lancamentosProdutor) {
		this.lancamentosProdutor = lancamentosProdutor;
	}



	public Double getValorDialogProdutor() {
		return valorDialogProdutor;
	}



	public void setValorDialogProdutor(Double valorDialogProdutor) {
		this.valorDialogProdutor = valorDialogProdutor;
	}



	public Double getReceitasDialogProdutor() {
		return receitasDialogProdutor;
	}



	public void setReceitasDialogProdutor(Double receitasDialogProdutor) {
		this.receitasDialogProdutor = receitasDialogProdutor;
	}



	public Double getDespesasDialogProdutor() {
		return despesasDialogProdutor;
	}



	public void setDespesasDialogProdutor(Double despesasDialogProdutor) {
		this.despesasDialogProdutor = despesasDialogProdutor;
	}



	public String getDataDialogProdutor() {
		return dataDialogProdutor;
	}



	public void setDataDialogProdutor(String dataDialogProdutor) {
		this.dataDialogProdutor = dataDialogProdutor;
	}
}
