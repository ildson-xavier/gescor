package br.com.cc.entidade.sinistro;

public enum StatusSinistro {

	AGUARDANDO_ANALISE("Aguardando an�lise"),
	EM_ANALISE ("Em an�lise"), 
	LIBERADO_CONSERTO ("Liberado para conserto"), 
	AGUARDANDO_PECA ("Aguarando pe�a"), 
	CONCLUIDO ("Conclu�do");
	
	private String descricao;

	private StatusSinistro(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
			
			
}
