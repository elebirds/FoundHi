package net.mcpes.foundhi.core.handle;

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/7/7
 * version 1.0
 */
public interface HandleResults {
    /* 通用result代码 */

    int NORMAL_SUCCESS = 0;

    int PLAYER_OFFLINE = -1;

    int THE_OTHER_OFFLINE = -2;

    int PARAMETER_ERROR = -3;

    int NOT_ENOUGH_MONEY = -4;

    int UNKNOW_ERROR = -5;

    int MONEY_TYPE_ERROR = -100;

    /*Special Access*/

    int MARRY_SUCCESS = 1;

    int SCCESS_SEND_APPLY = 2;

    /* Marry 分配 -6 ~ -11*/

    int ALREADY_HAVE_LOVER = -6;

    int NOT_HAVE_LOVER = -7;

    int THE_OTHER_HAVE_LOVER = -8;

    int NO_APPLICATION = -9;

    int ALREADY_APPLICATION = -10;

    int THE_OTHER_NO_APPLICATION = -11;

    /* Guild  分配 -12 ~ -35*/
    int NOT_EXIST_GUILD = -12;

    int NOT_JOIN_GUILD = -13;

    int THE_OTHER_ALREADY_JOIN_GUILD = -14;

    int ALREADY_JOIN_GUILD = -15;

    int GUILD_ALREADY_FULL = -16;

    int GUILD_NAME_EXIST = -17;

    int NOT_LEADER = -18;

    int NO_PERMISSION = -19;

    int ALREADY_INVITE = -20;

    int NO_INVITATION = -21;

    int ALREADY_IS_MEMBER = -22;

    int NOT_MEMBER = -23;

    int NOT_EXIST_POSITION = -24;

    int TOO_MUCH_ADMIN = -25;

    int ALREADY_IS_THIS_POSITION = -26;

    /* RPG  分配 -36 ~ -?*/

    int ALREADY_BUILD_MAX_LEVEL = -36;

    //Team 分配 -51 ~ -60
    int HAS_JOINED_TEAM = -51;

    int NOT_JOIN_TEAM = -52;

    int TEAM_NOT_LEADER = -53;
}