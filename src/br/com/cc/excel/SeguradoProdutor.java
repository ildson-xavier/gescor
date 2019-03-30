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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.entidade.Produtor;

/**
 *
 * @author Usuario
 */
public class SeguradoProdutor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Produtor> importarPlanilha(String arquivoXls)
			throws FileNotFoundException, IOException, Exception {

		List<Produtor> produtores = new ArrayList<Produtor>();
		Produtor produtor;
		String segurado = "";
		String parcela = "";

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

				// Começa a partir da linha 1
				if (linha.getRowNum() > 0) {

					if (linha.getCell(0).getCellType() == CellType.BLANK) {
						System.out.println("Fim de de linha");
						break;
					}

					// Inicia o objeto
					produtor = new Produtor();

					// Cada celula
					Iterator<Cell> celula = linha.cellIterator();

					// Itera cada celula da linha
					while (celula.hasNext()) {
						Cell cell = celula.next();

						switch (cell.getColumnIndex()) {
						// A Segurado
						case 0:
							if (cell.getCellType() == CellType.STRING) {
								segurado = cell.getStringCellValue();
								produtor.setSegurado(segurado);
							}
							break;
						// Produtor
						case 1:
							if (cell.getCellType() == CellType.STRING) {
								parcela = cell.getStringCellValue();
								produtor.setProdutor(parcela);
							}
							break;

						default:
							break;
						}
					}

					produtores.add(produtor);

				}

			}
		} catch (NullPointerException n){
			throw new Exception("Erro durante a leitura das celulas da planilha " + n.getMessage());
		} catch (IllegalStateException i){
			throw new Exception("Nao eh possivel converter o valor da celula " + i.getMessage());
		}

		return produtores;
	}

	public static void main(String[] args) {

		SeguradoProdutor p = new SeguradoProdutor();
		String caminho = "D:\\Adriana\\SegPro\\SEG2.xls";
		List<Produtor> lancs = new ArrayList<Produtor>();

		try {
			lancs = p.importarPlanilha(caminho);
			for (Produtor l : lancs) {
				System.out.println("Segurado: " + l.getSegurado());
				System.out.println("Produtor: " + l.getProdutor());

				System.out.println("_____________________________ ");
			}

		} catch (Exception ex) {
			Logger.getLogger(SeguradoProdutor.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}
