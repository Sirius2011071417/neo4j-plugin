package com.domain;

import java.util.List;

import javax.ws.rs.core.StreamingOutput;

public class Page {
	
	private int pageSize=5;
	private int pageIndex;
	private int startIndex;
	private long totalCount;
	private List list;
	private StreamingOutput stream;
	
	public Page(long totalCount, int pageIndex, int pageSize) {
		this.totalCount = totalCount;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		
		this.startIndex = (this.pageIndex-1) * this.pageSize;
		
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public StreamingOutput getStream() {
		return stream;
	}
	public void setStream(StreamingOutput stream) {
		this.stream = stream;
	}
}
