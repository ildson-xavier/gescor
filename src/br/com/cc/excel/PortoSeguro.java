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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.entidade.Lancamentos;

/**
 *
 * @author Usuario
 */
public class PortoSeguro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Lancamentos> importarPlanilha(String arquivoXls, String seguradora, Double percent)
			throws FileNotFoundException, IOException, Exception {

		List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();
		Lancamentos lancamento;
		Date dataPagamento = null;

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

				// Pega a data
				if (linha.getRowNum() == 1) {
					dataPagamento = linha.getCell(9).getDateCellValue();
					// System.out.println("Data: "+dataPagamento);
				}

				// Comeca a partir da linha 7
				if (linha.getRowNum() > 6) {

					// Sai do loop quando chegar no final do historico
					if (linha.getCell(0).getCellType() == CellType.STRING) {
						// System.out.println("linha.getCell(0): " +
						// linha.getCell(0).getStringCellValue());
						if ((linha.getCell(0).getStringCellValue().contains("Total Susep Favorecida"))
								|| linha.getCell(0).getStringCellValue().contains("Total Débito/Crédito ")) {
							break;
						}
					}

					// System.out.println("Linha:
					// "+linha.getCell(0).getStringCellValue());

					lancamento = new Lancamentos();

					// Se tiver a linha de 'PERFIL', pega e sai
					if (linha.getCell(0).getCellType() == CellType.STRING) {
						if ((linha.getCell(0).getStringCellValue().contains("PERFIL AP"))) {
							lancamento.setHistorico(linha.getCell(0).getStringCellValue());
							lancamento.setComissao(linha.getCell(11).getNumericCellValue());
							lancamento.setParcela(0);
						}
					}

					if (!linha.getCell(0).getStringCellValue().contains("PERFIL AP")) {

						// A partir do mês 9, a Porto adicionou a coluna "Marca"
						// System.out.println("Numero de colunas: " +
						// linha.getPhysicalNumberOfCells());

						// Itera cada celula da linha [le por coluna]
						Iterator<Cell> celula = linha.cellIterator();
						while (celula.hasNext()) {

							// Carrega a linha da variavel
							Cell cell = celula.next();

							// Verifica o conteudo de cada coluna
							switch (cell.getColumnIndex()) {

							// Coluna A HistÃ³rico
							case 0:
								if (cell.getCellType() == CellType.STRING) {
									System.out.println("Coluna A: " + cell.getStringCellValue());
									lancamento.setHistorico(cell.getStringCellValue());
								}
								break;

							// Coluna F Parcela
							case 5:
								if (cell.getCellType() == CellType.NUMERIC) {
									System.out.println("Coluna F: " + (int) cell.getNumericCellValue());

									try {
										lancamento.setParcela((int) cell.getNumericCellValue());
									} catch (NullPointerException e) {
										lancamento.setParcela(0);
									}
								}
								break;

							// Coluna G Parcela
							case 6:
								if (cell.getCellType() == CellType.NUMERIC) {
									System.out.println("Coluna G: " + (int) cell.getNumericCellValue());
									try{
										if (lancamento.getParcela() == 0)
											lancamento.setParcela((int) cell.getNumericCellValue());
									}catch (NullPointerException e){
										lancamento.setParcela((int) cell.getNumericCellValue());
									}
									
								}
								break;

							// Coluna L Comisao ou Taxa
							case 11:
								if (cell.getCellType() == CellType.NUMERIC) {
									// System.out.println("Coluna N: " +
									// cell.getNumericCellValue());
									lancamento.setComissao(cell.getNumericCellValue());
								}
								break;

							// Coluna L Comissao ou tipo
							case 12:
								if (cell.getCellType() == CellType.NUMERIC) {
									System.out.println("Coluna L: " + cell.getNumericCellValue());
									lancamento.setComissao(cell.getNumericCellValue());
								}
								break;

							default:
								// System.out.println("Default: " );
								break;
							}
						}
					}

					// Adiciona lista, se nao for null
					if (lancamento.getHistorico() != null) {
						lancamento.setDataIncluisao(Calendar.getInstance().getTime());
						lancamento.setSeguradora(seguradora);
						lancamento.setPeriodo(dataPagamento);
						lancamento.setClassificacao(1);
						if (lancamento.getComissao() < 0) {
							lancamento.setParcela(0);
						}
						calcularPercentualEPremioLiquido(lancamento, percent);
						lancamentos.add(lancamento);
					}
				}
			}
			return lancamentos;
		} catch (NullPointerException n) {
			throw new Exception("Erro durante a leitura das celulas da planilha. " + n.getMessage());
		}
	}

	public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
		lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
		lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
	}

	public static void main(String[] args) {

		PortoSeguro p = new PortoSeguro();
		String caminho = "D:\\Adriana\\Porto\\30.09.xls";
		List<Lancamentos> lancs = new ArrayList<Lancamentos>();
		String seg = "PortoSeguro";
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
			Logger.getLogger(PortoSeguro.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}
