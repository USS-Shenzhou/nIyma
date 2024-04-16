package cn.ussshenzhou.network;

import cn.ussshenzhou.log.LogUtils;
import cn.ussshenzhou.util.Tuple;
import io.netty.buffer.ByteBuf;
import kcp.ChannelConfig;
import kcp.KcpClient;
import kcp.Ukcp;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author USS_Shenzhou
 */
public class BroadcastManager {
    private final ConcurrentHashMap<String, Tuple<Ukcp, Ukcp>> mcServers = new ConcurrentHashMap<>();
    private final KcpClient kcpClient = new KcpClient();
    private final ChannelConfig channelConfig;
    private final UniversalSender universalSender = new UniversalSender(this);

    public BroadcastManager(ChannelConfig channelConfig) {
        this.channelConfig = channelConfig;
    }

    public void add(InetSocketAddress server, int playerDataPort, int othersPort) {
        var s = server.getHostString();
        LogUtils.getLogger().info("Trying to connecting {} with port {} and {}.", s, playerDataPort, othersPort);
        mcServers.put(s, new Tuple<>(
                kcpClient.connect(new InetSocketAddress(s, playerDataPort), channelConfig, universalSender),
                kcpClient.connect(new InetSocketAddress(s, othersPort), channelConfig, universalSender)
        ));
    }

    public void remove(InetSocketAddress server) {
        var s = server.getHostString();
        LogUtils.getLogger().info("Disconnect from {}.", s);
        mcServers.get(s).foreach((a, b) -> {
            a.close();
            b.close();
        });
        mcServers.remove(s);
    }

    public void broadcastPlayerData(InetSocketAddress from, ByteBuf content) {
        var s = from.getHostString();
        mcServers.forEach((server, tuple) -> {
            if (server.equals(s)) {
                return;
            }
            tuple.getA().write(content.retainedDuplicate());
            content.release();
        });
    }

    public void broadcastOther(InetSocketAddress from, ByteBuf content) {
        var s = from.getHostString();
        mcServers.forEach((server, tuple) -> {
            if (server.equals(s)) {
                return;
            }
            tuple.getB().write(content.retainedDuplicate());
            content.release();
        });
    }
}
