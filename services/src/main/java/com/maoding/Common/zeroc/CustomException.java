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
// Generated from file `Common.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.maoding.Common.zeroc;

public class CustomException extends com.zeroc.Ice.UserException
{
    public CustomException()
    {
        this.code = com.maoding.Common.zeroc.ErrorCode.Unknown;
        this.msg = "\u672a\u5b9a\u4e49\u5f02\u5e38";
    }

    public CustomException(Throwable cause)
    {
        super(cause);
        this.code = com.maoding.Common.zeroc.ErrorCode.Unknown;
        this.msg = "\u672a\u5b9a\u4e49\u5f02\u5e38";
    }

    public CustomException(ErrorCode code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public CustomException(ErrorCode code, String msg, Throwable cause)
    {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public String ice_id()
    {
        return "::zeroc::CustomException";
    }

    public ErrorCode code;

    public ErrorCode getCode()
    {
        return code;
    }

    public void setCode(ErrorCode code)
    {
        this.code = code;
    }

    public String msg;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::zeroc::CustomException", -1, true);
        ErrorCode.ice_write(ostr_, code);
        ostr_.writeString(msg);
        ostr_.endSlice();
    }

    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        code = ErrorCode.ice_read(istr_);
        msg = istr_.readString();
        istr_.endSlice();
    }

    public static final long serialVersionUID = 1932648154L;
}