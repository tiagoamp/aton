package br.com.tiagoamp.aton.model.to;

import br.com.tiagoamp.aton.model.MessaType;

public class MessageTO {

	public MessageTO(String text, MessaType type) {
		this.text = text;
		this.type = type;
	}
	
	
	private String text;
	private MessaType type;
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MessaType getType() {
		return type;
	}
	public void setType(MessaType tipo) {
		this.type = tipo;
	}
	
}

