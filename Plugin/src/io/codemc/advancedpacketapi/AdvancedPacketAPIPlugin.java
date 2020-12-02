package io.codemc.advancedpacketapi;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket;
import io.codemc.advancedpacketapi.nms.play.out.WrappedChatPacket.ChatMessageType;
import io.codemc.advancedpacketapi.packets.UnknownWrappedPacket;
import io.codemc.advancedpacketapi.test.TestHandler;

public class AdvancedPacketAPIPlugin extends JavaPlugin {

	private PacketListenerAPIImpl packetListenerAPI = new PacketListenerAPIImpl();

	@Override
	public void onLoad() {
		//Register this API if the plugin gets loaded
		packetListenerAPI.onLoad();
		AdvancedPacketAPI.setupInstance(packetListenerAPI);
		packetListenerAPI.setupNMS(new io.codemc.advancedpacketapi.nms.nms16R3.NMSHandlerImpl());
	}

	@Override
	public void onEnable() {
		if (!packetListenerAPI.injected) {
			getLogger().warning("Injection failed. Disabling...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		/*try {
			SpigetUpdate updater = new SpigetUpdate(this, 2930);
			updater.setUserAgent("PacketListenerAPI/" + getDescription().getVersion()).setVersionComparator(VersionComparator.SEM_VER_SNAPSHOT);
			updater.checkForUpdate(new UpdateCallback() {
				@Override
				public void updateAvailable(String s, String s1, boolean b) {
					getLogger().info("There is a new version available: https://r.spiget.org/2930");
				}

				@Override
				public void upToDate() {
					getLogger().info("Plugin is up-to-date");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();	
		}

		new Metrics(this, 225);*/

		//Initialize this API if the plugin got enabled
		packetListenerAPI.onEnable(this);
		//AdvancedPacketAPI.getAPI().addReceiveHandler(UnknownWrappedPacket.class, new TestHandler(this, "in"));
		//AdvancedPacketAPI.getAPI().addSendHandler(UnknownWrappedPacket.class, new TestHandler(this, "out"));
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			int counter = 0;
			for(Player p : Bukkit.getOnlinePlayers()) {
				long start = System.currentTimeMillis();
				while(System.currentTimeMillis() - start < 50) {
					WrappedChatPacket packet = packetListenerAPI.getNMS().createChatPacket("test", ChatMessageType.CHAT, UUID.randomUUID());
					packetListenerAPI.getNMS().sendPacket(p, packet);
					counter++;
				}
			}
			System.out.println("In 50ms: " + counter);
		}, 1, 1);
	}

	@Override
	public void onDisable() {
		//Disable this API if the plugin was enabled
		packetListenerAPI.onDisable();
	}
	
	public AdvancedPacketAPI getAPI() {
		return packetListenerAPI;
	}

}
