
# DRM-SEQ

(NOTE: currently being reworked for Mac compatibility).

Drum Machine/sampler created with Java and C++. Originally a project for
my off time, the goal is to eventually integrate this with input so a
person can map audio input (i.e. beatboxing) to real samples, and
quickly create loops or improvise during a show.

## Requirements

libsndfile (with C++ wrappers)
rtaudio
java >= 1.6 (with JNI libraries)

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

$ gradle build (if configuration fails, it is likely related to an error
  with the above)

Execution:

$ gradle jar

$ java -jar drm-seq.jar

Clean:

$ gradle clean

### C++ Helpers

The library helpers are located in libs/soundhelpers. To attempt to build only
the helpers, you can run something like:

$ cd libs/soundhelpers && ./configure && make

This done by default in `gradle build`. To clean the helpers, run:

$ make clean

## Usage

### Controls

Create a row by selecting a sound using the drop down menu. ("Load Sound" button)
Click any of the cells to insert an instance of the sound there. Universally, you control:

BPM: beats per minute
Time Signature: feeling of the beat/beats per measure
Groove: the "swing factor" of the notes. Above 50% gives more time to notes on
        the beat; below 50% gives more time to notes off the beat.

### Misc features

Solo/mute: If soloed, the part plays only by itself or with other soloed parts.
    Muted is self explanatory.

Deleting rows: right-click on a row to delete it

Adding sounds of your own: add your wav files to the "Samples" folder.

Beatmatching: Space to cue the track to the beginning, +/- keys (or the arrow buttons)
    to change the BPM
