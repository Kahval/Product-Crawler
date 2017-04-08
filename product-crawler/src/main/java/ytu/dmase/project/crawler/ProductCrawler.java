package ytu.dmase.project.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.service.IProductService;

public abstract class ProductCrawler extends WebCrawler {
	
	private IProductService _service;
	
	// Holds the number of products crawled.
	private int _productCounter = 0;
	
	// Holds the number of pages visited.
	private int _pageCounter = 0;
	
	protected ProductCrawler(IProductService service)
	{
		_service = service;
	}
	
	protected void handOver(Product...products)
	{
		_service.takeProduct(products);
		_productCounter++;
	}
	
	@Override
	public void visit(Page page) {
		
		_pageCounter++;
		super.visit(page);
	}
		
	public abstract String get_crawlerDomain();

	public int get_productCounter() {
		return _productCounter;
	}

	public int get_pageCounter() {
		return _pageCounter;
	}
}
