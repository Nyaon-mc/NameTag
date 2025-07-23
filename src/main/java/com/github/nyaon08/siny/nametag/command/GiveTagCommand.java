package com.github.nyaon08.siny.nametag.command;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.data.Tag;
import com.github.nyaon08.siny.nametag.manager.NameTagManager;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class GiveTagCommand extends RSCommand<NameTag> {

    private final NameTagManager nameTagManager;

    public GiveTagCommand(NameTag plugin) {
        super(plugin, "givetag", PermissionDefault.OP);
        this.nameTagManager = plugin.getNameTagManager();
    }

    @Override
    protected boolean execute(RSCommandData data) {
        Player target = provider().getPlayer(data.args(1));
        if (target == null) {
            return true;
        }

        String tag = data.args(2);
        String condition = String.join(" ", Arrays.copyOfRange(data.args(), 3, data.args().length));

        nameTagManager.add(target.getUniqueId(), new Tag(tag, condition, false));
        chat().announce(message().get(player(), "give_tag")
                .replace("[player]", target.getName())
                .replace("[tag]", tag));
        return true;
    }

    @Override
    protected List<String> tabComplete(RSCommandData data) {
        if (data.length(2)) return provider().getNames();
        if (data.length(3)) return List.of("<칭호>");
        if (data.length(4)) return List.of("<사유>");
        return List.of();
    }

}
