package ytu.dmase.project.ioc.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ytu.dmase.project.parser.IProductParser;
import ytu.dmase.project.parser.ParserTeknosa;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		
		Multibinder<IProductParser> parserBinder = Multibinder.newSetBinder(binder(), IProductParser.class);
		
		parserBinder.addBinding().to(ParserTeknosa.class);
		//parserBinder.addBinding().to(ParserHepsiburada.class);
	}
	
	
	
}
