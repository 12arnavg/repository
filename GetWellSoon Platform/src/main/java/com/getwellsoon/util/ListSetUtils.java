package com.getwellsoon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.getwellsoon.entity.BaseEntity;
import com.getwellsoon.entity.Location;
import com.getwellsoon.entity.TrialField;
import com.getwellsoon.model.LocationTO;

import org.locationtech.jts.geom.Geometry;

public class ListSetUtils {
	public static List<LocationTO> locationListToLocationTOList(List<Location> given) {
		List<LocationTO> toList = given.stream().map( item -> {
			LocationTO to = new LocationTO();
			to.setAddress(item.getRawAddress());
			to.setName(item.getName());
			to.setCity(item.getCity());
			to.setState(item.getState());
			to.setCountry(item.getCountry());
			to.setZip(item.getZip());
			return to;
			// to.setDistance(item.getCoordinates());
		} ).collect(Collectors.toList());
		return toList;
	}
	public static List<LocationTO> locationListToLocationTOList(Set<Location> given) {
		List<LocationTO> toList = given.stream().map( item -> {
			LocationTO to = new LocationTO();
			to.setAddress(item.getRawAddress());
			to.setName(item.getName());
			to.setCity(item.getCity());
			to.setState(item.getState());
			to.setCountry(item.getCountry());
			to.setZip(item.getZip());
			return to;
			// to.setDistance(item.getCoordinates());
		} ).collect(Collectors.toList());
		return toList;
	}

	public static List<String> setToStringList (Set<? extends BaseEntity> given) {
		List<String> strList = given.stream().map(
			item -> item.getName()
		).collect(Collectors.toList());
		return strList;
	}

	public static Map<String, Geometry> setToCoordinatesMap (Set<Location> given) {
		Map<String, Geometry> coordMap = new HashMap<String, Geometry>();
		given.forEach(item -> coordMap.put(item.getName(), item.getCoordinates()));
		// given.stream()
		// .collect(
		// 	Collectors.toMap(
		// 		item -> item.getName(),
		// 		item -> item.getCoordinates()
		// 	));

		return coordMap;
	}
}
