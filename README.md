# UPBH - Unlimited PDF Book House

[![Google Play](https://img.shields.io/badge/Google%20Play-Download-green?style=flat-square&logo=google-play)](https://play.google.com/store/apps/details?id=com.techtravelcoder.educationalbooks)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen?style=flat-square&logo=android)](https://android.com)
[![Language](https://img.shields.io/badge/Language-Java-orange?style=flat-square&logo=java)](https://www.java.com)

> A comprehensive digital library application providing access to thousands of high-quality PDF eBooks across diverse academic and professional categories.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

UPBH (Unlimited PDF Book House) is a mobile application designed to provide students, professionals, and avid readers with seamless access to a vast collection of educational and professional PDF books. The app features an intuitive interface, advanced search capabilities, and offline reading functionality.

### Key Statistics
- **100+** book categories
- **Daily updates** with new content
- **Offline reading** capability
- **Advanced search** functionality

## Features

### 📚 **Content Library**
- Extensive collection spanning Science, Arts, Commerce, and Engineering
- Academic materials for HSC, BSc, MSc, and university departments
- Specialized content for IELTS, GRE, LLB, and professional certifications
- Daily content updates with new releases

### 🔍 **Search & Discovery**
- Advanced search engine with filtering and sorting
- Category-based browsing across 100+ subjects
- Personalized recommendations
- Book request system for missing titles

### 📱 **User Experience**
- **Reading Modes**: Dark/Light theme support
- **Layout Options**: Horizontal and vertical reading orientations
- **Offline Access**: Download books for offline reading
- **Bookmarking**: Save and organize favorite books
- **Note-taking**: In-app annotation system
- **Sharing**: Social sharing capabilities

### 🎨 **Interface**
- PDF reflow for mobile optimization
- Smooth page-turning animations
- Responsive design for various screen sizes
- Intuitive navigation and user controls

## Screenshots

<div align="center">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-45-11-18_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Home Screen">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-45-19-60_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Book Categories">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-45-28-49_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Book Library">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-46-15-33_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Search Interface">
</div>

<div align="center">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-46-25-53_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Book Details">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-46-36-72_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="PDF Reader">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-48-36-55_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Reading Mode">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-48-58-85_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Bookmarks">
</div>

<div align="center">
  <img src="https://github.com/RlM100always/Hisab/blob/main/EducationalBook/Screenshot/Screenshot_2025-01-24-19-49-04-09_3a24a73f6a9ac5d7365b7e1fde02371c.jpg" width="200" alt="Settings">
</div>

## Installation

### Prerequisites
- Android 5.0 (API level 21) or higher
- 50MB free storage space
- Internet connection for downloading books

### Download Options

#### Google Play Store
[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.techtravelcoder.educationalbooks)

#### Manual Installation
1. Download the APK from [Releases](https://github.com/RlM100always/UPBH/releases)
2. Enable "Unknown Sources" in Android settings
3. Install the downloaded APK file

## Technology Stack

| Component | Technology |
|-----------|------------|
| **Frontend** | Java, XML |
| **Backend** | Firebase |
| **Database** | SQLite, SharedPreferences |
| **Platform** | Android SDK |


### Dependencies
- Firebase SDK for backend services
- SQLite for local data storage
- Material Design Components
- PDF rendering libraries

## Architecture

```
app/
├── src/main/java/com/techtravelcoder/educationalbooks/
│   ├── activities/          # Activity classes
│   ├── adapters/           # RecyclerView adapters
│   ├── fragments/          # Fragment classes
│   ├── models/             # Data models
│   ├── services/           # Background services
│   ├── utils/              # Utility classes
│   └── views/              # Custom views
├── src/main/res/
│   ├── layout/             # XML layouts
│   ├── values/             # Resources (strings, colors, etc.)
│   └── drawable/           # Images and icons
└── build.gradle           # Build configuration
```

## Contributing

We welcome contributions to improve UPBH! Please follow these guidelines:

### Getting Started
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request



## Roadmap

- [ ] android version development
- [ ] Enhanced search with AI recommendations
- [ ] Multi-language support
- [ ] Audio book integration
- [ ] Collaborative reading features

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

```
Copyright 2024 Md Rakib Hossain

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contact

**Md Rakib Hossain**  
<img src="https://avatars.githubusercontent.com/u/109984220?v=4" width="60" style="border-radius: 50%;" align="left">

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/rlm100always/)
[![Facebook](https://img.shields.io/badge/Facebook-Follow-blue?style=flat-square&logo=facebook)](https://www.facebook.com/rlm100always/)
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?style=flat-square&logo=github)](https://github.com/RlM100always)

---

<div align="center">
  <p>Made with ❤️ for the educational community</p>
  <p>⭐ Star this repository if you find it helpful!</p>
</div>
