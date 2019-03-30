package br.com.cc.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class JSFMessageUtil {
	public static void sendInfoMessageToUser(String message, String descricao) {
        FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_INFO, message, descricao);
        addMessageToJsfContext(facesMessage);
    }
	
	public static void sendAlertMessageToUser(String message, String descricao) {
        FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_WARN, message, descricao);
        addMessageToJsfContext(facesMessage);
    }
 
    public static void sendErrorMessageToUser(String message, String descricao) {
        FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_ERROR, message, descricao);
        addMessageToJsfContext(facesMessage);
    }
    
    public static void sendFatalMessageToUser(String message, String descricao) {
        FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_FATAL, message, descricao);
        addMessageToJsfContext(facesMessage);
    }
 
    private static FacesMessage createMessage(Severity severity, String mensagemErro, String descricao) {
        return new FacesMessage(severity, mensagemErro, descricao);
    }
 
    private static void addMessageToJsfContext(FacesMessage facesMessage) {
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
}
