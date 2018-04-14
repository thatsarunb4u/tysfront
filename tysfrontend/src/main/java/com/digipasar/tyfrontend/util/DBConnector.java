package com.digipasar.tyfrontend.util;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBConnector {

	private static final String DB_URL = "jdbc:mariadb://localhost:3306/tysdb?user=root&password=loosecontrol";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "loosecontrol";
	
	
	public static void writeToTable(String tableName, List<Map<String,String>> dataMap) {
		Connection connection = null;
		int batchNumber = 0;
		String errorRecord = "";
		PreparedStatement ps = null;
		try {
			connection= DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			//Clear the table before loading
			ps = connection.prepareStatement("DELETE FROM "+tableName);
			ps.executeUpdate();
			ps.close();
			
			//For getting column name
			for(Map<String,String> rowMap: dataMap) {
				ps = connection.prepareStatement(getInsertStmtString(rowMap, tableName));
				break;
			}
			
			System.out.println("Statement:"+ps);
			
			outer: for(Map<String,String> rowMap: dataMap) {
				int paramIndex = 1;
				int runningNumber = 0;
				
				errorRecord = rowMap.toString();
				
				for(String colName: rowMap.keySet()) {
					if(colName.equalsIgnoreCase(rowMap.get(colName))) {
						continue outer;
					}
					ps.setString(paramIndex++, handleNull(rowMap.get(colName)));
				}
				ps.addBatch();
				
				if(runningNumber % 100 == 0){
					batchNumber++;
					ps.executeBatch();
				}
			}

			
			//For last batch
			ps.executeBatch();
			
			System.out.println("Data Loaded Successfully for Table: "+tableName);
		} catch(BatchUpdateException e){
			e.printStackTrace();
    		e.getNextException().printStackTrace();
    		int count[] = e.getUpdateCounts();
    		for(int i=0;i<count.length;i++){
    			if(count[i] == Statement.EXECUTE_FAILED){
    				System.out.println("Batch No.:"+(batchNumber-1)+" Record Number:"+(i+1));
    			}
    		}
    		
    	} catch (SQLException e) {
    		System.out.println("Error Record:"+errorRecord);
    		System.out.println();
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(connection != null)
					connection.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void writeToTable(String tableName, List<Map<String,String>> dataMap, List<String> keyColumns) {
		Connection connection = null;
		int batchNumber = 0;
		String errorRecord = "";
		PreparedStatement ps = null;
				
		try {
			connection= DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			//For getting column names
			for(Map<String,String> rowMap: dataMap) {
				ps = connection.prepareStatement(getInsertStmtString(rowMap, tableName));
				break;
			}
			
			System.out.println("Statement:"+ps);
			
			for(Map<String,String> rowMap: dataMap) {
				int paramIndex = 1;
				
				int updateCount = updateIfExists(connection,tableName,keyColumns,rowMap);
				
				//If No rows updated, it means the record doesn't exist
				if(updateCount == 0) {
					for(String colName: rowMap.keySet()) {
						ps.setString(paramIndex++, handleNull(rowMap.get(colName)));
					}
					try {
						int insertCount = ps.executeUpdate();
						System.out.println("Insert result:"+(insertCount>0?"Success":"Failed")+"::Record:"+rowMap);
					}catch(SQLException e) {
						System.out.println("Error updating:"+rowMap);
						e.printStackTrace();
					}
				}
			}

			
			System.out.println("Data Loaded Successfully for Table: "+tableName);
		} catch(BatchUpdateException e){
			e.printStackTrace();
    		e.getNextException().printStackTrace();
    		int count[] = e.getUpdateCounts();
    		for(int i=0;i<count.length;i++){
    			if(count[i] == Statement.EXECUTE_FAILED){
    				System.out.println("Batch No.:"+(batchNumber-1)+" Record Number:"+(i+1));
    			}
    		}
    		
    	} catch (SQLException e) {
    		System.out.println("Error Record:"+errorRecord);
    		System.out.println();
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(connection != null)
					connection.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static int updateIfExists(Connection connection, String tableName, List<String> keyColumns,
			Map<String, String> rowMap) {
		
		int noOfRowsUpdated = 0;
		
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");
		
		String valuesStr = "";
		int j = 0;
		for(String colName: rowMap.keySet()) {
			//Skip key columns in the update statement
			if(keyColumns.contains(colName)) {
				continue;
			}
			if(j == 0) {
				valuesStr = valuesStr + colName + "=?";
			}else {
				valuesStr = valuesStr + "," + colName + "=?";
			}
			j++; 
		}
		
		sql.append(valuesStr);
		
		sql.append(" WHERE ");
				
		String whereClauseStr = "";
		
		for(int i = 0; i < keyColumns.size(); i++){
			if(i==0){
				whereClauseStr = keyColumns.get(i) + "= ?";
			}else{
				whereClauseStr = whereClauseStr + " AND " + keyColumns.get(i) + "=?";
			}
		}
		
		sql.append(whereClauseStr);
		sql.append(";");
		
		PreparedStatement ps = null;
		int paramIndex = 1;
		try {
			ps = connection.prepareStatement(sql.toString());
		
			for(String colName: rowMap.keySet()) {
				if(keyColumns.contains(colName)) {
					continue;
				}
				ps.setString(paramIndex++, handleNull(rowMap.get(colName))); 
			}
			for(String keyCol:keyColumns) {
				ps.setString(paramIndex++, rowMap.get(keyCol));
			}
			
			System.out.println("Update SQL:"+ps);
			
			noOfRowsUpdated = ps.executeUpdate();
			
			System.out.println("Number of rows updated:"+noOfRowsUpdated);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(ps!=null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return noOfRowsUpdated;
	}


	private static String handleNull(String str){
		return str == null?"":str;
	}
	
	private static String getInsertStmtString(Map<String,String> rowMap, String tableName){
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append(" (");
		sql.append(getColumnNameString(rowMap.keySet()));
		sql.append(")");
		sql.append(" values (");
		
		String questionMarkStr = "";
		
		for(int i = 0; i < rowMap.size(); i++){
			if(i==0){
				questionMarkStr += "?";
			}else{
				questionMarkStr += ",?";
			}
		}
		
		sql.append(questionMarkStr);
		sql.append(");");
		
		return sql.toString();
	}
	
	private static String getSelectQueryStmtStringByPrimaryKey(String tableName, List<String> keyColumns){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(tableName);
		sql.append(" WHERE ");
				
		String whereClauseStr = "";
		
		for(int i = 0; i < keyColumns.size(); i++){
			if(i==0){
				whereClauseStr = keyColumns.get(i) + "= ?";
			}else{
				whereClauseStr = whereClauseStr + " AND " + keyColumns.get(i) + "=?";
			}
		}
		
		sql.append(whereClauseStr);
		sql.append(";");
		
		return sql.toString();
	}
	
	private static String getColumnNameString(Set<String> colNameMap){
		String colStr = "";
		int i = 0;
		for(String columnName : colNameMap){
			if(i++==0){
				colStr += columnName;
			}else{
				colStr += ","+columnName;
			}
		}
		
		return colStr;
	}
	
}
