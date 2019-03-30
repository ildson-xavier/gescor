/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.cc.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Usuario
 */
@Entity
public class Lancamentos implements Serializable{
    

	private static final long serialVersionUID = 5521639644548637413L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String historico; //segurados
    private String produtor;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
    private String seguradora;
    private Double valor1; 
    private Double valor2; // Taxa
    private Double comissao;
    private String mesAno;
    private Integer parcela;
    private String tipo; //
    private String descricao;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataIncluisao;
    private Double valor3; // comissao liquida
    private Integer apolice;
    private Integer situacao; // 0 - inicial; 1 - novo; 2 - renovacao
    private Long contador;
    private String status; //Pago; Pendente; Agendado.
    private Integer classificacao; // 1 - receita; 2 - despesa
    private String cpf;
    private String tipoPessoa;
    
    @Oculto
    private Double valorBruto;
    @Oculto
    private Double valorImposto;
    @Oculto
    private Double valorLiquido;
    @Oculto
    private Double valorDespesa;
     
    
    
    public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public Double getValorImposto() {
		return valorImposto;
	}

	public void setValorImposto(Double valorImposto) {
		this.valorImposto = valorImposto;
	}

	public Double getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	//@ManyToOne(cascade={CascadeType.MERGE})
    @ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
    private Categoria categoria;
    
    //@ManyToOne(cascade={CascadeType.MERGE})
    @ManyToOne
    @JoinColumn(referencedColumnName="id",columnDefinition="integer")
    private SubTipo subTipo;
    

    public Integer getApolice() {
        return apolice;
    }

    public void setApolice(Integer apolice) {
        this.apolice = apolice;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }        

    public String getMesAno() {
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }        

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
    }
    
    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = produtor;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }    

    public Double getValor1() {
        return valor1;
    }

    public void setValor1(Double valor1) {
        this.valor1 = valor1;
    }

    public Double getValor2() {
        return valor2;
    }

    public void setValor2(Double valor2) {
        this.valor2 = valor2;
    }

    public Double getComissao() {
        return comissao;
    }

    public void setComissao(Double comissao) {
        this.comissao = comissao;
    }

    public Integer getParcela() {
        return parcela;
    }

    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataIncluisao() {
        return dataIncluisao;
    } 

    public void setDataIncluisao(Date dataIncluisao) {
        this.dataIncluisao = dataIncluisao;
    }

    public Double getValor3() {
        return valor3;
    }

    public void setValor3(Double valor3) {
        this.valor3 = valor3;
    }

    public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	
	public SubTipo getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(SubTipo subTipo) {
		this.subTipo = subTipo;
	}

	@Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 19 * hash + (this.historico != null ? this.historico.hashCode() : 0);
        hash = 19 * hash + (this.periodo != null ? this.periodo.hashCode() : 0);
        hash = 19 * hash + (this.seguradora != null ? this.seguradora.hashCode() : 0);
        hash = 19 * hash + (this.valor3 != null ? this.valor3.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lancamentos other = (Lancamentos) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.historico == null) ? (other.historico != null) : !this.historico.equals(other.historico)) {
            return false;
        }
        if (this.periodo != other.periodo && (this.periodo == null || !this.periodo.equals(other.periodo))) {
            return false;
        }
        if ((this.seguradora == null) ? (other.seguradora != null) : !this.seguradora.equals(other.seguradora)) {
            return false;
        }
        if (this.valor3 != other.valor3 && (this.valor3 == null || !this.valor3.equals(other.valor3))) {
            return false;
        }
        return true;
    }

	public Long getContador() {
		return contador;
	}

	public void setContador(Long contador) {
		this.contador = contador;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Double getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(Double valorDespesa) {
		this.valorDespesa = valorDespesa;
	}
	
	public void calcularPercentualEPremioLiquido(Double percent) {
		this.setValor2((percent * this.getComissao()) / 100);
		this.setValor3(this.getComissao() - (this.getValor2()));
	}
	
	public void atualizarValores() {
		this.setValorBruto(this.getComissao());
		this.setValorImposto(this.getValor2());
		this.setValorDespesa(this.getValor1());
		this.setValorLiquido(this.getValor3());
	}

	@Override
	public String toString() {
		return "Lancamentos [id=" + id + ", historico=" + historico + ", produtor=" + produtor + ", periodo=" + periodo
				+ ", seguradora=" + seguradora + ", valor1=" + valor1 + ", valor2=" + valor2 + ", comissao=" + comissao
				+ ", mesAno=" + mesAno + ", parcela=" + parcela + ", tipo=" + tipo + ", descricao=" + descricao
				+ ", dataIncluisao=" + dataIncluisao + ", valor3=" + valor3 + ", apolice=" + apolice + ", situacao="
				+ situacao + ", contador=" + contador + ", status=" + status + ", classificacao=" + classificacao
				+ ", cpf=" + cpf + ", tipoPessoa=" + tipoPessoa + ", valorBruto=" + valorBruto + ", valorImposto="
				+ valorImposto + ", valorLiquido=" + valorLiquido + ", valorDespesa=" + valorDespesa + ", categoria="
				+ categoria + ", subTipo=" + subTipo + "]";
	}
   
    
}
