package service;

import model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<Product> findAll();
    Product findById(Long id);
}
