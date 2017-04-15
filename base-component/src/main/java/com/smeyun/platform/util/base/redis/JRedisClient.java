/*
 * Copyright (C), 2002-2016, 重庆锋云汇智数据科技有限公司
 * FileName: JRedisClient.java
 * Author:   qxf
 * Date:     2016年12月14日 上午10:00:40
 */
package com.smeyun.platform.util.base.redis;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * REDIS客户端
 *
 * @author qxf
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class JRedisClient
{
    /*
     * 现在默认的template,key为字符串,value位字符串
     */
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 构建RedisTemplate和StringRedisTemplate
     * @param redisProperties
     */
    public static JRedisClient getInstance(RedisProperties redisProperties)
    {
        //RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
        poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        JedisShardInfo info = new JedisShardInfo(redisProperties.getHost(), redisProperties.getPort());
        info.setPassword(redisProperties.getPassword());
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setShardInfo(info);
        JRedisClient redisUtils = new JRedisClient(factory);
        return redisUtils;
    }
    
    /*
     * 构建RedisTemplate和StringRedisTemplate
     * @param factory
     */
    private JRedisClient(RedisConnectionFactory factory)
    {
        //        redisTemplate = new RedisTemplate();
        //        redisTemplate.setConnectionFactory(factory);
        //        redisTemplate.afterPropertiesSet();
        stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        stringRedisTemplate.afterPropertiesSet();
    }
    
    /**
     * 根据key和MAP中的key获取MAP中的值
     * @param key KEY
     * @param hashKey MAP中的KEY
     * @return 存储对象
     * @throws
     */
    public Object getHashValueByKey(String key, String hashKey)
    {
        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }
    
    /**
     * 通过KEY获取HashMap
     * @param key 关键字
     * @return MAP对象
     * @throws
     */
    public Map<Object, Object> getMapEntries(String key)
    {
        return stringRedisTemplate.opsForHash().entries(key);
    }
    
    /**
     * 将值存入MAP中
     * @param key 关键字
     * @param hashKey MAP的KEY
     * @param hasValue MAP的值
     * @throws
     */
    public void putValueToMap(String key, String hashKey, String hasValue)
    {
        stringRedisTemplate.opsForHash().put(key, hashKey, hasValue);
    }
    
    /**
     * 重新设置某个KEY的值
     * @param key
     * @param value
     */
    public void setValue(String key, String value)
    {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    
    /**
     * 添加值
     * @param key
     * @param value
     */
    public void addValue(String key, String value)
    {
        stringRedisTemplate.opsForValue().append(key, value);
    }
    
    /**
     * 添加值到LIST中
     * @param key
     * @param value
     */
    public void addValueToList(String key, String value)
    {
        stringRedisTemplate.boundListOps(key).leftPush(value);
    }
    
    /**
     * 根据KEY从list中获取所有值
     * @param key
     * @return
     */
    public List<String> getValuesFromList(String key)
    {
        return stringRedisTemplate.boundListOps(key).range(0, -1);
    }
    
    /**
     * 把值添加到队列的尾部
     * @param key KEY
     * @param value 值
     * @return
     */
    public void appendValueToList(String key, String value)
    {
        stringRedisTemplate.boundListOps(key).rightPush(value);
    }
    
    /**
     * 移除队列头部的值
     * @param key KEY
     * @return
     */
    public String lpopValueFromList(String key)
    {
        return stringRedisTemplate.boundListOps(key).leftPop();
    }
    
    /**
     * 移除队列头部的值
     * @param key KEY
     * @return
     */
    public String getValueFromList(String key, long index)
    {
        return stringRedisTemplate.boundListOps(key).index(index);
    }
    
    /**
     * 通过key从LIST中获取指定范围的值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> getValuesFromList(String key, int start, int end)
    {
        return stringRedisTemplate.boundListOps(key).range(start, end);
    }
    
    /**
     * 获取LIST的大小
     * @param key
     * @return
     */
    public Long getListSize(String key)
    {
        return stringRedisTemplate.boundListOps(key).size();
    }
    
    /**
     * 从redis中获取值
     * @param key
     * @return
     */
    public String getValue(String key)
    {
        return stringRedisTemplate.opsForValue().get(key);
    }
    
    /**
     * 添加一组值
     * @param values
     */
    public void addValues(Map<String, String> values)
    {
        stringRedisTemplate.opsForValue().multiSet(values);
    }
    
    /**
     * 获取某个KEY的值的数量
     * @param key
     * @return
     */
    public Long getSize(String key)
    {
        return stringRedisTemplate.opsForValue().size(key);
    }
    
    /**
     * 删除某个KEY的所有值
     * @param key
     */
    public void deleteValue(String key)
    {
        stringRedisTemplate.delete(key);
    }
    
    /**
     * 删除指定KEY的对象
     * @param keys
     */
    public void deleteValues(List<String> keys)
    {
        stringRedisTemplate.delete(keys);
    }
    
    /**
     * 根据KEY移除某个KEY下面的MAP中某几个HashKey的值
     * @param key
     * @param hashKeys
     */
    public void removeMapValues(String key, String... hashKeys)
    {
        stringRedisTemplate.opsForHash().delete(key, new Object[] {hashKeys});
    }
    
    /**
     * 获取hash的大小
     * @param key
     * @return
     */
    public Long getHashSize(String key)
    {
        return stringRedisTemplate.opsForHash().size(key);
    }
    
    /**
     * 对现有的KEY进行重命名
     * @param oldKey
     * @param newKey
     */
    public void renameKey(String oldKey, String newKey)
    {
        stringRedisTemplate.rename(oldKey, newKey);
        
    }
    
    /**
     * 检查MAP中是否包含已存在的值
     * @param key 关键字
     * @param hashKey MAP的KEY
     * @return
     */
    public boolean hasKeyInMap(String key, String hashKey)
    {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }
}
