package com.cricbuzz.favplayer;


public class PlayerRowDataItem {

	public String id;
	public String name;
	public String url;
	
	public PlayerRowDataItem(String id, String name, String url ) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	@Override
	public String toString() {
		return name;
	}
}
