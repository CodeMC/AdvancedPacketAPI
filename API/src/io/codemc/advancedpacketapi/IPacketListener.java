package io.codemc.advancedpacketapi;

import org.bukkit.entity.Player;

import io.codemc.advancedpacketapi.channel.ChannelWrapper;

public interface IPacketListener {

	Object onPacketSend(Player player, ChannelWrapper<?> channel, Object packet);

	Object onPacketReceive(Player player, ChannelWrapper<?> channel, Object packet);
	
	boolean hasSendHandler(Class<?> packet);
	
	boolean hasReceiveHandler(Class<?> packet);
}
