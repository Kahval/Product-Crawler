package ytu.dmase.project.service;

import java.util.Collection;

import ytu.dmase.project.model.product.Product;

public interface IProductService {
	
	void takeProduct(Product...product);
	void takeProduct(Collection<Product> products);
	
}
