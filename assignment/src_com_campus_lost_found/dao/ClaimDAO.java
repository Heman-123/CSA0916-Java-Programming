package com.campus.lostfound.dao;

import com.campus.lostfound.db.DatabaseManager;
import com.campus.lostfound.exception.DatabaseException;
import com.campus.lostfound.model.ClaimRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Recovery Claim records fulfilling Course Outcome CO5.
 */
public class ClaimDAO {

    public void insertClaim(ClaimRecord claim) throws DatabaseException {
        String sql = "INSERT INTO claims (claim_id, lost_item_id, found_item_id, claimant_name, " +
                "claimant_student_id, claimant_contact, hashed_pin_attempt, salt_used, proof_description, " +
                "claim_date, verified, verification_timestamp, status, remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, claim.getClaimId());
            ps.setString(2, claim.getLostItemId());
            ps.setString(3, claim.getFoundItemId());
            ps.setString(4, claim.getClaimantName());
            ps.setString(5, claim.getClaimantStudentId());
            ps.setString(6, claim.getClaimantContact());
            ps.setString(7, claim.getHashedPinAttempt());
            ps.setString(8, claim.getSaltUsed());
            ps.setString(9, claim.getProofDescription());
            ps.setString(10, claim.getClaimDate());
            ps.setInt(11, claim.isVerified() ? 1 : 0);
            ps.setLong(12, claim.getVerificationTimestamp());
            ps.setString(13, claim.getStatus());
            ps.setString(14, claim.getRemarks());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting claim record #" + claim.getClaimId() + ": " + e.getMessage(), e);
        }
    }

    public ClaimRecord getClaimById(String claimId) throws DatabaseException {
        String sql = "SELECT * FROM claims WHERE claim_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, claimId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapClaim(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching claim #" + claimId + ": " + e.getMessage(), e);
        }
        return null;
    }

    public List<ClaimRecord> getAllClaims() throws DatabaseException {
        List<ClaimRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM claims ORDER BY claim_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapClaim(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all claims: " + e.getMessage(), e);
        }
        return list;
    }

    public List<ClaimRecord> getClaimsForFoundItem(String foundItemId) throws DatabaseException {
        List<ClaimRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM claims WHERE found_item_id = ? ORDER BY claim_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, foundItemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapClaim(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching claims for found item #" + foundItemId + ": " + e.getMessage(), e);
        }
        return list;
    }

    public void updateClaimStatus(String claimId, String status, boolean verified, String remarks) throws DatabaseException {
        String sql = "UPDATE claims SET status = ?, verified = ?, verification_timestamp = ?, remarks = ? WHERE claim_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, verified ? 1 : 0);
            ps.setLong(3, verified ? System.currentTimeMillis() : 0L);
            ps.setString(4, remarks);
            ps.setString(5, claimId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error updating claim #" + claimId + ": " + e.getMessage(), e);
        }
    }

    public boolean deleteClaim(String claimId) throws DatabaseException {
        String sql = "DELETE FROM claims WHERE claim_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, claimId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting claim #" + claimId + ": " + e.getMessage(), e);
        }
    }

    private ClaimRecord mapClaim(ResultSet rs) throws SQLException {
        ClaimRecord c = new ClaimRecord(
                rs.getString("claim_id"),
                rs.getString("lost_item_id"),
                rs.getString("found_item_id"),
                rs.getString("claimant_name"),
                rs.getString("claimant_student_id"),
                rs.getString("claimant_contact"),
                rs.getString("hashed_pin_attempt"),
                rs.getString("salt_used"),
                rs.getString("proof_description"),
                rs.getInt("verified") == 1,
                rs.getString("status"),
                rs.getString("remarks")
        );
        c.setClaimDate(rs.getString("claim_date"));
        c.setVerificationTimestamp(rs.getLong("verification_timestamp"));
        return c;
    }
}
