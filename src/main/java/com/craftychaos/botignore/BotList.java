package com.craftychaos.botignore;

import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class BotList {
	private Integer interval;
	private String listUrl;
	private List<String> botList;

	public BotList(Integer interval, String listUrl) {
		this.interval = interval;
		this.listUrl = listUrl;
		this.setBotList(new ArrayList<String>());
		
		scheduleUpdate();
	}
	
	private void scheduleUpdate() {
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateList();
			}
		}, 0, 1000 * 60 * this.interval);
	}
	
	private void updateList() {
		BotIgnore.logger.error("Updating Bot List");
		try {
			URL url = new URL(this.listUrl);
			BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
			List<String> theList = new ArrayList<String>();
			String line;
			while ((line = input.readLine()) != null) {
				theList.add(line);
			}
			input.close();
			
			setBotList(theList);
		} catch (IOException e) {
			BotIgnore.logger.error("Unable to update list");
		}
	}

	private void setBotList(List<String> botList) {
		this.botList = botList;
	}
	
	public List<String> getBotList() {
		return botList;
	}
}
