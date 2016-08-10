package com.hm.his.framework.cache.redis;

import redis.clients.jedis.JedisPool;

public class RNode {

	/** 格式host:port **/
	private String nodeName;

	private JedisPool jPool;

	private RNodeGroup group;

	public RNode(String nodeName, JedisPool jPool) {
		this.nodeName = nodeName;
		this.jPool = jPool;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public JedisPool getjPool() {
		return jPool;
	}

	public void setjPool(JedisPool jPool) {
		this.jPool = jPool;
	}

	public RNodeGroup getGroup() {
		return group;
	}

	public void setGroup(RNodeGroup group) {
		this.group = group;
	}

}
