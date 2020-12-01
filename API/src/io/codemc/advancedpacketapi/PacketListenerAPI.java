package io.codemc.advancedpacketapi;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.apihelper.API;
import org.inventivetalent.apihelper.APIManager;

import io.codemc.advancedpacketapi.channel.ChannelWrapper;
import io.codemc.advancedpacketapi.handler.AbstractPacketHandler;
import io.codemc.advancedpacketapi.handler.HandlerManager;
import io.codemc.advancedpacketapi.handler.PacketEvent;
import io.codemc.advancedpacketapi.handler.UnknownWrappedPacket;
import io.codemc.advancedpacketapi.handler.WrappedPacket;

public class PacketListenerAPI implements IPacketListener, Listener, API {

	private ChannelInjector channelInjector;
	protected boolean injected = false;
	private HandlerManager sendManager;
	private HandlerManager receiveManager;

	Logger logger = Logger.getLogger("PacketListenerAPI");

	//This gets called either by #registerAPI above, or by the API manager if another plugin requires this API
	@Override
	public void load() {
		channelInjector = new ChannelInjector();
		if (injected = channelInjector.inject(this)) {
			channelInjector.addServerChannel();
			logger.info("Injected custom channel handlers.");
		} else {
			logger.severe("Failed to inject channel handlers");
		}

	}

	//This gets called either by #initAPI above or #initAPI in one of the requiring plugins
	@Override
	public void init(Plugin plugin) {
		sendManager = new HandlerManager();
		receiveManager = new HandlerManager();
		//Register our events
		APIManager.registerEvents(this, this);

		logger.info("Adding channels for online players...");
		for (Player player : Bukkit.getOnlinePlayers()) {
			channelInjector.addChannel(player);
		}
	}

	//This gets called either by #disableAPI above or #disableAPI in one of the requiring plugins
	@Override
	public void disable(Plugin plugin) {
		if (!injected) {
			return;//Not enabled
		}
		logger.info("Removing channels for online players...");
		for (Player player : Bukkit.getOnlinePlayers()) {
			channelInjector.removeChannel(player);
		}

		sendManager = new HandlerManager();
		receiveManager = new HandlerManager();
	}

	/**
	 * @param handler PacketSendHandler to add
	 * @return <code>true</code> if the handler was added
	 * @see PacketSendHandler#addHandler(PacketSendHandler)
	 */
	public <T extends WrappedPacket> boolean addSendHandler(Class<T> wrapperClass, AbstractPacketHandler<T> handler) {
		return sendManager.addHandler(wrapperClass, handler);
	}
	
	/**
	 * @param handler PacketReceiveHandler to add
	 * @return <code>true</code> if the handler was added
	 * @see PacketReceiveHandler#addHandler(PacketReceiveHandler)
	 */
	public <T extends WrappedPacket> boolean addReceiveHandler(Class<T> wrapperClass, AbstractPacketHandler<T> handler) {
		return receiveManager.addHandler(wrapperClass, handler);
	}

	/**
	 * @param handler PacketSendHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketSendHandler#removeHandler(PacketSendHandler)
	 */
	//TODO
	//public static boolean removePacketHandler(PacketSendHandler handler) {
	//	return PacketSendHandler.removeHandler(handler);
	//}
	
	/**
	 * @param handler PacketReceiveHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketReceiveHandler#removeHandler(PacketReceiveHandler)
	 */
	//TODO
	//public static boolean removePacketHandler(PacketReceiveHandler handler) {
	//	return PacketReceiveHandler.removeHandler(handler);
	//}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		channelInjector.addChannel(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		channelInjector.removeChannel(e.getPlayer());
	}

	@Override
	public Object onPacketReceive(Player player, ChannelWrapper<?> channel, Object packet) {
		PacketEvent<WrappedPacket> receivedPacket = new PacketEvent<WrappedPacket>(player, channel, new UnknownWrappedPacket(packet)); // TODO wrap packet
		receiveManager.notifyHandlers(receivedPacket);
		if (receivedPacket.isCancelled()) { return null; }
		return receivedPacket.getWrappedPacket().getPacket();
	}

	@Override
	public Object onPacketSend(Player player, ChannelWrapper<?> channel, Object packet) {
		PacketEvent<WrappedPacket> sentPacket =  new PacketEvent<WrappedPacket>(player, channel, new UnknownWrappedPacket(packet)); // TODO wrap packet
		sendManager.notifyHandlers(sentPacket);
		if (sentPacket.isCancelled()) { return null; }
		return sentPacket.getWrappedPacket().getPacket();
	}

	@Override
	public boolean hasSendHandler(Class<?> packet) {
		// TODO map nms class to wrapper class and keep track of what gets listened to
		return true;
	}

	@Override
	public boolean hasReceiveHandler(Class<?> packet) {
		// TODO map nms class to wrapper class and keep track of what gets listened to
		return true;
	}
}
