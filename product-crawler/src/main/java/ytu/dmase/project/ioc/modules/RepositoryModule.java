package ytu.dmase.project.ioc.modules;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import ytu.dmase.project.repository.IProductRepository;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		
	    bind(Connection.class).toProvider(ConnectionProvider.class);
		bind(IProductRepository.class).to(ytu.dmase.project.repository.pgsql.ProductRepository.class);
		//bind(IProductRepository.class).to(ytu.dmase.project.repository.memory.ProductRepository.class);
	}
}

class ConnectionProvider implements Provider<Connection>
{
	private static final Logger _logger = LoggerFactory.getLogger(ConnectionProvider.class);
	
	@Override
	public Connection get() {
		
		Properties prop = new Properties();
		
		try
		{
			Class.forName("org.postgresql.Driver");
			//InputStream is = new FileInputStream("database-config.properties");
			InputStream is = RepositoryModule.class.getResourceAsStream("/database-config.properties");
			prop.load(is);
			
			String host = prop.getProperty("host", "127.0.0.1");
			String port = prop.getProperty("port", "5432");
			String db = prop.getProperty("database");
			String user = prop.getProperty("user", "postgres");
			String password = prop.getProperty("password");
			
			String connectionString = String.format("jdbc:postgresql://%s:%s/%s",
					host, port, db);
			
			Connection connection = DriverManager.getConnection(connectionString,
					user, password);
			
			is.close();
			
			return connection;
		} 
		catch (Exception e)
		{
			_logger.error(e.getMessage());
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

		return null; // to remove ide warning.
	}
	
}
