package ntut.csie.ezScrum.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ntut.csie.ezScrum.issue.sql.service.core.IQueryValueSet;
import ntut.csie.ezScrum.issue.sql.service.internal.MySQLQuerySet;
import ntut.csie.ezScrum.web.dataObject.HistoryObject;
import ntut.csie.ezScrum.web.dataObject.TaskObject;
import ntut.csie.ezScrum.web.databasEnum.IssueTypeEnum;
import ntut.csie.ezScrum.web.databasEnum.TaskEnum;

public class TaskDAO extends AbstractDAO<TaskObject, TaskObject> {

	public static TaskDAO getInstance() {
		if (sInstance == null) {
			sInstance = new TaskDAO();
		}
		return (TaskDAO) sInstance;
	}
	
	@Override
	public long create(TaskObject task) {
		long currentTime = System.currentTimeMillis();
		
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addInsertValue(TaskEnum.SERIAL_ID, task.getSerialId());
		valueSet.addInsertValue(TaskEnum.NAME, task.getName());
		valueSet.addInsertValue(TaskEnum.HANDLER_ID, task.getHandlerId());
		valueSet.addInsertValue(TaskEnum.ESTIMATE, task.getEstimate());
		valueSet.addInsertValue(TaskEnum.REMAIN, task.getRemains());
		valueSet.addInsertValue(TaskEnum.ACTUAL, task.getActual());
		valueSet.addInsertValue(TaskEnum.NOTES, task.getNotes());
		valueSet.addInsertValue(TaskEnum.STATUS, task.getStatus());
		valueSet.addInsertValue(TaskEnum.PROJECT_ID, task.getProjectId());
		valueSet.addInsertValue(TaskEnum.STORY_ID, task.getStoryId());
		valueSet.addInsertValue(TaskEnum.CREATE_TIME, currentTime);
		valueSet.addInsertValue(TaskEnum.UPDATE_TIME, currentTime);
		String query = valueSet.getInsertQuery();
		
		mControl.execute(query, true);
		
		String[] keys = mControl.getKeys();
		long id = Long.parseLong(keys[0]);
		
		// add create task history
		HistoryDAO historyDao = HistoryDAO.getInstance();
		historyDao.create(new HistoryObject(
							task.getId(),
							IssueTypeEnum.TYPE_TASK,
							HistoryObject.TYPE_CREATE,
							"",
							"",
							currentTime));
		// add task parent history
		if (task.getStoryId() > 0) {
			// task append history
			historyDao.create(new HistoryObject(
								task.getId(),
								IssueTypeEnum.TYPE_TASK, 
								HistoryObject.TYPE_APPEND,
								"",
								String.valueOf(task.getStoryId()),
								currentTime));
			
			// task parent add child history
			historyDao.create(new HistoryObject(
								task.getStoryId(),
								IssueTypeEnum.TYPE_STORY, 
								HistoryObject.TYPE_ADD,
								"",
								String.valueOf(task.getId()),
								currentTime));
		}
		
		return id;
	}

	@Override
	public TaskObject get(long id) throws SQLException {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addEqualCondition(TaskEnum.ID, id);
		String query = valueSet.getSelectQuery();

		ResultSet result = mControl.executeQuery(query);
		
		TaskObject task = null;
		if (result.next()) {
			task = convert(result);
		}
		return task;
	}

	@Override
	public boolean update(TaskObject task) {
		long currentTime = System.currentTimeMillis();
		
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addInsertValue(TaskEnum.NAME, task.getName());
		valueSet.addInsertValue(TaskEnum.HANDLER_ID, task.getHandlerId());
		valueSet.addInsertValue(TaskEnum.ESTIMATE, task.getEstimate());
		valueSet.addInsertValue(TaskEnum.REMAIN, task.getRemains());
		valueSet.addInsertValue(TaskEnum.ACTUAL, task.getActual());
		valueSet.addInsertValue(TaskEnum.NOTES, task.getNotes());
		valueSet.addInsertValue(TaskEnum.STATUS, task.getStatus());
		valueSet.addInsertValue(TaskEnum.PROJECT_ID, task.getProjectId());
		valueSet.addInsertValue(TaskEnum.STORY_ID, task.getStoryId());
		valueSet.addInsertValue(TaskEnum.UPDATE_TIME, currentTime);
		valueSet.addEqualCondition(TaskEnum.ID, task.getId());
		String query = valueSet.getUpdateQuery();
		
		return mControl.execute(query);
	}

	@Override
	public boolean delete(long id) {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addEqualCondition(TaskEnum.ID, id);
		String query = valueSet.getDeleteQuery();
		return mControl.execute(query);
	}
	
	public ArrayList<TaskObject> getTasksByStory(long storyId) throws SQLException {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addEqualCondition(TaskEnum.STORY_ID, storyId);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		
		ArrayList<TaskObject> tasks = new ArrayList<TaskObject>();
		while (result.next()) {
			tasks.add(convert(result));
		}
		return tasks;
	}
	
	public ArrayList<TaskObject> getWildTasks(long projectId) throws SQLException {
		IQueryValueSet valueSet = new MySQLQuerySet();
		valueSet.addTableName(TaskEnum.TABLE_NAME);
		valueSet.addEqualCondition(TaskEnum.STORY_ID, -1);
		valueSet.addEqualCondition(TaskEnum.PROJECT_ID, projectId);
		String query = valueSet.getSelectQuery();
		ResultSet result = mControl.executeQuery(query);
		
		ArrayList<TaskObject> tasks = new ArrayList<TaskObject>();
		while (result.next()) {
			tasks.add(convert(result));
		}
		return tasks;
	}

	private TaskObject convert(ResultSet result) throws SQLException {
		TaskObject task = new TaskObject();
		task.setId(result.getLong(TaskEnum.ID))
			.setSerialId(result.getLong(TaskEnum.SERIAL_ID))
			.setName(result.getString(TaskEnum.NAME))
			.setHandlerId(result.getLong(TaskEnum.HANDLER_ID))
			.setEstimate(result.getInt(TaskEnum.ESTIMATE))
			.setRemains(result.getInt(TaskEnum.REMAIN))
			.setActual(result.getInt(TaskEnum.ACTUAL))
			.setNotes(result.getString(TaskEnum.NOTES))
			.setProjectId(result.getLong(TaskEnum.PROJECT_ID))
			.setStoryId(result.getLong(TaskEnum.STORY_ID))
			.setCreateTime(result.getLong(TaskEnum.CREATE_TIME))
			.setUpdateTime(result.getLong(TaskEnum.UPDATE_TIME));
		return task;
	}
}
