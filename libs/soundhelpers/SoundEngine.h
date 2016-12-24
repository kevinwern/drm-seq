#ifndef SOUNDENGINE_H
#define SOUNDENGINE_H

#include <RtAudio.h>
#include <sndfile.hh>
#include <map>
#include <string>

#define SAMPLE_RATE 44100
#define BUFFER_FRAMES 1024
#define BIT_MIN -32768
#define BIT_MAX 32767

class SoundWrapper;

class SoundEngine {
    public:
        static SoundEngine *GetInstance();
        unsigned int read(int16_t *buffer, unsigned int size);
        void add(SoundWrapper *sound, int id);
        bool QueueEmpty();

    private:
        SoundEngine();
        void ProcessQueue();

        RtAudio audio;
        std::map<unsigned int, SoundWrapper *> activeSounds;
        std::map<unsigned int, SoundWrapper *> queuedSounds;
        static SoundEngine *instance;
};

class SoundWrapper {

    public:
        static unsigned int GetId();
        SoundWrapper(std::string filename);
        void play();
        sf_count_t read(int16_t *buffer, unsigned int size);
        sf_count_t seek(sf_count_t frames, int whence);

    private:
        SndfileHandle fileHandle;
        SoundEngine* engine;
        unsigned int id;
        static unsigned int id_iterator;
};

#endif
