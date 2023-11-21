package net.mcpes.foundhi.plugin.currency.shop.handle;
/*
import cn.nukkit.Player;
import cn.nukkit.Server;
import net.mcpes.foundhi.core.handle.Handle;
import net.mcpes.foundhi.plugin.currency.shop.shop.system.RPGShop;

public class RPGShopHandle extends Handle {
    private String target;
    private int count;
    private RPGShop shop;

    public RPGShopHandle(String target, int count, RPGShop shop) {
        this.target = target;
        this.count = count;
        this.shop = shop;
    }

    @Override
    public void handle() {
        Player target = Server.getInstance().getPlayerExact(this.target);
        switch (this.result) {
            case 0:
                if (target != null && target.isOnline()) {
                    //TODO rpg商店
                }
                break;
            case -1:
                if (target != null && target.isOnline()) target.sendMessage("§a§l操作失败:出现未知错误,请重新进入服务器再次尝试!");
                break;
            case -2:
                if (target != null && target.isOnline()) target.sendMessage("§a§l操作失败:未知参数错误!");
                break;
        }
    }
}
*/