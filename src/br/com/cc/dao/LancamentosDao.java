/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cc.dao;

import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;
import br.com.cc.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author Usuario
 */
public class LancamentosDao extends GenericDAO<Lancamentos> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Lancamentos adicionarLancamento(Lancamentos lancamentos) throws Exception {
        //save(lancamentos);
		lancamentos.setValorBruto(lancamentos.getComissao());
		lancamentos.setValorImposto(lancamentos.getValor2());
		lancamentos.setValorDespesa(lancamentos.getValor1());
		lancamentos.setValorLiquido(lancamentos.getValor3());
        saveOrUpdate(lancamentos);
        return lancamentos;
    }

    public void atualizarLancamento(Lancamentos lancamentos) throws Exception {
		lancamentos.setValorBruto(lancamentos.getComissao());
		lancamentos.setValorImposto(lancamentos.getValor2());
		lancamentos.setValorDespesa(lancamentos.getValor1());
		lancamentos.setValorLiquido(lancamentos.getValor3());
        saveOrUpdate(lancamentos);
    }

    public void removerLancamento(Lancamentos lancamentos) throws Exception {
        remove(lancamentos);
    }

    public Lancamentos getLancamento(int id) throws Exception {
    	Lancamentos lanc = getPojo(Lancamentos.class, id);
        return lanc;
    }
   
	
    public List<Lancamentos> getLancamentosPorPeriodoESeguradora(String periodo, String seguradora) throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria is null "
                + "and date_format(lan.periodo, '%m/%Y') = ?"
                + "and seguradora = ? "
                + "order by lan.periodo", periodo, seguradora);
    }
    
    public List<Object[]> getLancamentosAgrupadoPorPeriodoEProdutor(String periodo, String produtor) throws Exception {
        return getList("Select lan.produtor, sum(lan.comissao) from Lancamentos lan where lan.categoria is null "
                + "and date_format(lan.periodo, '%m/%Y') = ?"
                + "and produtor = ? "
                + "group by  lan.produtor "
                + "order by lan.periodo", periodo, produtor);
    }
    
    public List<Lancamentos> getLancamentosPorPeriodo(String periodo) throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria is null "
                + "and date_format(lan.periodo, '%m/%Y') = ?"
                + "order by lan.periodo", periodo);
    }

    public List<Lancamentos> existeDespesa(Date date, SubTipo subTipo) throws Exception, NoResultException{
    	Calendar cal = Calendar.getInstance();
    	
    	String mesAno = "";
    	cal.setTime(date);
    	List<Lancamentos> lanc = new LinkedList<>();
    	
    	mesAno = mesAno.concat(String.valueOf(cal.get(Calendar.MONTH) + 1)).concat("/").
				concat(String.valueOf(cal.get(Calendar.YEAR)));
    	
    	mesAno = Util.ajustarData(mesAno);
    	//System.out.println("MesAno: "+mesAno);
    	//System.out.println("SubTipo: "+subTipo.getDescricao());
    	
    	lanc = getList("select l "
                + "from Lancamentos l "
                + "where l.categoria is not null "
                + "and date_format(l.periodo, '%m/%Y') = ?1 "
                + "and l.subTipo = ?2", mesAno, subTipo);
    	return lanc;
    }
    
    public Boolean existeLancamento(Date dtLancamento, String seguradora) throws NoResultException, Exception {
        List<Lancamentos> lanc = new LinkedList<Lancamentos>();
        lanc = getList("select l from Lancamentos l where l.periodo = ?1 "
                + "and l.seguradora = ?2 "
                + "and l.categoria is null", dtLancamento, seguradora);
        return lanc.isEmpty();
    }
    
    public Boolean existeLancamento(String segurado, Date dtLancamento, String seguradora, Double valor) throws NoResultException, Exception {
        List<Lancamentos> lanc = new LinkedList<Lancamentos>();
        lanc = getList("select l from Lancamentos l where l.periodo = ?1 "
                + "and l.seguradora = ?2 "
                + "and l.comissao = ?3 "
                + "and l.historico = ?4 "
                + "and l.categoria is null ", 
                dtLancamento, seguradora, valor, segurado);
        return lanc.isEmpty();
    }
    
    public void getLancamentosParaRemover() throws Exception {
    	List<Lancamentos> lanc = getList("Select lan from Lancamentos lan where lan.categoria.id is not null "
                + "and lan.dataIncluisao = ?1 ", Calendar.getInstance().getTime());
    	
    	for (Lancamentos l : lanc){
    		removerLancamento(l);
    	}
    }

    /*
     * Buscas para o relatorio Analitico
     */
    public List<Lancamentos> getLancamentosAnalitico() throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria is null "
                + "order by lan.periodo");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorPerido(Date data, String seguradora) throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria is null "
        		+ "and lan.periodo = ?1 "
        		+ "and lan.seguradora = ?2 "
                + "order by lan.periodo desc", data, seguradora);
    }
    
    public List<Lancamentos> getLancamentosAnaliticoCpf(String cpf) throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria is null "
        		+ "and replace(replace(replace(replace(lan.cpf, '.',''),'.',''),'-',''),'/','') = ? "
                + "order by lan.periodo", cpf.replace(".", "").replace(".", "").replace("-", "").replace("/", ""));
    }

    public List<Lancamentos> getLancamentosAnaliticoPorData(Date dtInicial, Date dtFinal) throws Exception {
        return getList("select lan from Lancamentos lan "
        		+ "where lan.periodo >= ?1 and lan.periodo <= ?2 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal);
    }

    public List<Lancamentos> getLancamentosAnaliticoPorDataProdutor(Date dtInicial, Date dtFinal, String produtor) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.produtor) like ?3 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + produtor.toUpperCase() + "%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorProdutor(String produtor) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
                + "and ucase(lan.produtor) like ?1 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + produtor.toUpperCase() + "%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorDataHistorico(Date dtInicial, Date dtFinal, String historico) throws Exception {
        return getList("select lan from Lancamentos lan "
        		+ "where lan.periodo >= ?1 and lan.periodo <= ?2 "
        		+ "and lan.historico like ?3 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%"+historico.toUpperCase()+"%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorDataProdutorSeguradoraHistorico(Date dtInicial, Date dtFinal, String produtor, String seguradora, String historico) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.produtor) like ?3 and "
                + "ucase(lan.seguradora) like ?4 "
                + "and lan.historico like ?5 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + produtor.toUpperCase() + "%",
                "%" + seguradora.toUpperCase() + "%",
                "%"+historico.toUpperCase()+"%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorDataProdutorHistorico(Date dtInicial, Date dtFinal, String produtor, String historico) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.produtor) like ?3 "
                + "and lan.historico like ?4 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + produtor.toUpperCase() + "%",
                "%"+historico.toUpperCase()+"%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorDataSHistorico(Date dtInicial, Date dtFinal, String produtor, String historico) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.produtor) like ?3 "
                + "and lan.historico like ?4 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + produtor.toUpperCase() + "%",
                "%"+historico.toUpperCase()+"%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorDataSeguradoraHistorico(Date dtInicial, Date dtFinal, String seguradora, String historico) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.seguradora) like ?3 "
                + "and lan.historico like ?4 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + seguradora.toUpperCase() + "%",
                "%"+historico.toUpperCase()+"%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorSeguradoraHistorico(String seguradora, String historico) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
                + "and ucase(lan.seguradora) like ?1 "
                + "and lan.historico like ?2 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + seguradora.toUpperCase() + "%"+"%"+historico.toUpperCase()+"%" );
    }

    public List<Lancamentos> getLancamentosAnaliticoPorSegurado(String nome) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
        		+ "and lan.historico like ?1 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + nome.toUpperCase() + "%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorSeguradoEProdutor(String nome, String produtor) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
        		+ "and lan.historico like ?1 "
        		+ "and ucase(lan.produtor) like ?2 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + nome.toUpperCase() + "%",
                "%" + produtor.toUpperCase() + "%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorSeguradoESeguradora(String nome, String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
        		+ "and lan.historico like ?1 "
        		+ "and ucase(lan.seguradora) like ?2 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + nome.toUpperCase() + "%",
                "%" + seguradora.toUpperCase() + "%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorSeguradoESeguradoraEProdutor(String nome, String produtor, String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
        		+ "and lan.historico like ?1 "
        		+ "and ucase(lan.produtor) like ?2 "
        		+ "and ucase(lan.seguradora) like ?3 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + nome.toUpperCase() + "%",
                "%" + produtor.toUpperCase() + "%",
                "%" + seguradora.toUpperCase() + "%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorDataProdutorSeguradora(Date dtInicial, Date dtFinal, String produtor, String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.produtor) like ?3 and "
                + "ucase(lan.seguradora) like ?4 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + produtor.toUpperCase() + "%",
                "%" + seguradora.toUpperCase() + "%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorDataSeguradora(Date dtInicial, Date dtFinal, String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.periodo >= ?1 and "
                + "lan.periodo <= ?2 and ucase(lan.seguradora) like ?3 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                dtInicial, dtFinal, "%" + seguradora.toUpperCase() + "%");
    }

    public List<Lancamentos> getLancamentosAnaliticoPorSeguradora(String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
                + "and ucase(lan.seguradora) like ?1 "
                + "and lan.categoria is null "
                + "order by lan.periodo",
                "%" + seguradora.toUpperCase() + "%");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoPorSeguradoraAtual(String seguradora) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
                + "and ucase(lan.seguradora) like ?1 "
                + "and lan.categoria is null "
                + "and lan.dataIncluisao = ?2 "
                + "order by lan.periodo",
                "%" + seguradora.toUpperCase() + "%", Calendar.getInstance().getTime());
    }
    
    public List<Lancamentos> getLancamentosCpfCnpj(String cpf) throws Exception {
        return getList("select lan from Lancamentos lan where lan.categoria is null "
                + "and replace(replace(replace(replace(lan.cpf, '.',''),'.',''),'-',''),'/','') = ?1 "
                + "order by lan.periodo",
                cpf.replace(".", "").replace(".", "").replace("-", "").replace("/", ""));
    }

    /*
     * Buscas para o relatorio Sintetico
     */
    public List<Object[]> getLancamentosSintetico() throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3)"
                + "from Lancamentos l where l.categoria is null "
                + "group by l.produtor, l.seguradora, date_format(l.periodo, '%M/%Y') "
                + "order by date_format(l.periodo, '%M/%Y')");
    }

    public List<Object[]> getLancamentosSinteticoPorData(Date dtInicial, Date dtFinal) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3) "
                + "from Lancamentos l "
                + "where l.periodo >= ?1 and l.periodo <= ?2 "
                + "and l.categoria is null "
                + "group by l.produtor, l.seguradora, date_format(l.periodo, '%M/%Y') "
                + "order by date_format(l.periodo, '%M/%Y')", dtInicial, dtFinal);
    }

    public List<Object[]> getLancamentosSinteticoPorDataProdutor(Date dtInicial, Date dtFinal, String produtor) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3) from Lancamentos l "
                + "where l.periodo >= ?1 and l.periodo <= ?2 and ucase(l.produtor) like ?3 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora ", Util.validarData(dtInicial, Util.setaDataInicial(dtInicial)), 
                Util.validarData(dtFinal, Util.setaDataFinal(dtFinal)), "%" + produtor.toUpperCase() + "%");
    }

    public List<Object[]> getLancamentosSinteticoPorDataSeguradora(Date dtInicial, Date dtFinal, String seguradora) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3) from Lancamentos l "
                + "where l.periodo >= ?1 and l.periodo <= ?2 and ucase(l.seguradora) like ?3 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y'),l.produtor, l.seguradora", Util.validarData(dtInicial, Util.setaDataInicial(dtInicial)), 
                Util.validarData(dtFinal, Util.setaDataFinal(dtFinal)), "%" + seguradora.toUpperCase() + "%");
    }

    public List<Object[]> getLancamentosSinteticoPorDataSeguradoraProdutor(Date dtInicial, Date dtFinal, String seguradora, String produtor) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3) from Lancamentos l "
                + "where l.periodo >= ?1 and l.periodo <= ?2 and ucase(l.seguradora) like ?3 and ucase (l.produtor) like ?4 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y'),l.produtor, l.seguradora", Util.validarData(dtInicial, Util.setaDataInicial(dtInicial)), 
                Util.validarData(dtFinal, Util.setaDataFinal(dtFinal)), "%" + seguradora.toUpperCase() + "%", "%" +produtor.toUpperCase() + "%");
    }

    public List<Object[]> getLancamentosSinteticoPorDataData(Date dtInicial, Date dtFinal) throws Exception {
        return getList("select date_format(l.periodo, '%M/%Y'), l.produtor, l.seguradora, sum(l.comissao), sum(l.valor3) "
                + "from Lancamentos l "
                + "where l.periodo >= ?1 and l.periodo <= ?2 "
                + "and l.categoria is null "
                + "group by date_format(l.periodo, '%M/%Y'), l.seguradora, l.produtor", dtInicial, dtFinal);
    }

    /* 8.21% 
     * Buscas complementares
     */
    public List<String> getProdutores() throws Exception {
        List<String> produtores = new LinkedList<String>();
        List<String> objs = getList("select distinct l.produtor from Lancamentos l "
        		+ "where l.categoria is null ");

        for (String s : objs) {
            produtores.add(s);
        }
        return produtores;
    }

    public List<String> getSeguradoras() throws Exception {
        List<String> seguradoras = new LinkedList<String>();
        List<String> lancamentos = getList("select distinct l.seguradora from Lancamentos l "
        		+ "where l.categoria is null ");
        for (String prod : lancamentos) {
            seguradoras.add(prod);
        }
        return seguradoras;
    }
    
    /**
     * Verifica se eh renovacao
     * Descrecente: busca a partir ano atual a um ano menos
     * Crescente: busca a partir de um ano menor para um ano manor
     */
    public boolean ehLancamentosDescrecenteRenovacao(Lancamentos lanc) throws Exception {
    	List<Lancamentos> list = new ArrayList<>();
    	boolean retorno = false;
    	Calendar cal = Calendar.getInstance();
        cal.setTime(lanc.getPeriodo());
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.MONTH, 3);
        list = getList("select lan from Lancamentos lan "
        		+ "where lan.historico = ?1 "
        		+ "and lan.periodo <= ?2 "
        		+ "and lan.categoria is null ",
                lanc.getHistorico(), 
                cal.getTime());
        if (!list.isEmpty()){
        	retorno = true; // eh renovacao
        } else {
        	retorno = ehLancamentoAnterioRenovacao(lanc);
        }
        return retorno;
    }
    
    // Pesquisa dentro de um tervalo de menos 4 meses
    public boolean ehLancamentoAnterioRenovacao(Lancamentos lanc) throws Exception {
    	List<Lancamentos> list = new ArrayList<>();
    	boolean retorno = false;
    	Calendar cal = Calendar.getInstance();
        cal.setTime(lanc.getPeriodo());
        cal.add(Calendar.MONTH, -4);
    	list = getList("select lan from Lancamentos lan "
        		+ "where lan.historico = ?1 "
        		+ "and lan.parcela = ?2 "
        		+ "and lan.categoria is null "
        		+ "and lan.periodo <= ?3 "
        		+ "and lan.parcela != 0 "
        		+ "and lan.parcela is not null ",
                lanc.getHistorico(), 
                lanc.getParcela(),
                cal.getTime());
    	if (!list.isEmpty()){
    		retorno = true; // eh renovacao
    	}
    	return retorno;
    }
    
    /**
     * O metodo que chamar este sempre vai considerar o lancamento como novo
     */
    public boolean ehLancamentosCrecenteRenovacao(Lancamentos lanc) throws Exception {
        List<Lancamentos> list = new ArrayList<>();
        boolean retorno = false;
    	Calendar cal = Calendar.getInstance();
        cal.setTime(lanc.getPeriodo());
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.MONTH, -3);
        list = getList("select lan from Lancamentos lan "
        		+ "where lan.historico = ?1 "
        		+ "and lan.situacao != 2 "
        		+ "and lan.periodo >= ?2 "
        		+ "and lan.categoria is null ",
                lanc.getHistorico(),  
                cal.getTime());
    	
        if (!list.isEmpty()){
        	for (Lancamentos l : list){
        		l.setSituacao(2); // renovacao
        		atualizarLancamento(l);
        	}        	
        	retorno = true; // Os maiores sao renovacao, esse nao
        } else {
        	retorno = false; // nao eh renovacao
        }
        
    	 return retorno;
    }
    
    /**
     * Trabalho o legado
     */
    public List<Lancamentos> getLancamentosAnaliticoLegado() throws Exception {
        return getList("Select lan from Lancamentos lan "
        		+ "where lan.situacao != 2 "
        		+ "and lan.categoria is null "
                + "order by lan.periodo asc");
    }
    
    public List<Lancamentos> getLancamentosAnaliticoLegadoIsNull() throws Exception {
        return getList("Select lan from Lancamentos lan "
        		+ "where lan.situacao is null "
        		+ "and lan.categoria is null "
                + "order by lan.periodo asc");
    }
    
    public void atualizaSituacaoGeral(){
    	LancamentosDao dao = new LancamentosDao();
    	List<Lancamentos> list = new ArrayList<>();
    	try {
			
    		//System.out.println("Atualiza para situacao 1");
    		list = dao.getLancamentosAnaliticoLegadoIsNull();
			for (Lancamentos l : list){
				l.setSituacao(1);
				dao.atualizarLancamento(l);
			}
			
			//System.out.println("Atualiza para situacao 2");
			list = new ArrayList<>();
    		list = dao.getLancamentosAnaliticoLegado();
			for (Lancamentos l : list){
				dao.ehLancamentosCrecenteRenovacao(l);	
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Movimento
     * @param args
     */
    
    public List<Lancamentos> getMovimentos() throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria_id is not null "
                + "order by lan.periodo");
    }
    
    public List<Lancamentos> getMovimentosPorData(Date dtInicio, Date dtFim) throws Exception {
        return getList("Select lan from Lancamentos lan where lan.categoria_id is not null "
        		+ "and lan.periodo >= ?1 and lan.periodo <= ?2"
                + "order by lan.periodo", dtInicio, dtFim);
    }
    
    public List<Object[]> getMovimentoPorCategoriaSeguradoImposto(String anoMes, String produtor) throws Exception, NullPointerException{
    	return getList("select date_format(l.periodo, '%M/%Y'), sum(l.valor2) "
                + "from Lancamentos l "
                + "where l.categoria is null "
                + "and date_format(l.periodo, '%m/%Y') = ?1 "
                + "and l.produtor like ?2 "
                + "group by date_format(l.periodo, '%M/%Y') ", anoMes, "%" +produtor+ "%");
    }
    
    public List<Object[]> getMovimentoPorCategoriaSeguradoImposto(String anoMes) throws Exception, NullPointerException{
    	return getList("select date_format(l.periodo, '%M/%Y'), sum(l.valor2) "
                + "from Lancamentos l "
                + "where l.categoria is null "
                + "and date_format(l.periodo, '%m/%Y') = ?1 "
                + "and l.produtor not like '%JAQUELINE%' "
                + "and l.produtor not like '%ADRIANA%' "
                + "group by date_format(l.periodo, '%/%Y') ", anoMes);
    }
    
    public List<Object[]> buscarSeguradoras(String periodo) throws Exception{
    	return getList("select date_format(l.periodo, '%m/%Y'), l.seguradora, sum(l.comissao) "
                + "from Lancamentos l "
                + "where l.categoria is null "
                + "and date_format(l.periodo, '%m/%Y') = ?1 "
                + "group by date_format(l.periodo, '%m/%Y'), l.seguradora ", periodo);
    }
    
    public static void main (String [] args){
    	LancamentosDao dao = new LancamentosDao();
    	List<Lancamentos> list = new ArrayList<>();
    	try {
			
    		//System.out.println("Atualiza para situacao 1");
    		list = dao.getLancamentosAnaliticoLegadoIsNull();
			for (Lancamentos l : list){
				l.setSituacao(1);
				dao.atualizarLancamento(l);
			}
			
			//System.out.println("Atualiza para situacao 2");
			list = new ArrayList<>();
    		list = dao.getLancamentosAnaliticoLegado();
			for (Lancamentos l : list){
				dao.ehLancamentosCrecenteRenovacao(l);
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
}
