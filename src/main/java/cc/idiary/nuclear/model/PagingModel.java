package cc.idiary.nuclear.model;

import java.util.List;

/**
 * 分页对象
 * 
 * @author Dzb
 *
 */
public class PagingModel {

	private Long total;
	private List<?> rows;

	public PagingModel() {
	}

	public PagingModel(Long total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
