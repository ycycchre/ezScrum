package ntut.csie.ezScrum.web.action.backlog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ntut.csie.ezScrum.issue.mail.service.core.MailConfiguration;
import ntut.csie.ezScrum.web.action.PermissionAction;

public class SendSprintMailAction extends PermissionAction{
	private static Log log = LogFactory.getLog(SendSprintMailAction.class);

	@Override
	public boolean isValidAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isXML() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StringBuilder getResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		MailConfiguration mConfig = new MailConfiguration();
		
		return null;
	}
}
