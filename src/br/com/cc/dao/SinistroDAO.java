package br.com.cc.dao;

import java.io.Serializable;

import java.util.List;

import br.com.cc.entidade.sinistro.Filtro;
import br.com.cc.entidade.sinistro.Sinistro;
import br.com.cc.util.Util;

public class SinistroDAO extends GenericDAO<Sinistro> implements Serializable{

	private static final long serialVersionUID = 1L;

	public Sinistro salvar(Sinistro sinistro){
		return super.saveOrUpdate(sinistro);
	}
	
	public List<Sinistro> listarSinistros(){
		return super.findAll();
	}
	
	public List<Sinistro> listarSinistros(Filtro filtro){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s FROM Sinistro s ");
		sql.append("WHERE 1 = 1 ");
		
		if (filtro != null){
			if ((filtro.getDataFim() != null && !filtro.getDataFim().equals("")) &&
					(filtro.getDataInicio() != null && !filtro.getDataInicio().equals(""))){
				sql.append(" AND s.dataSinistro >= '").append(Util.converterDateParaString(filtro.getDataInicio())).append("'");
				sql.append(" AND s.dataSinistro <= '").append(Util.converterDateParaString(filtro.getDataFim())).append("'");
			}
			
			if (filtro.getSegurado() != null && !filtro.getSegurado().equals("")){
				sql.append(" AND s.produtor.segurado like '%").append(filtro.getSegurado()).append("%'");
				System.out.println(filtro.getSegurado());
			}

			if (filtro.getSeguradora() != null && !filtro.getSeguradora().equals("")){
				sql.append(" AND s.veiculo.seguradora like '%").append(filtro.getSeguradora()).append("%'");
				System.out.println(filtro.getSeguradora());
			}
		}

		return getList(sql.toString());
	}
	
	public Sinistro getSinistro(int id){
		return super.getPojo(Sinistro.class, id);
	}
	
	public void remover(Sinistro sinistro){
		super.remove(sinistro);
	}
}
