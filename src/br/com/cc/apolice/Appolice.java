package br.com.cc.apolice;

import br.com.cc.entidade.ApoliceBean;

public interface Appolice {

	public void carregarApolice(String path) throws Exception;
	public void pegarDadosGerais() throws Exception;
	public void pegarDadosDoSeguradora() throws Exception;
	public void pegarDadosDaSurcusal() throws Exception;
	public void pegarDadosDoCorretor();
	public void pegarDadosDoSegurado();
	public void pegarDadosDoVeiculo();
	public ApoliceBean gerarApolice();
	public void fechar() throws Exception;
}
