package ytu.dmase.project.ioc.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import ytu.dmase.project.service.IPageHandler;
import ytu.dmase.project.service.IParserService;
import ytu.dmase.project.service.IProductService;
import ytu.dmase.project.service.PageHandler;
import ytu.dmase.project.service.ParserService;
import ytu.dmase.project.service.ProductService;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(IProductService.class).to(ProductService.class);
		bind(IPageHandler.class).to(PageHandler.class);
		bind(IParserService.class).to(ParserService.class).in(Singleton.class);;
	}	

	
}
