package db.training.easy.util.displaytag.pagination;

import java.util.ArrayList;
import java.util.List;

import org.displaytag.properties.SortOrderEnum;

public class PaginatedList<T> implements org.displaytag.pagination.PaginatedList {

	private List<T> list;

	private int count;

	private int pageNumber;

	private int fullListSize;

	private SortOrderEnum sortDirection;

	private String sortCriterion;

	public static final int LIST_OBJECTS_PER_PAGE = 20;

	public PaginatedList() {
		this(new ArrayList<T>());
	}

	public PaginatedList(List<T> list) {
		setList(list);
	}

	public void setFullListSize(int size) {
		this.fullListSize = size;
	}

	public int getFullListSize() {
		return fullListSize;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}

	public void setObjectsPerPage(int count) {
		this.count = count;
	}

	public int getObjectsPerPage() {
		return count;
	}

	public void setPageNumber(int page) throws IndexOutOfBoundsException {
		if (page < 1) {
			throw new IndexOutOfBoundsException("Seitenzählung beginnt mit 1.");
		}
		this.pageNumber = page;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public String getSearchId() {
		return null;
	}

	public void setSortCriterion(String criterion) {
		this.sortCriterion = criterion;
	}

	public String getSortCriterion() {
		return sortCriterion;
	}

	public void setSortDirection(SortOrderEnum dir) {
		this.sortDirection = dir;
	}

	public SortOrderEnum getSortDirection() {
		return sortDirection;
	}

}
