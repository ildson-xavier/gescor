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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Produtor;

/**
 *
 * @author Usuario
 */
public class PortoSeguroLegado implements Serializable{

	ProdutorDao dao = new ProdutorDao();
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Lancamentos> importarPlanilha(String arquivoXls, String seguradora, Double percent) throws FileNotFoundException, IOException, Exception {

        List<Lancamentos> lancamentos = new ArrayList<Lancamentos>();        
        Lancamentos lancamento;

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

                // Comeca a partir da linha 1
                if (linha.getRowNum() > 0) {

                    // Sai do loop quando chegar no final do historico
                    if (linha.getCell(0).getCellType() == CellType.STRING) {
                        //System.out.println("linha.getCell(0): " + linha.getCell(0).getStringCellValue());
                        if (linha.getCell(0).getStringCellValue().contains("Total Susep Favorecida")) {
                            break;
                        }
                    }

                    lancamento = new Lancamentos();

                    // Itera cada celula da linha [le por coluna]
                    Iterator<Cell> celula = linha.cellIterator();
                    while (celula.hasNext()) {

                        //Carrega a linha da variavel
                        Cell cell = celula.next();

                        // Verifica o conteudo de cada coluna
                        switch (cell.getColumnIndex()) {

                            // Coluna F: Data do movimento
                            case 5:
                                if (cell.getCellType() != CellType.BLANK) {
                                    System.out.println("Data: " + cell.getDateCellValue());
                                    lancamento.setPeriodo(cell.getDateCellValue());
                                }
                                break;
                            // Coluna N: Apolice
                           case 13:                        	   
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    System.out.println("Apolice: " + cell.getNumericCellValue());
                                    lancamento.setApolice((int) cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                	lancamento.setApolice(0);
                                	lancamento.setHistorico(linha.getCell(21).getStringCellValue());
                                } else {
                                	lancamento.setApolice(-1);
                                }
                                break;

                            // Coluna: R Parcela
                            case 17:
                                if (cell.getCellType() != CellType.BLANK) {
                                	if (cell.getCellType() == CellType.NUMERIC) {
                                		lancamento.setParcela((int) cell.getNumericCellValue());
                                	} else {
                                		if (lancamento.getApolice() > 0){
                                			lancamento.setParcela(1);
                                		}else {
                                			lancamento.setParcela(0);	
                                		}                                		
                                	}
                                } else {
                                	lancamento.setParcela(-1);
                                }
                                break;

                            // Coluna: U ComisÃ£o
                            case 20:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    System.out.println("Comissao: " + cell.getNumericCellValue());
                                    lancamento.setComissao(cell.getNumericCellValue());
                                }
                                break;
                            default:
                                //System.out.println("Default: " );
                                break;
                        }
                    }

                    // Adiciona lista, se nao for null
                    if (lancamento.getApolice() > -1) {
                        lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                        lancamento.setSeguradora(seguradora);
                        if (lancamento.getComissao() < 0) {
                            lancamento.setParcela(0);
                        }
                        calcularPercentualEPremioLiquido (lancamento, percent); 
                        if (lancamento.getApolice() > 1){
                        	obtemNomeSegurado(lancamento);
                        }                        
                        lancamentos.add(lancamento);
                    }
                }
            }
            return lancamentos;
        } catch (NullPointerException n) {
            throw new Exception("Erro durante a leitura das celulas da planilha");
        }         
    }
    
	public void obtemNomeSegurado(Lancamentos lancamento) throws Exception{
		String apolice = Integer.toString(lancamento.getApolice());
		System.out.println("Apolice: "+apolice);
		List<Produtor> produtor;
		try {
			produtor = dao.getProdutorPorCpf(apolice);
			if (!produtor.isEmpty()) {
				for (Produtor p : produtor){
					lancamento.setHistorico(p.getSegurado());
					break;
				}
			} else {
				lancamento.setHistorico(Integer.toString(lancamento.getApolice()));
			}						
		} catch (Exception ex) {
			throw new Exception("Erro ao tentar buscar o segurado pelo numero da apolice. "+ex.getMessage());
		}
	}
	
    public void calcularPercentualEPremioLiquido (Lancamentos lancamentos, Double percent) {
        lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
        lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
    }

    public static void main(String[] args) {

        PortoSeguroLegado p = new PortoSeguroLegado();
        String caminho = "D:\\Adriana\\Apolice\\X000_2014_7017NJ.xls";
        List<Lancamentos> lancs = new ArrayList<Lancamentos>();
        String seg = "Porto Seguro";
        Double percent = 8.21;
        try {
            lancs = p.importarPlanilha(caminho, seg, percent);
            for (Lancamentos l : lancs) {
            	System.out.println("_____________________________ ");
                System.out.println("Apólice: " + l.getApolice());
                System.out.println("Historico: " + l.getHistorico());
                System.out.println("Comisao: " + l.getComissao());
                System.out.println("Seguradora: " + l.getSeguradora());
                System.out.println("Periodo: " + l.getPeriodo());
                System.out.println("Parcela: " + l.getParcela());
                System.out.println("Imposto: " +l.getValor2());
                System.out.println("Liquido: "+l.getValor3());

                //System.out.println("_____________________________ ");
            }

        } catch (Exception ex) {
            Logger.getLogger(PortoSeguroLegado.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
}
