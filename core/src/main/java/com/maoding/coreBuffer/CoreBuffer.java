package com.maoding.coreBuffer;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 作    者 : 张成亮
 * 日    期 : 2018/6/1 11:06
 * 描    述 :
 */
public interface CoreBuffer {
    default <T> void setList(String key,List<? extends T> list,long aliveTime){}
    default <T> T getList(String key,long aliveTime){return null;}
    default <T> void replaceInList(T element,long aliveTime){}
    default <T> void removeFormList(T element){}
}
