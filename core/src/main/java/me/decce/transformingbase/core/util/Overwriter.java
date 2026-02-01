package me.decce.transformingbase.core.util;

import me.decce.transformingbase.core.LibraryAccessor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;

import static me.decce.transformingbase.core.util.ReflectionHelper.LOOKUP;
import static me.decce.transformingbase.core.util.ReflectionHelper.unchecked;

public class Overwriter {
    private static final InetAddress dummyAddress = unchecked(() -> InetAddress.getByAddress(new byte[] {0,0,0,0}));
    private static final String dummyAddressString = "0.0.0.0";
    private static final int dummyPort = 0;
    private static final URI dummyUri = unchecked(() -> URI.create("http://0.0.0.0"));
    private static final URL dummyUrl = unchecked(() -> dummyUri.toURL());

    private static final Class<?> classInetSocketAddressHolder = unchecked(()->Class.forName("java.net.InetSocketAddress$InetSocketAddressHolder"));
    private static final MethodHandle constructorInetSocketAddressHolder = unchecked(()->LOOKUP.findConstructor(classInetSocketAddressHolder, MethodType.methodType(void.class, String.class, InetAddress.class, int.class)));
    private static final VarHandle varHolder = unchecked(()->LOOKUP.unreflectVarHandle(InetSocketAddress.class.getDeclaredField("holder")));

    // Not in bootstrap classloader!
    private static final Class<?> classHttpRequestImpl = unchecked(()->Class.forName("jdk.internal.net.http.HttpRequestImpl", false, ClassLoader.getSystemClassLoader()));
    private static final VarHandle varUri = unchecked(()->LOOKUP.unreflectVarHandle(classHttpRequestImpl.getDeclaredField("uri")));
    private static final Class<?> classImmutableHttpRequest = unchecked(()->Class.forName("jdk.internal.net.http.ImmutableHttpRequest", false, ClassLoader.getSystemClassLoader()));
    private static final VarHandle varUriImmutable = unchecked(()->LOOKUP.unreflectVarHandle(classImmutableHttpRequest.getDeclaredField("uri")));

    private static final Class<?> classHttpClient = unchecked(()->Class.forName("sun.net.www.http.HttpClient", false, ClassLoader.getSystemClassLoader()));
    private static final VarHandle varUrl = unchecked(()->LOOKUP.unreflectVarHandle(classHttpClient.getDeclaredField("url")));
    private static final VarHandle varHost = unchecked(()->LOOKUP.unreflectVarHandle(classHttpClient.getDeclaredField("host")));
    private static final VarHandle varPort = unchecked(()->LOOKUP.unreflectVarHandle(classHttpClient.getDeclaredField("port")));

    public static void overwriteAddress(InetSocketAddress addr) {
        try {
            Object newHolder = constructorInetSocketAddressHolder.invoke(dummyAddressString, dummyAddress, dummyPort);
            varHolder.set(addr, newHolder);
        } catch (Throwable e) {
            LibraryAccessor.error("Failed to overwrite InetSocketAddress!", e);
        }
    }

    // Parameter type is HttpRequest, but cannot use as that class is on the system classloader while we're on the bootstrap classloader
    public static void overwriteHttpRequestAddress(Object httpRequest) {
        try {
            if (classHttpRequestImpl.isInstance(httpRequest)) {
                varUri.set(httpRequest, dummyUri);
            }
            else if (classImmutableHttpRequest.isInstance(httpRequest)) {
                varUriImmutable.set(httpRequest, dummyUri);
            }
            else throw new IllegalArgumentException("Unknown HttpRequest type: " + httpRequest.getClass().getName());
        } catch (Throwable e) {
            LibraryAccessor.error("Failed to overwrite HttpRequest!", e);
        }
    }

    public static void overwriteHttpClientAddress(Object sunHttpClient) {
        try {
            varUrl.set(sunHttpClient, dummyUrl);
            varHost.set(sunHttpClient, dummyAddressString);
            varPort.set(sunHttpClient, dummyPort);
        } catch (Throwable e) {
            LibraryAccessor.error("Failed to overwrite HttpClient!", e);
        }
    }
}
