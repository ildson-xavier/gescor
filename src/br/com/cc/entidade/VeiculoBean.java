package br.com.cc.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class VeiculoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String marcaModelo;
	private String anoFabricacaoAnoModelo;
	private String placa;
	private String zeroKm;
	private String chassi;
	
	@OneToMany(mappedBy = "veiculo", cascade=CascadeType.ALL)
	private List<ApoliceBean> apolices = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public String getAnoFabricacaoAnoModelo() {
		return anoFabricacaoAnoModelo;
	}
	public void setAnoFabricacaoAnoModelo(String anoFabricacaoAnoModelo) {
		this.anoFabricacaoAnoModelo = anoFabricacaoAnoModelo;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getZeroKm() {
		return zeroKm;
	}
	public void setZeroKm(String zeroKm) {
		this.zeroKm = zeroKm;
	}
	public String getChassi() {
		return chassi;
	}
	public void setChassi(String chassi) {
		this.chassi = chassi;
	}
	public List<ApoliceBean> getApolices() {
		return apolices;
	}
	public void setApolices(List<ApoliceBean> apolices) {
		this.apolices = apolices;
	}
	@Override
	public String toString() {
		return "VeiculoBean [id=" + id + ", marcaModelo=" + marcaModelo + ", anoFabricacaoAnoModelo="
				+ anoFabricacaoAnoModelo + ", placa=" + placa + ", zeroKm=" + zeroKm + ", chassi=" + chassi
				+ ", apolices=" + apolices + "]";
	}

	
}
