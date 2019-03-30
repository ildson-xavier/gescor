package br.com.cc.apolice;

import br.com.cc.entidade.ApoliceBean;
import br.com.cc.entidade.Produtor;
import br.com.cc.entidade.VeiculoBean;
import br.com.cc.util.Util;

public class ApoliceAzul extends ApoliceFile implements Appolice {

	private String texto;
	private ApoliceBean apoliceBean;
	private VeiculoBean veiculoBean;
	private Produtor seguradoBean;

	@Override
	public void carregarApolice(String path) throws Exception {
		super.apoliceFilePorLinha(path);
		this.texto = lerArquivoPorLinha(getPathTxt());
		apoliceBean = new ApoliceBean();
		veiculoBean = new VeiculoBean();
		seguradoBean = new Produtor();

	}

	@Override
	public void pegarDadosGerais() throws Exception {
		String linhas[] = this.texto.split("; ");

		for (int i = 0; i < linhas.length; i++) {
			if (linhas[i].length() > 3) {

				if (linhas[i].substring(0, 2).trim().equals("2")) {
					apoliceBean.setNumeroApolice(linhas[i].substring(2, 40).trim());
					apoliceBean.setDataVigenciaInicio(Util.formatarDataStr(linhas[i].substring(64, 75).trim()));
					apoliceBean.setDataTermino(Util.formatarDataStr(linhas[i].substring(84, 95).trim()));
				}

				if (linhas[i].substring(0, 2).trim().equals("3")) {
					apoliceBean.setSusep(linhas[i].substring(2, 44).trim());
					apoliceBean.setSeguradora("Azul");
				}
			}
		}
	}

	@Override
	public void pegarDadosDoSeguradora() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void pegarDadosDaSurcusal() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void pegarDadosDoCorretor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pegarDadosDoSegurado() {
		String linhas[] = this.texto.split("; ");

		for (int i = 0; i < linhas.length; i++) {
			if (linhas[i].length() > 3) {

				if (linhas[i].substring(0, 2).trim().equals("4")) {
					seguradoBean.setSegurado(linhas[i].substring(2, linhas[i].length()).trim());
				}

				if (linhas[i].substring(0, 2).trim().equals("5")) {
					seguradoBean.setEndereco(linhas[i].substring(2, linhas[i].length()).trim());
				}

				if (linhas[i].substring(0, 2).trim().equals("6")) {
					seguradoBean.setBairro(linhas[i].substring(2, linhas[i].length()).trim());
				}

				if (linhas[i].substring(0, 2).trim().equals("7")) {
					seguradoBean.setCep(linhas[i].substring(2, 13).trim());
					seguradoBean.setCidade(linhas[i].substring(14, linhas[i].length()).trim());
				}

				if (linhas[i].substring(0, 2).trim().equals("69")) {
					if (linhas[i].substring(3, 12).trim().equals("ASSIST. G")) {
						if (linhas[76].substring(0, 2).trim().equals("75")) {
							seguradoBean.setCpf(linhas[76].substring(34, 47).trim());
						}
					}
				}

				if (linhas[i].substring(0, 2).trim().equals("70")) {
					if (linhas[i].substring(3, 12).trim().equals("ASSIST. G")) {
						if (linhas[77].substring(0, 2).trim().equals("76")) {
							seguradoBean.setCpf(linhas[77].substring(34, 47).trim());
						}
					}
				}

				if (linhas[i].substring(0, 2).trim().equals("71")) {
					if (linhas[i].substring(3, 12).trim().equals("ASSIST. G")) {
						if (linhas[78].substring(0, 2).trim().equals("77")) {
							seguradoBean.setCpf(linhas[78].substring(34, 47).trim());
						}
					}
				}

				if (linhas[i].substring(0, 2).trim().equals("72")) {
					if (linhas[i].substring(3, 12).trim().equals("ASSIST. G")) {
						if (linhas[79].substring(0, 2).trim().equals("78")) {
							seguradoBean.setCpf(linhas[79].substring(34, 47).trim());
						}
					}
				}
			}
		}

	}

	@Override
	public void pegarDadosDoVeiculo() {
		String linhas[] = this.texto.split("; ");

		for (int i = 0; i < linhas.length; i++) {
			if (linhas[i].length() > 3) {

				if (linhas[i].substring(0, 2).trim().equals("41")) {
					if (linhas[i].substring(3, 6).trim().equals("SAC")) {
						if (linhas[48].substring(0, 2).trim().equals("47")) {
							veiculoBean.setAnoFabricacaoAnoModelo(linhas[48].substring(2, 25).trim());
							veiculoBean.setPlaca(linhas[48].substring(29, 40).trim());
						}

						if (linhas[49].substring(0, 2).trim().equals("48")) {
							veiculoBean.setChassi(linhas[49].substring(8, 30).trim());
						}

						if (linhas[47].substring(0, 2).trim().equals("46")) {
							veiculoBean.setMarcaModelo(linhas[47].substring(3, 30).trim());
						}
					}
				}
				
				if (linhas[i].substring(0, 2).trim().equals("42")) {
					if (linhas[i].substring(3, 6).trim().equals("SAC")) {
						if (linhas[49].substring(0, 2).trim().equals("48")) {
							veiculoBean.setAnoFabricacaoAnoModelo(linhas[49].substring(2, 25).trim());
							veiculoBean.setPlaca(linhas[49].substring(29, 40).trim());
						}

						if (linhas[50].substring(0, 2).trim().equals("49")) {
							veiculoBean.setChassi(linhas[50].substring(8, 30).trim());
						}

						if (linhas[48].substring(0, 2).trim().equals("47")) {
							veiculoBean.setMarcaModelo(linhas[48].substring(3, 30).trim());
						}
					}
				}
				
				if (linhas[i].substring(0, 2).trim().equals("43")) {
					if (linhas[i].substring(3, 6).trim().equals("SAC")) {
						if (linhas[50].substring(0, 2).trim().equals("49")) {
							veiculoBean.setAnoFabricacaoAnoModelo(linhas[50].substring(2, 25).trim());
							veiculoBean.setPlaca(linhas[50].substring(29, 40).trim());
						}

						if (linhas[51].substring(0, 2).trim().equals("50")) {
							veiculoBean.setChassi(linhas[51].substring(8, 30).trim());
						}

						if (linhas[49].substring(0, 2).trim().equals("48")) {
							veiculoBean.setMarcaModelo(linhas[49].substring(3, 30).trim());
						}
					}
				}
				
				if (linhas[i].substring(0, 2).trim().equals("44")) {
					if (linhas[i].substring(3, 6).trim().equals("SAC")) {
						if (linhas[51].substring(0, 2).trim().equals("50")) {
							veiculoBean.setAnoFabricacaoAnoModelo(linhas[51].substring(2, 25).trim());
							veiculoBean.setPlaca(linhas[51].substring(29, 40).trim());
						}

						if (linhas[52].substring(0, 2).trim().equals("51")) {
							veiculoBean.setChassi(linhas[52].substring(8, 30).trim());
						}

						if (linhas[50].substring(0, 2).trim().equals("49")) {
							veiculoBean.setMarcaModelo(linhas[50].substring(3, 30).trim());
						}
					}
				}

			}
		}

	}

	@Override
	public ApoliceBean gerarApolice() {
		apoliceBean.setSegurado(seguradoBean);
		apoliceBean.setVeiculo(veiculoBean);
		return apoliceBean;
	}

	public static void main(String[] args) throws Exception {
		String path = "X:\\Adriana\\Apolice\\";
		String pdf = "azul-neu.pdf";

		ApoliceAzul am = new ApoliceAzul();
		am.carregarApolice(path + pdf);
		am.pegarDadosGerais();
		am.pegarDadosDoSegurado();
		am.pegarDadosDoVeiculo();
		System.out.println(am.gerarApolice());
	}
}
