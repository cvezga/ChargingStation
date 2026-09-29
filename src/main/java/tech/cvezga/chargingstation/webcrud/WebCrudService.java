package tech.cvezga.chargingstation.webcrud;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.cvezga.chargingstation.entity.Villa;
import tech.cvezga.chargingstation.repository.Repositories;
import tech.cvezga.chargingstation.repository.VillaRepository;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebCrudService {

    @Value("${entity.package}")
    private String entityPackage;

    private final VillaRepository villaRepository;

    private final Repositories repositories;

    private Map<String, String> htmlMap = new HashMap<>();

    private final DataSource dataSource;

    public WebCrudService(VillaRepository villaRepository, Repositories repositories, DataSource dataSource) {
        this.villaRepository = villaRepository;
        this.repositories = repositories;
        this.dataSource = dataSource;
    }

    public String getList(String entity) throws ClassNotFoundException {
        String className = entityPackage + "." + entity;

        if (htmlMap.containsKey(className)) {
            return htmlMap.get(className);
        }

        Class<?> clazz = Class.forName(className);

        Field[] fields = clazz.getDeclaredFields();

        StringBuilder main = new StringBuilder();
        main.append("<h1>").append(entity).append("</h1>\n");

        main.append("<form action=\"/entity/save\" method=\"post\">\n");
        main.append("<input id=\"entity\"  name=\"entity\" type=\"text\" value=\"" + entity + "\" hidden/>\n");

        Table table = new Table();
        for (Field field : fields) {
            table.addColum(Templates.getLabel(field.getName(), field.getName()));
            if (field.getType().isEnum()) {
                Object[] values = field.getType().getEnumConstants();
                Map<String, String> map = new HashMap<>();
                for (Object value : values) {
                    map.put(value.toString(), value.toString());
                }
                table.addColum(Templates.getComboBox(field.getName(), field.getName(), map));
            } else if (field.getType().getCanonicalName().startsWith(entityPackage)) {
                table.addColum(Templates.getComboBox(field.getName(), field.getName(), executeQuery(field.getName())));
            } else {
                table.addColum(Templates.getInput(field.getName(), field.getName(), ""));
            }
            table.addRow();
        }

        main.append(table.getTable());
        main.append("<button type=\"submit\">Save</button>\n");
        main.append("</form>\n");

        String html = Templates.getHtml(main.toString());
        htmlMap.put(className, html);

        return html;
    }

    public String save(Map<String, Object> map) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        String className = entityPackage + "." + map.get("entity");

        Class<?> clazz = Class.forName(className);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("id")) continue;
            try {
                field.setAccessible(true);
                if (field.getType().isAnnotationPresent(Entity.class)) {
                    long id = Long.parseLong((String) map.get(field.getName()));
                    Object t = repositories.getEntity(id,field.getType());
                    field.set(instance, t);
                    System.out.println("Field is an Entity");
                } else    if (field.getType().isEnum()) {

                    Object value = convertValue(
                            field,
                            map.get(field.getName())
                    );

                    field.set(instance, value);
                } else  if (field.getType() == long.class || field.getType() == Long.class) {

                    Object value = convertValue(
                            field,
                            map.get(field.getName())
                    );

                    field.set(instance, Long.parseLong((String) value));
                } else {

                    field.set(instance, map.get(field.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        repositories.save(instance);

        return null;
    }


    private Map<String, String> executeQuery(String table) {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT id, name FROM " + table.toLowerCase() + "s order by name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();

            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                map.put(String.valueOf(id), name);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private Object convertValue(Field field, Object value) {

        if (value == null) {
            return null;
        }

        Class<?> type = field.getType();

        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            //return constants[((Number) value).intValue()].toString();
            return Enum.valueOf(
                    (Class<? extends Enum>) type,
                    value.toString()
            );
        }

        return value;
    }
}
