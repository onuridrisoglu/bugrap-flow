package com.vaadin.onur.bugrap.ui;
import org.vaadin.bugrap.domain.entities.Reporter;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.onur.bugrap.BugrapApp;
import com.vaadin.onur.bugrap.backend.BugrapService;

@Route(value = BugrapApp.PAGE_LOGIN, layout = BugrapApp.class)
@Tag("login-view")
@HtmlImport("context://frontend/login-view.html")
public class LoginView extends PolymerTemplate<LoginView.LoginViewModel>  {
	
	@Id("text-username")
	private TextField username;
	
	@Id("text-password")
	private PasswordField password;
	
	@Id("button-submit")
	private Button submitButton;
	
	public LoginView() {
		submitButton.addClickListener(evt -> login());
	}
	
    private void login() {
    		Reporter user = BugrapService.getInstance().authenticateReporter(username.getValue(), password.getValue());
    		if (user != null) {
    			UI.getCurrent().getSession().setAttribute(BugrapApp.SESSIONKEY_LOGINUSER, user);
    			UI.getCurrent().navigateTo(BugrapApp.PAGE_REPORTS);
    		} else {
    			getModel().setError(true);
    			getModel().setErrorMessage("Please check username and password");
    			username.clear();
    			password.clear();
    			username.focus();
    		}
	}

	public interface LoginViewModel extends TemplateModel {
		
		public void setError (boolean b);
		public void setErrorMessage (String msg);
    }
}
