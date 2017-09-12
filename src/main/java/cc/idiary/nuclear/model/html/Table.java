package cc.idiary.nuclear.model.html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 2017/5/26.
 */
public class Table {
    private List<Trow> rows;
    private Map<String, Tcell> cellMap = new HashMap<>();

    public List<Trow> getRows() {
        return rows;
    }

    public void addRow(Trow row){
        List<Tcell> cells = row.getCells();
        cells.forEach(tcell -> {
            Tcell tmpCell = cellMap.get(tcell.getKey());
            if (tmpCell == null){
                cellMap.put(tcell.getKey(), tcell);
            } else {
                tmpCell.setRowSpan(tmpCell.getRowSpan() + 1);
                tmpCell.mergeValue(tcell.getValue());
            }
        });

        cells.add(new Tcell<Object>((a,b)-> {return a;}));
    }
}
