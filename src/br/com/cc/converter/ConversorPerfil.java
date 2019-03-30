package br.com.cc.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.com.cc.dao.PerfilDAO;
import br.com.cc.entidade.Perfil;

public class ConversorPerfil implements Converter, Serializable{

	private static final long serialVersionUID = 1L;
	private PerfilDAO perfilDao;
	
	public ConversorPerfil() {
		System.out.println("Inicia conversor de perfil");
		perfilDao = new PerfilDAO();
	}
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String string) {
		if (string == null || string.equals("selecione")) {
			System.out.println("Paramentro nulo");
			return null;
		}
		
		return perfilDao.getPerfil(Integer.parseInt(string));
		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object obj) {
		if (obj == null){
			return null;
		}
		
		Perfil perfil = (Perfil) obj;
		return perfil.getId().toString();
		
	}

}
