package ytu.dmase.project.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;
import ytu.dmase.project.repository.RepositoryException;

public class ProductService implements IProductService {

	private static final Logger _logger = LoggerFactory.getLogger(ProductService.class);
	
	private IProductRepository _repo;
	
	private int _newProductCounter = 0;
	private int _updateCounter = 0;
	
	@Inject
	public ProductService(IProductRepository repository)
	{
		_repo = repository;
	}
	
	@Override
	public void takeProduct(Product...product) {
		
		for(Product p : product)
		{
			Product existingProduct = null;
			try 
			{
				existingProduct = _repo.getByUrl(p.get_url());
				// existingProduct null dönmüş ise depoda bu url'e sahip bir product yoktur.
				// Her ürünün linki tektir. Aynı linki paylaşan iki farklı ürün olamaz.
				if(existingProduct == null)
				{
					_logger.debug(String.format("Saving a new product. (%s)", p));
					_repo.save(p);
					_newProductCounter++;
				} 
				else
				{
					_logger.debug(String.format("Updating existing product. (%s)", existingProduct));
					existingProduct.set_price(p.get_price());
					existingProduct.set_category(p.get_category());
					existingProduct.set_image(p.get_image());
					existingProduct.set_description(p.get_description());
					existingProduct.set_brand(p.get_brand());
					existingProduct.set_model(p.get_model());
					existingProduct.set_name(p.get_name());
					_repo.update(existingProduct);
					_updateCounter++;
				}
			} catch(RepositoryException e)
			{
				_logger.error("Couldn't serve a product. %s", existingProduct, e);
			}
			
		}
	}
	
	public int get_updateCounter() {
		return _updateCounter;
	}

	public int get_newProductCounter() {
		return _newProductCounter;
	}

	@Override
	public void takeProduct(Collection<Product> products) {
		products.forEach(p -> takeProduct(p) );
	}
}
