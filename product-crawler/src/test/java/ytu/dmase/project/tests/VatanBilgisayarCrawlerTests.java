package ytu.dmase.project.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.uci.ics.crawler4j.url.WebURL;
import ytu.dmase.project.crawler.VatanBilgisayarCrawler;

public class VatanBilgisayarCrawlerTests {

	@Test
	public void testShouldVisitFalse() {
		
		VatanBilgisayarCrawler crawler = new VatanBilgisayarCrawler(null);
		
		WebURL url = new WebURL();
		url.setURL("http://www.vatanbilgisayar.com/lenovo-ideapad-310-core-i5-7200u-2-5ghz-8gb-ram-1tb-hdd-15-6-2gb-w10.html");
		
		boolean result = crawler.shouldVisit(null, url);
		assertFalse(result);
	}
	
	@Test
	public void testShouldVisitTrue()
	{
		VatanBilgisayarCrawler crawler = new VatanBilgisayarCrawler(null);
		
		WebURL url = new WebURL();
		url.setURL("http://www.vatanbilgisayar.com/fotograf-makinesi-video-kamera/");
		
		boolean result = crawler.shouldVisit(null, url);
		assertTrue(result);
	}

}
