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
public class TokioMarineXls implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Lancamentos> importarPlanilha(String arquivoXls, String seguradora, Double percent, Date dataRemessa)
			throws FileNotFoundException, IOException, Exception {

		List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();
		Lancamentos lancamento;
		String comissao = "";
		String segurado = "";
		String parcela = "";
		boolean negocio = false;

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

				if (linha.getCell(1).getStringCellValue().equals("Negócio")) {
					System.out.println("Negocio");
					negocio = true;
				}

				// ComeÃ§a a partir da linha 1
				if (linha.getRowNum() > 0) {

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

						if (!negocio) {
							switch (cell.getColumnIndex()) {
							// A Segurado
							case 0:
								if (cell.getCellType() == CellType.STRING) {
									segurado = cell.getStringCellValue();
									lancamento.setHistorico(segurado);
								}
								break;
							// Parcela
							case 5:
								if (cell.getCellType() == CellType.STRING) {
									parcela = cell.getStringCellValue();
									// System.out.println("Parcela "+parcela);
									parcela = parcela.split("/")[0];
									// System.out.println("Parcela "+parcela);
									lancamento.setParcela(Integer.parseInt(parcela));
								}
								break;
							// Valor
							case 8:
								if (cell.getCellType() == CellType.STRING) {
									comissao = cell.getStringCellValue();
									lancamento.setComissao(fomatarValorStr(comissao));
									calcularPercentualEPremioLiquido(lancamento, percent);
									// System.out.println("Comissao: " +
									// lancamento.getComissao());
								}
								break;

							default:
								break;
							}
						} else {
							switch (cell.getColumnIndex()) {
							// A Segurado
							case 0:
								if (cell.getCellType() == CellType.STRING) {
									segurado = cell.getStringCellValue();
									lancamento.setHistorico(segurado);
								}
								break;
							// Parcela
							case 6:
								if (cell.getCellType() == CellType.STRING) {
									parcela = cell.getStringCellValue();
									// System.out.println("Parcela "+parcela);
									parcela = parcela.split("/")[0];
									// System.out.println("Parcela "+parcela);
									lancamento.setParcela(Integer.parseInt(parcela));
								}
								break;
							// Valor
							case 9:
								if (cell.getCellType() == CellType.STRING) {
									comissao = cell.getStringCellValue();
									lancamento.setComissao(fomatarValorStr(comissao));
									calcularPercentualEPremioLiquido(lancamento, percent);
									// System.out.println("Comissao: " +
									// lancamento.getComissao());
								}
								break;

							default:
								break;
							}
						}

					}

					if (lancamento.getHistorico().contains("I.S.S")) {
						lancamento.setComissao(fomatarValorStr(linha.getCell(1).getStringCellValue()));
						if (lancamento.getComissao() == 0.0) {
							lancamento.setHistorico("");
						} else {
							lancamento.setComissao(lancamento.getComissao());
							calcularPercentualEPremioLiquido(lancamento, percent);
						}
					}

					if (lancamento.getHistorico().contains("I.R")) {
						lancamento.setComissao(fomatarValorStr(linha.getCell(1).getStringCellValue()));
						if (lancamento.getComissao() == 0.0) {
							lancamento.setHistorico("");
						} else {
							lancamento.setComissao(lancamento.getComissao());
							calcularPercentualEPremioLiquido(lancamento, percent);
						}
					}
					
					if (lancamento.getHistorico().contains("Ajustes sem")) {
						lancamento.setComissao(fomatarValorStr(linha.getCell(1).getStringCellValue()));
						if (lancamento.getComissao() == 0.0) {
							lancamento.setHistorico("");
						} else {
							lancamento.setComissao(lancamento.getComissao());
							calcularPercentualEPremioLiquido(lancamento, percent);
						}
					}

					if (lancamento.getHistorico().contains("I.N.S.S")) {
						lancamento.setComissao(fomatarValorStr(linha.getCell(1).getStringCellValue()));
						if (lancamento.getComissao() == 0.0) {
							lancamento.setHistorico("");
						} else {
							lancamento.setComissao(lancamento.getComissao());
							calcularPercentualEPremioLiquido(lancamento, percent);
						}
					}

					if (!lancamento.getHistorico().equals("") && lancamento.getHistorico() != null
							&& !lancamento.getHistorico().contains("Total")) {

						lancamento.setDataIncluisao(Calendar.getInstance().getTime());
						lancamento.setPeriodo(dataRemessa);
						lancamento.setSeguradora(seguradora);
						lancamento.setClassificacao(1);
						lancamentos.add(lancamento);

					}
				}
			}
			return lancamentos;

		} catch (NullPointerException n) {
			throw new Exception("Erro durante a leitura das celulas da planilha " + n.getMessage());
		} catch (IllegalStateException i) {
			throw new Exception("Nao eh possivel converter o valor da celula " + i.getMessage());
		}
	}

	public Double fomatarValorStr(String valor) throws Exception {
		if (valor == null || valor.equals("")) {
			return null;
		}
		Double valorDouble = null;
		try {
			valorDouble = Double.parseDouble(valor.replaceAll("\\.", "").replace(",", "."));
			// valorDouble = valorDouble / 100;
		} catch (NumberFormatException e) {
			throw new Exception("Erro ao tentar formatar o valor da comissao");
		}
		return valorDouble;
	}

	public Date pegarData(String data) throws Exception {
		String dataAux = "", ano = "", mes = "", dia = "";
		ano = data.substring(0, 4);
		mes = data.substring(4, 6);
		dia = data.substring(6, 8);

		dataAux = dataAux.concat(dia).concat("/").concat(mes).concat("/").concat(ano);

		// System.out.println("Data Formadata: "+dataAux);
		return formatarDataStr(dataAux);
	}

	public String addAno(String data){
		String ano = "";
		String dia = "";
		String mes = "";
		if (data.endsWith("/")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			ano = String.valueOf(cal.get(Calendar.YEAR));
			dia = data.substring(0, 2);
			mes = data.substring(3, 5);
		} else {
			dia = data.substring(0, 2);
			mes = data.substring(3, 5);
			ano = data.substring(6);
			
			if (ano.length() == 2){
				String _ano = "20"+ano;
				ano = _ano;
			}
		}
		return dia+"/"+mes+"/"+ano;
	}
	
	public Date pegarDataDaNomenclatura(String arquivo) throws Exception {
		String data = "";
		int index = 0;
		for (int i = 0; i < arquivo.length(); i++) {
			if (Character.isDigit(arquivo.charAt(i))) {
				index++;
				data += "" + (arquivo.charAt(i));
				if (index == 2 || index == 4) {
					data += "" + '/';
				}
			}
		}
		data = addAno(data);
		if (index > 8 || !validaData(data)) {
			throw new Exception("Erro na formatação da data [" + data + "].");
		}
		return formatarDataStr(data);
	}
	
	public boolean validaData(String data) throws Exception {
		boolean toReturn = true;

		if (data.length() == 10) {

			if (!diaOk(data.substring(0, 2))) {
				return false;
			}

			if (!mesOk(data.substring(3, 5))) {
				return false;
			}

			if (!anoOk(data.substring(6, 10))) {
				return false;
			}
		} else {
			return false;
		}
		return toReturn;
	}

	public boolean diaOk(String data) throws Exception {
		int dia;
		if (!data.equals("")) {
			dia = Integer.parseInt(data);
			if (dia > 0 && dia <= 31) {
				return true;
			}
		}
		return false;
	}

	public boolean mesOk(String data) throws Exception {
		int mes;
		if (!data.equals("")) {
			mes = Integer.parseInt(data);
			if (mes > 0 && mes <= 12) {
				return true;
			}
		}
		return false;
	}

	public boolean anoOk(String data) throws Exception {
		int ano;
		if (!data.equals("")) {
			ano = Integer.parseInt(data);
			if (ano != 0 && ano > 2010 && ano <= 2030) {
				return true;
			}
		}
		return false;
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

		TokioMarineXls p = new TokioMarineXls();
		String caminho = "D:\\Adriana\\Tokio\\14.12.2015.xls";
		List<Lancamentos> lancs = new ArrayList<Lancamentos>();
		String seg = "TokioMarine";
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
			Logger.getLogger(TokioMarineXls.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}
