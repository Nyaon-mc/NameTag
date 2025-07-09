package com.github.nyaon08.siny.nametag.command;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.configuration.IconConfig;
import com.github.nyaon08.siny.nametag.configuration.NameTagConfig;
import com.github.nyaon08.siny.nametag.inventory.NameTagInventory;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import kr.rtuserver.framework.bukkit.api.configuration.translation.TranslationType;
import kr.rtuserver.framework.bukkit.api.configuration.translation.message.MessageTranslation;

import java.util.List;

public class MainCommand extends RSCommand<NameTag> {

    private final IconConfig iconConfig;
    private final NameTagConfig nameTagConfig;

    public MainCommand(NameTag plugin) {
        super(plugin, "nametag");
        this.iconConfig = plugin.getIconConfig();
        this.nameTagConfig = plugin.getNameTagConfig();
    }

    @Override
    protected boolean execute(RSCommandData data) {
        if (player() == null) {
            chat().announce(message().get(MessageTranslation.Common.ONLY_PLAYER.getKey()));
            return false;
        };
        player().openInventory(new NameTagInventory(getPlugin(), player()).getInventory());
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        return List.of();
    }

    @Override
    protected void reload(RSCommandData data) {
        iconConfig.reload();
        nameTagConfig.reload();
    }

}
