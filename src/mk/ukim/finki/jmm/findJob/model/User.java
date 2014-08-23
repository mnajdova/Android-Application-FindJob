package mk.ukim.finki.jmm.findJob.model;

import java.io.Serializable;

public class User implements Serializable {
	public static final int ROLE_PUBLISHER = 1;
	public static final int ROLE_SUBSCRIBER = 2;	
	
	public Long id;
	public  String username;
	public   String password;
	public  String name;
	public  String phone;
	public  int role;
	public String CVLink;
	public String email;
	
	
}
