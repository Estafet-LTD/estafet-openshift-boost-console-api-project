package com.estafet.boostcd.project.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.openshift.restclient.model.IProject;
import com.estafet.boostcd.project.api.model.Project;
import com.estafet.boostcd.project.api.openshift.OpenShiftClient;
import com.estafet.openshift.boost.messages.users.User;

@Service
public class ProjectService {

	@Autowired
	private OpenShiftClient client;
	
	@Autowired
	private RestTemplate restTemplate;

	public static final String USER_SERVICE_API = System.getenv("USER_API_SERVICE_URI");

	public List<Project> getProjects(String productId) {
		List<Project> projects = new ArrayList<Project>();;
		List<IProject> iprojects = client.getProjects(productId);
		for (IProject iproject : iprojects) {
		    Project project = new Project();
		    project.setTitle(iproject.getDisplayName());
		    project.setNamespace(iproject.getNamespaceName());
		    project.setStatus(iproject.getStatus());
		    User user = restTemplate.getForObject(USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
		    project.setOwner(user.getName());
		    projects.add(project);
		}		
		return projects;
	}
	
	public Project getProject(String productId, String namespace) {
		IProject iproject = client.getProject(productId, namespace);
		Project project = new Project();
		project.setTitle(iproject.getDisplayName());
	    project.setNamespace(iproject.getNamespaceName());
	    project.setStatus(iproject.getStatus());
	    User user = restTemplate.getForObject(USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
	    project.setOwner(user.getName());
	    return project;
	}

	public String createProject(String productId, Project project) {
		User user = new User();
		user.setName(project.getOwner());
		user.setUid((restTemplate.getForObject(USER_SERVICE_API + "/user/name/" + project.getOwner() + "/", User.class).getUid()));
		String uid = user.getUid();
		client.executeCreateEnviromentPipeline(productId, project, uid);
		return "success";
	}
	
	public String deleteProject(String productId, String project) {
		client.deleteProject(productId, project);
		return "success";
	}
	
	public String editProject(String productId, Project project, String namespace) {
		client.editProject(productId, project, namespace);
		return "success";
	}

}
