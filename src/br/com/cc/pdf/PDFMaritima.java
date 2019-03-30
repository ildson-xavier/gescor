/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

import br.com.cc.entidade.Lancamentos;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class PDFMaritima implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// public List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
	public Lancamentos lancamento = null;
	public Lancamentos lancamentoDt = null;

	public List<Lancamentos> lerPdfMaritima(String path, String seguradora, Double percent) throws Exception {
		List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
		int numeroPaginas = 0;

		// Para reduzir o uso da memoria
		RandomAccessSourceFactory f = new RandomAccessSourceFactory();
		RandomAccessSource randomAccessSource = f.createBestSource(path);

		// Carregando o PDF
		PdfReader pdfReader = new PdfReader(new RandomAccessFileOrArray(randomAccessSource), null);

		// Numero de paginas
		numeroPaginas = pdfReader.getNumberOfPages();
		System.out.println("Numero de paginas: " + numeroPaginas);

		// Extraindo de p√°gina em p√°gina e jogando numa String
		for (int i = 1; i <= numeroPaginas; i++) {
			// Ignora a primeira pagina
			if (i > 0) {
				gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path, seguradora, percent, listaLanc);
			}
		}

		randomAccessSource.close();
		pdfReader.close();
		return listaLanc;
	}

	public void gravarPaginaTxt(String pagina, String path, String seguradora, Double percent, List<Lancamentos> lancs)
			throws Exception {
		String arquivoTxt = path + ".tmp";
		PrintWriter writer = new PrintWriter(new FileOutputStream(arquivoTxt));

		writer.print(pagina);
		writer.println();
		writer.flush();
		writer.close();

		lerPaginaTxt(arquivoTxt, seguradora, percent, lancs);
	}

	public void lerPaginaTxt(String path, String seguradora, Double percent, List<Lancamentos> listaLanc)
			throws FileNotFoundException, Exception {
		String arquivoTxt = path;
		int linhaInicial = 0;
		boolean subtotal = false;
		
		String nomeDri = "ADRIANA†CRISTINA†MACHADO†VITOR";
		String corretoraDri = "MACHADO†E†PAIARO†COR†SEG";
		
		String corretoraDri2 = "MACHADO E PAIARO COR SEG LTDA";

		FileReader arq = new FileReader(arquivoTxt);
		BufferedReader lerArq = new BufferedReader(arq);

		// l√™ a primeira linha
		String linha = lerArq.readLine();
		
		//System.out.println("[" + linhaInicial + "] Linha: " + linha);

		while (linha != null) {

			System.out.println("[" + linhaInicial + "] Linha: " + linha);

			// pega a data
			if (linhaInicial >= 12 && ((linha.contains(nomeDri))
					|| (linha.contains(corretoraDri)) || (linha.contains(corretoraDri2)))) {
				if ((linha.contains(nomeDri))
						|| (linha.contains(corretoraDri)) || (linha.contains(corretoraDri2))) {
					lancamentoDt = new Lancamentos();
					lancamentoDt.setPeriodo(formatarDataStr(pegaData(linha)));
					System.out.println("Periodo: " + formatarDataStr(pegaData(linha)));
				}
			}

			// Pega nome e valor
			if (linhaInicial > 28 && !subtotal && lancamentoDt != null) {
				if (Character.isDigit(linha.charAt(0))) {
					lancamento = new Lancamentos();
					lancamento.setDataIncluisao(Calendar.getInstance().getTime());
					lancamento.setHistorico(pegaNome(linha.substring(9)));
					System.out.println("Nome: " + lancamento.getHistorico());
					lancamento.setComissao(fomatarValorStr(pegaVlrComissao(linha)));
					System.out.println("Valor: " + lancamento.getComissao());
					lancamento.setParcela(Integer.valueOf(pegaParcela(linha)));
					lancamento.setSeguradora(seguradora);
					lancamento.setClassificacao(1);
					lancamento.setPeriodo(lancamentoDt.getPeriodo());
					calcularPercentualEPremioLiquido(lancamento, percent);
					listaLanc.add(lancamento);
				}
				if ((linha.contains("Sub†Totais")) || linha.contains("Sub Totais")) {
					subtotal = true;
				}
			}

			// Proxima linha
			linha = lerArq.readLine();
			linhaInicial++;
		}
		arq.close();
		apagarArquivoTemporario(arquivoTxt);
	}

	public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
		lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
		lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
	}

	public void apagarArquivoTemporario(String path) {
		File f = new File(path);
		f.delete();
	}

	public String pegaData(String linha) {
		return linha.substring(33, 43).trim();
	}

	public String pegaParcela(String linha) {
		int inicial = linha.indexOf("%");
		return linha.substring(inicial + 1, inicial + 4).trim();
	}

	public String pegaNome(String linha) {
		boolean caracter = false;
		String nome = "";
		for (int i = 0; i < linha.length(); i++) {
			if (Character.isAlphabetic(linha.charAt(i)) && !caracter) {
				caracter = true;
			}
			if (caracter && !(Character.isDigit(linha.charAt(i))) && (linha.charAt(i) != ',')) {
				nome += linha.charAt(i);
			}
		}
		return nome.trim();
	}

	public String pegaVlrComissao(String linha) {
		String vlr = null;
		vlr = linha.substring(iIndexInvertido(linha), linha.length() - 2);
		return vlr.trim();
	}

	public int iIndexNumero(String linha) {
		for (int i = 4; i < linha.length(); i++) {
			if (Character.isDigit(linha.charAt(i))) {
				return i;
			}
		}
		return 0;
	}

	public int iIndexInvertido(String linha) {
		int espaco = 0;
		for (int i = linha.length() - 1; i > 0; i--) {
			if (Character.isSpaceChar(linha.charAt(i))) {
				espaco++;
			}
			if (espaco == 2) {
				return i;
			}
		}
		return 0;
	}

	public boolean heNegativo(String linha) {
		for (int i = linha.length() - 1; i > 0; i--) {
			if (Character.isSpaceChar(linha.charAt(i))) {
				if (linha.charAt(i - 1) == '-') {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean heNegativo2(String linha) {
		for (int i = 0; i < linha.length() - 1; i++) {
			if (linha.charAt(i) == '-') {
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

	public Double fomatarValorStr(String valorComissao) throws Exception {
		String valor = valorComissao;
		Double valorDouble = null;

		if (valor == null || valor.equals("")) {
			return null;
		}

		try {
			valorDouble = retornaValorFormatado(valor);
		} catch (NumberFormatException e) {
			throw new Exception("Erro ao tentar formatar o valor da comissao");
		}
		return valorDouble;
	} 
	//valor.replaceAll("[^\\d-]+", "")
	private Double retornaValorFormatado(String valorComissao) throws NumberFormatException{
		String valor = valorComissao.trim();
		Double retorno = null;
		
		valor = tratarPotergaistem(valor);
		
		valor = valor.replaceAll("\\.", "").replace(",", ".");
		retorno = Double.parseDouble(valor);
		
		return retorno;
		
	}
	
    public String tratarPotergaistem(String valor){
    	char [] post = {'1', '2', '3', '4','5','6','7','8','9','0','-',','};
    	String novoValor = "";
    	
    	for (int i = 0; i < valor.length(); i++){
    		for (int j = 0; j < post.length; j++){
    			if (valor.charAt(i) == post[j]){
    				novoValor += post[j];
    				break;
    			} else {
    				if (i == 0 && j == 11){
    					novoValor = "-";
    					break;
    				}
    			}
    		}
    	}
    	return novoValor;
    }
	
	public static void main(String[] args) {
		String path = "D:\\Adriana\\Sompo\\";
		String pdf = "22.02.pdf";
		int index = 0;
		List<Lancamentos> lst = new ArrayList<Lancamentos>();
		PDFMaritima pdfAzul = new PDFMaritima();
		try {
			lst = pdfAzul.lerPdfMaritima(path + pdf, "Sompo", 8.21);

			for (Lancamentos l : lst) {
				System.out.println("Index: " + index++);
				System.out.println("Historico: " + l.getHistorico());
				System.out.println("Periodo: " + l.getPeriodo());
				System.out.println("Comissao: " + l.getComissao());
				System.out.println("Seguradora: " + l.getSeguradora());
				System.out.println("Imposto: " + l.getValor2());
				System.out.println("PL: " + l.getValor3());
				System.out.println("_________________________________");
			}

		} catch (Exception ex) {
			Logger.getLogger(PDFMaritima.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
