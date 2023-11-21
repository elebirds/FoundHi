package net.mcpes.foundhi.core.dao;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import net.mcpes.foundhi.core.ServerType;
import net.mcpes.foundhi.core.app.FoundHiAPIMain;
import net.mcpes.foundhi.core.dao.task.PrepareStatementDatabaseTask2;
import net.mcpes.foundhi.core.plugin.FoundHiPlugin;
import net.mcpes.foundhi.core.utils.FileUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    private static DruidDataSource source;
    private static JedisPool jedisPool;
    private static String redisPassword;
    private static boolean redisUsePw;
    private static boolean enableRedis;

    public static void init() throws Exception {
        if (ServerType.getType() == ServerType.API) {
            source = (DruidDataSource) DruidDataSourceFactory.createDataSource(FileUtils.readProperties(new File(FoundHiAPIMain.INSTANCE.getDATA_PATH() + "/database.properties")));
            JedisPoolConfig config = new JedisPoolConfig();
            Config redisConfig = new Config(new File(FoundHiAPIMain.INSTANCE.getDATA_PATH() + "/redis.yml"));
            if ((enableRedis = redisConfig.getBoolean("enable"))) {
                if (redisUsePw = (boolean) redisConfig.get("usePW")) {
                    redisPassword = redisConfig.get("password").toString();
                }
                jedisPool = new JedisPool(config, redisConfig.get("ip").toString(), Integer.parseInt(redisConfig.get("port").toString()));
                Jedis jedis = jedisPool.getResource();
                jedis.set("foundhi", FoundHiAPIMain.VERSION);
                jedis.close();
            }
        } else {
            source = (DruidDataSource) DruidDataSourceFactory.createDataSource(FileUtils.readProperties(new File(Nukkit.DATA_PATH + "/database.properties")));
            JedisPoolConfig config = new JedisPoolConfig();
            Config redisConfig = new Config(new File(Nukkit.DATA_PATH + "/redis.yml"));
            if ((enableRedis = redisConfig.getBoolean("enable"))) {
                if (redisUsePw = (boolean) redisConfig.get("usePW")) {
                    redisPassword = redisConfig.get("password").toString();
                }
                jedisPool = new JedisPool(config, redisConfig.get("ip").toString(), Integer.parseInt(redisConfig.get("port").toString()));
                Jedis jedis = jedisPool.getResource();
                jedis.sadd("connected_servers", FoundHiPlugin.getInstance().getId());
                jedis.close();
            }
        }
    }

    public static DruidDataSource getDruidDataSource() {
        return source;
    }

    public static DruidPooledConnection getMySQLConnection() throws SQLException {
        return source.getConnection();
    }

    public static boolean isEnableRedis() {
        return enableRedis;
    }

    public static boolean isRedisUsePassword() {
        return redisUsePw;
    }

    public static Jedis getRedisConnection() {
        if (!isEnableRedis()) return null;
        Jedis con = jedisPool.getResource();
        if (redisUsePw) {
            con.auth(redisPassword);
        }
        return con;
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    public static void prepareStatement(String sql, boolean api) {
        if (api) {
            DatabaseQueue.instance.addTask(() -> {
                try {
                    Connection con = Database.getMySQLConnection();
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.executeUpdate();
                    ps.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }, new ArrayList<>());
        } else
            Server.getInstance().getScheduler().scheduleAsyncTask(FoundHiPlugin.getInstance(), new PrepareStatementDatabaseTask2(sql));
    }

    public static void hashUptate(String key, String key2, String value) {
        if (!Database.isEnableRedis()) return;
        Jedis jedis = getRedisConnection();
        assert jedis != null;
        jedis.hset(key, key2, value);
        jedis.close();
    }

    public static void newTable(String sql) {
        try {
            DruidPooledConnection con = getMySQLConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
