package net.mcpes.foundhi.plugin.game.bedwars.gui;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.item.Item;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.DEFAULT_TITLE;
import static net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase.*;

@SuppressWarnings("unused")
public class GUIShopThirdLevel {
    private final int id;
    private final String iid;
    private int silver;
    private int gold;
    private int diamond;
    private int emerald;
    private String dn;
    private ElementSlider slider;
    private ElementLabel label;

    public GUIShopThirdLevel(int id, String iid, int silver, int gold, int diamond, int emerald, String dn) {
        this.id = id;
        this.iid = iid;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.emerald = emerald;
        this.dn = dn;
        this.label = new ElementLabel((this.dn = this.dn.replace("{s}", String.valueOf(silver)).replace("{g}", String.valueOf(gold)).replace("{d}", String.valueOf(diamond)).replace("{e}", String.valueOf(emerald))));
        this.slider = new ElementSlider("数量", 1, 64, 1, 1);
    }

    public static int getResourceCount(Player player, int type) {
        int a = 0;
        switch (type) {
            case 2:
                for (Item item : player.getInventory().slots.values()) {
                    if (Objects.equals(item.getCustomName(), SBedWars.silver.getCustomName())) {
                        a += item.getCount();
                    }
                }
                return a;
            case 3:
                for (Item item : player.getInventory().slots.values()) {
                    if (Objects.equals(item.getCustomName(), SBedWars.gold.getCustomName())) {
                        a += item.getCount();
                    }
                }
                return a;
            case 4:
                for (Item item : player.getInventory().slots.values()) {
                    if (Objects.equals(item.getCustomName(), SBedWars.diamond.getCustomName())) {
                        a += item.getCount();
                    }
                }
                return a;
            case 5:
                for (Item item : player.getInventory().slots.values()) {
                    if (Objects.equals(item.getCustomName(), SBedWars.emerald.getCustomName())) {
                        a += item.getCount();
                    }
                }
                return a;
        }
        return -1;
    }

    private static boolean hasEnoughResources(Player player, int silver, int gold, int diamond, int emerald) {
        return getResourceCount(player, 2) >= silver && getResourceCount(player, 3) >= gold && getResourceCount(player, 4) >= diamond && getResourceCount(player, 5) >= emerald;
    }

    public int getId() {
        return id;
    }

    public String getIid() {
        return iid;
    }

    public int getSilver() {
        return silver;
    }

    public int getGold() {
        return gold;
    }

    public int getDiamond() {
        return diamond;
    }

    public int getEmerald() {
        return emerald;
    }

    public String getDn() {
        return dn;
    }

    public void openGUI(Player player) {
        FormWindowCustom gui = new FormWindowCustom(DEFAULT_TITLE + " §e游戏商店");
        gui.addElement(label);
        gui.addElement(slider);
        player.showFormWindow(gui);
    }

    public void handle(Player player, FormResponseCustom response) {
        String name = player.getName();
        if ((!see.contains(name)) || response == null) {
            typeThree.remove(name);
            inLevel.remove(name);
            inType.remove(name);
            see.remove(name);
            return;
        }
        typeThree.remove(name);
        inLevel.remove(name);
        inType.remove(name);
        see.remove(name);
        int count = (int) response.getSliderResponse(1);
        int ns = silver * count;
        int ng = gold * count;
        int nd = diamond * count;
        int ne = emerald * count;
        if (hasEnoughResources(player, ns, ng, nd, ne)) {
            this.removeItem(player, Item.get(266, 0, ng));
            this.removeItem(player, Item.get(265, 0, ns));
            this.removeItem(player, Item.get(264, 0, nd));
            this.removeItem(player, Item.get(Item.EMERALD, 0, ne));
            for (int i = 0; i < count; i++) {
                player.getInventory().addItem((Item) GUIShopBase.itemInfo.get(iid).get("item"));
            }
            player.sendMessage(DEFAULT_TITLE + "§3成功兑换!");
        } else {
            player.sendMessage(DEFAULT_TITLE + "§c你的资源不足!");
        }
    }

    public void removeItem(Player player, Item... slots) {
        List<Item> itemSlots = new ArrayList<>();
        for (Item slot : slots) {
            if (slot.getId() != 0 && slot.getCount() > 0) {
                itemSlots.add(slot.clone());
            }
        }
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            Item item = player.getInventory().getItem(i);
            if (item.getId() == Item.AIR || item.getCount() <= 0) {
                continue;
            }
            for (Item slot : new ArrayList<>(itemSlots)) {
                if (slot.equals(item, false, false)) {
                    int amount = Math.min(item.getCount(), slot.getCount());
                    slot.setCount(slot.getCount() - amount);
                    item.setCount(item.getCount() - amount);
                    player.getInventory().setItem(i, item);
                    if (slot.getCount() <= 0) {
                        itemSlots.remove(slot);
                    }
                }
            }
            if (itemSlots.size() == 0) {
                break;
            }
        }
    }
}
