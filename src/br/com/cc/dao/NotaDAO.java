package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Nota;

public class NotaDAO extends GenericDAO<Nota> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void salvar(Nota nota) throws Exception {
		super.saveOrUpdate(nota);
	}
	
	public List<Nota> buscarNotas() throws Exception {
		return super.getList("select n from Nota n order by n.id  desc ");
	}
	
	public List<Nota> buscarNotaPorSeguradora(String seguradora) throws Exception {
		return super.getList("select n from Nota n where n.seguradora = ? order by n.id desc ", seguradora);
	}
	
	public List<Nota> buscarNotaPorPeriodo(String periodo) throws Exception {
		return super.getList("select n from Nota n where n.periodo = ? order by n.id desc", periodo);
	}
	
	public List<Nota> buscarNotaPorPeriodoESeguradora(String periodo, String seguradora) throws Exception {
		return super.getList("select n from Nota n where n.periodo = ? and n.seguradora = ? order by n.id desc", periodo, seguradora);
	}
	
	public Nota buscarNota(String periodo, String seguradora) throws Exception {
		return super.getPojo("select n from Nota n where n.periodo = ? and n.seguradora = ? order by n.id desc", periodo, seguradora);
	}
	
	public void deletar(Nota nota) throws Exception{
		super.remove(nota);
	}

}
