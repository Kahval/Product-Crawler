package ytu.dmase.project;

import java.util.ArrayList;
import java.util.Set;

import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import ytu.dmase.project.crawler.ProductCrawler;

public class CrawlerController {
	
	private Set<ProductCrawler> _crawlers;
	
	private String _crawlStorageFolder = "/crawlerdata/root";
	private CrawlConfig _config;
	private RobotstxtConfig _robotstxtConfig;
	private RobotstxtServer _robotstxtServer;
	private PageFetcher _pageFetcher;
	
	@Inject
	public CrawlerController(Set<ProductCrawler> crawlers) throws Exception
	{
		_config = new CrawlConfig();
		_config.setCrawlStorageFolder(_crawlStorageFolder);
		_config.setPolitenessDelay(1000);
		
		_pageFetcher = new PageFetcher(_config);
		
		_robotstxtConfig = new RobotstxtConfig();
		_robotstxtServer = new RobotstxtServer(_robotstxtConfig, _pageFetcher);
		_crawlers = crawlers;
	}
	
	public void Start() throws Exception
	{
		ArrayList<CrawlController> controllers = new ArrayList<CrawlController>();
		for(ProductCrawler crawler : _crawlers)
		{
			CrawlController controller = new CrawlController(_config, _pageFetcher, _robotstxtServer);
			String domain = crawler.get_crawlerDomain();
			controller.addSeed(domain);
			controller.startNonBlocking(new CrawlerFactory(crawler), 1);
		}
		
		for(CrawlController controller : controllers)
		{
			controller.waitUntilFinish();
		}
	}
}
