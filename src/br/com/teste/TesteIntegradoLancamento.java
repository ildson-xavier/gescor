package br.com.teste;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.cc.dao.CategoriaDAO;
import br.com.cc.dao.LancamentosDao;
import br.com.cc.dao.SubTipoDAO;
import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;

public class TesteIntegradoLancamento {

	/**
	 * Teste integrado para o cadastro de lançamentos de comissão
	 * @throws Exception
	 */
	@Test
	public void tesValorComissão() throws Exception {

		LancamentosDao ldao = new LancamentosDao();
		CategoriaDAO cdao = new CategoriaDAO();
		Categoria cat = cdao.getCategoria(1);
		SubTipoDAO sdao = new SubTipoDAO();
		SubTipo sub = sdao.getSubTiposPorIdECategoria(117, cat);
		
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
	public void testValorImposto() throws Exception{

		LancamentosDao ldao = new LancamentosDao();
		CategoriaDAO cdao = new CategoriaDAO();
		Categoria cat = cdao.getCategoria(1);
		SubTipoDAO sdao = new SubTipoDAO();
		SubTipo sub = sdao.getSubTiposPorIdECategoria(117, cat);
		
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
	public void testValorLiquido() throws Exception{

		LancamentosDao ldao = new LancamentosDao();
		CategoriaDAO cdao = new CategoriaDAO();
		Categoria cat = cdao.getCategoria(1);
		SubTipoDAO sdao = new SubTipoDAO();
		SubTipo sub = sdao.getSubTiposPorIdECategoria(117, cat);
		
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
