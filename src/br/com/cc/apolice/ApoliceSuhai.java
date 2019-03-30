package br.com.cc.apolice;

import java.io.IOException;

import br.com.cc.entidade.ApoliceBean;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.VeiculoBean;
import br.com.cc.util.Util;

public class ApoliceSuhai extends ApoliceFile implements Appolice{
	
	private String texto;
	private ApoliceBean apoliceBean;
	private VeiculoBean veiculoBean;
	private Produtor seguradoBean;
	
	public ApoliceSuhai(){
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
		int posicao = texto.indexOf("SUHAI SEGURADORA- SEGURO AUTOM�VEL");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoProcessoSusep = txt.indexOf("Processo SUSEP:");
		int posicaoProduto = txt.indexOf("Registro Eletr�nico de Produto Susep -");
		int posicaoVigenciaInicio = txt.indexOf("Data de Vig�ncia: Das 24:00 hs. de");
		int posicaoVigenciaFim = txt.indexOf("at� �s 24:00 hs. de");
		int posicaoApolice = txt.indexOf("AP�LICE N�");
		int posicaoFinal = txt.indexOf("Nr. Endosso:");
		
		apoliceBean.setNumeroApolice(txt.substring(posicaoApolice + "AP�LICE N�".length(), posicaoFinal).trim());
		apoliceBean.setSusep(txt.substring(posicaoProcessoSusep + "Processo SUSEP:".length(), posicaoProduto).trim());
		apoliceBean.setDataVigenciaInicio(Util.formatarDataStr(txt.substring(posicaoVigenciaInicio + "Data de Vig�ncia: Das 24:00 hs. de".length(), posicaoVigenciaFim).trim()));
		apoliceBean.setDataTermino(Util.formatarDataStr(txt.substring(posicaoVigenciaFim + "at� �s 24:00 hs. de".length(), posicaoApolice).trim()));
		apoliceBean.setSeguradora("Suhai");

	}
	
	public void pegarDadosDoSeguradora() throws Exception{
		
	}
	
	public void pegarDadosDaSurcusal() throws Exception{
		
	}
	
	public void pegarDadosDoCorretor(){
		
	}
	
	public void pegarDadosDoSegurado(){
		int posicao = texto.indexOf("PROPONENTE");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoNome = txt.indexOf("Nome:");
		int posicaoEmail = txt.indexOf("Email:");
		int posicaoCpf = txt.indexOf("CPF/CNPJ:");
		int posicaoEndereco = txt.indexOf("Endere�o:");
		int posicaoCep = txt.indexOf("CEP:");
		int posicaoTelefone = txt.indexOf("Tel. Residencial:");
		int posicaoTelComercial = txt.indexOf("Tel. Comercial: Tel.");
		int posicaoCelular = txt.indexOf("Celular:");
		int posicaoFinal = txt.indexOf("VE�CULO");

		seguradoBean.setCelular(txt.substring(posicaoCelular + "Celular:".length(), posicaoFinal).trim());
		seguradoBean.setCep(txt.substring(posicaoCep + "CEP:".length(), posicaoCep + "CEP:".length() + 8).trim());
		seguradoBean.setCpf(txt.substring(posicaoCpf - 14, posicaoCpf).trim());
		seguradoBean.setEndereco(txt.substring(posicaoCep + "CEP:".length() + 8, posicaoTelefone).trim());
		seguradoBean.setTelefone(txt.substring(posicaoTelefone + "Tel. Residencial:".length(), posicaoTelComercial).trim());
		seguradoBean.setCelular(txt.substring(posicaoCelular + "Celular:".length(), posicaoFinal).trim());
		seguradoBean.setSegurado(txt.substring(posicaoNome + "Nome:".length(), posicaoEmail).trim());
		seguradoBean.setEmail(txt.substring(posicaoEmail + "Email:".length(), posicaoCpf - 14));
	}
	
	public void pegarDadosDoVeiculo(){
		int posicao = texto.indexOf("VE�CULO");
		String txt = texto.substring(posicao, texto.length());
		
		int posicaoTipoSeguro = txt.indexOf("Tipo de Seguro:");
		int posicaoClasseBonus = txt.indexOf("Classe de B�nus:");
		int posicaoApoliceAtual = txt.indexOf("C�digo C.I. Ap�l. Atual:");
		int posicaoMarcaModelo = txt.indexOf("Marca, Ano de Fabrica��o/Modelo:");
		int posicaoKm = txt.indexOf("0 km:");
		int posicaoNChassi = txt.indexOf("Chassi:");
		int posicaoRenavam = txt.indexOf("Renavam:");
		int posicaoUso = txt.indexOf("Uso:");
		int posicaoPlaca = txt.indexOf("Placa:");
		int posicaoCor = txt.indexOf("Cor:");
		int posicaoCombustivel = txt.indexOf("Combust�vel:");
		int posicaoFipe = txt.indexOf("C�digo FIPE:");
		int posicaoRegial = txt.indexOf("Regi�o de Circula��o:");
		int posicaoCategoria = txt.indexOf("Cat.Tarif�ria:");
		int posicaoTipo = txt.indexOf("Tipo de utiliza��o:");
		int posicaoFinal = txt.indexOf("EPS");
		
		veiculoBean.setAnoFabricacaoAnoModelo(txt.substring(posicaoMarcaModelo + "Marca, Ano de Fabrica��o/Modelo:".length(), posicaoKm).trim());
		veiculoBean.setChassi(txt.substring(posicaoNChassi + "Chassi:".length(), posicaoRenavam).trim());
		veiculoBean.setMarcaModelo(txt.substring(posicaoMarcaModelo + "Marca, Ano de Fabrica��o/Modelo:".length(), posicaoKm).trim());
		veiculoBean.setPlaca(txt.substring(posicaoPlaca + "Placa:".length(), posicaoCor).trim());
		veiculoBean.setZeroKm(txt.substring(posicaoKm + "0 km:".length(), posicaoNChassi).trim());
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
		String pdf = "suhai.pdf";
		
		ApoliceSuhai am = new ApoliceSuhai();
		am.carregarApolice(path+pdf);
		am.pegarDadosGerais();
		am.pegarDadosDoSegurado();
		am.pegarDadosDoVeiculo();
		ApoliceBean bean = am.gerarApolice();
		System.out.println(am.texto);
		
		am.fechar();
	}
}
