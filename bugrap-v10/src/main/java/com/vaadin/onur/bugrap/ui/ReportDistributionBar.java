package com.vaadin.onur.bugrap.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.onur.bugrap.beans.ReportDistribution;

@Tag("report-distribution-bar")
@HtmlImport("context://frontend/report-distribution-bar.html")
public class ReportDistributionBar extends PolymerTemplate<ReportDistributionBar.ReportDistributionBarModel> {

	public void setValue(int closed, int assigned, int unassigned) {
		getModel().setClosedCount(closed);
		getModel().setAssignedCount(assigned);
		getModel().setUnassignedCount(unassigned);
	}
	
	public ReportDistributionBar() {
		getModel().setClosedCount(0);
		getModel().setAssignedCount(0);
		getModel().setUnassignedCount(0);
	}
	
    public interface ReportDistributionBarModel extends TemplateModel {
    		public void setClosedCount(int f);
    		public void setAssignedCount(int f);
    		public void setUnassignedCount(int f);
    }
}
