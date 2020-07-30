package com.estafet.boostcd.project.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.boostcd.commons.model.API;
import com.estafet.boostcd.project.api.model.Project;
import com.estafet.boostcd.project.api.service.ProjectService;

@RestController
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Value("${app.version}")
	private String appVersion;

	@GetMapping("/api")
	public API getAPI() {
		return new API(appVersion);
	}

	@GetMapping("/projects/{product}")
	public List<Project> getProjects(@PathVariable String product) {
		return projectService.getProjects(product);
	}
	
	@GetMapping("/project/{product}/{namespace}")
	public Project getProject(@PathVariable String product, @PathVariable String namespace) {
		return projectService.getProject(product, namespace);
	}

	@PostMapping("/project/{product}")
	public ResponseEntity<String> createProject(@PathVariable String product, @RequestBody Project project) {
		return new ResponseEntity<String>(projectService.createProject(product, project), HttpStatus.OK);
	}
	
	@DeleteMapping("/project/{project}")
	public ResponseEntity<String> deleteProject(@PathVariable String product, @PathVariable String project) {
		return new ResponseEntity<String>(projectService.deleteProject(product, project), HttpStatus.OK);
	}
	
	@PutMapping("/project/{namespace}")
	public ResponseEntity<String> deleteProject(@PathVariable String product, @PathVariable String namespace, @RequestBody Project project) {
		return new ResponseEntity<String>(projectService.editProject(product, project, namespace), HttpStatus.OK);
	}

}
