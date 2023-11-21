package net.mcpes.foundhi.plugin.game.bedwars.gui;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;

import java.util.ArrayList;
import java.util.HashMap;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.DEFAULT_TITLE;

public class GUIShopBase {
    public static HashMap<String, Integer> typeThree = new HashMap<>();
    public static HashMap<String, Integer> inType = new HashMap<>();
    public static HashMap<String, Integer> inLevel = new HashMap<>();
    public static ArrayList<String> see = new ArrayList<>();
    public static HashMap<String, HashMap<String, Object>> itemInfo = new HashMap<>();
    public static HashMap<Integer, GUIShopSecondLevel> second = new HashMap<>();
    private static ArrayList<ElementButton> mainButtons = new ArrayList<>();

    public static void init() {
        mainButtons.add(new ElementButton("武器"));
        mainButtons.add(new ElementButton("防具"));
        mainButtons.add(new ElementButton("食物"));
        mainButtons.add(new ElementButton("药水"));
        mainButtons.add(new ElementButton("方块"));
        mainButtons.add(new ElementButton("工具"));
        mainButtons.add(new ElementButton("道具"));
        mainButtons.add(new ElementButton("其他"));
        load();
    }

    private static void load() {
        Config config = new Config(SBedWars.getInstance().getDataFolder() + "/shop.yml");
        second.put(1, new GUIShopSecondLevel(1, config.get("Attack", new HashMap<>())));
        second.put(2, new GUIShopSecondLevel(2, config.get("Defense", new HashMap<>())));
        second.put(3, new GUIShopSecondLevel(3, config.get("Food", new HashMap<>())));
        second.put(4, new GUIShopSecondLevel(4, config.get("Effect", new HashMap<>())));
        second.put(5, new GUIShopSecondLevel(5, config.get("Block", new HashMap<>())));
        second.put(6, new GUIShopSecondLevel(6, config.get("Tool", new HashMap<>())));
        second.put(7, new GUIShopSecondLevel(7, config.get("Prop", new HashMap<>())));
        second.put(8, new GUIShopSecondLevel(8, config.get("Other", new HashMap<>())));
    }

    public static void openMain(Player player) {
        String name = player.getName();
        if (see.contains(name)) {
            see.remove(name);
            return;
        }
        see.add(name);
        inLevel.put(name, 1);
        player.showFormWindow(new FormWindowSimple(DEFAULT_TITLE + " §e游戏商店", "你拥有: §f银x" + GUIShopThirdLevel.getResourceCount(player, 2) + " §e金x" + GUIShopThirdLevel.getResourceCount(player, 3) + " §b钻石x" + GUIShopThirdLevel.getResourceCount(player, 4) + " §2绿宝石x" + GUIShopThirdLevel.getResourceCount(player, 5), mainButtons));
    }

    public static void handle(Player player, FormResponseSimple response) {
        String name = player.getName();
        if (!see.contains(name)) {
            return;
        }
        inLevel.put(name, 2);
        inType.put(name, response.getClickedButtonId() + 1);
        second.get(response.getClickedButtonId() + 1).openGUI(player);
    }
}
