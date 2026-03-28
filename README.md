# Datediff

An Android app that lets you compare the capture date of a photo to one of 3 preconfigured reference dates — directly from the Android gallery's Share menu.

## Overview

Datediff integrates with the Android share sheet. When viewing any photo in the gallery, tap **Share** and select **Datediff**. The app reads the photo's EXIF capture date and lets you pick one of your 3 configured reference dates to compare against. The result shows how many days before or after the reference date the photo was taken.

## Features

- Configure 3 independent reference dates (stored on device)
- Appears as a share target when viewing photos in the gallery
- Reads capture date from EXIF metadata (`DateTimeOriginal`)
- Displays the difference in days, with direction (before / after / same day)
- Works entirely offline, no account or internet required

## Requirements

- Android 8.0 (API level 26) or higher
- Permission to read media files / photo library

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/fchaumeil/Datediff.git
   ```
2. Open the project in Android Studio.
3. Build and run on a device or emulator (API 26+).

## Usage

1. Launch the **Datediff** app and set your 3 reference dates using the date pickers.
2. Open any photo in your gallery app.
3. Tap **Share** → select **Datediff**.
4. Tap one of the 3 reference date buttons to see the day difference.

## License

This project is open source. See [LICENSE](LICENSE) for details.
