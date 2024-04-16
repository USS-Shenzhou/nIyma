package cn.ussshenzhou.network;

import cn.ussshenzhou.log.LogUtils;
import io.netty.buffer.ByteBuf;
import kcp.KcpListener;
import kcp.Ukcp;

/**
 * @author USS_Shenzhou
 */
public class UniversalSender implements KcpListener {
    private final BroadcastManager broadcastManager;

    public UniversalSender(BroadcastManager broadcastManager) {
        this.broadcastManager = broadcastManager;
    }

    @Override
    public void onConnected(Ukcp ukcp) {

    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {

    }

    @Override
    public void handleException(Throwable ex, Ukcp ukcp) {
        LogUtils.getLogger().error(ex.getMessage());
        broadcastManager.remove(ukcp.user().getRemoteAddress());
    }

    @Override
    public void handleClose(Ukcp ukcp) {
        broadcastManager.remove(ukcp.user().getRemoteAddress());
    }
}
