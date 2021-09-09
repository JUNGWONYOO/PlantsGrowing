package application.dao;

public interface PlantsGrowingDao {
	public static final String members_selectAll = "select * from members where id= ? and password = ?";
	public static final String members_selectId = "select * from members where id= ?";
	public static final String members_insert ="insert into members values(?,?,'null','0','0','0','0',?,?,'0','1')";
	public static final String members_updatePlantSpecies = "update members set species = ? where id = ?";
	public static final String members_updatePlantName = "update members set plantName = ? where id = ?";
	public static final String members_updateAll = "update members set watering = ?,caring = ?,tanning = ?, nutrition = ?, level = ? where plantName = ?";
	public static final String fortune_pick = "select content from fortune order by rand() limit 1";
	
}
