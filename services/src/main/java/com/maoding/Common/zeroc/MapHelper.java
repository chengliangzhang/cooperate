// **********************************************************************
//
// Copyright (c) 2003-2017 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.7.0
//
// <auto-generated>
//
// Generated from file `Common.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.Common.zeroc;

public final class MapHelper
{
    public static void write(com.zeroc.Ice.OutputStream ostr, java.util.Map<java.lang.String, java.lang.String> v)
    {
        if(v == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(v.size());
            for(java.util.Map.Entry<java.lang.String, java.lang.String> e : v.entrySet())
            {
                ostr.writeString(e.getKey());
                ostr.writeString(e.getValue());
            }
        }
    }

    public static java.util.Map<java.lang.String, java.lang.String> read(com.zeroc.Ice.InputStream istr)
    {
        java.util.Map<java.lang.String, java.lang.String> v;
        v = new java.util.HashMap<String,String>();
        int sz0 = istr.readSize();
        for(int i0 = 0; i0 < sz0; i0++)
        {
            String key;
            key = istr.readString();
            String value;
            value = istr.readString();
            v.put(key, value);
        }
        return v;
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<java.util.Map<java.lang.String, java.lang.String>> v)
    {
        if(v != null && v.isPresent())
        {
            write(ostr, tag, v.get());
        }
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Map<java.lang.String, java.lang.String> v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            MapHelper.write(ostr, v);
            ostr.endSize(pos);
        }
    }

    public static java.util.Optional<java.util.Map<java.lang.String, java.lang.String>> read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            java.util.Map<java.lang.String, java.lang.String> v;
            v = MapHelper.read(istr);
            return java.util.Optional.of(v);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }
}
