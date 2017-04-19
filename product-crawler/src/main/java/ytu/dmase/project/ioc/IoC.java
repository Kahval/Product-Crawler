package ytu.dmase.project.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ytu.dmase.project.ioc.modules.*;

public class IoC {
	
	private Injector _injector;
	
	public IoC()
	{
		_injector = Guice.createInjector(
				new CrawlerModule(),
				new RepositoryModule(),
				new ServiceModule(),
				new ParserModule());
		
		
	}

	public Injector get_injector() {
		return _injector;
	}
}
