package com.telecominfraproject.wlan.profile.datastore.rdbms;

import java.util.ArrayList;

public class SqlQueryBuilder {

	private StringBuilder sqlBuilder;
	private ArrayList<Object> queryArgs;

	public SqlQueryBuilder()
	{
		sqlBuilder = new StringBuilder();
		queryArgs = new ArrayList<>();
	}
	
	public void addSqlWithArgument(String sql, Object argument)
	{
		sqlBuilder.append(sql);
		queryArgs.add(argument);
	}
	
	public void addSql(String sql)
	{
		sqlBuilder.append(sql);
	}
	
	public void addArgument(Object argument)
	{
		queryArgs.add(argument);
	}
	
	public String getSql()
	{
		return sqlBuilder.toString();
	}
	
	public ArrayList<Object> getQueryArgs()
	{
		return queryArgs;
	}
}