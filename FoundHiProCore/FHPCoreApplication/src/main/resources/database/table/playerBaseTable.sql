CREATE TABLE IF NOT EXISTS `player_base` (
  `id`                  varchar(255)  NOT NULL
  COMMENT '玩家ID',
  `permission`          int(1)        NULL DEFAULT 0
  COMMENT '权限',
  `money1`              double(10, 2) NULL DEFAULT 0.0
  COMMENT '金币',
  `money2`              double(10, 2) NULL DEFAULT 0.0
  COMMENT '绑定点券',
  `money3`              double(10, 2) NULL DEFAULT 0.0
  COMMENT '充值点券',
  `preference_settings` TEXT          NULL
  COMMENT '偏好设置',
  PRIMARY KEY (`id`)
);