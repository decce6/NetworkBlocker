package me.decce.transformingbase.transform.transformers;

import me.decce.transformingbase.core.NetworkBlocker;
import me.decce.transformingbase.core.NetworkBlockerConfig;
import me.decce.transformingbase.core.util.Overwriter;
import me.decce.transformingbase.core.util.Thrower;
import net.lenni0451.classtransform.annotations.CInline;
import net.lenni0451.classtransform.annotations.CReplaceCallback;
import net.lenni0451.classtransform.annotations.CShadow;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;

@CReplaceCallback
@CTransformer(name = "sun.net.www.http.HttpClient")
public class HttpClientTransformer {
    @CShadow
    protected String host;
    @CShadow
    protected int port;

    @CInline
    @CInject(method = "openServer", target = @CTarget("HEAD"))
    private void networkblocker$openServer() {
        if (!NetworkBlocker.getManager().checkConnect(host, port)) {
            if (NetworkBlockerConfig.BlockMethod.REDIRECT == NetworkBlocker.config.currentBlockMethod) {
                Overwriter.overwriteHttpClientAddress((Object)this);
            }
            else {
                Thrower.throwBlockedException();
            }
        }
    }
}
