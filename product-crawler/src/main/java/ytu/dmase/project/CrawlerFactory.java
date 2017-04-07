package ytu.dmase.project;

import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import ytu.dmase.project.crawler.ProductCrawler;

public class CrawlerFactory implements WebCrawlerFactory<ProductCrawler> {

	private ProductCrawler _crawler;

	public CrawlerFactory(ProductCrawler crawler)
	{
		_crawler = crawler;
	}
	
	public ProductCrawler newInstance() throws Exception {

		return _crawler;
	}
	
}
