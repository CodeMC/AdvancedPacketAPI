package io.codemc.advancedpacketapi.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import io.codemc.advancedpacketapi.channel.ChannelWrapper;

public class PacketEvent<T extends WrappedPacket> implements Cancellable {

	private final Player player;
	private final ChannelWrapper<?> channel;
	private T wrappedPacket;
	private boolean canceled = false;
	
	public PacketEvent(Player player, ChannelWrapper<?> channel, T packet) {
		this.player = player;
		this.channel = channel;
		this.wrappedPacket = packet;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public ChannelWrapper<?> getChannel(){
		return channel;
	}
	
	public void setWrappedPacket(T packet) {
		this.wrappedPacket = packet;
	}
	
	public T getWrappedPacket() {
		return wrappedPacket;
	}
	
	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.canceled = cancel;
	}

	@Override
	public String toString() {
		return "PacketEvent [player=" + player + ", channel=" + channel + ", wrappedPacket=" + wrappedPacket
				+ ", canceled=" + canceled + "]";
	}

}
