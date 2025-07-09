package com.github.nyaon08.siny.nametag.configuration;

import com.github.nyaon08.siny.nametag.NameTag;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import lombok.Getter;

@Getter
public class NameTagConfig extends RSConfiguration<NameTag> {

    private String nameTagItem = "minecraft:paper";
    private String nameTagInventoryName = "칭호북";

    public NameTagConfig(NameTag plugin) {
        super(plugin, "nametag.yml", 1);
        setup(this);
    }

    private void init() {
        nameTagItem = getString("nameTagItem", nameTagItem, """
                칭호북으로 설정할 아이템
                ex) namespace:id""");

        nameTagInventoryName = getString("nameTagInventoryName", nameTagInventoryName, """
                칭호북 인벤터리의 이름""");
    }

}
