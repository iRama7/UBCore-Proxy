package rama.ubcproxy.ubcorebungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rama.ubcproxy.ubcorebungee.sender.MainCommandSender;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PlayerJoinMessage implements Listener {

    private UBCoreBungee plugin;

    public PlayerJoinMessage(UBCoreBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void joinEvent(ServerConnectEvent e) throws IOException {
        if(e.getReason() == ServerConnectEvent.Reason.PLUGIN_MESSAGE){
            ServerInfo minasServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.minas-server-name"));
            ServerInfo villaServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.villa-server-name"));
            if(e.getTarget().equals(minasServer) || e.getTarget().equals(villaServer)) {
                ProxiedPlayer player = e.getPlayer();
                ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
                    public void run() {
                        if (!e.isCancelled()) {
                            MainCommandSender mcs = new MainCommandSender();
                            try {
                                ServerInfo minasServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.minas-server-name"));
                                ServerInfo villaServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.villa-server-name"));
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("PlayerJoinEvent");
                                out.writeUTF(e.getPlayer().getName());
                                try {
                                    Boolean debugMode = plugin.getConfig().getBoolean("config.debug-mode");
                                    if (debugMode) {
                                        plugin.getLogger().info("[UBCore] PlayerJoinEvent fired for " + e.getPlayer().getName());
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                if (minasServer.getPlayers() != null && !minasServer.getPlayers().isEmpty()) {
                                    mcs.sendData(player, out,minasServer);
                                } else {
                                    plugin.getLogger().info("[UBCore] Join message cancelled in minas");
                                }
                                if (villaServer.getPlayers() != null && !villaServer.getPlayers().isEmpty()) {
                                    mcs.sendData(player, out,villaServer);
                                } else {
                                    plugin.getLogger().info("[UBCore] Join message cancelled in villa");
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            try {
                                Boolean debugMode = plugin.getConfig().getBoolean("config.debug-mode");
                                if (debugMode) {
                                    plugin.getLogger().info("[UBCore] PlayerJoinEvent was cancelled for " + e.getPlayer().getName());
                                }
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                }, 1, TimeUnit.SECONDS);
            }
            plugin.getLogger().info("[UBCore] Target was " + e.getTarget());
        }
        plugin.getLogger().info("[UBCore] Reason was " + e.getReason());
    }
}
