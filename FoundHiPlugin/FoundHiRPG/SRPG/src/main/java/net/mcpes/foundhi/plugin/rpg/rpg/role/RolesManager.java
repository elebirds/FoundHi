package net.mcpes.foundhi.plugin.rpg.rpg.role;

import java.util.List;
import java.util.Map;

/**
 * @author yui
 * @date 2018/1/19
 * @since Yui-RPG
 */
@SuppressWarnings("unchecked")
public class RolesManager {
    private String id;
    private String roleName;//职业名称
    private int maxHealth;//初始血量上限
    private int maxBlue;//初始蓝上限
    private List<String> commands;

    public RolesManager(String rID, Map<String, Object> data) {
        this.id = rID;
        this.roleName = (String) data.get("职业名称");
        this.maxHealth = (int) data.get("血量上限");
        this.maxBlue = (int) data.get("魔法上限");
        this.commands = (List<String>) data.get("指令");
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public String getId() {
        return id;
    }

    public int getMaxBlue() {
        return maxBlue;
    }

    public String getRoleName() {
        return roleName;
    }

    public List<String> getCommands() {
        return commands;
    }
}
