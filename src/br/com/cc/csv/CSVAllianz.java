/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.csv;

import br.com.cc.entidade.Lancamentos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class CSVAllianz implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Lancamentos> lerCSVAllianz(String path, String seguradora, Double percent, Date dataRemessa)
            throws FileNotFoundException, Exception {
        int linhaInicial = 0;
        String novaLinha = "";
        int countFilial = 0;
        boolean tem16 = false;

        List<Lancamentos> listaLanc = new ArrayList<Lancamentos>();
        Lancamentos lancamento;

        FileReader arq = new FileReader(path);
        BufferedReader lerArq = new BufferedReader(arq);

        // lÃª a primeira linha
        String linha = lerArq.readLine();

        while (linha != null) {  
        	//System.out.println("["+linhaInicial+"]Linha: "+linha);
        	if (linhaInicial < 5 && !tem16) {
        		if (linha.contains("PERÍODO INÍCIO")){
        			countFilial = 16;
        			tem16 = true;
        		} else {
        			countFilial = 14;
        		}
        	}
        	
            //System.out.println("["+linhaInicial+"]Linha: "+linha);
            if (linhaInicial > countFilial && !linha.contains(";;;;TOTAL;")) {
            	//System.out.println("["+linhaInicial+"]Linha: "+linha);
            	lancamento = new Lancamentos();
                novaLinha = linha.substring(75, linha.length());
                //System.out.println("[" + linhaInicial + "]NovaLinha: " + pegaNome(novaLinha)
                //        + " " + fomatarValorStr(pegaVlrComissao(novaLinha)));

                lancamento.setHistorico(pegaNome(novaLinha));
                lancamento.setDataIncluisao(Calendar.getInstance().getTime());
                lancamento.setPeriodo(dataRemessa);
                lancamento.setSeguradora(seguradora);
                lancamento.setClassificacao(1);
                lancamento.setComissao(fomatarValorStr(pegaVlrComissao(novaLinha)));
                lancamento.setParcela(Integer.parseInt(pegaParcela(novaLinha)));
                if (lancamento.getComissao() < 0) {
                    lancamento.setParcela(0);
                }                
                calcularPercentualEPremioLiquido(lancamento, percent);
                listaLanc.add(lancamento);
            }
            linha = lerArq.readLine();
            linhaInicial++;
        }
        arq.close();
        lerArq.close();
        apagarArquivoTemporario(path);

        return listaLanc;
    }

    public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
        lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
        lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
    }

    public String pegaVlrComissao(String linha) {
        String vlr = null;
        vlr = linha.substring(iIndexInvertido2(linha), iIndexInvertido(linha));
        return vlr.trim();
    }

    public int iIndexInvertido(String linha) {
        for (int i = linha.length() - 2; i > 0; i--) {
            if (linha.charAt(i) == ';') {
                return i;
            }
        }
        return 0;
    }

    public int iIndexInvertido2(String linha) {
        int index = 0;
        for (int i = linha.length() - 2; i > 0; i--) {
            if (linha.charAt(i) == ';') {
                if (index == 1) {
                    return i + 1;
                }
                index = 1;
            }
        }
        return 0;
    }

    public String pegaParcela(String linha) {
        return linha.substring(iIndexLetra(linha) - 7, iIndexLetra(linha) - 5);
    }

    public String pegaNome(String linha) {
        String nome = null;
        nome = linha.substring(iIndexLetra(linha), iIndexEspaco(linha));
        return nome.trim();
    }

    public int iIndexLetra(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isAlphabetic(linha.charAt(i))) {
                return i;
            }
        }
        return 0;
    }

    public int iIndexEspaco(String linha) {
        for (int i = linha.length() - 1; i > 0; i--) {
            if (Character.isSpaceChar(linha.charAt(i))) {
                return i;
            }
        }
        return linha.length();
    }

    public int iIndexNumero(String linha) {
        for (int i = 0; i < linha.length(); i++) {
            if (Character.isDigit(linha.charAt(i))) {
                return i;
            }
        }
        return 0;
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

    public void apagarArquivoTemporario(String path) {
        File f = new File(path);
        f.delete();
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
        if (index > 8 || !validaData(data)) {
            throw new Exception("Erro na formataÃ§Ã£o da data [" + data + "].");
        }
        return formatarDataStr(data);
    }

    public boolean validaData(String data) throws Exception {
        boolean toReturn = true;

        if (data.length() == 10) {

            if (!diaOk(data.substring(0, 2))) {
                return false;
            }

            if (!mesOk(data.substring(3, 5))) {
                return false;
            }

            if (!anoOk(data.substring(6, 10))) {
                return false;
            }
        } else {
            return false;
        }
        return toReturn;
    }

    public boolean diaOk(String data) throws Exception {
        int dia;
        if (!data.equals("")) {
            dia = Integer.parseInt(data);
            if (dia > 0 && dia <= 31) {
                return true;
            }
        }
        return false;
    }

    public boolean mesOk(String data) throws Exception {
        int mes;
        if (!data.equals("")) {
            mes = Integer.parseInt(data);
            if (mes > 0 && mes <= 12) {
                return true;
            }
        }
        return false;
    }

    public boolean anoOk(String data) throws Exception {
        int ano;
        if (!data.equals("")) {
            ano = Integer.parseInt(data);
            if (ano != 0 && ano > 2010 && ano <= 2030) {
                return true;
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

    public static void main(String[] args) {

        String path = "D:\\Adriana\\ALLIANZ\\";
        String pdf = "29.10.2015.csv";
        List<Lancamentos> lst = new ArrayList<Lancamentos>();
        CSVAllianz csv = new CSVAllianz();
        try {
            System.out.println("Data: " + csv.pegarData(pdf));

            lst = csv.lerCSVAllianz(path + pdf, "Allianz", 8.13, csv.pegarData(pdf));

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
            Logger.getLogger(CSVAllianz.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
