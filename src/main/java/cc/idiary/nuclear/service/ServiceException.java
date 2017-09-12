package cc.idiary.nuclear.service;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 8166076676383969322L;

	public ServiceException() {
		super("System error !");
	}

	public ServiceException(String message) {
		super(message);
	}
	
	public int gcd(int a, int b) {
		if (b == 0) {
			return a;
		} else {
			return gcd(b, a % b);
		}
	}
}
