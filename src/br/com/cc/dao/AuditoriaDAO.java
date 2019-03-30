package br.com.cc.dao;

import java.util.Date;
import java.util.List;

import br.com.cc.entidade.Auditoria;

public class AuditoriaDAO extends GenericDAO<Auditoria> {

	public Auditoria adicionarAuditoria(Auditoria auditoria) throws Exception {
		auditoria = saveOrUpdate(auditoria);
		return auditoria;
	}

	public List<Auditoria> buscarAuditoria() throws Exception {
		return getList("select c from Auditoria c order by c.dataAlteracao desc");
	}

	public List<Auditoria> buscarAuditoria(Date inicio, Date fim) throws Exception {
		return getList(
				"select c from Auditoria c where c.dataAlteracao > ? and c.dataAlteracao < ? order by c.dataAlteracao desc",
				inicio, fim);
	}
}
