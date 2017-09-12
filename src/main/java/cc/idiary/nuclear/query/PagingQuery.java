package cc.idiary.nuclear.query;

public class PagingQuery extends BaseQuery {
	private int page;
	private int size;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "PagingQuery [page=" + page + ", size=" + size + "]";
	}
}
