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

public interface StorageService extends com.zeroc.Ice.Object
{
    NodeDTO getNodeInfo(CooperationQueryDTO query, com.zeroc.Ice.Current current);

    CooperateDirDTO getCooperateDirInfo(CooperationQueryDTO query, com.zeroc.Ice.Current current);

    boolean changeNodeInfo(NodeModifyRequestDTO request, String nodeId, com.zeroc.Ice.Current current);

    boolean modifyFileInfo(CooperateFileDTO fileInfo, com.zeroc.Ice.Current current);

    com.maoding.FileServer.zeroc.FileRequestDTO requestUpload(CooperateFileDTO fileInfo, int mode, com.zeroc.Ice.Current current);

    com.maoding.FileServer.zeroc.FileRequestDTO requestDownload(CooperateFileDTO fileInfo, int mode, com.zeroc.Ice.Current current);

    com.maoding.FileServer.zeroc.FileRequestDTO requestDownloadFromLast(CooperateFileDTO fileInfo, int mode, com.zeroc.Ice.Current current);

    CooperateFileDTO uploadCallback(java.util.Map<java.lang.String, java.lang.String> params, com.zeroc.Ice.Current current);

    void downloadCallback(java.util.Map<java.lang.String, java.lang.String> params, com.zeroc.Ice.Current current);

    void finishUpload(com.maoding.FileServer.zeroc.FileRequestDTO request, boolean succeeded, com.zeroc.Ice.Current current);

    void finishDownload(com.maoding.FileServer.zeroc.FileRequestDTO request, boolean succeeded, com.zeroc.Ice.Current current);

    boolean replaceFile(CooperateFileDTO fileInfo, com.maoding.FileServer.zeroc.FileDTO fileDTO, com.zeroc.Ice.Current current);

    boolean deleteFile(CooperateFileDTO fileInfo, com.zeroc.Ice.Current current);

    String createDirectory(CreateNodeRequestDTO request, com.zeroc.Ice.Current current);

    boolean deleteDirectory(String nodeId, boolean force, com.zeroc.Ice.Current current);

    String createFile(CreateNodeRequestDTO request, com.zeroc.Ice.Current current);

    CooperateFileDTO duplicateFile(CooperateFileDTO fileInfo, String path, com.zeroc.Ice.Current current);

    CooperateFileDTO createFileLink(CooperateFileDTO fileInfo, String path, com.zeroc.Ice.Current current);

    boolean duplicateDirectory(String path, String parent, com.zeroc.Ice.Current current);

    java.util.List<CooperateFileDTO> listFileLink(com.maoding.FileServer.zeroc.FileDTO fileDTO, com.zeroc.Ice.Current current);

    boolean restoreFile(CooperateFileDTO fileInfo, com.zeroc.Ice.Current current);

    boolean restoreDirectory(String path, com.zeroc.Ice.Current current);

    boolean lockFile(String fileId, String address, com.zeroc.Ice.Current current);

    boolean unlockFile(String fileId, com.zeroc.Ice.Current current);

    boolean isFileLocking(String fileId, com.zeroc.Ice.Current current);

    long getFree(CooperationQueryDTO query, com.zeroc.Ice.Current current);

    CooperateFileDTO getFileInfo(String nodeId, com.zeroc.Ice.Current current);

    int getLinkCount(com.maoding.FileServer.zeroc.FileDTO fileDTO, com.zeroc.Ice.Current current);

    String createVersion(CooperateFileDTO fileInfo, String version, com.zeroc.Ice.Current current);

    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::zeroc::StorageService"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::zeroc::StorageService";
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getNodeInfo(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperationQueryDTO iceP_query;
        iceP_query = CooperationQueryDTO.ice_read(istr);
        inS.endReadParams();
        NodeDTO ret = obj.getNodeInfo(iceP_query, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        NodeDTO.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getCooperateDirInfo(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperationQueryDTO iceP_query;
        iceP_query = CooperationQueryDTO.ice_read(istr);
        inS.endReadParams();
        CooperateDirDTO ret = obj.getCooperateDirInfo(iceP_query, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateDirDTO.ice_write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_changeNodeInfo(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        NodeModifyRequestDTO iceP_request;
        String iceP_nodeId;
        iceP_request = NodeModifyRequestDTO.ice_read(istr);
        iceP_nodeId = istr.readString();
        inS.endReadParams();
        boolean ret = obj.changeNodeInfo(iceP_request, iceP_nodeId, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_modifyFileInfo(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        istr.readPendingValues();
        inS.endReadParams();
        boolean ret = obj.modifyFileInfo(iceP_fileInfo, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_requestUpload(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        int iceP_mode;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_mode = istr.readInt();
        istr.readPendingValues();
        inS.endReadParams();
        com.maoding.FileServer.zeroc.FileRequestDTO ret = obj.requestUpload(iceP_fileInfo, iceP_mode, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        com.maoding.FileServer.zeroc.FileRequestDTO.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_requestDownload(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        int iceP_mode;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_mode = istr.readInt();
        istr.readPendingValues();
        inS.endReadParams();
        com.maoding.FileServer.zeroc.FileRequestDTO ret = obj.requestDownload(iceP_fileInfo, iceP_mode, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        com.maoding.FileServer.zeroc.FileRequestDTO.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_requestDownloadFromLast(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        int iceP_mode;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_mode = istr.readInt();
        istr.readPendingValues();
        inS.endReadParams();
        com.maoding.FileServer.zeroc.FileRequestDTO ret = obj.requestDownloadFromLast(iceP_fileInfo, iceP_mode, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        com.maoding.FileServer.zeroc.FileRequestDTO.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_uploadCallback(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        java.util.Map<java.lang.String, java.lang.String> iceP_params;
        iceP_params = com.maoding.Common.zeroc.MapHelper.read(istr);
        inS.endReadParams();
        CooperateFileDTO ret = obj.uploadCallback(iceP_params, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateFileDTO.ice_write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_downloadCallback(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        java.util.Map<java.lang.String, java.lang.String> iceP_params;
        iceP_params = com.maoding.Common.zeroc.MapHelper.read(istr);
        inS.endReadParams();
        obj.downloadCallback(iceP_params, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_finishUpload(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        com.maoding.FileServer.zeroc.FileRequestDTO iceP_request;
        boolean iceP_succeeded;
        iceP_request = com.maoding.FileServer.zeroc.FileRequestDTO.ice_read(istr);
        iceP_succeeded = istr.readBool();
        inS.endReadParams();
        obj.finishUpload(iceP_request, iceP_succeeded, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_finishDownload(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        com.maoding.FileServer.zeroc.FileRequestDTO iceP_request;
        boolean iceP_succeeded;
        iceP_request = com.maoding.FileServer.zeroc.FileRequestDTO.ice_read(istr);
        iceP_succeeded = istr.readBool();
        inS.endReadParams();
        obj.finishDownload(iceP_request, iceP_succeeded, current);
        return inS.setResult(inS.writeEmptyParams());
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_replaceFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        com.maoding.FileServer.zeroc.FileDTO iceP_fileDTO;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_fileDTO = com.maoding.FileServer.zeroc.FileDTO.ice_read(istr);
        istr.readPendingValues();
        inS.endReadParams();
        boolean ret = obj.replaceFile(iceP_fileInfo, iceP_fileDTO, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_deleteFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        istr.readPendingValues();
        inS.endReadParams();
        boolean ret = obj.deleteFile(iceP_fileInfo, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_createDirectory(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CreateNodeRequestDTO iceP_request;
        iceP_request = CreateNodeRequestDTO.ice_read(istr);
        inS.endReadParams();
        String ret = obj.createDirectory(iceP_request, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_deleteDirectory(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_nodeId;
        boolean iceP_force;
        iceP_nodeId = istr.readString();
        iceP_force = istr.readBool();
        inS.endReadParams();
        boolean ret = obj.deleteDirectory(iceP_nodeId, iceP_force, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_createFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CreateNodeRequestDTO iceP_request;
        iceP_request = CreateNodeRequestDTO.ice_read(istr);
        inS.endReadParams();
        String ret = obj.createFile(iceP_request, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_duplicateFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        String iceP_path;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_path = istr.readString();
        istr.readPendingValues();
        inS.endReadParams();
        CooperateFileDTO ret = obj.duplicateFile(iceP_fileInfo, iceP_path, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateFileDTO.ice_write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_createFileLink(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        String iceP_path;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_path = istr.readString();
        istr.readPendingValues();
        inS.endReadParams();
        CooperateFileDTO ret = obj.createFileLink(iceP_fileInfo, iceP_path, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateFileDTO.ice_write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_duplicateDirectory(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_path;
        String iceP_parent;
        iceP_path = istr.readString();
        iceP_parent = istr.readString();
        inS.endReadParams();
        boolean ret = obj.duplicateDirectory(iceP_path, iceP_parent, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_listFileLink(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        com.maoding.FileServer.zeroc.FileDTO iceP_fileDTO;
        iceP_fileDTO = com.maoding.FileServer.zeroc.FileDTO.ice_read(istr);
        inS.endReadParams();
        java.util.List<CooperateFileDTO> ret = obj.listFileLink(iceP_fileDTO, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateFileListHelper.write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_restoreFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        istr.readPendingValues();
        inS.endReadParams();
        boolean ret = obj.restoreFile(iceP_fileInfo, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_restoreDirectory(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_path;
        iceP_path = istr.readString();
        inS.endReadParams();
        boolean ret = obj.restoreDirectory(iceP_path, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_lockFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_fileId;
        String iceP_address;
        iceP_fileId = istr.readString();
        iceP_address = istr.readString();
        inS.endReadParams();
        boolean ret = obj.lockFile(iceP_fileId, iceP_address, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_unlockFile(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_fileId;
        iceP_fileId = istr.readString();
        inS.endReadParams();
        boolean ret = obj.unlockFile(iceP_fileId, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_isFileLocking(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_fileId;
        iceP_fileId = istr.readString();
        inS.endReadParams();
        boolean ret = obj.isFileLocking(iceP_fileId, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeBool(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getFree(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperationQueryDTO iceP_query;
        iceP_query = CooperationQueryDTO.ice_read(istr);
        inS.endReadParams();
        long ret = obj.getFree(iceP_query, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeLong(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getFileInfo(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        String iceP_nodeId;
        iceP_nodeId = istr.readString();
        inS.endReadParams();
        CooperateFileDTO ret = obj.getFileInfo(iceP_nodeId, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        CooperateFileDTO.ice_write(ostr, ret);
        ostr.writePendingValues();
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getLinkCount(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        com.maoding.FileServer.zeroc.FileDTO iceP_fileDTO;
        iceP_fileDTO = com.maoding.FileServer.zeroc.FileDTO.ice_read(istr);
        inS.endReadParams();
        int ret = obj.getLinkCount(iceP_fileDTO, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeInt(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_createVersion(StorageService obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        CooperateFileDTO iceP_fileInfo;
        String iceP_version;
        iceP_fileInfo = CooperateFileDTO.ice_read(istr);
        iceP_version = istr.readString();
        istr.readPendingValues();
        inS.endReadParams();
        String ret = obj.createVersion(iceP_fileInfo, iceP_version, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ostr.writeString(ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    final static String[] _iceOps =
    {
        "changeNodeInfo",
        "createDirectory",
        "createFile",
        "createFileLink",
        "createVersion",
        "deleteDirectory",
        "deleteFile",
        "downloadCallback",
        "duplicateDirectory",
        "duplicateFile",
        "finishDownload",
        "finishUpload",
        "getCooperateDirInfo",
        "getFileInfo",
        "getFree",
        "getLinkCount",
        "getNodeInfo",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "isFileLocking",
        "listFileLink",
        "lockFile",
        "modifyFileInfo",
        "replaceFile",
        "requestDownload",
        "requestDownloadFromLast",
        "requestUpload",
        "restoreDirectory",
        "restoreFile",
        "unlockFile",
        "uploadCallback"
    };

    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_changeNodeInfo(this, in, current);
            }
            case 1:
            {
                return _iceD_createDirectory(this, in, current);
            }
            case 2:
            {
                return _iceD_createFile(this, in, current);
            }
            case 3:
            {
                return _iceD_createFileLink(this, in, current);
            }
            case 4:
            {
                return _iceD_createVersion(this, in, current);
            }
            case 5:
            {
                return _iceD_deleteDirectory(this, in, current);
            }
            case 6:
            {
                return _iceD_deleteFile(this, in, current);
            }
            case 7:
            {
                return _iceD_downloadCallback(this, in, current);
            }
            case 8:
            {
                return _iceD_duplicateDirectory(this, in, current);
            }
            case 9:
            {
                return _iceD_duplicateFile(this, in, current);
            }
            case 10:
            {
                return _iceD_finishDownload(this, in, current);
            }
            case 11:
            {
                return _iceD_finishUpload(this, in, current);
            }
            case 12:
            {
                return _iceD_getCooperateDirInfo(this, in, current);
            }
            case 13:
            {
                return _iceD_getFileInfo(this, in, current);
            }
            case 14:
            {
                return _iceD_getFree(this, in, current);
            }
            case 15:
            {
                return _iceD_getLinkCount(this, in, current);
            }
            case 16:
            {
                return _iceD_getNodeInfo(this, in, current);
            }
            case 17:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 18:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 19:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 20:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 21:
            {
                return _iceD_isFileLocking(this, in, current);
            }
            case 22:
            {
                return _iceD_listFileLink(this, in, current);
            }
            case 23:
            {
                return _iceD_lockFile(this, in, current);
            }
            case 24:
            {
                return _iceD_modifyFileInfo(this, in, current);
            }
            case 25:
            {
                return _iceD_replaceFile(this, in, current);
            }
            case 26:
            {
                return _iceD_requestDownload(this, in, current);
            }
            case 27:
            {
                return _iceD_requestDownloadFromLast(this, in, current);
            }
            case 28:
            {
                return _iceD_requestUpload(this, in, current);
            }
            case 29:
            {
                return _iceD_restoreDirectory(this, in, current);
            }
            case 30:
            {
                return _iceD_restoreFile(this, in, current);
            }
            case 31:
            {
                return _iceD_unlockFile(this, in, current);
            }
            case 32:
            {
                return _iceD_uploadCallback(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
