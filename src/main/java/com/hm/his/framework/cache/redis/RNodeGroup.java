package com.hm.his.framework.cache.redis;

public class RNodeGroup {

	/** master节点，单个节点只可能存在一个 **/
	private RNode masterNode;

	/** slave节点，单节点单个slave节点 **/
	private RNode slaveNode;

	/** 负载均衡 **/
	int count;

	/** 主从 读的权重 **/
	private final int[] weight = {3, 7};

	/** 锁 **/
	Object mutex = new Object();

	/**
	 * 根据权重获取读取节点
	 * 
	 * @return
	 */
	public RNode getReadNode() {
		synchronized (mutex) {
			count++;
		}
		if (count >= 100000) {
			count = 0;
		}

		// 去模权重的总和
		int randomInt = count % 10;
		return randomInt < weight[0] ? masterNode : slaveNode;

	}

	public RNode getMasterNode() {
		return masterNode;
	}

	public void setMasterNode(RNode masterNode) {
		this.masterNode = masterNode;
	}

	public RNode getSlaveNode() {
		return slaveNode;
	}

	public void setSlaveNode(RNode slaveNode) {
		this.slaveNode = slaveNode;
	}

}
