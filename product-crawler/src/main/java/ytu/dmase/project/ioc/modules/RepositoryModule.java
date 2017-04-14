package ytu.dmase.project.ioc.modules;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.sis.internal.jdk7.Objects;
import org.apache.tika.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

import ytu.dmase.project.repository.IProductRepository;

public class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		Flyway flyway = new Flyway();
		flyway.setDataSource(new DataSourceProvider().get());
		//flyway.setBaselineOnMigrate(true);
		flyway.migrate();
		
	    bind(DataSource.class).toProvider(DataSourceProvider.class);
		bind(IProductRepository.class).to(ytu.dmase.project.repository.pgsql.ProductRepository.class);
		//bind(IProductRepository.class).to(ytu.dmase.project.repository.memory.ProductRepository.class);
	}
}

class DataSourceProvider implements Provider<DataSource>
{
	private static final Logger _logger = LoggerFactory.getLogger(DataSourceProvider.class);
	
	private String _dbconfig = "/database-config.properties";
	
	@Override
	public DataSource get() {
		
		Properties prop = new Properties();
		
		InputStream is = RepositoryModule.class.getResourceAsStream(_dbconfig);
		
		try {
			prop.load(is);
		} catch (IOException e) {
			_logger.error("Couldn't load database config file. '%s'", _dbconfig);
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
		
		String driverName = prop.getProperty("driver");
		Objects.requireNonNull("In database config file, there is no 'driver' parameter.");
		
		String username = prop.getProperty("user");
		Objects.requireNonNull("In database config file, there is no 'user' parameter.");
		
		String password = prop.getProperty("password");
		Objects.requireNonNull("In database config file, there is no 'password' parameter.");
		
		String url = prop.getProperty("url");
		Objects.requireNonNull("In database config file, there is no 'url' parameter.");
		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		
		return ds;
	}
}
