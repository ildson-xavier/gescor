/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

import br.com.cc.entidade.Lancamentos;
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
public class PDFBradescoFull implements Serializable {

    //public List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
    public Lancamentos lancamento = null;
    public Lancamentos lancamentoDt = null;
    public Date data;

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
        String novaLinha = "";
        Lancamentos lancamento;

        FileReader arq = new FileReader(arquivoTxt);
        BufferedReader lerArq = new BufferedReader(arq);

        // lê a primeira linha
        String linha = lerArq.readLine();

        while (linha != null) {

            linhaInicial++;
            //lancamento = new Lancamentos();

            //System.out.println("[" + linhaInicial + "]Linha: " + linha);
            if (linha.contains("DATA DE EMISSAO")) {
                //System.out.println("[" + linhaInicial + "]Linha: " + linha);
                //System.out.println("Data: " + pegarData(pegaData(linha)));
                data = pegarData(pegaData(linha));
            }
            if (linha.startsWith("460")
                    || linha.startsWith("519")
                    || linha.startsWith("120")
                    || linha.startsWith("170")
                    || linha.startsWith("410")
                    || linha.startsWith("927")) {                
                
                novaLinha = linha.substring(34, linha.length());                
                lancamento = new Lancamentos();
                lancamento.setPeriodo(data);
                lancamento.setHistorico(pegaNome(novaLinha));
                lancamento.setSeguradora(seguradora);
                lancamento.setComissao(fomatarValorStr(pegaVlrComissao(novaLinha)));
                lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                calcularPercentualEPremioLiquido(lancamento, percent);
                if (lancamento.getComissao() < 0) {
                    lancamento.setParcela(0);
                } else {
                    lancamento.setParcela(1);
                }
                
                listaLanc.add(lancamento);
                //System.out.println("[" + linhaInicial + "]Linha: " + novaLinha);
                //System.out.println("Nome: " + pegaNome(novaLinha)+ " Data: "+data);
                //System.out.println("Valor: " + fomatarValorStr(pegaVlrComissao(novaLinha)));
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

    public void apagarArquivoTemporario(String path) {
        File f = new File(path);
        f.delete();
    }

    public String pegaData(String linha) {
        return linha.substring(16, 27).trim();
    }

    public Date pegarData(String arquivo) throws Exception {
        String data = "";
        int index = 0;
        for (int i = 0; i < arquivo.length(); i++) {
            if (Character.isDigit(arquivo.charAt(i))) {
                index++;
                data += "" + (arquivo.charAt(i));
                if (index == 2 || index == 4) {
                    data += "" + '/';
                }
            }
        }
        return formatarDataStr(data);
    }

    public String pegaNome(String linha) {
        String nome = "";
        for (int i = 0; i < linha.length(); i++) {
            if (!Character.isDigit(linha.charAt(i))) {
                if (Character.isAlphabetic(linha.charAt(i))
                        || Character.isSpaceChar(linha.charAt(i))) {
                    nome += linha.charAt(i);
                }

            }
        }
        return nome.trim();
    }

    public String pegaVlrComissao(String linha) {
        String vlr = "", valor = "";
        int contaEspaco = 0;
        for (int i = linha.length() - 1; i > 0; i--) {
            if (!Character.isSpaceChar(linha.charAt(i))) {
                vlr += linha.charAt(i);
                contaEspaco--;
            } else {
                contaEspaco++;
            }

            if (contaEspaco == 3) {
                break;
            }
        }
        //System.out.println("Valor: " + vlr+ " tamanho: "+vlr.length());
        // Faz a volta
        for (int i = vlr.trim().length(); i > 0; i--) {
            //System.out.println("Character: "+vlr.charAt(i-1));
            valor += vlr.charAt(i - 1);
        }

        if (valor.contains("-")) {
            valor = valor.replace("-", "");
            valor = "-".concat(valor);
        }
        return valor.trim();
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
        String path = "E:\\Adriana\\Bradesco\\";
        String pdf = "comissao Bradesco.PDF";
        List<Lancamentos> lst = new ArrayList<Lancamentos>();
        PDFBradescoFull pdfAzul = new PDFBradescoFull();
        try {
            lst = pdfAzul.lerPdfAzul(path + pdf, "Bradesco", 8.21);

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
            Logger.getLogger(PDFBradescoFull.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
