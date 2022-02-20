package com.getwellsoon.core;

import org.hibernate.dialect.function.StandardSQLFunction;
// import org.hibernate.spatial.dialect.mysql.MySQL56InnoDBSpatialDialect;
// import org.hibernate.spatial.dialect.mysql.MySQL56SpatialDialect;
import org.hibernate.spatial.dialect.mysql.MySQL8SpatialDialect;
import org.hibernate.type.StandardBasicTypes;

public class MySQLOverrideDialect extends MySQL8SpatialDialect {
	public MySQLOverrideDialect(){
		super();
		registerFunction("ST_Distance_Sphere", new StandardSQLFunction("ST_Distance_Sphere", StandardBasicTypes.DOUBLE));
	}
}
