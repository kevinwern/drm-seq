#include "SoundHelpers.h"
#include "Sound.h"

JNIEXPORT jlong JNICALL Java_Sound_initCpp
  (JNIEnv *env, jobject obj, jstring filename) {
    const char *filename_char = env->GetStringUTFChars(filename, 0);
    void *return_obj = create_sound(filename_char);
    env->ReleaseStringUTFChars(filename, 0);
    return (jlong) return_obj;
}

JNIEXPORT void JNICALL Java_Sound_playCpp
  (JNIEnv *env, jobject obj, jlong int_address) {
    void *ptr = (void *) int_address;
    play_sound(ptr);
}
