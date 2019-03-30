package br.com.cc.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import br.com.cc.dao.LancamentosDao;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;
import br.com.cc.facade.CategoriaFacade;
import br.com.cc.util.Util;

public class DespesasXls implements Serializable {

	private static final long serialVersionUID = 1L;
	private CategoriaFacade categoriaFacade = new CategoriaFacade();
	private LancamentosDao lancamentosDao = new LancamentosDao();

	public void importarDespesas(String arquivo) throws Exception {
		System.out.println("importarDespesas");
		int index = 0;
		int coluna = 0;
		String data = "";
		DateFormat formatter = new SimpleDateFormat("dd/yyyy");
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		Row linha = null;

		try {
			// Abre o arquivo [planilha]
			InputStream inputStream = new FileInputStream(new File(arquivo));

			// Carrega a planilha
			HSSFWorkbook wb = new HSSFWorkbook(inputStream);

			// Seleciona a primeira aba
			HSSFSheet aba = wb.getSheetAt(0);

			// Equanto tiver linha pra ler, executa o codigo
			Iterator<Row> linhas = aba.iterator();
			while (linhas.hasNext()) {

				// Carrega a linha da variavel
				linha = linhas.next(); 

				// Pega a data
				if (linha.getRowNum() == 7) {
					data = linha.getCell(1).getStringCellValue();
					System.out.println("Data: " + data);
					date = (Date) formatter.parse(data);
					System.out.println("Date: " + date);
					calendar.setTime(date);
					System.out.println("Calendar: " + calendar.getTime());
					calendar.add(Calendar.MONTH, 1);
					System.out.println("Calendar: " + calendar.getTime());

				}

				// Começa a partir da linha 8
				if (linha.getRowNum() > 7) {

					if (linha.getCell(0).getStringCellValue().trim().equals("Total")) {
						break;
					}
					index++;
					//linha.getCell(9).setCellValue(linha.getCell(9).getStringCellValue().trim());
//					System.out.println("Linha: [" + index + "] " + linha.getCell(coluna).getStringCellValue() + " | "
//							+ linha.getCell(++coluna).getNumericCellValue() + " | " + linha.getCell(++coluna).getNumericCellValue()
//							+ " | " + linha.getCell(++coluna).getNumericCellValue() + " | "
//							+ linha.getCell(++coluna).getNumericCellValue() + " | " + linha.getCell(++coluna).getNumericCellValue()
//							+ " | " + linha.getCell(++coluna).getNumericCellValue() + " | "
//							+ linha.getCell(++coluna).getNumericCellValue() + " | " + linha.getCell(8).getNumericCellValue()
//							+ " | " + linha.getCell(++coluna).getNumericCellValue() + " | "
//							+ linha.getCell(++coluna).getNumericCellValue() + " | " + linha.getCell(11).getNumericCellValue()
//							+ " | " + linha.getCell(++coluna).getNumericCellValue() + " ");

					processarLinha(linha, date);
					coluna = 0;
				}
			}
		} catch (NullPointerException n) {
			throw new Exception("Erro durante a leitura das celulas da planilha " + n.getMessage());
		} catch (FileNotFoundException e) {
			throw new Exception("Arquivo não encontrado ou corrompido " + e.getMessage());
		} catch (IOException e) {
			throw new Exception("Erro durante o processamento do arquivo " + e.getMessage());
		} catch (ParseException e) {
			throw new Exception("Erro de conversão de tipos " + e.getMessage());
		} catch (Exception e) {
			String linhaErro = "Verifique a linha [" + (index + 8) + "] " + linha.getCell(0).getStringCellValue();
			String colunaErro = " e coluna [" +Util.getColuna(coluna)+ "] " +linha.getCell(coluna).getStringCellValue();
			
			System.out.println("Linha: [" + index + "] " + linha.getCell(0).getStringCellValue() + " | "
					+ linha.getCell(coluna).getStringCellValue());
			if (linha.getCell(9).getCellType()== CellType.NUMERIC){
				System.out.println("Numérico");
			} else if (linha.getCell(9).getCellType() == CellType.BLANK) {
				System.out.println("String");
				System.out.println(linha.getCell(9).getStringCellValue());
			} else {
				System.out.println("outro");
			}
			
			throw new Exception("Erro no processamento da planilha.\n\n"+ linhaErro +"\n\n" +colunaErro);
			//e.printStackTrace();
		}
	}

	public void processarLinha(Row linha, Date date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		String despesa = linha.getCell(0).getStringCellValue();
		Lancamentos lanc = new Lancamentos();
		Lancamentos lancam = null;
		Double valor = 0.0;
		double perc = 0.0;
		Double resultado = 0.0;
		List<Lancamentos> lancamentos = new ArrayList<>();

		try {
			List<SubTipo> sub = categoriaFacade.getListSubTipoPorDescricao(despesa);

			if (sub.size() == 0) {
				lancamentosDao.getLancamentosParaRemover();
				throw new Exception("[processarLinha] " + "Não foi encontrada o subtipo procurado [" + despesa + "]");

			} else if (sub.size() == 1) {
				SubTipo subTipo = sub.get(0);
				lanc.setSubTipo(subTipo);
				lanc.setCategoria(subTipo.getCategoria());
				lanc.setDataIncluisao(Calendar.getInstance().getTime());
				lanc.setStatus("Pago");
				for (int i = 0; i < 12; i++) {
					calendar.setTime(date);
					//linha.getCell(i + 1).setCellValue(linha.getCell(i + 1).getStringCellValue().trim());
					lanc.setValor3(linha.getCell(i + 1).getNumericCellValue());
					lanc.setValor1(linha.getCell(i + 1).getNumericCellValue());
					lanc.setComissao(linha.getCell(i + 1).getNumericCellValue());
					calendar.add(Calendar.MONTH, i);
					lanc.setPeriodo(calendar.getTime());
					lanc.setClassificacao(2);

					lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), subTipo);
					if (lancamentos.size() == 0) {
						if (lanc.getComissao() != 0) {
							lancamentosDao.adicionarLancamento(lanc);
						}
						System.out.println("NAO EXISTE");
					} else {
						System.out.println("JA EXISTE!");
						vAtualizarLancamento(lancamentos, lanc);
						lancamentos = new ArrayList<>();
					}

				}
			} else if (sub.size() == 2) {
				for (SubTipo su : sub) {
					System.out.println("Nome: " + su.getCategoria().getTipo());
					if ((su.getCategoria().getTipo().contains("CRISTINA")
							|| su.getCategoria().getTipo().contains("JAQUE"))) {

						lanc = new Lancamentos();
						lanc.setSubTipo(su);
						lanc.setCategoria(su.getCategoria());
						lanc.setDataIncluisao(Calendar.getInstance().getTime());
						lanc.setClassificacao(2);
						lanc.setStatus("Pago");

						if (!su.getDescricao().contains("SIMPLE")) {

							for (int i = 0; i < 12; i++) {
								calendar.setTime(date);
								//linha.getCell(i + 1).setCellValue(linha.getCell(i + 1).getStringCellValue().trim());
								valor = linha.getCell(i + 1).getNumericCellValue();
								perc = 100 - su.getPercentagem();
								perc = (perc / 100);
								resultado = valor - (valor * perc);
								lanc.setValor1(valor);
								lanc.setValor3(resultado);
								lanc.setComissao(resultado);
								calendar.add(Calendar.MONTH, i);
								lanc.setPeriodo(calendar.getTime());
								lanc.setClassificacao(2);

								lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), su);
								if (lancamentos.size() == 0) {
									if (lanc.getComissao() != 0) {
										lancamentosDao.adicionarLancamento(lanc);
									}
									System.out.println("NAO EXISTE");
								} else {
									System.out.println("JA EXISTE!");
									vAtualizarLancamento(lancamentos, lanc);
									lancamentos = new ArrayList<>();
								}

							}
						} else {
							for (int i = 0; i < 12; i++) {
								calendar.setTime(date);
								calendar.add(Calendar.MONTH, i);
								lanc.setPeriodo(calendar.getTime());
								lanc.setClassificacao(2);
								lancam = getMovimentoPorCategoriaSegurado(lanc.getPeriodo(),
										su.getCategoria().getTipo());
								if (lancam.getMesAno().equals("")) {
									lanc.setValor1(0.0);
									lanc.setValor3(0.0);
									lanc.setComissao(0.0);
								} else {
									lanc.setValor1(lancam.getComissao());
									lanc.setValor3(lancam.getComissao());
									lanc.setComissao(lancam.getComissao());
								}

								lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), su);
								if (lancamentos.size() == 0) {
									if (lanc.getComissao() != 0) {
										lancamentosDao.adicionarLancamento(lanc);
									}
									System.out.println("NAO EXISTE");
								} else {
									System.out.println("JA EXISTE!");
									vAtualizarLancamento(lancamentos, lanc);
									lancamentos = new ArrayList<>();
								}
							}
						}
					} else {
						throw new Exception(
								"[processarLinha] " + "Não foi encontrada o socio correspondente [Adriana/Jaqueline]");
					}
				}
			} else if (sub.size() == 3) {
				for (SubTipo su : sub) {
					System.out.println("Nome: " + su.getCategoria().getTipo());
					if ((su.getCategoria().getTipo().contains("CRISTINA")
							|| su.getCategoria().getTipo().contains("JAQUE"))) {

						lanc = new Lancamentos();
						lanc.setSubTipo(su);
						lanc.setCategoria(su.getCategoria());
						lanc.setDataIncluisao(Calendar.getInstance().getTime());
						lanc.setStatus("Pago");

						if (!su.getDescricao().contains("SIMPLE")) {

							for (int i = 0; i < 12; i++) {
								calendar.setTime(date);
								//linha.getCell(i + 1).setCellValue(linha.getCell(i + 1).getStringCellValue().trim());
								valor = linha.getCell(i + 1).getNumericCellValue();
								perc = 100 - su.getPercentagem();
								perc = (perc / 100);
								resultado = valor - (valor * perc);
								lanc.setValor1(valor);
								lanc.setValor3(resultado);
								lanc.setComissao(resultado);
								calendar.add(Calendar.MONTH, i);
								lanc.setPeriodo(calendar.getTime());
								lanc.setClassificacao(2);

								lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), su);
								if (lancamentos.size() == 0) {
									if (lanc.getComissao() != 0) {
										lancamentosDao.adicionarLancamento(lanc);
									}
									System.out.println("NAO EXISTE");
								} else {
									System.out.println("JA EXISTE!");
									vAtualizarLancamento(lancamentos, lanc);
									lancamentos = new ArrayList<>();
								}
							}
						} else {
							for (int i = 0; i < 12; i++) {
								calendar.setTime(date);
								calendar.add(Calendar.MONTH, i);
								lanc.setPeriodo(calendar.getTime());
								lanc.setClassificacao(2);
								lancam = getMovimentoPorCategoriaSegurado(lanc.getPeriodo(),
										su.getCategoria().getTipo());
								if (lancam.getMesAno().equals("")) {
									lanc.setValor1(0.0);
									lanc.setValor3(0.0);
									lanc.setComissao(0.0);
								} else {
									lanc.setValor1(lancam.getComissao());
									lanc.setValor3(lancam.getComissao());
									lanc.setComissao(lancam.getComissao());
								}

								lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), su);
								if (lancamentos.size() == 0) {
									if (lanc.getComissao() != 0) {
										lancamentosDao.adicionarLancamento(lanc);
									}
									System.out.println("NAO EXISTE");
								} else {
									System.out.println("JA EXISTE!");
									vAtualizarLancamento(lancamentos, lanc);
									lancamentos = new ArrayList<>();
								}
							}
						}
					} else if (su.getCategoria().getTipo().contains("CORRETORA")) {
						lanc = new Lancamentos();
						lanc.setSubTipo(su);
						lanc.setCategoria(su.getCategoria());
						lanc.setDataIncluisao(Calendar.getInstance().getTime());
						lanc.setStatus("Pago");

						for (int i = 0; i < 12; i++) {
							calendar.setTime(date);
							calendar.add(Calendar.MONTH, i);
							lanc.setPeriodo(calendar.getTime());
							lanc.setClassificacao(2);
							lancam = getMovimentoPorCategoriaSegurado(lanc.getPeriodo(), null);
							if (lancam.getMesAno().equals("")) {
								lanc.setValor1(0.0);
								lanc.setValor3(0.0);
								lanc.setComissao(0.0);
							} else {
								lanc.setValor1(lancam.getComissao());
								lanc.setValor3(lancam.getComissao());
								lanc.setComissao(lancam.getComissao());
							}

							lancamentos = lancamentosDao.existeDespesa(lanc.getPeriodo(), su);
							if (lancamentos.size() == 0) {
								if (lanc.getComissao() != 0) {
									lancamentosDao.adicionarLancamento(lanc);
								}
								System.out.println("NAO EXISTE");
							} else {
								System.out.println("JA EXISTE!");
								vAtualizarLancamento(lancamentos, lanc);
								lancamentos = new ArrayList<>();
							}
						}

					} else {
						throw new Exception(
								"[processarLinha] " + "Não foi encontrada o socio correspondente [Adriana/Jaqueline]");
					}
				}
			} else {
				throw new Exception("[processarLinha] " + "Atenção! foi encontrado mais de 3 "
						+ "lancamentos para o mesmo tipo [" + despesa + "]");
			}

		} catch (Exception e) {
			lancamentosDao.getLancamentosParaRemover();
			throw new Exception("[processarLinha] " + "Erro durante o processamento do arquivo " + e.getMessage());

		}

	}

	private void vAtualizarLancamento(List<Lancamentos> lancamentos, Lancamentos lanc) throws Exception {
		for (Lancamentos l : lancamentos) {
			if (l.getComissao() != lanc.getComissao()) {
				l.setComissao(lanc.getComissao());
				l.setValor1(lanc.getValor1());
				l.setValor3(lanc.getValor3());
				try {
					lancamentosDao.atualizarLancamento(l);
				} catch (Exception e) {
					throw new Exception(
							"[vAtualizarLancamento] " + "Erro durante a atualização do lancamento " + e.getMessage());
				}
			}
		}

	}

	private Lancamentos getMovimentoPorCategoriaSegurado(Date anoMes, String segurado) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(anoMes);
		String mesAno = "";

		// Diminiu um mês para pegar o imposto do mês anterior
		cal.add(Calendar.MONTH, -1);

		mesAno = mesAno.concat(String.valueOf(cal.get(Calendar.MONTH) + 1)).concat("/")
				.concat(String.valueOf(cal.get(Calendar.YEAR)));

		mesAno = ajudarData(mesAno);

		List<Object[]> objs = null;
		Lancamentos lanc = null;
		try {
			// Thread.sleep(100000);
			if (segurado != null) {
				objs = lancamentosDao.getMovimentoPorCategoriaSeguradoImposto(mesAno, segurado);
				System.out.println(
						"1 getMovimentoPorCategoriaSegurado() -> " + objs.size() + " -> " + mesAno + " -> " + segurado);

				// Thread.sleep(100000);
				lanc = new Lancamentos();
				lanc.setMesAno("");
				for (Object[] obj : objs) {
					lanc.setMesAno((String) obj[0]);
					lanc.setComissao((Double) obj[1]);
				}
			} else {
				objs = lancamentosDao.getMovimentoPorCategoriaSeguradoImposto(mesAno);
				System.out.println("2 getMovimentoPorCategoriaSegurado() -> " + objs.size() + " -> " + mesAno);
				lanc = new Lancamentos();
				lanc.setMesAno("");
				if (objs.size() > 0) {
					for (Object[] obj : objs) {
						lanc.setMesAno((String) obj[0]);
						lanc.setComissao((Double) obj[1]);
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lanc;
	}

	public String ajudarData(String data) {
		String dat = "0";

		if (data.length() == 6) {
			dat = dat.concat(data);
		} else {
			dat = data;
		}
		return dat;
	}

	public static void main(String[] args) throws Exception {

		DespesasXls des = new DespesasXls();
		String caminho = "E:\\Adriana\\Despesas\\Despesas Ano 2019 Corretora MP 01 a 12 (1).xls";
		des.importarDespesas(caminho);
	}
}
