# Fetch Rewards - Coding Exercise
Author: Siddhant Sharma  

This repository contains a native Android app written in Java that retrieves data from the provided URL https://fetch-hiring.s3.amazonaws.com/hiring.json and displays it to the user in an easy-to-read list, adhering to the specified requirements:
- Display all the items grouped by "listId"
- Sort the results first by "listId" then by "name" when displaying.
- Filter out any items where "name" is blank or null.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)

## Requirements

To run this app, ensure that you have the following:

- Android Studio (latest stable version) with Android SDK 33 installed
- An Android device/emulator running Android 13 'Tiramisu'

## Installation

1. Clone this repository to your local machine using:

   ```bash
   git clone https://github.com/SiddhantGSharma/fetch-repo.git
   ```
2. Open Android Studio and select "Open an existing Android Studio project."
3. Navigate to the cloned repository and select the root folder of the project.
4. Android Studio will sync the project and install any necessary dependencies.

## Usage

1. Run the app on your Android device/emulator by clicking the "Run" button in Android Studio.
2. The app will fetch the data from the provided URL https://fetch-hiring.s3.amazonaws.com/hiring.json.
3. The list of items will be displayed, grouped by "listId" and sorted first by "listId" and then by "name."
4. Any items where "name" is blank or null will be filtered out, and the remaining items will be shown in an easy-to-read list.

## Technologies Used

- Java
- Android Studio

