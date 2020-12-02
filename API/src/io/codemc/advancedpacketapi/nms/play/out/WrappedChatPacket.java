package io.codemc.advancedpacketapi.nms.play.out;

import io.codemc.advancedpacketapi.packets.WrappedPacket;

public abstract class WrappedChatPacket extends WrappedPacket {

	public WrappedChatPacket(Object packet) {
		super(packet);
	}
	
	
	public static enum ChatMessageType{
		CHAT, SYSTEM, GAME_INFO;
	}

}
