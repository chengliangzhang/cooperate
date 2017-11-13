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
// Generated from file `FileServer.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.FileServer.zeroc;

public class FileDTO implements java.lang.Cloneable,
                                java.io.Serializable
{
    public String scope;

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String key;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public FileDTO()
    {
        this.scope = "";
        this.key = "";
    }

    public FileDTO(String scope, String key)
    {
        this.scope = scope;
        this.key = key;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FileDTO r = null;
        if(rhs instanceof FileDTO)
        {
            r = (FileDTO)rhs;
        }

        if(r != null)
        {
            if(this.scope != r.scope)
            {
                if(this.scope == null || r.scope == null || !this.scope.equals(r.scope))
                {
                    return false;
                }
            }
            if(this.key != r.key)
            {
                if(this.key == null || r.key == null || !this.key.equals(r.key))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::FileDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, scope);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, key);
        return h_;
    }

    public FileDTO clone()
    {
        FileDTO c = null;
        try
        {
            c = (FileDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.scope);
        ostr.writeString(this.key);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.scope = istr.readString();
        this.key = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, FileDTO v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public FileDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        FileDTO v = new FileDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<FileDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, FileDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<FileDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(FileDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final FileDTO _nullMarshalValue = new FileDTO();

    public static final long serialVersionUID = 1193132814L;
}