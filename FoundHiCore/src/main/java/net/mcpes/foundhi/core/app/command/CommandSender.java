package net.mcpes.foundhi.core.app.command;

public interface CommandSender {
    /**
     * 给命令发送者返回信息。<br>
     * Sends a message to the command sender.
     *
     * @param message 要发送的信息。<br>Message to send.
     * @see cn.nukkit.utils.TextFormat
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    void sendMessage(String message);

    /**
     * 返回命令发送者的名称。<br>
     * Returns the name of the command sender.
     * <p>
     * <p>如果命令发送者是一个玩家，将会返回他的玩家名字(name)不是显示名字(display name)。
     * 如果命令发送者是控制台，将会返回{@code "CONSOLE"}。<br>
     * If this command sender is a player, will return his/her player name(not display name).
     * If it is a console, will return {@code "CONSOLE"}.</p>
     * <p>当你需要判断命令的执行者是不是控制台时，可以用这个：<br>
     * When you need to determine if the sender is a console, use this:<br>
     * {@code if(sender instanceof ConsoleCommandSender) .....;}</p>
     *
     * @return 命令发送者的名称。<br>the name of the command sender.
     * @see cn.nukkit.Player#getName()
     * @see cn.nukkit.command.ConsoleCommandSender#getName()
     * @see cn.nukkit.plugin.PluginDescription
     * @since Nukkit 1.0 | Nukkit API 1.0.0
     */
    String getName();

    boolean isPlayer();

    boolean isOp();

    void setOp(boolean value);
}
