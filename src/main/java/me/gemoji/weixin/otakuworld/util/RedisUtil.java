package me.gemoji.weixin.otakuworld.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.baidu.bae.api.util.BaeEnv;

public class RedisUtil {

	private static Jedis jedis = null;
	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	
	static{
		init();
	}
	
	public static void init(){
		try {
			String serverIp = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_REDIS_IP);
			String portStr = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_ADDR_REDIS_PORT);
			int port = NumberUtils.toInt(portStr, 6379);
			String user = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_AK);
			String password = BaeEnv.getBaeHeader(BaeEnv.BAE_ENV_SK);
			String databaseName = "BIvIHgsjrCOvkvEntPct";
			
			jedis = new Jedis(serverIp, port);
			jedis.connect();
			jedis.auth(user + "-" + password + "-" + databaseName);
			
			logger.info("Init redis client success!");
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in init redis client!", e);
		}
	}
	
	public static void set(String key, String value){
		try {
			jedis.set(key, value);
		} catch (Exception e) {
			logger.error("Error in set key: " + key + " with value: " + value, e);
		}
		
	}
	
	public static String get(String key){
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("Error in get value of key: " + key, e);
		}
		return value;
	}

}
