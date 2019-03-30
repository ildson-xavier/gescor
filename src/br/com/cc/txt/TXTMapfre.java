/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.txt;

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
public class TXTMapfre implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Lancamentos> lerTXTMapfre(String path, String seguradora, Double percent, Date data)
			throws FileNotFoundException, Exception {
		int linhaInicial = 0;

		List<Lancamentos> listaLanc = new ArrayList<Lancamentos>();
		Lancamentos lancamento;

		FileReader arq = new FileReader(path);
		BufferedReader lerArq = new BufferedReader(arq);

		// lÃª a primeira linha
		String linha = lerArq.readLine();

		while (linha != null) {
			// lancamento = new Lancamentos();
			// System.out.println("[" + linhaInicial + "]Linha: " + linha);
			// System.out.println("Tipo de linha: "+tipoLinha(linha));
			// System.out.println("Data: "+pegaData(linha));
			if (tipoLinha(linha) == 3 && !linha.contains("COMIS.SOBRE CUSTO DE EMISSAO")) {
				lancamento = new Lancamentos();
				lancamento.setPeriodo(dataIgual(data, pegarData(pegaData(linha))));
				lancamento.setDataIncluisao(Calendar.getInstance().getTime());
				lancamento.setHistorico(pegaNome(linha));
				lancamento.setSeguradora(seguradora);
				lancamento.setComissao(fomatarValorStr(pegaValor(linha)));
				if (lancamento.getComissao() < 0) {
					lancamento.setParcela(0);
				} else {
					lancamento.setParcela(pegaParcela(linha));
				}
				calcularPercentualEPremioLiquido(lancamento, percent);
				lancamento.setClassificacao(1);
				listaLanc.add(lancamento);

				System.out.println("[" + linhaInicial + "]Linha: " + linha);
				System.out.println("Data: " + pegarData(pegaData(linha)));
				System.out.println("Parcela: " + pegaParcela(linha));
				System.out.println("Nome: " + pegaNome(linha));
				System.out.println("Valor: " + fomatarValorStr(pegaValor(linha)));

			}

			linha = lerArq.readLine();
			linhaInicial++;
		}
		arq.close();
		lerArq.close();
		// apagarArquivoTemporario(path);

		return listaLanc;
	}

	public Date dataIgual(Date dataExterna, Date dataInterna) {
		Date retorno = null;

		Date dataI = dataInterna;
		Date dataE = dataExterna;

		if (dataE.compareTo(dataI) == 0) {
			retorno = dataInterna;
		} else {
			retorno = dataE;
		}

		return retorno;
	}

	public void apagarArquivoTemporario(String path) {
		File f = new File(path);
		f.delete();
	}

	public void calcularPercentualEPremioLiquido(Lancamentos lancamentos, Double percent) {
		lancamentos.setValor2((percent * lancamentos.getComissao()) / 100);
		lancamentos.setValor3(lancamentos.getComissao() - (lancamentos.getValor2()));
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
		if (index > 8) {
			throw new Exception("Erro na formatação da data [" + data + "].");
		}
		return formatarDataStr(data);
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

	public String pegaValor(String linha) {
		return linha.length() > 100 ? linha.substring(116, (117 + 15)) : "";
	}

	public int tipoLinha(String linha) {
		return linha.length() > 23 ? Integer.parseInt(linha.substring(21, 23)) : 0;
	}

	public String pegaData(String linha) {
		return linha.length() > 23 ? linha.substring(8, 16) : "";
	}

	public int pegaParcela(String linha) {
		return linha.length() > 23 ? Integer.parseInt(linha.substring(50, 52)) : 0;
	}

	public String pegaNome(String linha) {
		return linha.length() > 23 ? linha.substring(52, 92).trim() : "";
	}

	public static void main(String[] args) {

		String path = "D:\\Adriana\\MAPFRE\\";
		String pdf = "27.04.2015.txt";
		List<Lancamentos> lst = new ArrayList<Lancamentos>();

		TXTMapfre txt = new TXTMapfre();
		try {
			lst = txt.lerTXTMapfre(path + pdf, "Mapfre", 8.13, new Date());

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
			Logger.getLogger(TXTMapfre.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
