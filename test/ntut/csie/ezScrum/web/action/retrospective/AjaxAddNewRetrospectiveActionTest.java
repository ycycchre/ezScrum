package ntut.csie.ezScrum.web.action.retrospective;

import java.io.File;
import java.io.IOException;

import ntut.csie.ezScrum.issue.sql.service.core.Configuration;
import ntut.csie.ezScrum.issue.sql.service.core.InitialSQL;
import ntut.csie.ezScrum.iteration.core.ScrumEnum;
import ntut.csie.ezScrum.test.CreateData.CreateProject;
import ntut.csie.ezScrum.test.CreateData.CreateSprint;
import ntut.csie.ezScrum.web.dataObject.ProjectObject;
import servletunit.struts.MockStrutsTestCase;

public class AjaxAddNewRetrospectiveActionTest extends MockStrutsTestCase {
	private CreateProject mCP;
	private CreateSprint mCS;
	private Configuration mConfig;
	private String mPrefix = "TEST_RETROSPECTIVE_";
	private String mActionPath = "/ajaxAddNewRetrospective";
	
	public AjaxAddNewRetrospectiveActionTest(String testMethod) {
        super(testMethod);
    }
	
	protected void setUp() throws Exception {
		mConfig = new Configuration();
		mConfig.setTestMode(true);
		mConfig.save();
		
		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe(); // 初始化 SQL

		this.mCP = new CreateProject(1);
		this.mCP.exeCreateForDb(); // 新增一測試專案

		super.setUp();

		setContextDirectory(new File(mConfig.getBaseDirPath() + "/WebContent")); // 設定讀取的
		// struts-config
		// 檔案路徑
		setServletConfigFile("/WEB-INF/struts-config.xml");
		setRequestPathInfo(mActionPath);

		// ============= release ==============
		ini = null;
	}

	protected void tearDown() throws IOException, Exception {
		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe();

		// 讓 config 回到  Production 模式
		mConfig.setTestMode(false);
		mConfig.save();

		super.tearDown();

		// ============= release ==============
		ini = null;
		mCP = null;
		mCS = null;
		mConfig = null;
	}
	
	// case 1: One sprint with adding Good retrospective
	public void testAddGood() throws Exception {
		mCS = new CreateSprint(1, mCP);
		mCS.exe(); // 新增一個 Sprint	
		
		// ================ set initial data =======================
		ProjectObject project = mCP.getAllProjects().get(0);
		
		long sprintId = 1;
		long retrospectiveId = 1;
		String retrospectiveName = mPrefix + "name";
		String retrospectiveType = ScrumEnum.GOOD_ISSUE_TYPE;
		String retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	String expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());   	    	
	}		
					
	// case 2: One sprint with adding Improvement retrospective
	public void testAddImprovement() throws Exception {	
		mCS = new CreateSprint(1, mCP);
		mCS.exe(); // 新增一個 Sprint		
		
		// ================ set initial data =======================
		ProjectObject project = mCP.getAllProjects().get(0);
		long sprintId = 1;
		long retrospectiveId = 1;
		String retrospectiveName = mPrefix + "name";
		String retrospectiveType = ScrumEnum.IMPROVEMENTS_ISSUE_TYPE;
		String retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	String expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());   	    	
	}	
	
	// case 3: One sprint with adding Improvement & Good retrospective
	public void testAddImporveAndGood() throws Exception {	
		mCS = new CreateSprint(1, mCP);
		mCS.exe(); // 新增一個 Sprint
		
		/*
		 * (I) add Improvement
		 */
		// ================ set initial data =======================
		ProjectObject project = mCP.getAllProjects().get(0);
		long sprintId = 1;
		long retrospectiveId = 1;
		String retrospectiveName = mPrefix + "name";
		String retrospectiveType = ScrumEnum.IMPROVEMENTS_ISSUE_TYPE;
		String retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	String expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());
    	
    	// 執行下一次的action必須做此動作,否則response內容不會更新!
		clearRequestParameters();
		response.reset();
    	
    	/*
    	 * (II) add Good
    	 */    	    	
    	
		// ================ set initial data =======================
		retrospectiveId = 2;
		retrospectiveType = ScrumEnum.GOOD_ISSUE_TYPE;
		retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());     
	}	
	
	// case 4: One sprint with adding Good & Improvement retrospective
	public void testAddGoodAndImporve() throws Exception {	
		mCS = new CreateSprint(1, mCP);
		mCS.exe(); // 新增一個 Sprint
		
		/*
		 * (I) add Good
		 */
		// ================ set initial data =======================
		ProjectObject project = mCP.getAllProjects().get(0);
		long sprintId = 1;
		long retrospectiveId = 1;
		String retrospectiveName = mPrefix + "name";
		String retrospectiveType = ScrumEnum.GOOD_ISSUE_TYPE;
		String retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	String expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());
    	
    	// 執行下一次的action必須做此動作,否則response內容不會更新!
		clearRequestParameters();
		response.reset();
    	
    	/*
    	 * (II) add Improvement
    	 */    	    	
    	
		// ================ set initial data =======================
		retrospectiveId = 2;
		retrospectiveType = ScrumEnum.IMPROVEMENTS_ISSUE_TYPE;
		retrospectiveDescription = mPrefix + "description";
		// ================ set initial data =======================

		// ================== set parameter info ====================
		addRequestParameter("Name", retrospectiveName);
		addRequestParameter("SprintID", "#" + sprintId);
		addRequestParameter("Type", retrospectiveType);
		addRequestParameter("Description", retrospectiveDescription);		
		// ================== set parameter info ====================

		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());	
		request.getSession().setAttribute("Project", project);
		// ================ set session info ========================
		request.setHeader("Referer", "?projectName=" + project.getName());	// SessionManager 會對URL的參數作分析 ,未帶入此參數無法存入session

		actionPerform(); // 執行 action

		// 驗證回傳 path
    	verifyForwardPath(null);
    	verifyForward(null);
    	verifyNoActionErrors();
  
    	// 比對資料是否正確
    	expected = genXML(sprintId, retrospectiveId, retrospectiveName, retrospectiveType, retrospectiveDescription);
    	assertEquals(expected, response.getWriterBuffer().toString());     
	}
	
	
	
	private String genXML(long sprintId, long retrospectiveId, String name, String type, String description) {
		StringBuilder result = new StringBuilder("");

		result.append("<AddNewRetrospective><Result>true</Result><Retrospective>");
		result.append("<Id>" + retrospectiveId + "</Id>");
		result.append("<Link>" + "/ezScrum/showIssueInformation.do?issueID=" + retrospectiveId + "</Link>");
		result.append("<SprintID>" + sprintId + "</SprintID>");
		result.append("<Name>" + name + "</Name>");
		result.append("<Type>" + type + "</Type>");
		result.append("<Description>" + description + "</Description>");
		result.append("<Status>" + "new" + "</Status>");
		result.append("</Retrospective></AddNewRetrospective>");
		return result.toString();
	}
}
