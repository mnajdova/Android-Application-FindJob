package mk.ukim.finki.jmm.findJob.model;

import java.io.Serializable;
import java.util.Date;


public class Ad implements Serializable {
	public Long id;
	public  String name;
	public  String description;
	public  Date dateFrom;
	public  Date dateTo;
	public String Location;
	public  User publisher;
	
	@Override
	public String toString() {
		return name;
	}
	
}
