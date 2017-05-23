package com.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Query {
	private String orgName;
	private int pageSize;
	private int page;

	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String toString() {
		return orgName + " " + pageSize + " " + page; 
	}
}
