package cc.idiary.nuclear.model.system;

import cc.idiary.nuclear.model.BaseModel;

public class SysNameModel extends BaseModel {
	
	private String name;
	private String police;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPolice() {
		return police;
	}
	public void setPolice(String police) {
		this.police = police;
	}
	
}