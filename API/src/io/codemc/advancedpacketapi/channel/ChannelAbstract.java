package io.codemc.advancedpacketapi.channel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.util.AccessUtil;

import io.codemc.advancedpacketapi.IPacketListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ChannelAbstract {

	protected static final NMSClassResolver nmsClassResolver = new NMSClassResolver();

	static final Class<?> EntityPlayer     = nmsClassResolver.resolveSilent("EntityPlayer");
	static final Class<?> PlayerConnection = nmsClassResolver.resolveSilent("PlayerConnection");
	static final Class<?> NetworkManager   = nmsClassResolver.resolveSilent("NetworkManager");
	static final Class<?> Packet           = nmsClassResolver.resolveSilent("Packet");
	static final Class<?> ServerConnection = nmsClassResolver.resolveSilent("ServerConnection");
	static final Class<?> MinecraftServer  = nmsClassResolver.resolveSilent("MinecraftServer");

	protected static final FieldResolver entityPlayerFieldResolver     = new FieldResolver(EntityPlayer);
	protected static final FieldResolver playerConnectionFieldResolver = new FieldResolver(PlayerConnection);
	protected static final FieldResolver networkManagerFieldResolver   = new FieldResolver(NetworkManager);
	protected static final FieldResolver minecraftServerFieldResolver  = new FieldResolver(MinecraftServer);
	protected static final FieldResolver serverConnectionFieldResolver = new FieldResolver(ServerConnection);

	static final Field networkManager   = playerConnectionFieldResolver.resolveSilent("networkManager");
	static final Field playerConnection = entityPlayerFieldResolver.resolveSilent("playerConnection");
	static final Field serverConnection = minecraftServerFieldResolver.resolveByFirstTypeSilent(ServerConnection);
	static final Field connectionList   = serverConnectionFieldResolver.resolveByLastTypeSilent(List.class);

	protected static final MethodResolver craftServerFieldResolver = new MethodResolver(Bukkit.getServer().getClass());

	static final Method getServer = craftServerFieldResolver.resolveSilent("getServer");

	final Executor addChannelExecutor    = Executors.newSingleThreadExecutor();
	final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();

	static final String KEY_HANDLER = "packet_handler";
	static final String KEY_PLAYER  = "packet_listener_player";
	static final String KEY_SERVER  = "packet_listener_server";

	private IPacketListener iPacketListener;

	public ChannelAbstract(IPacketListener iPacketListener) {
		this.iPacketListener = iPacketListener;
	}

	public abstract void addChannel(Player player);

	public abstract void removeChannel(Player player);

	public void addServerChannel() {
		try {
			Object dedicatedServer = getServer.invoke(Bukkit.getServer());
			if (dedicatedServer == null) { return; }
			Object serverConnection = ChannelAbstract.serverConnection.get(dedicatedServer);
			if (serverConnection == null) { return; }
			List<?> currentList = (List<?>) connectionList.get(serverConnection);
			Field superListField = AccessUtil.setAccessible(currentList.getClass().getSuperclass().getDeclaredField("list"));
			Object list = superListField.get(currentList);
			if (IListenerList.class.isAssignableFrom(list.getClass())) { return; }
			List<Object> newList = Collections.synchronizedList(newListenerList());
			for (Object o : currentList) {
				newList.add(o);
			}
			connectionList.set(serverConnection, newList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract IListenerList<Object> newListenerList();

	protected final Object onPacketSend(Player player, ChannelWrapper<?> channel, Object packet) {
		return iPacketListener.onPacketSend(player, channel, packet);
	}

	protected final Object onPacketReceive(Player player, ChannelWrapper<?> channel, Object packet) {
		return iPacketListener.onPacketReceive(player, channel, packet);
	}
	
	protected boolean hasSendHandler(Class<?> packet) {
		return iPacketListener.hasSendHandler(packet);
	}
	
	protected boolean hasReceiveHandler(Class<?> packet) {
		return iPacketListener.hasReceiveHandler(packet);
	}

	interface IListenerList<E> extends List<E> {
	}

	interface IChannelHandler {
	}

	interface IChannelWrapper {
	}

}
