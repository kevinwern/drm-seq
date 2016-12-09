/* Sound.java: native interface for C helper functions */

class Sound {
    static {
        System.loadLibrary("sound");
    }

    public Sound(String fileName) {
        pointer_cpp = initCpp(fileName);
    }

    public void play() {
        playCpp(pointer_cpp);
    };

    public native long initCpp(String fileName);
    public native void playCpp(long pointer_cpp);

    private long pointer_cpp;
}
