package ytu.dmase.project;


import com.google.inject.*;

import ytu.dmase.project.ioc.*;

public class Program {

	public static void main(String[] args) throws Exception {
		
		IoC ioc = new IoC();
		Injector injector = ioc.get_injector();
		
		CrawlerController crawlerController = injector.getInstance(CrawlerController.class);
		crawlerController.Start();
		
	}

}
