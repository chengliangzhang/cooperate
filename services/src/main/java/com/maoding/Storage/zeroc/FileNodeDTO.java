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
    public String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String pid;

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public short typeId;

    public short getTypeId()
    {
        return typeId;
    }

    public void setTypeId(short typeId)
    {
        this.typeId = typeId;
    }

    public String typeName;

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String path;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public long createTimeStamp;

    public long getCreateTimeStamp()
    {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp)
    {
        this.createTimeStamp = createTimeStamp;
    }

    public String createTimeText;

    public String getCreateTimeText()
    {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText)
    {
        this.createTimeText = createTimeText;
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

    public String lastModifyTimeText;

    public String getLastModifyTimeText()
    {
        return lastModifyTimeText;
    }

    public void setLastModifyTimeText(String lastModifyTimeText)
    {
        this.lastModifyTimeText = lastModifyTimeText;
    }

    public boolean isReadOnly;

    public boolean getIsReadOnly()
    {
        return isReadOnly;
    }

    public void setIsReadOnly(boolean isReadOnly)
    {
        this.isReadOnly = isReadOnly;
    }

    public boolean isIsReadOnly()
    {
        return isReadOnly;
    }

    public long fileLength;

    public long getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    public String owner_duty_id;

    public String getOwner_duty_id()
    {
        return owner_duty_id;
    }

    public void setOwner_duty_id(String owner_duty_id)
    {
        this.owner_duty_id = owner_duty_id;
    }

    public String owner_name;

    public String getOwner_name()
    {
        return owner_name;
    }

    public void setOwner_name(String owner_name)
    {
        this.owner_name = owner_name;
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

    public String organizationId;

    public String getOrganizationId()
    {
        return organizationId;
    }

    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    public String organizationName;

    public String getOrganizationName()
    {
        return organizationName;
    }

    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    public short syncModeId;

    public short getSyncModeId()
    {
        return syncModeId;
    }

    public void setSyncModeId(short syncModeId)
    {
        this.syncModeId = syncModeId;
    }

    public String syncModeName;

    public String getSyncModeName()
    {
        return syncModeName;
    }

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

    public boolean isValid;

    public boolean getIsValid()
    {
        return isValid;
    }

    public void setIsValid(boolean isValid)
    {
        this.isValid = isValid;
    }

    public boolean isIsValid()
    {
        return isValid;
    }

    public boolean locking;

    public boolean getLocking()
    {
        return locking;
    }

    public void setLocking(boolean locking)
    {
        this.locking = locking;
    }

    public boolean isLocking()
    {
        return locking;
    }

    public String localFile;

    public String getLocalFile()
    {
        return localFile;
    }

    public void setLocalFile(String localFile)
    {
        this.localFile = localFile;
    }

    public String creatorDutyId;

    public String getCreatorDutyId()
    {
        return creatorDutyId;
    }

    public void setCreatorDutyId(String creatorDutyId)
    {
        this.creatorDutyId = creatorDutyId;
    }

    public String creatorDutyName;

    public String getCreatorDutyName()
    {
        return creatorDutyName;
    }

    public void setCreatorDutyName(String creatorDutyName)
    {
        this.creatorDutyName = creatorDutyName;
    }

    public String lastModifyDutyId;

    public String getLastModifyDutyId()
    {
        return lastModifyDutyId;
    }

    public void setLastModifyDutyId(String lastModifyDutyId)
    {
        this.lastModifyDutyId = lastModifyDutyId;
    }

    public String lastModifyDutyName;

    public String getLastModifyDutyName()
    {
        return lastModifyDutyName;
    }

    public void setLastModifyDutyName(String lastModifyDutyName)
    {
        this.lastModifyDutyName = lastModifyDutyName;
    }

    public FileNodeDTO()
    {
        this.id = "";
        this.name = "";
        this.pid = "";
        this.typeName = "";
        this.path = "";
        this.createTimeText = "";
        this.lastModifyTimeText = "";
        this.owner_duty_id = "";
        this.owner_name = "";
        this.fileChecksum = "";
        this.fileVersion = "";
        this.fileTypeName = "";
        this.organizationId = "";
        this.organizationName = "";
        this.syncModeName = "";
        this.lastModifyAddress = "";
        this.fileServerTypeId = "";
        this.fileServerTypeName = "";
        this.fileScope = "";
        this.fileKey = "";
        this.localFile = "";
        this.creatorDutyId = "";
        this.creatorDutyName = "";
        this.lastModifyDutyId = "";
        this.lastModifyDutyName = "";
    }

    public FileNodeDTO(String id, String name, String pid, short typeId, String typeName, String path, long createTimeStamp, String createTimeText, long lastModifyTimeStamp, String lastModifyTimeText, boolean isReadOnly, long fileLength, String owner_duty_id, String owner_name, String fileChecksum, String fileVersion, short fileTypeId, String fileTypeName, String organizationId, String organizationName, short syncModeId, String syncModeName, String lastModifyAddress, String fileServerTypeId, String fileServerTypeName, String fileScope, String fileKey, boolean isValid, boolean locking, String localFile, String creatorDutyId, String creatorDutyName, String lastModifyDutyId, String lastModifyDutyName)
    {
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
        this.owner_duty_id = owner_duty_id;
        this.owner_name = owner_name;
        this.fileChecksum = fileChecksum;
        this.fileVersion = fileVersion;
        this.fileTypeId = fileTypeId;
        this.fileTypeName = fileTypeName;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.syncModeId = syncModeId;
        this.syncModeName = syncModeName;
        this.lastModifyAddress = lastModifyAddress;
        this.fileServerTypeId = fileServerTypeId;
        this.fileServerTypeName = fileServerTypeName;
        this.fileScope = fileScope;
        this.fileKey = fileKey;
        this.isValid = isValid;
        this.locking = locking;
        this.localFile = localFile;
        this.creatorDutyId = creatorDutyId;
        this.creatorDutyName = creatorDutyName;
        this.lastModifyDutyId = lastModifyDutyId;
        this.lastModifyDutyName = lastModifyDutyName;
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
            if(this.owner_duty_id != r.owner_duty_id)
            {
                if(this.owner_duty_id == null || r.owner_duty_id == null || !this.owner_duty_id.equals(r.owner_duty_id))
                {
                    return false;
                }
            }
            if(this.owner_name != r.owner_name)
            {
                if(this.owner_name == null || r.owner_name == null || !this.owner_name.equals(r.owner_name))
                {
                    return false;
                }
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

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::FileNodeDTO");
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, owner_duty_id);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, owner_name);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileChecksum);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileVersion);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileTypeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileTypeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, organizationId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, organizationName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyAddress);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileServerTypeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileServerTypeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileScope);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileKey);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, isValid);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, locking);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, localFile);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorDutyName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyDutyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyDutyName);
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
        ostr.writeString(this.owner_duty_id);
        ostr.writeString(this.owner_name);
        ostr.writeString(this.fileChecksum);
        ostr.writeString(this.fileVersion);
        ostr.writeShort(this.fileTypeId);
        ostr.writeString(this.fileTypeName);
        ostr.writeString(this.organizationId);
        ostr.writeString(this.organizationName);
        ostr.writeShort(this.syncModeId);
        ostr.writeString(this.syncModeName);
        ostr.writeString(this.lastModifyAddress);
        ostr.writeString(this.fileServerTypeId);
        ostr.writeString(this.fileServerTypeName);
        ostr.writeString(this.fileScope);
        ostr.writeString(this.fileKey);
        ostr.writeBool(this.isValid);
        ostr.writeBool(this.locking);
        ostr.writeString(this.localFile);
        ostr.writeString(this.creatorDutyId);
        ostr.writeString(this.creatorDutyName);
        ostr.writeString(this.lastModifyDutyId);
        ostr.writeString(this.lastModifyDutyName);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
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
        this.owner_duty_id = istr.readString();
        this.owner_name = istr.readString();
        this.fileChecksum = istr.readString();
        this.fileVersion = istr.readString();
        this.fileTypeId = istr.readShort();
        this.fileTypeName = istr.readString();
        this.organizationId = istr.readString();
        this.organizationName = istr.readString();
        this.syncModeId = istr.readShort();
        this.syncModeName = istr.readString();
        this.lastModifyAddress = istr.readString();
        this.fileServerTypeId = istr.readString();
        this.fileServerTypeName = istr.readString();
        this.fileScope = istr.readString();
        this.fileKey = istr.readString();
        this.isValid = istr.readBool();
        this.locking = istr.readBool();
        this.localFile = istr.readString();
        this.creatorDutyId = istr.readString();
        this.creatorDutyName = istr.readString();
        this.lastModifyDutyId = istr.readString();
        this.lastModifyDutyName = istr.readString();
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

    public static final long serialVersionUID = 1148621190L;
}
