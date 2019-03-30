package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.SubTipo;

public class SubTipoDAO extends GenericDAO<SubTipo> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void adicionarSubTipo (SubTipo subTipo) throws Exception {
		saveOrUpdate(subTipo);
	}
	
	public void atualizarSubTipo (SubTipo subTipo) throws Exception {
		saveOrUpdate(subTipo);
	}
	
	public void removerSubTipo (SubTipo subTipo) throws Exception {
		//remove(subTipo);
		remover(subTipo);
	}
	
	public SubTipo getSubTipo (int subTipoId) throws Exception {
		return getPojo(SubTipo.class, subTipoId);
	}
	
	public SubTipo getSubTipoPorDescricao (String desc, Categoria categoria) throws Exception {
		return getPojo("select c from SubTipo c where "
				+ "c.descricao = ?1 and c.categoria = ?2 ",desc, categoria);
	}
	
	public SubTipo getSubTipoPorDescricao (String desc) throws Exception {
		return getPojo("select c from SubTipo c where "
				+ "c.descricao = ?1 ",desc);
	}
	
	public List<SubTipo> getListSubTipoPorDescricao (String desc) throws Exception {
		return getList("select c from SubTipo c where "
				+ "c.descricao = ?1 ",desc);
	}
	
	public List<SubTipo> getSubTipos() throws Exception {
		return getList("select c from SubTipo c");
	}
	
	public List<SubTipo> getSubTiposPorDescricao(SubTipo subTipo) throws Exception {
		return getList("select c from SubTipo c where c.descricao = ?1 ",subTipo.getDescricao());
	}
	
	public List<SubTipo> getSubTiposPorCategoria(Categoria categoria) throws Exception {
		return getList("select c from SubTipo c where c.categoria = ?1 ",categoria);
	}
	
	public SubTipo getSubTiposPorIdECategoria(int subTipoId, Categoria categoria) throws Exception {
		return getPojo("select c from SubTipo c where c.categoria = ?1 and c.id = ?2",categoria, subTipoId);
	}
}
