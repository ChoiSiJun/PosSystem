package com.pos.commerce.application.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.product.command.CreateProductCommand;
import com.pos.commerce.application.product.command.DeleteProductCommand;
import com.pos.commerce.application.product.command.UpdateProductCommand;
import com.pos.commerce.application.product.command.UpdateProductStockCommand;
import com.pos.commerce.application.product.query.GetActiveProductsQuery;
import com.pos.commerce.application.product.query.GetAllProductsQuery;
import com.pos.commerce.application.product.query.GetProductByCodeQuery;
import com.pos.commerce.application.product.query.GetProductByIdQuery;
import com.pos.commerce.domain.product.Product;
import com.pos.commerce.domain.product.ProductStatus;
import com.pos.commerce.infrastructure.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductApplicationService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(CreateProductCommand command) {
        if (productRepository.existsByProductCode(command.productCode())) {
            throw new IllegalArgumentException("이미 존재하는 상품 코드입니다: " + command.productCode());
        }
        ProductStatus status = command.status() != null ? command.status() : ProductStatus.ACTIVE;
        Product product = Product.builder()
                .productCode(command.productCode())
                .name(command.name())
                .description(command.description())
                .price(command.price())
                .stockQuantity(command.stockQuantity())
                .status(status)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(UpdateProductCommand command) {
        Product existingProduct = productRepository.findById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + command.productId()));

        ProductStatus status = command.status() != null ? command.status() : existingProduct.getStatus();

        Product updatedProduct = Product.builder()
                .id(existingProduct.getId())
                .productCode(existingProduct.getProductCode())
                .name(command.name())
                .description(command.description())
                .price(command.price())
                .stockQuantity(command.stockQuantity())
                .status(status)
                .createdAt(existingProduct.getCreatedAt())
                .build();

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(DeleteProductCommand command) {
        Long productId = command.productId();
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(GetProductByIdQuery query) {
        return productRepository.findById(query.productId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductByCode(GetProductByCodeQuery query) {
        return productRepository.findByProductCode(query.productCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts(GetAllProductsQuery query) {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getActiveProducts(GetActiveProductsQuery query) {
        return productRepository.findAll().stream()
                .filter(product -> product.getStatus() == ProductStatus.ACTIVE)
                .toList();
    }

    @Override
    public Product updateStock(UpdateProductStockCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + command.productId()));
        product.updateStock(command.quantity());
        return productRepository.save(product);
    }
}





