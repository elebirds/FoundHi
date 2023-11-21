package net.mcpes.foundhi.core.app.task;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.pool.DruidPooledResultSet;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.dao.Database;
import net.mcpes.foundhi.core.network.protocol.server.SendTopPacket;
import net.mcpes.foundhi.core.scheduler.AsyncTask;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.*;

public class GetMoneyTopTask extends AsyncTask {
    public static HashMap<String, Double> sort(Map<String, Double> unsortMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>();
        list.addAll(unsortMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        HashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aList : list) {
            sortedMap.put((String) ((Map.Entry) aList).getKey(), (Double) ((Map.Entry) aList).getValue());
        }
        return sortedMap;
    }

    @Override
    public void onRun() {
        while (true) {
            try {
                DruidPooledConnection con = Database.getMySQLConnection();
                String sql;
                {
                    sql = "SELECT name,money1 FROM `player_base` LIMIT 0, 10000000000;";
                    DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) con.prepareStatement(sql);
                    DruidPooledResultSet rs = (DruidPooledResultSet) ps.executeQuery();
                    HashMap<String, Double> data = new HashMap<>();
                    int t = 0;
                    while (rs.next()) {
                        t++;
                        if (t > 100) break;
                        data.put(rs.getString(1), rs.getDouble(2));
                    }
                    rs.close();
                    ps.close();
                    data = sort(data);
                    SendTopPacket stp = new SendTopPacket();
                    stp.type = 1;
                    stp.data = JSONObject.fromObject(data).toString();
                    APIFunction.getInstance().setMoney1Top(data);
                    APIFunction.getInstance().setMoney1TopS(stp.data);
                    APIFunction.getInstance().broadcastPacket(stp);
                }
                {
                    sql = "SELECT name,money2 FROM `player_base` LIMIT 0, 10000000000;";
                    DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) con.prepareStatement(sql);
                    DruidPooledResultSet rs = (DruidPooledResultSet) ps.executeQuery();
                    HashMap<String, Double> data = new HashMap<>();
                    int t = 0;
                    while (rs.next()) {
                        t++;
                        if (t > 100) break;
                        data.put(rs.getString(1), rs.getDouble(2));
                    }
                    rs.close();
                    ps.close();
                    data = sort(data);
                    SendTopPacket stp = new SendTopPacket();
                    stp.type = 2;
                    stp.data = JSONObject.fromObject(data).toString();
                    APIFunction.getInstance().setMoney2Top(data);
                    APIFunction.getInstance().setMoney2TopS(stp.data);
                    APIFunction.getInstance().broadcastPacket(stp);
                }
                {
                    sql = "SELECT name,money3 FROM `player_base` LIMIT 0, 10000000000;";
                    DruidPooledPreparedStatement ps = (DruidPooledPreparedStatement) con.prepareStatement(sql);
                    DruidPooledResultSet rs = (DruidPooledResultSet) ps.executeQuery();
                    HashMap<String, Double> data = new HashMap<>();
                    int t = 0;
                    while (rs.next()) {
                        t++;
                        if (t > 100) break;
                        data.put(rs.getString(1), rs.getDouble(2));
                    }
                    rs.close();
                    ps.close();
                    data = sort(data);
                    SendTopPacket stp = new SendTopPacket();
                    stp.type = 3;
                    stp.data = JSONObject.fromObject(data).toString();
                    APIFunction.getInstance().setMoney3Top(data);
                    APIFunction.getInstance().setMoney3TopS(stp.data);
                    APIFunction.getInstance().broadcastPacket(stp);
                }
                con.close();
                try {
                    Thread.sleep(6 * 60 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
