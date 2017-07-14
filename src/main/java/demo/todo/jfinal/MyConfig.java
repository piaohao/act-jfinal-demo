package demo.todo.jfinal;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import demo.todo.jfinal.model.Account;

public class MyConfig {

    public static void init() {
        {
            PropKit.use("common.properties");
            DruidPlugin dp = new DruidPlugin(PropKit.get("jdbc.master.url"), PropKit.get(
                    "jdbc.master.username"), PropKit.get("jdbc.master.password"));
            dp.setTestOnBorrow(true);
            dp.setTestWhileIdle(true);
            dp.setTestOnReturn(true);
            dp.addFilter(new StatFilter());
            WallFilter wall = new WallFilter();
            wall.setDbType("mysql");
            dp.addFilter(wall);

            ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
            arp.setShowSql(PropKit.getBoolean("devMode", true));
            // 所有配置在 MappingKit 中搞定
            arp.addMapping("account", "id", Account.class);
            dp.start();
            arp.start();
        }
    }
}
