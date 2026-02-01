package me.decce.transformingbase.transform.transformers;

import me.decce.transformingbase.core.NetworkBlocker;
import me.decce.transformingbase.core.util.Overwriter;
import net.lenni0451.classtransform.InjectionCallback;
import net.lenni0451.classtransform.annotations.CInline;
import net.lenni0451.classtransform.annotations.CReplaceCallback;
import net.lenni0451.classtransform.annotations.CTarget;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.CInject;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executor;

@CReplaceCallback
@CTransformer(name = "jdk.internal.net.http.HttpClientImpl")
public class HttpClientImplTransformer {
    @CInline
    @CInject(method = "sendAsync(Ljava/net/http/HttpRequest;Ljava/net/http/HttpResponse$BodyHandler;Ljava/net/http/HttpResponse$PushPromiseHandler;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", target = @CTarget("HEAD"), cancellable = true)
    private void networkblocker$sendAsync(HttpRequest userRequest,
                                          HttpResponse.BodyHandler<Object> responseHandler,
                                          HttpResponse.PushPromiseHandler<Object> pushPromiseHandler,
                                          Executor exchangeExecutor, InjectionCallback injectionCallback) {
        if (!NetworkBlocker.getManager().checkConnect(userRequest.uri().getHost(), userRequest.uri().getPort())) {
            Overwriter.overwriteHttpRequestAddress(userRequest);
        }
    }
}
