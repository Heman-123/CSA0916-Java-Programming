package com.campus.lostfound.dao;

import com.campus.lostfound.db.DatabaseManager;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.FoundItem;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemStatus;
import com.campus.lostfound.model.LostItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Lost and Found item entities fulfilling Course Outcome CO5 (JDBC CRUD).
 */
public class ItemDAO {

    // ==========================================
    // 1. CREATE OPERATIONS (INSERT)
    // ==========================================

    public void insertLostItem(LostItem item) throws DatabaseException {
        String sql = "INSERT INTO lost_items (item_id, title, category, description, location, item_date, " +
                "reporter_name, reporter_contact, reporter_id, status, reward_offered, claim_pin_hash, " +
                "claim_pin_salt, urgency_level, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getTitle());
            ps.setString(3, item.getCategory());
            ps.setString(4, item.getDescription());
            ps.setString(5, item.getLocation());
            ps.setString(6, item.getDate());
            ps.setString(7, item.getReporterName());
            ps.setString(8, item.getReporterContact());
            ps.setString(9, item.getReporterId());
            ps.setString(10, item.getStatus().name());
            ps.setDouble(11, item.getRewardOffered());
            ps.setString(12, item.getClaimPinHash());
            ps.setString(13, item.getClaimPinSalt());
            ps.setString(14, item.getUrgencyLevel());
            ps.setLong(15, item.getCreatedAtTimestamp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting lost item #" + item.getItemId() + ": " + e.getMessage(), e);
        }
    }

    public void insertFoundItem(FoundItem item) throws DatabaseException {
        String sql = "INSERT INTO found_items (item_id, title, category, description, location, item_date, " +
                "reporter_name, reporter_contact, reporter_id, status, storage_locker, custody_officer, " +
                "secret_feature, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getTitle());
            ps.setString(3, item.getCategory());
            ps.setString(4, item.getDescription());
            ps.setString(5, item.getLocation());
            ps.setString(6, item.getDate());
            ps.setString(7, item.getReporterName());
            ps.setString(8, item.getReporterContact());
            ps.setString(9, item.getReporterId());
            ps.setString(10, item.getStatus().name());
            ps.setString(11, item.getStorageLocker());
            ps.setString(12, item.getCustodyOfficer());
            ps.setString(13, item.getSecretDistinguishingFeature());
            ps.setLong(14, item.getCreatedAtTimestamp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting found item #" + item.getItemId() + ": " + e.getMessage(), e);
        }
    }

    // ==========================================
    // 2. RETRIEVE OPERATIONS (READ)
    // ==========================================

    public LostItem getLostItemById(String itemId) throws DatabaseException {
        String sql = "SELECT * FROM lost_items WHERE item_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapLostItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching lost item #" + itemId + ": " + e.getMessage(), e);
        }
        return null;
    }

    public FoundItem getFoundItemById(String itemId) throws DatabaseException {
        String sql = "SELECT * FROM found_items WHERE item_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFoundItem(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching found item #" + itemId + ": " + e.getMessage(), e);
        }
        return null;
    }

    public List<LostItem> getAllLostItems() throws DatabaseException {
        List<LostItem> list = new ArrayList<>();
        String sql = "SELECT * FROM lost_items ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapLostItem(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all lost items: " + e.getMessage(), e);
        }
        return list;
    }

    public List<FoundItem> getAllFoundItems() throws DatabaseException {
        List<FoundItem> list = new ArrayList<>();
        String sql = "SELECT * FROM found_items ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapFoundItem(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all found items: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Item> getAllItems() throws DatabaseException {
        List<Item> all = new ArrayList<>();
        all.addAll(getAllLostItems());
        all.addAll(getAllFoundItems());
        return all;
    }

    public List<LostItem> searchLostItems(String query, String category, String status) throws DatabaseException {
        List<LostItem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM lost_items WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (title LIKE ? OR description LIKE ? OR location LIKE ?) ");
            String wild = "%" + query.trim() + "%";
            params.add(wild);
            params.add(wild);
            params.add(wild);
        }
        if (category != null && !category.equals("ALL") && !category.trim().isEmpty()) {
            sql.append("AND category = ? ");
            params.add(category.trim());
        }
        if (status != null && !status.equals("ALL") && !status.trim().isEmpty()) {
            sql.append("AND status = ? ");
            params.add(status.trim());
        }
        sql.append("ORDER BY created_at DESC");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapLostItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching lost items: " + e.getMessage(), e);
        }
        return list;
    }

    public List<FoundItem> searchFoundItems(String query, String category, String status) throws DatabaseException {
        List<FoundItem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM found_items WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (title LIKE ? OR description LIKE ? OR location LIKE ?) ");
            String wild = "%" + query.trim() + "%";
            params.add(wild);
            params.add(wild);
            params.add(wild);
        }
        if (category != null && !category.equals("ALL") && !category.trim().isEmpty()) {
            sql.append("AND category = ? ");
            params.add(category.trim());
        }
        if (status != null && !status.equals("ALL") && !status.trim().isEmpty()) {
            sql.append("AND status = ? ");
            params.add(status.trim());
        }
        sql.append("ORDER BY created_at DESC");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapFoundItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching found items: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Smart SQL query to find candidates matching category, location, or date proximity.
     */
    public List<FoundItem> findCandidatesForLostItem(LostItem lost) throws DatabaseException {
        List<FoundItem> candidates = new ArrayList<>();
        String sql = "SELECT * FROM found_items WHERE status IN ('REPORTED_FOUND', 'MATCHED') " +
                "AND (category = ? OR location LIKE ? OR title LIKE ?) ORDER BY created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lost.getCategory());
            ps.setString(2, "%" + lost.getLocation() + "%");
            ps.setString(3, "%" + lost.getTitle() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(mapFoundItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error querying matching candidates: " + e.getMessage(), e);
        }
        return candidates;
    }

    // ==========================================
    // 3. UPDATE OPERATIONS
    // ==========================================

    public void updateLostItem(LostItem item) throws DatabaseException {
        String sql = "UPDATE lost_items SET title=?, category=?, description=?, location=?, item_date=?, " +
                "reporter_name=?, reporter_contact=?, reporter_id=?, status=?, reward_offered=?, " +
                "urgency_level=? WHERE item_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTitle());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getDescription());
            ps.setString(4, item.getLocation());
            ps.setString(5, item.getDate());
            ps.setString(6, item.getReporterName());
            ps.setString(7, item.getReporterContact());
            ps.setString(8, item.getReporterId());
            ps.setString(9, item.getStatus().name());
            ps.setDouble(10, item.getRewardOffered());
            ps.setString(11, item.getUrgencyLevel());
            ps.setString(12, item.getItemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating lost item #" + item.getItemId() + ": " + e.getMessage(), e);
        }
    }

    public void updateFoundItem(FoundItem item) throws DatabaseException {
        String sql = "UPDATE found_items SET title=?, category=?, description=?, location=?, item_date=?, " +
                "reporter_name=?, reporter_contact=?, reporter_id=?, status=?, storage_locker=?, " +
                "custody_officer=?, secret_feature=? WHERE item_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getTitle());
            ps.setString(2, item.getCategory());
            ps.setString(3, item.getDescription());
            ps.setString(4, item.getLocation());
            ps.setString(5, item.getDate());
            ps.setString(6, item.getReporterName());
            ps.setString(7, item.getReporterContact());
            ps.setString(8, item.getReporterId());
            ps.setString(9, item.getStatus().name());
            ps.setString(10, item.getStorageLocker());
            ps.setString(11, item.getCustodyOfficer());
            ps.setString(12, item.getSecretDistinguishingFeature());
            ps.setString(13, item.getItemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating found item #" + item.getItemId() + ": " + e.getMessage(), e);
        }
    }

    public void updateItemStatus(String itemId, String itemType, ItemStatus newStatus) throws DatabaseException {
        String table = "LOST".equalsIgnoreCase(itemType) ? "lost_items" : "found_items";
        String sql = "UPDATE " + table + " SET status = ? WHERE item_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setString(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating status of item #" + itemId + ": " + e.getMessage(), e);
        }
    }

    // ==========================================
    // 4. DELETE OPERATIONS
    // ==========================================

    public boolean deleteItem(String itemId, String itemType) throws DatabaseException {
        String table = "LOST".equalsIgnoreCase(itemType) ? "lost_items" : "found_items";
        String sql = "DELETE FROM " + table + " WHERE item_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting item #" + itemId + ": " + e.getMessage(), e);
        }
    }

    // ==========================================
    // Helper Row Mappers
    // ==========================================

    private LostItem mapLostItem(ResultSet rs) throws SQLException {
        LostItem item = new LostItem(
                rs.getString("item_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("item_date"),
                rs.getString("reporter_name"),
                rs.getString("reporter_contact"),
                rs.getString("reporter_id"),
                rs.getDouble("reward_offered"),
                rs.getString("claim_pin_hash"),
                rs.getString("claim_pin_salt"),
                rs.getString("urgency_level")
        );
        item.setStatus(ItemStatus.fromString(rs.getString("status")));
        item.setCreatedAtTimestamp(rs.getLong("created_at"));
        return item;
    }

    private FoundItem mapFoundItem(ResultSet rs) throws SQLException {
        FoundItem item = new FoundItem(
                rs.getString("item_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("item_date"),
                rs.getString("reporter_name"),
                rs.getString("reporter_contact"),
                rs.getString("reporter_id"),
                rs.getString("storage_locker"),
                rs.getString("custody_officer"),
                rs.getString("secret_feature")
        );
        item.setStatus(ItemStatus.fromString(rs.getString("status")));
        item.setCreatedAtTimestamp(rs.getLong("created_at"));
        return item;
    }
}
