package rama.ubcproxy.ubcorebungee.reader;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rama.ubcproxy.ubcorebungee.PlayerSender;
import rama.ubcproxy.ubcorebungee.UBCoreBungee;
import rama.ubcproxy.ubcorebungee.authhook.ServerHistory;

import java.io.IOException;

public class PMListener implements Listener {

    private UBCoreBungee plugin;

    public PMListener(UBCoreBungee plugin) throws IOException {
       this.plugin = plugin;
    }


    @EventHandler
    public void Listen(PluginMessageEvent e) throws IOException {
        if(!e.getTag().equals("BungeeCord")){
            return;
        }
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String channel = in.readUTF();
            if(channel.equals("UBCoreChannel")) {
                String subChannel = in.readUTF();
                //Minas - Nether - Dragon
                if (subChannel.equals("MainCommandChannel")) {
                    String data = in.readUTF();
                    String[] parts = data.split(";");
                    String player_name = parts[0];
                    String command_name = parts[1];
                    PlayerSender ps = new PlayerSender(plugin);
                    if (command_name.equals("minas")) {
                        ps.sendToMinas(player_name, command_name);
                    } else if (command_name.equals("nether")) {
                        ps.sendToMinas(player_name, command_name);
                    } else if (command_name.equals("dragon")) {
                        ps.sendToMinas(player_name, command_name);
                    } else if (command_name.equals("villa")) {
                        ps.sendToVilla(player_name, command_name);
                    } else if (command_name.equals("parcelas")) {
                        ps.sendToVilla(player_name, command_name);
                    }
                    Boolean debugMode = plugin.getConfig().getBoolean("config.debug-mode");
                    if (debugMode) {
                        plugin.getLogger().info("[UBCore] Got a MainCommandChannel message with the following details:");
                        plugin.getLogger().info("[UBCore] Data: " + data);
                        plugin.getLogger().info("[UBCore] Player name: " + player_name);
                        plugin.getLogger().info("[UBCore] Send name: " + command_name);
                    }
                } else if (subChannel.equals("LoginEventChannel") && plugin.getConfig().getBoolean("config.enable-login_event")) {
                    String data = in.readUTF();
                    String[] parts = data.split(";");
                    String uuid = parts[0];
                    String player_name = parts[1];
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(player_name);
                    String minas_server_name = plugin.getConfig().getString("config.minas-server-name");
                    ServerHistory SH = new ServerHistory(plugin);
                    if (SH.getLastServer(uuid).equals("minas")) {
                        player.connect(ProxyServer.getInstance().getServerInfo(minas_server_name));
                    }
                }
            }
    }
}
