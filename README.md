<div align="center">

# 🪄 Moodle AI Summarizer

<a id="moodle-ai"></a>


[![Last Commit](https://img.shields.io/github/last-commit/guilhermegarrote/moodle-ai-summarizer?style=flat-square)](https://github.com/guilhermegarrote/moodle-ai-summarizer/commits)
[![Top Language](https://img.shields.io/github/languages/top/guilhermegarrote/moodle-ai-summarizer?style=flat-square)](https://github.com/guilhermegarrote/moodle-ai-summarizer)
[![Language Count](https://img.shields.io/github/languages/count/guilhermegarrote/moodle-ai-summarizer?style=flat-square)](https://github.com/guilhermegarrote/moodle-ai-summarizer)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square\&logo=openjdk\&logoColor=white)](https://www.java.com/)
[![Jsoup](https://img.shields.io/badge/Jsoup-Web%20Scraping-4CAF50?style=flat-square)](https://jsoup.org/)
[![Gemini API](https://img.shields.io/badge/Google%20Gemini-AI-blue?style=flat-square\&logo=google)](https://ai.google.dev/)
[![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat-square\&logo=apachemaven)](https://maven.apache.org/)

</div>

---

## 📑 Table of Contents

* [🌟 Overview](#-overview)
* [🔧 Features](#-features)
* [⚙️ Architecture](#-architecture)
* [⚡ Installation](#-installation)
* [🚀 Usage](#-usage)
* [📄 Output](#-output)
* [🧠 AI Layer](#-ai-layer)
* [📌 Notes](#-notes)
* [📄 License](#-license)

---

## 🌟 Overview

Moodle AI Summarizer is a Java-based automation system that extracts academic content from Moodle courses and generates structured summaries using artificial intelligence (Google Gemini).

It transforms long, fragmented course materials into clean, organized, and easy-to-review study notes, optimizing the learning and revision process.

<div align="center"> <img src="src/main/resources/demo/demo.gif" width="700" alt="Moodle AI Summarizer demo"> </div>

---

## 🔧 Features

* 🔐 Automatic Moodle login with session handling
* 🌐 Course navigation and unit detection
* 📄 Content extraction from course pages
* ⚡ Parallel processing for improved performance
* 🧠 AI-powered summarization using Google Gemini
* 📦 Markdown generation for final output
* 🧩 Prompt-based customization system

---

## ⚙️ Architecture

The system is divided into three main layers:

### 1. Moodle Client

* Handles authentication
* Manages cookies/session
* Performs authenticated requests

### 2. Scraping Engine

* Extracts course structure
* Collects unit links
* Downloads page content

### 3. AI Processing Layer

* Sends content to Gemini API
* Generates summaries per unit
* Aggregates final course output

---

## ⚡ Installation

### 1. Clone the repository

```bash
git clone https://github.com/guilhermegarrote/moodle-ai-summarizer.git
cd moodle-ai-summarizer
```

---

### 2. Configure environment variables

```bash
MOODLE_USERNAME=your_user
MOODLE_PASSWORD=your_password
MOODLE_URL=https://your-moodle.com
MOODLE_COURSE_ID=393
GEMINI_MODEL=gemini-model-name
GOOGLE_API_KEY=your_api_key
```

---

### 3. Build project

```bash
mvn clean install
```

---

## 🚀 Usage

Run the application:

```bash
mvn exec:java
```

Or execute via IDE (IntelliJ / Eclipse).

---

## 📄 Output

After execution, the system generates:

```
course-content.md
```

Containing:

* Structured summaries per unit
* Cleaned and optimized study material
* AI-generated explanations

---

## 🧠 AI Layer

The project integrates **Google Gemini API** to:

* Summarize long academic texts
* Remove redundancy
* Improve readability
* Structure information for study purposes

---

## 📌 Notes

* The system depends on Moodle HTML structure
* Changes in Moodle layout may require adjustments
* API usage may incur costs depending on volume
* Designed for educational and personal use

---

## 📄 License

This project is for educational purposes only.

---

<div align="center">

[⬆ Back to top](#moodle-ai)

</div>

