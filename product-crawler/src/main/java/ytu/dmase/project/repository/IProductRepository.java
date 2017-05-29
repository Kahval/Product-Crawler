package ytu.dmase.project.repository;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import ytu.dmase.project.model.product.Category;
import ytu.dmase.project.model.product.Product;

public interface IProductRepository {
	
	Product getById(UUID uuid) throws RepositoryException;
	Product getById(String uuidString) throws RepositoryException;
	Product getByUrl(URL url) throws RepositoryException;
	Product getByUrl(String urlString) throws RepositoryException;
	List<Product> findByCategory(Category category) throws RepositoryException;
	List<Product> findByKeywords(String...keywords) throws RepositoryException;
	List<Product> findByName(String productName) throws RepositoryException;
	
	void save(Product product) throws RepositoryException;
	void update(Product product) throws RepositoryException;
	void delete(Product product) throws RepositoryException;
	int count() throws RepositoryException;
}
