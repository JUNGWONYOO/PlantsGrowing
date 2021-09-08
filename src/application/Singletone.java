package application;

import application.dao.PlantsGrowingDaoImple;

public class Singletone {
	
	private static final PlantsGrowingDaoImple instance = new PlantsGrowingDaoImple();
	
	private Singletone() {
		
	}
	public static PlantsGrowingDaoImple getInstance() {
		return instance;
	}

}
