package io.codemc.advancedpacketapi.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.bukkit.plugin.Plugin;

import io.codemc.advancedpacketapi.packets.PacketEvent;
import io.codemc.advancedpacketapi.packets.PacketHandler;
import io.codemc.advancedpacketapi.packets.WrappedPacket;

public class HandlerManager {

	private Map<Class<? extends WrappedPacket>, Set<PacketHandler<? extends WrappedPacket>>> handlers = new HashMap<>();
	
	/**
	 * @param <T>
	 * @param wrapperClass
	 * @param handler
	 * @return true when the new handler got added
	 */
	public <T extends WrappedPacket> boolean addHandler(Class<T> wrapperClass, PacketHandler<T> handler) {
		Objects.requireNonNull(wrapperClass);
		Objects.requireNonNull(handler);
		Set<PacketHandler<? extends WrappedPacket>> set = handlers.computeIfAbsent(wrapperClass, (c) -> new HashSet<>());
		boolean contains = set.contains(handler);
		if (!contains) {
			set.add(handler);
		}
		return !contains;
	}
	
	@SuppressWarnings("unchecked")
	public void notifyHandlers(PacketEvent<? extends WrappedPacket> packet) {
		if(handlers.containsKey(packet.getWrappedPacket().getClass())) {
			Set<PacketHandler<? extends WrappedPacket>> set = handlers.get(packet.getWrappedPacket().getClass());
			for(PacketHandler<? extends WrappedPacket> handler : set) {
				((PacketHandler<WrappedPacket>)handler).handle((PacketEvent<WrappedPacket>) packet);
			}
		}
	}

	public boolean removeHandler(PacketHandler<?> handler) {
		for(Set<PacketHandler<? extends WrappedPacket>> entries : handlers.values()) {
			if(entries.remove(handler)) {
				return true;
			}
		}
		return false;
	}

	public void removeHandlers(Plugin plugin) {
		for(Set<PacketHandler<? extends WrappedPacket>> entries : handlers.values()) {
			entries.removeIf(handler -> handler.getPlugin() == plugin);
		}
	}
	
}
