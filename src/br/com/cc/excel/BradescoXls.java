package br.com.cc.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.entidade.Lancamentos;

/**
 *
 * @author Usuario
 */
public class BradescoXls implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Lancamentos> importarPlanilha(String arquivoXls, String seguradora, Double percent) throws FileNotFoundException, IOException, Exception {

        List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();
        Lancamentos lancamento;
        String dataPagamento = "";
        String comissao = "";
        String segurado = "";

        try {
            // Abre o arquivo [planilha]
            InputStream inputStream = new FileInputStream(new File(arquivoXls));

            // Carrega a planilha
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);

            // Seleciona a primeira aba
            HSSFSheet aba = wb.getSheetAt(0);

            // Equanto tiver linha pra ler, executa o codigo
            Iterator<Row> linhas = aba.iterator();
            while (linhas.hasNext()) {

                // Carrega a linha da variavel
                Row linha = linhas.next();

                // Começa a partir da linha 3
                if (linha.getRowNum() > 1) {

                    if (linha.getCell(0).getCellType() == CellType.BLANK) {
                        System.out.println("Fim de de linha");
                        break;
                    }

                    // Inicia o objeto
                    lancamento = new Lancamentos();

                    // Cada celula
                    Iterator<Cell> celula = linha.cellIterator();

                    // Itera cada celula da linha
                    while (celula.hasNext()) {
                        Cell cell = celula.next();

                        switch (cell.getColumnIndex()) {
                            // A data
                            case 5:
                                if (cell.getCellType() == CellType.STRING) {
                                    dataPagamento = cell.getStringCellValue();
                                    lancamento.setPeriodo(pegarData(dataPagamento));
                                }
                                break;
                            case 10:
                                if (cell.getCellType() == CellType.STRING) {
                                    lancamento.setParcela(Integer.parseInt(cell.getStringCellValue()));
                                }
                            // Valor
                            case 14:
                                if (cell.getCellType() == CellType.STRING) {
                                    comissao = cell.getStringCellValue();
                                    lancamento.setComissao(fomatarValorStr(comissao));
                                    calcularPercentualEPremioLiquido(lancamento, percent);
                                    //System.out.println("Comissao: " + lancamento.getComissao());
                                }
                                break;

                            // Nome do segurado
                            case 17:
                                if (cell.getCellType() == CellType.STRING) {
                                    segurado = cell.getStringCellValue().trim();
                                    lancamento.setHistorico(segurado);
                                    //System.out.println("Segurado: " + segurado);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    if (!lancamento.getHistorico().equals("")) {
                        lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                        lancamento.setSeguradora(seguradora);
                        lancamento.setClassificacao(1);
                        lancamentos.add(lancamento);
                    }
                }
            }
            return lancamentos;

        } catch (NullPointerException n) {
            throw new Exception("Erro durante a leitura das celulas da planilha");
        } catch (IllegalStateException i) {
            throw new Exception("Nao eh possivel converter o valor da celula "+i.getMessage());
        }
    }

    public Double fomatarValorStr(String valor) throws Exception {
        if (valor == null || valor.equals("")) {
            return null;
        }
        Double valorDouble = null;
        try {
            valorDouble = Double.parseDouble(valor.replaceAll("\\.", "").replace(",", "."));
            valorDouble = valorDouble / 100;
        } catch (NumberFormatException e) {
            throw new Exception("Erro ao tentar formatar o valor da comissao");
        }
        return valorDouble;
    }

    public Date pegarData(String data) throws Exception {
        String dataAux = "",
                ano = "",
                mes = "",
                dia = "";
        ano = data.substring(0, 4);
        mes = data.substring(4, 6);
        dia = data.substring(6, 8);

        dataAux = dataAux.concat(dia).concat("/").concat(mes).concat("/").concat(ano);

        //System.out.println("Data Formadata: "+dataAux);
        return formatarDataStr(dataAux);
    }

    public Date formatarDataStr(String data) throws Exception {
        if (data == null || data.equals("")) {
            return null;
        }
        Date dtComissao = null;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dtComissao = dateFormat.parse(data);
        } catch (ParseException ex) {
            throw new Exception("Erro ao formatar a data: " + data);
        }
        return dtComissao;
    }

    public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
        lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
        lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
    }

    public static void main(String[] args) {

        BradescoXls p = new BradescoXls();
        String caminho = "X:\\Adriana\\Bradesco\\agosto 2018.xls";
        List<Lancamentos> lancs = new ArrayList<Lancamentos>();
        String seg = "Bradesco";
        Double percent = 8.21;

        try {
            lancs = p.importarPlanilha(caminho, seg, percent);
            for (Lancamentos l : lancs) {
                System.out.println("Historico: " + l.getHistorico());
                System.out.println("Produtor: " + l.getProdutor());
                System.out.println("Comisao: " + l.getComissao());
                System.out.println("Seguradora: " + l.getSeguradora());
                System.out.println("Periodo: " + l.getPeriodo());
                System.out.println("Parcela: " + l.getParcela());
                System.out.println("Imposto: " + l.getValor2());
                System.out.println("Liquido: " + l.getValor3());

                System.out.println("_____________________________ ");
            }

        } catch (Exception ex) {
            Logger.getLogger(BradescoXls.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
