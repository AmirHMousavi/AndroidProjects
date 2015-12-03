package com.example.assign3.callLog;



public class CallInfo {

	private final String number;
	private final long date;
	private final String type;
	private final String cachedName;
	private final String cachedNumberType;
	private final String duration;
	
	public CallInfo(String number,long date,String type,String cachedName,String cachedNumberType,String duration){
		this.number=number;
		this.date=date;
		this.type=type;
		this.cachedName=cachedName;
		this.cachedNumberType=cachedNumberType;
		this.duration=duration;
	}
	
	public String getNumber() {
		return number;
	}
	public long getDate() {
		return date;
	}
	public String getType() {
		return type;
	}
	public String getChachedName() {
		return cachedName;
	}
	public String getCachedNumberType() {
		return cachedNumberType;
	}
	public String getDuration() {
		return duration;
	}
	
	public String toString(){
		return number+" "+date+" "+type+" "+cachedName+" "+cachedNumberType+" "+duration+" ";
		
	}

}
