package ytu.dmase.project.parser;

public class ProductParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductParseException(String msg)
	{
		super(msg);
	}
	
	public ProductParseException(String msg, Exception e)
	{
		super(msg, e);
	}

}
