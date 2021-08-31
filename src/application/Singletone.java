package application;

import application.dao.DBConnector;

public class Singletone {
	
	private static final DBConnector instance = new DBConnector();
	
	private Singletone() {
		
	}
	public static DBConnector getInstance() {
		return instance;
	}

}
