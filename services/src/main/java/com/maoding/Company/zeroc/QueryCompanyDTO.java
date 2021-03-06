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
// Generated from file `CompanyData.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.company.zeroc;

public class QueryCompanyDTO implements java.lang.Cloneable,
                                        java.io.Serializable
{
    public String userIdString;

    public String getUserIdString()
    {
        return userIdString;
    }

    public void setUserIdString(String userIdString)
    {
        this.userIdString = userIdString;
    }

    public QueryCompanyDTO()
    {
        this.userIdString = "";
    }

    public QueryCompanyDTO(String userIdString)
    {
        this.userIdString = userIdString;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        QueryCompanyDTO r = null;
        if(rhs instanceof QueryCompanyDTO)
        {
            r = (QueryCompanyDTO)rhs;
        }

        if(r != null)
        {
            if(this.userIdString != r.userIdString)
            {
                if(this.userIdString == null || r.userIdString == null || !this.userIdString.equals(r.userIdString))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::QueryCompanyDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, userIdString);
        return h_;
    }

    public QueryCompanyDTO clone()
    {
        QueryCompanyDTO c = null;
        try
        {
            c = (QueryCompanyDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.userIdString);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.userIdString = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, QueryCompanyDTO v)
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

    static public QueryCompanyDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        QueryCompanyDTO v = new QueryCompanyDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<QueryCompanyDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, QueryCompanyDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<QueryCompanyDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(QueryCompanyDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final QueryCompanyDTO _nullMarshalValue = new QueryCompanyDTO();

    public static final long serialVersionUID = -1765271594L;
}
