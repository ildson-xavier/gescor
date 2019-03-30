package br.com.teste;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Test;

import br.com.cc.dao.CategoriaDAO;
import br.com.cc.dao.SubTipoDAO;
import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.SubTipo;
import junit.framework.TestCase;

public class TestSubTipo extends TestCase {

	@Test
	public void test() throws Exception {

		/*CategoriaDAO cao = new CategoriaDAO();
		Categoria ca = new Categoria(); // ca.setId(0); ca.setStatus(true);
		ca = cao.getCategoria(39);
		//ca.setTipo("IXM");

		SubTipo su = new SubTipo(); // su.setId(0);
		su.setDescricao("Novo");

		SubTipo su2 = new SubTipo(); // su2.setId(0);
		su2.setDescricao("Novo");
		su2.setCategoria(ca);

		ca.addSubTipo(su);
		ca.addSubTipo(su2);

		
		SubTipoDAO sao = new SubTipoDAO();
		cao.adicionarCategoria(ca);
		for (SubTipo s : ca.getSubTipo()) {
			sao.adicionarSubTipo(s);

		} // sao.adicionarSubTipo(su);*/

		SubTipoDAO sao = new SubTipoDAO();
		SubTipo sub = new SubTipo();
		
		sub = sao.getSubTipo(81);
		System.out.println("Descricao: "+sub.getDescricao());
		
		sao.removerSubTipo(sub);
		
		float num = 1;
		float num2 = 1;

		assertEquals(num, num2);
	}

	@Test
	public void testListCategoria() throws Exception {
		/*CategoriaDAO cdao = new CategoriaDAO();
		List<Categoria> cas = cdao.getCategorias();

		for (Categoria c : cas) {
			System.out.println("Id: " + c.getId());
			System.out.println("Tipo: " + c.getTipo());
			for (SubTipo s : c.getSubTipo()) {
				System.out.println("Id: " + s.getId() + " Descricao: " + s.getDescricao() + " Tipo: "
						+ s.getCategoria().getTipo());
			}
		}*/

		// Categoria cat = cdao.getCategoria(3);

		/*
		 * for (SubTipo s : cat.getSubTipo()) { System.out.println( "Id: " +
		 * s.getId() + " Descricao: " + s.getDescricao() + " Tipo: " +
		 * s.getCategoria().getTipo()); }
		 */

		//assertNotNull(cas);

	}

	@Test
	public void testListSubTipo() throws Exception {
		/*
		 * CategoriaDAO cdao = new CategoriaDAO(); Categoria cat =
		 * cdao.getCategoria(1); SubTipoDAO sdao = new SubTipoDAO();
		 * 
		 * SubTipo s = sdao.getSubTiposPorIdECategoria(20, cat);
		 * System.out.println("Id: "+ s.getId() + " Descricao: "+
		 * s.getDescricao() + " Tipo: "+s.getCategoria().getTipo());
		 * 
		 * List<SubTipo> sub = sdao.getSubTiposPorCategoria(cat);
		 * 
		 * for (SubTipo s : sub){ System.out.println("Id: "+ s.getId() +
		 * " Descricao: "+ s.getDescricao() + " Tipo: "
		 * +s.getCategoria().getTipo()); } assertNotNull(s);
		 */
	}

}
