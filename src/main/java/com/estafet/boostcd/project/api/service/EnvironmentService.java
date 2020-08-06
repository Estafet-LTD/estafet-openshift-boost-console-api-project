package com.estafet.boostcd.project.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.boostcd.project.api.dao.ProductDAO;
import com.estafet.boostcd.project.api.model.Product;
import com.estafet.openshift.boost.messages.environments.Environments;

@Service
public class EnvironmentService {
	
	@Autowired
	private ProductDAO productDAO;
	
	@Transactional
	public void updateEnv(Environments environments) {
		Product product = productDAO.getProduct(environments.getProductId());
		if (product == null) {
			product = Product.builder()
					.setProductId(environments.getProductId())
					.setRepo(environments.getRepo())
					.build();
			productDAO.create(product);
		}
	}

}
