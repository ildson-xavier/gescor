package br.com.cc.mail.template;

public class Template {

	public static String emailMachadoPaiaro(String conteudo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>                                                                                    ");
		sb.append("    <head>                                                                                                             ");
		sb.append("   <meta charset='UTF-8'>                                                              ");
		sb.append("	<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'                   ");
		sb.append("	integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>          ");
		sb.append("	</head>                                                                                                               ");
		sb.append("	<body style='background-color: #ccc'>                                                    ");
		sb.append("		<div style='margin: 50px; background-color: #fff;'>                                  ");
		sb.append("			<div style='text-align:center; padding: 20px'>                                   ");
		sb.append("			<img src='http://machadopaiaro.com.br/ccomissao/resources/images/logo/icon.png'/>");
		sb.append("			</div>                                                                           ");
		sb.append("			<div style='padding: 10px'>                                                      ");
		sb.append("				<p>                                                                          ");
		sb.append(conteudo);
		sb.append("				</p>                                                                         ");
		sb.append("			</div>                                                                           ");
		sb.append("			                                                                                 ");
		sb.append("<div style='margin-top: 30px; padding-left: 50px; padding-bottom: 10px; border: 0px solid red; width: 300px;'>       ");  
		sb.append("	<p style='font-weight: 600'>Ildson Xavier</p>                                                                                   ");
		sb.append("	<p>Diretor T�cnico - CTO</p>                                                                                                    ");
		sb.append("	<p>(11) 97464-4540</p>                                                                                                          ");
		sb.append("	<p>ildson@sofcor.com.br</p>                                                                                                     ");
		sb.append("</div>                                                                                                                           ");
		sb.append("		</div>                                                                               ");
		sb.append("	</body>                                                                                  ");
		sb.append("</html>                                                                                   ");
		return sb.toString();
	}

	public static String emailUsuarioNovo(String conteudo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>                                                                                    ");
		sb.append("    <head>                                                                                                             ");
		sb.append("   <meta charset='UTF-8'>                                                              ");
		sb.append("	<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'                   ");
		sb.append("	integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>          ");
		sb.append("	</head>                                                                                                               ");
		sb.append("	<body style='background-color: #ccc'>                                                    ");
		sb.append("    <div style='text-align:right; font-size: 10px; margin-right: 50px'>                   ");
		sb.append("       Visualizar como p�gina web                                                         ");
		sb.append("     </div>                                                                               ");
		sb.append("		<div style='margin: 5px 50px 25px 50px; background-color: #fff;'>                    ");
		sb.append("			<div style='text-align:center; padding: 20px'>                                   ");
		sb.append("			<img src='http://machadopaiaro.com.br/ccomissao/resources/images/logo/logo1.png'/>");
		sb.append("			</div>                                                                           ");
		sb.append("			<div style='padding: 10px'>                                                      ");
		sb.append("				<p>                                                                          ");
		sb.append(conteudo);
		sb.append("				</p>                                                                         ");
		sb.append("			</div>                                                                           ");
		sb.append("			                                                                                 ");
		sb.append("<div style='margin-top: 30px; padding-left: 50px; padding-bottom: 10px; border: 0px solid red; width: 300px; float: left'>       ");  
		sb.append("	<p style='font-weight: 600'>Ildson Xavier</p>                                                                                   ");
		sb.append("	<p>Diretor T�cnico - CTO</p>                                                                                                    ");
		sb.append("	<p>(11) 97464-4540</p>                                                                                                          ");
		sb.append("	<p>ildson@sofcor.com.br</p>                                                                                                     ");
		sb.append("</div>                                                                                                                           ");
		sb.append("<div style='margin-top: 30px; padding-left: 50px; padding-bottom: 10px; border: 0px solid red; width: 300px; margin-left: 60%'>  ");       
		sb.append("	<p style='font-weight: 600'>Rafael de Faria</p>                                                                                ");
		sb.append("	<p>Diretor de Opera��es - COO</p>                                                                                               ");
		sb.append("	<p>(12) 98261-1533</p>                                                                                                          ");
		sb.append("	<p>rafael@sofcor.com.br</p>                                                                                                     ");
		sb.append("</div> 			                                                                                                                ");
		sb.append("		</div>                                                                               ");
		sb.append("     <div style='font-size: 10px; text-align: center'>                                    ");
		sb.append("         Enviado por SofCor<br/>                                                          ");
		sb.append("         Alameda Graja�, 98 - Aphaville Industrial, Barueri - SP <br/>                    ");
		sb.append("         www.sofcor.com.br                                                                ");
		sb.append("     </div>                                                                               ");
		sb.append("	</body>                                                                                  ");
		sb.append("</html>                                                                                   ");
		return sb.toString();
	}

	public static String emailNovaAssinatura(String conteudo) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>                                                                                                                 ");
		sb.append("    <head>                                                                                                             ");
		sb.append("   <meta charset='UTF-8'>                                                              ");
		sb.append("	<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'                   ");
		sb.append("	integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>          ");
		sb.append("	</head>                                                                                                               ");
		sb.append("	<body style='background-color: #ccc'>                                                                                 ");
		sb.append("    <div style='text-align:right; font-size: 10px; margin-right: 50px'>                                                ");
		sb.append("       Visualizar como p�gina web                                                                                      ");
		sb.append("     </div>                                                                                                            ");
		sb.append("		<div class='container'	style='margin: 5px 50px 25px 50px; background-color: #fff;'>                            ");
		sb.append("			<div style='text-align:center; padding: 20px'>                                                                ");
		sb.append("			<img src='http://machadopaiaro.com.br/ccomissao/resources/images/logo/logo1.png'/>                            ");
		sb.append("			</div>                                                                                                        ");
		sb.append("			<div style='padding: 10px'>                                                                                   ");
		sb.append("           <p> Ol�                                                                                                     ");                                                   
		sb.append("           Bem vindo(a) =)</p>                                                                                         ");
		sb.append(" <p>Estou entrando em contato para explicar rapidamente nosso processo de assinatura. </p>                             ");
		sb.append("                                                                                                                       ");
		sb.append("		<p>N�s nos comprometemos em ampliar seus resultados, otimizar seu tempo em at� 90% e te auxiliar a               ");
		sb.append("		controlar os processos de sua corretora com excel�ncia. </p>                                                      ");
		sb.append("                                                                                                                       ");
		sb.append("		<p>Aproveitando ent�o, j� falei isso mas quero refor�ar: nosso trabalho � ajudar voc� a ter mais sucesso.         ");
		sb.append("		Queremos entender seus problemas e te ajudar a chegar na melhor solu��o pra voc�. Para isso, vamos te ligar! </p> ");
		sb.append("			</div>                                                                                                        ");
		sb.append("			<br/>                                                                                                         ");
		sb.append("			<br/>                                                                                                         ");
		sb.append("<div style='margin-top: 30px; padding-left: 50px; padding-bottom: 10px; border: 0px solid red; width: 300px; float: left'>       ");  
		sb.append("	<p style='font-weight: 600'>Ildson Xavier</p>                                                                                   ");
		sb.append("	<p>Diretor T�cnico - CTO</p>                                                                                                    ");
		sb.append("	<p>(11) 97464-4540</p>                                                                                                          ");
		sb.append("	<p>ildson@sofcor.com.br</p>                                                                                                     ");
		sb.append("</div>                                                                                                                           ");
		sb.append("<div style='margin-top: 30px; padding-left: 50px; padding-bottom: 10px; border: 0px solid red; width: 300px; margin-left: 60%'>  ");       
		sb.append("	<p style='font-weight: 600'>Rafael de Faria</p>                                                                                ");
		sb.append("	<p>Diretor de Opera��es - COO</p>                                                                                               ");
		sb.append("	<p>(12) 98261-1533</p>                                                                                                          ");
		sb.append("	<p>rafael@sofcor.com.br</p>                                                                                                     ");
		sb.append("</div> 			                                                                                                                ");
		sb.append("		</div>                                                                               ");
		sb.append("     <div style='font-size: 10px; text-align: center'>                                                                 ");
		sb.append("         Enviado por SofCor<br/>                                                                                       ");
		sb.append("         Alameda Graja�, 98 - Aphaville Industrial, Barueri - SP <br/>                                                 ");
		sb.append("         www.sofcor.com.br                                                                                             ");
		sb.append("     </div>                                                                                                            ");
		sb.append("  </div>                                                                                                            ");
		sb.append("	</body>                                                                                                               ");
		sb.append("</html>                               ");
		return sb.toString();
	}
}
