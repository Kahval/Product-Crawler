package ytu.dmase.project.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;

public class ParserHepsiburada implements IProductParser{

	@Override
	public Collection<Product> parse(WebPage webPage) throws ProductParseException {
		Collection<Product> products = new ArrayList<Product>();
		Document doc = Jsoup.parse(webPage.get_html());
		Elements productListElements = doc.select(".product-list li.search-item");
		for (Element elem : productListElements) {
			String productName 	= elem.select(".product-title").text();
			String urlString = "http://www.hepsiburada.com" + elem.select("a").attr("href");
			URL url;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {		
				throw new ProductParseException("Error when parsing product url.", e);
			}
			String priceString 	= elem.select(".price:not(.old)").text();
			if(!priceString.equals(""))
			{
				priceString	= priceString.substring(0, priceString.length()-3); // Remove TL from like "489,00 TL"
				priceString = priceString.replace(".", ""); // Remove thousand separator	
			}
			else
			{
				priceString = "-1";
			}
			Product product = null;		
			try {
				product = new Product(productName, url);
				NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
				Number number = format.parse(priceString);
				Double price = number.doubleValue();				
				product.set_price(price);
			} catch (ParseException e) {
				throw new ProductParseException("Error when parsing product price.", e);
			}
			products.add(product);
		}
		
		return products;
	}

}
