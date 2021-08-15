package application.firstLogin.Users;

public class UserInfo {
	
	private String id;
	private String password;
	private String plantName;
	private int watering;
	private int caring;
	private int tanning;
	private int nutrition;
	private String IP;
	private int port;
	
	// 1번 방울토마토 2번 해바라기
	// 유저정보 생성자
	private int species;
	private int level;
	
	
	public UserInfo() {
		super();
		
	}
	
	public UserInfo(String id, String password, String IP, int port, int level) {
		super();
		this.id = id;
		this.password = password;
		this.IP = IP;
		this.port = port;
		this.level = level;
		
	}
	
	public UserInfo(String id, String password) {
		super();
		this.id = id;
		this.password = password;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public int getWatering() {
		return watering;
	}

	public void setWatering(int watering) {
		this.watering = watering;
	}

	public int getCaring() {
		return caring;
	}

	public void setCaring(int caring) {
		this.caring = caring;
	}

	public int getTanning() {
		return tanning;
	}

	public void setTanning(int tanning) {
		this.tanning = tanning;
	}

	public int getNutrition() {
		return nutrition;
	}

	public void setNutrition(int nutrition) {
		this.nutrition = nutrition;
	}
	
	public String getIP() {
		return IP;
	}

	public void setIP(String IP) {
		this.IP = IP;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSpecies() {
		return species;
	}

	public void setSpecies(int species) {
		this.species = species;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
