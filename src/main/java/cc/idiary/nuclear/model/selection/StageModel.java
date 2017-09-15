package cc.idiary.nuclear.model.selection;

import cc.idiary.nuclear.model.BaseModel;

public class StageModel extends BaseModel {
    private String name;
    private int code;
    private boolean current;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
