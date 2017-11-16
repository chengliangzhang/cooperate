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

public class CooperateFileDTO implements java.lang.Cloneable,
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

    public String storageId;

    public String getStorageId()
    {
        return storageId;
    }

    public void setStorageId(String storageId)
    {
        this.storageId = storageId;
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

    public long fileLength;

    public long getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    public String checksum;

    public String getChecksum()
    {
        return checksum;
    }

    public void setChecksum(String checksum)
    {
        this.checksum = checksum;
    }

    public String version;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String specialtyId;

    public String getSpecialtyId()
    {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId)
    {
        this.specialtyId = specialtyId;
    }

    public String specialtyName;

    public String getSpecialtyName()
    {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName)
    {
        this.specialtyName = specialtyName;
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

    public int typeId;

    public int getTypeId()
    {
        return typeId;
    }

    public void setTypeId(int typeId)
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

    public String creatorPostId;

    public String getCreatorPostId()
    {
        return creatorPostId;
    }

    public void setCreatorPostId(String creatorPostId)
    {
        this.creatorPostId = creatorPostId;
    }

    public String creatorPostName;

    public String getCreatorPostName()
    {
        return creatorPostName;
    }

    public void setCreatorPostName(String creatorPostName)
    {
        this.creatorPostName = creatorPostName;
    }

    public java.util.Date createTime;

    public java.util.Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime)
    {
        this.createTime = createTime;
    }

    public String lastModifyPostId;

    public String getLastModifyPostId()
    {
        return lastModifyPostId;
    }

    public void setLastModifyPostId(String lastModifyPostId)
    {
        this.lastModifyPostId = lastModifyPostId;
    }

    public String lastModifyPostName;

    public String getLastModifyPostName()
    {
        return lastModifyPostName;
    }

    public void setLastModifyPostName(String lastModifyPostName)
    {
        this.lastModifyPostName = lastModifyPostName;
    }

    public java.util.Date lastModifyTime;

    public java.util.Date getLastModifyTime()
    {
        return lastModifyTime;
    }

    public void setLastModifyTime(java.util.Date lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
    }

    public int referenceFileCount;

    public int getReferenceFileCount()
    {
        return referenceFileCount;
    }

    public void setReferenceFileCount(int referenceFileCount)
    {
        this.referenceFileCount = referenceFileCount;
    }

    public java.util.List<CooperateFileNodeDTO> referenceFileList;

    public java.util.List<CooperateFileNodeDTO> getReferenceFileList()
    {
        return referenceFileList;
    }

    public void setReferenceFileList(java.util.List<CooperateFileNodeDTO> referenceFileList)
    {
        this.referenceFileList = referenceFileList;
    }

    public CooperateFileDTO()
    {
        this.id = "";
        this.storageId = "";
        this.name = "";
        this.checksum = "";
        this.version = "";
        this.specialtyId = "";
        this.specialtyName = "";
        this.lastModifyAddress = "";
        this.syncModeName = "";
        this.typeName = "";
        this.localFile = "";
        this.creatorPostId = "";
        this.creatorPostName = "";
        this.lastModifyPostId = "";
        this.lastModifyPostName = "";
    }

    public CooperateFileDTO(String id, String storageId, String name, long fileLength, String checksum, String version, String specialtyId, String specialtyName, String lastModifyAddress, short syncModeId, String syncModeName, int typeId, String typeName, boolean locking, String localFile, String creatorPostId, String creatorPostName, java.util.Date createTime, String lastModifyPostId, String lastModifyPostName, java.util.Date lastModifyTime, int referenceFileCount, java.util.List<CooperateFileNodeDTO> referenceFileList)
    {
        this.id = id;
        this.storageId = storageId;
        this.name = name;
        this.fileLength = fileLength;
        this.checksum = checksum;
        this.version = version;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
        this.lastModifyAddress = lastModifyAddress;
        this.syncModeId = syncModeId;
        this.syncModeName = syncModeName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.locking = locking;
        this.localFile = localFile;
        this.creatorPostId = creatorPostId;
        this.creatorPostName = creatorPostName;
        this.createTime = createTime;
        this.lastModifyPostId = lastModifyPostId;
        this.lastModifyPostName = lastModifyPostName;
        this.lastModifyTime = lastModifyTime;
        this.referenceFileCount = referenceFileCount;
        this.referenceFileList = referenceFileList;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        CooperateFileDTO r = null;
        if(rhs instanceof CooperateFileDTO)
        {
            r = (CooperateFileDTO)rhs;
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
            if(this.storageId != r.storageId)
            {
                if(this.storageId == null || r.storageId == null || !this.storageId.equals(r.storageId))
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
            if(this.fileLength != r.fileLength)
            {
                return false;
            }
            if(this.checksum != r.checksum)
            {
                if(this.checksum == null || r.checksum == null || !this.checksum.equals(r.checksum))
                {
                    return false;
                }
            }
            if(this.version != r.version)
            {
                if(this.version == null || r.version == null || !this.version.equals(r.version))
                {
                    return false;
                }
            }
            if(this.specialtyId != r.specialtyId)
            {
                if(this.specialtyId == null || r.specialtyId == null || !this.specialtyId.equals(r.specialtyId))
                {
                    return false;
                }
            }
            if(this.specialtyName != r.specialtyName)
            {
                if(this.specialtyName == null || r.specialtyName == null || !this.specialtyName.equals(r.specialtyName))
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
            if(this.creatorPostId != r.creatorPostId)
            {
                if(this.creatorPostId == null || r.creatorPostId == null || !this.creatorPostId.equals(r.creatorPostId))
                {
                    return false;
                }
            }
            if(this.creatorPostName != r.creatorPostName)
            {
                if(this.creatorPostName == null || r.creatorPostName == null || !this.creatorPostName.equals(r.creatorPostName))
                {
                    return false;
                }
            }
            if(this.createTime != r.createTime)
            {
                if(this.createTime == null || r.createTime == null || !this.createTime.equals(r.createTime))
                {
                    return false;
                }
            }
            if(this.lastModifyPostId != r.lastModifyPostId)
            {
                if(this.lastModifyPostId == null || r.lastModifyPostId == null || !this.lastModifyPostId.equals(r.lastModifyPostId))
                {
                    return false;
                }
            }
            if(this.lastModifyPostName != r.lastModifyPostName)
            {
                if(this.lastModifyPostName == null || r.lastModifyPostName == null || !this.lastModifyPostName.equals(r.lastModifyPostName))
                {
                    return false;
                }
            }
            if(this.lastModifyTime != r.lastModifyTime)
            {
                if(this.lastModifyTime == null || r.lastModifyTime == null || !this.lastModifyTime.equals(r.lastModifyTime))
                {
                    return false;
                }
            }
            if(this.referenceFileCount != r.referenceFileCount)
            {
                return false;
            }
            if(this.referenceFileList != r.referenceFileList)
            {
                if(this.referenceFileList == null || r.referenceFileList == null || !this.referenceFileList.equals(r.referenceFileList))
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
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::zeroc::CooperateFileDTO");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, id);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, storageId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, name);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, fileLength);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, checksum);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, version);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, specialtyId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, specialtyName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyAddress);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, syncModeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, typeId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, typeName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, locking);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, localFile);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorPostId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, creatorPostName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, createTime);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyPostId);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyPostName);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, lastModifyTime);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, referenceFileCount);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, referenceFileList);
        return h_;
    }

    public CooperateFileDTO clone()
    {
        CooperateFileDTO c = null;
        try
        {
            c = (CooperateFileDTO)super.clone();
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
        ostr.writeString(this.storageId);
        ostr.writeString(this.name);
        ostr.writeLong(this.fileLength);
        ostr.writeString(this.checksum);
        ostr.writeString(this.version);
        ostr.writeString(this.specialtyId);
        ostr.writeString(this.specialtyName);
        ostr.writeString(this.lastModifyAddress);
        ostr.writeShort(this.syncModeId);
        ostr.writeString(this.syncModeName);
        ostr.writeInt(this.typeId);
        ostr.writeString(this.typeName);
        ostr.writeBool(this.locking);
        ostr.writeString(this.localFile);
        ostr.writeString(this.creatorPostId);
        ostr.writeString(this.creatorPostName);
        ostr.writeSerializable(this.createTime);
        ostr.writeString(this.lastModifyPostId);
        ostr.writeString(this.lastModifyPostName);
        ostr.writeSerializable(this.lastModifyTime);
        ostr.writeInt(this.referenceFileCount);
        CooperateRelatedFileListHelper.write(ostr, this.referenceFileList);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.id = istr.readString();
        this.storageId = istr.readString();
        this.name = istr.readString();
        this.fileLength = istr.readLong();
        this.checksum = istr.readString();
        this.version = istr.readString();
        this.specialtyId = istr.readString();
        this.specialtyName = istr.readString();
        this.lastModifyAddress = istr.readString();
        this.syncModeId = istr.readShort();
        this.syncModeName = istr.readString();
        this.typeId = istr.readInt();
        this.typeName = istr.readString();
        this.locking = istr.readBool();
        this.localFile = istr.readString();
        this.creatorPostId = istr.readString();
        this.creatorPostName = istr.readString();
        this.createTime = istr.readSerializable(java.util.Date.class);
        this.lastModifyPostId = istr.readString();
        this.lastModifyPostName = istr.readString();
        this.lastModifyTime = istr.readSerializable(java.util.Date.class);
        this.referenceFileCount = istr.readInt();
        this.referenceFileList = CooperateRelatedFileListHelper.read(istr);
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, CooperateFileDTO v)
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

    static public CooperateFileDTO ice_read(com.zeroc.Ice.InputStream istr)
    {
        CooperateFileDTO v = new CooperateFileDTO();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<CooperateFileDTO> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, CooperateFileDTO v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<CooperateFileDTO> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(CooperateFileDTO.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final CooperateFileDTO _nullMarshalValue = new CooperateFileDTO();

    public static final long serialVersionUID = -62817270L;
}
