package com.memory.entity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 保存内存的范围
 * 
 * **/
public class MemoryRange 
{
	private long minValue;
	private long maxValue;
	
	public long getMinValue() 
	{
		return minValue;
	}
	public void setMinValue(long minValue) 
	{
		this.minValue = minValue;
	}
	public long getMaxValue() 
	{
		return maxValue;
	}
	public void setMaxValue(long maxValue)
	{
		this.maxValue = maxValue;
	}
}
