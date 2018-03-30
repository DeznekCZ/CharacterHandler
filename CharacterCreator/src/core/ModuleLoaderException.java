package core;

public class ModuleLoaderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2374861065603058624L;
	private String moduleName;

	public ModuleLoaderException(Exception e, String moduleName) {
		super(e);
		this.moduleName = moduleName;
		setStackTrace(e.getStackTrace());
	}

	@Override
	public void printStackTrace() {
		System.err.println(String.format("Module with name =\"%s\" has issues!", moduleName));
		super.printStackTrace();
		
	}
}
