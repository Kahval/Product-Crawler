package ytu.dmase.project.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.Page;
import ytu.dmase.project.crawler.TeknosaCrawler;
import ytu.dmase.project.model.product.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

public class TeknosaCrawlerTest {

	@Test
	public void testProductParsing() throws IOException {
		
		File file = new File("src/test/resources/teknosa_telekom.txt");
		byte[] pageContentData = Files.readAllBytes(file.toPath());
		
		Page mockedPage = mock(Page.class);
		when(mockedPage.getContentData()).thenReturn(pageContentData);
		when(mockedPage.getContentCharset()).thenReturn("utf-8");
		
		TeknosaCrawler teknosaCrawler = new TeknosaCrawler(null);
		Collection<Product> products = teknosaCrawler.parseProducts(mockedPage);
		
		assertTrue(products.size() == 21);
	}

}
