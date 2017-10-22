package com.craftychaos.botignore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BotIgnore.MODID, version = BotIgnore.VERSION)
public class BotIgnore {
	public static final String MODID = "botignore";
	public static final String VERSION = "1.0";
	
	public static final Integer updateInterval = 5;
	public static final String listUrl = "https://raw.githubusercontent.com/CraftyChaos/2b2t-bot-ignore/master/signatures/block.txt";
	
	public static final Logger logger = LogManager.getLogger(MODID);
	public static final BotList blockList = new BotList(updateInterval, listUrl);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		BotIgnore.logger.info("Loading BotIgnore");
		MinecraftForge.EVENT_BUS.register(new ChatListener());
	}
}
