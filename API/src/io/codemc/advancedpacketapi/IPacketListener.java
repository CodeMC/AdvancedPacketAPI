package io.codemc.advancedpacketapi;

public interface IPacketListener {

	Object onPacketSend(Object receiver, Object packet);

	Object onPacketReceive(Object sender, Object packet);
	
	boolean hasSendHandler(Class<?> packet);
	
	boolean hasReceiveHandler(Class<?> packet);
}
