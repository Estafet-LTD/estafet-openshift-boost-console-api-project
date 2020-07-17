package com.estafet.boostcd.project.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.openshift.restclient.model.IProject;
import com.estafet.boostcd.project.api.model.Project;
import com.estafet.boostcd.project.api.openshift.OpenShiftClient;
import com.estafet.boostcd.project.api.util.ENV;
import com.estafet.openshift.boost.messages.users.User;

@Service
public class ProjectService {

	@Autowired
	private OpenShiftClient client;
	
	@Autowired
	private RestTemplate restTemplate;
	


	public List<Project> getProjects() {
		List<Project> projects = new ArrayList<Project>();;
		List<IProject> iprojects = client.getProjects();
		for (IProject iproject : iprojects) {
		    Project project = new Project();
		    project.setTitle(iproject.getDisplayName());
		    project.setNamespace(iproject.getNamespaceName());
		    project.setStatus(iproject.getStatus());
		    User user = restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
		    project.setOwner(user.getName());
		    projects.add(project);
		}		
		return projects;
	}
	
	public Project getProject(String namespace) {
		IProject iproject = client.getProject(namespace);
		Project project = new Project();
		project.setTitle(iproject.getDisplayName());
	    project.setNamespace(iproject.getNamespaceName());
	    project.setStatus(iproject.getStatus());
	    User user = restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
	    project.setOwner(user.getName());
	    return project;
	}
	

	public String createProject(Project project) {
		User user = new User();
		user.setName(project.getOwner());
		user.setUid((restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/name/" + project.getOwner() + "/", User.class).getUid()));
		String uid = user.getUid();
		client.executeCreateEnviromentPipeline(project, uid);
		return "success";
	}
	
	public String deleteProject(String project) {
		client.deleteProject(project);
		return "success";
	}
	
	public String editProject(Project project, String namespace) {
		client.editProject(project, namespace);
		return "success";
	}

}
