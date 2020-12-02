package io.codemc.advancedpacketapi.nms;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket;
import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket.ChatMessageType;
import io.codemc.advancedpacketapi.packets.WrappedPacket;

public interface NMSHandler {

	void sendPacket(Player player, WrappedPacket packet);
	
	WrappedChatPacket createChatPacket(String message, ChatMessageType type, UUID uuid);
	
}
