package ytu.dmase.project.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.model.webpage.WebPage;
import ytu.dmase.project.parser.ParserVatanBilgisayar;
import ytu.dmase.project.parser.ProductParseException;
import ytu.dmase.project.tests.utils.WebPageUtils;
import ytu.dmase.project.utils.URLUtils;

public class ParserVatanBilgisayarTests {

	@Test
	public void testParse() throws IOException, ProductParseException {

		WebPage page = WebPageUtils.createWebPageFromFile("/vatanbilgisayar_bilgisayarlar.txt", 
				URLUtils.makeUrl("http://www.vatanbilgisayar.com/bilgisayar/?page=15"));
		
		ParserVatanBilgisayar parser = new ParserVatanBilgisayar();
		Collection<Product> products = parser.parse(page);
		
		assertTrue(products.size() == 20);
	}

}
