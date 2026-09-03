🎓 Smart Campus Lost & Found and Asset Recovery System
An enterprise-grade Java application for secure, automated campus asset recovery, built for the CSA09 – Programming in Java capstone assignment.

Java Version GUI Security Database Tests SDG

Table of Contents
About the Project
Course Outcomes Mapped
Key Features
System Architecture
Database Schema
Security Implementation
Project Structure
Prerequisites
Getting Started
Automated Test Suite
Deliverables
FAQ & Troubleshooting
Project Team
About the Project
Misplaced academic and personal belongings—laptops, calculators, laboratory apparatus, ID cards, wallets, and textbooks—cause substantial disruption in universities. Traditional lost-and-found desks rely on unindexed paper logbooks and unverified physical claims, creating security risks and high item-loss rates.

This system provides a unified, secure, automated ecosystem for campus asset recovery:

Intuitive GUI built with Java AWT and the Delegation Event Model (input forms, search filters, status monitors).
Applet lifecycle integration implementing init(), start(), paint(), stop(), and destroy().
Cryptographic security via SHA-256 salted PIN verification with constant-time equality checks.
Smart auto-matching engine that scores item similarity, campus-zone proximity, and timeline differences.
Persistent multi-tier storage combining SQLite JDBC CRUD, character-stream audit logging, and binary object serialization.
Course Outcomes Mapped
Outcome	Description	Bloom's Level
CO4	Create Java applications using Applet Programming, event handling, the Delegation Event Model, AWT components, and GUI design principles.	K6 – Create; K3 – Apply
CO5	Apply Java I/O Streams, serialization, hashing concepts, and JDBC database connectivity to develop applications that perform persistent data management and CRUD operations.	K6 – Create; K3 – Apply; K2 – Understand
Sustainable Development Goals (SDG) Alignment:

SDG	Goal	Relevance
SDG 4	Quality Education	Prevents academic disruption by enabling fast recovery of learning assets.
SDG 11	Sustainable Cities & Communities	Promotes transparent, digitally audited campus infrastructure.
SDG 12	Responsible Consumption & Production	Prolongs asset lifecycles and reduces electronic waste.
Key Features
1. Graphical User Interface & Delegation Event Model (CO4)
AWT controls: Frame, Applet, Panel, Label, TextField, TextArea, Button, Checkbox, Choice, List, Dialog, MenuBar.
Layout managers: BorderLayout, GridBagLayout, GridLayout, FlowLayout, CardLayout.
Event listeners: ActionListener, ItemListener, WindowListener.
Client-side validation of required fields, email format, and YYYY-MM-DD dates.
2. Applet Programming Lifecycle (CO4)
SmartCampusApplet demonstrates init(), start(), paint(), stop(), destroy().
HTML parameterization via applet.html (<param> tags for campus localization).
3. Cryptographic Hashing & Claimant Identity Security (CO5)
SHA-256 with a 128-bit cryptographic salt generated via java.security.SecureRandom.
Constant-time byte comparison (constantTimeEquals) to resist timing attacks.
Zero plaintext storage of raw PINs.
4. Relational Database Persistence & JDBC CRUD (CO5)
Embedded SQLite JDBC — no external server setup required.
Full CRUD support for lost items, found assets, and recovery claims.
Lifecycle transitions: REPORTED_LOST → MATCHED → CLAIM_VERIFIED → RESOLVED_RETURNED.
Parameterized PreparedStatement queries to prevent SQL injection.
5. Multi-Tier File I/O & Object Serialization (CO5)
Character streams (FileWriter, FileReader, BufferedReader, PrintWriter) for audit logs and CSV exports.
Byte streams (ObjectOutputStream, ObjectInputStream) with SHA-256 file checksum verification for binary backups.
6. Heuristic Multi-Factor Auto-Matching Engine
A weighted 4-factor scoring algorithm:

Category Exact Match – 40%
Keyword Jaccard Text Overlap – 20%
Campus Location Proximity – 20%
Date Temporal Proximity – 20%
System Architecture
The application follows a clean 4-tier architecture:

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
Database Schema
The relational schema is defined in src/com/campus/lostfound/db/DatabaseManager.java.

lost_items
Stores assets reported as lost, including the owner's salted SHA-256 claim PIN.

found_items
Stores assets registered as found, with storage-locker and custody metadata.

claims
Stores recovery claims with verification and status data.

audit_logs
Stores transactional audit events.

CREATE TABLE IF NOT EXISTS lost_items (
    item_id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    description TEXT,
    location TEXT NOT NULL,
    item_date TEXT NOT NULL,
    reporter_name TEXT NOT NULL,
    reporter_contact TEXT NOT NULL,
    reporter_id TEXT NOT NULL,
    status TEXT NOT NULL,
    reward_offered REAL DEFAULT 0.0,
    claim_pin_hash TEXT,
    claim_pin_salt TEXT,
    urgency_level TEXT DEFAULT 'MEDIUM',
    created_at INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS found_items (
    item_id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    category TEXT NOT NULL,
    description TEXT,
    location TEXT NOT NULL,
    item_date TEXT NOT NULL,
    reporter_name TEXT NOT NULL,
    reporter_contact TEXT NOT NULL,
    reporter_id TEXT NOT NULL,
    status TEXT NOT NULL,
    storage_locker TEXT,
    custody_officer TEXT,
    secret_feature TEXT,
    created_at INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS claims (
    claim_id TEXT PRIMARY KEY,
    lost_item_id TEXT,
    found_item_id TEXT,
    claimant_name TEXT NOT NULL,
    claimant_student_id TEXT NOT NULL,
    claimant_contact TEXT NOT NULL,
    hashed_pin_attempt TEXT,
    salt_used TEXT,
    proof_description TEXT,
    claim_date TEXT NOT NULL,
    verified INTEGER DEFAULT 0,
    verification_timestamp INTEGER DEFAULT 0,
    status TEXT DEFAULT 'PENDING',
    remarks TEXT
);

CREATE TABLE IF NOT EXISTS audit_logs (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    timestamp TEXT NOT NULL,
    action TEXT NOT NULL,
    entity_type TEXT NOT NULL,
    entity_id TEXT NOT NULL,
    actor TEXT NOT NULL,
    details TEXT,
    status TEXT NOT NULL
);
Security Implementation
When an owner registers a lost asset, they submit a secret 4+ digit PIN. The system processes credentials as follows:

Dynamic Salt Generation – a 16-byte cryptographically secure random salt is generated via SecureRandom and encoded as a 32-character hexadecimal string.
Salted SHA-256 Digest:
Digest = SHA-256( Salt || SecretPIN )
Verification – when a claimant submits their PIN, the system hashes the attempt using the stored salt and compares it using constantTimeEquals() to avoid side-channel timing leaks:
public static boolean verifyPIN(String enteredPin, String salt, String expectedHash) {
    String calculatedHash = hashWithSalt(enteredPin, salt);
    return constantTimeEquals(calculatedHash, expectedHash);
}
Project Structure
assignment/
├── lib/                              # External JAR dependencies
│   ├── sqlite-jdbc-3.45.1.0.jar      # SQLite JDBC Driver
│   ├── slf4j-api-1.7.36.jar          # SLF4J API
│   └── slf4j-simple-1.7.36.jar       # SLF4J Simple Logger
├── src/
│   └── com/campus/lostfound/
│       ├── model/                    # Domain entities (Item, LostItem, FoundItem, ClaimRecord, AuditLog, ItemStatus)
│       ├── security/                 # SHA-256 Hashing with Salt (HashUtil)
│       ├── db/                       # SQLite JDBC connection & schema (DatabaseManager)
│       ├── dao/                      # Data Access Objects (ItemDAO, ClaimDAO, AuditDAO)
│       ├── io/                       # File I/O & Serialization (FileLogManager, SerializationManager, CSVReportExporter)
│       ├── service/                  # Business Logic & Matching (LostFoundService, MatchingEngine)
│       ├── exception/                # Custom Exceptions (ValidationException, InvalidPINException, DatabaseException)
│       ├── gui/                      # AWT GUI & Applet (SmartCampusMainFrame, Panels, Applet, ComponentFactory)
│       └── app/                      # Application Entry Point (Main)
├── test/
│   └── com/campus/lostfound/test/    # Automated 23-Test Verification Suite (SmartCampusSystemTest)
├── docs/                             # Complete Documentation & Deliverables
│   ├── Smart_Campus_Lost_Found_Report.md
│   ├── Student_Deliverables_Report.md
│   ├── One_Page_Individual_Contribution.md
│   └── One_Page_Individual_Contribution.html
├── data/                             # Auto-generated runtime data
│   ├── campus_lost_found.db          # SQLite Database File
│   ├── audit_trail.log               # Character Stream Audit Log
│   ├── backup_lost_items.ser         # Serialized Binary Objects
│   └── campus_asset_report.csv       # Exported CSV Report
├── compile.bat                       # One-click compilation batch script
├── run.bat                           # Launch Desktop AWT GUI application
├── run_cli.bat                       # Launch automated CLI demo
├── run_applet.bat                    # Launch Appletviewer container
├── test.bat                          # Execute automated test suite
├── applet.html                       # HTML container for Applet execution
└── README.md                         # Master documentation (this file)
Prerequisites
Java Development Kit (JDK) — JDK 11 or higher, with java and javac available on PATH.
Operating System — Windows 10/11, Linux, or macOS.
Dependencies — all required JAR libraries are pre-bundled in lib/.
Getting Started
Option A: One-Click Desktop GUI Launch (Recommended)
.\run.bat
Compiles sources if needed and launches the interactive Smart Campus Main Frame with navigation tabs: Report Lost, Report Found, Auto-Match & Claim, Asset Inventory, and Audit Trail.

Option B: Run Automated Verification Tests
.\test.bat
Executes the 23-test suite across Hashing, JDBC CRUD, Serialization, File I/O, Heuristic Matching, and Exception handling.

Option C: CLI Demonstration Mode
.\run_cli.bat
Executes the full report → match → claim → verify → return workflow in the terminal without a GUI window.

Option D: Applet Container Mode
.\run_applet.bat
Launches the applet viewer, or open applet.html in an Applet-compatible browser / appletviewer applet.html.

Manual Command-Line Execution
# 1. Compile all Java source files
javac -encoding UTF-8 -d bin -cp ".;lib/*" src/com/campus/lostfound/*/*.java test/com/campus/lostfound/test/*.java

# 2. Run Desktop GUI Application
java -cp "bin;lib/*" com.campus.lostfound.app.Main

# 3. Run Automated Test Suite
java -cp "bin;lib/*" com.campus.lostfound.test.SmartCampusSystemTest

# 4. Run CLI Demo Mode
java -cp "bin;lib/*" com.campus.lostfound.app.Main --cli
Automated Test Suite
The test suite (SmartCampusSystemTest.java) verifies all functional, security, and algorithmic rubrics:

Test ID	Category	Description	Expected Outcome	Status
TC-01	Hashing (CO5)	SHA-256 Digest Length	Exactly 64 hex characters	PASS
TC-02	Hashing (CO5)	Salt Randomness & Uniqueness	Non-identical 32-char salts	PASS
TC-03	Hashing (CO5)	PIN Verification Success	Correct PIN matches hash	PASS
TC-04	Hashing (CO5)	Tampered PIN Rejection	Wrong PIN strictly rejected	PASS
TC-05	Hashing (CO5)	Constant-Time Equality	Timing-attack resistance	PASS
TC-06	Algorithm	High-Similarity Auto-Match	Score ≥ 75% confidence	PASS
TC-07	Algorithm	Low-Similarity Discrimination	Score < 35% confidence	PASS
TC-08	Serialization (CO5)	Byte Stream Serialization	.ser file with non-zero size	PASS
TC-09	Serialization (CO5)	Object Deserialization	Object graph attributes intact	PASS
TC-10	Serialization (CO5)	SHA-256 Binary Integrity Check	64-char tamper-detection checksum	PASS
TC-11	File I/O (CO5)	Character Stream Logging	Writes/reads audit log lines	PASS
TC-12	File I/O (CO5)	CSV Report Export	campus_asset_report.csv	PASS
TC-13	File I/O (CO5)	Summary Text Export	campus_summary_report.txt	PASS
TC-14	JDBC CRUD (CO5)	Insert Lost Item (Create)	Record stored in lost_items	PASS
TC-15	JDBC CRUD (CO5)	Insert Found Item (Create)	Record stored in found_items	PASS
TC-16	JDBC CRUD (CO5)	Retrieve Items (Read)	Rows mapped into List<Item>	PASS
TC-17	JDBC CRUD (CO5)	Smart Matching Query	Parameterized SQL query	PASS
TC-18	Security & Workflow	Claim PIN Verification	Claim approved & verified	PASS
TC-19	JDBC CRUD (CO5)	Update Status (Update)	Status → RESOLVED_RETURNED	PASS
TC-20	JDBC CRUD (CO5)	Delete Record (Delete)	Record removed from table	PASS
TC-21	Exception Handling	Missing Required Title	Throws ValidationException	PASS
TC-22	Exception Handling	Malformed Date Format	Rejects non YYYY-MM-DD	PASS
TC-23	Exception Handling	Wrong PIN Claim Attempt	Throws InvalidPINException	PASS
Summary: 23 Passed, 0 Failed (100.0% Pass Rate)

Deliverables
Deliverable	File	Description
Student Deliverables Report	docs/Student_Deliverables_Report.md	Complete 12-section report: Problem Statement, Objectives, Design, Algorithms, Implementation, Test Cases, Outputs, Analysis, Conclusion, Contributions, References.
Full Project Report	docs/Smart_Campus_Lost_Found_Report.md	Comprehensive capstone report.
Print-Ready 1-Page Write-Up	docs/One_Page_Individual_Contribution.html	Styled HTML with print CSS for direct 1-page printing.
1-Page Write-Up (Markdown)	docs/One_Page_Individual_Contribution.md	Individual contribution write-up.
Database Schema DDL	src/com/campus/lostfound/db/DatabaseManager.java	Table definitions and index creation.
FAQ & Troubleshooting
Q1: Compilation error: javac is not recognized

Ensure JDK 11+ is installed and the JDK bin directory is on your PATH.
Q2: GUI does not display in a headless environment

Run in CLI mode: run_cli.bat or java -cp "bin;lib/*" com.campus.lostfound.app.Main --cli.
Q3: How do I print the 1-page individual contribution write-up?

Open docs/One_Page_Individual_Contribution.html in a browser and press Ctrl + P. CSS is configured for A4 1-page portrait printing.
Project Team
Role	Name	Registration / Roll
Lead / Architect	Alex Johnson	2026-CS-0916-A
Database Layer	Member 2	2026-CS-0916-B
File I/O & Applet	Member 3	2026-CS-0916-C
Matching & Testing	Member 4	2026-CS-0916-D
Course: CSA09 – Programming in Java (Slot C)
Department: Department of Computer Science and Engineering
