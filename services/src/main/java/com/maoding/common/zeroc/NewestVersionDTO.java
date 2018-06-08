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
// Generated from file `CommonData.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.common.zeroc;

public class NewestVersionDTO implements java.lang.Cloneable,
                                         java.io.Serializable
{
    public String service;

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String client;

    public String getClient()
    {
        return client;
    }

    public void setClient(String client)
    {
        this.client = client;
    }

    public NewestVersionDTO()
    {
        this.service = "";
        this.client = "";
    }

    public NewestVersionDTO(String service, String client)
    {
        this.service = service;
        this.client = client;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        NewestVersionDTO r = null;
        if(rhs instanceof NewestVersionDTO)
        {
            r = (NewestVersionDTO)rhs;
        }

        if(r != null)
        {
            if(this.service != r.service)
            {
                if(this.service == null || r.service == null || !this.service.equals(r.service))
                {
                    return false;
                }
            }
            if(this.client != r.client)
            {
                if(this.client == null || r.client == null || !this.client.equals(r.client))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::NewestVersionDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, service);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, client);
        return h_;
    }

    public NewestVersionDTO clone()
    {
        NewestVersionDTO c = null;
        try
        {
            c = (NewestVersionDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.service);
        ostr.writeString(this.client);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.service = istr.readString();
        this.client = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, NewestVersionDTO v)
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

    static public NewestVersionDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        NewestVersionDTO v = new NewestVersionDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<NewestVersionDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, NewestVersionDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<NewestVersionDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(NewestVersionDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final NewestVersionDTO _nullMarshalValue = new NewestVersionDTO();

    public static final long serialVersionUID = 924294965L;
}
