package br.com.gescor.cobranca.model;

public enum Status {

	GERADO("Gerado"), CANCELADO("Cancelado"), PENDENTE("Pendente"), PAGO("Pago");
	
	private String descricao;
	
	private Status(String des) {
		this.setDescricao(des);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
