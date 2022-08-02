package rama.ubcproxy.ubcorebungee.authhook;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import rama.ubcproxy.ubcorebungee.UBCoreBungee;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ServerHistory implements Listener{

    private UBCoreBungee plugin;

    public ServerHistory(UBCoreBungee plugin){
        this.plugin = plugin;
    }

    public HashMap<ProxiedPlayer, Boolean> newJoinPlayers = new HashMap<>();

    @EventHandler
    public void getIfNew(ServerConnectEvent e){
        ServerConnectEvent.Reason reason = e.getReason();
            if (reason.toString().equalsIgnoreCase("JOIN_PROXY")) {
                newJoinPlayers.put(e.getPlayer(), true);
            }else{
                newJoinPlayers.put(e.getPlayer(), false);
            }
    }

    @EventHandler
    public void writeConnectedServer(ServerConnectedEvent e) throws IOException{
        String server_name = e.getServer().getInfo().getName();
        ProxiedPlayer player = e.getPlayer();
        Configuration database = plugin.getDatabase();
        UUID uuid = player.getUniqueId();
        String villa_server_name = plugin.getConfig().getString("config.villa-server-name");
        String minas_server_name = plugin.getConfig().getString("config.minas-server-name");
        if(newJoinPlayers.containsKey(player) && !newJoinPlayers.get(player)){
            if(server_name.equals(villa_server_name)){
                database.set(uuid+".villa", true);
                database.set(uuid+".minas", false);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(database, new File(plugin.getDataFolder(), "database.yml"));
            }else if(server_name.equals(minas_server_name)){
                database.set(uuid+".villa", false);
                database.set(uuid+".minas", true);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(database, new File(plugin.getDataFolder(), "database.yml"));
            }
        }
    }
    public String getLastServer(String uuid) throws IOException {
        Configuration database = plugin.getDatabase();
        if(database.getBoolean(uuid+".minas")){
            return "minas";
        }else{
            return "villa";
        }
    }
}
