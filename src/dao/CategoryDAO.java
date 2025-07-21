package dao;

import java.sql.*;
import java.util.*;

public class CategoryDAO {
    public Map<Integer, String> getAllCategories() {
        Map<Integer, String> map = new LinkedHashMap<>();
        String sql = "SELECT id, name FROM categories ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
}