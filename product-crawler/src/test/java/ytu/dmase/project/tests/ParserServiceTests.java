package ytu.dmase.project.tests;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashSet;

import org.junit.Test;

import ytu.dmase.project.parser.IProductParser;
import ytu.dmase.project.parser.ParserTeknosa;
import ytu.dmase.project.service.ParserService;
import ytu.dmase.project.utils.URLUtils;

public class ParserServiceTests {

	@Test
	public void testGetParserFor() {
		
		URL url = URLUtils.makeUrl("http://www.teknosa.com/urunler/110100978/lg-loudr-fh6-dturllk-600w-bt-boom-box-ses-sistemi");
		
		HashSet<IProductParser> set = new HashSet<IProductParser>();
		IProductParser parserTeknosa = new ParserTeknosa();
		set.add(parserTeknosa);
		ParserService parserService = new ParserService(set);
		
		IProductParser parser = parserService.getParserFor(url);
		
		assertEquals(parser, parserTeknosa);
	}

}
