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
public class PDFBradesco implements Serializable {

    //public List<Lancamentos> listaLanc = new LinkedList<Lancamentos>();
    public Lancamentos lancamento = null;
    public Lancamentos lancamentoDt = null;

    public List<Lancamentos> lerPdfBradesco(String path, String seguradora, Double percent) throws Exception {
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

        //Extraindo de página em página e jogando numa String
        for (int i = 1; i <= numeroPaginas; i++) {
            // Ignora a primeira pagina
            if (i > 1) {
                gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path, seguradora, percent, listaLanc);
            }
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
        String nome;
        String vlrComissao;
        Date dataComissao;
        Double valorComissao;

        FileReader arq = new FileReader(arquivoTxt);
        BufferedReader lerArq = new BufferedReader(arq);

        // lê a primeira linha
        String linha = lerArq.readLine();
        linha = lerArq.readLine();
        linha = lerArq.readLine();
        //linha = lerArq.readLine();

        while (linha != null) {

            //System.out.println("Linha: " + linha + " index: " + linhaInicial);
            //lancamento = new Lancamentos();
            // pega a data
            if (linhaInicial < 3) {
                if (linha.contains("Data")) {
                    lancamentoDt = new Lancamentos();
                    lancamentoDt.setPeriodo(formatarDataStr(pegaData(linha)));
                    //System.out.println("Periodo: " + formatarDataStr(pegaData(linha)));
                }
            }

            if (linhaInicial > 2) {
                if (!linha.contains("Página")) {
                    if (linha.startsWith("0 0 ")) {
                        lancamento = new Lancamentos();
                        lancamento.setHistorico(pegaNome(linha));
                        lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                        lancamento.setComissao(fomatarValorStr(pegaVlrComissao(linha)));
                        if (lancamento.getComissao() < 0) {
                            lancamento.setParcela(0);
                        } else {
                            lancamento.setParcela(1);
                        }
                        lancamento.setPeriodo(lancamentoDt.getPeriodo());
                        lancamento.setSeguradora(seguradora);
                        lancamento.setClassificacao(1);
                        calcularPercentualEPremioLiquido(lancamento, percent);
                        listaLanc.add(lancamento);
                    }
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
        return linha.substring(5, 15).trim();
    }

    public String pegaNome(String linha) {
        String nome = null;
        nome = linha.substring(4, iIndexNumero(linha));
        nome = nome.replaceAll(" COM.PG", " ");
        nome = nome.replaceAll("              ", " ");
        return nome.trim();

    }

    public String pegaVlrComissao(String linha) {
        String vlr = null;
        vlr = linha.substring(iIndexInvertido(linha), linha.length());
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
        for (int i = linha.length() - 1; i > 0; i--) {
            if (Character.isSpaceChar(linha.charAt(i))) {
                //System.out.println("Caracter: "+Character.isSpaceChar(linha.charAt(i)));
                //System.out.println("Ret: "+i+" nu: "+linha.length());
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
        String path = "E:\\Adriana\\";
        String pdf = "Bradesco.pdf";
        int index = 0;
        List<Lancamentos> lst = new ArrayList<Lancamentos>();
        PDFBradesco pdfAzul = new PDFBradesco();
        try {
            lst = pdfAzul.lerPdfBradesco(path + pdf, "Bradesco", 8.21);

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
            Logger.getLogger(PDFBradesco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
