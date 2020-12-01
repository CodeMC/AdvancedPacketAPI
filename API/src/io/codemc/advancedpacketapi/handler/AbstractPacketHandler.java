package io.codemc.advancedpacketapi.handler;

import java.util.Objects;

import org.bukkit.plugin.Plugin;

public abstract class AbstractPacketHandler<T extends WrappedPacket> {

	private final Plugin plugin;
	
	public AbstractPacketHandler(Plugin plugin) {
		Objects.requireNonNull(plugin);
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public abstract void handle(PacketEvent<T> event);
	
}
