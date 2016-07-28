package testrail.refrences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import testrail.api.TestRailUtil;
import testrail.enums.User;

public class Plan {
	private static final String ADD_PLAN = "add_plan/";
	private static final String GET_PLAN = "get_plan/";

	private Object blocked_count, completed_on, created_by, 
			created_on, description, failed_count, is_completed,
			passed_count, retest_count, untested_count, url;
	private Long assignedto_id, id, milestone_id, project_id;
	private String name;
	
	private List<Entry> entries;
	private List<Run> runs;
	private List<Test> tests;

	private JSONObject jsonObject;
	
	private TestRailUtil testRailUtil = TestRailUtil.getInstance();
	private Project project;

	public static Plan getPlan(long id) {
		return new Plan(TestRailUtil.getInstance().sendGetJObj(GET_PLAN + id));
	}
	
	public static Plan getPlan(String name, Project project) {
		for (Plan plan : project.getPlans())
			if (plan.getName().equals(name))
				return getPlan((Long)plan.getId());
		return null;
	}
	
	public Plan(JSONObject jsonObject) {
		this.project = TestRailUtil.getInstance().getProject();
		this.project_id = project.getId();
		this.jsonObject = jsonObject;
		initJObj();
	}
	
	@SuppressWarnings("unchecked")
	public Plan(Project project, Long assignedto_id, String name, JSONArray entries) {
		this.project = project;
		this.project_id = project.getId();
		this.assignedto_id = assignedto_id;
		this.name = name;
		for (Object o : entries)
			this.entries.add(new Entry((JSONObject) o));

		this.jsonObject = new JSONObject();
		this.jsonObject.put("project_id", project.getId());
		this.jsonObject.put("assignedto_id", assignedto_id);
		this.jsonObject.put("name", name);
		this.jsonObject.put("entries", entries);

		addPlan();
	}
	
	public Plan(String name, Suite suite) {
		this(null, null, name, suite);
	}
	
	public Plan(Project project, User user, String name, Suite suite) {
		this(project, user, name, suite, null);
	}
	
	@SuppressWarnings("unchecked")
	public Plan(Project project, User user, String name, Suite suite, ConfigurationGroup configurationGroup) {
		if (project == null)
			this.project = testRailUtil.getProject();
		else
			this.project = project;
		this.project_id = this.project.getId();
		if (user == null)
			this.assignedto_id = (Long) User.GIMSO_REPORTS.getValue();
		else
			this.assignedto_id = user.getValue();
		
		this.name = setPlanName(name);
		Entry entry;
		if (configurationGroup == null)
			entry = new Entry(suite);
		else
			entry = new Entry(suite, configurationGroup);

		this.entries = new ArrayList<>();
		this.entries.add(entry);
		JSONArray entriesArr = new JSONArray();
		entriesArr.add(entry.getJsonObject());
		
		this.jsonObject = new JSONObject();
		this.jsonObject.put("project_id", this.project_id);
		this.jsonObject.put("assignedto_id", this.assignedto_id);
		this.jsonObject.put("name", this.name);
		this.jsonObject.put("entries", entriesArr);

		addPlan();
	}

	private String setPlanName(String defaultName) {
		String build_tag = System.getenv("BUILD_TAG");
		String git_branch = System.getenv("GIT_BRANCH");

		if (build_tag != null && git_branch != null)
			return build_tag + "_" + git_branch;
		else
			return defaultName;
	}
	

	/**
	 * Initialized the bean obj
	 * 
	 * @param map
	 */
	private void initJObj() {
		this.assignedto_id=(Long) jsonObject.get("assignedto_id");
		this.blocked_count=jsonObject.get("blocked_count");
		this.completed_on=jsonObject.get("completed_on");
		this.created_by=jsonObject.get("created_by");
		this.created_on=jsonObject.get("created_on");
		this.description=jsonObject.get("description");
		this.failed_count=jsonObject.get("failed_count");
		this.id=(Long) jsonObject.get("id");
		this.is_completed=jsonObject.get("is_completed");
		this.milestone_id=(Long) jsonObject.get("milestone_id");
		this.name=(String) jsonObject.get("name");
		this.passed_count=jsonObject.get("passed_count");
		this.project_id=(Long) jsonObject.get("project_id");
		this.retest_count=jsonObject.get("retest_count");
		this.untested_count=jsonObject.get("untested_count");
		this.url=jsonObject.get("url");
		Object entries = jsonObject.get("entries");
		if (entries!=null) {
			this.entries = new ArrayList<>();
			for (Object o : (JSONArray) entries) {
				this.entries.add(new Entry((JSONObject) o));
			} 
		}
	}
	
	public List<Plan> getPlansCreatedAfterDate(Date createdAfter) {
		List<Plan> rv = new ArrayList<>();
		String unixTimeFromDate = (createdAfter.getTime() / 1000) + "";
		String sendGetCommand = "get_plans/" + this.id + "&created_after=" + unixTimeFromDate;
		
		JSONArray array = testRailUtil.sendGetJArray(sendGetCommand);
		for (Object o : array)
			if (o instanceof JSONObject)
				rv.add(new Plan((JSONObject) o));
		return rv;
	}

	public void addPlan() {
		Plan temp = getPlan(this.name, this.project);
		if (temp == null) {
			JSONObject rv = testRailUtil.sendPost(ADD_PLAN + project.getId(), jsonObject);
			Plan plan = new Plan(rv);
			testRailUtil.setPlan(plan);
			System.out.println("Plan " + plan.getName() + " Created");
		} else {
			testRailUtil.setPlan(temp);
			System.out.println("Plan " + temp.getName() + " Already exist");
		}
	}
	
	// Getters & Setters
	
	public Long getAssignedto_id() {
		return assignedto_id;
	}

	public void setAssignedto_id(Long assignedto_id) {
		this.assignedto_id = assignedto_id;
	}

	public Object getBlocked_count() {
		return blocked_count;
	}

	public void setBlocked_count(Object blocked_count) {
		this.blocked_count = blocked_count;
	}

	public Object getCompleted_on() {
		return completed_on;
	}

	public void setCompleted_on(Object completed_on) {
		this.completed_on = completed_on;
	}

	public Object getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Object created_by) {
		this.created_by = created_by;
	}

	public Object getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Object created_on) {
		this.created_on = created_on;
	}

	public Object getDescription() {
		return description;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Object getFailed_count() {
		return failed_count;
	}

	public void setFailed_count(Object failed_count) {
		this.failed_count = failed_count;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(Object is_completed) {
		this.is_completed = is_completed;
	}

	public Long getMilestone_id() {
		return milestone_id;
	}

	public void setMilestone_id(Long milestone_id) {
		this.milestone_id = milestone_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getPassed_count() {
		return passed_count;
	}

	public void setPassed_count(Object passed_count) {
		this.passed_count = passed_count;
	}

	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public Object getRetest_count() {
		return retest_count;
	}

	public void setRetest_count(Object retest_count) {
		this.retest_count = retest_count;
	}

	public Object getUntested_count() {
		return untested_count;
	}

	public void setUntested_count(Object untested_count) {
		this.untested_count = untested_count;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "Plan [assignedto_id=" + assignedto_id + ", blocked_count=" + blocked_count + ", completed_on="
				+ completed_on + ", created_by=" + created_by + ", created_on=" + created_on + ", description="
				+ description + ", entries=" + entries + ", failed_count=" + failed_count + ", id=" + id
				+ ", is_completed=" + is_completed + ", milestone_id=" + milestone_id + ", name=" + name
				+ ", passed_count=" + passed_count + ", project_id=" + project_id + ", retest_count=" + retest_count
				+ ", untested_count=" + untested_count + ", url=" + url + "]";
	}

	public List<Run> getRuns() {
		if (runs == null) {
			this.runs = new ArrayList<>();
			List<Entry> entries2 = getEntries();
			for(Entry entry : entries2){
				List<Run> runs2 = entry.getRuns();
				for(Run run : runs2){
					this.runs.add(run);
				}
			}
		}
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Test> getTests() {
		if (tests == null) {
			tests = new ArrayList<>();
			for (Run run : getRuns())
				for (Test test : run.getTests())
					tests.add(test);
		}
		return tests;
	}

	public void setTests(List<Test> tests) {
		this.tests = tests;
	}

}
