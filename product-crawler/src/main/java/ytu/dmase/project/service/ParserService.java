package ytu.dmase.project.service;

import java.net.URL;

import ytu.dmase.project.parser.IProductParser;
import ytu.dmase.project.parser.ParserTeknosa;

public class ParserService implements IParserService {

	@Override
	public IProductParser getParserFor(URL url) {

		return new ParserTeknosa();
	}

}
