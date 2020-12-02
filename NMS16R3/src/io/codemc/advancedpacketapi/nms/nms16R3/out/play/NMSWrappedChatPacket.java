package io.codemc.advancedpacketapi.nms.nms16R3.out.play;

import java.util.UUID;

import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;

public class NMSWrappedChatPacket extends WrappedChatPacket{

	private PacketPlayOutChat packet;
	
	public NMSWrappedChatPacket(Object packet) {
		super(packet);
		this.packet = (PacketPlayOutChat) packet;
	}
	
	public NMSWrappedChatPacket(String message, ChatMessageType type, UUID uuid) {
		super(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.b(message), net.minecraft.server.v1_16_R3.ChatMessageType.values()[type.ordinal()], uuid));
		this.packet = (PacketPlayOutChat) getPacket();
	}

}
