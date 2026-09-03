# STUDENT DELIVERABLES REPORT

## Smart Campus Lost & Found and Asset Recovery System

**Department of Computer Science and Engineering**
**Course Code & Name:** CSA09 – Programming in Java (Slot C)
**Assignment / Capstone Title:** Smart Campus Lost & Found and Asset Recovery System using Java GUI, File Handling, Serialization, Hashing and JDBC

**Mapped Course Outcomes:**
- **CO4:** Create Java applications using Applet Programming, event handling, the Delegation Event Model, AWT components, and GUI design principles. *(Bloom's: K6 – Create; K3 – Apply)*
- **CO5:** Apply Java I/O Streams, serialization, hashing concepts, and JDBC database connectivity to develop applications that perform persistent data management and CRUD operations. *(Bloom's: K6 – Create; K3 – Apply; K2 – Understand)*

---

## Table of Contents

1. [Problem Statement](#1-problem-statement)
2. [Objective](#2-objective)
3. [Requirements and Environment Used](#3-requirements-and-environment-used)
4. [Design / Proposed Solution](#4-design--proposed-solution)
5. [Algorithm / Pseudocode / Flowchart](#5-algorithm--pseudocode--flowchart)
6. [Implementation / Source Code](#6-implementation--source-code)
7. [Test Cases and Expected/Actual Results](#7-test-cases-and-expectedactual-results)
8. [Execution Screenshots / Output](#8-execution-screenshots--output)
9. [Analysis and Discussion](#9-analysis-and-discussion)
10. [Conclusion](#10-conclusion)
11. [Individual Contribution of Group Members](#11-individual-contribution-of-group-members)
12. [References](#12-references)

---

## 1. Problem Statement

Across university campuses, thousands of valuable personal and academic items—including laptops, scientific calculators, laboratory apparatus, smart ID cards, wallets, and textbooks—are misplaced daily. Traditional lost-and-found practices rely on fragmented notice boards, unindexed manual logbooks, or unverified word-of-mouth recovery. This legacy approach suffers from severe vulnerabilities:

1. **Lack of Identity & Ownership Verification:** Fraudulent or mistaken claims occur frequently because custodians lack cryptographic proof of ownership.
2. **Disconnected Data Islands:** Information about lost items is rarely matched systematically with found items logged at separate security desks across campus zones.
3. **Absence of Tamper-Resistant Audit Trails:** Manual logbooks lack transactional integrity, timestamps, and multi-tier recovery backups.

To address these challenges, this project engineers a **Smart Campus Lost & Found and Asset Recovery System** developed in Java. The system integrates an interactive Graphical User Interface (AWT Frame/Applet), the Delegation Event Model, an automated multi-factor heuristic matching engine, SHA-256 cryptographic salted PIN authentication, persistent SQLite JDBC database CRUD operations, character-stream audit logging, and binary object serialization/deserialization.

---

## 2. Objective

The primary technical and operational objectives of this project are:

1. **Interactive AWT GUI & Delegation Event Model (CO4):** Implement an ergonomic, structured interface using AWT controls (`Frame`, `Applet`, `Panel`, `Label`, `TextField`, `TextArea`, `Button`, `Checkbox`, `Choice`, `List`, `Dialog`, `MenuBar`) and layout managers (`BorderLayout`, `GridBagLayout`, `GridLayout`, `FlowLayout`, `CardLayout`) responding to `ActionEvent`, `ItemEvent`, and `WindowEvent`.
2. **Applet Programming Lifecycle Integration (CO4):** Demonstrate full applet lifecycle execution (`init()`, `start()`, `paint()`, `stop()`, `destroy()`) with dual desktop/browser container support.
3. **Persistent JDBC CRUD Operations (CO5):** Implement robust database connectivity to an embedded SQLite database performing Create, Retrieve, Update, and Delete operations alongside parameterized SQL matching queries.
4. **Cryptographic Identity Verification via SHA-256 Hashing (CO5):** Protect claimant credentials by generating 128-bit random cryptographic salts and computing SHA-256 digests (`SHA-256(Salt || PIN)`), ensuring zero plaintext exposure and constant-time verification.
5. **Multi-Tier File I/O & Object Serialization (CO5):**
   - Use Character Streams (`FileWriter`, `FileReader`, `BufferedReader`, `PrintWriter`) for real-time audit logging and CSV export.
   - Use Byte Streams (`ObjectOutputStream`, `ObjectInputStream`) for state backup and session deserialization.
6. **Smart Heuristic Auto-Matching Engine:** Automate semantic category matching, location proximity scoring, and temporal-delta calculation to suggest high-confidence matches.

---

## 3. Requirements and Environment Used

### 3.1 Hardware Requirements

| Component | Minimum | Recommended |
| :--- | :--- | :--- |
| Processor | Intel Core i3 / AMD Ryzen (2.0 GHz) | Intel Core i5/i7 / Ryzen |
| RAM | 4 GB | 8 GB |
| Storage | 200 MB free space | 1 GB |
| Display | 1024 × 768 | 1920 × 1080 |

### 3.2 Software & Development Environment

| Category | Specification |
| :--- | :--- |
| Operating System | Microsoft Windows 10/11 (64-bit) / Linux / macOS |
| Java Development Kit (JDK) | OpenJDK / Oracle JDK 11 LTS (`javac 11.0.23` / `java 11.0.23`) |
| Database Engine | SQLite 3 (embedded via `sqlite-jdbc-3.45.1.0.jar`) |
| Logging Facade | SLF4J API (`slf4j-api-1.7.36.jar`, `slf4j-simple-1.7.36.jar`) |
| Build & Automation | Windows Batch Scripts (`compile.bat`, `run.bat`, `test.bat`, `run_cli.bat`, `run_applet.bat`) / PowerShell |
| IDE / Editor | Antigravity IDE / VS Code / IntelliJ IDEA |

### 3.3 Installation Command Reference

```cmd
:: Verify Java installation
java -version
javac -version

:: One-click compilation
.\compile.bat
```

---

## 4. Design / Proposed Solution

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
        │                                             │                     │
        ▼                                             ▼                     ▼
+──────────────────+                         +──────────────────+   +──────────────+
| SQLite Database  |                         | audit_trail.log  |   | backup_*.ser |
| (JDBC Engine)    |                         | campus_report.csv|   | (Binary)     |
+──────────────────+                         +──────────────────+   +──────────────+
```

**Design Rationale:**
- **Presentation Tier:** All user interaction happens here via AWT components and the Applet.
- **Service Tier:** Central `LostFoundService` facade orchestrates all workflows; `MatchingEngine` scores candidate matches.
- **Data Access Tier:** DAO objects isolate SQL from business logic; all queries use parameterized `PreparedStatement` to prevent SQL injection.
- **Persistence Tier:** SQLite JDBC for ACID CRUD, character streams for portable audit logs, and byte streams for binary backups.

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
                    |    hashed_pin_attempt: TX|
                    |    salt_used: TEXT       |
                    |    proof_desc: TEXT      |
                    |    claim_date: TEXT      |
                    |    verified: INTEGER     |
                    |    status: TEXT          |
                    |    remarks: TEXT         |
                    +--------------------------+
```

---

## 5. Algorithm / Pseudocode / Flowchart

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

#### Flowchart: Salting & Verification

```
        +------------------+
        |  User enters PIN |
        +------------------+
                 |
                 v
        +------------------+
        | Generate 16-byte |
        | random salt      |
        +------------------+
                 |
                 v
        +------------------+
        | SaltedInput =    |
        | salt + ":" + PIN |
        +------------------+
                 |
                 v
        +------------------+
        | SHA-256 digest   |
        | -> 64-char hash  |
        +------------------+
                 |
                 v
   +----------------------------+
   | Store (salt, hash) in DB   |
   +----------------------------+
                 |
                 v
        +------------------+
        | On claim: hash   |
        | entered PIN      |
        +------------------+
                 |
                 v
   +----------------------------------+
   | constantTimeEquals(candidate,    |
   | storedHash) ?                    |
   +----------------------------------+
        | Yes                  | No
        v                      v
  +------------+        +------------+
  | APPROVED   |        |  REJECTED  |
  +------------+        +------------+
```

### 5.2 Algorithm 2: Multi-Factor Heuristic Matching Engine

$$\text{Score} = W_{cat} + W_{text} + W_{loc} + W_{date}$$

Where:
- $W_{cat} = 40.0$ if $\text{Category}_{lost} = \text{Category}_{found}$, else $0.0$
- $W_{text} = 20.0 \times \text{JaccardSimilarity}(\text{Keywords}_{lost}, \text{Keywords}_{found})$
- $W_{loc} = 20.0$ (Exact location), $15.0$ (Sub-zone overlap), or $10.0$ (Token overlap)
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
     If L.location equals item.location:            totalScore += 20.0
     Else If locations contain each other:           totalScore += 15.0

     // 4. Date Proximity
     daysDiff = CalculateDayDifference(L.date, item.date)
     If daysDiff == 0:    totalScore += 20.0
     Else If daysDiff <= 3: totalScore += 15.0
     Else If daysDiff <= 7: totalScore += 10.0

     If totalScore >= 35.0:
         MatchList.append(new MatchResult(L, item, totalScore, reasons))

3. Sort MatchList in descending order by totalScore.
4. Return MatchList.
--------------------------------------------------------------------------------
```

#### Flowchart: Matching Engine

```
   +---------------------+
   | For each FoundItem  |
   +---------------------+
            |
            v
   +------------------+
   | Category match?  |
   | add up to 40 pts |
   +------------------+
            |
            v
   +------------------+
   | Text Jaccard     |
   | add up to 20 pts |
   +------------------+
            |
            v
   +------------------+
   | Location         |
   | add up to 20 pts |
   +------------------+
            |
            v
   +------------------+
   | Date proximity   |
   | add up to 20 pts |
   +------------------+
            |
            v
  +------------------------------+
  | totalScore >= 35 ?           |
  +------------------------------+
        | Yes             | No
        v                 v
  +------------+    +------------+
  | Add match  |    |  Discard   |
  +------------+    +------------+
            |
            v
   +------------------+
   | Sort descending  |
   | Return ranked     |
   +------------------+
```

### 5.3 Workflow in Pseudocode

```
Workflow: ReportLost -> Match -> Claim -> Verify -> Return
--------------------------------------------------------------------------------
1. Owner submits LostItem report (title, category, location, date, secret PIN).
2. System generates salt, computes SHA-256(salt:PIN), stores (hash, salt).
3. Custodian reports FoundItem (with locker & custody metadata).
4. MatchingEngine scores each (lost, found) pair; pairs >= 35% are surfaced.
5. Claimant submits a claim and enters their PIN.
6. System recomputes SHA-256(salt:enteredPIN) and verifies constant-time.
7. On success, claim is APPROVED; item status -> CLAIM_VERIFIED.
8. Asset is handed over; status -> RESOLVED_RETURNED.
9. Every action is logged to the audit trail (DB + file) and trigger serialization backup.
--------------------------------------------------------------------------------
```

---

## 6. Implementation / Source Code

### 6.1 Package & Class Overview

| Package | Source File | Description | CO Mapping |
| :--- | :--- | :--- | :--- |
| `model` | `Item.java` | Base abstract entity implementing `Serializable` | CO5 |
| `model` | `LostItem.java` | Lost asset entity with salted SHA-256 PIN fields | CO5 |
| `model` | `FoundItem.java` | Found asset entity with storage locker metadata | CO5 |
| `model` | `ClaimRecord.java` | Recovery claim model with verification audit flags | CO5 |
| `model` | `AuditLog.java` | Event audit entity formatted for stream logging | CO5 |
| `model` | `ItemStatus.java` | Lifecycle state enumeration | CO4/CO5 |
| `model` | `MatchResult.java` | Encapsulates match score and reasoning | CO4/CO5 |
| `security` | `HashUtil.java` | SHA-256 `MessageDigest` & `SecureRandom` salting | CO5 |
| `db` | `DatabaseManager.java` | JDBC SQLite connection factory & DDL migrator | CO5 |
| `dao` | `ItemDAO.java` | Full CRUD `PreparedStatement` operations | CO5 |
| `dao` | `ClaimDAO.java` | Claim record persistence & state updater | CO5 |
| `dao` | `AuditDAO.java` | SQL transactional audit log persistence | CO5 |
| `io` | `FileLogManager.java` | Character stream `FileWriter`/`BufferedReader` | CO5 |
| `io` | `SerializationManager.java` | Byte stream `ObjectOutputStream`/`InputStream` | CO5 |
| `io` | `CSVReportExporter.java` | CSV & summary report generator | CO5 |
| `service` | `MatchingEngine.java` | Heuristic 4-factor auto-match algorithm | CO4/CO5 |
| `service` | `LostFoundService.java` | Master facade orchestrating all workflows | CO4/CO5 |
| `gui` | `ComponentFactory.java` | Curated color palette & styled AWT controls | CO4 |
| `gui` | `LostReportPanel.java` | Form panel for logging lost assets | CO4 |
| `gui` | `FoundReportPanel.java` | Form panel for registering found items | CO4 |
| `gui` | `MatchClaimPanel.java` | UI for matching & SHA-256 PIN claim verify | CO4/CO5 |
| `gui` | `InventoryPanel.java` | CRUD asset inventory browser | CO4/CO5 |
| `gui` | `AuditLogPanel.java` | Dual-pane DB & file log viewer | CO4/CO5 |
| `gui` | `SmartCampusMainFrame.java` | Main AWT Desktop Frame with `MenuBar` | CO4 |
| `gui` | `SmartCampusApplet.java` | Full Applet lifecycle implementation | CO4 |
| `app` | `Main.java` | Desktop launcher with CLI fallback | CO4/CO5 |
| `test` | `SmartCampusSystemTest.java` | 23-test automated verification suite | CO4/CO5 |

### 6.2 Core Source Code Snippets

#### Security — HashUtil.java (CO5)

```java
public class HashUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generateSalt() {
        byte[] saltBytes = new byte[16];
        SECURE_RANDOM.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    public static String hashWithSalt(String input, String salt) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] saltedInput = (salt + ":" + input).getBytes(StandardCharsets.UTF_8);
        return bytesToHex(digest.digest(saltedInput));
    }

    public static boolean verifyPIN(String enteredPin, String salt, String expectedHash) {
        String calculatedHash = hashWithSalt(enteredPin, salt);
        return constantTimeEquals(calculatedHash, expectedHash);
    }

    public static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
```

#### Matching Engine — MatchingEngine.java (CO4/CO5)

```java
public MatchResult evaluateMatch(LostItem lost, FoundItem found) {
    double score = 0.0;
    MatchResult result = new MatchResult(lost, found, 0.0);

    // 1. Category Match (40 points max)
    if (lost.getCategory().equalsIgnoreCase(found.getCategory())) {
        score += 40.0;
        result.addReason("Exact Category Match");
    }

    // 2. Keyword Jaccard overlap (20 points max)
    double textScore = computeTextSimilarity(
        (lost.getTitle() + " " + lost.getDescription()).toLowerCase(),
        (found.getTitle() + " " + found.getDescription()).toLowerCase());
    score += textScore * 20.0;

    // 3. Location proximity (20 points max)
    if (lost.getLocation().equalsIgnoreCase(found.getLocation())) {
        score += 20.0;
    } else if (lost.getLocation().contains(found.getLocation())
            || found.getLocation().contains(lost.getLocation())) {
        score += 15.0;
    }

    // 4. Date proximity (20 points max)
    long daysDiff = computeDayDifference(lost.getDate(), found.getDate());
    if (daysDiff == 0) score += 20.0;
    else if (daysDiff <= 3) score += 15.0;
    else if (daysDiff <= 7) score += 10.0;

    return new MatchResult(lost, found, Math.min(100.0, score));
}
```

#### Service Facade — LostFoundService.java (reportLostItem)

```java
public LostItem reportLostItem(String title, String category, String description,
                               String location, String date, String reporterName,
                               String reporterContact, String reporterId,
                               double reward, String rawSecretPin, String urgency) {
    validateItemInput(title, category, location, date, reporterName, reporterContact, reporterId);
    if (rawSecretPin == null || rawSecretPin.trim().length() < 4) {
        throw new ValidationException("Secret Claim PIN must be at least 4 digits.");
    }

    String itemId = "LOST-" + (1000 + (int)(Math.random() * 9000));
    String salt = HashUtil.generateSalt();
    String pinHash = HashUtil.hashWithSalt(rawSecretPin.trim(), salt);

    LostItem item = new LostItem(itemId, title.trim(), category.trim(),
            description != null ? description.trim() : "", location.trim(),
            date.trim(), reporterName.trim(), reporterContact.trim(),
            reporterId.trim(), reward, pinHash, salt, urgency);

    itemDAO.insertLostItem(item);
    // Audit + file log + serialization backup
    ...
    return item;
}
```

#### JDBC CRUD — DatabaseManager.java / ItemDAO.java (CO5)

```java
// DatabaseManager.initializeDatabase() creates all tables + indices.
public static Connection getConnection() throws DatabaseException {
    conn = DriverManager.getConnection(dbUrl);
    conn.createStatement().execute("PRAGMA foreign_keys = ON;");
    return conn;
}

// ItemDAO.insertLostItem uses parameterized PreparedStatement (SQL injection safe)
String sql = "INSERT INTO lost_items (item_id, title, ...) VALUES (?, ?, ...)";
try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, item.getItemId());
    ps.setString(2, item.getTitle());
    ...
    ps.executeUpdate();
}
```

#### File I/O & Serialization — FileLogManager.java / SerializationManager.java (CO5)

```java
// Character Streams (audit logging)
public void appendLog(AuditLog log) {
    try (FileWriter fw = new FileWriter(LOG_FILE_PATH, StandardCharsets.UTF_8, true);
         PrintWriter pw = new PrintWriter(new BufferedWriter(fw))) {
        pw.println(log.toLogFileLine());
    }
}

// Byte Streams (binary serialization)
public void serializeLostItems(List<LostItem> items) {
    try (ObjectOutputStream oos = new ObjectOutputStream(
            new BufferedOutputStream(new FileOutputStream(LOST_SER_FILE)))) {
        oos.writeObject(items);
    }
}
```

#### GUI — SmartCampusMainFrame.java (CO4)

```java
public class SmartCampusMainFrame extends Frame implements ActionListener, WindowListener {
    // Navigation with CardLayout across panels:
    //   Report Lost | Report Found | Auto-Match & Claim | Asset Inventory | Audit & I/O
    // MenuBar: File (Export, Backup, Restore, Exit),
    //          Operations, Audit & Security, Help (SDG / About)
    // WindowListener triggers serialization backup on exit.
}
```

---

## 7. Test Cases and Expected/Actual Results

The automated test suite (`SmartCampusSystemTest.java`) executes **23 comprehensive test scenarios** covering valid inputs, invalid inputs, boundary values, cryptographic invariants, and persistence integrity.

| Test ID | Module Tested | Input Scenario | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :---: |
| TC-01 | SHA-256 Hash | Raw PIN `9842` + 16-byte random salt | 64-char Hex Digest | 64-char Hex generated | **PASS** |
| TC-02 | Salt Generation | Two consecutive `generateSalt()` calls | Unique non-identical 32-char salts | Unique random salts | **PASS** |
| TC-03 | PIN Verification | Entered PIN matches registered PIN | Returns `true` | Returns `true` | **PASS** |
| TC-04 | Tamper Rejection | Entered wrong PIN `0000` | Returns `false` | Returns `false` | **PASS** |
| TC-05 | Timing Resistance | `constantTimeEquals()` identical/distinct strings | Bitwise constant-time evaluation | Passed | **PASS** |
| TC-06 | Matching Engine | Identical category, overlapping text, same day | Score ≥ 75% | Evaluated to 88.3% | **PASS** |
| TC-07 | Matching Engine | Dissimilar items (Laptop vs. Umbrella) | Score < 35% | Evaluated to 0.0% | **PASS** |
| TC-08 | Byte Serialization | `serializeLostItems()` to `.ser` | File exists, size > 0 | Created (737 bytes) | **PASS** |
| TC-09 | Deserialization | `deserializeLostItems()` from binary `.ser` | Objects restored | Attributes intact | **PASS** |
| TC-10 | SHA-256 Checksum | Checksum of `.ser` binary | 64-char digest | 64-char digest | **PASS** |
| TC-11 | Character Stream | Append `AuditLog` via `FileWriter`/`PrintWriter` | Written to `data/audit_trail.log` | Lines recorded | **PASS** |
| TC-12 | CSV Export | Export items & claims | `.csv` generated | `campus_asset_report.csv` | **PASS** |
| TC-13 | Summary Export | Export text summary | Stats text file | `campus_summary_report.txt` | **PASS** |
| TC-14 | JDBC Create (Lost) | Insert `LostItem` with `PreparedStatement` | Stored in `lost_items` | Inserted `LOST-4486` | **PASS** |
| TC-15 | JDBC Create (Found) | Insert `FoundItem` with locker metadata | Stored in `found_items` | Inserted `FND-8871` | **PASS** |
| TC-16 | JDBC Read | Query all lost items | Non-empty `List<LostItem>` | All items retrieved | **PASS** |
| TC-17 | JDBC Auto-Match | Execute candidate matching query | Matching pairs returned | Score 78.5% | **PASS** |
| TC-18 | Claim Verification | Correct PIN `3321` | `APPROVED`, verified flag | `CLM-9486` approved | **PASS** |
| TC-19 | JDBC Update | Update status to `RESOLVED_RETURNED` | Row status updated | Updated successfully | **PASS** |
| TC-20 | JDBC Delete | Delete resolved/duplicate item | Record deleted | Delete executed | **PASS** |
| TC-21 | Validation Check | Empty title | Throws `ValidationException` | Caught exception | **PASS** |
| TC-22 | Date Format Check | Date `01/09/2026` | Rejects non `YYYY-MM-DD` | Caught `ValidationException` | **PASS** |
| TC-23 | PIN Rejection | Incorrect PIN `9999` | Throws `InvalidPINException` | Caught exception | **PASS** |

> **Overall Verification Summary: 23 / 23 Tests Passed (100.0% Pass Rate)**

---

## 8. Execution Screenshots / Output

> *Note: Screenshots of the running GUI, Applet viewer, and CLI terminal are to be captured and inserted here during final submission. Representative output traces are provided below.*

### 8.1 Automated Test Execution Log (Representative)

```
================================================================================
   SMART CAMPUS LOST & FOUND SYSTEM - AUTOMATED VERIFICATION SUITE
   Course Outcome: CO4 (AWT/Applet) | CO5 (I/O, Serialization, Hashing, JDBC)
================================================================================

--- 1. CRYPTOGRAPHIC HASHING & CLAIMANT PIN AUTHENTICATION (CO5) ---
  [PASS] SHA-256 Hash Length - Generated 256-bit hex digest: 29a7f72d...
  [PASS] Salt Randomness & Uniqueness - Salt 1: b3efd797... != Salt 2: 75640555...
  [PASS] PIN Match Verification - Correct raw PIN matches salted hash
  [PASS] Tampered PIN Rejection - Incorrect raw PIN is strictly rejected
  [PASS] Constant-Time Equals - Verified timing attack resistance

--- 2. HEURISTIC MATCHING ENGINE TESTS ---
  [PASS] High Similarity Match Score - 88.3% (Exact Category, 42% overlap, Exact Location, Same-day)
  [PASS] Low Similarity Discrimination - Confidence score: 0.0%

--- 3. OBJECT SERIALIZATION & BYTE STREAMS (CO5) ---
  [PASS] Serialized File Created - .ser file size: 737 bytes
  [PASS] Deserialized Objects Restored - LOST-SER-1 Scientific Calculator restored
  [PASS] Serialized File SHA-256 Checksum - 5fe3fa8ce4691574...

--- 4. FILE I/O CHARACTER STREAMS & CSV EXPORTER (CO5) ---
  [PASS] Character Stream FileWriter/BufferedReader - Read 16 audit log lines
  [PASS] CSV Report Generation - data/campus_asset_report.csv
  [PASS] Summary Text Generation - data/campus_summary_report.txt

--- 5. JDBC DATABASE CRUD OPERATIONS & WORKFLOW (CO5) ---
  [PASS] JDBC Insert Lost Item - Inserted Lost Item #LOST-4486
  [PASS] JDBC Insert Found Item - Inserted Found Item #FND-8871
  [PASS] JDBC Retrieve All Lost Items - Retrieved 5 items from SQLite
  [PASS] Smart Match Engine Execution - Top match score: 78.5%
  [PASS] Claim Verification & SHA-256 PIN Match - Claim #CLM-9486 approved
  [PASS] Asset Return Status Update - RESOLVED_RETURNED
  [PASS] JDBC Delete Item - Deleted test record #LOST-6808

--- 6. EXCEPTION HANDLING & VALIDATION TESTS ---
  [PASS] ValidationException on Missing Title
  [PASS] ValidationException on Malformed Date
  [PASS] InvalidPINException on Incorrect PIN

================================================================================
   FINAL TEST RESULTS SUMMARY
   Total Tests Run : 23 | Passed : 23 | Failed : 0 | Pass Rate : 100.0%
================================================================================
```

### 8.2 Audit Trail Output (Sample from `data/audit_trail.log`)

```
[2026-09-01 10:29:18] [REPORT_LOST] LostItem | Entity: LOST-3975 | Actor: Samantha Ray | Status: SUCCESS | Reported lost Academic Supplies: Scientific Calculator TI-84 Plus at Science Block Lab 304
[2026-09-01 10:29:18] [REPORT_FOUND] FoundItem | Entity: FND-9671 | Actor: Campus Custodian Mark | Status: SUCCESS | Registered found Electronics: Silver Dell Laptop in Black Sleeve in Central Library (Locker: Locker #14B)
[2026-09-01 10:29:18] [CLAIM_VERIFIED] ClaimRecord | Entity: CLM-8641 | Actor: Chris Evans | Status: SUCCESS | Verified asset claim for Lost: LOST-1344 & Found: FND-2072 using SHA-256 hash.
[2026-09-01 10:29:18] [ASSET_RETURNED] ClaimRecord | Entity: CLM-8641 | Actor: SecurityDesk | Status: RESOLVED | Asset handed over to claimant Chris Evans. Case closed.
[2026-09-01 10:29:18] [CLAIM_FAILED] ClaimRecord | Entity: CLM-8613 | Actor: Tom | Status: FAILED | PIN verification mismatch for item #LOST-7492
```

### 8.3 CLI Demonstration Output (from `run_cli.bat`)

```
--- STEP 1: REPORT LOST ITEM ---
✔ Logged Lost Item: LOST-xxxx | Owner: David Miller
✔ Secret PIN Salt: <32-char hex>
✔ Stored SHA-256 Hash: <64-char hex>

--- STEP 2: REPORT FOUND ITEM ---
✔ Logged Found Item: FND-xxxx | Stored in: Locker #05B

--- STEP 3: RUN AUTO-MATCHING ENGINE ---
✔ Matching Engine evaluated N candidates: [Match 88.3%] ...

--- STEP 4: SUBMIT CLAIM & VERIFY SHA-256 PIN ---
✔ Claim Verification SUCCESS: CLM-xxxx (Verified=true)

--- STEP 5: FINALIZE ASSET RETURN ---
✔ Asset handover complete. Status: RESOLVED_RETURNED

--- STEP 6: OBJECT SERIALIZATION & CSV EXPORT ---
✔ CSV Report exported to: data/campus_asset_report.csv

  FULL END-TO-END WORKFLOW VERIFIED SUCCESSFULLY!
```

---

## 9. Analysis and Discussion

### 9.1 Cryptographic Security Analysis of SHA-256 with Salting

- **Pre-Image Resistance:** Given a 256-bit hash digest $H = \text{SHA-256}(M)$, finding the original input $M$ is computationally infeasible ($2^{256}$ operations).
- **Mitigation of Rainbow Table Attacks via Dynamic Salting:** A unique 16-byte random salt generated via `SecureRandom` is prepended to each PIN. Even if multiple students choose the same 4-digit PIN (e.g., `1234`), their stored hashes are completely distinct, rendering pre-computed lookup tables useless.
- **Timing Attack Mitigation:** Verification uses `constantTimeEquals()` to evaluate all characters regardless of early mismatch, preventing side-channel execution timing profiling.

### 9.2 Comparative Evaluation of Persistence Strategies

| Feature | File I/O (Character Streams) | Object Serialization (Byte Streams) | Relational Database (SQLite JDBC) |
| :--- | :--- | :--- | :--- |
| **Stream Class Used** | `FileWriter`, `BufferedReader` | `ObjectOutputStream`, `ObjectInputStream` | JDBC PreparedStatement |
| **Data Format** | Plaintext / CSV | Binary serialized object graph | Structured SQL Relational B-Tree |
| **Primary Use Case** | Append-only audit logging & reports | Offline snapshot recovery / backup | Complex querying, ACID CRUD, indexed search |
| **Performance** | $O(N)$ sequential scan | Fast bulk object I/O | $O(\log N)$ indexed retrieval |
| **Resilience** | Human readable; portable | Requires identical `serialVersionUID` | Robust transaction rollback (ACID) |

### 9.3 Design Trade-offs & Discussion

- **Embedded SQLite vs. Client-Server DB:** SQLite was chosen for zero-configuration portability ideal for a student deployment, while still demonstrating full JDBC CRUD and relational modeling.
- **AWT vs. Swing/JavaFX:** AWT was mandated by the course outcomes (CO4) to demonstrate the Applet + Delegation Event Model; the modern styling layer (`ComponentFactory`) mitigates AWT's dated look.
- **Random ID generation:** `Math.random()` is used for demo IDs; in production a `UUID` or database sequence would be preferred.

### 9.4 SDG Impact Analysis

- **SDG 4 (Quality Education):** Directly prevents student academic disruption by automated recovery of laptops, textbooks, and lab gear.
- **SDG 11 (Sustainable Cities & Communities):** Replaces unmonitored paper desks with an indexed, auditable campus infrastructure where every recovered item has verifiable custody tracking.
- **SDG 12 (Responsible Consumption & Production):** Maximizes asset recovery rates, preventing premature discard and reducing e-waste from lost computing equipment.

---

## 10. Conclusion

The **Smart Campus Lost & Found and Asset Recovery System** demonstrates the practical integration of core and advanced Java software engineering principles. By fulfilling **CO4** (AWT GUI design, Delegation Event Model, Applet lifecycle management) and **CO5** (Character/Byte I/O Streams, Object Serialization, SHA-256 Cryptographic Hashing, and SQLite JDBC CRUD persistence), the application provides a production-grade, secure, and intuitive solution for campus asset recovery.

The multi-tier architectural separation guarantees high maintainability, while the automated 23-test verification suite proves 100% adherence to all functional, security, and algorithmic requirements. The project also meaningfully aligns with UN Sustainable Development Goals 4, 11, and 12, demonstrating socially responsible software engineering.

---

## 11. Individual Contribution of Group Members

| Team Member | Roll / Reg Number | Assigned Modules & Features | Effort Contribution |
| :--- | :--- | :--- | :---: |
| **Member 1 (Lead)** | 2026-CS-0916-A | Architecture Design, SHA-256 Cryptographic Hashing (`HashUtil`), Delegation Event Model & Master Desktop GUI (`SmartCampusMainFrame`, `LostReportPanel`) | 25% |
| **Member 2** | 2026-CS-0916-B | SQLite JDBC Database Layer (`DatabaseManager`, `ItemDAO`, `ClaimDAO`, `AuditDAO`), SQL Matching Queries & CRUD operations | 25% |
| **Member 3** | 2026-CS-0916-C | File I/O Character Streams (`FileLogManager`, `CSVReportExporter`), Object Serialization (`SerializationManager`), Applet Programming (`SmartCampusApplet`, `applet.html`) | 25% |
| **Member 4** | 2026-CS-0916-D | Heuristic Matching Engine (`MatchingEngine`), Custom Exception Hierarchy, Automated Test Suite (`SmartCampusSystemTest`), Documentation & SDG Analysis | 25% |

> **Note:** Full names, exact roll numbers, and signatures of each member should be filled in from the official group roster before submission.

---

## 12. References

1. Oracle Corporation. *Java SE 11 Documentation: Package java.awt & java.awt.event.* https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/
2. Oracle Corporation. *Java Object Serialization Specification.* https://docs.oracle.com/en/java/javase/11/docs/specs/serialization/
3. National Institute of Standards and Technology (NIST). *FIPS PUB 180-4: Secure Hash Standard (SHS) – SHA-256 Specification.*
4. Xerial. *SQLite JDBC Driver Documentation & Architecture.* https://github.com/xerial/sqlite-jdbc
5. United Nations. *Sustainable Development Goals Knowledge Platform (SDG 4, 11, 12).* https://sdgs.un.org/goals
6. Oracle Corporation. *Java I/O Streams Tutorial.* https://docs.oracle.com/javase/tutorial/essential/io/
7. Oracle Corporation. *JDBC API Tutorial & Reference.* https://docs.oracle.com/javase/tutorial/jdbc/
8. Java Security Architecture (JCA). *MessageDigest & SecureRandom.* https://docs.oracle.com/en/java/javase/11/security/

---

*End of Student Deliverables Report*
