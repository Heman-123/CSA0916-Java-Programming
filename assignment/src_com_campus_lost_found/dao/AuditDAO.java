package com.campus.lostfound.dao;

import com.campus.lostfound.db.DatabaseManager;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.AuditLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for persisting and reading Audit Trail logs to SQLite database (CO5).
 */
public class AuditDAO {

    public void insertLog(AuditLog log) throws DatabaseException {
        String sql = "INSERT INTO audit_logs (timestamp, action, entity_type, entity_id, actor, details, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getTimestamp());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getEntityType());
            ps.setString(4, log.getEntityId());
            ps.setString(5, log.getActor());
            ps.setString(6, log.getDetails());
            ps.setString(7, log.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting audit log: " + e.getMessage(), e);
        }
    }

    public List<AuditLog> getAllLogs(int limit) throws DatabaseException {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY log_id DESC LIMIT ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 100);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog(
                            rs.getLong("log_id"),
                            rs.getString("action"),
                            rs.getString("entity_type"),
                            rs.getString("entity_id"),
                            rs.getString("actor"),
                            rs.getString("details"),
                            rs.getString("status")
                    );
                    log.setTimestamp(rs.getString("timestamp"));
                    list.add(log);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching audit logs: " + e.getMessage(), e);
        }
        return list;
    }
}
