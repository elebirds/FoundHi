package net.mcpes.foundhi.plugin.currency.shop.shop;

import net.mcpes.foundhi.core.plugin.data.NukkitPlayer;
import net.sf.json.JSONObject;

public interface Shop {

    int getType();

    void load(JSONObject data);

    void save();

    void onTouch(NukkitPlayer player);

    boolean open(NukkitPlayer player);

    boolean handle(NukkitPlayer player, int count, boolean gui);

    JSONObject getJSONData();
}
