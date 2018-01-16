package com.vaadin.onur.bugrap.ui;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Report.Priority;
import org.vaadin.bugrap.domain.entities.Report.Status;
import org.vaadin.bugrap.domain.entities.Report.Type;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.onur.bugrap.backend.BugrapService;
import com.vaadin.onur.bugrap.ui.events.IReportChangeListener;
import com.vaadin.onur.bugrap.ui.utils.StringUtil;

@Tag("report-edit")
@HtmlImport("context://frontend/report-edit.html")
public class ReportEdit extends PolymerTemplate<ReportEdit.ReportEditModel> {

	@Id("combo-report-priority")
	private ComboBox<Priority> priorityCombo;

	@Id("combo-report-type")
	private ComboBox<Type> typeCombo;

	@Id("combo-report-status")
	private ComboBox<Status> statusCombo;
	
	@Id("combo-report-assigned")
	private ComboBox<Reporter> assignedCombo;

	@Id("combo-report-version")
	private ComboBox<ProjectVersion> versionCombo;

	@Id("button-update")
	private Button updateButton;

	@Id("button-revert")
	private Button revertButton;
	
	
	private final Binder<Report> binder = new Binder<Report>();
	private List<IReportChangeListener> changeListeners = new ArrayList<IReportChangeListener>();
	
	public ReportEdit() {
		initializeCombos();
		initializeBinder();
		updateButton.addClickListener(evt -> update());
		revertButton.addClickListener(evt -> revert());
		getModel().setHasRecord(false);
	}
	
	 public interface ReportEditModel extends TemplateModel {
		 public void setHasRecord(boolean b);
		 public void setSummary(String s);
		 public String getSummary();
	 }
	
	private void initializeCombos() {
		priorityCombo.setItems(Priority.values());
		priorityCombo.setItemLabelGenerator(e -> e != null ? StringUtil.converToCamelCaseString(e.name()) : "");

		typeCombo.setItems(Type.values());
		typeCombo.setItemLabelGenerator(e -> e != null ? StringUtil.converToCamelCaseString(e.name()) : "");
		
		statusCombo.setItems(Status.values());
		statusCombo.setItemLabelGenerator(e -> e != null ? StringUtil.converToCamelCaseString(e.name()) : "");

		assignedCombo.setItems(BugrapService.getInstance().findAllReporters());
		assignedCombo.setItemLabelGenerator(r -> r != null ? r.getName() : "");
	}
	
	private void initializeBinder() {
		binder.bind(priorityCombo, Report::getPriority, Report::setPriority);
		binder.bind(typeCombo, Report::getType, Report::setType);
		binder.bind(statusCombo, Report::getStatus, Report::setStatus);
		binder.bind(assignedCombo, Report::getAssigned, Report::setAssigned);
		binder.bind(versionCombo, Report::getVersion, Report::setVersion);
	}
	
	public void read(Report report) {
		getModel().setHasRecord(report != null);
		getModel().setSummary(report != null ? report.getSummary() : "");
		binder.setBean(report);
	}
	
	public void addChangeListener(IReportChangeListener listener) {
		changeListeners.add(listener);
	}
	
	public void processProjectChange(Project p) {
		versionCombo.setItems(BugrapService.getInstance().findVersionsByProject(p));
		versionCombo.setItemLabelGenerator(v -> v != null ? v.getVersion() : "");
	}
	
	private void update() {
		Report r = BugrapService.getInstance().saveReport(binder.getBean());
		binder.setBean(r);
		changeListeners.forEach(listener -> listener.reportChanged());
	}
	
	private void revert() {
		Report r = BugrapService.getInstance().getReportById(binder.getBean().getId());
		binder.setBean(r);
	}
}
