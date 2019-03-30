/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.Produtor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class PDFApoliceSegurado implements Serializable {

	private List<Produtor> listProdutor = new ArrayList<Produtor>();
	private ProdutorDao dao = new ProdutorDao();
	private Produtor produtor;

	public void lerApoliceSegurado(String path) throws Exception {
		int numeroPaginas;
		String s = "";
		// Carregando o PDF
		PdfReader pdfReader = new PdfReader(path);

		// Numero de paginas
		numeroPaginas = pdfReader.getNumberOfPages();
		System.out.println("Numero de paginas: " + numeroPaginas);

		// Extraindo de página em página e jogando numa String
		for (int i = 1; i <= numeroPaginas; i++) {
			// s = s.concat(PdfTextExtractor.getTextFromPage(pdfReader, i) +
			// "\n\n");
			// System.out.println("linha: " + i + " " +
			// PdfTextExtractor.getTextFromPage(pdfReader, i));
			gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path);

		}
		// System.out.println(s);
	}

	public void gravarPaginaTxt(String pagina, String path) throws IOException {
		String arquivoTxt = path + ".txt";
		// System.out.println("Arq: "+arquivoTxt);
		PrintWriter writer = new PrintWriter(new FileOutputStream(arquivoTxt));

		writer.print(pagina);
		writer.println();
		writer.flush();
		writer.close();

		lerPaginaTxt(arquivoTxt);
	}

	public void lerPaginaTxt(String path) throws FileNotFoundException, IOException {
		String arquivoTxt = path;
		String nome = "";
		String apolice = "";
		int linhaInicial = 0;

		FileReader arq = new FileReader(arquivoTxt);
		BufferedReader lerArq = new BufferedReader(arq);

		// lê a primeira linha
		String linha = lerArq.readLine();

		while (linha != null) {

			linhaInicial++;

			if (ehLinhaValida(linha)) {
				//System.out.println("Linha [" + linhaInicial + "] " + linha);
				//System.out.println("Apolice: "+pegaNumApolice(linha));
				//System.out.println("Segurado: "+pegaNomeSegurado(linha));
				apolice = pegaNumApolice(linha);
				nome = pegaNomeSegurado(linha);
				
				try {
					listProdutor = dao.getProdutoresPorSegurado(nome.substring(0, 12));
					if (!listProdutor.isEmpty()) {
						for (Produtor p : listProdutor) {
							//System.out.println("Nome: "+p.getSegurado());
							p.setApolice(apolice);
							// Atualizar
							dao.atualizarProdutor(p);
						}
					} else {
						produtor = new Produtor();
						produtor.setSegurado(nome);
						produtor.setApolice(apolice);
						// Inserir
						dao.adicionarProdutor(produtor);
						//System.out.println("Nao achou");
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			linha = lerArq.readLine();
		}
		arq.close();
		 pagarArquivoTemporario(arquivoTxt);
	}

	public boolean ehLinhaValida(String linha) {
		String digitos = "";
		if (linha.length() > 18) {
			String primeiros = linha.substring(0, 6);

			for (int i = 0; i < primeiros.length(); i++) {
				if (Character.isAlphabetic(primeiros.charAt(i)) ||
						primeiros.charAt(i) == ',') {
					return false;
				} else {
					digitos += primeiros.charAt(i);
				}
			}
		}
		return (!digitos.equals(""));
	}
	
	public String pegaNumApolice(String linha){
		String alfa = "";
		String apolice = linha.substring(0, iIndexAlfa(linha));
		for (int i = 0; i < apolice.length(); i++){
			if (Character.isDigit(apolice.charAt(i))){
				alfa += apolice.charAt(i);
			}
		}
		return alfa;
	}
	
	public String pegaNomeSegurado(String linha){
		String nova = linha.substring(iIndexAlfa(linha), iIndexAlfa(linha) + 17);
		return nova;
	}
	
	public int iIndexAlfa(String linha) {
		for (int i = 0; i < linha.length(); i++) {
			if (Character.isAlphabetic(linha.charAt(i))) {
				return i;
			}
		}
		return 0;
	}

	public void pagarArquivoTemporario(String path) {
		File f = new File(path);
		f.delete();
	}

	public static void main(String[] args) {
		String path = "D:\\Adriana\\Apolice\\";
		String pdf = "apolice.pdf";
		int i = 0;
		PDFApoliceSegurado p = new PDFApoliceSegurado();
		try {
			p.lerApoliceSegurado(path + pdf);
		} catch (Exception ex) {
			Logger.getLogger(PDFApoliceSegurado.class.getName()).log(Level.SEVERE, null, ex);
		}
		// p.lerPaginaTxt();
		for (Produtor pr : p.listProdutor) {
			System.out.println("Index: " + i++ + " Segurado: " + pr.getSegurado() + " Produtor: " + pr.getProdutor());
		}
	}

	public List<Produtor> getListProdutor() {
		return listProdutor;
	}

	public void setListProdutor(List<Produtor> listProdutor) {
		this.listProdutor = listProdutor;
	}

}
