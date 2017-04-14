package ytu.dmase.project.service;

import java.net.URL;

import ytu.dmase.project.parser.IProductParser;

public interface IParserService {
	
	IProductParser getParserFor(URL url);
	
}
