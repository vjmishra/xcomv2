package com.xpedx.nextgen.common.util;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

public class XPXItemDetail implements SQLData{
	private String sql_type = "YFS_ITEM_OBJECT";
	
	private String itemId;
	private String itemKey;
	private String keyWords;
	private String description;
	private String shortDescription;
	private String extendedDescription;
	private String bestMatch;
	
	public String getBestMatch() {
		return bestMatch;
	}

	public void setBestMatch(String bestMatch) {
		this.bestMatch = bestMatch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getExtendedDescription() {
		return extendedDescription;
	}

	public void setExtendedDescription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}

	
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String toString(){
		return getItemId()+"_"+getItemKey()+"_"+getKeyWords()+"_"+getDescription()+"_"+getShortDescription()+"_"+getExtendedDescription();
	}
		
	public XPXItemDetail () {}
	
	public String getSQLTypeName() throws SQLException { return sql_type; } 
	
	public void readSQL(SQLInput stream, String typeName) throws SQLException {
		sql_type = typeName;
		itemKey = stream.readString();
		itemId  = stream.readString();
		//bestMatch = stream.readString();
		keyWords = stream.readString();
		shortDescription = stream.readString();
		description = stream.readString();
		extendedDescription = stream.readString();
	}
	
	public void writeSQL(SQLOutput stream) throws SQLException {
		stream.writeString (itemKey);
		stream.writeString (itemId);
		//stream.writeString (bestMatch);
		stream.writeString (keyWords);
		stream.writeString(shortDescription);
		stream.writeString(description);
		stream.writeString(extendedDescription);
	}
	
	
}
