package br.com.cc.entidade.sinistro;

public enum StatusSinistro {

	AGUARDANDO_ANALISE("Aguardando análise"),
	EM_ANALISE ("Em análise"), 
	LIBERADO_CONSERTO ("Liberado para conserto"), 
	AGUARDANDO_PECA ("Aguarando peça"), 
	CONCLUIDO ("Concluído");
	
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
