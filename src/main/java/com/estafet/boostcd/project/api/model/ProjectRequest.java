package com.estafet.boostcd.project.api.model;


public class ProjectRequest {

	private String title;
	
	private String owner;
	
	private String namespace;



	public String getTitle() {
		return title;
	}

	public String getOwner() {
		return owner;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}

	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
