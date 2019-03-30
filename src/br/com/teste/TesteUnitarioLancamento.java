package br.com.teste;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;

public class TesteUnitarioLancamento {

	/**
	 * Teste unitário para o cadastro de lançamentos de comissão
	 * @throws Exception
	 */
	@Test
	public void tesValorComissão() {

		SubTipo sub = new SubTipo();
		sub.setDescricao("Alugule");
		sub.setId(1);
		Categoria categoria = new Categoria();
		categoria.setTipo("Corretora");
		sub.setCategoria(categoria);
		
		Lancamentos lanc = new Lancamentos();
		lanc.setApolice(0);

		lanc.setComissao((double) 300);
		lanc.calcularPercentualEPremioLiquido(8.1);
		lanc.atualizarValores();
		lanc.setContador((long) 3);
		lanc.setDataIncluisao(Calendar.getInstance().getTime());
		lanc.setHistorico("Teste unitário");
		lanc.setSubTipo(sub);
		lanc.setCategoria(sub.getCategoria());
		
		System.out.println(lanc.toString());
		
		assertEquals(new Double(300), lanc.getComissao());
	}
	
	@Test
	public void testValorImposto(){

		SubTipo sub = new SubTipo();
		sub.setDescricao("Alugule");
		sub.setId(1);
		Categoria categoria = new Categoria();
		categoria.setTipo("Corretora");
		sub.setCategoria(categoria);
		
		Lancamentos lanc = new Lancamentos();
		lanc.setApolice(0);

		lanc.setComissao((double) 300);
		lanc.calcularPercentualEPremioLiquido(8.1);
		lanc.atualizarValores();
		lanc.setContador((long) 3);
		lanc.setDataIncluisao(Calendar.getInstance().getTime());
		lanc.setHistorico("Teste unitário");
		lanc.setSubTipo(sub);
		lanc.setCategoria(sub.getCategoria());

		assertEquals(new Double(24.3), lanc.getValorImposto());
	}
	
	@Test
	public void testValorLiquido(){

		SubTipo sub = new SubTipo();
		sub.setDescricao("Alugule");
		sub.setId(1);
		Categoria categoria = new Categoria();
		categoria.setTipo("Corretora");
		sub.setCategoria(categoria);
		
		Lancamentos lanc = new Lancamentos();
		lanc.setApolice(0);

		lanc.setComissao((double) 300);
		lanc.calcularPercentualEPremioLiquido(8.1);
		lanc.atualizarValores();
		lanc.setContador((long) 3);
		lanc.setDataIncluisao(Calendar.getInstance().getTime());
		lanc.setHistorico("Teste unitário");
		lanc.setSubTipo(sub);
		lanc.setCategoria(sub.getCategoria());
		
		assertEquals(new Double(275.7), lanc.getValorLiquido());
	}

}
