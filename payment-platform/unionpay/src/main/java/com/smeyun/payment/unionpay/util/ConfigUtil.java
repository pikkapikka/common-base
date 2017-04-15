package com.smeyun.payment.unionpay.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtil {
	
	private static SDKConfig config;
	
	@Autowired
	public void setConfig(SDKConfig sdkConfig)
	{
		config = sdkConfig;
	}
	
	public static SDKConfig getConfig()
	{
		if(config == null)
		{
			config = new SDKConfig();
		}
		return config;
	}
}
