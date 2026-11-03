
package com.vitngon.vitngon_backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Lấy danh sách sản phẩm đang hoạt động
    @GetMapping
    public List<Product> getProducts() {
        return productRepository.findByActiveTrue();
    }

    // Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestBody Product request) {

        if (request.getName() == null
                || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Tên sản phẩm không được để trống");
        }

        if (request.getPrice() == null
                || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body("Giá sản phẩm phải lớn hơn 0");
        }

        if (request.getCategory() == null
                || request.getCategory().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Danh mục không được để trống");
        }

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory().trim());
        product.setImage(request.getImage());
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }
}
