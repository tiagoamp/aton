package br.com.tiagoamp.aton.model.to;

public class AuthorizerResultTO {
	
	public AuthorizerResultTO() {		
	}
	
	public AuthorizerResultTO(String urlRedirect, String msgErro) {
		this.urlRedirect = urlRedirect;
		this.msgErro = msgErro;
	}

	private String urlRedirect;
	private String msgErro;
	
	
	public String getUrlRedirect() {
		return urlRedirect;
	}
	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}

	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

}
