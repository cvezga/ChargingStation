package tech.cvezga.chargingstation.webcrud;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<String> rows = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    public void addRow() {
        if (!columns.isEmpty()) {
            StringBuilder row = new StringBuilder();
            row.append("<tr>");
            for (String column : columns) {
                row.append("<td>").append(column).append("</td>");
            }
            row.append("</tr>");
            rows.add(row.toString());
        }
        columns = new ArrayList<>();
    }

    public void addColum(String value) {
        columns.add(value);
    }

    public String getTable() {
        StringBuilder table = new StringBuilder();
        table.append("<table>\n");
        for (String row : rows) {
            table.append(row).append("\n");
        }
        table.append("</table>\n");
        return table.toString();
    }
}

