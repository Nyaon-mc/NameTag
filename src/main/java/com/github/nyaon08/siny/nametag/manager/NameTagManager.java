package com.github.nyaon08.siny.nametag.manager;

import com.github.nyaon08.siny.nametag.NameTag;
import com.github.nyaon08.siny.nametag.data.Tag;
import com.google.gson.JsonObject;
import kr.rtuserver.framework.bukkit.api.platform.JSON;
import kr.rtuserver.framework.bukkit.api.storage.Storage;
import lombok.RequiredArgsConstructor;

import java.util.*;
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
            if (result.isEmpty()) return List.of();

            List<Tag> tags = new ArrayList<>();
            for (JsonObject jsonObject : result) {
                JsonObject obj = jsonObject.getAsJsonObject();
                Tag tag = new Tag(
                        obj.get("name").getAsString(),
                        obj.get("condition").getAsString(),
                        obj.get("active").getAsBoolean()
                );
                tags.add(tag);
                cache.put(uuid, tag);
            }

            return tags;
        });
    }

    public void activateTag(UUID uuid, Tag tag) {
        Storage storage = plugin.getStorage();
        storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            for (JsonObject obj : result) {
                JsonObject json = obj.getAsJsonObject();
                if (json.get("active").getAsBoolean()) {
                    String tagName = json.get("name").getAsString();
                    storage.set("nametag", JSON.of("uuid", uuid.toString()).append("name", tagName), JSON.of("active", false)).join();
                }
            }
            return null;
        }).join();

        cache.put(uuid, tag);
        storage.set("nametag", JSON.of("uuid", uuid.toString()).append("name", tag.name()), JSON.of("active", true)).join();
    }

    public String activateTag(UUID uuid) {
        Storage storage = plugin.getStorage();

        Tag cached = cache.get(uuid);
        if (cached != null && cached.active()) {
            return cached.name();
        }

        return storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            for (JsonObject obj : result) {
                JsonObject json = obj.getAsJsonObject();
                if (json.has("active") && json.get("active").getAsBoolean()) {
                    return json.get("tag").getAsString();
                }
            }
            return "";
        }).join();
    }

    public void deactivateTag(UUID uuid) {
        Storage storage = plugin.getStorage();
        storage.get("nametag", JSON.of("uuid", uuid.toString())).thenApplyAsync(result -> {
            for (JsonObject obj : result) {
                JsonObject json = obj.getAsJsonObject();

                if (json.has("active") && json.get("active").getAsBoolean()) {
                    String tagName = json.get("name").getAsString();

                    storage.set("nametag",
                            JSON.of("uuid", uuid.toString()).append("name", tagName),
                            JSON.of("active", false)
                    ).join();
                }
            }
            return null;
        }).join();

        cache.remove(uuid);
    }

}
