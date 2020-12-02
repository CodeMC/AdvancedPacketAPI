package io.codemc.advancedpacketapi;

import org.bukkit.plugin.Plugin;

import io.codemc.advancedpacketapi.nms.NMSHandler;
import io.codemc.advancedpacketapi.packets.PacketHandler;
import io.codemc.advancedpacketapi.packets.WrappedPacket;

public abstract class AdvancedPacketAPI {

	private static AdvancedPacketAPI instance = null;
	
	public static AdvancedPacketAPI getAPI() {
		return instance;
	}
	
	protected static void setupInstance(AdvancedPacketAPI inst) {
		instance = inst;
	}
	
	/**
	 * @param handler PacketSendHandler to add
	 * @return <code>true</code> if the handler was added
	 * @see PacketSendHandler#addHandler(PacketSendHandler)
	 */
	public abstract <T extends WrappedPacket> boolean addSendHandler(Class<T> wrapperClass, PacketHandler<T> handler);

	/**
	 * @param handler PacketReceiveHandler to add
	 * @return <code>true</code> if the handler was added
	 * @see PacketReceiveHandler#addHandler(PacketReceiveHandler)
	 */
	public abstract <T extends WrappedPacket> boolean addReceiveHandler(Class<T> wrapperClass, PacketHandler<T> handler);

	/**
	 * @param handler PacketSendHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketSendHandler#removeHandler(PacketSendHandler)
	 */
	public abstract boolean removeSendHandler(PacketHandler<?> handler);

	/**
	 * @param handler PacketReceiveHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketReceiveHandler#removeHandler(PacketReceiveHandler)
	 */
	public abstract boolean removeReceiveHandler(PacketHandler<?> handler);

	public abstract void removePluginHandlers(Plugin plugin);
	
	public abstract NMSHandler getNMS();

}