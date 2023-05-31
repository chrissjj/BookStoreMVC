package com.chrissj.bookstore.service.implementation;

import com.chrissj.bookstore.model.Author;
import com.chrissj.bookstore.model.Category;
import com.chrissj.bookstore.model.Product;
import com.chrissj.bookstore.model.Publisher;
import com.chrissj.bookstore.model.exception.NoProductFoundException;
import com.chrissj.bookstore.repository.ProductRepository;
import com.chrissj.bookstore.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final String FOLDER_PATH = "D:\\Projects\\Book_store\\upload\\products\\";

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(int id) throws IOException {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoProductFoundException(id));
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product add(String name, Float price, Category category, Publisher publisher, Author author) {
        Product product = new Product(name, price, category, publisher, author);
        return productRepository.save(product);
    }

    @Override
    public Product add(String name, Float price, Category category, Publisher publisher, Author author, MultipartFile image) {
        Product product = new Product(name, price, category, publisher, author);
        String filePath = this.FOLDER_PATH + image.getOriginalFilename();
        product.setImagePath(filePath);
        product.setImageType(image.getContentType());
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteById(int id) throws IOException {
        productRepository.deleteById(id);
    }

    @Override
    public Product update(int id, Product product) throws IOException {
        Product pUpdate = getById(id);
        pUpdate.setName(product.getName());
        pUpdate.setAuthors(product.getAuthors());
        pUpdate.setCategory(product.getCategory());
        pUpdate.setPrice(product.getPrice());
        pUpdate.setPublisher(product.getPublisher());
        return productRepository.save(pUpdate);
    }

    @Override
    public Product updateImage(int id, MultipartFile image) throws IOException {
        Product p = getById(id);
        p.setImageType(image.getContentType());
        String filePath = String.format("%s_%d%s", this.FOLDER_PATH, id, image.getOriginalFilename());
        p.setImagePath(filePath);
        image.transferTo(new File(filePath));
        return productRepository.save(p);
    }
}
