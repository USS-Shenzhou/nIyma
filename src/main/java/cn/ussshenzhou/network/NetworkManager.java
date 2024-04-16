package cn.ussshenzhou.network;

import cn.ussshenzhou.config.ConfigHelper;
import cn.ussshenzhou.config.NIymaConfig;
import kcp.*;

/**
 * @author USS_Shenzhou
 */
public class NetworkManager {
    private final ChannelConfig channelConfig;
    private final PlayerDataReceiver playerDataReceiver;
    private final OthersReceiver othersReceiver;
    private final BroadcastManager broadcastManager;

    public NetworkManager() {
        channelConfig = new ChannelConfig();
        channelConfig.nodelay(true, 40, 2, true);
        channelConfig.setSndwnd(256);
        channelConfig.setRcvwnd(256);
        channelConfig.setMtu(400);
        channelConfig.setUseConvChannel(true);
        channelConfig.setTimeoutMillis(2000);
        playerDataReceiver = initReceiver(new PlayerDataReceiver(this), ConfigHelper.getConfigRead(NIymaConfig.class).playerDataPort);
        othersReceiver = initReceiver(new OthersReceiver(this), ConfigHelper.getConfigRead(NIymaConfig.class).othersPort);
        broadcastManager = new BroadcastManager(channelConfig);
    }

    public BroadcastManager getBroadcastManager() {
        return broadcastManager;
    }

    private <T extends KcpListener> T initReceiver(T receiver, int port) {
        KcpServer kcpServer = new KcpServer();
        kcpServer.init(receiver, channelConfig, port);
        return receiver;
    }
}
