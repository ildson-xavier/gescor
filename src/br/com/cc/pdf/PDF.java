package br.com.cc.pdf;

import java.util.List;

public interface PDF<T> {

	String lerPdf(String path);
	
	String criarArquivoTxtTemporario(String path);
	
	String gravarTxt(String path);
	
	String lerTxt(String path);
	
	void apagarArquivoTxtTemporario(String path);
	
	List<T> retornarDadosPdf(String path);
}
