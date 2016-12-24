#include "SoundEngine.h"
#include <string>

unsigned int SoundWrapper::id_iterator = 0;

unsigned int SoundWrapper::GetId() {
    return id_iterator++;
}

SoundWrapper::SoundWrapper(std::string filename) {
    fileHandle = SndfileHandle(filename);
    engine = SoundEngine::GetInstance();
    id = GetId();
}

void SoundWrapper::play() {
   engine->add(this, id);
}

sf_count_t SoundWrapper::read(int16_t *buffer, unsigned int size) {
   return fileHandle.readf(buffer, size);
}

sf_count_t SoundWrapper::seek(sf_count_t frames, int whence) {
   return fileHandle.seek(frames, whence);
}
