package com.talanlabs.typeadapters;

import com.google.common.reflect.TypeToken;

public class TypeAdaptersHelper {

    private TypeAdaptersHelper() {
        super();
    }

    public static <E, Src, Dst> ITypeAdapterFactory<E> newTypeFactory(Class<Src> srcClass, Class<Dst> dstClass, ITypeAdapter<Src, Dst> typeAdapter) {
        return newTypeFactory(TypeToken.of(srcClass), TypeToken.of(dstClass), typeAdapter);
    }

    public static <E, Src, Dst> ITypeAdapterFactory<E> newTypeFactory(final TypeToken<Src> srcTypeToken1, final TypeToken<Dst> dstTypeToken2, final ITypeAdapter<Src, Dst> typeAdapter) {
        return new ITypeAdapterFactory<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public <Src2, Dst2> ITypeAdapter<Src2, Dst2> create(E parent, TypeToken<Src2> srcTypeToken, TypeToken<Dst2> dstTypeToken) {
                if (!srcTypeToken1.equals(srcTypeToken) || !dstTypeToken2.equals(dstTypeToken)) {
                    return null;
                }
                return (ITypeAdapter<Src2, Dst2>)new ITypeAdapter<Src, Dst>() {

                    @Override
                    public Dst toDst(Src scr) {
                        return typeAdapter.toDst(scr);
                    }

                    @Override
                    public Src toSrc(Dst dst) {
                        return typeAdapter.toSrc(dst);
                    }
                };
            }

            @Override
            public String toString() {
                return "Factory[typeHierarchy=[\" + srcTypeToken1 + \" , \" + dstTypeToken1 + \"],adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <E, SrcDst> ITypeAdapterFactory<E> newUniqueTypeFactory(Class<SrcDst> clazz, ITypeAdapter<SrcDst, SrcDst> typeAdapter) {
        return newUniqueTypeFactory(TypeToken.of(clazz), typeAdapter);
    }

    public static <E, SrcDst> ITypeAdapterFactory<E> newUniqueTypeFactory(final TypeToken<SrcDst> typeToken, final ITypeAdapter<SrcDst, SrcDst> typeAdapter) {
        return new ITypeAdapterFactory<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public <Src2, Dst2> ITypeAdapter<Src2, Dst2> create(E parent, TypeToken<Src2> srcTypeToken, TypeToken<Dst2> dstTypeToken) {
                if (!typeToken.equals(srcTypeToken) || !typeToken.equals(dstTypeToken)) {
                    return null;
                }
                return (ITypeAdapter<Src2, Dst2>)new ITypeAdapter<SrcDst, SrcDst>() {

                    @Override
                    public SrcDst toDst(SrcDst scr) {
                        return typeAdapter.toDst(scr);
                    }

                    @Override
                    public SrcDst toSrc(SrcDst dst) {
                        return typeAdapter.toSrc(dst);
                    }
                };
            }

            @Override
            public String toString() {
                return "Factory[typeSame=[" + typeToken + "],adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <E, Src, Dst> ITypeAdapterFactory<E> newTypeHierarchyFactory(Class<Src> srcClass, Class<Dst> dstClass, ITypeAdapter<Src, Dst> typeAdapter) {
        return newTypeHierarchyFactory(TypeToken.of(srcClass), TypeToken.of(dstClass), typeAdapter);
    }

    public static <E, Src, Dst> ITypeAdapterFactory<E> newTypeHierarchyFactory(final TypeToken<Src> srcTypeToken1, final TypeToken<Dst> dstTypeToken1, final ITypeAdapter<Src, Dst> typeAdapter) {
        return new ITypeAdapterFactory<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public <Src2, Dst2> ITypeAdapter<Src2, Dst2> create(E parent, TypeToken<Src2> srcTypeToken, TypeToken<Dst2> dstTypeToken) {
                if (!srcTypeToken1.isSupertypeOf(srcTypeToken) || !dstTypeToken1.isSupertypeOf(dstTypeToken)) {
                    return null;
                }
                return (ITypeAdapter<Src2, Dst2>)new ITypeAdapter<Src, Dst>() {

                    @Override
                    public Dst toDst(Src scr) {
                        return typeAdapter.toDst(scr);
                    }

                    @Override
                    public Src toSrc(Dst dst) {
                        return typeAdapter.toSrc(dst);
                    }
                };
            }

            @Override
            public String toString() {
                return "Factory[typeHierarchy=[" + srcTypeToken1 + " , " + dstTypeToken1 + "],adapter=" + typeAdapter + "]";
            }
        };
    }
}
