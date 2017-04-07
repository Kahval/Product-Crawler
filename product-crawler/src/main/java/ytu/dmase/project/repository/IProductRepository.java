package ytu.dmase.project.repository;

import java.net.URL;
import java.util.UUID;

import ytu.dmase.project.model.product.*;

public interface IProductRepository {
	
	Product GetById(UUID uuid);
	Product GetById(String uuidString);
	Product GetByUrl(URL url);
	Product GetByUrl(String urlString);
	Iterable<Product> FindByCategory(Category category);
	Iterable<Product> FindByKeywords(String...keywords);
	Iterable<Product> FindByName(String productName);
	
	void Save(Product product);
	void Update(Product product);
	void Delete(Product product);
}
