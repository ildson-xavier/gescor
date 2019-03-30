/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

import br.com.cc.entidade.Lancamentos;
import br.com.cc.util.Util;

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
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.util.StringUtil;

/**
 *
 * @author Usuario
 */
public class PDFSulAmerica implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date data;
    boolean isData = false;
    boolean isValor = false;
    boolean isParcela = false;
    boolean isValido = false;
    boolean isNome = false;
    String parcela = "";

    Double valor = null;
    int parc = 0;
    String historico = "";

    public List<Lancamentos> lerPdfSulAmerica(String path, String seguradora, Double percent) throws Exception {
        List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
        int numeroPaginas = 0;

        // Para reduzir o uso da memoria
        RandomAccessSourceFactory f = new RandomAccessSourceFactory();
        RandomAccessSource randomAccessSource = f.createBestSource(path);

        //Carregando o PDF
        PdfReader pdfReader = new PdfReader(new RandomAccessFileOrArray(randomAccessSource), null);

        // Numero de paginas
        numeroPaginas = pdfReader.getNumberOfPages();
        System.out.println("Numero de paginas: " + numeroPaginas);

        //Extraindo de pÃ¡gina em pÃ¡gina e jogando numa String
        for (int i = 1; i <= numeroPaginas; i++) {
            //System.out.println("PAGINA: " + i);
            //System.out.println("linha: " + i + " " + PdfTextExtractor.getTextFromPage(pdfReader, i));
            gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path, seguradora, percent, listaLanc);
        }
        randomAccessSource.close();
        pdfReader.close();
        return listaLanc;
    }

    public void gravarPaginaTxt(String pagina, String path, String seguradora,
            Double percent, List<Lancamentos> lancs) throws Exception {
        String arquivoTxt = path + ".tmp";
        PrintWriter writer = new PrintWriter(new FileOutputStream(arquivoTxt));

        writer.print(pagina);
        writer.println();
        writer.flush();
        writer.close();

        lerPaginaTxt(arquivoTxt, seguradora, percent, lancs);
    }

    public void lerPaginaTxt(String path, String seguradora, Double percent,
            List<Lancamentos> listaLanc) throws FileNotFoundException, Exception {

        String arquivoTxt = path;
        int linhaInicial = 0;


        FileReader arq = new FileReader(arquivoTxt);
        BufferedReader lerArq = new BufferedReader(arq);

        // lÃª a primeira linha
        String linha = lerArq.readLine();
        while (linha != null) {

            //System.out.println("Linha: [" + linhaInicial + "] " + linha);

            if (isData) {
                System.out.println("Data: " + pegaData(linha));
                data = formatarDataStr(pegaData(linha));
                isData = false;
            }

            if (linha.contains("do Extrato Data de") || linha.contains("do Extrato Data de")) {
                isData = true;
            }

            if (linha.contains("Nome:") && linha.contains("Apólice") && !isNome) {
                System.out.println("Nome: " + pegaNome(linha));
                historico = pegaNome(linha);
                isNome = true;
            }

            if (isValor) {
                System.out.println("Valor>>: " + fomatarValorStr(pegaValor(linha)));
                isValor = false;
                isParcela = false;
                valor = fomatarValorStr(pegaValor(linha));
            }

            if ((linha.contains("Vlr. Prêmio Liq.: Remuneração") || 
            		linha.contains("Vlr. Prêmio Liq.: Remuneração"))) {
                isValor = true;
                isParcela = false;
            } else if (linha.contains("Vlr. Prêmio Liq.: 0,00 Remuneração")) {
                System.out.println("Valor2: " + fomatarValorStr(pegaValor2(linha)));
                valor = fomatarValorStr(pegaValor2(linha));
                isValor = false;
                isParcela = false;
            }

            if (linha.contains("Seg.: Qtd. Parc. Seg.:") && !isParcela) {
                System.out.println("Parcela2: " + parcela);
                parc = 0;
                isParcela = true;
            }

            if ((linha.contains("Seg.:") || linha.contains("Remun.:")) && !isParcela) {
                parcela = pegaParcela(linha).trim();
                System.out.println("ParcelaFull:" + parcela.trim());
                
                if (!temNumero(parcela).equals("")){
                    parc = Integer.parseInt(temNumero(parcela));
                    isParcela = true;
                    System.out.println("ParcelaFull:" + parc);
                }
                
                if (Character.isDigit(parcela.charAt(0))) {
                    isParcela = true;
                    //isValido = true;                    
                    parc = Integer.parseInt(parcela);
                    System.out.println("Parcela: " + parc);
                }
                //isValido = true;
                System.out.println("-----------------------------------");
                System.out.println("Parcela: " + parc);
                System.out.println("Valor: " + valor);
                System.out.println("Histo: " + historico);
                System.out.println("-----------------------------------");
            }

            if (!historico.equals("") && valor != null && !isValor && isParcela) {
                //System.out.println("Ildson Xavier de Morases");
                Lancamentos lanc = new Lancamentos();
                lanc.setPeriodo(data);
                lanc.setHistorico(Util.criarNome(historico));
                lanc.setComissao(valor);
                if (lanc.getComissao() < 0) {
                    lanc.setParcela(0);
                } else {
                    lanc.setParcela(parc);
                }
                lanc.setDataIncluisao(Calendar.getInstance().getTime());
                lanc.setSeguradora(seguradora);
                lanc.setClassificacao(1);
                calcularPercentualEPremioLiquido(lanc, percent);
                listaLanc.add(lanc);

                historico = "";
                valor = null;
                isParcela = false;
                isNome = false;
                //isValido = false;
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

    public String pegaParcela(String linha) {
        String nova = "";
        if (linha.contains("Seg.:")) {
        	nova = linha.substring(linha.indexOf("Seg.:") + 5, linha.indexOf("Seg.:") + 7);
        } else {
        	nova = linha.substring(linha.indexOf("Remun.:") + 8, linha.indexOf("Remun.:") + 9);
        }
        
        return nova.trim();
    }

    public String pegaValor(String linha) {
    	System.out.println("Antes: "+linha);
        int iSpace = 1;
        String valor = "";
        String nova = linha.trim();
        for (int i = 0; i < nova.length(); i++) {
            if (Character.isSpaceChar(nova.charAt(i))) {
                iSpace++;
            }
            if (iSpace == 4) {
                valor += nova.charAt(i);
            }
        }
        System.out.println("Depois: "+valor.trim());
        return tratarPotergaistem(valor.trim());
    }
    
    public String tratarPotergaistem(String valor){
    	char [] post = {'1', '2', '3', '4','5','6','7','8','9','0','-','­',','};
    	String novoValor = "";
    	
    	for (int i = 0; i < valor.length(); i++){
    		for (int j = 0; j < post.length; j++){
    			if (valor.charAt(i) == post[j]){
    				if (j == 11){
    					novoValor += post[j - 1];
    				} else{
    					novoValor += post[j];
    				}
    				
    			}
    		}
    	}
    	return novoValor;
    }

    public String pegaValor2(String linha) {
        return linha.substring(linha.indexOf("0,00 Vlr. Remuneração:") + 22, linha.indexOf("Tipo Remun:"));
    }

    public Double fomatarValorStr(String valor) throws Exception {
        if (valor == null || valor.equals("")) {
            return null;
        }       
        
        Double valorDouble = null;
        
        try {
        	String valorFormatado = valor.replaceAll("\\.", "").replace(",", ".");
            valorDouble = Double.parseDouble(valorFormatado);
        } catch (NumberFormatException e) {
        	e.printStackTrace();
            throw new Exception("Erro ao tentar formatar o valor da comissao > "+e.getMessage());            
        }
        return valorDouble.doubleValue();
    }

    public String pegaNome(String linha) {
        String nova = linha.substring(6, linha.length());
        return nova.substring(0, nova.indexOf("Apólice")).trim();
    }

    public String temNumero(String linha) {
        String num = "";
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isDigit(linha.charAt(i))) {
                num += linha.charAt(i);
            }
        }
        return num;
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

    public String pegaData(String linha) {
        return linha.substring(9, 19).trim();
    }

    public void apagarArquivoTemporario(String path) {
        File f = new File(path);
        f.delete();
    }

    public static void main(String[] args) throws Exception {
        String path = "X:\\Adriana\\SulAmerica\\";
        String pdf = "sul-1005.pdf";

        int index = 0;
        List<Lancamentos> lst = new ArrayList<Lancamentos>();
        PDFSulAmerica sul = new PDFSulAmerica();;
        try {
            lst = sul.lerPdfSulAmerica(path + pdf, "SulAmerica", 8.21);

            for (Lancamentos l : lst) {
                System.out.println("_________________________________");
                System.out.println("Index: " + index++);
                System.out.println("Historico: " + l.getHistorico());
                System.out.println("Periodo: " + l.getPeriodo());
                System.out.println("Parcela: " + l.getParcela());
                System.out.println("Comissao: " + l.getComissao());
                System.out.println("Seguradora: " + l.getSeguradora());
                System.out.println("Imposto: " + l.getValor2());
                System.out.println("PL: " + l.getValor3());

            }

        } catch (Exception ex) {
            Logger.getLogger(PDFBradesco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
