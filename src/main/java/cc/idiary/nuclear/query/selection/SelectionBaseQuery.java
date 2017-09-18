package cc.idiary.nuclear.query.selection;

import cc.idiary.nuclear.query.PagingQuery;

public abstract class SelectionBaseQuery extends PagingQuery {
    private Boolean current;    //是否查询当前正在进行中的活动
    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
