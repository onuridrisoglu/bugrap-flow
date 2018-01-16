package com.vaadin.onur.bugrap.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.vaadin.bugrap.domain.BugrapRepository;
import org.vaadin.bugrap.domain.BugrapRepository.ReportsQuery;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.onur.bugrap.beans.ReportDistribution;
import com.vaadin.onur.bugrap.ui.utils.ReportUtil;

public class BugrapService {

	private static final BugrapService instance = new BugrapService();
	
	private static final String DB_LOCATION = "/Users/onuridrisoglu/Development/eclipse-workspace/bugrap-data/bugrap";
	private BugrapRepository repo;
	
	private BugrapService() {
		repo = new BugrapRepository(DB_LOCATION);
	}
	
	public static BugrapService getInstance() {
		return instance;
	}
	
	public Reporter authenticateReporter(String un, String pw) {
		return repo.authenticate(un, pw);
	}
	
	public Collection<Reporter> findAllReporters() {
		return repo.findReporters();
	}

	public List<Project> findAllProjects() {
		return getAsSortedList(repo.findProjects());
	}
	public List<ProjectVersion> findVersionsByProject(Project p) {
		return getAsSortedList(repo.findProjectVersions(p));
	}
	public Collection<Report> findReports(Project p, ProjectVersion pv) {
		ReportsQuery query = new ReportsQuery();
		query.project = p;
		query.projectVersion = pv;
		return repo.findReports(query);
	}
	
	public Report saveReport(Report modifiedReport) {
		Report report = getReportById(modifiedReport.getId());
		ReportUtil.setFields(report, modifiedReport);
		return repo.save(report);
	}
	
	public Report getReportById(long id) {
		return repo.getReportById(id);
	}
	
	public ReportDistribution getReportDistribution(Project project, ProjectVersion version) {
		ReportDistribution distribution = new ReportDistribution();
		boolean isAllVersions = version == null;
		distribution.setClosedReports(isAllVersions ? repo.countClosedReports(project) : repo.countClosedReports(version));
		distribution.setAssignedReports(isAllVersions ? repo.countOpenedReports(project) : repo.countOpenedReports(version));
		distribution.setUnassignedReports(isAllVersions ? repo.countUnassignedReports(project) : repo.countUnassignedReports(version));
		return distribution;
	}
	
	private  <T extends Comparable<? super T>> List<T> getAsSortedList(Set<T> coll){
		List<T> list = new ArrayList<T>();
		list.addAll(coll);
		Collections.sort(list);
		return list;
	}
	
}
