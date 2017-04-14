package ytu.dmase.project.repository;

public class RepositoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RepositoryException(String msg)
	{
		super(msg);
	}
	
	public RepositoryException(String msg, Exception innerException)
	{
		super(msg, innerException);
	}
	
}
