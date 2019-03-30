package br.com.cc.apolice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class ApoliceFile extends Apolice{

	private FileReader arq;
	private String pathTxt; 
	
	public ApoliceFile(){
		
	}
	
	public void apoliceFile(String path) throws Exception {
		super.apolice(path);
		this.setPathTxt(gravarArquivo(path, getPagina()));
	}
	
	public void apoliceFilePorLinha(String path) throws Exception {
		super.apolice(path);
		this.setPathTxt(gravarArquivoPorLinha(path, getPagina()));
	}

	
	public String gravarArquivo(String path, String pagina) throws Exception{
		String arquivoTxt = path + ".tmp";
		PrintWriter writer;
		try {
			String pag = pagina.replaceAll("\n", "").replaceAll("\r", "");
			writer = new PrintWriter(new FileOutputStream(arquivoTxt));
			writer.print(pag);
	        writer.println();
	        writer.flush();
	        writer.close();
	        
		} catch (FileNotFoundException e) {
			throw new Exception("Falha ao tentar gravar dados do PDF no txt. "+e.getMessage());
		}
		
        return arquivoTxt;
	}
	
	public String gravarArquivoPorLinha(String path, String pagina) throws Exception{
		String arquivoTxt = path + ".tmp";
		PrintWriter writer;
		//System.out.println(pagina);
		try {
			//String pag = pagina.replaceAll("\n", "").replaceAll("\r", "");
			writer = new PrintWriter(new FileOutputStream(arquivoTxt));
			writer.print(pagina);
	        writer.println();
	        writer.flush();
	        writer.close();
	        
		} catch (FileNotFoundException e) {
			throw new Exception("Falha ao tentar gravar dados do PDF no txt. "+e.getMessage());
		}
		
        return arquivoTxt;
	}
	
	public String lerArquivo(String path) throws Exception {
		String arquivoTxt = path;
		
		try{
			arq = new FileReader(arquivoTxt);
	        BufferedReader lerArq = new BufferedReader(arq);
	        
	        String linha = lerArq.readLine();
	        
	        return linha;
		} catch (Exception e) {
			throw new Exception("Falha o tentar ler arquivo. "+e.getMessage());
		}	
	}
	
	public String lerArquivoPorLinha(String path) throws Exception {
		String arquivoTxt = path;
		StringBuilder sb = new StringBuilder();
		
		try{
			arq = new FileReader(arquivoTxt);
	        BufferedReader lerArq = new BufferedReader(arq);
	        
	        String linha = lerArq.readLine();
	        int index = 0;
	        while (linha != null){
	        	sb.append("; ");
	        	sb.append(index++).append(" ");
	        	sb.append(linha).append("\n");
	        	linha = lerArq.readLine();
	        }
	        
	        return sb.toString();
		} catch (Exception e) {
			throw new Exception("Falha o tentar ler arquivo. "+e.getMessage());
		}	
	}
	
	public void fechar() throws IOException{
		arq.close();
	}
	
    public void apagarArquivoTemporario(String path) {
        File f = new File(path);
        f.delete();
    }


	public String getPathTxt() {
		return pathTxt;
	}


	public void setPathTxt(String pathTxt) {
		this.pathTxt = pathTxt;
	}
	
	
}
