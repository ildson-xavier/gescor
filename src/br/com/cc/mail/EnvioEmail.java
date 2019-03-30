package br.com.cc.mail;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.outjected.email.api.MailMessage;

import br.com.cc.util.JSFMessageUtil;

public class EnvioEmail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static Mailer mailer;
	
	public static void enviarEmail(String arquivo){
		
		mailer = new Mailer();
		try {
			MailMessage message = mailer.novaMensagem();
					
			message.to("ildson.xavier@gmail.com")
			.subject("MachadoPaiaro 2")
			.bodyHtml("Teste de envio de email "+arquivo+"")
			.send();
			JSFMessageUtil.sendInfoMessageToUser("E-mail enviado com sucesso", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
