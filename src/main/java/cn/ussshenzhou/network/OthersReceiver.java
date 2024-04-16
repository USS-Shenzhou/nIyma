package cn.ussshenzhou.network;

import cn.ussshenzhou.log.LogUtils;
import cn.ussshenzhou.util.Utf8String;
import io.netty.buffer.ByteBuf;
import kcp.KcpListener;
import kcp.Ukcp;

/**
 * @author USS_Shenzhou
 */
public class OthersReceiver implements KcpListener {
    private final NetworkManager networkManager;

    public OthersReceiver(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void onConnected(Ukcp ukcp) {

    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        var type = Utf8String.readString(byteBuf);
        switch (type) {
            case "register" -> register(byteBuf, ukcp);
            case "unregister" -> unregister(ukcp);
            case "playerLogOff" ->
                    networkManager.getBroadcastManager().broadcastOther(ukcp.user().getRemoteAddress(), byteBuf.readerIndex(0));
            case "config" -> {
                //TODO
            }
            default -> {
                LogUtils.getLogger().warn("Received unknown packet type:<{}> from {}.", type, ukcp.user().getRemoteAddress());
                ukcp.close();
            }
        }
    }

    private void register(ByteBuf byteBuf, Ukcp ukcp) {
        networkManager.getBroadcastManager().add(ukcp.user().getRemoteAddress(), byteBuf.readInt(), byteBuf.readInt());
    }

    private void unregister(Ukcp ukcp) {
        networkManager.getBroadcastManager().remove(ukcp.user().getRemoteAddress());
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
