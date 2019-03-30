/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.pdf;

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
public class PDFProdutorSegurado implements Serializable{

    private List<Produtor> listProdutor = new ArrayList<Produtor>();
    private Produtor produtor;

    public void lerPdfProdudorSegurado(String path) throws Exception {
        int numeroPaginas;
        String s = "";
        //Carregando o PDF
        PdfReader pdfReader = new PdfReader(path);

        // Numero de paginas
        numeroPaginas = pdfReader.getNumberOfPages();
        System.out.println("Numero de paginas: " + numeroPaginas);

        //Extraindo de página em página e jogando numa String
        for (int i = 1; i <= numeroPaginas; i++) {
                //s = s.concat(PdfTextExtractor.getTextFromPage(pdfReader, i) + "\n\n");
            //System.out.println("linha: " + i + " " + PdfTextExtractor.getTextFromPage(pdfReader, i));
            gravarPaginaTxt(PdfTextExtractor.getTextFromPage(pdfReader, i), path);

        }
        //System.out.println(s);
    }

    public void gravarPaginaTxt(String pagina, String path) throws IOException {
        String arquivoTxt = path + ".txt";
        //System.out.println("Arq: "+arquivoTxt);
        PrintWriter writer = new PrintWriter(new FileOutputStream(arquivoTxt));

        writer.print(pagina);
        writer.println();
        writer.flush();
        writer.close();

        lerPaginaTxt(arquivoTxt);
    }

    public void lerPaginaTxt(String path) throws FileNotFoundException, IOException {
        String arquivoTxt = path;
        //System.out.println("Ler pagina");
        int linhaInicial = 0;

        FileReader arq = new FileReader(arquivoTxt);
        BufferedReader lerArq = new BufferedReader(arq);

        // lê a primeira linha
        String linha = lerArq.readLine();

        while (linha != null) {
            //System.out.printf("%s\n", linha);
            produtor = new Produtor();

            if (linhaInicial++ > 4) {
                //System.out.printf("%s\n", linha);
                produtor.setSegurado(pegarSegurado(linha));
                produtor.setProdutor(pegarProdutor(linha));
            }

            if (linha.isEmpty()) {
                break;
            }
            // lê da segunda até a última linha
            linha = lerArq.readLine();
            if (produtor.getProdutor() != null && produtor.getProdutor().length() > 0) {
                listProdutor.add(produtor);
            }
        }
        arq.close();
        //pagarArquivoTemporario(arquivoTxt);
    }
    
    public void pagarArquivoTemporario (String path){
        File f = new File(path);
        f.delete();
    }

    public String pegarSegurado(String linha) {
        String nome = "";
        if (!linha.contains("Total Listado") && !linha.contains("SEG Professional Edition")) {
            nome = linha.substring(0, iIndexNumero(linha));
            //System.out.println("NOme: " + nome);
        }
        return nome.trim();
    }

    public String pegarProdutor(String linha) {
        String nome = "";
        String nome2 = "";

        if (!linha.contains("Total Listado") && !linha.contains("SEG Professional Edition")) {
            nome = linha.substring(iIndexInvertido(linha), linha.length());
            nome2 = nome.substring(iIndexComBr(nome), nome.length() - 2);
            //System.out.println("Produtor: "+nome);
            //System.out.println("Produtor2: " + nome2);
            //System.out.println("Linha: " + linha);
        }
        return nome2.trim();
    }

    public int iIndexNumero(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isDigit(linha.charAt(i))) {
                return i;
            }
        }
        return 0;
    }

    public int iIndexInvertido(String linha) {
        for (int i = linha.length() - 2; i > 0; i--) {
            if (Character.isDigit(linha.charAt(i))) {
                //System.out.println("LInha: "+linha);
                //System.out.println("Ret: "+i+" nu: "+linha.length());
                return i + 1;
            }
        }
        return 0;
    }

    public int iIndexComBr(String linha) {
        if (linha.contains(".com.br")) {
            return (linha.indexOf(".com.br") + 7);
        } else if (linha.contains(".com")) {
            return (linha.indexOf(".com") + 4);
        }
        return 0;
    }

    public static void main(String[] args) {
        String path = "E:\\Adriana\\";
        String pdf = "Segurados.pdf";
        int i = 0;
        PDFProdutorSegurado p = new PDFProdutorSegurado();
        try {
            p.lerPdfProdudorSegurado(path + pdf);
        } catch (Exception ex) {
            Logger.getLogger(PDFProdutorSegurado.class.getName()).log(Level.SEVERE, null, ex);
        }
        //p.lerPaginaTxt();
        for (Produtor pr : p.listProdutor) {
            System.out.println("Index: "+i++ +" Segurado: " + pr.getSegurado() + " Produtor: " + pr.getProdutor());
        }
    }

    public List<Produtor> getListProdutor() {
        return listProdutor;
    }

    public void setListProdutor(List<Produtor> listProdutor) {
        this.listProdutor = listProdutor;
    }

}
