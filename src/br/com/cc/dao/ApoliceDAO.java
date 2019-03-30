package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.apolice.view.ApoliceFiltro;
import br.com.cc.entidade.ApoliceBean;

public class ApoliceDAO  extends GenericDAO<ApoliceBean> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void adicionarApolice(ApoliceBean produtor) throws Exception {
        save(produtor);
    }

    public ApoliceBean atualizarApolice(ApoliceBean produtor) throws Exception {
        return saveOrUpdate(produtor);
    }

    public void removerApolice(ApoliceBean produtor) throws Exception {
        remove(produtor);
    }

    public ApoliceBean getApolice(int idProdutor) throws Exception {
        return getPojo(ApoliceBean.class, idProdutor);
    }

    public List<ApoliceBean> getApolices() throws Exception {
        return getList("select p from ApoliceBean p");
    }
    
    public ApoliceBean getNumeroApolice (String apolice) throws Exception {
    	return getPojo("select p from ApoliceBean p where "
    			+ "p.numeroApolice = ?1 ", 
    			apolice);
    }
    
    public List<ApoliceBean> getApolicePorSegurado (String segurado) throws Exception {
    	return getPojo("select p from ApoliceBean p where "
    			+ "p.segurado.segurado = ?1", 
    			segurado);
    }
    
    public List<ApoliceBean> getApolicePorPlaca (String placa) throws Exception {
    	return getPojo("select p from ApoliceBean p where "
    			+ "p.veiculo.placa = ?1", 
    			placa);
    }
    
    public List<ApoliceBean> getApoliceFiltro (ApoliceFiltro filtro) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	sql.append("select p from ApoliceBean p where ");
    	if (filtro != null && !filtro.getNumeroApolice().equals("")){
    		sql.append("p.numeroApolice like "+"'%"+filtro.getNumeroApolice()+"%'" + " and ");
    	}
    	if (filtro != null && !filtro.getSusep().equals("")){
    		sql.append("p.susep like "+"'%"+filtro.getSusep()+"%'" + " and ");
    	}
    	if (filtro != null && !filtro.getCorretor().equals("")){
    		sql.append("ucase(p.segurado.produtor) like "+"'%"+filtro.getCorretor().toUpperCase()+"%'"+  " and ");
    	}
    	if (filtro != null && !filtro.getMarcaMoldelo().equals("")){
    		sql.append("ucase(p.veiculo.marcaModelo) like "+"'%"+filtro.getMarcaMoldelo().toUpperCase()+"%'" + " and ");
    	}
    	if (filtro != null && !filtro.getPlaca().equals("")){
    		sql.append("ucase(p.veiculo.placa) like "+"'%"+filtro.getPlaca().toUpperCase()+"%'" + " and ");
    	}
    	if (filtro != null && filtro.getInicioVigencia() != null && filtro.getFimVigencia() != null){
    		sql.append("p.dataVigenciaInicio >= "+filtro.getInicioVigencia() 
    		+" p.dataTermino <= "+filtro.getFimVigencia() + " and ");
    	}
    	sql.append("1 = 1 order by p.dataTermino asc ");
    	
    	return getList(sql.toString());
    }
}
