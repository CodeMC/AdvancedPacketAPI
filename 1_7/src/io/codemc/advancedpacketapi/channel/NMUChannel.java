package io.codemc.advancedpacketapi.channel;

import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;
import org.inventivetalent.reflection.minecraft.Minecraft;

import io.codemc.advancedpacketapi.IPacketListener;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.ArrayList;

public class NMUChannel extends ChannelAbstract {

	private static final Field channelField = networkManagerFieldResolver.resolveByFirstTypeSilent(net.minecraft.util.io.netty.channel.Channel.class);

	public NMUChannel(IPacketListener iPacketListener) {
		super(iPacketListener);
	}

	@Override
	public void addChannel(final Player player) {
		try {
			final net.minecraft.util.io.netty.channel.Channel channel = getChannel(player);
			addChannelExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						channel.pipeline().addBefore(KEY_HANDLER, KEY_PLAYER, new ChannelHandler(player, new NMUChannelWrapper(channel)));
						if (channel.pipeline().get(KEY_SERVER) != null) {
							channel.pipeline().remove(KEY_SERVER);
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to add channel for " + player, e);
		}
	}

	@Override
	public void removeChannel(Player player) {
		try {
			final net.minecraft.util.io.netty.channel.Channel channel = getChannel(player);
			removeChannelExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						if (channel.pipeline().get(KEY_PLAYER) != null) {
							channel.pipeline().remove(KEY_PLAYER);
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to remove channel for " + player, e);
		}
	}

	net.minecraft.util.io.netty.channel.Channel getChannel(Player player) throws ReflectiveOperationException {
		final Object handle = Minecraft.getHandle(player);
		final Object connection = playerConnection.get(handle);
		return (net.minecraft.util.io.netty.channel.Channel) channelField.get(networkManager.get(connection));
	}

	@Override
	public IListenerList<Object> newListenerList() {
		return new ListenerList<>();
	}

	class ListenerList<E> extends ArrayList<E> implements IListenerList<E> {

		@Override
		public boolean add(E paramE) {
			try {
				final E a = paramE;
				addChannelExecutor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							net.minecraft.util.io.netty.channel.Channel channel = null;
							while (channel == null) {
								channel = (net.minecraft.util.io.netty.channel.Channel) channelField.get(a);
							}
							if (channel.pipeline().get(KEY_SERVER) == null) {
								channel.pipeline().addBefore(KEY_HANDLER, KEY_SERVER, new ChannelHandler(null, new NMUChannelWrapper(channel)));
							}
						} catch (Exception e) {
						}
					}
				});
			} catch (Exception e) {
			}
			return super.add(paramE);
		}

		@Override
		public boolean remove(Object arg0) {
			try {
				final Object a = arg0;
				removeChannelExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							net.minecraft.util.io.netty.channel.Channel channel = null;
							while (channel == null) {
								channel = (net.minecraft.util.io.netty.channel.Channel) channelField.get(a);
							}
							channel.pipeline().remove(KEY_SERVER);
						} catch (Exception e) {
						}
					}
				});
			} catch (Exception e) {
			}
			return super.remove(arg0);
		}
	}

	class ChannelHandler extends net.minecraft.util.io.netty.channel.ChannelDuplexHandler implements IChannelHandler {

		private final Player player;
		private final ChannelWrapper<?> channel;

		public ChannelHandler(Player player, ChannelWrapper<?> channel) {
			this.player = player;
			this.channel = channel;
		}

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			Object pckt = msg;
			if (Packet.isAssignableFrom(msg.getClass())) {
				pckt = onPacketSend(player, channel, msg);
			}
			if (pckt == null) { return; }
			super.write(ctx, pckt, promise);
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			Object pckt = msg;
			if (Packet.isAssignableFrom(msg.getClass())) {
				pckt = onPacketReceive(player, channel, msg);
			}
			if (pckt == null) { return; }
			super.channelRead(ctx, pckt);
		}

		@Override
		public String toString() {
			return "NMUChannel$ChannelHandler@" + hashCode() + " (" + this.player + ", " + this.channel + ")";
		}
	}

	class NMUChannelWrapper extends ChannelWrapper<net.minecraft.util.io.netty.channel.Channel> implements IChannelWrapper {

		public NMUChannelWrapper(net.minecraft.util.io.netty.channel.Channel channel) {
			super(channel);
		}

		@Override
		public SocketAddress getRemoteAddress() {
			return this.channel().remoteAddress();
		}

		@Override
		public SocketAddress getLocalAddress() {
			return this.channel().localAddress();
		}
	}
}
