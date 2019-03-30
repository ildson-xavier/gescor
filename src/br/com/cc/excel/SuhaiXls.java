/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.entidade.Lancamentos;

/**
 *
 * @author Usuario
 */
public class SuhaiXls implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Lancamentos> importarPlanilha(String arquivoXls, String seguradora, Double percent, Date dataRemessa)
			throws FileNotFoundException, IOException, Exception {

		List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();
		Lancamentos lancamento = null;
		Double comissao = 0.0;
		String segurado = "";
		Double parcela = 0.0;
		Date periodo = null;

		try {
			// Abre o arquivo [planilha]
			InputStream inputStream = new FileInputStream(new File(arquivoXls));

			// Carrega a planilha
			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(inputStream);

			// Seleciona a primeira aba
			HSSFSheet aba = wb.getSheetAt(0);

			// Equanto tiver linha pra ler, executa o codigo
			Iterator<Row> linhas = aba.iterator();
			while (linhas.hasNext()) {

				// Carrega a linha da variavel
				Row linha = linhas.next();
				lancamento = new Lancamentos();
				if (linha.getRowNum() == 1) {
					Date data = linha.getCell(6).getDateCellValue();
					periodo = data;
				}
				if (linha.getRowNum() > 3) {
					if(linha.getCell(0).getCellType() == CellType.STRING) {
						segurado = linha.getCell(0).getStringCellValue();
						if (segurado == null || segurado.equals("") || segurado.contains("vl_bruto")) {
							break;
						}
						lancamento.setHistorico(segurado);
						lancamento.setPeriodo(periodo);
						lancamento.setSeguradora("Suhai");
						lancamento.setClassificacao(1);
						lancamento.setDataIncluisao(Calendar.getInstance().getTime());
					}
					if (linha.getCell(3).getCellType() == CellType.NUMERIC) {
						parcela = linha.getCell(3).getNumericCellValue();
						lancamento.setParcela(parcela.intValue());
					}
					if (linha.getCell(4).getCellType() == CellType.NUMERIC) {
						comissao = linha.getCell(7).getNumericCellValue();
						lancamento.setComissao(comissao);
						calcularPercentualEPremioLiquido(lancamento, percent);
					}
				}
				
				lancamentos.add(lancamento);
				lancamentos.removeIf(lanc -> (lanc.getHistorico() == null || lanc.getHistorico().equals("vl_bruto")));
			}
			return lancamentos;

		} catch (NullPointerException n) {
			throw new Exception("Erro durante a leitura das celulas da planilha " + n.getMessage());
		} catch (IllegalStateException i) {
			throw new Exception("Nao eh possivel converter o valor da celula " + i.getMessage());
		}
	}
	
	public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
		lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
		lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
	}

	public static void main(String[] args) {

		SuhaiXls p = new SuhaiXls();
		String caminho = "E:\\Adriana\\Suhai\\Suhai - EXCEL.xls";
		List<Lancamentos> lancs = new ArrayList<Lancamentos>();
		String seg = "Suhai";
		Double percent = 8.21;

		try {
			lancs = p.importarPlanilha(caminho, seg, percent, Calendar.getInstance().getTime());
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
			Logger.getLogger(SuhaiXls.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}
