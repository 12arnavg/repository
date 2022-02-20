package com.getwellsoon.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class GWSUUIDGenerator{
	// private static UUID uuid;
	private static long get64LeastSignificantBitsForVersion1() {
		Random random = new Random();
		long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
		long variant3BitFlag = 0x8000000000000000L;
		return random63BitLong + variant3BitFlag;
	}

	private static long get64MostSignificantBitsForVersion1() {
		LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
		Duration duration = Duration.between(start, LocalDateTime.now());
		long seconds = duration.getSeconds();
		long nanos = duration.getNano();
		long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
		long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
		long version = 1 << 12;
		return 
			(timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
	}

	/**
	 * Restricting to create another instance of the class.
	 */
	// public static final GWSUUIDGenerator INSTANCE = new GWSUUIDGenerator();
	// private GWSUUIDGenerator() {
	// 	uuid = new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1());
	// }

	public static UUID getGenerated() {	//throws Exception
		// if(!(INSTANCE instanceof GWSUUIDGenerator)) throw new Exception("Direct access not allowed");
		return new UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1());
	}
}
