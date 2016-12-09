#include "SoundHelpers.h"
#include "SoundWrapper.h"

void *create_sound(const char *filename) {
    return new SoundWrapper(filename);
}

void play_sound(void *sound_object) {
    SoundWrapper *sound_object_conv = (SoundWrapper *) sound_object;
    sound_object_conv->play();
}
