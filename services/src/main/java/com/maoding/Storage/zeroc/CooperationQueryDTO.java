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
// Generated from file `Storage.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.Storage.zeroc;

/**
 **/
@Deprecated
public class CooperationQueryDTO implements java.lang.Cloneable,
                                            java.io.Serializable
{
    public String nodeId;

    public String getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }

    public String pNodeId;

    public String getPNodeId()
    {
        return pNodeId;
    }

    public void setPNodeId(String pNodeId)
    {
        this.pNodeId = pNodeId;
    }

    public String nodeName;

    public String getNodeName()
    {
        return nodeName;
    }

    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
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

    public String dutyId;

    public String getDutyId()
    {
        return dutyId;
    }

    public void setDutyId(String dutyId)
    {
        this.dutyId = dutyId;
    }

    public String orgId;

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String projectId;

    public String getProjectId()
    {
        return projectId;
    }

    public void setProjectId(String projectId)
    {
        this.projectId = projectId;
    }

    public String taskId;

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

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

    public int level;

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public CooperationQueryDTO()
    {
        this.nodeId = "";
        this.pNodeId = "";
        this.nodeName = "";
        this.userId = "";
        this.dutyId = "";
        this.orgId = "";
        this.projectId = "";
        this.taskId = "";
        this.scope = "";
        this.key = "";
    }

    public CooperationQueryDTO(String nodeId, String pNodeId, String nodeName, String userId, String dutyId, String orgId, String projectId, String taskId, String scope, String key, int level)
    {
        this.nodeId = nodeId;
        this.pNodeId = pNodeId;
        this.nodeName = nodeName;
        this.userId = userId;
        this.dutyId = dutyId;
        this.orgId = orgId;
        this.projectId = projectId;
        this.taskId = taskId;
        this.scope = scope;
        this.key = key;
        this.level = level;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CooperationQueryDTO r = null;
        if(rhs instanceof CooperationQueryDTO)
        {
            r = (CooperationQueryDTO)rhs;
        }

        if(r != null)
        {
            if(this.nodeId != r.nodeId)
            {
                if(this.nodeId == null || r.nodeId == null || !this.nodeId.equals(r.nodeId))
                {
                    return false;
                }
            }
            if(this.pNodeId != r.pNodeId)
            {
                if(this.pNodeId == null || r.pNodeId == null || !this.pNodeId.equals(r.pNodeId))
                {
                    return false;
                }
            }
            if(this.nodeName != r.nodeName)
            {
                if(this.nodeName == null || r.nodeName == null || !this.nodeName.equals(r.nodeName))
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
            if(this.dutyId != r.dutyId)
            {
                if(this.dutyId == null || r.dutyId == null || !this.dutyId.equals(r.dutyId))
                {
                    return false;
                }
            }
            if(this.orgId != r.orgId)
            {
                if(this.orgId == null || r.orgId == null || !this.orgId.equals(r.orgId))
                {
                    return false;
                }
            }
            if(this.projectId != r.projectId)
            {
                if(this.projectId == null || r.projectId == null || !this.projectId.equals(r.projectId))
                {
                    return false;
                }
            }
            if(this.taskId != r.taskId)
            {
                if(this.taskId == null || r.taskId == null || !this.taskId.equals(r.taskId))
                {
                    return false;
                }
            }
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
            if(this.level != r.level)
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::CooperationQueryDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, nodeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, pNodeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, nodeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, userId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, dutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, orgId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, projectId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, scope);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, key);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, level);
        return h_;
    }

    public CooperationQueryDTO clone()
    {
        CooperationQueryDTO c = null;
        try
        {
            c = (CooperationQueryDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeString(this.nodeId);
        ostr.writeString(this.pNodeId);
        ostr.writeString(this.nodeName);
        ostr.writeString(this.userId);
        ostr.writeString(this.dutyId);
        ostr.writeString(this.orgId);
        ostr.writeString(this.projectId);
        ostr.writeString(this.taskId);
        ostr.writeString(this.scope);
        ostr.writeString(this.key);
        ostr.writeInt(this.level);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.nodeId = istr.readString();
        this.pNodeId = istr.readString();
        this.nodeName = istr.readString();
        this.userId = istr.readString();
        this.dutyId = istr.readString();
        this.orgId = istr.readString();
        this.projectId = istr.readString();
        this.taskId = istr.readString();
        this.scope = istr.readString();
        this.key = istr.readString();
        this.level = istr.readInt();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, CooperationQueryDTO v)
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

    static public CooperationQueryDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        CooperationQueryDTO v = new CooperationQueryDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<CooperationQueryDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, CooperationQueryDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<CooperationQueryDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(CooperationQueryDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final CooperationQueryDTO _nullMarshalValue = new CooperationQueryDTO();

    public static final long serialVersionUID = -1876730753L;
}
