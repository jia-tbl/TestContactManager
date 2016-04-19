package com.zhang.contacts;

public class ContactBean {
	private int contactId;

	private String desplayName;
	private String phoneNum;
	private Long photoId;
	/**
	 * Sort key that takes into account locale-based traditions for sorting
	 * names in address books. The default sort key is
	 * {@link #DISPLAY_NAME_PRIMARY}. For Chinese names the sort key is the
	 * name's Pinyin spelling, and for Japanese names it is the Hiragana version
	 * of the phonetic name.
	 */
	private String sortKey;
	private String lookUpKey;

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getDesplayName() {
		return desplayName;
	}

	public void setDesplayName(String desplayName) {
		this.desplayName = desplayName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getLookUpKey() {
		return lookUpKey;
	}

	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}
}
