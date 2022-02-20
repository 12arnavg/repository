package com.getwellsoon.util;

public class MathUtils {
	public static final Double MILE_TO_METER_RATIO = 1609.34;
	public static final Double MILE_TO_KM_RATIO = 1.60934;
	public static final Double KM_TO_MILE_RATIO = 0.621371;
	public static final Double LAT_KM_TO_DEG_RATIO = 110.574;
	public static final Double LONG_KM_TO_DEG_RATIO = 111.319;

	public static Double radianToDegree(Double radian) {
		return ((radian * 100) / Math.PI);
	}

	public static Double degreeToRadian(Double degree) {
		return ((degree * Math.PI) / 100);
	}
}
