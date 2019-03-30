/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Usuario
 */
public class Util implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void verificaMemoria(String msg) {
		double heapSize = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		double heapMaxSize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		double heapFreeSize = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		Runtime.getRuntime().gc();
		System.out.println(String.format(
				"%s --> Tamanho Memoria Heap: %sMb, Tamanho Maximo Memoria Heap: %sMb, Memoria Heap Disponivel: %sMb",
				msg, heapSize, heapMaxSize, heapFreeSize));

	}

	public String removerEnter(String str) {  
		System.out.println("Remove enter");
    	StringBuffer buf = new StringBuffer();  
    	char[] c = str.toCharArray();  
    	  
    	for (int i = 0; i < c.length; i++) {  
    		if (Character.isSpaceChar(c[i])){
    			buf.append(c[i]);
    		} 
    	    if (c[i] >= 32 && c[i] <= 126) {  
    	        buf.append(c[i]);  
    	    }  
    	}  
    	  
    	return new String(buf);
	}
	
	
	public static String getFormatDatePt(String dateUsa){
        String [] formatoUsa = new String[50];
        String [] formatoPt = new String[50];
        String dataBr = "";
        int indexUsa = 0; 
        int indexPt = 0;
        
        formatoUsa [indexUsa++] = "January";
        formatoUsa [indexUsa++] = "February";
        formatoUsa [indexUsa++] = "March";
        formatoUsa [indexUsa++] = "April";
        formatoUsa [indexUsa++] = "May";
        formatoUsa [indexUsa++] = "June";
        formatoUsa [indexUsa++] = "July";
        formatoUsa [indexUsa++] = "August";        
        formatoUsa [indexUsa++] = "September";
        formatoUsa [indexUsa++] = "October";
        formatoUsa [indexUsa++] = "November";
        formatoUsa [indexUsa] = "December";
        
        formatoPt [indexPt++] = "Janeiro";
        formatoPt [indexPt++] = "Fevereiro";
        formatoPt [indexPt++] = "Março";
        formatoPt [indexPt++] = "Abril";
        formatoPt [indexPt++] = "Maio";
        formatoPt [indexPt++] = "Junho";
        formatoPt [indexPt++] = "Julho";
        formatoPt [indexPt++] = "Agosto";
        formatoPt [indexPt++] = "Setembro";
        formatoPt [indexPt++] = "Outubro";
        formatoPt [indexPt++] = "Novembro";
        formatoPt [indexPt] = "Dezembro";
        
        for (; indexUsa >= 0; indexUsa --){
            //System.out.println("Index: "+indexUsa);
            if (dateUsa.contains(formatoUsa[indexUsa])){
                dataBr = dateUsa.replace(formatoUsa[indexUsa], formatoPt[indexUsa]);
            }
        }
        
        return dataBr;
    }
    
     public static String getFormatDateUsa(String dateBr){
        String [] formatoUsa = new String[50];
        String [] formatoPt = new String[50];
        String dataUsa = "";
        int indexUsa = 0; 
        int indexPt = 0;
        
         System.out.println("getFormatDateUsa -> "+dateBr);
        
        formatoUsa [indexUsa++] = "January";
        formatoUsa [indexUsa++] = "February";
        formatoUsa [indexUsa++] = "March";
        formatoUsa [indexUsa++] = "April";
        formatoUsa [indexUsa++] = "May";
        formatoUsa [indexUsa++] = "June";
        formatoUsa [indexUsa++] = "July";
        formatoUsa [indexUsa++] = "August";
        formatoUsa [indexUsa++] = "September";
        formatoUsa [indexUsa++] = "October";
        formatoUsa [indexUsa++] = "November";
        formatoUsa [indexUsa] = "December";
        
        formatoPt [indexPt++] = "Janeiro";
        formatoPt [indexPt++] = "Fevereiro";
        formatoPt [indexPt++] = "Março";
        formatoPt [indexPt++] = "Abril";
        formatoPt [indexPt++] = "Maio";
        formatoPt [indexPt++] = "Junho";
        formatoPt [indexPt++] = "Julho";
        formatoPt [indexPt++] = "Agosto";
        formatoPt [indexPt++] = "Setembro";
        formatoPt [indexPt++] = "Outubro";
        formatoPt [indexPt++] = "Novembro";
        formatoPt [indexPt] = "Dezembro";
        
        for (; indexPt >= 0; indexPt --){
            System.out.println("Index: "+indexPt);
            if (dateBr.contains(formatoPt[indexPt])){
                dataUsa = dateBr.replace(formatoPt[indexPt], formatoUsa[indexPt]);
            }
        }
        
        return dataUsa;
    }
	
	public static String ajustarData(String data){
		String dat = "0";
		
		if (data.length() == 6){
			dat = dat.concat(data);
		} else {
			dat = data;
		}		
		return dat;
	}
	
	public static char getColuna(int i){
		char [] colunas = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P'};
		return colunas[i];
	}
	
	public static String formatarData(Date data){
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(data);
	}
	
	public static String formatarDataHora(Date data){
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		return formatter.format(data);
	}
	
	public static String nomeArquivo(String path){
		String nome = path.substring(path.lastIndexOf("resources") + 10, path.length());
		return nome.substring(nome.lastIndexOf('/') + 1, nome.length());
	}
	
	@SuppressWarnings("deprecation")
	public static Date setaDataInicial(Date data){
		String dataInicialDefault = "01/01/1999";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(dataInicialDefault));
		return cal.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public static Date setaDataFinal(Date data){
		String dataFinalDefault = "01/01/2090";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(dataFinalDefault));
		return cal.getTime();
	}
	
	public static Date validarData(Date data, Date dataValida){
		return data == null ? dataValida : data;
	}
	
	/**
	 * Validacao de data
	 * @param args
	 * @throws ParseException
	 */
	public static Date pegarDataDaNomenclatura(String arquivo) throws Exception {
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
		data += addAno(data);
		if (index > 8 || !validaData(data)) {
			throw new Exception("Erro na formatação da data [" + data + "].");
		}
		return formatarDataStr(data);
	}
	
	public static boolean validaData(String data) throws Exception {
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

	public static boolean diaOk(String data) throws Exception {
		int dia;
		if (!data.equals("")) {
			dia = Integer.parseInt(data);
			if (dia > 0 && dia <= 31) {
				return true;
			}
		}
		return false;
	}

	public static boolean mesOk(String data) throws Exception {
		int mes;
		if (!data.equals("")) {
			mes = Integer.parseInt(data);
			if (mes > 0 && mes <= 12) {
				return true;
			}
		}
		return false;
	}

	public static boolean anoOk(String data) throws Exception {
		int ano;
		if (!data.equals("")) {
			ano = Integer.parseInt(data);
			if (ano != 0 && ano > 2010 && ano <= 2030) {
				return true;
			}
		}
		return false;
	}

	public static Date formatarDataStr(String data) throws Exception {
		if (data == null || data.equals("")) {
			return null;
		}
		Date dt = null;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dt = dateFormat.parse(data);
		} catch (ParseException ex) {
			throw new Exception("Erro ao formatar a data: " + data);
		}
		return dt;
	}
	
	public static String converterDateParaString(Date data){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(data);
	}
	
	public static String extensaoData(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		return df.format(cal.getTime());
	}
	
	public static Date dataAtual(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Date converterDataStrParaDate(String data) throws Exception {
		if (data == null || data.equals("")) {
			return null;
		}
		Date dtComissao = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dtComissao = dateFormat.parse(data);
		} catch (ParseException ex) {
			throw new Exception("Erro ao formatar a data: " + data);
		}
		return dtComissao;
	}
	
	public static String addAno(String data){
		String ano = "";
		if (data.endsWith("/")){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			ano = String.valueOf(cal.get(Calendar.YEAR));
		}
		return ano;
	}

	public static String formataMesAno(Date data){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		return sdf.format(data);
	}
	
	public static boolean comparaDadas(Date dataUsa, String dataPt) throws ParseException{
		DateFormat formatPt = new SimpleDateFormat("dd/MM/yyyy");
		Date dateUsa = dataUsa;
		Date datePt = (Date) formatPt.parse(dataPt);
		
		if (dateUsa.compareTo(datePt) == 0){
			return true;
		}
		return false;
		
	}
	
	public static Double novoValor(Double valor){
		System.out.println("novoValor: valor: "+valor);
		Double val = valor;
		if (val != 0.0){
			Double calculado = val; /// 100;
			System.out.println("novoValor: calculado: "+calculado + " valor: "+valor);
			return validarValor (valor, calculado);
		}
		return val;
	}
	
	private static Double validarValor(Double entrada, Double saida){
		Double resultado = saida * 100;
		System.out.println("validarValor: entrada: "+arredondar(entrada) + " resultado: "+arredondar(resultado) + " saida: "+saida);
		if (arredondar(resultado) < arredondar(entrada)){
			return entrada;
		}
		return saida;
	}
	
	public static String criarNome(String nome){
		String novoNome = "";
		char [] pos = {' ',' ','A','B','C','D','E','F','G','I','J','K','L','M','N','O','P','Q','R','S','T','V','U','X','W','Y','Z','Ç', 'Ì', 'À', 'Á', 'Â', 'Ô', 'Ê', 'Ã', 'Õ'};
		for (int i = 0; i < nome.length(); i++){
			for (int j = 0; j < pos.length; j++){
				if (nome.charAt(i) == pos[j]){
					if (j == 1){
						novoNome += pos[j - 1];
					} else {
						novoNome += pos[j];
					}
					
				}
			}
		}
		return novoNome.equals("") ?  nome : novoNome;
	}
	
	public static Double arredondar(Double valor){
		BigDecimal bd = new BigDecimal(valor);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	public static Date converterData(Date data){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dataFormato = null;
		try {
			String dataFormatada = formatador.format(data);
			dataFormato = formato.parse(dataFormatada);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dataFormato;
	}
	
	
	
	public static void main(String[] args) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) formatter.parse("2015-01-11");
		Date date2 = new Date();
		String mesAno = "";
		// Date dt = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date2);
		System.out.println("Data > " +  Calendar.getInstance().getTime());
	
	}
}
