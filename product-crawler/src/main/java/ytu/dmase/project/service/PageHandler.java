package ytu.dmase.project.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;
import ytu.dmase.project.parser.IProductParser;
import ytu.dmase.project.parser.ProductParseException;

public class PageHandler implements IPageHandler {

	private final static Logger _logger = LoggerFactory.getLogger(PageHandler.class);
	
	private IParserService _parserService;
	private IProductService _productService;

	@Inject
	public PageHandler(IParserService parserService, IProductService productService)
	{
		_parserService = parserService;
		_productService = productService;
	}
	
	@Override
	public void handle(WebPage webPage) {
		
		IProductParser parser = _parserService.getParserFor(webPage.get_url());
		
		if(parser == null)
		{
			_logger.warn("No parser to parse this url : %s", webPage.get_url().toString());
			return;
		}
		
		Collection<Product> products;
		try {
			
			products = parser.parse(webPage);
			_productService.takeProduct(products);
			
		} catch (ProductParseException e) {
			_logger.error("Couldn't parse products in page %s", 
					webPage.get_url().toString(), e);;
		}
	}

}
