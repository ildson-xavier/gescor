/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import br.com.cc.entidade.Lancamentos;

/**
 *
 * @author Usuario
 */
public class PDFAzul implements Serializable {

	private static final long serialVersionUID = 1L;
	//public List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
    public Lancamentos lancamento = null;
    public Lancamentos lancamentoDt = null;

    public List<Lancamentos> lerPdfAzul(String path, String seguradora, Double percent) throws Exception {
        List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
        int numeroPaginas = 0;
        //Carregando o PDF
        PdfReader pdfReader = new PdfReader(path);

        // Numero de paginas
        numeroPaginas = pdfReader.getNumberOfPages();
        System.out.println("Numero de paginas: " + numeroPaginas);

        //Extraindo de página em página e jogando numa String
        for (int i = 1; i <= numeroPaginas; i++) {
            //s = s.concat(PdfTextExtractor.getTextFromPage(pdfReader, i) + "\n\n");
            //System.out.println("linha: " + i + " " + PdfTextExtractor.getTextFromPage(pdfReader, i));
            //if (i == 1) 
            gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path, seguradora, percent, listaLanc);
            
        }

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
        String nome;
        String vlrComissao;
        Date dataComissao;
        Double valorComissao;

        FileReader arq = new FileReader(arquivoTxt);
        BufferedReader lerArq = new BufferedReader(arq);

        // lê a primeira linha
        String linha = lerArq.readLine();

        while (linha != null) {

            linhaInicial++;
            //lancamento = new Lancamentos();

            System.out.println("[" + linhaInicial + "]Linha: " + linha);

            // pega a data
            if (linha.contains("Remessa -")) {
                lancamentoDt = new Lancamentos();
                lancamentoDt.setPeriodo(formatarDataStr(pegaData(linha)));
                //System.out.println("Periodo Ori: "+lancamentoDt.getPeriodo());
            }

            if (((linhaInicial > 6 && !linha.contains("Remessa -")
            		&& !linha.contains("RenovAzul")
            		&& !linha.contains("Campanha A")
            		&& !linha.contains("COMISSAO AC")
                    && temNumero(linha) && temLetra(linha))
                    || linha.contains("Visa"))
                    && !linha.startsWith("Total")) {
                lancamento = new Lancamentos();

                // pega o nome e parcela
                if (!linha.contains("Visa")) {                    
                    nome = pegaNome(linha);
                    if (nome != null && nome.length() > 0) {
                        //System.out.println("Nome: " + nome);
                        lancamento.setHistorico(nome);
                        lancamento.setParcela(Integer.parseInt(pegaParcela(linha)));
                    }
                } else {                    
                    nome = pegaNome(linha.substring(4));
                    if (nome != null && nome.length() > 0) {
                        //System.out.println("Nome: " + nome);
                        lancamento.setHistorico(nome);
                    }
                }
                // pega o valor da comissao
                vlrComissao = pegaVlrComissao(linha);
                if (vlrComissao != null && vlrComissao.length() > 0) {
                    //System.out.println("Comissao: " + fomatarValorStr (vlrComissao));
                    lancamento.setComissao(fomatarValorStr(vlrComissao));
                    if (heNegativo(linha)) {
                        lancamento.setComissao(lancamento.getComissao() * -1);
                    }
                } else {
                    break;
                }

                //System.out.println("Parcela: "+pegaParcela (linha));
                // copia a data
                lancamento.setPeriodo(lancamentoDt.getPeriodo());
                //System.out.println("Periodo Ori: "+lancamento.getPeriodo());

                if (lancamento != null) {
                    lancamento.setSeguradora(seguradora);
                    lancamento.setClassificacao(1);
                    lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                    if (lancamento.getComissao() < 0){
                        lancamento.setParcela(0);
                    }
                    calcularPercentualEPremioLiquido(lancamento, percent);
                    listaLanc.add(lancamento);
                }
            }

            // Proxima linha
            linha = lerArq.readLine();
        }
        arq.close();
        apagarArquivoTemporario(arquivoTxt);
    }

    public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
        lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
        lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
    }

    public String pegaParcela(String linha) {
        int count = 0;
        String parcela = "";
        for (int i = 0; i < linha.length(); i++) {
            if (!Character.isAlphabetic(linha.charAt(i))) {
                if (!Character.isSpaceChar(linha.charAt(i))) {
                    count++;
                    if (count > 20) {
                        if (linha.charAt(i) == '/') {
                            break;
                        }
                        parcela += linha.charAt(i);
                    }
                }
            }
        }

        return parcela;
    }

    public void apagarArquivoTemporario(String path) {
        File f = new File(path);
        f.delete();
    }

    public String pegaData(String linha) {
        return linha.substring(13, 24).trim();
    }

    public String pegaNome(String linha) {
        String nome = null;
        if (!linha.contains("Total Segurados Total")) {
            nome = linha.substring(0, iIndexNumero(linha));
            return nome.trim();
        } else {
            return nome.trim();
        }
    }

    public String pegaVlrComissao(String linha) {
        String vlr = null;
        if (!linha.contains("Total Segurados Total")) {
            vlr = linha.substring(iIndexInvertido(linha), linha.length());
            return vlr.trim();
        } else {
            return vlr.trim();
        }
    }

    public int iIndexNumero(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isDigit(linha.charAt(i))) {
                return i;
            }
        }
        return 0;
    }

    public boolean temNumero(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isDigit(linha.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean temLetra(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isAlphabetic(linha.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public int iIndexInvertido(String linha) {
        for (int i = linha.length() - 1; i > 0; i--) {
            if (Character.isSpaceChar(linha.charAt(i))) {
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

    public Double fomatarValorStr(String valor) throws Exception {
        if (valor == null || valor.equals("")) {
            return null;
        }
        Double valorDouble = null;
        try {
            valorDouble = Double.parseDouble(valor.replaceAll("\\.", "").replace(",", "."));
        } catch (NumberFormatException e) {
            throw new Exception("Erro ao tentar formatar o valor da comissao");
        }
        return valorDouble;
    }

    public static void main(String[] args) {
        String path = "D:\\Adriana\\AZUL\\";
        String pdf = "08.03.pdf";
        List<Lancamentos> lst = new ArrayList<Lancamentos>();
        PDFAzul pdfAzul = new PDFAzul();
        try {
            lst = pdfAzul.lerPdfAzul(path + pdf, "Azul", 8.21);

            for (Lancamentos l : lst) {
                System.out.println("");
                System.out.println("Historico: " + l.getHistorico());
                System.out.println("Periodo: " + l.getPeriodo());
                System.out.println("Comissao: " + l.getComissao());
                System.out.println("Seguradora: " + l.getSeguradora());
                System.out.println("Parcela: " + l.getParcela());
                System.out.println("Imposto: " + l.getValor2());
                System.out.println("PL: " + l.getValor3());
                System.out.println("_________________________________");
            }

        } catch (Exception ex) {
            Logger.getLogger(PDFAzul.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
