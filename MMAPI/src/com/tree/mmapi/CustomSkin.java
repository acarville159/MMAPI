package com.tree.mmapi;

public class CustomSkin {

	String name;
	String value;
	String signature;
	
	public CustomSkin(String name,String value,String signature) {
		this.name = name;
		this.value = value;
		this.signature = signature;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getSignature() {
		return signature;
	}
	
}
