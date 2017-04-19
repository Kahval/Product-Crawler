package ytu.dmase.project.ioc.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import ytu.dmase.project.parser.IProductParser;
import ytu.dmase.project.parser.ParserTeknosa;
import ytu.dmase.project.parser.ParserVatanBilgisayar;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {

		Multibinder<IProductParser> parserBinder = Multibinder.newSetBinder(binder(), IProductParser.class);
		
		parserBinder.addBinding().to(ParserTeknosa.class).in(Singleton.class);
		//parserBinder.addBinding().to(ParserHepsiburada.class).in(Singleton.class);
		//parserBinder.addBinding().to(ParserN11.class).in(Singleton.class);
		parserBinder.addBinding().to(ParserVatanBilgisayar.class).in(Singleton.class);
	}

}
