package io.codemc.advancedpacketapi.test;

import org.bukkit.plugin.Plugin;

import io.codemc.advancedpacketapi.handler.AbstractPacketHandler;
import io.codemc.advancedpacketapi.handler.PacketEvent;
import io.codemc.advancedpacketapi.handler.UnknownWrappedPacket;

public class TestHandler extends AbstractPacketHandler<UnknownWrappedPacket>{

	private String dir;
	
	public TestHandler(Plugin plugin, String dir) {
		super(plugin);
		this.dir = dir;
	}

	@Override
	public void handle(PacketEvent<UnknownWrappedPacket> event) {
		System.out.println("Packet " + dir + ": " + event);
	}

}
