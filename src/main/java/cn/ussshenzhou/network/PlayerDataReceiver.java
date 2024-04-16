package cn.ussshenzhou.network;

import cn.ussshenzhou.log.LogUtils;
import io.netty.buffer.ByteBuf;
import kcp.KcpListener;
import kcp.Ukcp;

/**
 * @author USS_Shenzhou
 */
public class PlayerDataReceiver implements KcpListener {
    private final NetworkManager networkManager;

    public PlayerDataReceiver(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void onConnected(Ukcp ukcp) {

    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        networkManager.getBroadcastManager().broadcastPlayerData(ukcp.user().getRemoteAddress(), byteBuf.retainedDuplicate());
    }

    @Override
    public void handleException(Throwable throwable, Ukcp ukcp) {
        LogUtils.getLogger().error(throwable.getMessage());
        networkManager.getBroadcastManager().remove(ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleClose(Ukcp ukcp) {

    }
}
