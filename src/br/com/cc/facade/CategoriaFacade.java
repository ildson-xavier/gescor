package br.com.cc.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;

import br.com.cc.dao.CategoriaDAO;
import br.com.cc.dao.SubTipoDAO;
import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;
import br.com.cc.util.Util;

public class CategoriaFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	private CategoriaDAO dao = new CategoriaDAO();
	private SubTipoDAO tipoDAO = new SubTipoDAO();
	private List<Lancamentos> lancamentos;
	private BarChartModel barModelCategoria;
	private BarChartModel barModelCategoriaData;
	private HorizontalBarChartModel barModelFaturamentoSubTipo;

	public void adicionarCategoriaComSub(Categoria c) throws Exception {
		dao.adicionarCategoria(c);
		for (SubTipo s : c.getSubTipo()) {
			tipoDAO.adicionarSubTipo(s);
		}
	}

	public void adicionarCategoria(Categoria categoria) throws Exception {
		dao.adicionarCategoria(categoria);
	}

	public void atualizarCategoria(Categoria categoria) throws Exception {
		dao.atualizarCategoria(categoria);
	}

	public void atualizarSubCategoria(SubTipo subTipo) throws Exception {
		tipoDAO.atualizarSubTipo(subTipo);
	}

	public List<Categoria> listarCategorias() throws Exception {
		return dao.getCategorias();
	}

	public List<Categoria> listarCategoriaPorData(Date inicio, Date fim) throws Exception {
		return dao.getCategoriasPorData(inicio, fim);
	}
	
	public List<Categoria> listarCategoriaPorDataESubTipo(Date inicio, Date fim, String subTipo) throws Exception {
		return reagrupar(dao.getCategoriasPorDataESubTipo(inicio, fim, subTipo));
	}
	
	public List<Categoria> reagrupar(List<Categoria> lista) throws Exception{
		List<Categoria> list = lista;
		List<Categoria> novaLista = new ArrayList<>();
		int listaCategoria = this.listarCategorias().size();
		if (list.size() < 3){
			for (int i = 0; i < listaCategoria; i ++){
				if (i < lista.size() && 
						list.get(i).getTipo().equals(this.listarCategorias().get(i).getTipo())){
					novaLista.add(list.get(i));
				} else {
					novaLista.add(this.listarCategorias().get(i));
					novaLista.get(i).setLancamentos(new ArrayList<Lancamentos>());
				}
			}
			return novaLista;
		} else {
			return list;
		}
	}
	
	public List<Categoria> listarCategoriaPorSubTipo(String subTipo) throws Exception {
		return dao.getCategoriasPorSubTipo(subTipo);
	}

	public void removerCategoria(Categoria categoria) throws Exception {
		dao.removerCategoria(categoria);
	}

	public Categoria getCategoria(int id) throws Exception {
		return dao.getCategoria(id);
	}

	public void removerSubTipo(SubTipo subTipo) throws Exception {
		tipoDAO.removerSubTipo(subTipo);
	}

	public SubTipo getSubTipo(int id) throws Exception {
		return tipoDAO.getSubTipo(id);
	}

	public SubTipo getSubTipoPorDescricao(String desc, Categoria categoria) throws Exception {
		return tipoDAO.getSubTipoPorDescricao(desc, categoria);
	}

	public SubTipo getSubTipoPorDescricao(String desc) throws Exception {
		return tipoDAO.getSubTipoPorDescricao(desc);
	}

	public List<SubTipo> getListSubTipoPorDescricao(String desc) throws Exception {
		return tipoDAO.getListSubTipoPorDescricao(desc);
	}

	public Categoria getCategoriaPorTipo(String tipo) throws Exception {
		return dao.getCategoriasPorTipo(tipo);
	}

	public SubTipo getSubtipoPorDescricao(Categoria categoria, String descricao) {
		if (categoria.getSubTipo().size() > 0) {
			for (SubTipo s : categoria.getSubTipo()) {
				if (s.getDescricao().contains(descricao)) {
					return s;
				}
			}
		}
		return null;
	}
	
	public List<Lancamentos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamentos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public HorizontalBarChartModel getBarModelFaturamentoSubTipo() {
		return barModelFaturamentoSubTipo;
	}

	public void setBarModelFaturamentoSubTipo(HorizontalBarChartModel barModelFaturamentoSubTipo) {
		this.barModelFaturamentoSubTipo = barModelFaturamentoSubTipo;
	}

	public BarChartModel getBarModelCategoria() {
		return barModelCategoria;
	}

	public void setBarModelCategoria(HorizontalBarChartModel barModelCategoria) {
		this.barModelCategoria = barModelCategoria;
	}

	/**
	 * Busca por data
	 * @throws Exception 
	 */
	
	public BarChartModel iniciarBarChartModelCusto(String ano) throws Exception{
		BarChartModel model = new BarChartModel();
		System.out.println("Ano: "+ano);
		List<Object[]> list = dao.getCategoriasGraficoDataCusto(ano);
		System.out.println("Lista Tam: "+list.size());
		lancamentos = new ArrayList<>();
		ChartSeries fixo = new ChartSeries();
		ChartSeries variavel = new ChartSeries();
		
		fixo.setLabel("Fixo");
		variavel.setLabel("Variavel");

		Lancamentos	lanc = new Lancamentos();
		for (Object[] obj : list){
			//Lancamentos	lanc = new Lancamentos();
			lanc.setDescricao((String) obj[0]);
			lanc.setMesAno((String) obj[1]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			lanc.setComissao((Double) obj[2]);
			lanc.setValor3((Double) obj[3]);
			if (lanc.getDescricao().contains("Fixo")){
				//receitas.setLabel(lanc.getDescricao());
				fixo.set(lanc.getMesAno(), lanc.getValor3());				
			} else {
				//despesas.setLabel(lanc.getDescricao());
				variavel.set(lanc.getMesAno(), lanc.getValor3());				
			}
			lancamentos.add(lanc);
		}
		model.addSeries(fixo);
		model.addSeries(variavel);
		
		return model;
	}
	
	private BarChartModel iniciarGraficoData(String ano) throws Exception {
		BarChartModel model = new BarChartModel();
		ChartSeries fat = new ChartSeries();
		List<Object[]> list = dao.getCategoriasGraficoData(ano);
		System.out.println("Lista Tam: "+list.size());
		lancamentos = new ArrayList<>();
		fat.setLabel("Totais");
		Lancamentos	lanc = new Lancamentos();
		for (Object[] obj : list){
			//Lancamentos lanc = new Lancamentos();
			lanc.setMesAno((String) obj[0]);
			lanc.setMesAno(Util.getFormatDatePt(lanc.getMesAno()));
			lanc.setComissao((Double) obj[1]);
			lanc.setContador((Long) obj[2]);
			lancamentos.add(lanc);
			System.out.println("Mes/Ano: "+lanc.getMesAno()+ " Valor: "+lanc.getComissao());
			fat.set(lanc.getMesAno(), lanc.getComissao());
		}
		model.addSeries(fat);
		
		return model;
	}
	
	public BarChartModel criarGraficoData(String ano) throws Exception{
		barModelCategoriaData = null;
		barModelCategoriaData = iniciarGraficoData(ano);
		if (barModelCategoriaData != null){
			barModelCategoriaData.setTitle("Despesas por Data");
			barModelCategoriaData.setAnimate(true);
			barModelCategoriaData.setLegendPosition("ne");
			barModelCategoriaData.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
			barModelCategoriaData.setShowPointLabels(true);
			
			Axis xAxis = barModelCategoriaData.getAxis(AxisType.X);
			xAxis.setLabel("Data");

			Axis yAxis = barModelCategoriaData.getAxis(AxisType.Y);
			yAxis.setLabel("Valores");

			xAxis.setTickAngle(-50);
			
			yAxis.setTickFormat("R$ %1.2f");
		}
		
		return barModelCategoriaData;
	}
	
	/**
	 * Busca resumo categoria
	 * 
	 * @throws Exception
	 */

	public void criarGraficoCategoria(Object... values) throws Exception {
		System.out.println("criarGraficoCategoria");
		barModelCategoria = iniciarGraficoCategoria(values);
		System.out.println("Lista: " + getLancamentos().size());
		System.out.println("Bar: " + barModelCategoria);
		if (barModelCategoria != null) {
			barModelCategoria.setTitle("Categoria de Despesas ");
			barModelCategoria.setAnimate(true);
			barModelCategoria.setLegendPosition("ne");
			barModelCategoria.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
			barModelCategoria.setShowPointLabels(true);

			Axis xAxis = barModelCategoria.getAxis(AxisType.X);
			xAxis.setLabel("Categorias");

			Axis yAxis = barModelCategoria.getAxis(AxisType.Y);
			yAxis.setLabel("Valores");
			
			xAxis.setTickAngle(-50);

			yAxis.setTickFormat("R$ %1.2f");
		}
	}

	private BarChartModel iniciarGraficoCategoria(Object... values) throws Exception {
		Object[] objs = values;
		BarChartModel model = null;
		lancamentos = new LinkedList<>();
		if (objs != null) {
			model = new BarChartModel();
			ChartSeries fat = new ChartSeries();
			fat.setLabel("Totais");

			switch (objs.length) {
			case 1:
				if (((String) objs[0]).contains("0")) {
					System.out.println("Case " + objs.length + " -- " + (String) objs[0]);
					List<Object[]> lst = dao.getCategoriasGrafico((String) objs[0]);
					System.out.println("Lista > " + lst.size());
					Lancamentos	lanc = new Lancamentos();
					for (Object[] obj : lst) {
						//Lancamentos lanc = new Lancamentos();
						lanc.setTipo((String) obj[0]);
						//lanc.setMesAno((String) obj[1]);
						lanc.setComissao((Double) obj[1]);
						lanc.setContador((Long) obj[2]);
						lancamentos.add(lanc);

						fat.set(lanc.getTipo(), lanc.getComissao());
					}
					model.addSeries(fat);
				} else {
					System.out.println("Case " + objs.length + " -- " + (String) objs[0]);
					List<Object[]> lst = dao.getCategoriasGraficoTipo((String) objs[0]);
					System.out.println("Lista > " + lst.size());
					Lancamentos	lanc = new Lancamentos();
					for (Object[] obj : lst) {
						//Lancamentos lanc = new Lancamentos();
						lanc.setTipo((String) obj[0]);
						//lanc.setMesAno((String) obj[1]);
						lanc.setComissao((Double) obj[1]);
						lanc.setContador((Long) obj[2]);
						lancamentos.add(lanc);

						fat.set(lanc.getTipo(), lanc.getComissao());
					}
					model.addSeries(fat);
				}

				break;

			case 2:
				List<Object[]> lst2 = dao.getCategoriasGrafico((Date) objs[0], (Date) objs[1]);
				Lancamentos	lanc = new Lancamentos();
				for (Object[] obj : lst2) {
					//Lancamentos lanc = new Lancamentos();
					lanc.setTipo((String) obj[0]);
					//lanc.setMesAno((String) obj[1]);
					lanc.setComissao((Double) obj[1]);
					lanc.setContador((Long) obj[2]);
					lancamentos.add(lanc);

					fat.set(lanc.getTipo(), lanc.getComissao());
				}
				model.addSeries(fat);
				break;

			case 3:
				List<Object[]> lst3 = dao.getCategoriasGrafico((Date) objs[0], (Date) objs[1], (String) objs[2]);
				lanc = null;
				lanc = new Lancamentos();
				for (Object[] obj : lst3) {
					//Lancamentos lanc = new Lancamentos();
					lanc.setTipo((String) obj[0]);
					//lanc.setMesAno((String) obj[1]);
					lanc.setComissao((Double) obj[1]);
					lanc.setContador((Long) obj[2]);
					lancamentos.add(lanc);

					fat.set(lanc.getTipo(), lanc.getComissao());
				}
				model.addSeries(fat);
				break;

			default:
				System.out.println("Seleciona o filtro");
				model = null;
				break;
			}
		}
		return model;
	}
	
	/**
	 * Busca resumo categoria sub
	 * 
	 * @throws Exception
	 */

	public void criarGraficoCategoriaSub(Object... values) throws Exception {
		System.out.println("criarGraficoCategoria");
		barModelFaturamentoSubTipo = iniciarGraficoCategoriaSub(values);
		System.out.println("Lista: " + getLancamentos().size());
		System.out.println("Bar: " + barModelFaturamentoSubTipo);
		if (barModelFaturamentoSubTipo != null) {
			barModelFaturamentoSubTipo.setTitle("Sub-Categorias de Despesas: ");
			barModelFaturamentoSubTipo.setAnimate(true);
			barModelFaturamentoSubTipo.setLegendPosition("ne");
			barModelFaturamentoSubTipo.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
			barModelFaturamentoSubTipo.setShowPointLabels(true);
			barModelFaturamentoSubTipo.setStacked(true);
			
			Axis xAxis = barModelFaturamentoSubTipo.getAxis(AxisType.X);
			xAxis.setLabel("Valores");

			Axis yAxis = barModelFaturamentoSubTipo.getAxis(AxisType.Y);
			yAxis.setLabel("Sub-Categorias");
			
			xAxis.setTickAngle(-50);
			
			yAxis.setTickFormat("R$ %1.2f");

		}
	}

	private HorizontalBarChartModel iniciarGraficoCategoriaSub(Object... values) throws Exception {
		Object[] objs = values;
		HorizontalBarChartModel model = null;
		lancamentos = new LinkedList<>();
		if (objs != null) {
			model = new HorizontalBarChartModel();
			ChartSeries fat = new ChartSeries();
			fat.setLabel("Totais");

			switch (objs.length) {
			case 1:
				if (((String) objs[0]).contains("0")) {
					System.out.println("Case " + objs.length + " -- " + (String) objs[0]);
					List<Object[]> lst = dao.getCategoriasPorSubTipoGrafico((String) objs[0]);
					System.out.println("Lista > " + lst.size());
					Lancamentos	lanc = new Lancamentos();
					for (Object[] obj : lst) {
						//Lancamentos lanc = new Lancamentos();
						lanc.setTipo((String) obj[0]);
						//lanc.setMesAno((String) obj[1]);
						lanc.setComissao((Double) obj[1]);
						lanc.setContador((Long) obj[2]);
						lancamentos.add(lanc);

						if (lanc.getComissao() != null && lanc.getComissao() > 0){
							fat.set(lanc.getTipo(), lanc.getComissao());
						}
						
					}
					model.addSeries(fat);
				} else {
					System.out.println("Case " + objs.length + " -- " + (String) objs[0]);
					List<Object[]> lst = dao.getCategoriasPorSubTipoGraficoDescr((String) objs[0]);
					System.out.println("Lista > " + lst.size());
					Lancamentos	lanc = new Lancamentos();
					for (Object[] obj : lst) {
						//Lancamentos lanc = new Lancamentos();
						lanc.setTipo((String) obj[0]);
						//lanc.setMesAno((String) obj[1]);
						lanc.setComissao((Double) obj[1]);
						lanc.setContador((Long) obj[2]);
						lancamentos.add(lanc);

						if (lanc.getComissao() != null && lanc.getComissao() > 0){
							fat.set(lanc.getTipo(), lanc.getComissao());
						}
					}					
					model.addSeries(fat);
				}

				break;

			case 2:
				List<Object[]> lst2 = dao.getCategoriasPorSubTipoGrafico((Date) objs[0], (Date) objs[1]);
				Lancamentos	lanc = new Lancamentos();
				for (Object[] obj : lst2) {
					//Lancamentos lanc = new Lancamentos();
					lanc.setTipo((String) obj[0]);
					//lanc.setMesAno((String) obj[1]);
					lanc.setComissao((Double) obj[1]);
					lanc.setContador((Long) obj[2]);
					lancamentos.add(lanc);

					if (lanc.getComissao() != null && lanc.getComissao() > 0){
						fat.set(lanc.getTipo(), lanc.getComissao());
					}
				}
				model.addSeries(fat);
				break;

			case 3:
				List<Object[]> lst3 = dao.getCategoriasPorSubTipoGrafico((Date) objs[0], (Date) objs[1], (String) objs[2]);
				lanc = null;
				lanc = new Lancamentos();
				for (Object[] obj : lst3) {
					//Lancamentos lanc = new Lancamentos();
					lanc.setTipo((String) obj[0]);
					//lanc.setMesAno((String) obj[1]);
					lanc.setComissao((Double) obj[1]);
					lanc.setContador((Long) obj[2]);
					lancamentos.add(lanc);

					if (lanc.getComissao() != null && lanc.getComissao() > 0){
						fat.set(lanc.getTipo(), lanc.getComissao());
					}
				}
				model.addSeries(fat);
				break;

			default:
				System.out.println("Seleciona o filtro");
				model = null;
				break;
			}
		}
		return model;
	}

	public BarChartModel getBarModelCategoriaData() {
		return barModelCategoriaData;
	}

	public void setBarModelCategoriaData(BarChartModel barModelCategoriaData) {
		this.barModelCategoriaData = barModelCategoriaData;
	}
}

