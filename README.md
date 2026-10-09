# Assignment 3: WBS, LOE Estimates, and Gantt Chart

**Course:** Application Development 2
**App Topic:** JAC Student Marketplace (JACPot)

## 1. Project Overview

This project is a multi-platform (Android + Desktop/Web) application that serves as a student marketplace for John Abbott College. It utilizes Kotlin/Compose Multiplatform, Navigation 3, and Material 3 to provide 4 primary screens: a listing creation form, an item preview screen, an interactive feed of listings, and an informational "About" screen.

## How to Use & Run

### Running on Android

- Open the project in Android Studio.
- Select the `androidApp` run configuration and click the green **Play** button to launch on an emulator or physical device.

### Running on Desktop (POSSIBLE BUG)

- My own pc for some reason cannot run the desktop version from the normal run button top right, I get this error "Failed to find suitable JetBrains Runtime 25 installation on your system." This may not happen on any other properly set up devices.
- For the terminal option, open the built-in terminal at the bottom of Android Studio. (This works on my device even with the runtime system error I got).
- Run the following Gradle command to launch the standalone desktop window:
  ```bash
  .\gradlew :desktopApp:run
  ```

### Known Issues & Limitations

- Desktop Layout Scaling: While the application features a fully responsive adaptive layout (switching from a mobile bottom navigation bar to a desktop navigation rail), the marketplace item cards on wide desktop displays utilize a fixed max-width constraint to prevent images from stretching excessively. On ultra-wide monitors, extra whitespace may appear around the outer edges of the feed.

- Navigation State Restorations: Rapid back-and-forth navigation during active layout resizes can occasionally trigger lifecycle warnings in the underlying beta KMP navigation bundle, though core functionality remains fully stable.

- A believed system side issue in running the desktop version from the run button (more of this explained above in the Running on Desktop section). It should just be my system which is causing the issue.

## 2. Work Breakdown Structure (WBS) & Level of Effort (LOE) Estimates

The project has been broken down into 6 primary task categories with at least 5 distinct subtasks as required. The total estimated Level of Effort (LOE) is 6.5 hours.

| Task ID | Task Description                                                          | Dependencies  | Estimated LOE | Actuals      |
| :------ | :------------------------------------------------------------------------ | :------------ | :------------ | :----------- |
| **1.0** | **Project Setup & Preliminary Design**                                    | **None**      | **1.0 hr**    | **0.7 hrs**  |
| 1.1     | Initialize Kotlin/Compose Multiplatform project (Android + Desktop/Web)   | None          | 0.25 hrs      | 0.2 hrs      |
| 1.2     | Setup Git repository and main machine environment                         | 1.1           | 0.25 hrs      | 0.25 hrs     |
| 1.3     | Iterate with AI on preliminary design and document findings               | 1.2           | 0.5 hrs       | 0.25 hrs     |
| **2.0** | **Navigation & Core Layout**                                              | **1.0**       | **1.0 hr**    | **0.45 hrs** |
| 2.1     | Implement shared layout with persistent navigation bar                    | 1.1           | 0.5 hrs       | 0.30 hrs     |
| 2.2     | Define routing using a sealed class and set up Navigation 3               | 2.1           | 0.5 hrs       | 0.15 hrs     |
| **3.0** | **Data Entry & Preview Screens (Screens 1 & 2)**                          | **2.0**       | **1.5 hrs**   | **0.45 hrs** |
| 3.1     | Build "Create Listing" form with Material 3 inputs and image URL field    | 2.2           | 0.75 hrs      | 0.15 hrs     |
| 3.2     | Implement visually compelling "Listing Preview" screen passing parameters | 3.1           | 0.75 hrs      | 0.30 hrs     |
| **4.0** | **Interactive State & Feed Screen (Screen 3)**                            | **3.0**       | **1.5 hrs**   | **0.55 hrs** |
| 4.1     | Implement shared state provider for the list of items                     | 3.1           | 0.5 hrs       | 0.35 hrs     |
| 4.2     | Build interactive list (view details, remove items)                       | 4.1           | 1.0 hr        | o.20 hrs     |
| **5.0** | **Information Screen & Polish**                                           | **2.0**       | **1.0 hr**    | **0.75 hrs** |
| 5.1     | Design visually impressive "About JAC Marketplace" screen                 | 2.2           | 0.5 hrs       | 0.25 hrs     |
| 5.2     | Apply responsive design for smaller/larger screens (Bonus Requirement)    | 2.1           | 0.5 hrs       | 0.5 hrs      |
| **6.0** | **Documentation & Final Review**                                          | **1.0 - 5.0** | **1.0 hrs**   | **2.25hrs**  |
| 6.1     | Compile AI usage log in ADR format (min. 3 key decisions)                 | 1.3           | 0.25 hrs      | 0.25 hrs     |
| 6.2     | Finalize Readme and compile actual LOE metrics                            | 6.1           | 0.25 hrs      | 0.5 hrs      |
| 6.3     | Testing                                                                   | All           | 0.5 hrs       | 1.5 hrs      |
|         | **Total Estimated LOE**                                                   |               | **6.5 hrs**   | **5.15 hrs** |

## 3. Gantt Chart

(Note: The Gantt chart below visually maps the task dependencies and expected durations in a waterfall sequence.)

![JAC Marketplace Gantt Chart](RootResources/GanttChart.jpg)

# Architecture Decision Record (ADR) & AI Usage Log

**Status:** Accepted

**Context:**
The project involves developing the JAC Student Marketplace application using Kotlin Multiplatform (KMP) and Jetpack Compose to support both Android and Desktop targets. The immediate goals were to implement a responsive layout, a modernized image-centric feed, seamless item deletion flows, and robust cross-platform navigation while overcoming environment-specific build and library version hurdles.

**Decision (AI Prompts & Outputs):**

## Key Architectural Decisions (ADR Log)

### Decision 1: Master State Management (State Hoisting vs. ViewModel)
* **Prompt:** Requested guidance on how to share and persist the `itemList` across multiple screens (Create, Preview, and Feed) without setting up a complex KMP ViewModel.
* **Output:** Provided a clean State Hoisting pattern using `rememberSaveable` with a custom `listSaver` serialized at the root `App.kt` level.
* **Impact:** Kept the codebase lightweight, instantly compatible with prior course knowledge, and solved multiplatform state preservation.

### Decision 2: Responsive Shared Layout & Navigation
* **Prompt:** Asked how to implement a responsive adaptive layout that dynamically switches between a mobile bottom bar and a desktop navigation rail based on screen width.
* **Output:** Outlined the implementation of `BoxWithConstraints` to evaluate window width dynamically (< 600.dp vs. wide screen), adapting both `MarketplaceBottomBar` and `MarketplaceNavRail`.
* **Impact:** Fulfilled responsive design requirements and gave the app a native look and feel on both mobile and desktop form factors.

### Decision 3: Feed UX & Deletion Flow
* **Prompt:** Requested a modern, image-focused marketplace card layout for Screen 3 with safe item deletion and detailed viewing capabilities.
* **Output:** Designed full-width image banners with cards capped in width for desktop, paired with a Material 3 `AlertDialog` for viewing details and a separate red confirmation popup for deletions.
* **Impact:** Prevented accidental deletions, cleanly separated view actions from destructive actions, and optimized visual presentation.

---
**Rationale:**
Since the assignment required at least 50%, I decided to embrace it and use it for basically everything. It was freeing and helped me get it looking nice, but I ran into an error (I think from my pc) which I spend a LONG time fixing which ended up using all my remaining ai tokens, so there may have been some changes to the desktop view I may have wanted ai to revamp but fixing the errors took too much energy (from me and the AI lol).

**Consequences / AI Generation Limits:**

- **AI-Generated Code:** ~65% (Heavy AI collaboration was utilized to refactor build scripts, resolve multiplatform Gradle/runtime errors, and structure modern UI cards).
- **Impact:** AI assistance drastically reduced debugging time for cross-platform environment issues, ensured adherence to Material 3 design guidelines, and enabled the successful delivery of a responsive multiplatform application within the tight deadline.
- **Chat Transcript:** Collaborative session spanning KMP state management, desktop build troubleshooting, and UI refinement.
- **Chat Link:** https://share.gemini.google/20Xeyrzs4LUD
