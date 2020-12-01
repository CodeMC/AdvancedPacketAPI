package io.codemc.advancedpacketapi.packets;

import java.util.Objects;

import org.bukkit.plugin.Plugin;

public abstract class PacketHandler<T extends WrappedPacket> {

	private final Plugin plugin;
	
	public PacketHandler(Plugin plugin) {
		Objects.requireNonNull(plugin);
		this.plugin = plugin;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public abstract void handle(PacketEvent<T> event);
	
}
