#include "SoundEngine.h"
#include <iostream>

int fplay(void *outputBuffer, void *inputBuffer, unsigned int nBufferFrames,
          double streamTime, RtAudioStreamStatus status, void *userData) {
    int16_t *buffer = (int16_t *) outputBuffer;
    SoundEngine *sndfile = reinterpret_cast<SoundEngine*>(userData);
    unsigned int count;
  
    if (status) {
        std::cout << "Stream underflow detected!" << std::endl;
    }
  
    count = sndfile->read(buffer, nBufferFrames);
  
    return 0;
}

SoundEngine *SoundEngine::instance = nullptr;

SoundEngine *SoundEngine::GetInstance()
{
    if (instance == nullptr)
        instance = new SoundEngine();
    return instance;
}

unsigned int SoundEngine::read(int16_t *buffer, unsigned int size)
{
    int16_t addBuffer[size * 2];
    int count, maxReturn = 0;
    std::map<unsigned int, SoundWrapper *>::iterator it;
    memset(buffer, 0, sizeof(int16_t) * size * 2);
    ProcessQueue();
    it = activeSounds.begin();
    while (it != activeSounds.end()) {
        count = it->second->read(addBuffer, size);
        for (int i = 0; i < count; i++) {
            buffer[2 * i] = buffer[2 * i] + addBuffer[i];
            buffer[2 * i + 1] = buffer[2 * i + 1] + addBuffer[i];
        }
        if (count > maxReturn) {
            maxReturn = count;
        }
        if (count < size) {
            it->second->seek(0, SEEK_SET);
            it = activeSounds.erase(it);
        }
        else {
            it++;
        }
    }
    return maxReturn;
}

void SoundEngine::add(SoundWrapper *sound, int id)
{
    queuedSounds[id] = sound;

    if (!audio.isStreamRunning()) {
       audio.setStreamTime(0);
       audio.startStream(); 
    }
}

void SoundEngine::ProcessQueue()
{
    std::map<unsigned int, SoundWrapper *>::iterator it = queuedSounds.begin();
    while (it != queuedSounds.end()) {
        unsigned int id = it->first;
        if (activeSounds.find(id) != activeSounds.end()) {
            activeSounds[id]->seek(0, SEEK_SET);
        }
        else {
            activeSounds[id] = it->second;
        }
        it = queuedSounds.erase(it);
    }
}

bool SoundEngine::QueueEmpty()
{
    return queuedSounds.empty();
}

SoundEngine::SoundEngine()
{
    unsigned int bufferFrames = BUFFER_FRAMES;
    RtAudio::StreamParameters parameters;
    parameters.deviceId = audio.getDefaultOutputDevice();
    parameters.nChannels = 2;
    parameters.firstChannel = 0;
    audio.openStream(&parameters, NULL, RTAUDIO_SINT16, SAMPLE_RATE,
                     &bufferFrames, &fplay, (void *)this);
}
