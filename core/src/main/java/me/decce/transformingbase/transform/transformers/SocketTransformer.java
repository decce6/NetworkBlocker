package me.decce.transformingbase.transform.transformers;

import me.decce.transformingbase.core.NetworkBlocker;
import me.decce.transformingbase.core.util.Overwriter;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CInline;
import net.lenni0451.classtransform.annotations.CReplaceCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@CReplaceCallback
@CTransformer(Socket.class)
public class SocketTransformer {
    @CInline
    @CInject(method = "connect(Ljava/net/SocketAddress;I)V", target = @CTarget(value = "INVOKE", target = "Ljava/net/InetSocketAddress;getAddress()Ljava/net/InetAddress;"))
    private void networkblocker$connect(SocketAddress endpoint, int timeout, InjectionCallback injectionCallback) {
        var epoint = (InetSocketAddress) endpoint;
        var addr = epoint.getAddress();
        var host = epoint.isUnresolved() ? epoint.getHostName() : addr.getHostAddress();
        if (!NetworkBlocker.getManager().checkConnect(host, epoint.getPort())) {
            Overwriter.overwriteAddress(epoint);
        }
    }
}
