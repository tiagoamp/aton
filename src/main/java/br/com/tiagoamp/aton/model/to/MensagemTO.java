package br.com.tiagoamp.aton.model.to;

import br.com.tiagoamp.aton.model.TipoMensagem;

public class MensagemTO {

	public MensagemTO(String mensagem, TipoMensagem tipo) {
		this.mensagem = mensagem;
		this.tipo = tipo;
	}
	
	
	private String mensagem;
	private TipoMensagem tipo;
	
	
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public TipoMensagem getTipo() {
		return tipo;
	}
	public void setTipo(TipoMensagem tipo) {
		this.tipo = tipo;
	}
	
}

