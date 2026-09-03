# INDIVIDUAL CONTRIBUTION WRITE-UP
## Capstone Assignment: Smart Campus Lost & Found and Asset Recovery System
### Course Code & Name: CSA09 – Programming in Java (Slot C)

---

### Student Information
- **Student Name**: [Student Name / Alex Johnson]
- **Register / Roll Number**: [Register Number / 2026-CS-0916]
- **Department**: Department of Computer Science and Engineering
- **Capstone Team ID / Slot**: Team Slot C - Group 04
- **Project Title**: Smart Campus Lost & Found and Asset Recovery System using Java GUI, File Handling, Serialization, Hashing and JDBC

---

### 1. Executive Summary & Assigned Role
As the **Core Architect and Lead Backend/Security Developer** for this Capstone project, I was responsible for architecting the end-to-end multi-tier system, implementing the cryptographic authentication subsystem using SHA-256 with 128-bit dynamic salting, and engineering the core desktop GUI with the Delegation Event Model. My role focused on fulfilling Course Outcomes **CO4** (GUI & Event Model) and **CO5** (Cryptographic Hashing & Persistent Data Management).

---

### 2. Specific Modules, Features & Tasks Handled

#### A. Cryptographic Hashing Subsystem (`com.campus.lostfound.security.HashUtil`)
- Designed and coded the **SHA-256 cryptographic salted authentication mechanism** using `java.security.MessageDigest` and `java.security.SecureRandom`.
- Implemented 16-byte random salt generation encoded in 32-character hexadecimal format to eliminate rainbow table and dictionary vulnerabilities.
- Integrated **constant-time byte comparison** (`constantTimeEquals`) to protect identity credential verification from side-channel timing attacks.
- Ensured zero plaintext PIN persistence across SQLite database tables, audit logs, and serialized binary files.

#### B. AWT GUI Architecture & Delegation Event Model (`com.campus.lostfound.gui.*`)
- Developed the master application frame (`SmartCampusMainFrame`) implementing `ActionListener` and `WindowListener`.
- Built reusable UI components with `ComponentFactory` using customized color tokens, styled typography, and card panels.
- Designed `LostReportPanel` and `MatchClaimPanel` using `GridBagLayout` and `CardLayout` for input validation and interactive feedback dialogs.
- Created event handlers responding to `ActionEvent`, `ItemEvent`, and window lifecycle transitions with automatic backup serialization on exit.

#### C. Full-Stack Integration & Service Orchestration (`com.campus.lostfound.service.LostFoundService`)
- Implemented the master business logic facade coordinating `ItemDAO`, `ClaimDAO`, `AuditDAO`, `FileLogManager`, and `SerializationManager`.
- Created custom exception handling workflows (`ValidationException`, `InvalidPINException`, `DatabaseException`) providing graceful UI error alerts.
- Formulated the 4-factor heuristic auto-match algorithm combining category matching, text similarity, location proximity, and timeline difference.

---

### 3. Key Learning Outcomes & Sustainable Development Goals (SDG) Reflection
- **Technical Growth**: Gained deep practical mastery of Java GUI event models, constant-time cryptographic verification, byte/character stream persistence, and JDBC ACID transaction management.
- **SDG Alignment**: Contributed directly to **SDG 4** (Quality Education) and **SDG 12** (Responsible Consumption) by building an asset recovery platform that saves students time, prevents academic disruption, and reduces campus electronic waste.

---

**Student Signature**: ___________________________ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Date**: ___________________________

**Faculty Evaluator Remarks & Signature**: ____________________________________________________
