package cc.idiary.nuclear.model.html;

import java.util.function.BiFunction;

/**
 * Created by Neo on 2017/5/26.
 */
public class Tcell<T> {
    private String key;
    private T value;
    private int rowSpan = 1;
    private int colSpan = 1;
    private boolean display = true;

    private BiFunction<T, T, T> merge;

    public Tcell(BiFunction<T, T, T> merge) {
        this.merge = merge;
    }

    /**
     *
     * @param nVal 新添加内容
     */
    public void mergeValue(T nVal){
        this.value = merge.apply(this.value, nVal);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }
}
