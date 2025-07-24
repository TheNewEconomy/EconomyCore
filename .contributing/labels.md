# 🏷️ Repository Labels Guide

This markdown document defines and organizes the labels used in this repository. Labels help triage, filter, and communicate the purpose and status of issues and pull requests.

---

## 📦 Scope Labels
These indicate which part of the codebase or system is affected.

| Label Name           | Description                                             | Example                              |
|----------------------|---------------------------------------------------------|--------------------------------------|
| `scope: commands`    | Command logic and registration                          | Adding `/f help`                     |
| `scope: events`      | Bukkit/Spigot event handlers                            | Handling `BlockBreakEvent`          |
| `scope: permissions` | Permissions and role enforcement                        | Setting up `plugin.use.fly`         |
| `scope: config`      | Configuration options and reload support                | Adding toggle for friendly fire     |
| `scope: economy`     | Economy logic, Vault integration                        | Charging for teleporting            |
| `scope: integrations`| Plugin interoperability                                 | Supporting WorldGuard               |
| `scope: gui`         | Inventory menus and UI elements                         | Creating a faction GUI              |
| `scope: api`         | Public API or exposed interface                         | Adding `Faction#getHome()`          |
| `scope: player-side` | Player-facing features                                  | Adding sounds to events             |
| `scope: operator`    | Admin tools or commands                                 | Adding `/plugin reload`             |
| `scope: db`          | Database or storage implementation                      | Switching from YAML to MySQL        |
| `scope: docs`        | Documentation changes                                   | Updating wiki                       |

---

## ⚙️ Type Labels
These describe the nature of the issue or contribution.

| Label Name             | Description                                 | Example                              |
|------------------------|---------------------------------------------|--------------------------------------|
| `type: bug`            | Something isn't working                     | Null pointer on login                |
| `type: enhancement`    | Improvements to existing features           | Speeding up claims                   |
| `type: feature`        | New functionality                           | Adding land borders                  |
| `type: discussion`     | Start of a conversation                     | Proposal for a feature               |
| `type: documentation`  | Docs-only changes                           | Updating README                      |
| `type: refactor`       | Code restructuring                          | Cleaning up utility classes          |
| `type: test`           | Adding or improving tests                   | Unit test coverage                   |
| `type: performance`    | Performance improvements                    | Reducing lag during teleport         |
| `type: security`       | Security fixes or upgrades                  | Fixing permission escalation         |
| `type: style`          | Code style or formatting                    | Applying code formatter              |

---

## 🚦 Status Labels
Track progress and decision-making for issues and PRs.

| Label Name                | Description                             | Example                              |
|---------------------------|-----------------------------------------|--------------------------------------|
| `status: backlog`         | Not started yet                         | Logged feature request               |
| `status: in progress`     | Work is underway                        | Implementing claim preview           |
| `status: blocked`         | Cannot proceed yet                      | Waiting on upstream dependency       |
| `status: needs review`    | Awaiting code or peer review            | Open PR ready for feedback           |
| `status: needs testing`   | QA or manual verification needed        | Testing changes after merge          |
| `status: ready to merge`  | Finished and approved                   | Awaiting final merge                 |
| `status: duplicate`       | Already tracked                         | Duplicate of another issue           |
| `status: won't fix`       | Wont be fixed intentionally             | Unsupported MC version               |
| `status: invalid`         | Invalid or incorrect issue              | User error or invalid feature        |

---

## ⏱️ Effort Labels
Estimate the time or complexity of resolving an issue.

| Label Name            | Description                      | Example                           |
|-----------------------|----------------------------------|-----------------------------------|
| `effort: trivial`     | Less than 5 minutes              | Fix typo                          |
| `effort: low`         | Around 1 hour                    | Add a config toggle               |
| `effort: medium`      | A few hours                      | Create a GUI                      |
| `effort: high`        | Several hours of work            | Rewrite claim logic               |
| `effort: very high`   | Days of work                     | Add database layer                |
| `effort: unknown`     | Needs investigation              | Vague bug report                  |

---

## 📟 Platform Labels
Labels that indicate the platform(s) the issue or feature applies to.

| Label Name              | Description                    | Example                          |
|-------------------------|--------------------------------|----------------------------------|
| `platform: spigot`      | For Spigot-specific logic       | Legacy method support            |
| `platform: paper`       | For Paper features              | Async chunk loading              |
| `platform: sponge`      | For Sponge implementations      | Sponge API usage                 |
| `platform: hybrid`      | Shared/multi-platform code      | Universal plugin logic           |
| `platform: fabric`      | Fabric-related code             | Mixin usage                      |
| `platform: neoforge`    | NeoForge support                | Forge hooks                      |

---

## 🧱 Minecraft Version Labels
Specify which Minecraft version an issue, feature, or support path targets.

| Label Name         | Description                          |
|--------------------|--------------------------------------|
| `mc: 1.8`          | Supports Minecraft 1.8               |
| `mc: 1.9`          | Supports Minecraft 1.9               |
| `mc: 1.10`         | Supports Minecraft 1.10              |
| `mc: 1.11`         | Supports Minecraft 1.11              |
| `mc: 1.12`         | Supports Minecraft 1.12              |
| `mc: 1.13`         | Supports Minecraft 1.13              |
| `mc: 1.14`         | Supports Minecraft 1.14              |
| `mc: 1.15`         | Supports Minecraft 1.15              |
| `mc: 1.16`         | Supports Minecraft 1.16              |
| `mc: 1.17`         | Supports Minecraft 1.17              |
| `mc: 1.18`         | Supports Minecraft 1.18              |
| `mc: 1.19`         | Supports Minecraft 1.19              |
| `mc: 1.20`         | Supports Minecraft 1.20              |
| `mc: 1.21.1`       | Supports Minecraft 1.21.1            |
| `mc: 1.21.2`       | Supports Minecraft 1.21.2            |
| `mc: 1.21.3`       | Supports Minecraft 1.21.3            |
| `mc: 1.21.4`       | Supports Minecraft 1.21.4            |
| `mc: 1.21.5`       | Supports Minecraft 1.21.5            |

---

## 🔨 Work Complexity Labels
Label the cognitive or structural difficulty of the issue or task.

| Label Name           | Description                                     | Example                                |
|----------------------|-------------------------------------------------|----------------------------------------|
| `work: obvious`      | Straightforward fix or task                    | Config typo                            |
| `work: complicated`  | Requires some domain knowledge                 | Designing endpoints                    |
| `work: complex`      | May involve iteration and planning             | New game mechanic                      |
| `work: chaotic`      | Emergency or undefined issue                   | Prod crash                             |
| `work: disorder`     | Unclear and lacks info                         | Poorly defined bug report              |

---

## 🚨 Priority Labels
Helps prioritize tasks during sprints or planning.

| Label Name                        | Description                          | Example                          |
|----------------------------------|--------------------------------------|----------------------------------|
| `priority: must-have`            | Critical for release or core feature | Core login system                |
| `priority: should-have`          | High value, but not blocker          | Admin filters                    |
| `priority: could-have`           | Nice to have                         | Dark mode                        |
| `priority: won't-have (this time)`| Not included in this cycle           | Mobile support                   |

---

## 📝 Usage Guidelines
- **Assign at least one `scope` and one `type` label.**
- **Use `status` labels to track progress.**
- **Use `platform` and `mc` labels for compatibility tracking.**
- **Use `effort`, `work`, and `priority` labels for planning and estimation.**