
# DRM-SEQ

Drum Machine/sampler (NOTE: currently being reworked for Mac compatibility).

## Requirements

Oracle java 1.7 or above: This WILL NOT WORK with OpenJDK (throws LineUnavailableException when opening sound input stream).

## Overview

DRM-SEQ is a live drum sequencer/sampler capable of creating loops with small
WAV samples.  Any WAV file can be imported to the program. It has 8 "banks" capable
of storing a unique instance of a loop.

Compilation:
$ make

Execution:
$ make run

Clean:
$ make clean

## Controls

Create a row by selecting a sound using the drop down menu. ("Load Sound" button)
Click any of the cells to insert an instance of the sound there. Universally, you control:

BPM: beats per minute
Time Signature: feeling of the beat/beats per measure
Groove: the "swing" of the notes.  The number is a percentage representing the amount of *actual* time given to the first 8th note of an entire quarter note

**Other assorted info:**

Solo/mute: If soloed, the part plays only by itself or with other soloed parts.
    Muted is self explanatory.

Deleting rows: right-click on a row to delete it

Adding sounds of your own: simply add your wav files to the "Samples" folder.

Beatmatching: Space to cue the track to the beginning, +/- keys (or the arrow buttons)
    to change the BPM
