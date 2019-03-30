package br.com.cc.dao;

import java.io.Serializable;
import java.util.List;

import br.com.cc.entidade.ConfiguracaoProdutor;

public class ConfiguracaoProdutorDAO extends GenericDAO<ConfiguracaoProdutor> implements Serializable {

	private static final long serialVersionUID = 1L;

	public void adicionarConfiguracao(ConfiguracaoProdutor produtor) {
		save(produtor);
	}

	public List<ConfiguracaoProdutor> listarConfiguracaoProdutor() {
		return getList(
				"select m from ConfiguracaoProdutor m order by m.id desc");
	}
}
