package com.github.nyaon08.siny.nametag.inventory;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.configuration.IconConfig;
import com.github.nyaon08.siny.nametag.data.Tag;
import com.github.nyaon08.siny.nametag.manager.NameTagManager;
import kr.rtuserver.framework.bukkit.api.format.ComponentFormatter;
import kr.rtuserver.framework.bukkit.api.inventory.RSInventory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.apache.commons.collections4.ListUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class NameTagInventory extends RSInventory<NameTag> {

    private final IconConfig iconConfig;
    private final NameTagManager manager;

    @Getter
    private final Inventory inventory;
    private final Player player;

    private final List<Tag> data = new ArrayList<>();

    private int page = 0;
    private int maxPage = 0;

    public NameTagInventory(NameTag plugin, Player player) {
        super(plugin);
        this.iconConfig = plugin.getIconConfig();
        this.manager = plugin.getNameTagManager();
        this.player = player;

        Component title = ComponentFormatter.mini(plugin.getNameTagConfig().getNameTagInventoryName());
        this.inventory = createInventory(54, title);

        data.addAll(manager.get(player.getUniqueId()).join());
        this.maxPage = Math.max(partition().size() - 1, 0);
        loadPage(0);
    }

    private List<List<Tag>> partition() {
        if (data.isEmpty()) return new ArrayList<>();
        return ListUtils.partition(data, 45);
    }

    private List<Tag> page(int page) {
        List<List<Tag>> partition = partition();
        if (partition.isEmpty()) return new ArrayList<>();
        else return partition.get(page);
    }

    protected void loadPage(int page) {
        this.page = page;
        inventory.clear();
        inventory.setItem(45, pageIcon(Navigation.FIRST));
        inventory.setItem(46, pageIcon(Navigation.PREVIOUS));
        inventory.setItem(52, pageIcon(Navigation.NEXT));
        inventory.setItem(53, pageIcon(Navigation.LAST));
        for (Tag tag : page(page)) inventory.addItem(item(tag));
    }

    private ItemStack pageIcon(Navigation navigation) {
        String name = navigation.name().toLowerCase();
        String available = navigation.check(page, maxPage) ? "available" : "unavailable";
        String display = message().get(player, "icon.menu.pagination." + name + "." + available);
        display = display.replace("[current]", String.valueOf(page + 1)).replace("[max]", String.valueOf(maxPage + 1));
        return iconConfig.get("menu.pagination." + name + "." + available, display);
    }

    private ItemStack item(Tag tag) {
        return null;
    }

    private List<Component> toComponents(String message) {
        List<Component> result = new ArrayList<>();
        String[] split = message.split("\n");
        for (String str : split) result.add(ComponentFormatter.mini("<!italic><white>" + str));
        return result;
    }

    private void init() {
    }

    @Override
    public boolean onClick(Event<InventoryClickEvent> event, RSInventory.Click click) {
        return false;
    }

    @Override
    public boolean onDrag(Event<InventoryDragEvent> event, Drag drag) {
        return true;
    }

    @RequiredArgsConstructor
    protected enum Navigation {
        FIRST((page, maxPage) -> page != 0),
        PREVIOUS((page, maxPage) -> page > 0),
        NEXT((page, maxPage) -> page < maxPage),
        LAST((page, maxPage) -> !Objects.equals(page, maxPage));

        private final BiPredicate<Integer, Integer> condition;

        public boolean check(int page, int maxPage) {
            return condition.test(page, maxPage);
        }
    }

}
