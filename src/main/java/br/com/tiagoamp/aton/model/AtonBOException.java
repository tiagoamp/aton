package br.com.tiagoamp.aton.model;

/**
 * Exception to handle business logic exception.
 * 
 * @author tiagoamp
 */
public class AtonBOException extends Exception {
	
	public AtonBOException() {		
	}
	
	public AtonBOException(String message) {
		this.msg = message;
	}
	
	public AtonBOException(Throwable exceptionCause){
		super(exceptionCause);
	}
	
	public AtonBOException(String msg, Throwable exceptionCause){
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
