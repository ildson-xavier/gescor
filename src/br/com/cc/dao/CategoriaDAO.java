package br.com.cc.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.SubTipo;

public class CategoriaDAO extends GenericDAO<Categoria> implements Serializable {

	private static final long serialVersionUID = 1L;

	public void adicionarCategoria(Categoria categoria) throws Exception {
		save(categoria);
	}

	public void atualizarCategoria(Categoria categoria) throws Exception {
		saveOrUpdate(categoria);
	}

	public void removerCategoria(Categoria categoria) throws Exception {
		//remove(categoria);
		remover(categoria);
	}

	public Categoria getCategoria(int categoriaId) throws Exception {
		return getPojo(Categoria.class, categoriaId);
	}

	public List<Categoria> getCategorias() throws Exception {
		return getList("select c from Categoria c ");
	}

	public List<Categoria> getCategoriasPorDataECategoria(String tipo, Date inicio, Date fim) throws Exception {
		return getList("select c from Categoria c join c.lancamentos l "
				+ "where c.tipo = ?1 and l.periodo >= ?2 and l.periodo <= ?3 "
				+ "order by l.periodo desc ", tipo, inicio, fim);
	}

	public List<Categoria> getCategoriasPorData(Date inicio, Date fim) throws Exception {
		return getList("select distinct c from Categoria c join fetch c.lancamentos l "
				+ "where l.periodo between ?1 and ?2 " 
				+ "order by l.periodo desc ", inicio, fim);
	}
	
	public List<Categoria> getCategoriasPorDataESubTipo(Date inicio, Date fim, String tipo) throws Exception {
		return getList("select distinct c from Categoria c join fetch c.lancamentos l "
				+ "where l.periodo between ?1 and ?2 "
				+ "and l.subTipo.descricao like ?3 " 
				+ "order by l.periodo desc", inicio, fim, "%" + tipo.toUpperCase() + "%");
	}
	
	public List<Categoria> getCategoriasPorSubTipo(String tipo) throws Exception {
		return getList("select distinct c from Categoria c join fetch c.lancamentos l "
				+ "where "
				+ "l.subTipo.descricao like ?1 " 
				+ "order by l.periodo desc ", "%" + tipo.toUpperCase() + "%");
	}

	public List<Categoria> getListCategoriasPorTipo(Categoria categoria) throws Exception {
		return getList("select c from Categoria c where c.tipo = ?1 ", categoria.getTipo());
	}

	public Categoria getCategoriasPorTipo(String categoria) throws Exception {
		return getPojo("select c from Categoria c where c.tipo like ?1 ", "%" + categoria.toUpperCase() + "%");
	}

	public List<Categoria> getCategoriasPorSubTipo(SubTipo subTipo) throws Exception {
		return getList("select c from Categoria c where c.categoria = ?1 ", subTipo.getCategoria());
	}

	public List<SubTipo> getSubTipoPorDescricao(SubTipo subTipo) throws Exception {
		return getList("select c from SubTipo c where c.descricao = ?1 ", subTipo.getDescricao());
	}

	public SubTipo getSubTipoPorId(int subTipoId) throws Exception {
		return getPojo(SubTipo.class, subTipoId);
	}

	/**
	 * Busca para tela de resumo
	 */
	
	public List<Object[]> getCategoriasGraficoTipo(String tipo) throws Exception {
		return getList("select l.categoria.tipo, sum(l.comissao), count(*) from Lancamentos l "
				+ "where l.categoria.tipo like ?1 "
				+ "and l.categoria.id is not null "
				+ "group by l.categoria.tipo ", "%"+ tipo.toUpperCase() +"%");
	}
	
	public List<Object[]> getCategoriasGrafico(String ano) throws Exception {
		return getList("select l.categoria.tipo, sum(l.comissao), count(*) from Lancamentos l "
				+ "where date_format(l.periodo, '%Y') = ?1 "
				+ "and l.categoria.id is not null "
				+ "group by l.categoria.tipo ", ano);
	}

	public List<Object[]> getCategoriasGraficoData(String ano) throws Exception {
		return getList("select date_format(l.periodo, '%M/%Y'), sum(l.comissao), count(*) from Lancamentos l "
				      + "where date_format(l.periodo, '%Y') = ?1  "
				      + "and l.categoria.id is not null "
				+ "group by date_format(l.periodo, '%M/%Y') "
				+ "order by l.periodo asc", ano);
	}
	//s.custo, date_format(l.periodo, '%M/%Y'), sum(l.comissao), sum(l.valor3)
	public List<Object[]> getCategoriasGraficoDataCusto(String ano)
			throws Exception {
		return getList(
				"select l.periodo "
				        + "from Lancamentos l "
				        + " where l.subTipo.id is not null ");
//				        + "where exists ( "
//				        + "select s from SubTipo s where s.lancamentos = l ) ");
						/*+ "where date_format(l.periodo, '%Y') = ?1  "
						+ "and l.categoria.id is not null "
						+ "group by s.custo,  date_format(l.periodo, '%M/%Y') "
						+ "order by l.periodo, s.custo",
						ano);*/
		
		
//		"SELECT e FROM Professor e WHERE EXISTS "
//        + "(SELECT p FROM Phone p WHERE p.employee = e)"
		
		
	}
	
	public List<Object[]> getCategoriasGrafico(Date dtInicial, Date dtFinal) throws Exception {
		return getList("select l.categoria.tipo, sum(l.comissao), count(*) from Lancamentos l "
				      + "where l.periodo between ?1 and ?2 "
				      + "and l.categoria.id is not null "
				+ "group by l.categoria.tipo", dtInicial, dtFinal);
	}

	public List<Object[]> getCategoriasGrafico(Date dtInicial, Date dtFinal, String tipo) throws Exception {
		return getList("select l.categoria.tipo, sum(l.comissao), count(*) from Lancamentos l "
				+ "where l.periodo between ?1 and ?2 " 
				+ "and l.categoria.tipo like ?3 "
				+ "and l.categoria.id is not null "
				+ "group by l.categoria.tipo", dtInicial, dtFinal, "%"+tipo.toUpperCase()+"%");
	}

	public List<Object[]> getCategoriasPorSubTipoGrafico(Date dtInicial, Date dtFinal, String tipo, String sub)
			throws Exception {
		return getList(
				"select l.subTipo.descricao, sum(l.comissao), count(*) from Categoria c "
						+ "c join fetch c.lancamentos l " 
						+ "where c.periodo between ?1 and ?2 " + "and c.tipo = ?3 "
						+ "and l.subTipo.descricao = ?4"
						+ "group by l.subTipo.descricao ",
				dtInicial, dtFinal, tipo, sub);
	}

	public List<Object[]> getCategoriasPorSubTipoGraficoDescr(String desc) throws Exception {
		return getList(
				"select l.subTipo.descricao, sum(l.comissao), count(*) from Lancamentos l "
						 + "where l.subTipo.descricao like ?1 "
						 + "and l.subTipo.id is not null "
						 + "group by l.subTipo.descricao, l.subTipo.descricao ", "%"+ desc.toUpperCase() +"%");
		}
	
	public List<Object[]> getCategoriasPorSubTipoGrafico(String ano) throws Exception {
		return getList(
				"select l.subTipo.descricao, sum(l.comissao), count(*) from Lancamentos l "
						 + "where date_format(l.periodo, '%Y') = ?1 "
						 + "and l.subTipo.id is not null "
						 + "group by l.subTipo.descricao ", ano);
	}

	public List<Object[]> getCategoriasPorSubTipoGrafico(Date dtInicial, Date dtFinal) throws Exception {
		return getList(
				"select l.subTipo.descricao, sum(l.comissao), count(*) from Lancamentos l "
						+ "where l.periodo between ?1 and ?2 " 
						+ "and l.subTipo.id is not null "
						+ "group by l.subTipo.descricao ",
				dtInicial, dtFinal);
	}

	public List<Object[]> getCategoriasPorSubTipoGrafico(Date dtInicial, Date dtFinal, String desc) throws Exception {
		return getList(
				"select l.subTipo.descricao,  sum(l.comissao), count(*) from Lancamentos l "
					    + "where l.periodo between ?1 and ?2 "
						+ "and l.subTipo.descricao like ?3 "
						+ "and l.subTipo.id is not null "
						+ "group by l.subTipo.descricao ",
				dtInicial, dtFinal, "%"+ desc.toUpperCase() +"%");
	}
}
