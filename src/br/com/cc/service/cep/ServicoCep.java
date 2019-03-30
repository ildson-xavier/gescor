package br.com.cc.service.cep;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

import br.com.cc.entidade.Corretora;
import br.com.cc.entidade.Endereco;

public class ServicoCep {

	private Client client;
    private WebResource webResource;
    
    public ServicoCep(String cep) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("http://cep.republicavirtual.com.br/web_cep.php?cep=");
		sb.append(cep);
		sb.append("&formato=json");
		
    	ClientConfig clientConfig = new DefaultClientConfig(GensonProvider.class);
		client = Client.create(clientConfig);
        client.addFilter(new LoggingFilter(System.out));
        webResource = client.resource(sb.toString());
	}
	public Corretora obterDados() throws Exception {
	        
	        return converterParaCorretora(webResource.get(new GenericType<Endereco>() {}));
			
	}

	private static String validar(String cep) throws Exception {
		if (cep != null && !cep.equals("")) {
			String cp = cep.replace("-", "").trim();
			return cp;
		}
		throw new Exception("Informe um CEP");
	}
	
	private static Corretora converterParaCorretora(Endereco endereco){
		Corretora corretora = new Corretora();
		if (endereco != null && !endereco.getUf().equals("")){

			corretora.setBairro(endereco.getBairro());
			corretora.setCidade(endereco.getCidade());
			corretora.setEndereco(endereco.getTipo_logradouro() + " " + endereco.getLogradouro());
			corretora.setUf(endereco.getUf());
		}	
		
		return corretora;
	}
	
	public static void main(String[] args) throws Exception {
		ServicoCep cep = new ServicoCep("08598140");
		System.out.println(cep.obterDados());
	}

}
