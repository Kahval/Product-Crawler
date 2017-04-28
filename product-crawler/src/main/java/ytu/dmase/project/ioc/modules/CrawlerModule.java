package ytu.dmase.project.ioc.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import ytu.dmase.project.crawler.HepsiburadaCrawler;
import ytu.dmase.project.crawler.ProductCrawler;
import ytu.dmase.project.crawler.TeknosaCrawler;
import ytu.dmase.project.crawler.VatanBilgisayarCrawler;

public class CrawlerModule extends AbstractModule {

	@Override
	protected void configure() {
		Multibinder<ProductCrawler> crawlerBinder = Multibinder.newSetBinder(binder(), ProductCrawler.class);
		
		// Crawlers here
		crawlerBinder.addBinding().to(TeknosaCrawler.class);
		//crawlerBinder.addBinding().to(N11Crawler.class);
		crawlerBinder.addBinding().to(HepsiburadaCrawler.class);
		crawlerBinder.addBinding().to(VatanBilgisayarCrawler.class);
		//crawlerBinder.addBinding().to(TekzenCrawler.class);
		
		//bind(CrawlerController.class).to(CrawlerController.class);
	}

}
