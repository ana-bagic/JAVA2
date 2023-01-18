package hr.fer.oprpp2.hw06.model;

public class StatsDTO {

	private String nick;
	private int entryCount;
	
	public StatsDTO(String nick, int entryCount) {
		this.nick = nick;
		this.entryCount = entryCount;
	}

	public String getNick() {
		return nick;
	}

	public int getEntryCount() {
		return entryCount;
	}

}
