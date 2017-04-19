package ytu.dmase.project.service;

import java.net.URL;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ytu.dmase.project.parser.IProductParser;

public class ParserService implements IParserService {

	private static final Logger _logger = LoggerFactory.getLogger(ParserService.class);
	
	private Set<IProductParser> _parsers;
	
	@Inject
	public ParserService(Set<IProductParser> parsers)
	{
		_parsers = parsers;
	}
	
	@Override
	public IProductParser getParserFor(URL url) {
		
		for (IProductParser parser : _parsers) {
			
			String parserHost = parser.get_domain().getHost();
			String targetHost = url.getHost();
			
			if(parserHost.equals(targetHost))
				return parser;
			
		}
		
		_logger.warn("Parser for url '%s' couldn't found.", url);
		return null;
	}

}
