/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.dao;

import br.com.cc.entidade.Lancamentos;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class GraficoDao extends GenericDAO<Lancamentos> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Object[]> getGraficoSeguradora(String ano) throws Exception {
        return getList("select count(*), l.seguradora, date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and l.categoria is null "
                + "group by l.seguradora, date_format(l.periodo, '%M/%Y') "
                + "order by date_format(l.periodo, '%m/%Y'), l.seguradora asc ", ano);
    }

    public List<Object[]> getGraficoSeguradoraCrescimento(String ano) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y') "
                + "order by l.periodo asc ", ano);
    }

    public List<Object[]> getGraficoSeguradoraFaturamento(String ano) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y') "
                + "order by l.periodo asc ", ano);
    }

    public List<Object[]> getGraficoProdutorFaturamento(String ano, String produtor) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and l.categoria is null "
                + "and ucase(l.produtor) like ?2 "
                + "group by date_format(l.periodo, '%M/%Y') "
                + "order by l.periodo asc ", ano, "%" + produtor + "%");
    }

    public List<String> getProdutores(String ano) throws Exception {
        return getList("select distinct l.produtor "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and l.categoria is null "
                + "and l.produtor is not null "
                + "order by l.produtor asc ", ano);
    }
    
    /**
     * Busca detalhada 
     */

    public List<Object[]> getGraficoSeguradorasPizza_novo(String seguradora, String periodo){
    	return getList("select count(*), l.seguradora, date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%M/%Y') = ?1 "
                + "and l.categoria is null "
                + "and l.seguradora = ?2 "
                + "and l.situacao = 1 "
                + "group by l.seguradora, date_format(l.periodo, '%M/%Y') "
                + "order by date_format(l.periodo, '%m/%Y'), l.seguradora asc ", periodo, seguradora);
    }
    
    public List<Object[]> getGraficoSeguradorasPizza_renovacao(String seguradora, String periodo){
    	return getList("select count(*), l.seguradora, date_format(l.periodo, '%M/%Y'), sum(l.valor3) "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%M/%Y') = ?1 "
                + "and l.categoria is null "
                + "and l.seguradora = ?2 "
                + "and l.situacao = 2 "
                + "group by l.seguradora, date_format(l.periodo, '%M/%Y') "
                + "order by date_format(l.periodo, '%m/%Y'), l.seguradora asc ", periodo, seguradora);
    }
}
