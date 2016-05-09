package com.talanlabs.typeadapters;

public interface ITypeAdapter<Src, Dst> {

    /**
     * Src to Dst
     *
     * @param src value of Src
     * @return instance of Dst
     */
    Dst toDst(Src src);

    /**
     * Dst to Src
     *
     * @param dst value of Dst
     * @return instance of Src
     */
    Src toSrc(Dst dst);

}
