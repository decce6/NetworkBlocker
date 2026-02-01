package me.decce.transformingbase.transform;

import me.decce.transformingbase.transform.transformers.HttpClientImplTransformer;
import me.decce.transformingbase.transform.transformers.HttpClientTransformer;
import me.decce.transformingbase.transform.transformers.SocketTransformer;

public enum TransformerDefinitions {
    SOCKET("java.net.Socket",SocketTransformer.class),
    HTTP_CLIENT("sun.net.www.http.HttpClient", HttpClientTransformer.class),
    HTTP_CLIENT_IMPL("jdk.internal.net.http.HttpClientImpl", HttpClientImplTransformer.class);

    public final TransformerDefinition definition;

    TransformerDefinitions(String target, Class<?> transformer) {
        this(new TransformerDefinition(target, transformer));
    }

    TransformerDefinitions(TransformerDefinition definition) {
        this.definition = definition;
    }
}
