# DRM-SEQ

![Screenshot](examples/v1.png?raw=true "Screen Shot")

Drum Machine/sampler created with Java and C++. Originally a project for
my off time, the goal is to eventually integrate this with input so a
person can map audio input (i.e. beatboxing) to real samples, and
quickly create loops or improvise during a show.

## Requirements

[libsndfile, with C++ wrappers (version 1.0.17 or later)](http://www.mega-nerd.com/libsndfile/)

[rtaudio](https://www.music.mcgill.ca/~gary/rtaudio/)

java >= 1.6 (with JNI)

## Overview

DRM-SEQ is a live drum sequencer/sampler capable of creating loops with small
WAV samples.  Any WAV file can be imported to the program. It has 8 "banks"
capable of storing a unique instance of a loop.

This project was created with IntelliJ, and all the metadata is intended for
that IDE.

## Setup before compilation

1) Install the aforementioned libraries.

2) If the libraries are not in a default location on your system, establish
   the following environment variables:

   LDFLAGS: The search location for your libraries binaries.

   CFLAGS: The search location for C includes (in this case, JNI).

   CXXFLAGS: The search location for C++ includes.

## Build

### Project

Build this project using gradle:

Compilation:

    gradle build

(if configuration fails, it is likely related to an error with the above)

Execution:

    gradle jar

    java -jar drm-seq.jar

Clean:

     gradle clean

### C++ Helpers

The library helpers are located in libs/soundhelpers. To attempt to build only
the helpers, you can run something like:

    cd libs/soundhelpers && ./configure && make

This done by default in `gradle build`. To clean the helpers, run:

    make clean

## Usage

Create a row by selecting a sound in the file tree. Click any of the cells to play the sound at that point.

You have 8 banks, which you can populate with sounds. Click the bank you want to show. Select a bank for play by pressing the corresponding number key.

The click is universal, meaning that the position does not change when switching between banks. This is intentional, to make transitioning between loops easier.

For all banks, you control the following parameters:

Parameter| Possible Values |Description
---|---|---
BPM | [1,240] |Beats per minute
Time Signature | [1,12]/(4\|8)|The number of beats per measure (beat count), and the subdivision (value of each beat).
Groove | [0,100] |The "swing factor" of the notes. Above 50% gives more time to notes on the beat; below 50% gives more time to notes off the beat.

For each row:

Button | Action
---|---
Mute | Don't play row.
Solo | Mute all other unsoloed tracks.

Additional hotkeys:

Key | Action
---|---
Right click | Delete row.
Space | Play from beginning.
+/- | Change the BPM.
[1-8] | Select bank for play.
