package com.zh.shenshouexpensetracker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类（基于 Jedis 连接池）
 * 提供常用数据类型操作以及对象序列化存储
 *
 * @author <your-name>
 * @date 2026-06-11
 */
public class RedisUtil {

    private static JedisPool jedisPool;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 私有构造器，防止实例化
    private RedisUtil() {
    }

    /**
     * 初始化连接池（应在应用启动时调用一次）
     *
     * @param host     Redis 主机地址
     * @param port     Redis 端口
     * @param password Redis 密码（无密码传 null 或空字符串）
     * @param database 数据库索引（默认 0）
     * @param maxTotal 最大连接数
     * @param maxIdle  最大空闲连接数
     * @param minIdle  最小空闲连接数
     */
    public static void initPool(String host, int port, String password, int database,
                                int maxTotal, int maxIdle, int minIdle) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(true);   // 借用连接时测试是否可用
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(true); // 连接耗尽时等待

        if (password != null && !password.trim().isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port, 2000, password, database);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, 2000, null, database);
        }
    }

    /**
     * 获取 Jedis 实例（使用 try-with-resources 或手动归还）
     */
    public static Jedis getJedis() {
        if (jedisPool == null) {
            throw new RuntimeException("JedisPool 未初始化，请先调用 initPool 方法");
        }
        return jedisPool.getResource();
    }

    /**
     * 归还 Jedis 连接（如果未使用 try-with-resources）
     */
    @Deprecated
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close(); // Jedis 实现了 AutoCloseable，close 会归还到连接池
        }
    }

    // =============================== 通用操作 ===============================

    /**
     * 删除指定 key
     */
    public static Long del(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key);
        }
    }

    /**
     * 批量删除
     */
    public static Long del(String... keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(keys);
        }
    }

    /**
     * 判断 key 是否存在
     */
    public static Boolean exists(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    }

    /**
     * 设置过期时间（秒）
     */
    public static Long expire(String key, int seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key, seconds);
        }
    }

    /**
     * 设置过期时间（毫秒）
     */
    public static Long pexpire(String key, long milliseconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.pexpire(key, milliseconds);
        }
    }

    /**
     * 获取剩余生存时间（秒，-1表示永不过期，-2表示不存在）
     */
    public static Long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    /**
     * 移除过期时间，使 key 永久有效
     */
    public static Long persist(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.persist(key);
        }
    }

    /**
     * 返回 key 对应的数据类型（none, string, list, set, zset, hash）
     */
    public static String type(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.type(key);
        }
    }

    // =============================== 字符串(String) ===============================

    /**
     * 设置字符串键值
     */
    public static String set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    /**
     * 设置字符串键值及过期时间（秒）
     */
    public static String setex(String key, String value, int seconds) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, seconds, value);
        }
    }

    /**
     * 获取字符串值
     */
    public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    /**
     * 删除并返回旧值，设置新值
     */
    public static String getSet(String key, String newValue) {
        try (Jedis jedis = getJedis()) {
            return jedis.getSet(key, newValue);
        }
    }

    /**
     * 递增（原子操作）
     */
    public static Long incr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.incr(key);
        }
    }

    /**
     * 递增指定步长
     */
    public static Long incrBy(String key, long increment) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, increment);
        }
    }

    /**
     * 递减
     */
    public static Long decr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.decr(key);
        }
    }

    /**
     * 递减指定步长
     */
    public static Long decrBy(String key, long decrement) {
        try (Jedis jedis = getJedis()) {
            return jedis.decrBy(key, decrement);
        }
    }

    /**
     * 仅当 key 不存在时设置（返回 1 成功，0 失败）
     */
    public static Long setnx(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setnx(key, value);
        }
    }

    // =============================== 对象序列化存储(JSON) ===============================

    /**
     * 存储对象（转为 JSON 字符串）
     */
    public static String setObject(String key, Object obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            return set(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转 JSON 失败", e);
        }
    }

    /**
     * 存储对象并设置过期时间（秒）
     */
    public static String setObjectEx(String key, Object obj, int seconds) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            return setex(key, json, seconds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转 JSON 失败", e);
        }
    }

    /**
     * 获取对象（从 JSON 反序列化）
     */
    public static <T> T getObject(String key, Class<T> clazz) {
        String json = get(key);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 转对象失败，key=" + key, e);
        }
    }

    // =============================== 哈希(Hash) ===============================

    /**
     * 设置哈希字段值
     */
    public static Long hset(String key, String field, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hset(key, field, value);
        }
    }

    /**
     * 获取哈希字段值
     */
    public static String hget(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        }
    }

    /**
     * 批量设置哈希字段
     */
    public static String hmset(String key, Map<String, String> hash) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmset(key, hash);
        }
    }

    /**
     * 批量获取哈希字段值
     */
    public static List<String> hmget(String key, String... fields) {
        try (Jedis jedis = getJedis()) {
            return jedis.hmget(key, fields);
        }
    }

    /**
     * 获取整个哈希表
     */
    public static Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hgetAll(key);
        }
    }

    /**
     * 删除哈希字段
     */
    public static Long hdel(String key, String... fields) {
        try (Jedis jedis = getJedis()) {
            return jedis.hdel(key, fields);
        }
    }

    /**
     * 判断哈希字段是否存在
     */
    public static Boolean hexists(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hexists(key, field);
        }
    }

    /**
     * 获取哈希所有字段名
     */
    public static Set<String> hkeys(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(key);
        }
    }

    /**
     * 获取哈希所有值
     */
    public static List<String> hvals(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hvals(key);
        }
    }

    /**
     * 哈希字段递增
     */
    public static Long hincrBy(String key, String field, long value) {
        try (Jedis jedis = getJedis()) {
            return jedis.hincrBy(key, field, value);
        }
    }

    // =============================== 列表(List) ===============================

    /**
     * 从左推入元素（头部）
     */
    public static Long lpush(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, values);
        }
    }

    /**
     * 从右推入元素（尾部）
     */
    public static Long rpush(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpush(key, values);
        }
    }

    /**
     * 从左弹出元素（头部）
     */
    public static String lpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpop(key);
        }
    }

    /**
     * 从右弹出元素（尾部）
     */
    public static String rpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.rpop(key);
        }
    }

    /**
     * 获取列表指定范围元素（0 到 -1 表示全部）
     */
    public static List<String> lrange(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        }
    }

    /**
     * 获取列表长度
     */
    public static Long llen(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.llen(key);
        }
    }

    /**
     * 修剪列表，只保留指定范围
     */
    public static String ltrim(String key, long start, long end) {
        try (Jedis jedis = getJedis()) {
            return jedis.ltrim(key, start, end);
        }
    }

    /**
     * 根据索引获取元素
     */
    public static String lindex(String key, long index) {
        try (Jedis jedis = getJedis()) {
            return jedis.lindex(key, index);
        }
    }

    /**
     * 移除列表中与 value 匹配的元素（count>0 从左向右，count<0 从右向左，count=0 移除所有）
     */
    public static Long lrem(String key, long count, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrem(key, count, value);
        }
    }

    // =============================== 集合(Set) ===============================

    /**
     * 添加成员
     */
    public static Long sadd(String key, String... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, members);
        }
    }

    /**
     * 移除成员
     */
    public static Long srem(String key, String... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, members);
        }
    }

    /**
     * 获取所有成员
     */
    public static Set<String> smembers(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        }
    }

    /**
     * 判断是否为成员
     */
    public static Boolean sismember(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, member);
        }
    }

    /**
     * 获取集合大小
     */
    public static Long scard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        }
    }

    /**
     * 随机弹出成员
     */
    public static String spop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        }
    }

    /**
     * 随机获取 count 个成员（不移除）
     */
    public static List<String> srandmember(String key, int count) {
        try (Jedis jedis = getJedis()) {
            return jedis.srandmember(key, count);
        }
    }

    // =============================== 有序集合(SortedSet) ===============================

    /**
     * 添加成员及分数
     */
    public static Long zadd(String key, double score, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, score, member);
        }
    }

    /**
     * 批量添加
     */
    public static Long zadd(String key, Map<String, Double> scoreMembers) {
        try (Jedis jedis = getJedis()) {
            return jedis.zadd(key, scoreMembers);
        }
    }

    /**
     * 获取成员分数
     */
    public static Double zscore(String key, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zscore(key, member);
        }
    }

    /**
     * 移除成员
     */
    public static Long zrem(String key, String... members) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrem(key, members);
        }
    }

    /**
     * 按分数范围移除成员
     */
    public static Long zremrangeByScore(String key, double start, double stop) {
        try (Jedis jedis = getJedis()) {
            return jedis.zremrangeByScore(key, start, stop);
        }
    }

    /**
     * 获取有序集合大小
     */
    public static Long zcard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.zcard(key);
        }
    }

    /**
     * 对成员增加分数
     */
    public static Double zincrby(String key, double increment, String member) {
        try (Jedis jedis = getJedis()) {
            return jedis.zincrby(key, increment, member);
        }
    }
}