package com.lbs.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisClient {

	//Jedis  JedisPool  JedisCluster
	@Test
	public void test1() {
		Jedis jedis = new Jedis("10.0.143.211",6379);
		jedis.set("person", "liu");
		String ss = jedis.get("person");
		System.out.println(ss);
		jedis.close();
	}
	
	@Test
	public void test2() {
		JedisPool pool = new JedisPool("10.0.143.211",6379);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("person"));
		jedis.close();
	}
	
	@Test
	public void test3() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("10.0.143.211",7001));
		nodes.add(new HostAndPort("10.0.143.211",7002));
		nodes.add(new HostAndPort("10.0.143.211",7003));
		nodes.add(new HostAndPort("10.0.143.211",7004));
		nodes.add(new HostAndPort("10.0.143.211",7005));
		nodes.add(new HostAndPort("10.0.143.211",7006));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("name", "wnag");
		System.out.println(cluster.get("name"));
	}
	
	@Test
	public void test4() {
		Jedis jedis = new Jedis("10.0.143.211",6379);
		jedis.set("name", "bbb");
		System.out.println(jedis.get("name"));
		jedis.close();
	}
	@Test
	public void test5() {
		JedisPool pool = new JedisPool("10.0.143.211",6379);
		Jedis jedis = pool.getResource();
		System.out.println(jedis.get("person"));
		jedis.hset("liu", "name", "liu");
		System.out.println(jedis.hget("liu", "name"));
		/*jedis.hset("name","1", "liu");
		System.out.println(jedis.hget("name", "1"));*/
		jedis.close();
		
	}
	@Test
	public void test6() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("10.0.143.211",7001));
		nodes.add(new HostAndPort("10.0.143.211",7002));
		nodes.add(new HostAndPort("10.0.143.211",7003));
		nodes.add(new HostAndPort("10.0.143.211",7004));
		nodes.add(new HostAndPort("10.0.143.211",7005));
		nodes.add(new HostAndPort("10.0.143.211",7006));
		
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("name", "sdf");
		System.out.println(cluster.get("name"));
		cluster.close();
	}
}





















