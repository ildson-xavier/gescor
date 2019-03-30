/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.grafico;

import br.com.cc.dao.GraficoDao;
import br.com.cc.entidade.Lancamentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Usuario
 */
@ManagedBean
@ViewScoped
public class GraficoView_bkp implements Serializable {

    private LineChartModel lineChartModelSeguradoras;
    private LineChartModel lineChartModelCrescimento;
    private BarChartModel barModelFaturamento, barModelFaturamentoPorPordutor;
    private GraficoDao graficoDao = new GraficoDao();
    private String anoSeg, anoCres, anoFat, anoFatProd, nomeProd;
    private int indexTab = 0;
    private Double maxValor = 0.0, maxValorAux = 0.0;

    boolean graficoSeguradoras = false,
            graficoFaturamento = false,
            graficoCrescimento = false,
            graficoFatProdutor = false;

    private String ano;
    private String produtor;
    private Map<String, String> anos;
    private Map<String, String> produtores;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

    @PostConstruct
    public void init() {
        anos = new HashMap<String, String>();
        anos.put("2012", "2012");
        anos.put("2013", "2013");
        anos.put("2014", "2014");
        anos.put("2015", "2015");
        anos.put("2016", "2016");
        anos.put("2017", "2017");
        anos.put("2018", "2018");
        anos.put("2019", "2019");
        anos.put("2020", "2020");

        anoSeg = "2015";
        anoCres = "2015";
        anoFat = "2015";
        anoFatProd = "2015";
        ano = anoFatProd;
        nomeProd = "CORRETORA DE SEGUROS";
        produtor = nomeProd;
        criarGraficoSeguradoras();
        criarGraficoCresicimento();
        criarGraficoFaturamento();
        criarGraficoFaturamentoPorProdutor();
        onAnoSelecionado();
    }

    public String fazerGraficoSeguradoras() {
        criarGraficoSeguradoras();
        indexTab = 0;
        return "graficoview.jsf";
    }

    public String fazerGraficoCrescimento() {
        criarGraficoCresicimento();
        indexTab = 1;
        return "graficoview.jsf";
    }

    public String fazerGraficoFaturamento() {
        criarGraficoFaturamento();
        indexTab = 2;
        return "graficoview.jsf";
    }

    public String fazerGraficoFaturamentoPorProdutor() {
        if (verificarSelecao()) {
            criarGraficoFaturamentoPorProdutor();
        }
        System.out.println("Produtor: " + produtor + "Ano: " + ano);
        indexTab = 3;
        return "graficoview.jsf";
    }

    /**
     * Grafico de linha que monstra o faturamento por mes e seguradora
     */
    private void criarGraficoSeguradoras() {
        try {
            lineChartModelSeguradoras = iniciarGraficoSeguradora();
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
                    ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (lineChartModelSeguradoras != null) {
            graficoSeguradoras = true;
            lineChartModelSeguradoras.setTitle("Seguradoras");
            lineChartModelSeguradoras.setAnimate(true);
            lineChartModelSeguradoras.setLegendPosition("ne");
            lineChartModelSeguradoras.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
            //lineChartModelSeguradoras.setZoom(true);
            lineChartModelSeguradoras.setShowPointLabels(true);
            lineChartModelSeguradoras.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
            Axis yAxis = lineChartModelSeguradoras.getAxis(AxisType.Y);
            yAxis.setMin(0);
            yAxis.setTickFormat("R$ %1.2f");
        }

    }

    private LineChartModel iniciarGraficoSeguradora() throws Exception {
        String seg = "";
        int index = 0;
        LineChartModel model = new LineChartModel();
        if (getSeries().size() > 0) {
            for (LineChartSeries serie : getSeries()) {
                model.addSeries(serie);
            }
        } else {
            throw new Exception("Não há informação para o ano [" + anoSeg + "] pesquisado.");
        }
        return model;
    }

    private List<LineChartSeries> getSeries() {

        LineChartSeries[] series = new LineChartSeries[12];
        List<LineChartSeries> listSeries = new LinkedList<LineChartSeries>();

        Lancamentos[] lancamento = new Lancamentos[12];

        int index = 0,
                indexPorto = 0,
                indexBradesco = 0,
                indexAllianz = 0,
                indexMapfre = 0,
                indexSul = 0,
                indexTokio = 0,
                indexHdi = 0,
                indexAzul = 0;
        String segPorto = "",
                segBradesco = "",
                segAllianz = "",
                segMapfre = "",
                segSul = "",
                segTokio = "",
                segHdi = "",
                segAzul = "";

        try {
            List<Object[]> objs = graficoDao.getGraficoSeguradora(anoSeg);

            for (Object[] obj : objs) {
                Lancamentos lanc = new Lancamentos();
                lanc.setSeguradora((String) obj[0]);
                lanc.setMesAno((String) obj[1]);
                lanc.setValor3((Double) obj[2]);

                if (lanc.getSeguradora().equals("PortoSeguro")) {
                    if (!segPorto.equals(lanc.getSeguradora())) {
                        segPorto = lanc.getSeguradora();
                        indexPorto = index;
                        series[indexPorto] = new LineChartSeries();
                        series[indexPorto].setLabel(segPorto);
                        series[indexPorto].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexPorto].set(lanc.getMesAno(), lanc.getValor3());
                    }

                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                    
                } else if (lanc.getSeguradora().equals("Bradesco")) {
                    if (!segBradesco.equals(lanc.getSeguradora())) {
                        segBradesco = lanc.getSeguradora();
                        indexBradesco = index;
                        series[indexBradesco] = new LineChartSeries();
                        series[indexBradesco].setLabel(segBradesco);
                        series[indexBradesco].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexBradesco].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("Azul")) {
                    if (!segAzul.equals(lanc.getSeguradora())) {
                        segAzul = lanc.getSeguradora();
                        indexAzul = index;
                        series[indexAzul] = new LineChartSeries();
                        series[indexAzul].setLabel(segAzul);
                        series[indexAzul].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexAzul].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("Allianz")) {
                    if (!segAllianz.equals(lanc.getSeguradora())) {
                        segAllianz = lanc.getSeguradora();
                        indexAllianz = index;
                        series[indexAllianz] = new LineChartSeries();
                        series[indexAllianz].setLabel(segAllianz);
                        series[indexAllianz].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexAllianz].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("Mapfre")) {
                    if (!segMapfre.equals(lanc.getSeguradora())) {
                        segMapfre = lanc.getSeguradora();
                        indexMapfre = index;
                        series[indexMapfre] = new LineChartSeries();
                        series[indexMapfre].setLabel(segMapfre);
                        series[indexMapfre].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexMapfre].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("SulAmerica")) {
                    if (!segSul.equals(lanc.getSeguradora())) {
                        segSul = lanc.getSeguradora();
                        indexSul = index;
                        series[indexSul] = new LineChartSeries();
                        series[indexSul].setLabel(segSul);
                        series[indexSul].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexSul].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("TokioMarine")) {
                    if (!segTokio.equals(lanc.getSeguradora())) {
                        segTokio = lanc.getSeguradora();
                        indexTokio = index;
                        series[indexTokio] = new LineChartSeries();
                        series[indexTokio].setLabel(segTokio);
                        series[indexTokio].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexTokio].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                } else if (lanc.getSeguradora().equals("HDI")) {
                    if (!segHdi.equals(lanc.getSeguradora())) {
                        segHdi = lanc.getSeguradora();
                        indexHdi = index;
                        series[indexHdi] = new LineChartSeries();
                        series[indexHdi].setLabel(segHdi);
                        series[indexHdi].set(lanc.getMesAno(), lanc.getValor3());
                        index++;
                    } else {
                        series[indexHdi].set(lanc.getMesAno(), lanc.getValor3());
                    }
                    System.out.println("Indice: ["+index+"] "+lanc.getSeguradora()+"Mes: "+lanc.getMesAno() +" Valor: "+lanc.getValor3());
                }
            }

            for (int i = 0; i < index; i++) {
                listSeries.add(series[i]);
            }

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao gerar grafico das seguradoras ", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ex.printStackTrace();
        }

        return listSeries;
    }

    /**
     * Grafico que monstra a taxa basica de crescimento e a taxa media de
     * crescimento
     *
     * @return
     */
    private void criarGraficoCresicimento() {
        try {
            lineChartModelCrescimento = iniciarGraficoCrescimento();
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ",
                    ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        //lineChartModelCrescimento.setTitle("Taxa Basica de Crescimento");
        if (lineChartModelCrescimento != null) {
            graficoCrescimento = true;
            lineChartModelCrescimento.setAnimate(true);
            lineChartModelCrescimento.setLegendPosition("ne");
            lineChartModelCrescimento.setLegendPlacement(LegendPlacement.INSIDE);
            //lineChartModelSeguradoras.setZoom(true);
            lineChartModelCrescimento.setShowPointLabels(true);
            lineChartModelCrescimento.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
            Axis yAxis = lineChartModelCrescimento.getAxis(AxisType.Y);
            //yAxis.setMin(0);
            yAxis.setTickFormat("%1.2f %");
        }

        //yAxis.setMax(10000.00);
    }

    private LineChartModel iniciarGraficoCrescimento() throws Exception {
        LineChartModel model = new LineChartModel();
        if (getSeriesCrescimento().size() > 0) {
            for (LineChartSeries l : getSeriesCrescimento()) {
                model.addSeries(l);
            }
        } else {
            throw new Exception("Não há informação para o ano [" + anoCres + "] pesquisado.");
        }

        return model;
    }

    private List<LineChartSeries> getSeriesCrescimento() {
        LineChartSeries series[] = new LineChartSeries[2];
        List<LineChartSeries> listSerie = new ArrayList<LineChartSeries>();
        List<Lancamentos> listaLanc = new ArrayList<Lancamentos>();

        int index = 0;
        Double valor = 0.0,
                vlrInicial = 0.0,
                vlrFinal = 0.0,
                vlrDiv = 0.0,
                vlrPeriodo = 0.0,
                vlrMedia = 0.0;

        series[0] = new LineChartSeries();
        series[0].setLabel("Taxa Básica de Crescimento");
        series[1] = new LineChartSeries();
        series[1].setLabel("Taxa Média de Crescimento");

        try {
            List<Object[]> objs = graficoDao.getGraficoSeguradoraCrescimento(anoCres);

            for (Object[] obj : objs) {
                Lancamentos lanc = new Lancamentos();
                lanc.setMesAno((String) obj[0]);
                lanc.setValor3((Double) obj[1]); // premio liquido
                if (index == 0) {
                    valor = lanc.getValor3();
                    vlrInicial = lanc.getValor3();
                    //listaLanc.add(lanc);
                    index++;
                } else {
                    lanc.setValor2((lanc.getValor3() / valor) - 1);
                    lanc.setValor2(lanc.getValor2() * 100);
                    series[0].set(lanc.getMesAno(), lanc.getValor2());
                    //System.out.println("Data: " + lanc.getMesAno());
                    //System.out.println("Valor3: " + lanc.getValor3());
                    //System.out.println("Valor2: " + lanc.getValor2());
                    listaLanc.add(lanc);
                    valor = lanc.getValor3();
                    vlrFinal = valor;
                    index++;
                }
            }

            // Calcula a taxa media de crescimento por periodo
            vlrDiv = vlrFinal / vlrInicial;
            vlrPeriodo = (double) 1 / (index - 1);
            vlrMedia = Math.pow(vlrDiv, vlrPeriodo) - 1;
            vlrMedia *= 100; //%

            if (listaLanc.size() > 0) {
                for (Lancamentos l : listaLanc) {
                    series[1].set(l.getMesAno(), vlrMedia);
                }
            }

            listSerie.add(series[0]);
            listSerie.add(series[1]);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar grafico de crescimento ", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ex.printStackTrace();
        }

        return listSerie;
    }

    /**
     *
     * @return
     */
    private void criarGraficoFaturamento() {
        barModelFaturamento = iniciarGraficoFaturamento();

        if (barModelFaturamento != null) {
            graficoFaturamento = true;
            barModelFaturamento.setTitle("Faturamento");
            barModelFaturamento.setAnimate(true);
            barModelFaturamento.setLegendPosition("ne");
            barModelFaturamento.setShowPointLabels(true);
            barModelFaturamento.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

            Axis xAxis = barModelFaturamento.getAxis(AxisType.X);
            xAxis.setLabel("Mês");

            Axis yAxis = barModelFaturamento.getAxis(AxisType.Y);
            yAxis.setLabel("Valores");

            yAxis.setTickFormat("R$ %1.2f");
        }

    }

    private BarChartModel iniciarGraficoFaturamento() {
        BarChartModel model = new BarChartModel();

        ChartSeries fat = new ChartSeries();
        fat.setLabel("Totais");
        try {
            List<Object[]> objs = graficoDao.getGraficoSeguradoraFaturamento(anoFat);

            for (Object[] obj : objs) {
                fat.set((String) obj[0], (Double) obj[1]);
            }

            model.addSeries(fat);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar grafico de faturamento ", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ex.printStackTrace();
        }

        return model;
    }

    /**
     * Grafico de faturamento por produtor
     *
     * @return
     */
    private void criarGraficoFaturamentoPorProdutor() {
        barModelFaturamentoPorPordutor = iniciarGraficoFaturamentoPorProdutor();

        if (barModelFaturamentoPorPordutor != null) {
            graficoFatProdutor = true;
            barModelFaturamentoPorPordutor.setTitle("Corretor: " + produtor);
            barModelFaturamentoPorPordutor.setAnimate(true);
            barModelFaturamentoPorPordutor.setLegendPosition("ne");
            barModelFaturamentoPorPordutor.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
            barModelFaturamentoPorPordutor.setShowPointLabels(true);

            Axis xAxis = barModelFaturamentoPorPordutor.getAxis(AxisType.X);
            xAxis.setLabel("Mês");

            Axis yAxis = barModelFaturamentoPorPordutor.getAxis(AxisType.Y);
            yAxis.setLabel("Valores");

            yAxis.setTickFormat("R$ %1.2f");
        }

    }

    private BarChartModel iniciarGraficoFaturamentoPorProdutor() {
        BarChartModel model = new BarChartModel();

        ChartSeries fat = new ChartSeries();
        fat.setLabel("Totais");
        try {
            System.out.println("Nome: " + produtor + " Ano: " + ano);
            List<Object[]> objs = graficoDao.getGraficoProdutorFaturamento(ano, produtor);

            for (Object[] obj : objs) {
                fat.set((String) obj[0], (Double) obj[1]);
            }

            model.addSeries(fat);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar grafico de faturamento de produtor ",
                    ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ex.printStackTrace();
        }

        return model;
    }

    /**
     * REtorno os produtores por ano
     *
     * @return
     */
    public void selecionaProdutores(String an) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> prods = new LinkedList<String>();
        try {
            prods = graficoDao.getProdutores(an);

            for (String pr : prods) {
                map.put(pr, pr);
                System.out.println("Produtor: " + pr);
            }
            data.put(an, map);

        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao retornor os produtores ",
                    ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            ex.printStackTrace();
        }
    }

    public void onAnoSelecionado() {
        if (ano != null && !ano.equals("")) {
            selecionaProdutores(ano);

            produtores = data.get(ano);
        } else {
            produtores = new HashMap<String, String>();
        }
        System.out.println("Ano: " + ano);
    }

    public boolean verificarSelecao() {
        if (ano == null && (produtor == null || produtor.equals(""))) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ", "Selecione um produtor");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return false;
        } else if (produtor == null || produtor.equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "ATENÇÃO! ", "Selecione um produtor");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return false;
        }
        return true;
    }

    public LineChartModel getLineChartModelSeguradoras() {
        return lineChartModelSeguradoras;
    }

    public void setLineChartModelSeguradoras(LineChartModel lineChartModelSeguradoras) {
        this.lineChartModelSeguradoras = lineChartModelSeguradoras;
    }

    public LineChartModel getLineChartModelCrescimento() {
        return lineChartModelCrescimento;
    }

    public void setLineChartModelCrescimento(LineChartModel lineChartModelCrescimento) {
        this.lineChartModelCrescimento = lineChartModelCrescimento;
    }

    public int getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(int indexTab) {
        this.indexTab = indexTab;
    }

    public String getAnoSeg() {
        return anoSeg;
    }

    public void setAnoSeg(String anoSeg) {
        this.anoSeg = anoSeg;
    }

    public String getAnoCres() {
        return anoCres;
    }

    public void setAnoCres(String anoCres) {
        this.anoCres = anoCres;
    }

    public BarChartModel getBarModelFaturamento() {
        return barModelFaturamento;
    }

    public void setBarModelFaturamento(BarChartModel barModelFaturamento) {
        this.barModelFaturamento = barModelFaturamento;
    }

    public String getAnoFat() {
        return anoFat;
    }

    public void setAnoFat(String anoFat) {
        this.anoFat = anoFat;
    }

    public String getAnoFatProd() {
        return anoFatProd;
    }

    public void setAnoFatProd(String anoFatProd) {
        this.anoFatProd = anoFatProd;
    }

    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    public BarChartModel getBarModelFaturamentoPorPordutor() {
        return barModelFaturamentoPorPordutor;
    }

    public void setBarModelFaturamentoPorPordutor(BarChartModel barModelFaturamentoPorPordutor) {
        this.barModelFaturamentoPorPordutor = barModelFaturamentoPorPordutor;
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

    public Map<String, String> getAnos() {
        return anos;
    }

    public void setAnos(Map<String, String> anos) {
        this.anos = anos;
    }

    public Map<String, String> getProdutores() {
        return produtores;
    }

    public void setProdutores(Map<String, String> produtores) {
        this.produtores = produtores;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public boolean isGraficoSeguradoras() {
        return graficoSeguradoras;
    }

    public void setGraficoSeguradoras(boolean graficoSeguradoras) {
        this.graficoSeguradoras = graficoSeguradoras;
    }

    public boolean isGraficoFaturamento() {
        return graficoFaturamento;
    }

    public void setGraficoFaturamento(boolean graficoFaturamento) {
        this.graficoFaturamento = graficoFaturamento;
    }

    public boolean isGraficoCrescimento() {
        return graficoCrescimento;
    }

    public void setGraficoCrescimento(boolean graficoCrescimento) {
        this.graficoCrescimento = graficoCrescimento;
    }

    public boolean isGraficoFatProdutor() {
        return graficoFatProdutor;
    }

    public void setGraficoFatProdutor(boolean graficoFatProdutor) {
        this.graficoFatProdutor = graficoFatProdutor;
    }

}
