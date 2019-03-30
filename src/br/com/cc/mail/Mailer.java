package br.com.cc.mail;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.outjected.email.api.MailMessage;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.MailMessageImpl;
import com.outjected.email.impl.SimpleMailConfig;

public class Mailer implements Serializable{

	private static final long serialVersionUID = 1L;

	private SessionConfig mailConfig;
	
	public MailMessage novaMensagem() throws IOException{
		return new MailMessageImpl(this.getMailConfig());
	}
	
	public SessionConfig getMailConfig() throws IOException{
		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/mail.properties"));
		
		SimpleMailConfig config = new SimpleMailConfig();
		config.setServerHost(properties.getProperty("mail.server.host"));
		config.setServerPort(Integer.parseInt(properties.getProperty("mail.server.port")));
		config.setEnableSsl(Boolean.valueOf(properties.getProperty("mail.enable.ssl")));
		config.setAuth(Boolean.valueOf(properties.getProperty("mail.auth")));
		config.setUsername(properties.getProperty("mail.username"));
		config.setPassword(properties.getProperty("mail.password"));
		
		
		return config;
	}

	public void setMailConfig(SessionConfig mailConfig) {
		this.mailConfig = mailConfig;
	}
}
