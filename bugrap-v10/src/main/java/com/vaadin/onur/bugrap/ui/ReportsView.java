package com.vaadin.onur.bugrap.ui;

import java.util.List;
import java.util.Set;

import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.ProjectVersion;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.onur.bugrap.BugrapApp;
import com.vaadin.onur.bugrap.backend.BugrapService;
import com.vaadin.onur.bugrap.beans.ReportDistribution;
import com.vaadin.onur.bugrap.comparator.ReportPriorityComparator;
import com.vaadin.onur.bugrap.comparator.ReportVersionComparator;
import com.vaadin.onur.bugrap.ui.events.IReportChangeListener;
import com.vaadin.onur.bugrap.ui.utils.FineDateFormatter;
import com.vaadin.onur.bugrap.ui.utils.StringUtil;

@Tag("reports-view")
@Route(value = BugrapApp.PAGE_REPORTS, layout = BugrapApp.class)
@HtmlImport("context://frontend/reports-view.html")
public class ReportsView extends PolymerTemplate<ReportsView.ReportsViewModel> implements IReportChangeListener {
	
	public interface ReportsViewModel extends TemplateModel {
		void setLoginUser(String str);
    }
	
	@Id("combo-project")
	private ComboBox<Project> projectsCombo;

	@Id("combo-project-version")
	private ComboBox<ProjectVersion> projectVersionsCombo;
	
	@Id("button-logout")
	private Button logoutButton;
	
	@Id("textsearch")
	private TextField searchReportText;

	@Id("grid-reports")
	private Grid<Report> reportsGrid;

	@Id("report-edit")
	private ReportEdit reportEditView;
	
	@Id("split-reports")
	private SplitLayout split;
	
	@Id("report-dist-bar")
	private ReportDistributionBar distributionBar;
	
	public ReportsView() {
		initializeListeners();
		initializeCombos();
		initializeGrid();
		Reporter loginUser = (Reporter)UI.getCurrent().getSession().getAttribute(BugrapApp.SESSIONKEY_LOGINUSER);
		if (loginUser != null)
			getModel().setLoginUser(loginUser.getName());
	}
	
	private void initializeListeners() {
		logoutButton.addClickListener(evt -> logout());
		projectsCombo.addValueChangeListener(evt -> onSelectedProjectChange());
		projectVersionsCombo.addValueChangeListener(evt -> onSelectedVersionChange(evt));
		reportEditView.addChangeListener(this);
		reportsGrid.addSelectionListener(evt -> onReportSelected(evt.getAllSelectedItems()));
	}

	private void initializeGrid() {
		reportsGrid.addColumn(r -> (r.getVersion() != null ? r.getVersion().getVersion() : "" )).setHeader("Version").setComparator(new ReportVersionComparator());
		reportsGrid.addColumn(r -> StringUtil.converToCamelCaseString(r.getPriority().name())).setHeader("Priority").setComparator(new ReportPriorityComparator());//.setSortProperty("priority").setFlexGrow(0).setWidth("50px");
		reportsGrid.addColumn(r -> StringUtil.converToCamelCaseString(r.getType().name())).setHeader("Type");//.setWidth("50px").setFlexGrow(0);
		reportsGrid.addColumn(r -> r.getSummary()).setHeader("Summary");//.setWidth("480px").setFlexGrow(6);
		reportsGrid.addColumn(r -> (r.getAssigned() != null ? r.getAssigned().getName() : "") ).setHeader("Assigned To");//.setWidth("240px").setFlexGrow(3);
		reportsGrid.addColumn(r -> FineDateFormatter.getFineTextFromDate(r.getTimestamp())).setHeader("Updated At");//.setWidth("80px").setFlexGrow(1)
		reportsGrid.addColumn(r -> FineDateFormatter.getFineTextFromDate(r.getReportedTimestamp())).setHeader("Created At");//.setWidth("80px").setFlexGrow(1)
		reportsGrid.setMultiSort(true);
	}
	
	private void logout() {
		UI.getCurrent().getSession().setAttribute(BugrapApp.SESSIONKEY_LOGINUSER, null);
		UI.getCurrent().navigateTo(BugrapApp.PAGE_LOGIN);
	}

	private void initializeCombos() {
		projectsCombo.setItemLabelGenerator(Project::getName);
		projectVersionsCombo.setItemLabelGenerator(ProjectVersion::getVersion);
		List<Project> projects = BugrapService.getInstance().findAllProjects();
		projectsCombo.setItems(projects);
		projectsCombo.setValue(projects.get(0));
	}
	
	public void onSelectedProjectChange() {
		projectVersionsCombo.setItems(BugrapService.getInstance().findVersionsByProject(projectsCombo.getValue()));
		reportEditView.processProjectChange(projectsCombo.getValue());
		refreshReportsGrid();
		refreshReportDistribution();
	}
	
	public void onSelectedVersionChange(ValueChangeEvent<ComboBox<ProjectVersion>, ProjectVersion> evt) {
		refreshReportsGrid();
		refreshReportDistribution();
		reportsGrid.getColumns().get(0).setVisible(evt.getValue() == null);
	}
	
	private void refreshReportsGrid() {
		reportsGrid.setItems(BugrapService.getInstance().findReports(projectsCombo.getValue(), projectVersionsCombo.getValue()));
	}
	private void refreshReportDistribution() {
		ReportDistribution rd = BugrapService.getInstance().getReportDistribution(projectsCombo.getValue(), projectVersionsCombo.getValue()); 
		distributionBar.setValue(rd.getClosedReports(), rd.getAssignedReports(), rd.getUnassignedReports());
	}

	private void onReportSelected(Set<Report> selectedReports) {
		if (selectedReports.isEmpty()) {
			split.setSizeFull();
			reportEditView.read(null);
		}
		else if (selectedReports.size() == 1) {
			split.setSplitterPosition(66);
			reportEditView.read(selectedReports.stream().findFirst().get());
		}
	}

	@Override
	public void reportChanged() {
		refreshReportsGrid();
	}
}