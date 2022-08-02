package rama.ubcproxy.ubcorebungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import rama.ubcproxy.ubcorebungee.authhook.ServerHistory;
import rama.ubcproxy.ubcorebungee.reader.PMListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static net.md_5.bungee.api.ProxyServer.getInstance;

public final class UBCoreBungee extends Plugin {


    public UBCoreBungee() throws IOException {
    }

    @Override
    public void onEnable() {
        registerConfig();
        registerDatabase();
        try {
            registerEvents();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getProxy().registerChannel("ub:channel");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Configuration getConfig() throws IOException {
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        return configuration;
    }

    public void registerEvents() throws IOException {
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(UBCore) &6Registrando eventos..."));
        getInstance().getPluginManager().registerListener(this, new PMListener(this));
        if(this.getConfig().getBoolean("config.enable-login_event")){
            getInstance().getPluginManager().registerListener(this, new ServerHistory(this));
        }
        getInstance().getPluginManager().registerListener(this, new PlayerJoinMessage(this));
    }

    public void registerConfig(){
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(UBCore) &6Registrando configuración..."));
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Configuration getDatabase() throws IOException {
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "database.yml"));
        return configuration;
    }
    public void registerDatabase(){
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(UBCore) &6Registrando database..."));
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        File file = new File(getDataFolder(), "database.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("database.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
