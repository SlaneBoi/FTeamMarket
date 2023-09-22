package FTeamMarket;

public class User {
	
	protected String userId;
	protected String username;
	protected String password;
	protected String gender;
	protected String email;
	protected String phoneNumber;
	protected int age;
	protected String role;
	public static int count;
	
	public User(String userId, String username, String password, String gender, String email, String phoneNumber, int age, String role) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.age = age;
		this.role = role;
		count++;
	}

	public User() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		User.count = count;
	}

}