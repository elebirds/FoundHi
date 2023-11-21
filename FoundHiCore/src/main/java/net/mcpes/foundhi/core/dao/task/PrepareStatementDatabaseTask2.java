package net.mcpes.foundhi.core.dao.task;

import cn.nukkit.scheduler.AsyncTask;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.mcpes.foundhi.core.dao.Database.getMySQLConnection;


public class PrepareStatementDatabaseTask2 extends AsyncTask {
    private final String sql;

    public PrepareStatementDatabaseTask2(String sql) {
        this.sql = sql;
    }

    @Override
    public void onRun() {
        PreparedStatement ps;
        try {
            DruidPooledConnection con = getMySQLConnection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
