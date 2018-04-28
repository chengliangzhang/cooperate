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
// Generated from file `UserData.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.User.zeroc;

public final class WebRoleListHelper
{
    public static void write(com.zeroc.Ice.OutputStream ostr, java.util.List<WebRoleDTO> v)
    {
        if(v == null)
        {
            ostr.writeSize(0);
        }
        else
        {
            ostr.writeSize(v.size());
            for(WebRoleDTO elem : v)
            {
                WebRoleDTO.ice_write(ostr, elem);
            }
        }
    }

    public static java.util.List<WebRoleDTO> read(com.zeroc.Ice.InputStream istr)
    {
        final java.util.List<WebRoleDTO> v;
        v = new java.util.ArrayList<WebRoleDTO>();
        final int len0 = istr.readAndCheckSeqSize(48);
        for(int i0 = 0; i0 < len0; i0++)
        {
            WebRoleDTO elem;
            elem = WebRoleDTO.ice_read(istr);
            v.add(elem);
        }
        return v;
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<java.util.List<WebRoleDTO>> v)
    {
        if(v != null && v.isPresent())
        {
            write(ostr, tag, v.get());
        }
    }

    public static void write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.List<WebRoleDTO> v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            WebRoleListHelper.write(ostr, v);
            ostr.endSize(pos);
        }
    }

    public static java.util.Optional<java.util.List<WebRoleDTO>> read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            java.util.List<WebRoleDTO> v;
            v = WebRoleListHelper.read(istr);
            return java.util.Optional.of(v);
        }
        else
        {
            return java.util.Optional.empty();
        }
    }
}
