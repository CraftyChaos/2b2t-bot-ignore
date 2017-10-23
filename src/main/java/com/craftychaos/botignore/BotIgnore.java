package com.craftychaos.botignore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BotIgnore.MODID, version = BotIgnore.VERSION, updateJSON = BotIgnore.UPDATE)
public class BotIgnore {
	private final static Minecraft minecraft = Minecraft.getMinecraft();
	public static final String MODID = "botignore";
	public static final String VERSION = "latest";
  public static final String UPDATE = "https://raw.githubusercontent.com/CraftyChaos/2b2t-bot-ignore/master/update.json";
	public static final Integer updateInterval = 5;
	public static final String listUrl = "https://raw.githubusercontent.com/CraftyChaos/2b2t-bot-ignore/master/signatures/block.txt";
	public static final Logger logger = LogManager.getLogger(MODID);
	public static BotList blockList;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		BotIgnore.logger.info("Loading BotIgnore");
		BotIgnore.blockList = new BotList(updateInterval, listUrl);
		MinecraftForge.EVENT_BUS.register(new ChatListener());
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}
}
