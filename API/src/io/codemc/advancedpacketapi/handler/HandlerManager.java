package io.codemc.advancedpacketapi.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HandlerManager {

	private Map<Class<? extends WrappedPacket>, Set<AbstractPacketHandler<? extends WrappedPacket>>> handlers = new HashMap<>();
	
	/**
	 * @param <T>
	 * @param wrapperClass
	 * @param handler
	 * @return true when the new handler got added
	 */
	public <T extends WrappedPacket> boolean addHandler(Class<T> wrapperClass, AbstractPacketHandler<T> handler) {
		Objects.requireNonNull(wrapperClass);
		Objects.requireNonNull(handler);
		Set<AbstractPacketHandler<? extends WrappedPacket>> set = handlers.computeIfAbsent(wrapperClass, (c) -> new HashSet<>());
		boolean contains = set.contains(handler);
		if (!contains) {
			set.add(handler);
		}
		return !contains;
	}
	
	@SuppressWarnings("unchecked")
	public void notifyHandlers(PacketEvent<? extends WrappedPacket> packet) {
		if(handlers.containsKey(packet.getWrappedPacket().getClass())) {
			Set<AbstractPacketHandler<? extends WrappedPacket>> set = handlers.get(packet.getWrappedPacket().getClass());
			for(AbstractPacketHandler<? extends WrappedPacket> handler : set) {
				((AbstractPacketHandler<WrappedPacket>)handler).handle((PacketEvent<WrappedPacket>) packet);
			}
		}
	}
	
}
