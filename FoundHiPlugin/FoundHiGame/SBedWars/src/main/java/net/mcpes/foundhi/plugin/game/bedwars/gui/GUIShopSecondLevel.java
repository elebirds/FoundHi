package net.mcpes.foundhi.plugin.game.bedwars.gui;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import net.mcpes.foundhi.plugin.game.bedwars.utils.ItemName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.DEFAULT_TITLE;
import static net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase.*;

@SuppressWarnings("unchecked")
public class GUIShopSecondLevel {
    private final int id;
    private HashMap<Integer, GUIShopThirdLevel> third;
    private ArrayList<ElementButton> mainButtons = new ArrayList<>();

    GUIShopSecondLevel(int id, HashMap<String, Object> data) {
        this.id = id;
        this.third = new HashMap<>();
        for (String key : data.keySet()) {
            this.setItem(key, (HashMap<String, Object>) data.get(key));
        }
        this.setGUI();
    }

    public int getId() {
        return id;
    }

    public HashMap<Integer, GUIShopThirdLevel> getThird() {
        return third;
    }

    private void setItem(String key, HashMap<String, Object> data) {
        HashMap<String, Object> info = new HashMap<>();
        Map<String, Object> need = (Map<String, Object>) data.get("Need");
        info.put("silver", Integer.valueOf(need.get("Silver").toString()));
        info.put("gold", Integer.valueOf(need.get("Gold").toString()));
        info.put("diamond", Integer.valueOf(need.get("Diamond").toString()));
        info.put("emerald", Integer.valueOf(need.get("Emerald").toString()));
        info.put("dn", data.get("DisplayName"));
        info.put("in", data.get("Name"));
        info.put("ii", data.get("Item"));
        info.put("co", data.get("Count"));
        info.put("en", data.get("Enchant"));
        info.put("ne", need);
        String[] ids = data.get("Item").toString().split(":");
        Item item = Item.get(Integer.valueOf(ids[0]), Integer.valueOf(ids[1]), Integer.valueOf(data.get("Count").toString()));
        if (!data.get("Name").equals("default")) {
            item.setCustomName(DEFAULT_TITLE + data.get("Name").toString());
        } else {
            item.setCustomName(DEFAULT_TITLE + ItemName.getItemName(item.getId(), item.getDamage()));
        }
        ArrayList<String> enchant = (ArrayList<String>) data.get("Enchant");
        if (!enchant.isEmpty()) {
            for (String mlgb : enchant) {
                String[] es = mlgb.split(":");
                Enchantment enchantment = Enchantment.get(Integer.valueOf(es[0]));
                enchantment.setLevel(Integer.valueOf(es[1]));
                item.addEnchantment(enchantment);
            }
        }
        item.setCustomBlockData(new CompoundTag().putString("bid", key));
        info.put("item", item);
        GUIShopBase.itemInfo.put(key, info);
        third.put(third.size(), new GUIShopThirdLevel(third.size(),
                key,
                (int) info.get("silver"),
                (int) info.get("gold"),
                (int) info.get("diamond"),
                (int) info.get("emerald"),
                info.get("dn").toString()));
    }

    private void setGUI() {
        for (GUIShopThirdLevel gt : third.values()) {
            mainButtons.add(new ElementButton(gt.getDn()));
        }
    }

    public void openGUI(Player player) {
        player.showFormWindow(new FormWindowSimple(DEFAULT_TITLE + " §e游戏商店", "你拥有: §f银x" + GUIShopThirdLevel.getResourceCount(player, 2) + " §e金x" + GUIShopThirdLevel.getResourceCount(player, 3) + " §b钻石x" + GUIShopThirdLevel.getResourceCount(player, 4) + " §2绿宝石x" + GUIShopThirdLevel.getResourceCount(player, 5), mainButtons));
    }

    public void handle(Player player, FormResponseSimple response) {
        String name = player.getName();
        if ((!see.contains(name)) || response == null) {
            typeThree.remove(name);
            inLevel.remove(name);
            inType.remove(name);
            see.remove(name);
            return;
        }
        inLevel.put(name, 3);
        typeThree.put(name, response.getClickedButtonId());
        third.get(response.getClickedButtonId()).openGUI(player);
    }
}
