package com.github.nyaon08.siny.nametag.dependency;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.manager.NameTagManager;
import kr.rtuserver.framework.bukkit.api.integration.RSPlaceholder;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPI extends RSPlaceholder<NameTag> {

    private final NameTagManager manager;

    public PlaceholderAPI(NameTag plugin) {
        super(plugin);
        this.manager = plugin.getNameTagManager();
    }

    @Override
    public String request(OfflinePlayer offlinePlayer, String[] params) {
        if (params[0].equals("tag")) {
            return manager.activeTag(offlinePlayer.getUniqueId());
        }
        return "ERROR";
    }

}
