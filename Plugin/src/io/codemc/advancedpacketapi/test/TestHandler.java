package io.codemc.advancedpacketapi.test;

import org.bukkit.plugin.Plugin;

import io.codemc.advancedpacketapi.packets.PacketEvent;
import io.codemc.advancedpacketapi.packets.PacketHandler;
import io.codemc.advancedpacketapi.packets.UnknownWrappedPacket;

public class TestHandler extends PacketHandler<UnknownWrappedPacket>{

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
