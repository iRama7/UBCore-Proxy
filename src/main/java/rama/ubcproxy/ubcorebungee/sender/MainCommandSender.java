package rama.ubcproxy.ubcorebungee.sender;

import com.google.common.io.ByteArrayDataOutput;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MainCommandSender {


    public void sendData(ProxiedPlayer player, ByteArrayDataOutput out, ServerInfo server){
        player.sendData("ub:channel", out.toByteArray());
        server.sendData("ub:channel", out.toByteArray());
    }
}
