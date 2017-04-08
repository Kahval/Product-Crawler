package ytu.dmase.project;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import com.google.inject.Inject;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import ytu.dmase.project.crawler.ProductCrawler;
import ytu.dmase.project.ioc.modules.RepositoryModule;

public class CrawlerController {
	
	private Set<ProductCrawler> _crawlers;
	
	private CrawlConfig _config;
	private RobotstxtConfig _robotstxtConfig;
	private RobotstxtServer _robotstxtServer;
	private PageFetcher _pageFetcher;
	
	@Inject
	public CrawlerController(Set<ProductCrawler> crawlers) throws Exception
	{
		_config = readConfig("/crawl-config.properties");
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
	
	private CrawlConfig readConfig(String path)
	{
		CrawlConfig config = new CrawlConfig();
		InputStream is = RepositoryModule.class.getResourceAsStream(path);
		Properties prop = new Properties();
		
		try
		{
			prop.load(is);
			
			config.setCrawlStorageFolder(prop.getProperty("CRAWL_STORAGE"));
			config.setResumableCrawling(prop.get("RESUMABLE").equals("true"));
			config.setMaxDepthOfCrawling(Integer.parseInt(prop.getProperty("MAX_DEPTH")));
			config.setMaxPagesToFetch(Integer.parseInt(prop.getProperty("MAX_PAGES")));
			config.setPolitenessDelay(Integer.parseInt(prop.getProperty("POLITENESS_DELAY")));
			config.setIncludeHttpsPages(prop.get("INCLUDE_HTTPS").equals("true"));
			config.setIncludeBinaryContentInCrawling(prop.get("INCLUDE_BINARY_CONTENT").equals("true"));
			config.setProcessBinaryContentInCrawling(prop.get("PROCESS_BINARY_CONTENT").equals("true"));
			config.setMaxConnectionsPerHost(Integer.parseInt(prop.getProperty("MAX_CONNECTION_PER_HOST")));
			config.setMaxTotalConnections(Integer.parseInt(prop.getProperty("MAX_TOTAL_CONNECTIONS")));
			config.setSocketTimeout(Integer.parseInt(prop.getProperty("SOCKET_TIMEOUT")));
			config.setConnectionTimeout(Integer.parseInt(prop.getProperty("CONNECTION_TIMEOUT")));
			config.setMaxOutgoingLinksToFollow(Integer.parseInt(prop.getProperty("MAX_OUTGOING_LINKS")));
			config.setMaxDownloadSize(Integer.parseInt(prop.getProperty("MAX_DOWNLOAD_SIZE")));
			config.setFollowRedirects(prop.get("FOLLOW_REDIRECTS").equals("true"));
			config.setOnlineTldListUpdate(prop.getProperty("ONLINE_TLD_LIST_UPDATE").equals("true"));
			config.setShutdownOnEmptyQueue(prop.getProperty("SHUTDOWN_ON_EMPTY_QUEUE").equals("true"));
			config.setThreadMonitoringDelaySeconds(Integer.parseInt(prop.getProperty("THREAD_MONITORING_DELAY")));
			config.setThreadShutdownDelaySeconds(Integer.parseInt(prop.getProperty("THREAD_SHUTDOWN_DELAY")));
			config.setCleanupDelaySeconds(Integer.parseInt(prop.getProperty("CLEANUP_DELAY")));
			
			String proxyHost = prop.getProperty("PROXY_HOST");
			if(!proxyHost.equals(""))
			{
				config.setProxyHost(proxyHost);
				config.setProxyUsername(prop.getProperty("PROXY_USERNAME"));
				config.setProxyPassword(prop.getProperty("PROXY_PASSWORD"));
				config.setProxyPort(Integer.parseInt(prop.getProperty("PROXY_PORT")));
			}
			
			is.close();
			
			return config;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		
		return null;
	}
}
