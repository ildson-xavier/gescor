package br.com.cc.apolice;

import java.io.IOException;

import br.com.cc.entidade.ApoliceBean;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.VeiculoBean;
import br.com.cc.util.Util;

public class ApoliceMapfre extends ApoliceFile implements Appolice{
	
	private String texto;
	private ApoliceBean apoliceBean;
	private VeiculoBean veiculoBean;
	private Produtor seguradoBean;
	
	public ApoliceMapfre(){
		super();
	}
	public void carregarApolice(String path) throws Exception {
		super.apoliceFile(path);
		this.texto = lerArquivo(getPathTxt());
		apoliceBean = new ApoliceBean();
		veiculoBean = new VeiculoBean();
		seguradoBean = new Produtor();
	}	
	
	public void pegarDadosGerais() throws Exception{
		int posicao = texto.indexOf("DADOS GERAIS");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoProcessoSusep = txt.indexOf("Processo SUSEP Autom�vel N�:");
		int posicaoProduto = txt.indexOf("Produto:");
		int posicaoApolice = txt.indexOf("N� Ap�lice:");
		int posicaoEndosso = txt.indexOf("Endosso:");
		int posicaoItens = txt.indexOf("Itens:");
		int posicaoVia = txt.indexOf("N� Via:");
		int posicaoVigenciaInicio = txt.indexOf("Vig�ncia in�cio 24h do dia:");
		int posicaoVigenciaFim = txt.indexOf("T�rmino 24h do dia:");
		int posicaoDataProposta = txt.indexOf("Data e hora da proposta:");
		int posicaoProposta = txt.indexOf("N� Proposta:");
		int posicaoFinal = txt.indexOf("Vers�o de c�lculo:");
		
		apoliceBean.setNumeroApolice(txt.substring(posicaoApolice + "N� Ap�lice:".length(), posicaoEndosso).trim());
		apoliceBean.setSusep(txt.substring(posicaoProcessoSusep + "Processo SUSEP Autom�vel N�:".length(), posicaoProduto).trim());
		apoliceBean.setDataVigenciaInicio(Util.formatarDataStr(txt.substring(posicaoVigenciaInicio + "Vig�ncia in�cio 24h do dia:".length(), posicaoVigenciaFim).trim()));
		apoliceBean.setDataTermino(Util.formatarDataStr(txt.substring(posicaoVigenciaFim + "T�rmino 24h do dia:".length(), posicaoDataProposta).trim()));
		apoliceBean.setSeguradora("Mapfre");

	}
	
	public void pegarDadosDoSeguradora() throws Exception{
		
	}
	
	public void pegarDadosDaSurcusal() throws Exception{
		
	}
	
	public void pegarDadosDoCorretor(){
		
	}
	
	public void pegarDadosDoSegurado(){
		int posicao = texto.indexOf("DADOS DO SEGURADO");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoNome = txt.indexOf("Nome:");
		int posicaoTipoPessoa = txt.indexOf("Tipo de pessoa:");
		int posicaoCnpj = txt.indexOf("CNPJ:");
		int posicaoCpf = txt.indexOf("CPF:");
		int posicaoEndereco = txt.indexOf("Endere�o:");
		int posicaoBairro = txt.indexOf("Bairro:");
		int posicaoCep = txt.indexOf("CEP:");
		int posicaoCidade = txt.indexOf("Cidade:");
		int posicaoUf = txt.indexOf("UF:");
		int posicaoTelefone = txt.indexOf("Telefone residencial:");
		int posicaoCelular = txt.indexOf("Telefone celular:");
		int posicaoFinal = txt.indexOf("QUESTION�RIO DE AVALIA��O DE RISCO");
		
		seguradoBean.setBairro(txt.substring(posicaoBairro + "Bairro:".length(), posicaoCep).trim());
		seguradoBean.setCelular(txt.substring(posicaoCelular + "Telefone celular:".length(), posicaoFinal).trim());
		seguradoBean.setCep(txt.substring(posicaoCep + "CEP:".length(), posicaoCidade).trim());
		seguradoBean.setCidade(txt.substring(posicaoCidade + "Cidade:".length(), posicaoUf).trim());
		seguradoBean.setCpf(txt.substring(posicaoCpf + "CPF:".length(), posicaoEndereco).trim());
		seguradoBean.setEndereco(txt.substring(posicaoEndereco + "Endere�o:".length(), posicaoBairro).trim());
		seguradoBean.setBairro(txt.substring(posicaoBairro + "Bairro:".length(), posicaoCep).trim());
		seguradoBean.setTelefone(txt.substring(posicaoTelefone + "Telefone residencial:".length(), posicaoCelular).trim());
		seguradoBean.setCelular(txt.substring(posicaoCelular + "Telefone celular:".length(), posicaoFinal).trim());
		seguradoBean.setUf(txt.substring(posicaoUf + "UF:".length(), posicaoTelefone).trim());
		seguradoBean.setSegurado(txt.substring(posicaoNome + "Nome:".length(), posicaoTipoPessoa).trim());
	}
	
	public void pegarDadosDoVeiculo(){
		int posicao = texto.indexOf("DADOS DO VE�CULO");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoMarcaModelo = txt.indexOf("Marca/Modelo:");
		int posicaoAno = txt.indexOf("Ano de fabrica��o/Ano do modelo:");
		int posicaoPlaca = txt.indexOf("Placa:");
		int posicaoKm = txt.indexOf("0 KM:");
		int posicaoNChassi = txt.indexOf("N� Chassi:");
		int posicaoChassiR = txt.indexOf("Chassi Remarcado:");
		int posicaoPassageiro = txt.indexOf("Capacidade/passageiros:");
		int posicaoCategoria = txt.indexOf("Categoria tarif�ria:");
		int posicaoUso = txt.indexOf("Uso:");
		int posicaoTipo = txt.indexOf("Tipo de isen��o:");
		int posicaoBlindagem = txt.indexOf("Blindagem:");
		int posicaoCertificado = txt.indexOf("Certificado de propriedade:");
		int posicaoFinal = txt.indexOf("OPCIONAIS E SISTEMAS DE SEGURAN�A");
		
		veiculoBean.setAnoFabricacaoAnoModelo(txt.substring(posicaoAno + "Ano de fabrica��o/Ano do modelo:".length(), posicaoPlaca).trim());
		veiculoBean.setChassi(txt.substring(posicaoNChassi + "N� Chassi:".length(), posicaoChassiR).trim());
		veiculoBean.setMarcaModelo(txt.substring(posicaoMarcaModelo + "Marca/Modelo:".length(), posicaoAno).trim());
		veiculoBean.setPlaca(txt.substring(posicaoPlaca + "Placa:".length(), posicaoKm).trim());
		veiculoBean.setZeroKm(txt.substring(posicaoKm + "0 KM:".length(), posicaoNChassi).trim());
	}
	
	public ApoliceBean gerarApolice(){
		apoliceBean.setSegurado(seguradoBean);
		apoliceBean.setVeiculo(veiculoBean);
		return apoliceBean;
		
	}
	
	public void fechar() throws IOException{
		apagarArquivoTemporario(getPathTxt());
		super.fechar();
	}
	
	public static void main(String[] args) throws Exception {
		String path = "X:\\Adriana\\Apolice\\";
		String pdf = "mapfre.pdf";
		
		ApoliceMapfre am = new ApoliceMapfre();
		am.carregarApolice(path+pdf);
		am.pegarDadosGerais();
		am.pegarDadosDoSegurado();
		am.pegarDadosDoVeiculo();
		ApoliceBean bean = am.gerarApolice();
		System.out.println(bean);
		
		am.fechar();
	}
}
