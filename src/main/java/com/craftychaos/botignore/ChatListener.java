package com.craftychaos.botignore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private final String chatPattern = "<(.*?)> (.*)";
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		String message = event.getMessage().getUnformattedText();
		Pattern pattern = Pattern.compile(this.chatPattern);
		Matcher haystack = pattern.matcher(message);
				
		if (haystack.find() ) {
			final NetHandlerPlayClient netHandlerPlayClient = minecraft.getConnection();
			String theNick = haystack.group(1);
			String theMessage = haystack.group(2);
			UUID theNickUUID = netHandlerPlayClient.getPlayerInfo(theNick).getGameProfile().getId();
			
			// log chat users and their UUID for future list additions
			BotIgnore.logger.info(theNick + " [" + theNickUUID.toString() + "]: " + theMessage);
			
			// loop through the bot list
			for (String listEntry:BotIgnore.blockList.getBotList()) {
				if (theNickUUID.toString() == listEntry) {
					
					// setup a timer to ignore player
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							Minecraft.getMinecraft().player.sendChatMessage( "/ignorehard " + theNick);
						}
					}, 1000);
					break;
				}
			}
		}
	}
}
