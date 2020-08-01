package com.estafet.boostcd.project.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
public class Product {
    
    @Id
	@Column(name = "PRODUCT_ID", nullable = false)
    private String productId;
	
	@Column(name = "REPO", nullable = false)
    private String repo;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }
    
    public static class ProductBuilder {

        private String productId;
        private String repo;

		public ProductBuilder setRepo(String repo) {
			this.repo = repo;
			return this;
		}

		public ProductBuilder setProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setProductId(productId);
            product.setRepo(repo);
            return product;
        }
        
    }

}