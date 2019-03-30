package br.com.teste;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.cc.dao.ConfiguracaoProdutorDAO;
import br.com.cc.entidade.ConfiguracaoProdutor;
import junit.framework.TestCase;

public class TesteConfiguracao extends TestCase{

	ConfiguracaoProdutorDAO dao = new ConfiguracaoProdutorDAO();
	@Test
	public void test() throws Exception {
		ConfiguracaoProdutor c = new ConfiguracaoProdutor();
		c.setDataInclusao(new Date());
		c.setQuantidade(100);
		c.setUltimoId(10);
		
		dao.adicionarConfiguracao(c);
	}
	
	@Test
	public void testObter(){
		List<ConfiguracaoProdutor> numero =  dao.listarConfiguracaoProdutor();
		for (ConfiguracaoProdutor c : numero){
			System.out.println(c.getDataInclusao() + " > "+ c.getUltimoId());
		}
		
	}
}
