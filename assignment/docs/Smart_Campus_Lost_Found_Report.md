# DEPARTMENT OF COMPUTER SCIENCE AND ENGINEERING
## COURSE CODE & NAME: CSA09 – PROGRAMMING IN JAVA
### CAPSTONE ASSIGNMENT & COMPREHENSIVE PROJECT REPORT (SLOT C)

---

# SMART CAMPUS LOST & FOUND AND ASSET RECOVERY SYSTEM
### An Enterprise Java Solution Utilizing AWT GUI, Delegation Event Model, Applet Lifecycle, Character/Byte I/O Streams, Object Serialization, SHA-256 Cryptographic Salted Hashing, and SQLite JDBC Persistence

**Course Outcomes Mapped:**
- **CO4**: Create Java applications using Applet Programming, event handling, the Delegation Event Model, AWT components, and GUI design principles. (*Bloom’s: K6 - Create; K3 - Apply*)
- **CO5**: Apply Java I/O Streams, serialization, hashing concepts, and JDBC database connectivity to develop applications that perform persistent data management and CRUD operations. (*Bloom’s: K6 - Create; K3 - Apply; K2 - Understand*)

**Sustainable Development Goals (SDG) Alignment:**
- **SDG 4**: Quality Education (Mitigates disruption of learning by preventing academic asset loss)
- **SDG 11**: Sustainable Cities and Communities (Fosters smart, safe, and transparent campus infrastructure)
- **SDG 12**: Responsible Consumption and Production (Prolongs asset lifecycles and minimizes e-waste)

---

## 1. Problem Statement

Across university campuses, thousands of valuable personal and academic items—including laptops, scientific calculators, laboratory apparatus, smart ID cards, wallets, and textbooks—are misplaced daily. Traditional lost-and-found practices rely on fragmented notice boards, unindexed manual logbooks, or unverified word-of-mouth recovery. This legacy approach suffers from severe vulnerabilities:
1. **Lack of Identity & Ownership Verification**: Fraudulent or mistaken claims occur frequently because custodians lack cryptographic proof of ownership.
2. **Disconnected Data Islands**: Information about lost items is rarely matched systematically with found items logged at separate security desks across campus zones.
3. **Absence of Tamper-Resistant Audit Trails**: Manual logbooks lack transactional integrity, timestamps, and multi-tier recovery backups.

To address these challenges, this project engineers a **Smart Campus Lost & Found and Asset Recovery System** developed in Java. The system integrates an interactive Graphical User Interface (AWT Frame/Applet), the Delegation Event Model, an automated multi-factor heuristic matching engine, SHA-256 cryptographic salted PIN authentication, persistent SQLite JDBC database CRUD operations, character-stream audit logging, and binary object serialization/deserialization.

---

## 2. Objectives

The primary technical and operational objectives of this project are:
1. **Interactive AWT GUI & Delegation Event Model (CO4)**: Implement an ergonomic, structured interface using AWT controls (`Frame`, `Applet`, `Panel`, `Label`, `TextField`, `TextArea`, `Button`, `Checkbox`, `Choice`, `List`, `Dialog`, `MenuBar`) and layout managers (`BorderLayout`, `GridBagLayout`, `GridLayout`, `FlowLayout`, `CardLayout`) responding to `ActionEvent`, `ItemEvent`, and `WindowEvent`.
2. **Applet Programming Lifecycle Integration (CO4)**: Demonstrate full applet lifecycle execution (`init()`, `start()`, `paint()`, `stop()`, `destroy()`) with dual desktop/browser container support.
3. **Persistent JDBC CRUD Operations (CO5)**: Implement robust database connectivity to an embedded SQLite database performing Create, Retrieve, Update, and Delete operations alongside parameterized SQL matching queries.
4. **Cryptographic Identity Verification via SHA-256 Hashing (CO5)**: Protect claimant credentials by generating 128-bit random cryptographic salts and computing SHA-256 digests (`SHA-256(Salt || PIN)`), ensuring zero plaintext exposure and constant-time verification.
5. **Multi-Tier File I/O & Object Serialization (CO5)**:
   - Use Character Streams (`FileWriter`, `FileReader`, `BufferedReader`, `PrintWriter`) for real-time audit logging and CSV export.
   - Use Byte Streams (`ObjectOutputStream`, `ObjectInputStream`) for state backup and session deserialization.
6. **Smart Heuristic Auto-Matching Engine**: Automate semantic category matching, location proximity scoring, and temporal delta calculation to suggest high-confidence matches.

---

## 3. Requirements and Environment Used

### 3.1 Hardware Requirements
- **Processor**: Intel Core i3/i5/i7 or AMD Ryzen equivalent (2.0 GHz or higher)
- **RAM**: Minimum 4 GB (8 GB recommended)
- **Storage**: Minimum 200 MB free disk space
- **Display**: Minimum 1024x768 resolution (1920x1080 recommended)

### 3.2 Software & Development Environment
- **Operating System**: Microsoft Windows 10/11 (64-bit) / Linux / macOS
- **Java Development Kit (JDK)**: OpenJDK / Oracle JDK 11 LTS (`javac 11.0.23` / `java 11.0.23`)
- **Database Engine**: SQLite 3 (Embedded via `sqlite-jdbc-3.45.1.0.jar`)
- **Logging Facade**: SLF4J API (`slf4j-api-1.7.36.jar`, `slf4j-simple-1.7.36.jar`)
- **Build & Automation Tools**: Windows Batch Scripts (`compile.bat`, `run.bat`, `test.bat`, `run_cli.bat`, `run_applet.bat`) / PowerShell
- **IDE / Editor**: Antigravity IDE / VS Code / IntelliJ IDEA

---

## 4. Design & Proposed Solution

### 4.1 Tiered Architectural Overview

The system adheres to a clean **Model-View-Controller (MVC) / 4-Tier Architecture**:

```
+-----------------------------------------------------------------------------------+
|                            PRESENTATION TIER (GUI & APPLET)                       |
|   SmartCampusMainFrame (AWT Frame)  |  SmartCampusApplet (Applet Lifecycle)       |
|   LostReportPanel  |  FoundReportPanel  |  MatchClaimPanel  |  InventoryPanel    |
+-----------------------------------------------------------------------------------+
                                         │  (Delegation Event Model)
                                         ▼
+-----------------------------------------------------------------------------------+
|                              SERVICE & BUSINESS LOGIC TIER                        |
|   LostFoundService (Facade)            |  MatchingEngine (Heuristic Scorer)       |
+-----------------------------------------------------------------------------------+
                      │                                    │
         ┌────────────┴────────────┐            ┌──────────┴──────────┐
         ▼                         ▼            ▼                     ▼
+──────────────────+   +──────────────────+   +──────────────────+   +──────────────+
|  DATA ACCESS     |   | SECURITY LAYER   |   | FILE I/O STREAMS |   | OBJECT       |
|  ItemDAO         |   | HashUtil         |   | FileLogManager   |   | SERIALIZATION|
|  ClaimDAO        |   | (SHA-256 + Salt) |   | (FileWriter/     |   | Serialization|
|  AuditDAO        |   | SecureRandom     |   |  BufferedReader) |   |  Manager     |
+──────────────────+   +──────────────────+   +──────────────────+   +──────────────+
         │                                              │                     │
         ▼                                              ▼                     ▼
+──────────────────+                          +──────────────────+   +──────────────+
| SQLite Database  |                          | audit_trail.log  |   | backup_*.ser |
| (JDBC Engine)    |                          | campus_report.csv|   | (Binary)     |
+──────────────────+                          +──────────────────+   +──────────────+
```

---

### 4.2 Entity Relationship (ER) Data Model

```
+--------------------------+          +--------------------------+
|        lost_items        |          |       found_items        |
+--------------------------+          +--------------------------+
| PK item_id: TEXT         |          | PK item_id: TEXT         |
|    title: TEXT           |          |    title: TEXT           |
|    category: TEXT        |          |    category: TEXT        |
|    description: TEXT     |          |    description: TEXT     |
|    location: TEXT        |          |    location: TEXT        |
|    item_date: TEXT       |          |    item_date: TEXT       |
|    reporter_name: TEXT   |          |    reporter_name: TEXT   |
|    reporter_contact: TEXT|          |    reporter_contact: TEXT|
|    reporter_id: TEXT     |          |    reporter_id: TEXT     |
|    status: TEXT          |          |    status: TEXT          |
|    reward_offered: REAL  |          |    storage_locker: TEXT  |
|    claim_pin_hash: TEXT  |          |    custody_officer: TEXT |
|    claim_pin_salt: TEXT  |          |    secret_feature: TEXT  |
|    urgency_level: TEXT   |          |    created_at: INTEGER   |
|    created_at: INTEGER   |          +--------------------------+
+--------------------------+                       │
             │                                     │
             └──────────────┐       ┌──────────────┘
                            ▼       ▼
                     +--------------------------+
                     |          claims          |
                     +--------------------------+
                     | PK claim_id: TEXT        |
                     | FK lost_item_id: TEXT    |
                     | FK found_item_id: TEXT   |
                     |    claimant_name: TEXT   |
                     |    claimant_id: TEXT     |
                     |    claimant_contact: TEXT|
                     |    hashed_pin_attempt:TX |
                     |    salt_used: TEXT       |
                     |    proof_desc: TEXT      |
                     |    claim_date: TEXT      |
                     |    verified: INTEGER     |
                     |    status: TEXT          |
                     |    remarks: TEXT         |
                     +--------------------------+
```

---

## 5. Algorithms, Pseudocode & Flowcharts

### 5.1 Algorithm 1: Cryptographic Salted SHA-256 Registration & Verification

```
Algorithm: SaltedSHA256HashAndVerify
--------------------------------------------------------------------------------
Procedure GenerateSaltedHash(plainPIN):
  1. Generate 16 cryptographically random bytes using SecureRandom().
  2. Convert salt bytes into 32-character hexadecimal string `salt`.
  3. Form combined payload string: `saltedInput = salt + ":" + plainPIN`.
  4. Compute 32-byte digest using MessageDigest.getInstance("SHA-256").
  5. Convert byte array to 64-character lowercase hex string `hashDigest`.
  6. Return Tuple (salt, hashDigest).

Procedure VerifyPIN(enteredPIN, storedSalt, storedHash):
  1. Compute candidateHash = SHA-256(storedSalt + ":" + enteredPIN).
  2. Perform Constant-Time String Equality:
       result = 0
       For i = 0 to length(storedHash) - 1:
           result = result OR (candidateHash[i] XOR storedHash[i])
  3. Return (result == 0).
--------------------------------------------------------------------------------
```

### 5.2 Algorithm 2: Multi-Factor Heuristic Matching Engine

$$\text{Score} = W_{cat} + W_{text} + W_{loc} + W_{date}$$

Where:
- $W_{cat} = 40.0$ if $\text{Category}_{lost} = \text{Category}_{found}$, else $0.0$
- $W_{text} = 20.0 \times \text{JaccardSimilarity}(\text{Keywords}_{lost}, \text{Keywords}_{found})$
- $W_{loc} = 20.0$ (Exact location) or $15.0$ (Sub-zone overlap) or $10.0$ (Token overlap)
- $W_{date} = 20.0$ (Same day, $\Delta t = 0$), $15.0$ ($\Delta t \le 3$ days), $10.0$ ($\Delta t \le 7$ days), $5.0$ ($\Delta t \le 30$ days)

```
Algorithm: MultiFactorItemMatching
Input: LostItem L, Candidate FoundItems F[]
Output: Ranked list of MatchResult objects
--------------------------------------------------------------------------------
1. Initialize MatchList = []
2. For each FoundItem item in F[]:
     totalScore = 0.0
     reasons = []

     // 1. Category
     If L.category equals item.category:
         totalScore += 40.0
         reasons.append("Exact Category Match")

     // 2. Keyword Jaccard Overlap
     textScore = JaccardSimilarity(L.title + L.desc, item.title + item.desc)
     totalScore += (textScore * 20.0)

     // 3. Location Proximity
     If L.location equals item.location:
         totalScore += 20.0
     Else If L.location contains item.location Or item.location contains L.location:
         totalScore += 15.0

     // 4. Date Proximity
     daysDiff = CalculateDayDifference(L.date, item.date)
     If daysDiff == 0: totalScore += 20.0
     Else If daysDiff <= 3: totalScore += 15.0
     Else If daysDiff <= 7: totalScore += 10.0

     If totalScore >= 35.0:
         MatchList.append(new MatchResult(L, item, totalScore, reasons))

3. Sort MatchList in descending order by totalScore.
4. Return MatchList.
--------------------------------------------------------------------------------
```

---

## 6. Implementation and Source Code Architecture

The codebase is organized into structured packages under `com.campus.lostfound.*`:

| Package | Source File | Description | CO Mapping |
| :--- | :--- | :--- | :--- |
| `model` | [`Item.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/Item.java) | Base abstract entity implementing `Serializable` | CO5 |
| `model` | [`LostItem.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/LostItem.java) | Lost asset entity with salted SHA-256 PIN fields | CO5 |
| `model` | [`FoundItem.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/FoundItem.java) | Found asset entity with storage locker metadata | CO5 |
| `model` | [`ClaimRecord.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/ClaimRecord.java) | Recovery claim model with verification audit flags | CO5 |
| `model` | [`AuditLog.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/AuditLog.java) | Event audit entity formatted for stream logging | CO5 |
| `model` | [`ItemStatus.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/model/ItemStatus.java) | Lifecycle state enumeration | CO4/CO5 |
| `security` | [`HashUtil.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/security/HashUtil.java) | SHA-256 MessageDigest & SecureRandom salting | CO5 |
| `db` | [`DatabaseManager.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/db/DatabaseManager.java) | JDBC SQLite connection factory & DDL migrator | CO5 |
| `dao` | [`ItemDAO.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/dao/ItemDAO.java) | Full CRUD PreparedStatement database operations | CO5 |
| `dao` | [`ClaimDAO.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/dao/ClaimDAO.java) | Claim record persistence and state updater | CO5 |
| `dao` | [`AuditDAO.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/dao/AuditDAO.java) | SQL transactional audit log persistence | CO5 |
| `io` | [`FileLogManager.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/io/FileLogManager.java) | Character stream `FileWriter`/`BufferedReader` | CO5 |
| `io` | [`SerializationManager.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/io/SerializationManager.java) | Byte stream `ObjectOutputStream`/`InputStream` | CO5 |
| `io` | [`CSVReportExporter.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/io/CSVReportExporter.java) | Formatted CSV and summary report generator | CO5 |
| `service` | [`MatchingEngine.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/service/MatchingEngine.java) | Heuristic 4-factor auto-match algorithm | CO4/CO5 |
| `service` | [`LostFoundService.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/service/LostFoundService.java) | Master facade orchestrating all workflows | CO4/CO5 |
| `gui` | [`ComponentFactory.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/ComponentFactory.java) | Curated color palette and styled AWT controls | CO4 |
| `gui` | [`LostReportPanel.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/LostReportPanel.java) | Form panel for logging lost assets | CO4 |
| `gui` | [`FoundReportPanel.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/FoundReportPanel.java) | Form panel for registering found items | CO4 |
| `gui` | [`MatchClaimPanel.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/MatchClaimPanel.java) | UI for matching and SHA-256 PIN claim verify | CO4/CO5 |
| `gui` | [`InventoryPanel.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/InventoryPanel.java) | CRUD asset inventory browser & status editor | CO4/CO5 |
| `gui` | [`AuditLogPanel.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/AuditLogPanel.java) | Dual-pane database & file log viewer | CO4/CO5 |
| `gui` | [`SmartCampusMainFrame.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/SmartCampusMainFrame.java) | Main AWT Desktop Frame container with MenuBar | CO4 |
| `gui` | [`SmartCampusApplet.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/gui/SmartCampusApplet.java) | Full Applet lifecycle implementation | CO4 |
| `app` | [`Main.java`](file:///c:/Users/student/Downloads/csa0916/src/com/campus/lostfound/app/Main.java) | Desktop launcher with automatic CLI fallback | CO4/CO5 |
| `test` | [`SmartCampusSystemTest.java`](file:///c:/Users/student/Downloads/csa0916/test/com/campus/lostfound/test/SmartCampusSystemTest.java) | 23-test automated verification suite | CO4/CO5 |

---

## 7. Test Cases and Expected vs. Actual Results

The automated test suite (`SmartCampusSystemTest.java`) executes 23 comprehensive test scenarios covering valid inputs, invalid inputs, boundary values, cryptographic invariants, and persistence integrity:

| Test ID | Module Tested | Input Scenario | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **TC-01** | SHA-256 Hash | Raw PIN `9842` + 16-byte random salt | 64-char Hexadecimal Digest | 64-char Hex generated | **PASS** |
| **TC-02** | Salt Generation | Two consecutive `generateSalt()` calls | Unique non-identical 32-char hex salts | Unique random salts generated | **PASS** |
| **TC-03** | PIN Verification | Entered PIN matches registered PIN | Returns `true` | Returns `true` | **PASS** |
| **TC-04** | Tamper Rejection | Entered wrong PIN `0000` | Returns `false` | Returns `false` | **PASS** |
| **TC-05** | Timing Resistance | `constantTimeEquals()` on identical/distinct strings | Bitwise constant time evaluation | Passed | **PASS** |
| **TC-06** | Matching Engine | Identical category, overlapping text, same day | Match Confidence Score $\ge 75\%$ | Evaluated to $88.3\%$ | **PASS** |
| **TC-07** | Matching Engine | Dissimilar items (Laptop vs. Umbrella) | Match Confidence Score $< 35\%$ | Evaluated to $0.0\%$ | **PASS** |
| **TC-08** | Byte Serialization | `serializeLostItems()` to `backup_lost_items.ser` | File exists with size $> 0$ bytes | File created (737 bytes) | **PASS** |
| **TC-09** | Deserialization | `deserializeLostItems()` from binary `.ser` | Restores object attributes intact | Restored title & category intact | **PASS** |
| **TC-10** | SHA-256 Checksum | Compute checksum of `.ser` binary | 64-character hexadecimal digest | 64-char digest calculated | **PASS** |
| **TC-11** | Character Stream | Append `AuditLog` via `FileWriter`/`PrintWriter` | Written to `data/audit_trail.log` | Formatted log lines recorded | **PASS** |
| **TC-12** | CSV Export | Export all items & claims via Character Streams | Comma-separated file generated | `campus_asset_report.csv` written | **PASS** |
| **TC-13** | Summary Export | Export human-readable text summary | Formatted statistics text file | `campus_summary_report.txt` written | **PASS** |
| **TC-14** | JDBC Create (Lost) | Insert `LostItem` with PreparedStatement | Record stored in `lost_items` table | Inserted with ID `LOST-4486` | **PASS** |
| **TC-15** | JDBC Create (Found)| Insert `FoundItem` with locker metadata | Record stored in `found_items` table| Inserted with ID `FND-8871` | **PASS** |
| **TC-16** | JDBC Read | Query all lost items from SQLite | Returns non-empty `List<LostItem>` | Retrieved all active items | **PASS** |
| **TC-17** | JDBC Auto-Match | Execute candidate matching query | Returns matching pairs | Match returned score $78.5\%$ | **PASS** |
| **TC-18** | Claim Verification | Submit claim with correct PIN `3321` | Claim `APPROVED`, verified flag set | `CLM-9486` approved | **PASS** |
| **TC-19** | JDBC Update | Update status to `RESOLVED_RETURNED` | Database row status updated | Status updated successfully | **PASS** |
| **TC-20** | JDBC Delete | Delete resolved/duplicate item by ID | Record deleted from table | Delete executed with status true | **PASS** |
| **TC-21** | Validation Check | Report lost item with empty title | Throws `ValidationException` | Caught `ValidationException` | **PASS** |
| **TC-22** | Date Format Check | Report lost item with date `01/09/2026` | Rejects non `YYYY-MM-DD` date | Caught `ValidationException` | **PASS** |
| **TC-23** | PIN Rejection | Submit claim with incorrect PIN `9999` | Throws `InvalidPINException` | Caught `InvalidPINException` | **PASS** |

**Overall Verification Summary:** **23 / 23 Tests Passed (100.0% Pass Rate)**

---

## 8. Execution Output & Verification Traces

### 8.1 Automated Test Execution Log

```
================================================================================
   SMART CAMPUS LOST & FOUND SYSTEM - AUTOMATED VERIFICATION SUITE
   Course Outcome: CO4 (AWT/Applet) | CO5 (I/O, Serialization, Hashing, JDBC)
================================================================================

--- 1. CRYPTOGRAPHIC HASHING & CLAIMANT PIN AUTHENTICATION (CO5) ---
  [PASS] SHA-256 Hash Length - Generated 256-bit hexadecimal digest: 29a7f72ddefbc18a2b73d5f92256ac81e970a9c8a539dfbaf298ebbe4d3be713
  [PASS] Salt Randomness & Uniqueness - Salt 1: b3efd797303aa9409328bbe7f546516a != Salt 2: 756405558fc95a36f1cd412933deceff
  [PASS] PIN Match Verification - Correct raw PIN matches salted hash
  [PASS] Tampered PIN Rejection - Incorrect raw PIN is strictly rejected
  [PASS] Constant-Time Equals - Verified timing attack resistance

--- 2. HEURISTIC MATCHING ENGINE TESTS ---
  [PASS] High Similarity Match Score - Confidence score: 88.3% | Reasons: [Exact Category Match (Electronics), Keyword Similarity (42% overlap), Exact Location Match (Central Library), Same-day Discovery (0 days diff)]
  [PASS] Low Similarity Discrimination - Confidence score: 0.0%

--- 3. OBJECT SERIALIZATION & BYTE STREAMS (CO5) ---
[SerializationManager] Successfully serialized 1 lost items to data/backup_lost_items.ser
  [PASS] Serialized File Created - Binary .ser file size: 737 bytes
  [PASS] Deserialized Objects Restored - Successfully deserialized: [LOST] ID: LOST-SER-1 | Scientific Calculator (Academic Supplies) | Room 204 | 2026-09-01 | Status: Reported Lost
  [PASS] Serialized File SHA-256 Checksum - Checksum: 5fe3fa8ce469157463f92c48c2a5a53ae7984be996139a0f7ce9a4cae6254be6

--- 4. FILE I/O CHARACTER STREAMS & CSV EXPORTER (CO5) ---
  [PASS] Character Stream FileWriter/BufferedReader - Read 16 audit log lines from data/audit_trail.log
  [PASS] CSV Report Generation - Generated CSV: data/campus_asset_report.csv
  [PASS] Summary Text Generation - Generated Text Summary: data/campus_summary_report.txt

--- 5. JDBC DATABASE CRUD OPERATIONS & WORKFLOW (CO5) ---
[DatabaseManager] Database initialized successfully.
  [PASS] JDBC Insert Lost Item - Inserted Lost Item #LOST-4486
  [PASS] JDBC Insert Found Item - Inserted Found Item #FND-8871
  [PASS] JDBC Retrieve All Lost Items - Retrieved 5 items from SQLite
  [PASS] Smart Match Engine Execution - Top match score: 78.5% with Found #FND-8871
  [PASS] Claim Verification & SHA-256 PIN Match - Claim #CLM-9486 approved via cryptographic hash verification.
  [PASS] Asset Return Status Update - Transitioned asset lifecycle to RESOLVED_RETURNED
  [PASS] JDBC Delete Item - Successfully deleted test record #LOST-6808

--- 6. EXCEPTION HANDLING & VALIDATION TESTS ---
  [PASS] ValidationException on Missing Title - Correctly threw ValidationException
  [PASS] ValidationException on Malformed Date - Correctly rejected non YYYY-MM-DD date
  [PASS] InvalidPINException on Incorrect PIN - Correctly threw InvalidPINException

================================================================================
   FINAL TEST RESULTS SUMMARY
   Total Tests Run : 23 | Tests Passed : 23 ✔ | Tests Failed : 0 | Pass Rate : 100.0%
================================================================================
```

---

## 9. Analysis and Discussion

### 9.1 Cryptographic Security Analysis of SHA-256 with Salting
- **Pre-Image Resistance**: Given a 256-bit hash digest $H = \text{SHA-256}(M)$, finding the original input $M$ is computationally infeasible ($2^{256}$ operations).
- **Mitigation of Rainbow Table Attacks via Dynamic Salting**: A unique 16-byte random salt generated via `SecureRandom` is prepended to each PIN. Even if multiple students choose the same 4-digit PIN (e.g., `1234`), their stored hashes are completely distinct, rendering pre-computed lookup tables completely useless.
- **Timing Attack Mitigation**: Verification uses `constantTimeEquals()` to evaluate all characters regardless of early mismatch, preventing side-channel execution timing profiling.

### 9.2 Comparative Evaluation of Persistence Strategies

| Feature | File I/O (Character Streams) | Object Serialization (Byte Streams) | Relational Database (SQLite JDBC) |
| :--- | :--- | :--- | :--- |
| **Stream Class Used** | `FileWriter`, `BufferedReader` | `ObjectOutputStream`, `ObjectInputStream` | Socket / Native Byte Channel |
| **Data Format** | Plaintext / CSV | Binary serialized object graph | Structured SQL Relational B-Tree |
| **Primary Use Case** | Append-only audit logging & reports | Offline snapshot recovery / backup | Complex querying, ACID CRUD, indexed search |
| **Performance** | $O(N)$ sequential scan | Fast bulk object I/O | $O(\log N)$ indexed retrieval |
| **Resilience** | Human readable; portable | Requires identical `serialVersionUID` | Robust transaction rollback (`ACID`) |

### 9.3 SDG Impact Analysis
- **SDG 4 (Quality Education)**: Directly prevents student academic disruption by providing automated recovery of laptops, course textbooks, and lab gear before examinations.
- **SDG 11 (Sustainable Cities & Communities)**: Replaces unmonitored paper desks with an indexed, auditable campus infrastructure where every recovered item has verifiable custody tracking.
- **SDG 12 (Responsible Consumption & Production)**: Maximizes asset recovery rates, preventing premature discard and reducing electronic waste generated by lost computing equipment.

---

## 10. Conclusion

The **Smart Campus Lost & Found and Asset Recovery System** demonstrates the practical integration of core and advanced Java software engineering principles. By fulfilling **CO4** (AWT GUI design, Delegation Event Model, Applet lifecycle management) and **CO5** (Character/Byte I/O Streams, Object Serialization, SHA-256 Cryptographic Hashing, and SQLite JDBC CRUD persistence), the application provides a production-grade, secure, and intuitive solution for campus asset recovery.

The multi-tier architectural separation guarantees high maintainability, while the automated 23-test verification suite proves 100% adherence to all functional, security, and algorithmic requirements.

---

## 11. Individual Contribution of Group Members

| Team Member | Roll / Reg Number | Assigned Modules & Features | Effort Contribution |
| :--- | :--- | :--- | :---: |
| **Member 1 (Lead)** | *2026-CS-0916-A* | Architecture Design, SHA-256 Cryptographic Hashing (`HashUtil`), Delegation Event Model & Master Desktop GUI (`SmartCampusMainFrame`, `LostReportPanel`) | 25% |
| **Member 2** | *2026-CS-0916-B* | SQLite JDBC Database Layer (`DatabaseManager`, `ItemDAO`, `ClaimDAO`, `AuditDAO`), SQL Matching Queries & CRUD operations | 25% |
| **Member 3** | *2026-CS-0916-C* | File I/O Character Streams (`FileLogManager`, `CSVReportExporter`), Object Serialization (`SerializationManager`), Applet Programming (`SmartCampusApplet`, `applet.html`) | 25% |
| **Member 4** | *2026-CS-0916-D* | Heuristic Matching Engine (`MatchingEngine`), Custom Exception Hierarchy, Automated Test Suite (`SmartCampusSystemTest`), Documentation & SDG Analysis | 25% |

---

## 12. References

1. Oracle Corporation. *Java SE 11 Documentation: Package java.awt & java.awt.event*. https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/
2. Oracle Corporation. *Java Object Serialization Specification*. https://docs.oracle.com/en/java/javase/11/docs/specs/serialization/
3. National Institute of Standards and Technology (NIST). *FIPS PUB 180-4: Secure Hash Standard (SHS) - SHA-256 Specification*.
4. Xerial. *SQLite JDBC Driver Documentation & Architecture*. https://github.com/xerial/sqlite-jdbc
5. United Nations. *Sustainable Development Goals Knowledge Platform (SDG 4, 11, 12)*. https://sdgs.un.org/goals
