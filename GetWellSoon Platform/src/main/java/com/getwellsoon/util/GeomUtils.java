package com.getwellsoon.util;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeomUtils {
	public static Integer WGS84_EPSG_SRID = 4326;

	public static Geometry getPoint (Map<String, Double> coords) {
		Geometry point = null;
		final StringBuilder wktPoint = new StringBuilder();
		wktPoint.append("POINT (");
		wktPoint.append(coords.get("lng").toString());
		wktPoint.append(" ");
		wktPoint.append(coords.get("lat").toString());
		wktPoint.append(")");
		String pointString = wktPoint.toString();

		try {
			point = new WKTReader(new GeometryFactory(new PrecisionModel(), GeomUtils.WGS84_EPSG_SRID)).read(pointString);
			point.setSRID(WGS84_EPSG_SRID);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return point;
	}

	public static Geometry getPoint (Expression<Coordinate> coordinate) {
		Geometry point = null;
		Map<String, Double> coords = new HashMap<String, Double>();
		// coords.put("lat", coordinate.);
		return point;
	}
}