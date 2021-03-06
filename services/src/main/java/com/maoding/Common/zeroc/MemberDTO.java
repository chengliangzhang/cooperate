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

/**
 **/
@Deprecated
public class MemberDTO implements java.lang.Cloneable,
                                  java.io.Serializable
{
    public String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String userId;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String userName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public short memberTypeId;

    public short getMemberTypeId()
    {
        return memberTypeId;
    }

    public void setMemberTypeId(short memberTypeId)
    {
        this.memberTypeId = memberTypeId;
    }

    public String memberTypeName;

    public String getMemberTypeName()
    {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName)
    {
        this.memberTypeName = memberTypeName;
    }

    public MemberDTO()
    {
        this.id = "";
        this.userId = "";
        this.userName = "";
        this.memberTypeName = "";
    }

    public MemberDTO(String id, String userId, String userName, short memberTypeId, String memberTypeName)
    {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.memberTypeId = memberTypeId;
        this.memberTypeName = memberTypeName;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        MemberDTO r = null;
        if(rhs instanceof MemberDTO)
        {
            r = (MemberDTO)rhs;
        }

        if(r != null)
        {
            if(this.id != r.id)
            {
                if(this.id == null || r.id == null || !this.id.equals(r.id))
                {
                    return false;
                }
            }
            if(this.userId != r.userId)
            {
                if(this.userId == null || r.userId == null || !this.userId.equals(r.userId))
                {
                    return false;
                }
            }
            if(this.userName != r.userName)
            {
                if(this.userName == null || r.userName == null || !this.userName.equals(r.userName))
                {
                    return false;
                }
            }
            if(this.memberTypeId != r.memberTypeId)
            {
                return false;
            }
            if(this.memberTypeName != r.memberTypeName)
            {
                if(this.memberTypeName == null || r.memberTypeName == null || !this.memberTypeName.equals(r.memberTypeName))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::MemberDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, id);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, userId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, userName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, memberTypeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, memberTypeName);
        return h_;
    }

    public MemberDTO clone()
    {
        MemberDTO c = null;
        try
        {
            c = (MemberDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.id);
        ostr.writeString(this.userId);
        ostr.writeString(this.userName);
        ostr.writeShort(this.memberTypeId);
        ostr.writeString(this.memberTypeName);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.id = istr.readString();
        this.userId = istr.readString();
        this.userName = istr.readString();
        this.memberTypeId = istr.readShort();
        this.memberTypeName = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, MemberDTO v)
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

    static public MemberDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        MemberDTO v = new MemberDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<MemberDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, MemberDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<MemberDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(MemberDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final MemberDTO _nullMarshalValue = new MemberDTO();

    public static final long serialVersionUID = 2137287503L;
}
