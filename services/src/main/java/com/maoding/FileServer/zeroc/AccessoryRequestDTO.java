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
// Generated from file `FileServerData.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.fileServer.zeroc;

public class AccessoryRequestDTO implements java.lang.Cloneable,
                                            java.io.Serializable
{
    public String path;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public byte[] data;

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public byte getData(int index)
    {
        return this.data[index];
    }

    public void setData(int index, byte val)
    {
        this.data[index] = val;
    }

    public AccessoryRequestDTO()
    {
        this.path = "";
    }

    public AccessoryRequestDTO(String path, byte[] data)
    {
        this.path = path;
        this.data = data;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        AccessoryRequestDTO r = null;
        if(rhs instanceof AccessoryRequestDTO)
        {
            r = (AccessoryRequestDTO)rhs;
        }

        if(r != null)
        {
            if(this.path != r.path)
            {
                if(this.path == null || r.path == null || !this.path.equals(r.path))
                {
                    return false;
                }
            }
            if(!java.util.Arrays.equals(this.data, r.data))
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::AccessoryRequestDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, path);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, data);
        return h_;
    }

    public AccessoryRequestDTO clone()
    {
        AccessoryRequestDTO c = null;
        try
        {
            c = (AccessoryRequestDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.path);
        ostr.writeByteSeq(this.data);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.path = istr.readString();
        this.data = istr.readByteSeq();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, AccessoryRequestDTO v)
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

    static public AccessoryRequestDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        AccessoryRequestDTO v = new AccessoryRequestDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<AccessoryRequestDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, AccessoryRequestDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<AccessoryRequestDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(AccessoryRequestDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final AccessoryRequestDTO _nullMarshalValue = new AccessoryRequestDTO();

    public static final long serialVersionUID = -1730606983L;
}
