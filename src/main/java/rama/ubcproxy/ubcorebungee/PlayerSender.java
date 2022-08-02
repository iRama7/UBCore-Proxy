package rama.ubcproxy.ubcorebungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rama.ubcproxy.ubcorebungee.sender.MainCommandSender;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class PlayerSender {

    private UBCoreBungee plugin;

    public PlayerSender(UBCoreBungee plugin){
        this.plugin = plugin;
    }

    public void sendToMinas(String player_name, String send) throws IOException {
        ProxiedPlayer player = plugin.getProxy().getPlayer(player_name);
        ServerInfo minasServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.minas-server-name"));
        if(minasServer == null){
            plugin.getLogger().info(ChatColor.RED+"[UBCore] No se pudo inicializar el servidor de minas con nombre "+plugin.getConfig().getString("config.minas-server-name"));
            return;
        }
        player.connect(minasServer);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BungeeMainCommandChannel");
        out.writeUTF(player_name);
        out.writeUTF(send);
        MainCommandSender mcs = new MainCommandSender();
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            public void run () {
                    mcs.sendData(player, out, minasServer);
                    plugin.getLogger().info(ChatColor.YELLOW+"[UBCore] Enviando al jugador "+ChatColor.RED+player_name+ChatColor.YELLOW+" al servidor de Minas para luego teletransportarlo a "+ChatColor.RED+send);
            }
        }, 750, TimeUnit.MILLISECONDS);
    }

    public void sendToVilla(String player_name, String send) throws IOException {
        ProxiedPlayer player = plugin.getProxy().getPlayer(player_name);
        ServerInfo villaServer = ProxyServer.getInstance().getServerInfo(plugin.getConfig().getString("config.villa-server-name"));
        if(villaServer == null){
            plugin.getLogger().info(ChatColor.RED+"[UBCore] No se pudo inicializar el servidor de villa con nombre "+plugin.getConfig().getString("config.villa-server-name"));
            return;
        }
        player.connect(villaServer);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("BungeeMainCommandChannel");
        out.writeUTF(player_name);
        out.writeUTF(send);
        MainCommandSender mcs = new MainCommandSender();
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            public void run () {
                mcs.sendData(player, out, villaServer);
                plugin.getLogger().info(ChatColor.YELLOW+"[UBCore] Enviando al jugador "+ChatColor.RED+player_name+ChatColor.YELLOW+" al servidor de Villa para luego teletransportarlo a "+ChatColor.RED+send);
            }
        }, 750, TimeUnit.MILLISECONDS);
    }
}
