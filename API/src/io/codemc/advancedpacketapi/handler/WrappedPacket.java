package io.codemc.advancedpacketapi.handler;

import java.util.Objects;

public abstract class WrappedPacket {

	private final Object packet;
	
	public WrappedPacket(Object packet) {
		Objects.requireNonNull(packet);
		this.packet = packet;
	}
	
	public Object getPacket() {
		return packet;
	}
	
}
