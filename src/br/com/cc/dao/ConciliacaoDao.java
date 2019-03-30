package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.Resultado;

public class ConciliacaoDao extends GenericDAO<Lancamentos> implements Serializable{

	private static final long serialVersionUID = 1L;

	private ConciliacaoDao() {
		super();		
	}
	
	public static ConciliacaoDao Criar(){
		return new ConciliacaoDao();
	}
	
	public List<Object[]> getGraficoPorProdutorDonut(String mesAno, Categoria produtor) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), sum(l.valor3), date_format(l.periodo, '%M/%Y') "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%m/%Y') = ?1 "
                + "and (l.produtor like ?2 or l.categoria = ?3 ) "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                , mesAno, "%" + produtor.getTipo() + "%", produtor);
    }
	
	public List<Object[]> getGraficoPorProdutorBarras(String ano, Categoria produtor) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), sum(l.valor3), date_format(l.periodo, '%M/%Y') "
        		+ "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "and (l.produtor like ?2 or l.categoria = ?3 ) "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                + "order by l.periodo asc "
                , ano, "%" + produtor.getTipo() + "%", produtor);
    }
	
	public List<Object[]> getGraficoDonutDialogProduto(String mesAno, Categoria produtor) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), sum(l.valor3), date_format(l.periodo, '%M/%Y') "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%M/%Y') = ?1 "
                + "and (l.produtor like ?2 or l.categoria = ?3 ) "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                , mesAno, "%" + produtor.getTipo() + "%", produtor);
    }
	
	/**
	 * 
	 * @param mesAno
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getGraficoDonut(String mesAno) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), date_format(l.periodo, '%M/%Y') "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%m/%Y') = ?1 "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                , mesAno);
    }
	
	public List<Object[]> getGraficoDonutDialog(String mesAno) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), date_format(l.periodo, '%M/%Y') "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%M/%Y') = ?1 "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                , mesAno);
    }
	
	public List<Object[]> getGraficoBarras(String ano) throws Exception {
        return getList("select l.classificacao, sum(l.comissao), date_format(l.periodo, '%M/%Y') "
                + "from Lancamentos l "
                + "where date_format(l.periodo, '%Y') = ?1 "
                + "group by l.classificacao, date_format(l.periodo, '%M/%Y') "
                + "order by l.periodo asc "
                , ano);
    }
	
	public List<Resultado> getResultadoConciliacaoGeral(Long anoMes, Long anoMesAte, String produtor) throws Exception {
		String query = "SELECT r FROM Resultado r ";
		if ((anoMes != null && anoMes > 0 && (produtor == null || produtor.length() == 0)) && 
				(anoMesAte != null && anoMesAte > 0 && (produtor == null || produtor.length() == 0))){
			query += "where r.sequencia >= ?1 ";
			query += "and r.sequencia <= ?2 ";
			return getList(query, anoMes, anoMesAte);
			
		} else if ((anoMes != null && anoMes > 0 && (produtor != null && produtor.length() > 0)) && 
				(anoMesAte != null && anoMesAte > 0 && (produtor != null && produtor.length() > 0))){
			query += "where r.sequencia >= ?1 ";
			query += "and r.sequencia <= ?2 ";
			query += "and r.produtor like ?3 ";
			return getList(query, anoMes, anoMesAte, "%"+produtor+"%");
		} else {
			return getList(query);
		}
		
		
	}
}
