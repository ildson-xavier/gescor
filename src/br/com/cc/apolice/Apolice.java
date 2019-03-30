package br.com.cc.apolice;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public abstract class Apolice {

	private String pagina;
	
	public Apolice(){
		
	}
	public void apolice(String path) throws Exception {

		PDDocument pdfDocument = null;
		try {
			pdfDocument = PDDocument.load(new File(path));
			PDFTextStripper stripper = new PDFTextStripper();
			String texto = stripper.getText(pdfDocument);
			this.setPagina(texto);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (pdfDocument != null)
				try {
					pdfDocument.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

//	public static void main(String[] args) throws Exception {
//		String path = "X:\\Adriana\\Apolice\\";
//		String pdf = "azul-neu.pdf";
//		// String pdf = "apolice.pdf";
//
//		Apolice apolice = new Apolice(path+pdf);
//		System.out.println(apolice.lerPdfApolice(path + pdf));
//	}
}
