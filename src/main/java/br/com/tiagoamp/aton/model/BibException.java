package br.com.tiagoamp.aton.model;

/**
 * Exception to handle business logic exception.
 * 
 * @author tiagoamp
 */
public class BibException extends Exception {
	
	public BibException() {		
	}
	
	public BibException(String message) {
		this.msg = message;
	}
	
	public BibException(Throwable exceptionCause){
		super(exceptionCause);
	}
	
	public BibException(String msg, Throwable exceptionCause){
		super(msg, exceptionCause);
	}

	private static final long serialVersionUID = 4147593836582665366L;
	
	
	private String msg;
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
