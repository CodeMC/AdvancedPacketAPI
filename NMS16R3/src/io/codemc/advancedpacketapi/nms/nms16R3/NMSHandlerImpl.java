package io.codemc.advancedpacketapi.nms.nms16R3;

import java.util.UUID;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.codemc.advancedpacketapi.nms.NMSHandler;
import io.codemc.advancedpacketapi.nms.nms16R3.out.play.NMSWrappedChatPacket;
import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket;
import io.codemc.advancedpacketapi.packets.WrappedPacket;
import net.minecraft.server.v1_16_R3.Packet;

public class NMSHandlerImpl implements NMSHandler{

	@Override
	public void sendPacket(Player player, WrappedPacket packet) {
		((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet<?>) packet.getPacket());
	}

	@Override
	public WrappedChatPacket createChatPacket(String message,
			io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket.ChatMessageType type, UUID uuid) {
		return new NMSWrappedChatPacket(message, type, uuid);
	}

	
}
