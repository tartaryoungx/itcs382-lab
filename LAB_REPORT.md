# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/tartaryoungx/itcs382-lab.git

## Tools

| Item | Version or name |
| --- | --- |
| JDK | OpenJDK 21.0.13.0.2 (Homebrew); Maven project targets Java 17 |
| Maven | Apache Maven 3.10.0 |
| IDE | Visual Studio Code (version to be confirmed) |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | |

## Task 1. SonarQube on the starter code

Commit of the starter, before any Java source change: 9770192

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 2 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 3 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 4 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |
| 5 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 6 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 7 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 10 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 11 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 12 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 13 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | Pending confirmation of the review run date |
| AI tool and exact model | Pending confirmation from the review chat |
| Commit reviewed | 9770192 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | Attached four Java files, according to the supplied review output |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

The original review contains 32 findings: 14 in Check 1 and 18 in Check 2 (6 cohesion, 5 coupling, 4 connascence and 3 encapsulation). The table below checks the findings against the starter source; it does not claim runtime tests were performed for this review. AI finding numbers refer to the unchanged review file. Related AI findings are retained with overlap noted so every original claim can be checked. Findings shared with SonarQube appear once and are marked both. Java-extension warnings are not counted as SonarQube findings.

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | `ParcelService.java:42` | AI #1: Strings are compared using `==` instead of `.equals()`, so pickup may fail even when parcel codes contain identical text. | both | java:S4973; String comparison | Yes: line 42 compares String references, not their text. |
| 2 | `ParcelService.java:76–80` | AI #2: `FileWriter` is never closed, risking buffered data loss and resource leaks. | both | java:S2095; resource management | Yes: the writer is neither closed nor managed by try-with-resources. |
| 3 | `ParcelService.java:80–81` | AI #3: Empty catch block silently ignores file-writing errors. | both | java:S108; empty catch | Yes: the IOException handler contains no statements and hides save failures. |
| 4 | `Main.java:8–26` | AI #4: `Scanner` is not closed. | AI | Resource management | Yes: the Scanner has no close operation. Closing it also closes System.in, which is acceptable when this standalone program exits. |
| 5 | `Main.java:11–17` | AI #5: Commands access array positions without checking argument counts, potentially causing `ArrayIndexOutOfBoundsException`. | AI | Input validation | Yes: store and pickup index words without checking its length; incomplete commands can throw an exception. |
| 6 | `Main.java:13,17` | AI #6: Invalid numeric input can cause an uncaught `NumberFormatException`. | AI | Input validation | Yes: both parseInt calls can throw NumberFormatException and no handler catches it. |
| 7 | `ParcelService.java:24–36` | AI #7: Parcel sizes are not validated, allowing invalid sizes to be processed. | AI | Input validation | Yes: no size check exists. For example, size 0 passes the locker size comparison and is stored. |
| 8 | `ParcelService.java:39–49` | AI #8: Returning `0` when a parcel is not found makes failure indistinguishable from a successful free pickup. | AI | Ambiguous result | Yes: both a missing parcel and a valid pickup within three days produce a fee of zero. |
| 9 | `ParcelService.java:6` | AI #9: Unused import `java.util.Date`. | both | java:S1128; unused import | Yes: java.util.Date is imported but never referenced. |
| 10 | `ParcelService.java:12` | AI #10: Field `maxParcels` is declared but never used. | both | java:S1068; unused field | Yes: maxParcels is initialized but never read or used to enforce a limit. |
| 11 | `Parcel.java:13, ParcelService.java:28,44,68` | AI #11: Parcel status is represented by repeated string literals. | AI | Status convention | Yes, with qualification: STORED is repeated; NEW and PICKED_UP each occur once. An enum would make valid states explicit. |
| 12 | `Main.java:6–27` | AI #12: The main method combines input parsing, command dispatching, service calls and output handling, making it difficult to read and maintain. | AI | Method complexity | No as an established defect: main is short and its parsing, dispatch and output all serve the CLI. Extraction may help, but the finding gives no clear complexity evidence. |
| 13 | `ParcelService.java:24–36` | AI #13: `storeParcel()` combines locker selection, state updates, notification output and persistence, making the method unnecessarily complex. | AI | Mixed responsibilities | Yes: storeParcel allocates a locker, changes state, prints a notification and triggers persistence. This is mainly a design concern, not proof that the method is too long. |
| 14 | `ParcelService.java:74–82` | AI #14: `FileWriter` overwrites `parcels.csv` on each save, which can destroy previously saved data if historical records are expected to persist across application restarts. | AI | Persistence assumption | No as a confirmed requirements violation: FileWriter overwrites the file, but preserving history across restarts is not specified. The finding depends on an extra assumption. |
| 15 | `Main.java:6` | AI #15: `main()` has procedural cohesion because it groups command parsing, command execution and output operations in one control flow. | AI | Procedural cohesion (claimed) | No as justified here: the operations serve command processing and pass related data between steps. Sharing a control flow alone does not establish procedural cohesion. |
| 16 | `ParcelService.java:24` | AI #16: `storeParcel()` has procedural cohesion because it combines storage, notification and file persistence as steps in one workflow. | AI | Procedural cohesion (claimed) | No as justified here: the steps serve the same parcel-storage operation. Mixed responsibilities are observable, but their order alone does not prove this cohesion level. |
| 17 | `ParcelService.java:39` | AI #17: `pickUp()` has procedural cohesion because it combines parcel lookup, locker modification, status updates, persistence and fee calculation. | AI | Procedural cohesion (claimed) | No as justified here: lookup, state transition and fee calculation use the same pickup data. The review does not justify why cohesion is only procedural. |
| 18 | `ParcelService.java:64` | AI #18: `printParcels()` has logical cohesion because a boolean selects between two different output behaviors. | AI | Logical cohesion | Yes: the boolean selects between a staff listing and a customer listing within the same method. |
| 19 | `ParcelService.java:9–82` | AI #19: `ParcelService` has weak class cohesion because it manages lockers, parcel records, fee calculation, notification output and file persistence. | AI | Weak class cohesion | Yes: ParcelService owns allocation, fees, notification output and CSV persistence, which have separate reasons to change. |
| 20 | `Main.java:5–27` | AI #20: `Main` mixes user interface parsing with application workflow decisions rather than focusing only on application entry and command handling. | AI | Weak class cohesion (claimed) | No as justified here: Main parses and dispatches CLI commands while delegating storage and fee rules to ParcelService. This fits a CLI entry-point responsibility. |
| 21 | `Main.java:22` | AI #21: Stamp coupling: `Main` receives the complete locker collection through `getLockers()` although it only needs the number of free lockers. | AI | Stamp coupling | Yes: Main receives a collection of Locker objects although it only needs a free-locker count. |
| 22 | `Main.java:19–21`, `ParcelService.java:64` | AI #22: Control coupling: `Main` passes a boolean to choose which behavior `printParcels()` performs. | AI | Control coupling | Yes: Main passes true or false to select the behavior of printParcels. |
| 23 | `ParcelService.java:28,44,67–68` | AI #23: Content coupling: `ParcelService` directly reads and modifies the public `status` field belonging to `Parcel`. | AI | Content coupling; encapsulation | Yes: ParcelService directly accesses Parcel.status rather than using controlled operations. This overlaps AI finding 30. |
| 24 | `Main.java:22` | AI #24: `Main` reaches through `ParcelService` to access `Locker` objects and invoke `isFree()`, increasing coupling to internal implementation details. | AI | Coupling to internal structure | Yes: Main traverses the service collection and calls Locker.isFree. This is another aspect of AI finding 21. |
| 25 | `ParcelService.java:24–32,39–46,64–69,74–78` | AI #25: `ParcelService` depends on `Locker`, `Parcel`, console output and file I/O, creating multiple implementation dependencies. | AI | Implementation dependencies | Yes, with qualification: console and file operations are embedded in the service. The dependency count alone is not proof of excessive coupling. |
| 26 | `Main.java:11–17` | AI #26: Connascence of position: The command parser depends on arguments appearing at fixed array positions. | AI | Connascence of position (claimed) | No as an inter-class finding: words indexes describe the command format inside Main; no second class sharing these positions is identified. |
| 27 | `Locker.java:5`, `Parcel.java:6`, `ParcelService.java:56–61` | AI #27: Connascence of convention: Multiple classes rely on the undocumented numeric mapping `1=small`, `2=medium`, `3=large`. | AI | Connascence of convention | Yes for the shared size mapping, but not the claim that it is undocumented: Locker and Parcel both comment that 1, 2 and 3 mean small, medium and large. |
| 28 | `Parcel.java:13`, `ParcelService.java:28,44,68` | AI #28: Connascence of name: Classes must agree on exact status strings such as `NEW`, `STORED` and `PICKED_UP`. | AI | Connascence of convention; name (claimed) | No for the stated type: the meaning of status string values is a shared convention. Name connascence concerns agreed identifier names; an enum would make the convention explicit. |
| 29 | `ParcelService.java:52–61` | AI #29: Connascence of algorithm: Fee calculation relies on the hardcoded three-day free period and size-based rates, requiring coordinated changes if fee rules appear elsewhere. | AI | Connascence of algorithm (claimed) | No: the fee algorithm appears only in fee(). The review identifies no second implementation requiring coordinated changes. |
| 30 | `Parcel.java:7` | AI #30: The `status` field is public, allowing other classes to modify parcel state directly. | AI | Encapsulation | Yes: status is public and can be changed directly by any caller. This overlaps AI finding 23. |
| 31 | `ParcelService.java:20–21` | AI #31: `getLockers()` returns the mutable internal `List<Locker>`, allowing callers to add or remove lockers. | AI | Encapsulation; mutable collection | Yes: getLockers returns the original list, so callers can add or remove lockers. An unmodifiable list would still expose mutable Locker elements. |
| 32 | `Locker.java:21–26` | AI #32: Callers can directly obtain and replace a locker's parcel without validation, bypassing service-level storage rules. | AI | Encapsulation; invariant protection | Yes: callers can replace a locker parcel without checking whether it is occupied or whether the parcel fits. |
| 33 | Main.java:9 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 34 | Main.java:15 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 35 | Main.java:17 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 36 | Main.java:22 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 37 | ParcelService.java:1 | Use the "java.time" API for date and time. | SonarQube | java:S2143 | No as a needed API migration: Date is unused, so removing the import is sufficient. Line 1 is the location reported by SonarQube; the import is on line 6. |
| 38 | ParcelService.java:30 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 39 | ParcelService.java:67 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |
| 40 | ParcelService.java:69 | Replace this use of System.out by a logger. | SonarQube | java:S106 | Yes: this statement uses System.out. For a CLI, user-facing output can be kept with a documented reason; it is not automatically a functional bug. |

2.1 Name two problems that only the AI review found.

First, Main passes a boolean to printParcels(), creating control coupling because the caller selects the method's behavior. Second, getLockers() returns the service's mutable internal list, allowing callers to add or remove lockers. Neither problem appears in the collected SonarQube results.

2.2 Name one problem that only SonarQube found, or write none.

SonarQube reported java:S106 for System.out, for example at Main.java:9. The AI discussed output responsibilities but did not report the specific logger rule. Since this program is a CLI, keeping user-facing console output can be justified rather than treating every occurrence as a bug.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

AI finding 29 claims connascence of algorithm for the fee rules. The calculation appears only in ParcelService.fee(), and the review identifies no other class implementing the same algorithm. Its claim depends on rules appearing elsewhere, which is not shown in the four starter files, so it is not a supported inter-class connascence finding.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Kept | CLI command help is intentional user-facing output, so System.out is appropriate. |
| 2 | Kept | The assigned locker or unavailable-locker message is the CLI response to store. |
| 3 | Kept | The pickup fee is a required user-facing CLI result. |
| 4 | Kept | The free-locker count is a user-facing response to the staff command. |
| 5 | Fixed | e7cb125: removed the unused java.util.Date import; no date API is needed. |
| 6 | Fixed | e7cb125: removed the unused java.util.Date import. |
| 7 | Fixed | b366ee9: removed the unused maxParcels field. |
| 8 | Kept | The console SMS message is the existing notification simulation; retaining it keeps the CLI behavior unchanged. |
| 9 | Fixed | 9aba929: compare parcel-code text with equals() instead of comparing String references. |
| 10 | Kept | Staff parcel records are intentional CLI output; the statement now belongs to printStaffParcels(). |
| 11 | Kept | Waiting-parcel messages are intentional CLI output; the statement now belongs to printWaitingParcels(). |
| 12 | Fixed | e26da71: use try-with-resources to close and flush FileWriter. |
| 13 | Fixed | 481c3c2: log IOException with System.Logger so save failures are visible. |

Number of SonarQube issues that remain after your fixes: **Pending a fresh SonarQube for IDE scan.** Seven original java:S106 occurrences were intentionally kept; this is not a measured post-fix issue count.

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Content coupling and exposed state (AI findings 23 and 30) | Made Parcel.status private. ParcelService now uses markStored(), markPickedUp(), getStatus() and isStored(), so it no longer directly reads or writes the field or interprets status strings. | f4daf4d |
| Control coupling and logical cohesion (AI findings 18 and 22) | Replaced printParcels(boolean) with printWaitingParcels() and printStaffParcels(), and updated Main to call the appropriate operation without passing a control flag. | a2eb7a2 |

Validation: Maven package succeeded after each fix using JDK 21 with Java release 17. CLI checks verified pickup, customer and staff listings, and CSV contents. A separate check made parcels.csv a directory in a temporary working folder to force a write failure; the error was logged instead of silently ignored. These were command-line checks, not a committed automated test suite. The checks used temporary working folders so existing parcel data was not overwritten.

The changes are deliberately limited to the listed issues and two design problems. Input validation, mutable locker access and the broader service responsibilities remain possible follow-up improvements.

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | | |
| AI tool and exact model | | |
| Prompt (B or C as given, or your changed prompt in full) | | |
| Builds with mvn package (yes or no) | | |
| Number of classes, interfaces, enums and records | | |
| SonarQube issues, total | | |
| SonarQube issues, by rule | | |
| AI review findings in Check 1 | | |
| AI review findings in Check 2 | | |
| Review output file in the reviews folder | | |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

5.2 When should you not trust the AI review? Give an example from this lab.

5.3 What will you do differently in Project Phase 2 after this lab?

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| | | | |
