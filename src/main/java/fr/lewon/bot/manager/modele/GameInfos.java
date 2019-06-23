package fr.lewon.bot.manager.modele;

import java.util.ArrayList;
import java.util.List;

public class GameInfos {

	private String id;
	private String name;
	private List<RunnerInfos> runnerInfos;


	public GameInfos(String id, String name) {
		this(id, name, new ArrayList<>());
	}
	
	public GameInfos(String id, String name, List<RunnerInfos> runnerInfos) {
		this.id = id;
		this.name = name;
		this.runnerInfos = runnerInfos;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RunnerInfos> getRunnerInfos() {
		return runnerInfos;
	}
	public void setRunnerInfos(List<RunnerInfos> runnerInfos) {
		this.runnerInfos = runnerInfos;
	}

}
