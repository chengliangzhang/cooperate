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
// Generated from file `StorageData.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.Storage.zeroc;

public class UpdateAnnotateDTO implements java.lang.Cloneable,
                                          java.io.Serializable
{
    public String relatedId;

    public String getRelatedId()
    {
        return relatedId;
    }

    public void setRelatedId(String relatedId)
    {
        this.relatedId = relatedId;
    }

    public String content;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public long lastModifyTimeStamp;

    public long getLastModifyTimeStamp()
    {
        return lastModifyTimeStamp;
    }

    public void setLastModifyTimeStamp(long lastModifyTimeStamp)
    {
        this.lastModifyTimeStamp = lastModifyTimeStamp;
    }

    public String lastModifyUserId;

    public String getLastModifyUserId()
    {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId)
    {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String lastModifyRoleId;

    public String getLastModifyRoleId()
    {
        return lastModifyRoleId;
    }

    public void setLastModifyRoleId(String lastModifyRoleId)
    {
        this.lastModifyRoleId = lastModifyRoleId;
    }

    public UpdateAnnotateDTO()
    {
        this.relatedId = "";
        this.content = "";
        this.lastModifyUserId = "";
        this.lastModifyRoleId = "";
    }

    public UpdateAnnotateDTO(String relatedId, String content, long lastModifyTimeStamp, String lastModifyUserId, String lastModifyRoleId)
    {
        this.relatedId = relatedId;
        this.content = content;
        this.lastModifyTimeStamp = lastModifyTimeStamp;
        this.lastModifyUserId = lastModifyUserId;
        this.lastModifyRoleId = lastModifyRoleId;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        UpdateAnnotateDTO r = null;
        if(rhs instanceof UpdateAnnotateDTO)
        {
            r = (UpdateAnnotateDTO)rhs;
        }

        if(r != null)
        {
            if(this.relatedId != r.relatedId)
            {
                if(this.relatedId == null || r.relatedId == null || !this.relatedId.equals(r.relatedId))
                {
                    return false;
                }
            }
            if(this.content != r.content)
            {
                if(this.content == null || r.content == null || !this.content.equals(r.content))
                {
                    return false;
                }
            }
            if(this.lastModifyTimeStamp != r.lastModifyTimeStamp)
            {
                return false;
            }
            if(this.lastModifyUserId != r.lastModifyUserId)
            {
                if(this.lastModifyUserId == null || r.lastModifyUserId == null || !this.lastModifyUserId.equals(r.lastModifyUserId))
                {
                    return false;
                }
            }
            if(this.lastModifyRoleId != r.lastModifyRoleId)
            {
                if(this.lastModifyRoleId == null || r.lastModifyRoleId == null || !this.lastModifyRoleId.equals(r.lastModifyRoleId))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::UpdateAnnotateDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, relatedId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, content);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyTimeStamp);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyUserId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyRoleId);
        return h_;
    }

    public UpdateAnnotateDTO clone()
    {
        UpdateAnnotateDTO c = null;
        try
        {
            c = (UpdateAnnotateDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.relatedId);
        ostr.writeString(this.content);
        ostr.writeLong(this.lastModifyTimeStamp);
        ostr.writeString(this.lastModifyUserId);
        ostr.writeString(this.lastModifyRoleId);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.relatedId = istr.readString();
        this.content = istr.readString();
        this.lastModifyTimeStamp = istr.readLong();
        this.lastModifyUserId = istr.readString();
        this.lastModifyRoleId = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, UpdateAnnotateDTO v)
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

    static public UpdateAnnotateDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        UpdateAnnotateDTO v = new UpdateAnnotateDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<UpdateAnnotateDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, UpdateAnnotateDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<UpdateAnnotateDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(UpdateAnnotateDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final UpdateAnnotateDTO _nullMarshalValue = new UpdateAnnotateDTO();

    public static final long serialVersionUID = -1099611853L;
}