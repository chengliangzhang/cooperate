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

public class FullNodeDTO implements java.lang.Cloneable,
                                    java.io.Serializable
{
    public SimpleNodeDTO basic;

    public SimpleNodeDTO getBasic()
    {
        return basic;
    }

    public void setBasic(SimpleNodeDTO basic)
    {
        this.basic = basic;
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

    public String projectName;

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String classicId;

    public String getClassicId()
    {
        return classicId;
    }

    public void setClassicId(String classicId)
    {
        this.classicId = classicId;
    }

    public String classicName;

    public String getClassicName()
    {
        return classicName;
    }

    public void setClassicName(String classicName)
    {
        this.classicName = classicName;
    }

    public String issueId;

    public String getIssueId()
    {
        return issueId;
    }

    public void setIssueId(String issueId)
    {
        this.issueId = issueId;
    }

    public String issueName;

    public String getIssueName()
    {
        return issueName;
    }

    public void setIssueName(String issueName)
    {
        this.issueName = issueName;
    }

    public String issuePath;

    public String getIssuePath()
    {
        return issuePath;
    }

    public void setIssuePath(String issuePath)
    {
        this.issuePath = issuePath;
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

    public String taskName;

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String taskPath;

    public String getTaskPath()
    {
        return taskPath;
    }

    public void setTaskPath(String taskPath)
    {
        this.taskPath = taskPath;
    }

    public String companyId;

    public String getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

    public String companyName;

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String ownerDutyId;

    public String getOwnerDutyId()
    {
        return ownerDutyId;
    }

    public void setOwnerDutyId(String ownerDutyId)
    {
        this.ownerDutyId = ownerDutyId;
    }

    public String ownerUserId;

    public String getOwnerUserId()
    {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public String ownerName;

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public String storagePath;

    public String getStoragePath()
    {
        return storagePath;
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
    }

    public FullNodeDTO()
    {
        this.basic = new SimpleNodeDTO();
        this.projectId = "";
        this.projectName = "";
        this.classicId = "";
        this.classicName = "";
        this.issueId = "";
        this.issueName = "";
        this.issuePath = "";
        this.taskId = "";
        this.taskName = "";
        this.taskPath = "";
        this.companyId = "";
        this.companyName = "";
        this.ownerDutyId = "";
        this.ownerUserId = "";
        this.ownerName = "";
        this.storagePath = "";
    }

    public FullNodeDTO(SimpleNodeDTO basic, String projectId, String projectName, String classicId, String classicName, String issueId, String issueName, String issuePath, String taskId, String taskName, String taskPath, String companyId, String companyName, String ownerDutyId, String ownerUserId, String ownerName, String storagePath)
    {
        this.basic = basic;
        this.projectId = projectId;
        this.projectName = projectName;
        this.classicId = classicId;
        this.classicName = classicName;
        this.issueId = issueId;
        this.issueName = issueName;
        this.issuePath = issuePath;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskPath = taskPath;
        this.companyId = companyId;
        this.companyName = companyName;
        this.ownerDutyId = ownerDutyId;
        this.ownerUserId = ownerUserId;
        this.ownerName = ownerName;
        this.storagePath = storagePath;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FullNodeDTO r = null;
        if(rhs instanceof FullNodeDTO)
        {
            r = (FullNodeDTO)rhs;
        }

        if(r != null)
        {
            if(this.basic != r.basic)
            {
                if(this.basic == null || r.basic == null || !this.basic.equals(r.basic))
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
            if(this.projectName != r.projectName)
            {
                if(this.projectName == null || r.projectName == null || !this.projectName.equals(r.projectName))
                {
                    return false;
                }
            }
            if(this.classicId != r.classicId)
            {
                if(this.classicId == null || r.classicId == null || !this.classicId.equals(r.classicId))
                {
                    return false;
                }
            }
            if(this.classicName != r.classicName)
            {
                if(this.classicName == null || r.classicName == null || !this.classicName.equals(r.classicName))
                {
                    return false;
                }
            }
            if(this.issueId != r.issueId)
            {
                if(this.issueId == null || r.issueId == null || !this.issueId.equals(r.issueId))
                {
                    return false;
                }
            }
            if(this.issueName != r.issueName)
            {
                if(this.issueName == null || r.issueName == null || !this.issueName.equals(r.issueName))
                {
                    return false;
                }
            }
            if(this.issuePath != r.issuePath)
            {
                if(this.issuePath == null || r.issuePath == null || !this.issuePath.equals(r.issuePath))
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
            if(this.taskName != r.taskName)
            {
                if(this.taskName == null || r.taskName == null || !this.taskName.equals(r.taskName))
                {
                    return false;
                }
            }
            if(this.taskPath != r.taskPath)
            {
                if(this.taskPath == null || r.taskPath == null || !this.taskPath.equals(r.taskPath))
                {
                    return false;
                }
            }
            if(this.companyId != r.companyId)
            {
                if(this.companyId == null || r.companyId == null || !this.companyId.equals(r.companyId))
                {
                    return false;
                }
            }
            if(this.companyName != r.companyName)
            {
                if(this.companyName == null || r.companyName == null || !this.companyName.equals(r.companyName))
                {
                    return false;
                }
            }
            if(this.ownerDutyId != r.ownerDutyId)
            {
                if(this.ownerDutyId == null || r.ownerDutyId == null || !this.ownerDutyId.equals(r.ownerDutyId))
                {
                    return false;
                }
            }
            if(this.ownerUserId != r.ownerUserId)
            {
                if(this.ownerUserId == null || r.ownerUserId == null || !this.ownerUserId.equals(r.ownerUserId))
                {
                    return false;
                }
            }
            if(this.ownerName != r.ownerName)
            {
                if(this.ownerName == null || r.ownerName == null || !this.ownerName.equals(r.ownerName))
                {
                    return false;
                }
            }
            if(this.storagePath != r.storagePath)
            {
                if(this.storagePath == null || r.storagePath == null || !this.storagePath.equals(r.storagePath))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::FullNodeDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, basic);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, projectId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, projectName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, classicId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, classicName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, issueId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, issueName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, issuePath);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskPath);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, companyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, companyName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerUserId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, storagePath);
        return h_;
    }

    public FullNodeDTO clone()
    {
        FullNodeDTO c = null;
        try
        {
            c = (FullNodeDTO)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        SimpleNodeDTO.ice_write(ostr, this.basic);
        ostr.writeString(this.projectId);
        ostr.writeString(this.projectName);
        ostr.writeString(this.classicId);
        ostr.writeString(this.classicName);
        ostr.writeString(this.issueId);
        ostr.writeString(this.issueName);
        ostr.writeString(this.issuePath);
        ostr.writeString(this.taskId);
        ostr.writeString(this.taskName);
        ostr.writeString(this.taskPath);
        ostr.writeString(this.companyId);
        ostr.writeString(this.companyName);
        ostr.writeString(this.ownerDutyId);
        ostr.writeString(this.ownerUserId);
        ostr.writeString(this.ownerName);
        ostr.writeString(this.storagePath);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.basic = SimpleNodeDTO.ice_read(istr);
        this.projectId = istr.readString();
        this.projectName = istr.readString();
        this.classicId = istr.readString();
        this.classicName = istr.readString();
        this.issueId = istr.readString();
        this.issueName = istr.readString();
        this.issuePath = istr.readString();
        this.taskId = istr.readString();
        this.taskName = istr.readString();
        this.taskPath = istr.readString();
        this.companyId = istr.readString();
        this.companyName = istr.readString();
        this.ownerDutyId = istr.readString();
        this.ownerUserId = istr.readString();
        this.ownerName = istr.readString();
        this.storagePath = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, FullNodeDTO v)
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

    static public FullNodeDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        FullNodeDTO v = new FullNodeDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<FullNodeDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, FullNodeDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<FullNodeDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(FullNodeDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final FullNodeDTO _nullMarshalValue = new FullNodeDTO();

    public static final long serialVersionUID = 155459258L;
}
