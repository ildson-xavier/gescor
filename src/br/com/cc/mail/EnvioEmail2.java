package br.com.cc.mail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.outjected.email.impl.SimpleMailConfig;

import br.com.cc.util.JSFMessageUtil;

public class EnvioEmail2 implements Serializable {

	private static final long serialVersionUID = 1L;

	public void enviarEmail(String arquivo) {
		try {
			// objeto para definicao das propriedades de configuracao do
			// provider
			// o valor "smtp.host.com.br" deve ser alterado para o seu servidor
			// SMTP
			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", 465);
			properties.put("mail.smtp.auth", "true");
			//properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.enable.ssl", "true");
			properties.put("mail.user", "ildson.moraes@gmail.com");
			properties.put("mail.password", "X@vier1985");

			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("ildson.moraes@gmail.com", "X@vier1985");
				}
			};

			// obtendo um Session com a configuração
			// do servidor SMTP definida em props
			Session session = Session.getDefaultInstance(properties, auth);

			// criando a mensagem
			MimeMessage message = new MimeMessage(session);

			// substituir pelos e-mails desejados

			Address from = new InternetAddress("ildson.moraes@gmail.com");
			Address to = new InternetAddress("ildson.xavier@gmail.com");

			// configurando o remetente e o destinatario
			message.setFrom(from);
			message.addRecipient(RecipientType.TO, to);

			// configurando a data de envio, o assunto e o texto da mensagem
			message.setSentDate(new Date());
			message.setSubject("Enviando uma mensagem formatada com anexo");

			// conteudo html que sera atribuido a mensagem
			String htmlMessage = "< h t m l > Código HTML da mensagem ";

			// criando a Multipart
			Multipart multipart = new MimeMultipart();

			// criando a primeira parte da mensagem
			MimeBodyPart attachment0 = new MimeBodyPart();

			// configurando o htmlMessage com o mime type
			attachment0.setContent(htmlMessage, "text/html; charset=UTF-8");

			// adicionando na multipart
			multipart.addBodyPart(attachment0);

			// arquivo que será anexado
			String pathname = arquivo;// pode conter o caminho
			File file = new File(pathname);

			// criando a segunda parte da mensagem
			MimeBodyPart attachment1 = new MimeBodyPart();

			// configurando o DataHandler para o arquivo desejado
			// a leitura dos bytes, descoberta e configuracao do tipo
			// do arquivo serão resolvidos pelo JAF (DataHandler e
			// FileDataSource)
			attachment1.setDataHandler(new DataHandler(new FileDataSource(file)));

			// configurando o nome do arquivo que pode ser diferente do arquivo
			// original Ex: setFileName("outroNome.png")
			attachment1.setFileName(file.getName());

			// adicionando o anexo na multipart
			multipart.addBodyPart(attachment1);

			// adicionando a multipart como conteudo da mensagem
			message.setContent(multipart);

			// enviando
			Transport.send(message);

			System.out.println("E-mail enviado com sucesso!");
			//JSFMessageUtil.sendInfoMessageToUser("E-mail enviado com sucesso");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
