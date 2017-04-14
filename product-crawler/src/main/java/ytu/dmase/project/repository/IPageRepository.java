package ytu.dmase.project.repository;

import java.util.Collection;

import ytu.dmase.project.model.webpage.WebPage;

public interface IPageRepository {
	
	WebPage getRandom();
	Collection<WebPage> findByUrl();
	Collection<WebPage> findAll();
	int count();
	
}
