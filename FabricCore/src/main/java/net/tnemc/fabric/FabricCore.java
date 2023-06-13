package net.tnemc.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.fabric.impl.FabricServerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricCore extends TNECore implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("fabric-tne");
	private MinecraftServer server;

	private static FabricCore instance;

	public FabricCore(LogProvider logger) {
		super(new FabricServerProvider(), logger);
		instance = this;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerLifecycleEvents.SERVER_STARTED.register((server)->{
			this.server = server;
		});

		LOGGER.info("Hello Fabric world!");
	}

	public static MinecraftServer mcSERVER() {
		return instance.server;
	}

	@Override
	public void registerCommands() {

	}

	public static FabricCore instance() {
		return instance;
	}
}
