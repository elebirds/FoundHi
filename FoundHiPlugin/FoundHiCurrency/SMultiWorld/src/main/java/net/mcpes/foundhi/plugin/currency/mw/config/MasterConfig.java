package net.mcpes.foundhi.plugin.currency.mw.config;

import cn.nukkit.utils.SimpleConfig;
import net.mcpes.foundhi.plugin.currency.mw.SMultiWorld;

/**
 * @author hhm
 * @date 2017/7/8
 * @since SMultiWorld
 */

public class MasterConfig extends SimpleConfig {
    @Path(value = "use-moneyCore")
    public String moneyCore = "money";

    public MasterConfig() {
        super(SMultiWorld.getInstance(), "config.yml");
    }
}
