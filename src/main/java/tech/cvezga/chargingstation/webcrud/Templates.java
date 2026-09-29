package tech.cvezga.chargingstation.webcrud;

import java.util.Map;

public class Templates {

    static final String LABEL = "<label id=\"$id\">$label: </label>";
    static final String INPUT = "<input id=\"$id\" type=\"text\" name=\"$name\" value=\"$value\"/>";

    static final String HTML = """
             <!DOCTYPE html>
                    <html>
                    <head>
                        <link rel="stylesheet" href="/css/style.css">
                    </head>
                    <body>
                        <main>
                            $main
                        </main>
                    </body>
                    </html>
            """;

    static final String COMBOBOX = """
                     <select id="$id" name="$name">
                        <option value="">-- Select --</option>
                        $options
                    </select>
            """;

    static final String COMBOBOX_OPTION = """
                        <option value="$value">$label</option>
            """;

    public static String getLabelAndInput(String id, String name, String value) {
        return LABEL.replace("$id", id).replace("$label", name)
                + INPUT.replace("$id", id).replace("$name", name).replace("$value", value);
    }

    public static String getInput(String id, String name, String value) {
        return INPUT.replace("$id", id).replace("$name", name).replace("$value", value);
    }

    public static String getLabel(String id, String name) {
        return LABEL.replace("$id", id).replace("$label", name);
    }

    public static String getHtml(String main) {
        return HTML.replace("$main", main);
    }

    public static String getComboBox(String id, String name, Map<String,String> option) {
        String combobox = COMBOBOX.replace("id", name).replace("$name", name);
        StringBuilder options = new StringBuilder();
        for(Map.Entry<String, String> entry : option.entrySet()) {
            options.append(COMBOBOX_OPTION.replace("$value", entry.getKey()).replace("$label", entry.getValue()));
        }
        combobox =  combobox.replace("$options", options.toString());
        return combobox;
    }
}
