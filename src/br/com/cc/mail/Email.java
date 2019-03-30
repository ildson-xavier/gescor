/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * http://blog.globalcode.com.br/2011/04/javamail-enviando-mensagem-html-com.html
 */
package br.com.cc.mail;

import java.io.File;

import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.cc.util.JSFMessageUtil;

/**
 *
 * @author Usuario
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	public void sendMail(String para, String copia, String titulo, String conteudo, String arquivo) throws Exception {
		final Properties props = new Properties();

		props.load(getClass().getResourceAsStream("/mail.properties"));

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.username").toString(),
						props.getProperty("mail.password").toString());
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username").toString()));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));

		if (copia != null && !copia.trim().isEmpty()) {
			String cc[] = copia.split(",");
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc[0]));
			if (cc.length > 1) {
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(cc[1]));
			}
		}

		message.setSubject(titulo);
		message.setContent(formatarHtml(conteudo), "text/html");

		// criando a Multipart
		Multipart multipart = new MimeMultipart();

		// criando a primeira parte da mensagem
		MimeBodyPart attachment0 = new MimeBodyPart();

		// configurando o htmlMessage com o mime type
		attachment0.setContent(formatarHtml(conteudo), "text/html; charset=UTF-8");

		// adicionando na multipart
		multipart.addBodyPart(attachment0);

		if (arquivo != null && !arquivo.trim().isEmpty()) {

			File file = new File(arquivo);

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
		}

		// Objeto encarregado de enviar os dados para o email
		Transport tr;
		try {
			tr = session.getTransport("smtp"); // define smtp para transporte
			/*
			 * 1 - define o servidor smtp 2 - seu nome de usuario do gmail 3 - sua senha do
			 * gmail
			 */

			tr.connect("smtp.machadopaiaro.com.br", "machadopaiaro2@machadopaiaro.com.br", "xavier1985");
			message.saveChanges(); // don't forget this
			// envio da mensagem
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (MessagingException e) {
			throw new Exception("Erro: Não foi possivel enviar o e-mail -> " + e.getMessage());
		}

		// Transport.send(message);

	}

	public static String formatarHtml(String conteudo) {
		StringBuilder conteudoFormatado = new StringBuilder();
		conteudoFormatado.append("<html>");
		conteudoFormatado.append("<body>");
		conteudoFormatado.append("<img src='http://machadopaiaro.com.br/ccomissao/resources/images/logo/icon.png'/>");
		conteudoFormatado.append("<br/>");
		conteudoFormatado.append("<b>");
		conteudoFormatado.append("Teste");
		conteudoFormatado.append("</b>");
		conteudoFormatado.append("</body>");
		conteudoFormatado.append("</html>");
		return conteudo;
	}

	public void enviarEmail(String para, String copia, String titulo, String conteudo, String arquivo) {

		try {
			sendMail(para, copia, titulo, conteudo, arquivo);

			if (arquivo != null && !arquivo.trim().isEmpty()) {
				JSFMessageUtil.sendInfoMessageToUser("E-MAIL.",
						"Foi identificado um problema ao tentar processar o arquivo, um e-mail foi enviado para o desenvolvedor do sistema."
								+ " Em breve entraremos em contato.");
			} else {
				// Foi enviado o usuário e senha para o e-mail informado
				JSFMessageUtil.sendInfoMessageToUser("", "");
			}

			System.out.println("Enviado com Sucesso");
		} catch (Exception e) {
			JSFMessageUtil.sendErrorMessageToUser("Falha ao tentar enviar o e-mail",
					"Por favor, entre com contado conosco pelo telefone (11) 97464-4540 " + "Erro: " + e.getMessage());
			System.out.println("Enviado com Sucesso");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		System.out.println("E-mail!");
		// String pathname = "X:\\Adriana\\ALLIANZ\\05.09.2015.csv";
		System.out.println("E-mail!");
		new Email().enviarEmail("ildson.moraes@gmail.com", "ildson.xavier@gmail.com", "Teste 2", "Testes xpto", null);
		System.out.println("E-mail enviado!");

	}
}
