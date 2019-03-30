package br.com.cc.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.NoResultException;

import org.primefaces.event.RowEditEvent;

import br.com.cc.auditoria.AuditoriaService;
import br.com.cc.dao.LancamentosDao;
import br.com.cc.entidade.Categoria;
import br.com.cc.entidade.Lancamentos;
import br.com.cc.entidade.SubTipo;
import br.com.cc.facade.CategoriaFacade;
import br.com.cc.util.Util;

@ViewScoped
@ManagedBean
public class CategoriaView extends AbstractMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Categoria categoria = new Categoria();
	private SubTipo subtipo = new SubTipo();
	private Lancamentos lancamentoMB = new Lancamentos();
	private List<Categoria> categorias = new LinkedList<Categoria>();
	private List<Categoria> categoriasSum = new LinkedList<Categoria>();
	private CategoriaFacade categoriaFacade;
	private String tipo2;
	private String subTipo = "";
	private Map<String, String> tipos = new HashMap<String, String>();
	private Date dataInicial;
	private Date dataFinal;
	private Double sumTotal = 0.0;
	private LancamentosDao lancamentosDao;
	
	@ManagedProperty(value = "#{usuarioLoginBean}")
	private UsuarioLoginBean usuarioLoginBean;

	public CategoriaFacade getCategoriaFacade() {
		// if (categoriaFacade == null) {
		categoriaFacade = new CategoriaFacade();
		// }
		return categoriaFacade;
	}

	public LancamentosDao getLancamentoDao() {
		if (lancamentosDao == null) {
			lancamentosDao = new LancamentosDao();
		}
		return lancamentosDao;
	}

	public SubTipo getSubtipo() {
		if (subtipo == null) {
			subtipo = new SubTipo();
		}
		return subtipo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	private void resetCategoria() {
		categoria = new Categoria();
		lancamentoMB = new Lancamentos();
	}

	private void carregarCategorias() {
		System.out.println("carregarCategorias");

		try {
			categorias = getCategoriaFacade().listarCategorias();
			//Categoria categoria = lancamentosConcatenados();
			//categorias.add(categoria);

			listarCategoriasSum();
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar carregar categorias " + e.getMessage());
			e.printStackTrace();
		}
	}

	private Categoria lancamentosConcatenados() {
		Categoria cat = new Categoria();
		int i = 0;
		if (!categorias.isEmpty() && categorias.size() > 0) {
			for (i = 0; i < categorias.size(); i++) {
				for (Lancamentos l : categorias.get(i).getLancamentos()) {

					//cat.addLancamentos(l);
					cat.getLancamentos().add(l);
				}				
			}
			cat.setTipo("TODOS");
		}

		return cat;
	}

	public void carregarTipos() {
		List<SubTipo> lst = categoria.getSubTipo();
		System.out.println("Categoria: " + categoria.getTipo());
		tipos = new HashMap<String, String>();
		for (SubTipo s : lst) {
			tipos.put(s.getDescricao(), s.getDescricao());
		}
	}

	public String adicionarCategoriaComSub() {
		Categoria cat = null;
		try {
			subtipo.setDescricao(subtipo.getDescricao().toUpperCase());
			categoria.addSubTipo(subtipo);
			getCategoriaFacade().adicionarCategoriaComSub(categoria);
			// Clonar -> inicio
			if (subtipo.isClonar()) {
				cat = getCategoriaFacade().getCategoriaPorTipo("JAQUE");
				if (subtipo.getPercentagem() > 0) {
					subtipo.setPercentagem(100 - subtipo.getPercentagem());
				}
				cat.addSubTipo(subtipo);
				if (cat != null) {
					getCategoriaFacade().adicionarCategoriaComSub(cat);
				}
			}
			// Clonar -> fim
			closeDialog();
			displayInfoMessageToUser("Criada com sucesso");
			resetCategoria();
		} catch (NoResultException e) {
			System.out.println("Sem categoria geral");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar adicionar as sub-categorias " + e.getMessage());
			e.printStackTrace();
		}
		return "cadastrarcategoriaview.jsf";
	}

	public String adicionarCategoria() {
		System.out.println("adicionarCategoria");
		if (tipo2.isEmpty()){
			return "cadastrarcategoriaview.jsf";
		}
		try {
			categoria.setTipo(tipo2.toUpperCase());
			getCategoriaFacade().adicionarCategoria(categoria);
			closeDialog();
			displayInfoMessageToUser("Categoria criada com sucesso");
			resetCategoria();
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar adicinar a categoria " + e.getMessage());
			e.printStackTrace();
		}
		return "cadastrarcategoriaview.jsf";
	}

	private void atualizarMovimentoComSubTipo() {
		System.out.println("atualizarMovimentoComSubTipo");
		LancamentosDao dao = new LancamentosDao();
		try {
			lancamentoMB.setSubTipo(getCategoriaFacade().getSubTipoPorDescricao(subtipo.getDescricao(), categoria));
			lancamentoMB.setClassificacao(2);
			dao.atualizarLancamento(lancamentoMB);
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar atualizar o movimento " + e.getMessage());
			e.printStackTrace();
		}

	}

	
	public void adicionarItemMovimentatacao(){
		int index = 0;
		this.categoria.getTipo();
		if (this.categorias.size() > 0){
			for (Categoria c : categorias){
				if (c.getTipo() == this.categoria.getTipo()){
					this.categorias.get(index).addLancamentos(lancamentoMB);
				}
				index++;
			}
		}
	}
	
	public void adicionarMovimento() {
		System.out.println("adicionarMovimento");
		Date dt = Calendar.getInstance().getTime();
		try {
			if (lancamentoMB.getPeriodo().after(dt)) {
				lancamentoMB.setStatus("Agendado");
			}
			lancamentoMB.setDataIncluisao(dt);
			lancamentoMB.setClassificacao(2);
			lancamentoMB.setComissao(Util.novoValor(lancamentoMB.getValor3()));
			lancamentoMB.setValor1(Util.novoValor(lancamentoMB.getValor3()));
			lancamentoMB.setValor3(Util.novoValor(lancamentoMB.getValor3()));
			categoria.addLancamentos(lancamentoMB);
			getCategoriaFacade().adicionarCategoria(categoria);
			
			adicionarItemMovimentatacao();
			
			atualizarMovimentoComSubTipo();
			closeDialog();
			displayInfoMessageToUser("Movimento ["+ this.subtipo.getDescricao() +"] inserido com sucesso");
			resetCategoria();
			listarCategoriasPorFiltro();
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar cadastra o movimento. " + e.getMessage());
			e.printStackTrace();
		}

		//return "cadastrarmovimentacoesview.jsf";
	}

	public String removerSubtipo() {
		try {
			System.out.println("Descricao: " + subtipo.getDescricao());
			subtipo = categoriaFacade.getSubTipo(subtipo.getId());
			getCategoriaFacade().removerSubTipo(subtipo);
			subtipo = new SubTipo();
			closeDialog();
			displayInfoMessageToUser("Removida com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar remover a sub-categoria " + e.getMessage());
			e.printStackTrace();
		}
		return "cadastrarcategoriaview.jsf";
	}

	public void removerMovimento() {
		System.out.println("removerMovimento");
		try {
			
			AuditoriaService aud = new AuditoriaService();
			aud.adicionarLogAuditoria(lancamentoMB, getUsuarioLoginBean().getUsuarioLogador(), "Deletado");
			aud = null;
			
			lancamentoMB = getLancamentoDao().getLancamento(lancamentoMB.getId());
			getLancamentoDao().removerLancamento(lancamentoMB);
			
			lancamentoMB = new Lancamentos();
			listarCategoriasPorFiltro();
			closeDialog();
			displayInfoMessageToUser("Removida com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar remover o movimento " + e.getMessage());
			e.printStackTrace();
		}
		//return "cadastrarmovimentacoesview.jsf";
	}

	public String removerCategoria() {
		if (verificaDependencia()){
			return "cadastrarcategoriaview"; 
		}
		try {
			this.categoria = getCategoriaFacade().getCategoria(categoria.getId());
			getCategoriaFacade().removerCategoria(this.categoria);
			categoria = new Categoria();
			closeDialog();
			displayInfoMessageToUser("Removida com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar remover categoria " + e.getMessage());
			e.printStackTrace();
		}
		return "cadastrarcategoriaview";
	}
	
	private boolean verificaDependencia(){
		if (categoria.getSubTipo().size() > 0){
			closeDialog();
			displayInfoMessageToUser("A categoria "+categoria.getTipo()+" nao pode ser excluida porque há subcategorias vinculadas.");
			return true;
		}
		return false;
	}

	public List<Categoria> listarCategorias() {
		if (categorias.isEmpty()) {
			carregarCategorias();
			System.out.println("Tamanho: " + categorias.size());
		}
		return categorias;
	}

	public String listarCategoriasPorFiltro() {
		categorias.clear();
		Util.verificaMemoria("ccomissao-listarCategoriasPorFiltro");
		try {
			if (dataInicial != null && dataFinal != null && subTipo.equals("")) {
				System.out.println("Filtro");
				categorias = null;
				categoriaFacade = null;

				categorias = getCategoriaFacade().listarCategoriaPorData(dataInicial, dataFinal);
				List<Categoria> list = getCategoriaFacade().listarCategorias();
				for (Categoria cat : categorias) {
					list.remove(cat);
				}

				for (Categoria c : list){
					c.setLancamentos(new ArrayList<Lancamentos>());
					categorias.add(c);
				}
				
				//Categoria cat = lancamentosConcatenados();
				//categorias.add(cat);
				// removerItemForaDaData(dataInicial, dataFinal);
				listarCategoriasSum();

			} else if (dataInicial != null && dataFinal != null && !subTipo.equals("")) {
				categorias = null;
				categoriaFacade = null;

				categorias = getCategoriaFacade().listarCategoriaPorDataESubTipo(dataInicial, dataFinal, subTipo);
				List<Categoria> list = getCategoriaFacade().listarCategorias();
				for (Categoria cat : categorias) {
					list.remove(cat);
				}

				for (Categoria c : list){
					c.setLancamentos(new ArrayList<Lancamentos>());
					categorias.add(c);
				}
				
				
				//Categoria cat = lancamentosConcatenados();
				//categorias.add(cat);
				listarCategoriasSum();

			} else if (dataInicial == null && dataFinal == null && !subTipo.equals("")) {
				categorias = null;
				categoriaFacade = null;

				categorias = getCategoriaFacade().listarCategoriaPorSubTipo(subTipo);
				
				List<Categoria> list = getCategoriaFacade().listarCategorias();
				for (Categoria cat : categorias) {
					list.remove(cat);
				}

				for (Categoria c : list){
					c.setLancamentos(new ArrayList<Lancamentos>());
					categorias.add(c);
				}
				
				//Categoria cat = lancamentosConcatenados();
				//categorias.add(cat);
				listarCategoriasSum();

			} else {
				categorias = new LinkedList<Categoria>();
				listarCategorias();
			}
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar listar categorias por filtro " + e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void listarCategoriasSum() {
		categoriasSum = new LinkedList<Categoria>();
		int tam = categorias.size();
		int index = 0;
		double valor = 0.0;
		sumTotal = 0.0;

		if (tam > 0) {
			String[] tipos = new String[tam + 1];
			for (Categoria c : categorias) {
				Categoria sum = new Categoria();
				tipos[index] = c.getTipo();
				for (Lancamentos l : c.getLancamentos()) {
					valor += l.getValor3();
				}
				sum.setTipo(tipos[index]);
				sum.setValorSum(valor);
				sumTotal += sum.getValorSum();
				categoriasSum.add(sum);
				valor = 0.0;
				index++;
			}
		}
	}

	/**
	 * Metodos dinamicos
	 * 
	 * @param categorias
	 */
	public void onEdit(RowEditEvent event) {
		try {
			categoriaFacade.atualizarCategoria(((Categoria) event.getObject()));
			closeDialog();
			displayInfoMessageToUser("Atualizada com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar atualizar a categoria " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void onCancel(RowEditEvent event) {
		System.out.println("Ildson: "+((Categoria) event.getObject()).getTipo());
		try {
			categoriaFacade.removerCategoria((Categoria) event.getObject());
			closeDialog();
			displayInfoMessageToUser("Cancelada atualizacao");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar excluir a categoria " + e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void onEditSub(RowEditEvent event) {
		try {
			categoriaFacade.atualizarSubCategoria(((SubTipo) event.getObject()));
			closeDialog();
			displayInfoMessageToUser("Atualizada com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar atualizar a subcategoria " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void onCancelSub(RowEditEvent event) {
		closeDialog();
		displayInfoMessageToUser("Cancelada atualizacao");
	}

	public void controlarValores(Integer id, Lancamentos lanc) throws Exception{

		if (Double.compare(Util.arredondar(lanc.getComissao()), Util.arredondar(lanc.getValorBruto())) != 0 ){
			lanc.setComissao(Util.novoValor(lanc.getComissao()));
		}
		if (Double.compare(Util.arredondar(lanc.getValor1()), Util.arredondar(lanc.getValorDespesa())) != 0 ){
			lanc.setValor1(Util.novoValor(lanc.getValor1()));
		}
		if (Double.compare(Util.arredondar(lanc.getValor3()), Util.arredondar(lanc.getValorLiquido())) != 0 ){
			lanc.setValor3(Util.novoValor(lanc.getValor3()));
		}
	}
	
	public void onEditMov(RowEditEvent event) {
		carregarTipos();
		try {
			Lancamentos lanc = (Lancamentos) event.getObject();
			controlarValores(lanc.getId(), lanc);

			LancamentosDao dao = new LancamentosDao();
			Lancamentos anterior = dao.getLancamento(lanc.getId());
			dao = null;
			
			getLancamentoDao().atualizarLancamento(lanc);
			
			AuditoriaService aud = new AuditoriaService();
			aud.adicionarLogAuditoria(lanc, anterior, getUsuarioLoginBean().getUsuarioLogador(), "Atualizacao");
			aud = null;
			
			closeDialog();
			displayInfoMessageToUser("Atualizada com sucesso");
		} catch (Exception e) {
			keepDialogOpen();
			displayErrorMessageToUser("Erro ao tentar atualizar a movimento " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void onCancelMov(RowEditEvent event) {
		closeDialog();
		displayInfoMessageToUser("Cancelada atualizacao");
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setCategoriaFacade(CategoriaFacade categoriaFacade) {
		this.categoriaFacade = categoriaFacade;
	}

	public void setSubtipo(SubTipo subtipo) {
		this.subtipo = subtipo;
	}

	public String getTipo2() {
		return tipo2;
	}

	public void setTipo2(String tipo) {
		this.tipo2 = tipo;
	}

	public Map<String, String> getTipos() {
		return tipos;
	}

	public void setTipos(Map<String, String> tipos) {
		this.tipos = tipos;
	}

	public Lancamentos getLancamentoMB() {
		return lancamentoMB;
	}

	public void setLancamentoMB(Lancamentos lancamentoMB) {
		this.lancamentoMB = lancamentoMB;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<Categoria> getCategoriasSum() {
		return categoriasSum;
	}

	public void setCategoriasSum(List<Categoria> categoriasSum) {
		this.categoriasSum = categoriasSum;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public Double getSumTotal() {
		BigDecimal bd = new BigDecimal(sumTotal);
		return bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}

	public void setSumTotal(Double sumTotal) {
		this.sumTotal = sumTotal;
	}

	public String getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}

	public static void main(String[] args) {
		CategoriaView cv = new CategoriaView();
		cv.carregarCategorias();
		cv.lancamentosConcatenados();
	}

	public UsuarioLoginBean getUsuarioLoginBean() {
		return usuarioLoginBean;
	}

	public void setUsuarioLoginBean(UsuarioLoginBean usuarioLoginBean) {
		this.usuarioLoginBean = usuarioLoginBean;
	}
}
