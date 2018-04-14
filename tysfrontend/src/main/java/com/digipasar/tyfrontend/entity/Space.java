package com.digipasar.tyfrontend.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="Space")
public class Space {
	
	private String spcaeId;
	
	
	private String spaceGroup;
	
	
	private String  spaceGroupDescription;
	
	
	private String spaceName;
	
	
	private String spaceImageURL;
	
	
	private String spaceStatus;
	
	
	private String spaceDescription;

	@Id
	@Column(name="SpaceID")
	public String getSpcaeId() {
		return spcaeId;
	}

	public void setSpcaeId(String spcaeId) {
		this.spcaeId = spcaeId;
	}

	@Column(name="SpaceGroup")
	public String getSpaceGroup() {
		return spaceGroup;
	}

	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}

	@Column(name="SpaceGroupDescription")
	public String getSpaceGroupDescription() {
		return spaceGroupDescription;
	}

	public void setSpaceGroupDescription(String spaceGroupDescription) {
		this.spaceGroupDescription = spaceGroupDescription;
	}

	@Column(name="SpaceName")
	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	@Column(name="SpaceImageURL")
	public String getSpaceImageURL() {
		return spaceImageURL;
	}

	public void setSpaceImageURL(String spaceImageURL) {
		this.spaceImageURL = spaceImageURL;
	}

	@Column(name="SpaceStatus")
	public String getSpaceStatus() {
		return spaceStatus;
	}

	public void setSpaceStatus(String spaceStatus) {
		this.spaceStatus = spaceStatus;
	}

	@Column(name="SpaceDescription")
	public String getSpaceDescription() {
		return spaceDescription;
	}

	public void setSpaceDescription(String spaceDescription) {
		this.spaceDescription = spaceDescription;
	}

	@Override
	public String toString() {
		return "Space [spcaeId=" + spcaeId + ", spaceGroup=" + spaceGroup + ", spaceGroupDescription="
				+ spaceGroupDescription + ", spaceName=" + spaceName + ", spaceImageURL=" + spaceImageURL
				+ ", spcaeStatus=" + spaceStatus + ", spaceDescription=" 
				+ spaceDescription 
				+ "]";
	}
	
}
