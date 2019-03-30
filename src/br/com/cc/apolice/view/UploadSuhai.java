package br.com.cc.apolice.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import br.com.cc.apolice.ApoliceSuhai;
import br.com.cc.apolice.servico.ApoliceService;

@ManagedBean
@ViewScoped
public class UploadSuhai extends Upload implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean flagHabilita = false;
	private String seguradora;

	public UploadSuhai() {
		super(new ApoliceService(new ApoliceSuhai()));
		seguradora = "Suhai";
	}

	public void handleFileUpload(FileUploadEvent event) {
		Properties props = new Properties();
		if (event != null) {

			try {
				props.load(getClass().getResourceAsStream("/apolice.properties"));
				super.upload(event, props.getProperty("dir.apolices").toString());
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload do arquivo ",
						event.getFile().getFileName() + " realizado com sucesso");
				FacesContext.getCurrentInstance().addMessage(null, message);
				setFlagHabilita(true);
			} catch (IOException ex) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!",
						" Não foi possivel fazer upload do arquivo " + event.getFile().getFileName() + " "
								+ ex.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public void processarPdf(){
		super.processarPdf(this.seguradora);
		setFlagHabilita(false);
	}

	public boolean isFlagHabilita() {
		return flagHabilita;
	}

	public void setFlagHabilita(boolean flagHabilita) {
		this.flagHabilita = flagHabilita;
	}
}
