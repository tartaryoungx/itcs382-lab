# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/tartaryoungx/itcs382-lab.git

## Tools

| Item | Version or name |
| --- | --- |
| JDK | OpenJDK 21.0.13.0.2 (Homebrew); Maven project targets Java 17 |
| Maven | Apache Maven 3.10.0 |
| IDE | Visual Studio Code 1.141.0 |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | Codex, GPT-6 family; exact model identifier not exposed in this session. The original ParcelPoint review tool/model was not recorded. |

## Task 1. SonarQube on the starter code

Commit of the starter, before any Java source change: 36003bd

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
| Date | Original run date not recorded; review received on 25 October 2026 |
| AI tool and exact model | Not recorded in the supplied original review; cannot verify the tool or exact model |
| Commit reviewed | 36003bd |
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
| 5 | Fixed | ab0ec4f: removed the unused java.util.Date import; no date API is needed. |
| 6 | Fixed | ab0ec4f: removed the unused java.util.Date import. |
| 7 | Fixed | 111d284: removed the unused maxParcels field. |
| 8 | Kept | The console SMS message is the existing notification simulation; retaining it keeps the CLI behavior unchanged. |
| 9 | Fixed | f2dc5b5: compare parcel-code text with equals() instead of comparing String references. |
| 10 | Kept | Staff parcel records are intentional CLI output; the statement now belongs to printStaffParcels(). |
| 11 | Kept | Waiting-parcel messages are intentional CLI output; the statement now belongs to printWaitingParcels(). |
| 12 | Fixed | 6ee2096: use try-with-resources to close and flush FileWriter. |
| 13 | Fixed | 4eae130: log IOException with System.Logger so save failures are visible. |

Number of SonarQube issues that remain after your fixes: **7**, all java:S106, confirmed by the post-fix IDE Problems output supplied on 25 October 2026. Locations: Main.java:9, 15, 17, 22 and ParcelService.java:29, 65, 72. These console-output issues were intentionally kept for the reasons in Table 3. The separate Java-extension warning about the unclosed Scanner at Main.java:8 remains, but is not included in the SonarQube count.

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Content coupling and exposed state (AI findings 23 and 30) | Made Parcel.status private. ParcelService now uses markStored(), markPickedUp(), getStatus() and isStored(), so it no longer directly reads or writes the field or interprets status strings. | 5df8b6d |
| Control coupling and logical cohesion (AI findings 18 and 22) | Replaced printParcels(boolean) with printWaitingParcels() and printStaffParcels(), and updated Main to call the appropriate operation without passing a control flag. | 3f6de8b |

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
| Date | 25 October 2026 (generation) | 25 October 2026 (generation) |
| AI tool and exact model | Codex, GPT-6 family; exact model identifier unavailable | Codex, GPT-6 family; exact model identifier unavailable |
| Prompt (B or C as given, or your changed prompt in full) | B as given, with the supplied BikeShare specification | C as given, with the supplied BikeShare specification |
| Builds with mvn package (yes or no) | yes (JDK 21, Java release 17) | yes (JDK 21, Java release 17) |
| Number of classes, interfaces, enums and records | 1 class; 0 interfaces; 0 enums; 0 records | 4 classes; 1 interface; 2 enums; 1 record |
| SonarQube issues, total | 18 | 9 |
| SonarQube issues, by rule | java:S106: 10; java:S1141: 1; java:S1192: 2; java:S135: 1; java:S2386: 3; java:S3776: 1 | java:S106: 9 |
| AI review findings in Check 1 | 3 | 0 |
| AI review findings in Check 2 | 4 | 3 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

Poor-version AI review: 25 October 2026; Codex, GPT-6 family as reported by the supplied review, exact model identifier unavailable. According to the supplied provenance, the review used a fresh agent without the earlier conversation history rather than a new UI chat. This is a procedural deviation from the lab's new-chat instruction. The review file preserves all findings and not-found rows; its counts are the original AI counts, not a count of findings accepted after checking.

Review check: Findings 1-3 and 5-7 point to visible code: the long combined method, repeated station names and numeric assumptions, boolean-selected operations, mixed class responsibilities and public mutable arrays. Finding 4 is debatable: arranging CLI steps in sequence alone does not establish procedural cohesion. The review correctly distinguishes dependencies within the single Main class from coupling between application classes. No BikeShare code was changed in response to this review.

Good-version AI review: 25 October 2026; Codex, GPT-6 family, exact model identifier unavailable. Prompt A was applied to all eight Java files opened from the local folder at commit bae3c03. At my request, this review used the same assistance chat instead of a fresh chat, so it is not an independent review and may be influenced by the generation history. The three reported design findings are Notes, not confirmed functional bugs. I have kept the complete output in the reviews folder and recorded this limitation instead of treating the review as proof of correctness.

Generation note: Prompts B and C were applied in the ongoing assistance chat, rather than separate new chats as requested in the lab procedure. The poor version built successfully and passed command-line checks for both fee rates, cross-station returns, empty stations, invalid input, CSV append across runs and file-write failure handling.

The good version passed the same rental/return scenarios, plus checks for exactly two initial bikes per station, CSV escaping of member IDs and fee calculations without int overflow. Its CSV columns are bike ID, member ID, minutes, fee and destination; the poor version records bike ID, minutes, fee and destination. No CSV schema was prescribed by the specification. Both versions retain bike inventory in memory and append completed rides to disk.

SonarQube scan note: The new Problems output supplied after opening the BikeShare project on 25 October 2026 contains 18 SonarQube issues and no non-project-file diagnostic. The earlier java:S1172 warning about main(String[] args) is absent from this scan. Counts use this latest output only.

The supplied good-version Problems output contains 9 issues, all java:S106 in Main.java at lines 23, 28, 29, 30, 52, 55, 57, 64 and 69, with no non-project-file diagnostic. No issues from other files appear in that output.

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

No rule in the collected output explicitly identifies a cohesion level or coupling type. Related design concerns are flagged: java:S2386 reports `Make this member "protected".` for the public mutable arrays at Main.java:14-16, and java:S3776 reports "Refactor this method to reduce its Cognitive Complexity from 22 to the 15 allowed." at Main.java:52. These address exposed mutable state and method complexity, but do not directly classify the boolean-based operation as logical cohesion or control coupling.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. The poor review identifies public mutable arrays at Main.java:14-16, which allow callers to change fleet and rental state independently. The good review does not find that issue: BikeService keeps its map private, Bike keeps its state private, and no internal mutable collection is returned. The poor review reports 3 code-quality and 4 design findings, while the good review reports 0 and 3; these counts alone are not a quality score, and the reviews were performed with different conversation context.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

I would extend bikeshare-good. Adding VISITOR(4) to MemberType supplies the new rate, and the CLI already parses enum values, so only its help text also needs updating. BikeService and CsvRideStore would not need changes. In bikeshare-poor, the parser and the int-based fee conditional both need changes; otherwise a new type would fall through to the staff rate.


## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

In this lab, I used SonarQube to identify concrete issues such as String comparison with ==, an unclosed FileWriter, an empty catch block and unused code. It also reported exposed mutable fields and method complexity in bikeshare-poor, so it can flag some design-related symptoms. However, the collected results did not explain cohesion levels or coupling types, so I used the AI reviews and the source code to examine those separately.

5.2 When should you not trust the AI review? Give an example from this lab.

I should not accept an AI finding just because it uses a course term or sounds confident. For example, the ParcelPoint review claimed connascence of algorithm even though the fee calculation appeared in only one method and no second implementation was identified. I need to check the cited code and the definition, and I should be especially cautious about the good-version review because it was produced in the same chat that generated the code.

5.3 What will you do differently in Project Phase 2 after this lab?

In Phase 2, I will keep a baseline commit and collect both SonarQube results and an AI review before changing the code. I will use those results to choose small fixes, check program behavior and saved data, and record the reason for any warning I keep. I will also record the exact AI model and date at the time of each run, and keep generation and review in separate chats so I do not repeat the documentation and review limitations from this lab.

## AI usage declaration

I completed this lab with AI collaboration. I collected the SonarQube results in VS Code and supplied them for the report, while AI generated the BikeShare programs, helped apply ParcelPoint fixes, ran command-line checks and drafted report sections. The Java code and report are therefore not entirely handwritten by me; the table records the assistance used, including work beyond the required AI reviews.

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Original review tool/model not recorded; output received 25 October 2026 | Review the original ParcelPoint code with Prompt A (Task 2) | reviews/parcelpoint-review.md; findings used in Table 2 | Findings were compared with the starter source and collected SonarQube results, with AI assistance. Table 2 records supported, disputed and overlapping claims rather than accepting the whole review. |
| Codex, GPT-6 family; exact identifier unavailable; 25 October 2026 | Assist with setup, apply selected ParcelPoint fixes, generate poor/good BikeShare using Prompts B/C, run checks and draft the report | ParcelPoint Main.java, Parcel.java and ParcelService.java; all files in bikeshare-poor and bikeshare-good; LAB_REPORT.md and repository setup | I supplied the IDE's SonarQube results. Codex ran Maven builds and CLI checks for pickup fees, CSV output, BikeShare rentals/returns, invalid input and save failures; the report distinguishes these AI-run checks from my IDE checks. |
| Codex, GPT-6 family as reported in the supplied review; exact identifier unavailable; 25 October 2026 | Review bikeshare-poor with Prompt A using a fresh agent without prior conversation history | reviews/bikeshare-poor-review.md; Task 4 review counts | I supplied the review output. Its locations were checked against Main.java with AI assistance; the report notes that the procedural-cohesion claim is debatable and retains the original counts. |
| Codex, GPT-6 family; exact identifier unavailable; 25 October 2026 | Review bikeshare-good with Prompt A in the same assistance chat and help compare the versions | reviews/bikeshare-good-review.md; Task 4 comparison and Task 5 draft | The cited locations were inspected in all eight files and compared with the actual SonarQube results. I have disclosed that this was a same-chat review of AI-generated code and is not independent verification. |

The exact model labels were not available in the assistance session, and the original ParcelPoint review metadata was not recorded. These are reporting limitations against the lab's exact-model requirement; no model identifier or original run date has been invented. All assistance in this working session took place on 25 October 2026.
