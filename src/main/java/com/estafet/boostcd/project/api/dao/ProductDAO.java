package com.estafet.boostcd.project.api.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.estafet.boostcd.project.api.model.Product;

@Repository
public class ProductDAO {

	@PersistenceContext
	private EntityManager entityManager;
		
	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {
		return entityManager.createQuery("Select p from Environments p").getResultList();
	}
	
	public Product getProduct(String productId) {
		return entityManager.find(Product.class, productId);
	}

	public Product create(Product product) {
		entityManager.persist(product);
		return product;
	}
	
}
