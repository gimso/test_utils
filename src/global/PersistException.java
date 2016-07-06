package global;

public class PersistException extends Exception {

	private static final long serialVersionUID = -3881174829366362873L;

	private String errorMessgae;

	public PersistException() {
	}

	public PersistException(String errorMessage) {
		this.errorMessgae = errorMessage;
	}

	@Override
	public String getMessage() {
		if (super.getMessage() != null) {
			return super.getMessage() + "\n" + errorMessgae;
		} else {
			return errorMessgae;
		}
	}

	@Override
	public String getLocalizedMessage() {
		if (super.getLocalizedMessage() != null) {
			return super.getLocalizedMessage() + "\n" + errorMessgae;
		} else {
			return errorMessgae;
		}
	}

	@Override
	public String toString() {
		return "PersistException [errorMessgae=" + errorMessgae + "]";
	}

}
