package com.github.nyaon08.siny.nametag;

import com.github.nyaon08.siny.nametag.data.Tag;
import org.bukkit.OfflinePlayer;

/**
 * 플러그인의 NameTag 시스템과 상호작용할 수 있는 메서드를 제공하는 클래스입니다.
 */
public final class NameTagAPI {

    private static NameTag plugin;

    private NameTagAPI() {
        throw new UnsupportedOperationException("NameTagAPI is not instantiable");
    }

    /**
     * 플러그인 인스턴스를 반환합니다.
     * @return 플러그인 인스턴스
     */
    private static NameTag plugin() {
        if (plugin == null) plugin = NameTag.getInstance();
        return plugin;
    }

    /**
     * 특정 플레이어의 활성화된 태그를 가져옵니다.
     * @param player 오프라인 플레이어
     * @return 활성화된 태그 이름
     */
    public static String getActiveTag(OfflinePlayer player) {
        if (player == null) return null;
        return plugin().getNameTagManager().activeTag(player.getUniqueId());
    }

    /**
     * 지정된 플레이어에게 새 태그를 추가합니다.
     * @param player 오프라인 플레이어
     * @param tag 추가할 태그
     */
    public static void addTag(OfflinePlayer player, Tag tag) {
        if (player == null || tag == null) return;
        plugin().getNameTagManager().add(player.getUniqueId(), tag);
    }
}
