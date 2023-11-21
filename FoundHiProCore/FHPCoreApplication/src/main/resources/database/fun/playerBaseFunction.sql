CREATE PROCEDURE fhPlayerCreate(IN pn VARCHAR(255), OUT num INT)
  BEGIN
    IF (SELECT COUNT(*) FROM `player_base` WHERE `id` = pn) = 0
    THEN
      INSERT INTO `player_base` (`id`,`permission`,`preference_settings`) VALUES (pn,0,'default');
      SET num = 1;
    ELSE
      SET num = 2;
    END IF;
  END;

CREATE PROCEDURE fhPlayerRemove(IN pn VARCHAR(255), OUT num INT)
  BEGIN
    IF (SELECT COUNT(*) FROM `player_base` WHERE `id` = pn) != 0
    THEN
      DELETE FROM `player_base` WHERE `id` = pn;
      SET num = 1;
    ELSE
      SET num = 2;
    END IF;
  END;