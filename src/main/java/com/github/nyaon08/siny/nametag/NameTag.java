package com.github.nyaon08.siny.nametag;

import com.github.nyaon08.siny.nametag.command.MainCommand;
import com.github.nyaon08.siny.nametag.configuration.IconConfig;
import com.github.nyaon08.siny.nametag.configuration.NameTagConfig;
import com.github.nyaon08.siny.nametag.dependency.PlaceholderAPI;
import com.github.nyaon08.siny.nametag.manager.NameTagManager;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import lombok.Getter;

public class NameTag extends RSPlugin {

    @Getter
    private static NameTag instance;

    @Getter
    private IconConfig iconConfig;

    @Getter
    private NameTagConfig nameTagConfig;

    @Getter
    private NameTagManager nameTagManager;

    private PlaceholderAPI placeholder;

    @Override
    public void load() {
        instance = this;
    }

    @Override
    public void enable() {
        getConfigurations().getStorage().init("nametag");

        iconConfig = new IconConfig(this);
        nameTagConfig = new NameTagConfig(this);
        nameTagManager = new NameTagManager(this);

        registerCommand(new MainCommand(this), true);

        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder = new PlaceholderAPI(this);
            placeholder.register();
        }
    }

    @Override
    public void disable() {
        instance = null;
        if (getFramework().isEnabledDependency("PlaceholderAPI")) {
            placeholder.unregister();
        }
    }

}
