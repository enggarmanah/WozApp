package id.urbanwash.wozapp;

public class Config {
	
	public static boolean isByPassLogin = false;
	
	//public static String SERVER_URL = "http://192.168.1.8:8888";
	//public static String SERVER_URL = "http://192.168.169.2:8888";
	//public static String SERVER_URL = "http://192.168.1.161:8888";
	//public static String SERVER_URL = "http://172.20.10.4:8888";

	public static String SERVER_URL = "http://project-3419620684716107167.appspot.com";
	
	public static boolean isDebug() {
		
		return isDevelopment() && isByPassLogin;
	}
		
	public static boolean isDevelopment() {

		return !SERVER_URL.equals("http://project-3419620684716107167.appspot.com");
	}
}