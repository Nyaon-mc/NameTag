package com.github.nyaon08.siny.nametag.manager;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.data.Tag;
import com.google.gson.JsonObject;
import kr.rtuserver.framework.bukkit.api.platform.JSON;
import kr.rtuserver.framework.bukkit.api.storage.Storage;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class NameTagManager {

    private final NameTag plugin;

    private final Map<UUID, Tag> cache = new HashMap<>();

    public void add(UUID uuid, Tag tag) {
        Storage storage = plugin.getStorage();
        storage.add("nametag", JSON.of("uuid", uuid.toString())
                .append("name", tag.name())
                .append("condition", tag.condition())
                .append("active", tag.active())
        ).join();
    }

    public CompletableFuture<List<Tag>> get(UUID uuid) {
        Storage storage = plugin.getStorage();
        return storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            if (result.isEmpty() || result.getFirst().isJsonNull()) return List.of();

            JsonObject obj = result.getFirst().getAsJsonObject();
            return List.of(new Tag(obj.get("name").getAsString(), obj.get("condition").getAsString() ,obj.get("active").getAsBoolean()));
        });
    }

    public void activeTag(UUID uuid, Tag tag) {
        Storage storage = plugin.getStorage();
        storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            if (!result.isEmpty() && !result.getFirst().isJsonNull()) {
                JsonObject obj = result.getFirst().getAsJsonObject();
                if (obj.get("active").getAsBoolean()) {
                    storage.set("nametag", JSON.of("uuid", uuid.toString()), JSON.of("active", false)).join();
                }
            }
            return null;
        }).join();

        cache.put(uuid, tag);
        storage.set("nametag", JSON.of("uuid", uuid.toString()), JSON.of("active", true)).join();
    }

    public String activeTag(UUID uuid) {
        Storage storage = plugin.getStorage();
        if (cache.containsKey(uuid)) return cache.get(uuid).name();
        return storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            if (result.isEmpty() || result.getFirst().isJsonNull()) return "";
            JsonObject obj = result.getFirst().getAsJsonObject();
            boolean isActive = obj.get("active").getAsBoolean();
            if (isActive) return obj.get("tag").getAsString();
            return null;
        }).join();
    }

}
