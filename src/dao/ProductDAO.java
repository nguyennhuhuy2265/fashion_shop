package dao;

import java.sql.*;
import java.util.*;
import model.Product;

public class ProductDAO {

    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price"), // đổi sang int
                            rs.getInt("quantity"),
                            rs.getString("size"),
                            rs.getString("color"),
                            rs.getString("material"),
                            rs.getString("image_url"),
                            rs.getInt("category_id"),
                            rs.getString("category_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS categoryName FROM products p JOIN categories c ON p.category_id = c.id";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"), // đổi sang int
                        rs.getInt("quantity"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("image_url"),
                        rs.getInt("category_id"),
                        rs.getString("categoryName")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (name, description, price, quantity, size, color, material, image_url, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getPrice()); // đổi sang int
            ps.setInt(4, p.getQuantity());
            ps.setString(5, p.getSize());
            ps.setString(6, p.getColor());
            ps.setString(7, p.getMaterial());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.getCategoryId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET name=?, description=?, price=?, quantity=?, size=?, color=?, material=?, image_url=?, category_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getPrice()); // đổi sang int
            ps.setInt(4, p.getQuantity());
            ps.setString(5, p.getSize());
            ps.setString(6, p.getColor());
            ps.setString(7, p.getMaterial());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.getCategoryId());
            ps.setInt(10, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> searchProductByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS categoryName FROM products p JOIN categories c ON p.category_id = c.id WHERE p.name LIKE ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"), // đổi sang int
                        rs.getInt("quantity"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("image_url"),
                        rs.getInt("category_id"),
                        rs.getString("categoryName")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> searchProduct(String keyword, int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS categoryName FROM products p JOIN categories c ON p.category_id = c.id WHERE p.name LIKE ?";

        if (categoryId != -1) {
            sql += " AND p.category_id = ?";
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            if (categoryId != -1) {
                ps.setInt(2, categoryId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("price"), // đổi sang int
                        rs.getInt("quantity"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getString("material"),
                        rs.getString("image_url"),
                        rs.getInt("category_id"),
                        rs.getString("categoryName")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean increaseQuantityById(int productId, int quantityToAdd) {
        String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantityToAdd);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
