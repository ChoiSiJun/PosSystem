package com.pos.commerce.application.product;

import java.util.List;
import java.util.Optional;

import com.pos.commerce.application.product.command.CreateProductCommand;
import com.pos.commerce.application.product.command.DeleteProductCommand;
import com.pos.commerce.application.product.command.UpdateProductCommand;
import com.pos.commerce.application.product.command.UpdateProductStockCommand;
import com.pos.commerce.application.product.query.GetActiveProductsQuery;
import com.pos.commerce.application.product.query.GetAllProductsQuery;
import com.pos.commerce.application.product.query.GetProductByCodeQuery;
import com.pos.commerce.application.product.query.GetProductByIdQuery;
import com.pos.commerce.domain.product.Product;

public interface ProductService {
    Product createProduct(CreateProductCommand command);
    Product updateProduct(UpdateProductCommand command);
    void deleteProduct(DeleteProductCommand command);
    Optional<Product> getProductById(GetProductByIdQuery query);
    Optional<Product> getProductByCode(GetProductByCodeQuery query);
    List<Product> getAllProducts(GetAllProductsQuery query);
    List<Product> getActiveProducts(GetActiveProductsQuery query);
    Product updateStock(UpdateProductStockCommand command);
}

