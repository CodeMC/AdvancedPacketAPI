package io.codemc.advancedpacketapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import io.codemc.advancedpacketapi.handler.UnknownWrappedPacket;
import io.codemc.advancedpacketapi.metrics.Metrics;
import io.codemc.advancedpacketapi.test.TestHandler;

public class AdvancedPacketAPIPlugin extends JavaPlugin {

	private PacketListenerAPI packetListenerAPI = new PacketListenerAPI();

	@Override
	public void onLoad() {
		//Register this API if the plugin gets loaded
		APIManager.registerAPI(packetListenerAPI, this);
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
		APIManager.initAPI(PacketListenerAPI.class);
		packetListenerAPI.addReceiveHandler(UnknownWrappedPacket.class, new TestHandler(this, "in"));
		packetListenerAPI.addSendHandler(UnknownWrappedPacket.class, new TestHandler(this, "out"));
	}

	@Override
	public void onDisable() {
		//Disable this API if the plugin was enabled
		APIManager.disableAPI(PacketListenerAPI.class);
	}

}
