# Appsolute Chaos

An immersive XR spatial game built with Android XR and Jetpack Compose. Navigate through chaotic 3D environments, solve puzzles, and test your spatial awareness across multiple challenging levels.

## 🎮 Game Overview

**Appsolute Chaos** is a two-level XR experience that challenges players with spatial navigation, timing, and audio-visual puzzles in a fully immersive 3D environment.

### ✨ Features

- **Immersive XR Experience**: Full 3D spatial interface using Android XR
- **Dynamic 3D UI**: Floating panels and interactive elements positioned in 3D space
- **Spatial Audio Cues**: 3D positional audio guides and challenges
- **Adaptive Difficulty**: Progressive challenge scaling with penalties
- **Comprehensive Scoring**: Time-based scoring with accuracy bonuses

## 🕹️ How to Play

### Main Menu
- Look around to see level descriptions and instructions on floating panels
- Select **LEVEL 1** or **LEVEL 2** to begin
- Use the **EXIT GAME** button to return to the main interface

### Level Descriptions

#### Level 1: Floating Button Hunt

Randomized floating UI modals positioned in 3D space around the user. Only one button will progress to the next level. 

**Mechanics:**
- 🔄 Timer randomizes all button positions every 3 seconds
- ❌ Incorrect button press increases repositioning time by 0.25 seconds  
- 🏆 Final score based on loops survived, total time, and accuracy
- 🎨 Buttons have different shapes (circle, square, rounded rectangle) and colors
- 💡 Hint modal rotates slowly around the user's head showing current stats

**Strategy:**
- Look for the ✓ symbol on the correct button
- Speed matters, but accuracy is more important
- Pay attention to the repositioning timer

#### Level 2: Audio Keypad Challenge

3D UI sounds trigger away from the user's current orientation. When looking toward the sound, a UI modal spawns with a keypad interface.

**Mechanics:**
- 🔊 3D audio cues trigger keypad spawning (look toward the sound!)
- 🔢 Listen for additional sound triggers that spawn number hint windows
- ⏱️ Number hints last only 1.5 seconds each
- 🌅 Keypad slowly fades away over 10 seconds if unused
- 🔄 Reset buttons spawn in different locations when fading begins
- 🎯 Enter the correct 6-digit code to complete the level

**Strategy:**
- Turn your head to follow audio cues
- Memorize number hints quickly (they disappear fast!)
- Find reset buttons to restore fading keypads
- Work efficiently - time pressure is real

## 🎯 Scoring System

- **Lower scores are better**
- **Time**: Base score from completion time
- **Loops**: +10 points penalty per loop in Level 1
- **Wrong Presses**: +25 points penalty per incorrect input
- **Completion Bonus**: Successfully finishing both levels

## 🛠️ Technical Implementation

### Architecture
- **Android XR**: Built on Android XR framework
- **Jetpack Compose**: Modern UI toolkit with spatial extensions
- **Kotlin**: 100% Kotlin implementation
- **Material Design 3**: Consistent design system adapted for XR

### XR Components Used
- `Subspace`: 3D spatial container
- `SpatialPanel`: Individual floating UI panels
- `SpatialCurvedRow`: Curved spatial arrangements
- Spatial positioning with 3D coordinates (x, y, z)
- Spatial audio cue simulation

### Key Features
- **Spatial UI Management**: Dynamic positioning of 3D interface elements
- **Animation System**: Smooth rotations, fading, and position transitions
- **Game State Management**: Comprehensive state handling across levels
- **Audio-Visual Feedback**: Coordinated sound and visual cues
- **Adaptive Timing**: Dynamic difficulty based on player performance

## 🚀 Getting Started

### Prerequisites
- Android Studio with XR development support
- Android XR compatible device or emulator
- Minimum SDK 34

### Build and Run
1. Clone the repository
2. Open in Android Studio
3. Build the project: `./gradlew assembleDebug`
4. Deploy to XR device or emulator
5. Launch **Appsolute Chaos**

### Controls
- **Head Movement**: Look around to navigate 3D space
- **Gaze + Tap**: Interact with buttons and UI elements
- **Spatial Awareness**: Use 360° environment for optimal gameplay

## 🎪 The Chaos Experience

**Appsolute Chaos** isn't just a game—it's a journey through controlled mayhem in virtual space. Every floating button, every audio cue, every fading interface element is designed to challenge your spatial reasoning while providing an engaging, immersive experience.

From the floating button hunt of Level 1 to the complex audio-keypad challenges of Level 2, players must adapt to an ever-changing 3D environment where quick thinking and spatial awareness are key to success.

**Are you ready for the chaos?** 🌪️

---

*Built with ❤️ using Android XR and Jetpack Compose* 