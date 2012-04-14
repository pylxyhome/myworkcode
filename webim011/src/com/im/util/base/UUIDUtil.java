
package com.im.util.base;

import java.util.UUID;

/**
 * UUID
 * @author Administrator
 *
 */
public final class UUIDUtil {
    
	public static String getRandomUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	public static String getMDUUID(){
		UUID uuid = UUID.randomUUID();
		return MD5.MD5Encode(uuid.toString());
	}
} 


