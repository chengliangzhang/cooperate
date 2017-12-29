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

public class FileNodeDTO implements java.lang.Cloneable,
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

    /**
     **/
    @Deprecated
    public String id;

    /**
     **/
    @Deprecated
    public String getId()
    {
        return id;
    }

    /**
     **/
    @Deprecated
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     **/
    @Deprecated
    public String name;

    /**
     **/
    @Deprecated
    public String getName()
    {
        return name;
    }

    /**
     **/
    @Deprecated
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     **/
    @Deprecated
    public String pid;

    /**
     **/
    @Deprecated
    public String getPid()
    {
        return pid;
    }

    /**
     **/
    @Deprecated
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    /**
     **/
    @Deprecated
    public short typeId;

    /**
     **/
    @Deprecated
    public short getTypeId()
    {
        return typeId;
    }

    /**
     **/
    @Deprecated
    public void setTypeId(short typeId)
    {
        this.typeId = typeId;
    }

    /**
     **/
    @Deprecated
    public String typeName;

    /**
     **/
    @Deprecated
    public String getTypeName()
    {
        return typeName;
    }

    /**
     **/
    @Deprecated
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    /**
     **/
    @Deprecated
    public String path;

    /**
     **/
    @Deprecated
    public String getPath()
    {
        return path;
    }

    /**
     **/
    @Deprecated
    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     **/
    @Deprecated
    public long createTimeStamp;

    /**
     **/
    @Deprecated
    public long getCreateTimeStamp()
    {
        return createTimeStamp;
    }

    /**
     **/
    @Deprecated
    public void setCreateTimeStamp(long createTimeStamp)
    {
        this.createTimeStamp = createTimeStamp;
    }

    /**
     **/
    @Deprecated
    public String createTimeText;

    /**
     **/
    @Deprecated
    public String getCreateTimeText()
    {
        return createTimeText;
    }

    /**
     **/
    @Deprecated
    public void setCreateTimeText(String createTimeText)
    {
        this.createTimeText = createTimeText;
    }

    /**
     **/
    @Deprecated
    public long lastModifyTimeStamp;

    /**
     **/
    @Deprecated
    public long getLastModifyTimeStamp()
    {
        return lastModifyTimeStamp;
    }

    /**
     **/
    @Deprecated
    public void setLastModifyTimeStamp(long lastModifyTimeStamp)
    {
        this.lastModifyTimeStamp = lastModifyTimeStamp;
    }

    /**
     **/
    @Deprecated
    public String lastModifyTimeText;

    /**
     **/
    @Deprecated
    public String getLastModifyTimeText()
    {
        return lastModifyTimeText;
    }

    /**
     **/
    @Deprecated
    public void setLastModifyTimeText(String lastModifyTimeText)
    {
        this.lastModifyTimeText = lastModifyTimeText;
    }

    /**
     **/
    @Deprecated
    public boolean isReadOnly;

    /**
     **/
    @Deprecated
    public boolean getIsReadOnly()
    {
        return isReadOnly;
    }

    /**
     **/
    @Deprecated
    public void setIsReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    @Deprecated
    public boolean isIsReadOnly()
    {
        return isReadOnly;
    }

    /**
     **/
    @Deprecated
    public long fileLength;

    /**
     **/
    @Deprecated
    public long getFileLength()
    {
        return fileLength;
    }

    /**
     **/
    @Deprecated
    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    public String fileChecksum;

    public String getFileChecksum()
    {
        return fileChecksum;
    }

    public void setFileChecksum(String fileChecksum)
    {
        this.fileChecksum = fileChecksum;
    }

    public String fileVersion;

    public String getFileVersion()
    {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion)
    {
        this.fileVersion = fileVersion;
    }

    public short fileTypeId;

    public short getFileTypeId()
    {
        return fileTypeId;
    }

    public void setFileTypeId(short fileTypeId)
    {
        this.fileTypeId = fileTypeId;
    }

    public String fileTypeName;

    public String getFileTypeName()
    {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName)
    {
        this.fileTypeName = fileTypeName;
    }

    /**
     **/
    @Deprecated
    public short syncModeId;

    /**
     **/
    @Deprecated
    public short getSyncModeId()
    {
        return syncModeId;
    }

    /**
     **/
    @Deprecated
    public void setSyncModeId(short syncModeId)
    {
        this.syncModeId = syncModeId;
    }

    /**
     **/
    @Deprecated
    public String syncModeName;

    /**
     **/
    @Deprecated
    public String getSyncModeName()
    {
        return syncModeName;
    }

    /**
     **/
    @Deprecated
    public void setSyncModeName(String syncModeName)
    {
        this.syncModeName = syncModeName;
    }

    public String lastModifyAddress;

    public String getLastModifyAddress()
    {
        return lastModifyAddress;
    }

    public void setLastModifyAddress(String lastModifyAddress)
    {
        this.lastModifyAddress = lastModifyAddress;
    }

    public String fileServerTypeId;

    public String getFileServerTypeId()
    {
        return fileServerTypeId;
    }

    public void setFileServerTypeId(String fileServerTypeId)
    {
        this.fileServerTypeId = fileServerTypeId;
    }

    public String fileServerTypeName;

    public String getFileServerTypeName()
    {
        return fileServerTypeName;
    }

    public void setFileServerTypeName(String fileServerTypeName)
    {
        this.fileServerTypeName = fileServerTypeName;
    }

    public String fileScope;

    public String getFileScope()
    {
        return fileScope;
    }

    public void setFileScope(String fileScope)
    {
        this.fileScope = fileScope;
    }

    public String fileKey;

    public String getFileKey()
    {
        return fileKey;
    }

    public void setFileKey(String fileKey)
    {
        this.fileKey = fileKey;
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

    public String ownerUserId;

    public String getOwnerUserId()
    {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId)
    {
        this.ownerUserId = ownerUserId;
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

    public String ownerName;

    public String getOwnerName()
    {
        return ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
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

    public java.util.List<com.maoding.Project.zeroc.MemberDTO> taskMemberList;

    public java.util.List<com.maoding.Project.zeroc.MemberDTO> getTaskMemberList()
    {
        return taskMemberList;
    }

    public void setTaskMemberList(java.util.List<com.maoding.Project.zeroc.MemberDTO> taskMemberList)
    {
        this.taskMemberList = taskMemberList;
    }

    public java.util.List<HistoryDTO> historyList;

    public java.util.List<HistoryDTO> getHistoryList()
    {
        return historyList;
    }

    public void setHistoryList(java.util.List<HistoryDTO> historyList)
    {
        this.historyList = historyList;
    }

    /**
     **/
    @Deprecated
    public boolean isValid;

    /**
     **/
    @Deprecated
    public boolean getIsValid()
    {
        return isValid;
    }

    /**
     **/
    @Deprecated
    public void setIsValid(boolean isValid)
    {
        this.isValid = isValid;
    }

    @Deprecated
    public boolean isIsValid()
    {
        return isValid;
    }

    /**
     **/
    @Deprecated
    public boolean locking;

    /**
     **/
    @Deprecated
    public boolean getLocking()
    {
        return locking;
    }

    /**
     **/
    @Deprecated
    public void setLocking(boolean locking)
    {
        this.locking = locking;
    }

    @Deprecated
    public boolean isLocking()
    {
        return locking;
    }

    /**
     **/
    @Deprecated
    public String localFile;

    /**
     **/
    @Deprecated
    public String getLocalFile()
    {
        return localFile;
    }

    /**
     **/
    @Deprecated
    public void setLocalFile(String localFile)
    {
        this.localFile = localFile;
    }

    /**
     **/
    @Deprecated
    public String creatorDutyId;

    /**
     **/
    @Deprecated
    public String getCreatorDutyId()
    {
        return creatorDutyId;
    }

    /**
     **/
    @Deprecated
    public void setCreatorDutyId(String creatorDutyId)
    {
        this.creatorDutyId = creatorDutyId;
    }

    /**
     **/
    @Deprecated
    public String creatorDutyName;

    /**
     **/
    @Deprecated
    public String getCreatorDutyName()
    {
        return creatorDutyName;
    }

    /**
     **/
    @Deprecated
    public void setCreatorDutyName(String creatorDutyName)
    {
        this.creatorDutyName = creatorDutyName;
    }

    /**
     **/
    @Deprecated
    public String lastModifyDutyId;

    /**
     **/
    @Deprecated
    public String getLastModifyDutyId()
    {
        return lastModifyDutyId;
    }

    /**
     **/
    @Deprecated
    public void setLastModifyDutyId(String lastModifyDutyId)
    {
        this.lastModifyDutyId = lastModifyDutyId;
    }

    /**
     **/
    @Deprecated
    public String lastModifyDutyName;

    /**
     **/
    @Deprecated
    public String getLastModifyDutyName()
    {
        return lastModifyDutyName;
    }

    /**
     **/
    @Deprecated
    public void setLastModifyDutyName(String lastModifyDutyName)
    {
        this.lastModifyDutyName = lastModifyDutyName;
    }

    /**
     **/
    @Deprecated
    public String organizationId;

    /**
     **/
    @Deprecated
    public String getOrganizationId()
    {
        return organizationId;
    }

    /**
     **/
    @Deprecated
    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    /**
     **/
    @Deprecated
    public String organizationName;

    /**
     **/
    @Deprecated
    public String getOrganizationName()
    {
        return organizationName;
    }

    /**
     **/
    @Deprecated
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    public FileNodeDTO()
    {
        this.basic = new SimpleNodeDTO();
        this.id = "";
        this.name = "";
        this.pid = "";
        this.typeName = "";
        this.path = "";
        this.createTimeText = "";
        this.lastModifyTimeText = "";
        this.fileChecksum = "";
        this.fileVersion = "";
        this.fileTypeName = "";
        this.syncModeName = "";
        this.lastModifyAddress = "";
        this.fileServerTypeId = "";
        this.fileServerTypeName = "";
        this.fileScope = "";
        this.fileKey = "";
        this.scope = "";
        this.key = "";
        this.ownerUserId = "";
        this.ownerDutyId = "";
        this.ownerName = "";
        this.issueId = "";
        this.issueName = "";
        this.taskId = "";
        this.taskName = "";
        this.projectId = "";
        this.projectName = "";
        this.localFile = "";
        this.creatorDutyId = "";
        this.creatorDutyName = "";
        this.lastModifyDutyId = "";
        this.lastModifyDutyName = "";
        this.organizationId = "";
        this.organizationName = "";
    }

    public FileNodeDTO(SimpleNodeDTO basic, String id, String name, String pid, short typeId, String typeName, String path, long createTimeStamp, String createTimeText, long lastModifyTimeStamp, String lastModifyTimeText, boolean isReadOnly, long fileLength, String fileChecksum, String fileVersion, short fileTypeId, String fileTypeName, short syncModeId, String syncModeName, String lastModifyAddress, String fileServerTypeId, String fileServerTypeName, String fileScope, String fileKey, String scope, String key, String ownerUserId, String ownerDutyId, String ownerName, String issueId, String issueName, String taskId, String taskName, String projectId, String projectName, java.util.List<com.maoding.Project.zeroc.MemberDTO> taskMemberList, java.util.List<HistoryDTO> historyList, boolean isValid, boolean locking, String localFile, String creatorDutyId, String creatorDutyName, String lastModifyDutyId, String lastModifyDutyName, String organizationId, String organizationName)
    {
        this.basic = basic;
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.typeId = typeId;
        this.typeName = typeName;
        this.path = path;
        this.createTimeStamp = createTimeStamp;
        this.createTimeText = createTimeText;
        this.lastModifyTimeStamp = lastModifyTimeStamp;
        this.lastModifyTimeText = lastModifyTimeText;
        this.isReadOnly = isReadOnly;
        this.fileLength = fileLength;
        this.fileChecksum = fileChecksum;
        this.fileVersion = fileVersion;
        this.fileTypeId = fileTypeId;
        this.fileTypeName = fileTypeName;
        this.syncModeId = syncModeId;
        this.syncModeName = syncModeName;
        this.lastModifyAddress = lastModifyAddress;
        this.fileServerTypeId = fileServerTypeId;
        this.fileServerTypeName = fileServerTypeName;
        this.fileScope = fileScope;
        this.fileKey = fileKey;
        this.scope = scope;
        this.key = key;
        this.ownerUserId = ownerUserId;
        this.ownerDutyId = ownerDutyId;
        this.ownerName = ownerName;
        this.issueId = issueId;
        this.issueName = issueName;
        this.taskId = taskId;
        this.taskName = taskName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskMemberList = taskMemberList;
        this.historyList = historyList;
        this.isValid = isValid;
        this.locking = locking;
        this.localFile = localFile;
        this.creatorDutyId = creatorDutyId;
        this.creatorDutyName = creatorDutyName;
        this.lastModifyDutyId = lastModifyDutyId;
        this.lastModifyDutyName = lastModifyDutyName;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        FileNodeDTO r = null;
        if(rhs instanceof FileNodeDTO)
        {
            r = (FileNodeDTO)rhs;
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
            if(this.id != r.id)
            {
                if(this.id == null || r.id == null || !this.id.equals(r.id))
                {
                    return false;
                }
            }
            if(this.name != r.name)
            {
                if(this.name == null || r.name == null || !this.name.equals(r.name))
                {
                    return false;
                }
            }
            if(this.pid != r.pid)
            {
                if(this.pid == null || r.pid == null || !this.pid.equals(r.pid))
                {
                    return false;
                }
            }
            if(this.typeId != r.typeId)
            {
                return false;
            }
            if(this.typeName != r.typeName)
            {
                if(this.typeName == null || r.typeName == null || !this.typeName.equals(r.typeName))
                {
                    return false;
                }
            }
            if(this.path != r.path)
            {
                if(this.path == null || r.path == null || !this.path.equals(r.path))
                {
                    return false;
                }
            }
            if(this.createTimeStamp != r.createTimeStamp)
            {
                return false;
            }
            if(this.createTimeText != r.createTimeText)
            {
                if(this.createTimeText == null || r.createTimeText == null || !this.createTimeText.equals(r.createTimeText))
                {
                    return false;
                }
            }
            if(this.lastModifyTimeStamp != r.lastModifyTimeStamp)
            {
                return false;
            }
            if(this.lastModifyTimeText != r.lastModifyTimeText)
            {
                if(this.lastModifyTimeText == null || r.lastModifyTimeText == null || !this.lastModifyTimeText.equals(r.lastModifyTimeText))
                {
                    return false;
                }
            }
            if(this.isReadOnly != r.isReadOnly)
            {
                return false;
            }
            if(this.fileLength != r.fileLength)
            {
                return false;
            }
            if(this.fileChecksum != r.fileChecksum)
            {
                if(this.fileChecksum == null || r.fileChecksum == null || !this.fileChecksum.equals(r.fileChecksum))
                {
                    return false;
                }
            }
            if(this.fileVersion != r.fileVersion)
            {
                if(this.fileVersion == null || r.fileVersion == null || !this.fileVersion.equals(r.fileVersion))
                {
                    return false;
                }
            }
            if(this.fileTypeId != r.fileTypeId)
            {
                return false;
            }
            if(this.fileTypeName != r.fileTypeName)
            {
                if(this.fileTypeName == null || r.fileTypeName == null || !this.fileTypeName.equals(r.fileTypeName))
                {
                    return false;
                }
            }
            if(this.syncModeId != r.syncModeId)
            {
                return false;
            }
            if(this.syncModeName != r.syncModeName)
            {
                if(this.syncModeName == null || r.syncModeName == null || !this.syncModeName.equals(r.syncModeName))
                {
                    return false;
                }
            }
            if(this.lastModifyAddress != r.lastModifyAddress)
            {
                if(this.lastModifyAddress == null || r.lastModifyAddress == null || !this.lastModifyAddress.equals(r.lastModifyAddress))
                {
                    return false;
                }
            }
            if(this.fileServerTypeId != r.fileServerTypeId)
            {
                if(this.fileServerTypeId == null || r.fileServerTypeId == null || !this.fileServerTypeId.equals(r.fileServerTypeId))
                {
                    return false;
                }
            }
            if(this.fileServerTypeName != r.fileServerTypeName)
            {
                if(this.fileServerTypeName == null || r.fileServerTypeName == null || !this.fileServerTypeName.equals(r.fileServerTypeName))
                {
                    return false;
                }
            }
            if(this.fileScope != r.fileScope)
            {
                if(this.fileScope == null || r.fileScope == null || !this.fileScope.equals(r.fileScope))
                {
                    return false;
                }
            }
            if(this.fileKey != r.fileKey)
            {
                if(this.fileKey == null || r.fileKey == null || !this.fileKey.equals(r.fileKey))
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
            if(this.ownerUserId != r.ownerUserId)
            {
                if(this.ownerUserId == null || r.ownerUserId == null || !this.ownerUserId.equals(r.ownerUserId))
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
            if(this.ownerName != r.ownerName)
            {
                if(this.ownerName == null || r.ownerName == null || !this.ownerName.equals(r.ownerName))
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
            if(this.taskMemberList != r.taskMemberList)
            {
                if(this.taskMemberList == null || r.taskMemberList == null || !this.taskMemberList.equals(r.taskMemberList))
                {
                    return false;
                }
            }
            if(this.historyList != r.historyList)
            {
                if(this.historyList == null || r.historyList == null || !this.historyList.equals(r.historyList))
                {
                    return false;
                }
            }
            if(this.isValid != r.isValid)
            {
                return false;
            }
            if(this.locking != r.locking)
            {
                return false;
            }
            if(this.localFile != r.localFile)
            {
                if(this.localFile == null || r.localFile == null || !this.localFile.equals(r.localFile))
                {
                    return false;
                }
            }
            if(this.creatorDutyId != r.creatorDutyId)
            {
                if(this.creatorDutyId == null || r.creatorDutyId == null || !this.creatorDutyId.equals(r.creatorDutyId))
                {
                    return false;
                }
            }
            if(this.creatorDutyName != r.creatorDutyName)
            {
                if(this.creatorDutyName == null || r.creatorDutyName == null || !this.creatorDutyName.equals(r.creatorDutyName))
                {
                    return false;
                }
            }
            if(this.lastModifyDutyId != r.lastModifyDutyId)
            {
                if(this.lastModifyDutyId == null || r.lastModifyDutyId == null || !this.lastModifyDutyId.equals(r.lastModifyDutyId))
                {
                    return false;
                }
            }
            if(this.lastModifyDutyName != r.lastModifyDutyName)
            {
                if(this.lastModifyDutyName == null || r.lastModifyDutyName == null || !this.lastModifyDutyName.equals(r.lastModifyDutyName))
                {
                    return false;
                }
            }
            if(this.organizationId != r.organizationId)
            {
                if(this.organizationId == null || r.organizationId == null || !this.organizationId.equals(r.organizationId))
                {
                    return false;
                }
            }
            if(this.organizationName != r.organizationName)
            {
                if(this.organizationName == null || r.organizationName == null || !this.organizationName.equals(r.organizationName))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::FileNodeDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, basic);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, id);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, name);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, pid);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, typeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, typeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, path);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, createTimeStamp);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, createTimeText);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyTimeStamp);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyTimeText);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, isReadOnly);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileLength);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileChecksum);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileVersion);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileTypeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileTypeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyAddress);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileServerTypeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileServerTypeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileScope);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileKey);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, scope);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, key);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerUserId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, ownerName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, issueId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, issueName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, projectId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, projectName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, taskMemberList);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, historyList);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, isValid);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, locking);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, localFile);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorDutyName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyDutyName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, organizationId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, organizationName);
        return h_;
    }

    public FileNodeDTO clone()
    {
        FileNodeDTO c = null;
        try
        {
            c = (FileNodeDTO)super.clone();
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
        ostr.writeString(this.id);
        ostr.writeString(this.name);
        ostr.writeString(this.pid);
        ostr.writeShort(this.typeId);
        ostr.writeString(this.typeName);
        ostr.writeString(this.path);
        ostr.writeLong(this.createTimeStamp);
        ostr.writeString(this.createTimeText);
        ostr.writeLong(this.lastModifyTimeStamp);
        ostr.writeString(this.lastModifyTimeText);
        ostr.writeBool(this.isReadOnly);
        ostr.writeLong(this.fileLength);
        ostr.writeString(this.fileChecksum);
        ostr.writeString(this.fileVersion);
        ostr.writeShort(this.fileTypeId);
        ostr.writeString(this.fileTypeName);
        ostr.writeShort(this.syncModeId);
        ostr.writeString(this.syncModeName);
        ostr.writeString(this.lastModifyAddress);
        ostr.writeString(this.fileServerTypeId);
        ostr.writeString(this.fileServerTypeName);
        ostr.writeString(this.fileScope);
        ostr.writeString(this.fileKey);
        ostr.writeString(this.scope);
        ostr.writeString(this.key);
        ostr.writeString(this.ownerUserId);
        ostr.writeString(this.ownerDutyId);
        ostr.writeString(this.ownerName);
        ostr.writeString(this.issueId);
        ostr.writeString(this.issueName);
        ostr.writeString(this.taskId);
        ostr.writeString(this.taskName);
        ostr.writeString(this.projectId);
        ostr.writeString(this.projectName);
        com.maoding.Project.zeroc.MemberListHelper.write(ostr, this.taskMemberList);
        HistoryListHelper.write(ostr, this.historyList);
        ostr.writeBool(this.isValid);
        ostr.writeBool(this.locking);
        ostr.writeString(this.localFile);
        ostr.writeString(this.creatorDutyId);
        ostr.writeString(this.creatorDutyName);
        ostr.writeString(this.lastModifyDutyId);
        ostr.writeString(this.lastModifyDutyName);
        ostr.writeString(this.organizationId);
        ostr.writeString(this.organizationName);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.basic = SimpleNodeDTO.ice_read(istr);
        this.id = istr.readString();
        this.name = istr.readString();
        this.pid = istr.readString();
        this.typeId = istr.readShort();
        this.typeName = istr.readString();
        this.path = istr.readString();
        this.createTimeStamp = istr.readLong();
        this.createTimeText = istr.readString();
        this.lastModifyTimeStamp = istr.readLong();
        this.lastModifyTimeText = istr.readString();
        this.isReadOnly = istr.readBool();
        this.fileLength = istr.readLong();
        this.fileChecksum = istr.readString();
        this.fileVersion = istr.readString();
        this.fileTypeId = istr.readShort();
        this.fileTypeName = istr.readString();
        this.syncModeId = istr.readShort();
        this.syncModeName = istr.readString();
        this.lastModifyAddress = istr.readString();
        this.fileServerTypeId = istr.readString();
        this.fileServerTypeName = istr.readString();
        this.fileScope = istr.readString();
        this.fileKey = istr.readString();
        this.scope = istr.readString();
        this.key = istr.readString();
        this.ownerUserId = istr.readString();
        this.ownerDutyId = istr.readString();
        this.ownerName = istr.readString();
        this.issueId = istr.readString();
        this.issueName = istr.readString();
        this.taskId = istr.readString();
        this.taskName = istr.readString();
        this.projectId = istr.readString();
        this.projectName = istr.readString();
        this.taskMemberList = com.maoding.Project.zeroc.MemberListHelper.read(istr);
        this.historyList = HistoryListHelper.read(istr);
        this.isValid = istr.readBool();
        this.locking = istr.readBool();
        this.localFile = istr.readString();
        this.creatorDutyId = istr.readString();
        this.creatorDutyName = istr.readString();
        this.lastModifyDutyId = istr.readString();
        this.lastModifyDutyName = istr.readString();
        this.organizationId = istr.readString();
        this.organizationName = istr.readString();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, FileNodeDTO v)
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

    static public FileNodeDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        FileNodeDTO v = new FileNodeDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<FileNodeDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, FileNodeDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<FileNodeDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(FileNodeDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final FileNodeDTO _nullMarshalValue = new FileNodeDTO();

    public static final long serialVersionUID = -1648008554L;
}
