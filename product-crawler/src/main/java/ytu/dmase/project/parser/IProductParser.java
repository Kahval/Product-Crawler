package ytu.dmase.project.parser;

import java.net.URL;
import java.util.Collection;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;

public interface IProductParser {
	
	Collection<Product> parse(WebPage webPage) throws ProductParseException;
	URL get_domain();
}
