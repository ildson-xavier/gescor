package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.VeiculoBean;

public class VeiculoDAO extends GenericDAO<VeiculoBean> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public void adicionarVeiculo(VeiculoBean produtor) throws Exception {
        save(produtor);
    }

    public VeiculoBean atualizarVeiculo(VeiculoBean produtor) throws Exception {
        return saveOrUpdate(produtor);
    }

    public void removerVeiculo(VeiculoBean produtor) throws Exception {
        remove(produtor);
    }

    public VeiculoBean getVeiculo(int idProdutor) throws Exception {
        return getPojo(VeiculoBean.class, idProdutor);
    }

    public List<VeiculoBean> getVeiculos() throws Exception {
        return getList("select p from VeiculoBean p");
    }
    
    public VeiculoBean getVeiculoPorPlaca (String placa) throws Exception {
    	return getPojo("select p from VeiculoBean p where "
    			+ "p.placa = ?1", 
    			placa);
    }
    
    public List<VeiculoBean> getVeiculoPorPlacas (String placa) throws Exception {
    	return getList("select p from VeiculoBean p where "
    			+ "p.placa like ?1", 
    			"%" + placa.toUpperCase() + "%");
    }

}
