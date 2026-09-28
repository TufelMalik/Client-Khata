# 📱 TQD-Khata (Client CRM & Requirement Tracker)

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Room Database](https://img.shields.io/badge/Room-SQLite%20Offline-orange?logo=sqlite&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean%20Flow-blue)](https://developer.android.com/topic/architecture)

**TQD-Khata** is an offline-first, modern Client Relationship Management (CRM) and requirement tracking Android application built with **Jetpack Compose (Material 3)**. Tailored for agencies, independent consultants, contractors, and small business owners, TQD-Khata simplifies client onboarding, detailed requirement logging, project lifecycle monitoring, and follow-up scheduling.

---

## 🎨 Brand Design System

TQD-Khata is styled with a bespoke, warm, professional color palette:

| Color Role | Hex Code | Visual Reference | Purpose |
|:---|:---|:---|:---|
| **Primary Container** | `#F3E4C9` | `BrandCream` | Badges, avatar backgrounds, active highlights |
| **Secondary / Action** | `#0A2947` | `BrandNavy` | Primary buttons, headers, active chips, labels |
| **Outlines & Borders** | `#D3D4C0` | `BrandSage` | Card borders, dividers, chip outlines |
| **Accent / Priorities** | `#8B5E3C` | `BrandBronze` | Status accents, secondary highlights, icons |
| **Background** | `#FAF7F2` | `WarmBackground`| Eye-friendly warm surface background |

---

## ✨ Features & Capabilities

### 1. 📇 Unified Client Management (Add & Edit)
- **Single-Screen Architecture**: Seamlessly handles both new client creation and existing client updates via a unified navigation route (`client_form?clientId={clientId}`).
- **Rich Contact & Business Details**: Name, phone number, email, company name, address, and estimated budget.
- **Default City Pre-fill**: Automatically defaults the city field to `"Bharuch"` for rapid localized client entry.
- **Top-Aligned Requirement Input**: Multiline requirements text box with top-aligned icons for optimal ergonomics.
- **Responsive Priority Selector**: High, Medium, and Low priority chips that adapt cleanly to all device screen sizes.

### 2. 📊 Fluid, Scrollable Dashboard
- **Unified Scroll Hierarchy**: Header, KPI stats, search bar, active city filters, and status filters scroll smoothly together with client list cards in a single `LazyColumn`.
- **Single-Line Quick Search**: Compact 52dp search bar with instant query matching on client names, phone numbers, and company names.
- **City & Status Filters**: Filter clients by city with a one-tap **Clear (✕)** button, or filter by project lifecycle status chips.
- **Real-Time KPIs**: Live counters for Total Clients, Active Projects, Pending Reminders, and Converted/Delivered clients.

### 3. 🔄 Project Lifecycle & Pipeline Tracker
Track projects through 7 distinct stages with color-coded badges:
- 🔵 **Lead** (Initial inquiry)
- 🟡 **Meeting Scheduled** (Under discussion)
- 🟣 **Proposal Quoted** (Price & scope sent)
- 🟢 **In Progress** (Work underway)
- 🔷 **Delivered** (Project completed & handed over)
- ⚪ **On Hold** (Paused by client/agency)
- 🔴 **Cancelled** (Closed or dropped)

### 4. 📞 Instant One-Tap Communication
- **Direct WhatsApp Messaging**: Launches WhatsApp chat directly to the client's number (`wa.me/phone`) without intrusive pre-filled templates.
- **One-Tap Phone Calls**: Direct launch to system dialer.
- **Native Summary Sharing**: Share formatted client summaries and requirement notes to any messaging or note-taking app via Android's native share sheet.

### 5. ⏰ Smart Follow-up & Reminder System
- **Quick Scheduling Chips**: One-tap scheduling presets:
  - ⚡ *Today 5 PM*
  - ⚡ *Tomorrow 11 AM*
  - 📅 *Custom Date & Time* via native pickers.
- **Dedicated Reminders Screen**: Filter pending vs. completed tasks, mark reminders done with one click, or jump straight to the client's details.

### 6. 🖼️ Cohesive, Custom-Designed Dialogs
- **Status Update Dialog**: 24dp rounded dialog displaying all project stages with active selection indicators.
- **Add Reminder Dialog**: Clean topic and notes input with quick scheduling chips and schedule clearance.
- **Delete Confirmation Dialog**: Prominent danger badge and warning text to prevent accidental client removal.

### 7. 🔒 100% Offline-First & Private
- Fully powered by **Room SQLite Database** with reactive `Flow` observation.
- Zero tracking, zero cloud dependency, zero authentication hurdles—your business data never leaves your device.

---

## 🏗️ Architecture & Technology Stack

The project adheres to modern Android Architecture Components (Clean MVVM):

```
app/src/main/java/com/techquantum/tqdkhata/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Room DB declaration & migrations
│   │   ├── ClientDao.kt            # Reactive queries & search operations
│   │   ├── ReminderDao.kt          # Reminder queries & joins
│   │   └── Converters.kt          # Type converters (Enums, Dates)
│   ├── model/
│   │   ├── ClientEntity.kt         # Client database model
│   │   ├── ReminderEntity.kt       # Reminder database model
│   │   ├── ReminderWithClient.kt   # Relation query model
│   │   ├── Priority.kt             # High, Medium, Low
│   │   └── ProjectStatus.kt        # 7-stage project pipeline
│   └── repository/
│       └── ClientRepository.kt     # Single source of truth
├── ui/
│   ├── components/                 # Reusable Compose components
│   │   ├── ClientCard.kt           # Dashboard client list item
│   │   ├── PriorityBadge.kt        # Priority tag badge
│   │   ├── StatusBadge.kt          # Status badge
│   │   └── StatSummaryCard.kt      # KPI summary widget
│   ├── screens/                    # Jetpack Compose Screens
│   │   ├── ClientListScreen.kt     # Dashboard & search
│   │   ├── AddEditClientScreen.kt  # Unified Client form
│   │   ├── ClientDetailScreen.kt   # Details, actions & custom dialogs
│   │   └── RemindersScreen.kt      # Reminders hub
│   ├── theme/                      # Styling & Design Tokens
│   │   ├── Color.kt                # Brand palette (#F3E4C9, #0A2947, #D3D4C0, #8B5E3C)
│   │   ├── Theme.kt                # Material3 theme setup
│   │   └── Type.kt                 # Typography definitions
│   └── viewmodel/
│       ├── ClientViewModel.kt      # UI state management & search debounce
│       └── ClientViewModelFactory.kt
└── util/
    ├── DateUtils.kt                # Formatting & relative timestamp helpers
    └── IntentUtils.kt              # WhatsApp, dialer & share intents
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Android Studio Ladybug (2024.2.1) or newer
- **JDK**: Java 17 or Java 21
- **Android SDK**: Compile SDK `37`, Minimum SDK `24` (Android 7.0+)

### Building from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/techquantum/TQD-Khata.git
   cd TQD-Khata
   ```

2. **Open in Android Studio** or build using the Gradle wrapper:
   ```bash
   # On Windows PowerShell
   .\gradlew assembleDebug

   # On macOS / Linux
   ./gradlew assembleDebug
   ```

3. **Install Debug APK onto a connected device/emulator:**
   ```bash
   .\gradlew installDebug
   ```

---

## 📄 License

This project is licensed under the [Apache License 2.0](LICENSE).
Built with ❤️ by **TechQuantum** for entrepreneurs and businesses worldwide.