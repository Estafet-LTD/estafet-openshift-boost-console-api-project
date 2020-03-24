package com.estafet.openshift.boost.console.api.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openshift.restclient.model.IProject;
import com.estafet.openshift.boost.console.api.project.model.Project;
import com.estafet.openshift.boost.console.api.project.openshift.OpenShiftClient;
import com.estafet.openshift.boost.console.api.project.util.ENV;
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
		System.out.println(iprojects);
		for (IProject iproject : iprojects) {
		    Project project = new Project();
		    project.setTitle(iproject.getDisplayName());
		    project.setNamespace(iproject.getNamespaceName());	
		    System.out.println("Project: " + iproject.getDisplayName() + "user url: " + ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"));
		    User user = restTemplate.getForObject(ENV.USER_SERVICE_API + "/user/uid/" + iproject.getLabels().get("userId"), User.class);
		    project.setOwner(user.getName());
		    projects.add(project);
		}		
		return projects;
	}
}
