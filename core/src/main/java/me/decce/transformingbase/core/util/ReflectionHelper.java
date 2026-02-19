package me.decce.transformingbase.core.util;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {
    public static final MethodHandles.Lookup LOOKUP = getImplLookup();

    public static MethodHandles.Lookup getImplLookup() {
        try {
            var ctor = ReflectionFactory.getReflectionFactory()
                    .newConstructorForSerialization(MethodHandles.Lookup.class, MethodHandles.Lookup.class.getDeclaredConstructor(Class.class));
            var lookup = (MethodHandles.Lookup) ctor.newInstance(MethodHandles.Lookup.class);
            return (MethodHandles.Lookup) lookup.findStaticGetter(MethodHandles.Lookup.class, "IMPL_LOOKUP", MethodHandles.Lookup.class).invokeExact();
        }
        catch (Throwable throwable) {
            try {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Unsafe unsafe = (Unsafe) theUnsafe.get(null);
                Field implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                return (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookup), unsafe.staticFieldOffset(implLookup));
            }
            catch (Throwable throwable1) {
                throwable1.addSuppressed(throwable);
                throw new RuntimeException(throwable1);
            }
        }
    }

    public static <T> T unchecked(UncheckedSupplier<T, ?> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T tryGet(UncheckedSupplier<T, ?> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static MethodHandle unreflect(UncheckedSupplier<Method, ?> method) {
        try {
            return LOOKUP.unreflect(method.get());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle unreflectGetter(UncheckedSupplier<Field, ?> field) {
        try {
            return LOOKUP.unreflectGetter(field.get());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface UncheckedSupplier<T, E extends Exception> {
        T get() throws E;
    }
}
