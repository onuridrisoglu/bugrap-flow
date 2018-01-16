package com.vaadin.onur.bugrap.beans;

public class ReportDistribution {

	private int closedReports;
	private int assignedReports;
	private int unassignedReports;
	
	public int getClosedReports() {
		return closedReports;
	}
	public void setClosedReports(long closedReports) {
		this.closedReports = (int)closedReports;
	}
	public int getUnassignedReports() {
		return unassignedReports;
	}
	public void setUnassignedReports(long unassignedReports) {
		this.unassignedReports = (int)unassignedReports;
	}
	public int getAssignedReports() {
		return assignedReports;
	}
	public void setAssignedReports(long assignedReports) {
		this.assignedReports = (int)assignedReports;
	}
}
