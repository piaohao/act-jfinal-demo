package demo;

import act.Act;
import act.conf.AppConfig;
import act.job.OnAppStart;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import demo.model.Account;

public class MyConfig {

    // let's use Act's config instead of JFinal PropKit here
    // because the latter one is not able to get configuration by profile (e.g. "prod", "sit", "uat" etc)
    @OnAppStart
    public static void init(AppConfig<?> config) {
        DruidPlugin dp = new DruidPlugin(config.get("jdbc.master.url"), config.get(
                "jdbc.master.username"), config.get("jdbc.master.password"));
        dp.setTestOnBorrow(true);
        dp.setTestWhileIdle(true);
        dp.setTestOnReturn(true);
        dp.addFilter(new StatFilter());
        WallFilter wall = new WallFilter();
        wall.setDbType("mysql");
        dp.addFilter(wall);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(Act.isDev());
        // 所有配置在 MappingKit 中搞定
        // TODO @piaohao it shall use configuration from AppConfig, which load
        // TODO  - different configuration from different folder based on app's profile
        arp.addMapping("account", "id", Account.class);
        dp.start();
        arp.start();
    }
    
}
