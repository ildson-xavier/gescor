package br.com.cc.pdf;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class PDFApolice {

	public String lerPdfApolice(String path) throws Exception {

	    PDDocument pdfDocument = null;
	    try {
	      pdfDocument = PDDocument.load(new File(path));
	      PDFTextStripper stripper = new PDFTextStripper();
	      String texto = stripper.getText(pdfDocument);
	      return texto;
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    } finally {
	      if (pdfDocument != null) try {
	        pdfDocument.close();
	      } catch (IOException e) {
	        throw new RuntimeException(e);
	      }
	    }

	}

	public static void main(String[] args) throws Exception {
		String path = "X:\\Adriana\\Apolice\\";
		String pdf = "mapfre.pdf";
		//String pdf = "apolice.pdf";

		PDFApolice apolice = new PDFApolice();
		System.out.println(apolice.lerPdfApolice(path + pdf));
	}
}
