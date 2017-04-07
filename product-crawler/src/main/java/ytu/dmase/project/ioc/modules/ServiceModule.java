package ytu.dmase.project.ioc.modules;

import com.google.inject.AbstractModule;

import ytu.dmase.project.service.IProductService;
import ytu.dmase.project.service.ProductService;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(IProductService.class).to(ProductService.class);
	}	

	
}
