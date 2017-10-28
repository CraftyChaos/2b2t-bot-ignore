package com.craftychaos.botignore;

import java.util.TimerTask;
import java.util.stream.Collectors;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
		}, 60000, 1000 * 60 * this.interval);
	}
	
	private void updateList() {
		ITextComponent statusMessage = new TextComponentString("BotIgnore: Updating bot list");
		try {
			BotIgnore.getMinecraft().player.sendMessage(statusMessage);
			BotIgnore.logger.info("Updating Bot List");
			URL url = new URL(this.listUrl);
			URLConnection con = url.openConnection();
			con.setUseCaches(false);
			
			BufferedReader input = new BufferedReader(
				new InputStreamReader(con.getInputStream())
			);
			
			List<String> theList = new ArrayList<String>();
			String line;
			while ((line = input.readLine()) != null) {
				theList.add(line);
			}
			input.close();
			
			List<String> result = theList.stream().filter(elem -> !getBotList().contains(elem)).collect(Collectors.toList());
			if (result.size() > 0) {
				ITextComponent diff = new TextComponentString("Added: " + result.toString());
				BotIgnore.getMinecraft().player.sendMessage(diff);
			}
			
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
