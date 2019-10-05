package com.bootdo.system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain.ApiInvokeRecord;
/**
 * 接口调用记录表
 * @author liy
 * @email 
 * @date 2019-10-02
 */
@Mapper
public interface ApiInvokeRecordDao {
    int deleteByPrimaryKey(String id);

    int insert(ApiInvokeRecord record);

    int insertSelective(ApiInvokeRecord record);

    ApiInvokeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ApiInvokeRecord record);

    int updateByPrimaryKey(ApiInvokeRecord record);
}