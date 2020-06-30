package net.oopscraft.core;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.oopscraft.core.mybatis.PageRowBounds;

public class Pagination {

	int page = 1;
	int rows = Integer.MAX_VALUE;
	boolean enableTotalCount = false;
	long totalCount = -1;
	
	public Pagination() {
		
	}

	public Pagination(int rows, int page) {
		this.rows = rows;
		this.page = page;
	}

	public Pagination(int rows, int page, boolean enableTotalCount) {
		this(rows, page);
		this.enableTotalCount = enableTotalCount;
	}
	
	/**
	 * Returns spring-data PageRequest object.
	 * @return
	 */
	public PageRequest toPageRequest() {
		return new PageRequest(page - 1, rows);
	}
	
	/**
	 * Returns spring-data PageRequest object.
	 * @param sort
	 * @return
	 */
	public PageRequest toPageRequest(Sort sort) {
		return new PageRequest(page - 1, rows, sort);
	}

	/**
	 * Returns MYBATIS RowBounds object.
	 * @return
	 */
	public PageRowBounds toRowBounds() {
		return new PageRowBounds(getOffset(), getLimit(), enableTotalCount);
	}
	
	/**
	 * Gets Content-Range value
	 * 
	 * @param response
	 */
	public String getContentRange() {
		StringBuffer contentRange = new StringBuffer();
		contentRange.append("items");
		contentRange.append(" ");
		contentRange.append(getOffset() + 1).append("-").append(getOffset() + getLimit());
		contentRange.append("/");
		contentRange.append(isEnableTotalCount() ? getTotalCount() : "*");
		return contentRange.toString();
	}
	
	/**
	 * toResponse
	 * @param response
	 */
	public void setContentRangeHeader(HttpServletResponse response) {
		response.setHeader("Content-Range", this.getContentRange());
	}
	
	public int getOffset() {
		return (rows * page) - this.rows;
	}


	public int getLimit() {
		return rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public boolean isEnableTotalCount() {
		return enableTotalCount;
	}

	public void setEnableTotalCount(boolean enableTotalCount) {
		this.enableTotalCount = enableTotalCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	




}
