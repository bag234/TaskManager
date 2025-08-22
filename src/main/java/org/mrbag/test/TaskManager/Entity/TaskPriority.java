package org.mrbag.test.TaskManager.Entity;

public enum TaskPriority {

	LOW(0), MEDIUM(1), HIGH(2);

	private int level;
	
	private TaskPriority(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
}
