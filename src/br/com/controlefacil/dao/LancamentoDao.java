package br.com.controlefacil.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.cc.dao.GenericDAO;
import br.com.controlefacil.model.Lancamento;
import br.com.controlefacil.model.UsuarioControle;

public class LancamentoDao extends GenericDAO<Lancamento> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void adicionarLancamento(Lancamento usuario) throws Exception {
        save(usuario);
    }
	
	public void salvarMerge(Lancamento usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void atualizarLancamento (Lancamento usuario) throws Exception {
        saveOrUpdate(usuario);
    }
    
    public void removerLancamento (Lancamento usuario) throws Exception {
        remove(usuario);
    }
    
    public List<Lancamento> getLancamentoPorUsuario(UsuarioControle us) throws Exception {
        return getList("select us from Lancamento us where us.usuario = ?", us);
    }
    
    public List<Lancamento> getLancamentos() throws Exception {
        return getList("select us from Lancamento us ");
    }
    
    public Lancamento getLancamento(Long usuario_id, String data, String descricao, BigDecimal valor) throws Exception {
    	return getPojo("select lan from Lancamento lan where lan.usuario.id = ? "
    			+ "and lan.descricao = ? and lan.dataLancamento = ? and valor = ?", 
    			usuario_id, descricao, data, valor);
    }
}
