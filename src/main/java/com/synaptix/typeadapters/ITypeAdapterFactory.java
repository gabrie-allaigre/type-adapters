package com.synaptix.typeadapters;

import com.google.common.reflect.TypeToken;

public interface ITypeAdapterFactory<E> {

    /**
     * Get a typeAdapter for source and destination type
     *
     * @param parent     instance of parent
     * @param srcTypeToken source type
     * @param dstTypeToken destination type
     * @return typeAdapter or null if does not match
     */
    <Src, Dst> ITypeAdapter<Src, Dst> create(E parent, TypeToken<Src> srcTypeToken, TypeToken<Dst> dstTypeToken);

}
