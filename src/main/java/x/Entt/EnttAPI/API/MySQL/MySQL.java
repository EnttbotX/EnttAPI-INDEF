package x.Entt.EnttAPI.API.MySQL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQL {

    private Connection connection;
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();

    public boolean connect(String host, String port, String database, String username, String password) {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true&serverTimezone=UTC",
                    username,
                    password
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Document document(String table, String id) {
        return new Document(table, id);
    }

    public class Document {

        private final String table;
        private final String id;
        private final Map<String, Object> cache = new HashMap<>();

        public Document(String table, String id) {
            this.table = table;
            this.id = id;

            try (PreparedStatement ps = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                            "`id` VARCHAR(255) PRIMARY KEY," +
                            "`data` LONGTEXT" +
                            ")"
            )) {
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            loadFromDatabase();
        }

        private void loadFromDatabase() {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT `data` FROM `" + table + "` WHERE `id` = ?"
            )) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("data");
                        if (json != null && !json.isEmpty()) {
                            Map<String, Object> databaseMap = GSON.fromJson(json, MAP_TYPE);
                            if (databaseMap != null) {
                                cache.putAll(databaseMap);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Document set(String key, Object value) {
            cache.put(key, value);
            return this;
        }

        public void save() {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO `" + table + "` (`id`, `data`) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE `data` = ?"
            )) {
                String jsonString = GSON.toJson(cache);

                ps.setString(1, id);
                ps.setString(2, jsonString);
                ps.setString(3, jsonString);

                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getString(String key) {
            Object val = cache.get(key);
            return val != null ? val.toString() : null;
        }

        public int getInt(String key) {
            Object val = cache.get(key);
            if (val instanceof Number) return ((Number) val).intValue();
            try {
                return val != null ? Integer.parseInt(val.toString()) : 0;
            } catch (NumberFormatException e) {
                try {
                    return (int) Double.parseDouble(val.toString());
                } catch (Exception ex) {
                    return 0;
                }
            }
        }

        public long getLong(String key) {
            Object val = cache.get(key);
            if (val instanceof Number) return ((Number) val).longValue();
            try {
                return val != null ? Long.parseLong(val.toString()) : 0L;
            } catch (Exception e) {
                try {
                    return (long) Double.parseDouble(val.toString());
                } catch (Exception ex) {
                    return 0L;
                }
            }
        }

        public double getDouble(String key) {
            Object val = cache.get(key);
            if (val instanceof Number) return ((Number) val).doubleValue();
            try {
                return val != null ? Double.parseDouble(val.toString()) : 0.0D;
            } catch (Exception e) {
                return 0.0D;
            }
        }

        public boolean getBoolean(String key) {
            Object val = cache.get(key);
            if (val instanceof Boolean) return (boolean) val;
            return val != null && Boolean.parseBoolean(val.toString());
        }

        public boolean exists() {
            if (!cache.isEmpty()) return true;

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT 1 FROM `" + table + "` WHERE `id` = ?"
            )) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (Exception e) {
                return false;
            }
        }

        public void delete() {
            cache.clear();
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM `" + table + "` WHERE `id` = ?"
            )) {
                ps.setString(1, id);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}