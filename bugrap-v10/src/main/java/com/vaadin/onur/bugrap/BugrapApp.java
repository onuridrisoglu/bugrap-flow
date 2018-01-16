package com.vaadin.onur.bugrap;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.templatemodel.TemplateModel;

@Route("")
@Tag("bugrap-app")
@HtmlImport("context://frontend/bugrap-app.html")
public class BugrapApp extends PolymerTemplate<BugrapApp.BugrapModel> implements RouterLayout, BeforeEnterObserver {
	public static final String PAGE_LOGIN = "login";
	public static final String PAGE_REPORTS = "reports";
	
	public static final String SESSIONKEY_LOGINUSER = "loginuser";
	
	public interface BugrapModel extends TemplateModel {
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (PAGE_LOGIN.equals(event.getLocation().getFirstSegment()))
			return;
		if (UI.getCurrent().getSession().getAttribute(SESSIONKEY_LOGINUSER) == null) {
			event.rerouteTo(PAGE_LOGIN);
		}
	}
}
