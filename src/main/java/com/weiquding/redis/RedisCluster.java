package com.weiquding.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Redis集群示例
 *
 * @author beliveyourself
 * @version V1.0
 * @date 2020/4/12
 */
public class RedisCluster {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCluster.class);

    public static void main(String[] args) {

        // 节点信息
        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort("192.168.40.128", 7000));
        jedisClusterNode.add(new HostAndPort("192.168.40.128", 7001));
        jedisClusterNode.add(new HostAndPort("192.168.40.129", 7000));
        jedisClusterNode.add(new HostAndPort("192.168.40.129", 7001));
        jedisClusterNode.add(new HostAndPort("192.168.40.130", 7000));
        jedisClusterNode.add(new HostAndPort("192.168.40.130", 7001));

        JedisPoolConfig config = new JedisPoolConfig();
        // 最大连接数
        config.setMaxTotal(100);
        // 最大空闲连接数
        config.setMaxIdle(10);
        // 是否测试连接
        config.setTestOnBorrow(true);

        JedisCluster jedisCluster = null;
        try {
            //connectionTimeout：连接超时时间
            //soTimeout：读取超时时间
            jedisCluster = new JedisCluster(
                    jedisClusterNode,
                    6000,
                    5000,
                    10,
                    "mypassword",
                    config);

            LOGGER.info("set key:[{}]==>value:[{}], result:[{}]", "name", "believeyourself", jedisCluster.set("name", "believeyourself"));
            LOGGER.info("set key:[{}]==>value:[{}], result:[{}]", "age", "19", jedisCluster.set("age", "19"));
            LOGGER.info("get key:[{}]==>value:[{}]", "name", jedisCluster.get("name"));
            LOGGER.info("get key:[{}]==>value:[{}]", "age", jedisCluster.get("age"));
        } catch (Exception e) {
            LOGGER.error("测试出错==>jedisClusterNode:[{}]", jedisClusterNode, e);
        } finally {
            if (jedisCluster != null) {
                try {
                    jedisCluster.close();
                } catch (IOException e) {
                    // ignore exception
                }
            }
        }
    }

}
