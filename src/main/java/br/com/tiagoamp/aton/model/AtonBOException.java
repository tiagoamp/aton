package br.com.tiagoamp.aton.model;

/**
 * Exception to handle business logic exception.
 * 
 * @author tiagoamp
 */
public class AtonBOException extends Exception {
	
	public AtonBOException() {		
	}	
	
	public AtonBOException(String businessMessage) {
		this.businessMessage = businessMessage;
	}
	
	public AtonBOException(Throwable exceptionCause){
		super(exceptionCause);
		this.businessMessage = exceptionCause.getMessage();		
	}
	
	public AtonBOException(String businessMessage, Throwable exceptionCause){
		super(businessMessage, exceptionCause);
		this.businessMessage = businessMessage;
	}

	
	private static final long serialVersionUID = 4147593836582665366L;
		
	private String businessMessage;
	
	
	@Override
	public String toString() {
		return "Exception: " + businessMessage + " - " + super.toString(); 
	}
	
	
	public String getBusinessMessage() {
		return businessMessage;
	}
	public void setBusinessMessage(String businessMessage) {
		this.businessMessage = businessMessage;
	}

}
