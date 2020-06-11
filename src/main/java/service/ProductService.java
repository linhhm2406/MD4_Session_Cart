package service;

import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import repository.IProductRepository;

import java.util.List;

public class ProductService implements IProductService {

    @Autowired
    IProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findOne(id);
    }
}
