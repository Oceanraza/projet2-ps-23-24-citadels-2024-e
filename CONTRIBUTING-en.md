# Contributing to Citadels

Welcome to Citadels!
[French Version](README.md)

To ensure a smooth and collaborative experience, please take a moment to review the contributing guidelines outlined below.

---

### Table of Contents
* [Code of Conduct](#code-of-conduct)
* [How to Contribute](#how-to-contribute)
  * [Asking Questions](#asking-questions)
  * [Creating an Issue](#creating-an-issue)
  * [Issue Lifecycle](#issue-lifecycle)
    * [Committing](#committing)
    * [Submitting a Pull Request](#submitting-a-pull-request)
    * [Participating in Reviews](#participating-in-reviews)
* [Source Code Style](#source-code-style)
* [Git Flow Branching Strategy](#git-flow-branching-strategy)

---

### Code of Conduct

Citadels operates under the guidance of Team E. As a contributor, you are expected to adhere to our code of conduct to maintain a professional environment.

---

### How to Contribute

#### Asking Questions

If you have inquiries regarding the project or its development, please don't hesitate to reach out through our communication channels.

#### Creating an Issue

Contributions begin by creating well-defined issues. Each issue should include a comprehensive description and appropriate labels to facilitate efficient categorization. Additionally, every issue should be linked to a milestone, which helps organize tasks within a designated timeframe. Our project boards provide an overview of all issues associated with a milestone.

#### Issue Lifecycle

Upon creation, an issue is marked as `To Do`, awaiting assignment to a team member. Once assigned, it transitions to `In Progress`. Upon completion, it progresses to a pull request stage, requiring review by at least one team member. The issue is marked as `Ready for review`. Following thorough discussion, comments, and approval, the pull request can be merged and the issue is considered `Done`. While closed, issues may be reopened if further adjustments are necessary, though typically, they remain closed once resolved.

##### Committing

Each commit must adhere to a conventional start (conventional commit).

##### Submitting a Pull Request

If your changes correspond to an existing issue, ensure to reference the relevant GitHub issue number in the pull request description.

##### Participating in Reviews

Reviews play a pivotal role in ensuring the integrity and quality of the codebase, thereby minimizing potential issues.

---

### Source Code Style

Our project upholds a rigorous coding standard in alignment with established industry practices and adheres to the guidelines provided by SonarQube. Code reviews are conducted to identify and rectify any code smells, ensuring the overall quality and maintainability of the codebase.

---

### Git Flow Branching Strategy

Our development process follows the Git Flow branching strategy which involves:

- **Master Branch:** Houses stable production versions.
- **Develop Branch:** Originating from master, this branch serves as the primary development branch. Feature branches are created from develop for individual issues.
- **Feature Branches:** Each issue is assigned a dedicated feature branch for development purposes. Upon completion, a pull request is initiated to merge the feature into develop.

Any push or pull request affecting master and develop triggers automated testing and build processes. Build execution involves `mvn exec:java`, while tests are conducted using `mvn clean package`.

This strategic approach ensures systematic development, rigorous testing, and seamless integration of features into the project.