package cn.ussshenzhou;


import cn.ussshenzhou.config.ConfigHelper;
import cn.ussshenzhou.config.NIymaConfig;
import cn.ussshenzhou.log.LogUtils;
import cn.ussshenzhou.network.NetworkManager;

/**
 * @author USS_Shenzhou
 */
public class NIyma {
    public static void main(String[] args) {
        ConfigHelper.loadConfig(new NIymaConfig());
        var networkManager = new NetworkManager();
    }
}