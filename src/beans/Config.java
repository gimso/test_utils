package beans;

public class Config {
	private String groupName;
	private Long groupId;
	private String name;
	private Long id;

	public Config(String groupName, Long groupId, String name, Long id) {
		super();
		this.groupName = groupName;
		this.groupId = groupId;
		this.name = name;
		this.id = id;
	}

	public Config(String name, Long id) {
		this.name = name;
		this.id = id;
	}

	public Config() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Config [groupName=" + groupName + ", groupId=" + groupId + ", name=" + name + ", id=" + id + "]";
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}