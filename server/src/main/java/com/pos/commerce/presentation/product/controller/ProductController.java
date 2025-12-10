package com.pos.commerce.presentation.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.product.ProductService;
import com.pos.commerce.application.product.command.CreateProductCommand;
import com.pos.commerce.application.product.command.DeleteProductCommand;
import com.pos.commerce.application.product.command.UpdateProductCommand;
import com.pos.commerce.application.product.command.UpdateProductStockCommand;
import com.pos.commerce.application.product.query.GetActiveProductsQuery;
import com.pos.commerce.application.product.query.GetAllProductsQuery;
import com.pos.commerce.application.product.query.GetProductByCodeQuery;
import com.pos.commerce.application.product.query.GetProductByIdQuery;
import com.pos.commerce.domain.product.Product;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.product.dto.CreateProductRequest;
import com.pos.commerce.presentation.product.dto.ProductResponse;
import com.pos.commerce.presentation.product.dto.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /* @상품 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createProduct(CreateProductRequest request) {
  
        CreateProductCommand command = new CreateProductCommand(
            request.getCode(),
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getStock(),
            request.getStatus(),
            request.getImage()
        );
        productService.createProduct(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("상품이 생성되었습니다.",null));
    }

    /* @상품 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,UpdateProductRequest request) {

                System.out.println("request: " + request.getName());
                System.out.println("request: " + request.getDescription());
                System.out.println("request: " + request.getPrice());
                System.out.println("request: " + request.getStock());
                System.out.println("request: " + request.getImage());
                System.out.println("request: " + request.getImageUrl());
                System.out.println("request: " + request.getStatus());
        UpdateProductCommand command = new UpdateProductCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getImage(),
                request.getImageUrl(),
                request.getStatus()
        );

        productService.updateProduct(command);
        return ResponseEntity.ok(ApiResponse.success("상품이 수정되었습니다.", null));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(new DeleteProductCommand(id));
        return ResponseEntity.ok(ApiResponse.success("상품이 삭제되었습니다.", null));
    }

    /* @상품 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return productService.getProductById(new GetProductByIdQuery(id))
                .map(product -> ResponseEntity.ok(ApiResponse.success(ProductResponse.from(product))))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/code/{productCode}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductByCode(@PathVariable String productCode) {
        return productService.getProductByCode(new GetProductByCodeQuery(productCode))
                .map(product -> ResponseEntity.ok(ApiResponse.success(ProductResponse.from(product))))
                .orElse(ResponseEntity.notFound().build());
    }

    /* @모든 상품 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts(new GetAllProductsQuery()).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /* @활성 상품 조회 */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getActiveProducts() {
        List<ProductResponse> products = productService.getActiveProducts(new GetActiveProductsQuery()).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /* @재고 수정 */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<ProductResponse>> updateStock(
            @PathVariable Long id,
            @RequestBody Integer quantity) {
        Product updated = productService.updateStock(new UpdateProductStockCommand(id, quantity));
        return ResponseEntity.ok(ApiResponse.success("재고가 수정되었습니다.", ProductResponse.from(updated)));
    }
}

