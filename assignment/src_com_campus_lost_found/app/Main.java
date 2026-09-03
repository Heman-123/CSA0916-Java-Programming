package com.campus.lostfound.app;

import com.campus.lostfound.gui.SmartCampusMainFrame;
import com.campus.lostfound.model.ClaimRecord;
import com.campus.lostfound.model.FoundItem;
import com.campus.lostfound.model.LostItem;
import com.campus.lostfound.model.MatchResult;
import com.campus.lostfound.service.LostFoundService;

import java.awt.EventQueue;
import java.util.List;

/**
 * Primary Application Entry Point for Smart Campus Lost & Found System.
 * Supports both Graphical User Interface (AWT Desktop Frame) and Command Line Interface (CLI).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  SMART CAMPUS LOST & FOUND AND ASSET RECOVERY SYSTEM");
        System.out.println("  Course: CSA09 - Programming in Java (CO4 / CO5 Capstone Implementation)");
        System.out.println("================================================================================");

        if (args.length > 0 && args[0].equalsIgnoreCase("--cli")) {
            runCliDemo();
            return;
        }

        // Launch AWT Desktop GUI
        EventQueue.invokeLater(() -> {
            try {
                SmartCampusMainFrame frame = new SmartCampusMainFrame();
                frame.setVisible(true);
                System.out.println("[Main] GUI Application launched successfully.");
            } catch (Exception e) {
                System.err.println("[Main] Failed to initialize GUI in current graphical environment: " + e.getMessage());
                System.out.println("[Main] Falling back to automated CLI demonstration mode...");
                runCliDemo();
            }
        });
    }

    private static void runCliDemo() {
        System.out.println("\n[CLI Engine] Starting automated full-workflow demonstration...");
        LostFoundService service = new LostFoundService();

        try {
            System.out.println("\n--- STEP 1: REPORT LOST ITEM ---");
            LostItem lost = service.reportLostItem(
                    "MacBook Pro 16 M2 Space Gray",
                    "Electronics",
                    "Has GitHub sticker on lid and USB-C dongle attached",
                    "Computer Science Dept 3rd Floor Lab",
                    "2026-09-01",
                    "David Miller",
                    "david.m@campus.edu",
                    "STU-9921",
                    100.0,
                    "9842",
                    "HIGH"
            );
            System.out.println("✔ Logged Lost Item: " + lost.getItemId() + " | Owner: " + lost.getReporterName());
            System.out.println("✔ Secret PIN Salt: " + lost.getClaimPinSalt());
            System.out.println("✔ Stored SHA-256 Hash: " + lost.getClaimPinHash());

            System.out.println("\n--- STEP 2: REPORT FOUND ITEM ---");
            FoundItem found = service.reportFoundItem(
                    "Apple Laptop Space Gray with Tech Stickers",
                    "Electronics",
                    "Left on desk near CS Lab 301",
                    "Computer Science Dept Lab 301",
                    "2026-09-01",
                    "Custodian Bob",
                    "security@campus.edu",
                    "STAFF-401",
                    "Locker #05B",
                    "Officer Wilson",
                    "Developer stickers on back casing"
            );
            System.out.println("✔ Logged Found Item: " + found.getItemId() + " | Stored in: " + found.getStorageLocker());

            System.out.println("\n--- STEP 3: RUN AUTO-MATCHING ENGINE ---");
            List<MatchResult> matches = service.getMatchesForLostItem(lost);
            System.out.println("✔ Matching Engine evaluated " + matches.size() + " candidates:");
            for (MatchResult mr : matches) {
                System.out.println("  " + mr);
            }

            System.out.println("\n--- STEP 4: SUBMIT CLAIM & VERIFY SHA-256 PIN ---");
            System.out.println("Attempting verification with correct PIN (9842)...");
            ClaimRecord claim = service.submitAndVerifyClaim(
                    lost.getItemId(),
                    found.getItemId(),
                    "David Miller",
                    "STU-9921",
                    "david.m@campus.edu",
                    "9842",
                    "Purchased at Apple Store, serial matching C02..."
            );
            System.out.println("✔ Claim Verification SUCCESS: " + claim.getClaimId() + " (Verified=" + claim.isVerified() + ")");

            System.out.println("\n--- STEP 5: FINALIZE ASSET RETURN ---");
            service.completeAssetReturn(claim.getClaimId(), "Verified in person at Campus Security Desk.");
            System.out.println("✔ Asset handover complete. Status: RESOLVED_RETURNED");

            System.out.println("\n--- STEP 6: OBJECT SERIALIZATION & CSV EXPORT ---");
            service.triggerSerializationBackup();
            String csv = service.exportReports();
            System.out.println("✔ CSV Report exported to: " + csv);

            System.out.println("\n================================================================================");
            System.out.println("  FULL END-TO-END WORKFLOW VERIFIED SUCCESSFULLY!");
            System.out.println("================================================================================\n");

        } catch (Exception e) {
            System.err.println("[CLI Demo Error] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
