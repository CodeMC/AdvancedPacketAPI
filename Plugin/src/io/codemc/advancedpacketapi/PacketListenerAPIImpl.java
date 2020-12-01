package io.codemc.advancedpacketapi;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import io.codemc.advancedpacketapi.channel.ChannelWrapper;
import io.codemc.advancedpacketapi.handler.HandlerManager;
import io.codemc.advancedpacketapi.packets.PacketEvent;
import io.codemc.advancedpacketapi.packets.PacketHandler;
import io.codemc.advancedpacketapi.packets.UnknownWrappedPacket;
import io.codemc.advancedpacketapi.packets.WrappedPacket;

public class PacketListenerAPIImpl extends AdvancedPacketAPI implements IPacketListener, Listener {

	private ChannelInjector channelInjector;
	protected boolean injected = false;
	private HandlerManager sendManager;
	private HandlerManager receiveManager;

	Logger logger = Logger.getLogger("PacketListenerAPI");

	public void onLoad() {
		if(injected)return;
		channelInjector = new ChannelInjector();
		if (injected = channelInjector.inject(this)) {
			channelInjector.addServerChannel();
			logger.info("Injected custom channel handlers.");
		} else {
			logger.severe("Failed to inject channel handlers");
		}

	}

	public void onEnable(Plugin plugin) {
		if (!injected) {
			return;//Not enabled
		}
		sendManager = new HandlerManager();
		receiveManager = new HandlerManager();
		//Register our events
		Bukkit.getPluginManager().registerEvents(this, plugin);

		logger.info("Adding channels for online players...");
		for (Player player : Bukkit.getOnlinePlayers()) {
			channelInjector.addChannel(player);
		}
	}

	//This gets called either by #disableAPI above or #disableAPI in one of the requiring plugins
	public void onDisable() {
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
	@Override
	public <T extends WrappedPacket> boolean addSendHandler(Class<T> wrapperClass, PacketHandler<T> handler) {
		return sendManager.addHandler(wrapperClass, handler);
	}
	
	/**
	 * @param handler PacketReceiveHandler to add
	 * @return <code>true</code> if the handler was added
	 * @see PacketReceiveHandler#addHandler(PacketReceiveHandler)
	 */
	@Override
	public <T extends WrappedPacket> boolean addReceiveHandler(Class<T> wrapperClass, PacketHandler<T> handler) {
		return receiveManager.addHandler(wrapperClass, handler);
	}

	/**
	 * @param handler PacketSendHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketSendHandler#removeHandler(PacketSendHandler)
	 */
	@Override
	public boolean removeSendHandler(PacketHandler<?> handler) {
		return sendManager.removeHandler(handler);
	}
	
	/**
	 * @param handler PacketReceiveHandler to remove
	 * @return <code>true</code> if the handler was removed
	 * @see PacketReceiveHandler#removeHandler(PacketReceiveHandler)
	 */
	@Override
	public boolean removeReceiveHandler(PacketHandler<?> handler) {
		return receiveManager.removeHandler(handler);
	}
	
	@Override
	public void removePluginHandlers(Plugin plugin) {
		sendManager.removeHandlers(plugin);
		receiveManager.removeHandlers(plugin);
	}

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
