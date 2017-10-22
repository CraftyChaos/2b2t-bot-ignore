package com.craftychaos.botignore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private final String chatPattern = "<(.*?)> (.*)";
	private List<String> recentlyBlocked = new ArrayList<String>();
	private Long interval = (long) 1;
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		String message = event.getMessage().getUnformattedText();
		Pattern pattern = Pattern.compile(this.chatPattern);
		Matcher haystack = pattern.matcher(message);
		
		// find chat messages
		if (haystack.find() ) {
			final NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();
			String theNick = haystack.group(1);
			String theMessage = haystack.group(2);
			UUID theNickUUID = netHandlerPlayClient.getPlayerInfo(theNick).getGameProfile().getId();
			Integer found = 0;
			
			// we don't want to act on the same nick spamming real fast
			if (!recentlyBlocked.contains(theNickUUID.toString())) {
				
				// loop through the bot list
				for (String listEntry:BotIgnore.blockList.getBotList()) {
					
					// process only matching uuids
					if (listEntry.equals(theNickUUID.toString())) {
						
						// add to recent list to prevent further processing until
						// operation is complete.
						recentlyBlocked.add(listEntry);
					
						// increase time delays to avoid getting disconnected
						// from rapid fire ignore commands
						interval = interval+5;
						
						Timer timer = new Timer();
						found = 1;
						
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								BotIgnore.logger.info("Match: " + listEntry);
								Minecraft.getMinecraft().player.sendChatMessage( "/ignorehard " + theNick);
								
								// clean up
								recentlyBlocked.remove(listEntry);
								interval = interval-5;
							}
						}, interval * 1000);
						
						// break loop to save some cycles for long lists
						break;
					}
				}
			}
			
			// log uuids for nicks not blocked
			if (found == 0) {
				BotIgnore.logger.info(theNick + " [" + theNickUUID.toString() + "]: " + theMessage);
			}
		}
	}
}
