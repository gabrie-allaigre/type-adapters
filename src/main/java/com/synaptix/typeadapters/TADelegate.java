package com.synaptix.typeadapters;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TADelegate<E> {

    private final E parent;
    private final ThreadLocal<Map<Pair<TypeToken<?>, TypeToken<?>>, FutureTypeAdapter<?, ?>>> calls = new ThreadLocal<>();
    private final Map<Pair<TypeToken<?>, TypeToken<?>>, ITypeAdapter<?, ?>> typeTokenCache = Collections.synchronizedMap(new HashMap<>());
    private final List<ITypeAdapterFactory<E>> factories;

    public TADelegate(E parent, List<? extends ITypeAdapterFactory<E>> factories) {
        super();

        this.parent = parent;
        this.factories = ImmutableList.copyOf(factories);
    }

    /**
     * @return Get TypeAdatperFactories
     */
    public List<ITypeAdapterFactory<E>> getFactories() {
        return factories;
    }

    /**
     * Find type adater for source and destination type token
     *
     * @param srcTypeToken source
     * @param dstTypeToken destination
     * @return a type adapter, if not found throw exception
     */
    @SuppressWarnings("unchecked")
    public <Src, Dst> ITypeAdapter<Src, Dst> getTypeAdapter(TypeToken<Src> srcTypeToken, TypeToken<Dst> dstTypeToken) {
        Pair<TypeToken<?>, TypeToken<?>> pair = Pair.of(srcTypeToken, dstTypeToken);

        ITypeAdapter<?, ?> cached = typeTokenCache.get(pair);
        if (cached != null) {
            return (ITypeAdapter<Src, Dst>) cached;
        }

        Map<Pair<TypeToken<?>, TypeToken<?>>, FutureTypeAdapter<?, ?>> threadCalls = calls.get();
        boolean requiresThreadLocalCleanup = false;
        if (threadCalls == null) {
            threadCalls = new HashMap<>();
            calls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }

        // the key and value type parameters always agree
        FutureTypeAdapter<Src, Dst> ongoingCall = (FutureTypeAdapter<Src, Dst>) threadCalls.get(pair);
        if (ongoingCall != null) {
            return ongoingCall;
        }

        try {
            FutureTypeAdapter<Src, Dst> call = new FutureTypeAdapter<>();
            threadCalls.put(pair, call);

            for (ITypeAdapterFactory<E> factory : factories) {
                ITypeAdapter<Src, Dst> candidate = factory.create(parent, srcTypeToken, dstTypeToken);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    typeTokenCache.put(pair, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("TADelegate cannot handle for [" + srcTypeToken + " -> " + dstTypeToken + "]");
        } finally {
            threadCalls.remove(pair);

            if (requiresThreadLocalCleanup) {
                calls.remove();
            }
        }
    }

    private static class FutureTypeAdapter<Src, Dst> implements ITypeAdapter<Src, Dst> {

        private ITypeAdapter<Src, Dst> delegate;

        void setDelegate(ITypeAdapter<Src, Dst> typeAdapter) {
            if (delegate != null) {
                throw new AssertionError();
            }
            delegate = typeAdapter;
        }

        @Override
        public Dst toDst(Src src) {
            return delegate.toDst(src);
        }

        @Override
        public Src toSrc(Dst dst) {
            return delegate.toSrc(dst);
        }
    }
}
