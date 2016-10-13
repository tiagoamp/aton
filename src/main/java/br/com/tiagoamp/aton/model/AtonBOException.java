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
		this.msg = exceptionCause.getMessage();
	}
	
	public AtonBOException(String msg, Throwable exceptionCause){
		super(msg, exceptionCause);
		this.msg = msg;
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
