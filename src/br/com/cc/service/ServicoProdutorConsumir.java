package br.com.cc.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import br.com.cc.dao.ProdutorDao;
import br.com.cc.entidade.Produtor;

public class ServicoProdutorConsumir {

	
	public static String consumer3() {
	    Client c = Client.create();
	    WebResource wr = c.resource(
	      //"http://35.164.12.236:8080/ccomissao/rest/produtor/listar");
	    		//"http://localhost:8080/ccomissao/rest/produtor/listar");
	    		//"http://machadopaiaro.com.br/ccomissao/rest/produtor/listar");
	    		"http://cep.republicavirtual.com.br/web_cep.php?cep=08598140&formato=json");
	    		
	    return wr.get(String.class);
	  }
	
	
	
	public static String sendPost(String url, String json) throws Exception {

	    try {
	        // Cria um objeto HttpURLConnection:
	        HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();

	        try {
	            // Define que a conexão pode enviar informações e obtê-las de volta:
	            request.setDoOutput(true);
	            request.setDoInput(true);

	            // Define o content-type:
	            request.setRequestProperty("Content-Type", "application/json");

	            // Define o método da requisição:
	            request.setRequestMethod("POST");

	            // Conecta na URL:
	            request.connect();

	            // Escreve o objeto JSON usando o OutputStream da requisição:
	            //try (OutputStream outputStream = request.getOutputStream()) {
	            //    outputStream.write(json.getBytes("UTF-8"));
	            //}
	            
				OutputStream outputStream = request.getOutputStream();
				try {
					outputStream.write(json.getBytes("UTF-8"));
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
				}

	            // Caso você queira usar o código HTTP para fazer alguma coisa, descomente esta linha.
	            //int response = request.getResponseCode();

	            return readResponse2(request);
	        } finally {
	            request.disconnect();
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	    
	    return "Ildson";
	}
	
	private static String readResponse(HttpURLConnection request) throws IOException {
	    ByteArrayOutputStream os;
	    try (InputStream is = request.getInputStream()) {
	        os = new ByteArrayOutputStream();
	        int b;
	        while ((b = is.read()) != -1) {
	            os.write(b);
	        }
	    }
	    return new String(os.toByteArray());
	}
	
	private static String readResponse2(HttpURLConnection request) throws IOException {
	    ByteArrayOutputStream os;
	    try {
	    	
	    	InputStream is = request.getInputStream(); 
		        os = new ByteArrayOutputStream();
		        int b;
		        while ((b = is.read()) != -1) {
		            os.write(b);
		        }
		    
	    } finally {
	    	System.out.println("OK");
	    }
	    return new String(os.toByteArray());
	}
	
	private static String getDados(){	
		ProdutorDao dao = new ProdutorDao();
		try {
			JSONObject object = new JSONObject();
			Produtor p = dao.getProdutor(5);

			//object.put("id", p.getId());
			object.put("apolice", p.getApolice());
			object.put("produtor", p.getProdutor());
			object.put("segurado", p.getSegurado());
			object.put("cpf", p.getCpf());
			//object.put("sigla", p.getSigla());
			
			System.out.println("Json: "+object.toString());
			return object.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
		
	}

	public void consumir(String json) throws JSONException{
		Produtor produtor = new Produtor();
		List<Produtor> lista = new ArrayList<>();
		//JSONArray object = new JSONArray(json);
		JSONObject obj = new JSONObject(json);
		
		//produtor.setId(object.getJSONArray(0).getInt(0));
		System.out.println(obj.length());

		
		System.out.println(obj.getString("uf"));
		System.out.println(obj.getString("cidade"));
		//JSONObject j = new JSONObject(object.get(1).toString());
		
		//System.out.println(j.getString("produtor"));

	}
	
	public static void main(String[] args) throws Exception {

		System.out.println(ServicoProdutorConsumir.consumer3());
		
		new ServicoProdutorConsumir().consumir(ServicoProdutorConsumir.consumer3());
		
		//String msg = sendPost("http://35.164.12.236:8080/ccomissao/rest/produtor/adicionar", 
		//		getDados());
		
		//System.out.println("Mensagem: "+msg);
		
		//getDados();
	}
}
