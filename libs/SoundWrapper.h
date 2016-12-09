#include <map>
#include "RtAudio.h"
#include <sndfile.hh>
#include <iostream>
#include <cmath>

#define SAMPLE_RATE 44100
#define BUFFER_FRAMES 1024
#define BIT_MIN -32768
#define BIT_MAX 32767

int fplay(void *outputBuffer, void *inputBuffer, unsigned int nBufferFrames,
         double streamTime, RtAudioStreamStatus status, void *userData);

class SoundEngine {

  public:
      static SoundEngine *GetInstance() {
          if (instance == nullptr)
              instance = new SoundEngine();
          return instance;
      }

      unsigned int read(int16_t *buffer, unsigned int size) {
          int16_t addBuffer[size * 2];
          int count, maxReturn = 0;
          std::map<unsigned int, SndfileHandle *>::iterator it;
          memset(buffer, 0, sizeof(int16_t) * size * 2);
          it = activeFiles.begin();
          while (it != activeFiles.end()) {
              count = it->second->readf(addBuffer, size);
              for (int i = 0; i < count; i++) {
                  buffer[2 * i] = buffer[2 * i] + addBuffer[i];
                  buffer[2 * i + 1] = buffer[2 * i + 1] + addBuffer[i];
              }
              if (count > maxReturn) {
                  maxReturn = count;
              }
              if (count < size) {
                  it->second->seek(0, SEEK_SET);
                  it = activeFiles.erase(it);
              }
              else {
                  it++;
              }
          }
          return maxReturn;
      }

      void add(SndfileHandle *file, int id) {
          if (activeFiles.find(id) != activeFiles.end()) {
              activeFiles[id]->seek(0, SEEK_SET);
          }
          else {
              activeFiles[id] = file;
          }
          if (!audio.isStreamRunning()) {
             audio.setStreamTime(0);
             audio.startStream(); 
          }
      }

  private:
      SoundEngine() {
          unsigned int bufferFrames = BUFFER_FRAMES;
          RtAudio::StreamParameters parameters;
          parameters.deviceId = audio.getDefaultOutputDevice();
          parameters.nChannels = 2;
          parameters.firstChannel = 0;
          audio.openStream(&parameters, NULL, RTAUDIO_SINT16, SAMPLE_RATE,
                           &bufferFrames, &fplay, (void *)this);
      }

      RtAudio audio;
      std::map<unsigned int, SndfileHandle *> activeFiles;
      static SoundEngine *instance;
};

SoundEngine *SoundEngine::instance = nullptr;

class SoundWrapper {

    public:
        static unsigned int GetId() {
          return id_iterator++;
        }

        SoundWrapper(std::string filename) {
            fileHandle = SndfileHandle(filename);
            engine = SoundEngine::GetInstance();
            id = GetId();
        }

        void play() {
           engine->add(&fileHandle, id);
        }

    private:
        SndfileHandle fileHandle;
        SoundEngine* engine;
        unsigned int id;
        static unsigned int id_iterator;
};

unsigned int SoundWrapper::id_iterator = 0;

int fplay(void *outputBuffer, void *inputBuffer, unsigned int nBufferFrames,
         double streamTime, RtAudioStreamStatus status, void *userData) {
  int16_t *buffer = (int16_t *) outputBuffer;
  SoundEngine *sndfile = reinterpret_cast<SoundEngine*>(userData);

  if ( status ){
    std::cout << "Stream underflow detected!" << std::endl;
  }

  unsigned int count = sndfile->read(buffer, nBufferFrames);

  if (count < nBufferFrames) {
      return 1;
  }

  return 0;
}

