package com.chrissj.bookstore.controller;

import com.chrissj.bookstore.model.Author;
import com.chrissj.bookstore.model.Category;
import com.chrissj.bookstore.model.Product;
import com.chrissj.bookstore.model.Publisher;
import com.chrissj.bookstore.service.AuthorService;
import com.chrissj.bookstore.service.CategoryService;
import com.chrissj.bookstore.service.ProductService;
import com.chrissj.bookstore.service.PublisherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    public ProductController(ProductService productService, CategoryService categoryService, AuthorService authorService, PublisherService publisherService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @GetMapping
    public String getProduct(Model model){
        List<Product> products = this.productService.getAll();
        model.addAttribute("products", products);
        return "product/product_home";
    }

    @GetMapping("/add")
    public String addProduct(Model model){
        List<Category> categories = this.categoryService.getAll();
        List<Author> authors = this.authorService.getAll();
        List<Publisher> publishers = this.publisherService.getAll();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        return "product/product_add";
    }

    @PostMapping("/add")
    public String addProductPost(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "price") Float price,
                                 @RequestParam(name = "category") int categoryId,
                                 @RequestParam(name = "author") int authorId,
                                 @RequestParam(name = "publisher") int publisherId,
                                 @RequestParam(name = "image") MultipartFile image){
        try{
            Category category = this.categoryService.getById(categoryId);
            Author author = this.authorService.getById(authorId);
            Publisher publisher = this.publisherService.getById(publisherId);
            this.productService.add(name, price, category, publisher, author, image);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return "redirect:/admin/product";
    }
}
