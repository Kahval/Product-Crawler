package ytu.dmase.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import ytu.dmase.project.model.product.Product;
import ytu.dmase.project.repository.IProductRepository;

public class ProductService implements IProductService {

	private final Logger _logger = LoggerFactory.getLogger(ProductService.class);
	
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
			Product existingProduct = _repo.GetByUrl(p.get_url());
			// existingProduct null dönmüş ise depoda bu url'e sahip bir product yoktur.
			// Her ürünün linki tektir. Aynı linki paylaşan iki farklı ürün olamaz.
			if(existingProduct == null)
			{
				_logger.info(String.format("Saving a new product. (%s)", p));
				_repo.Save(p);
				_newProductCounter++;
			} 
			else
			{
				_logger.info(String.format("Updating existing product. (%s)", existingProduct));
				existingProduct.set_price(p.get_price());
				_repo.Update(existingProduct);
				_updateCounter++;
			}
		}
	}

	public int get_updateCounter() {
		return _updateCounter;
	}

	public int get_newProductCounter() {
		return _newProductCounter;
	}
}