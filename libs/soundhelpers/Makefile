LDFLAGS=-L/opt/local/lib -L/Users/kwern/work/drm-seq/libs/rtaudio/lib
LDLIBS=-lsndfile -lrtaudio -shared
CXXFLAGS=-I/Users/kwern/work/drm-seq/libs/rtaudio/include/rtaudio/ -I/opt/local/include/
CFLAGS=-I/Users/kwern/work/drm-seq/libs/rtaudio/include/rtaudio/ -I/opt/local/include/ -I /Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home/include/ -I/Library/Java/JavaVirtualMachines/jdk1.8.0_111.jdk/Contents/Home/include/darwin

CFILES=$(wildcard *.c)
CXXFILES=$(wildcard *.cpp)
COBJECTS=$(patsubst %.c, %.o, $(CFILES))
CXXOBJECTS=$(patsubst %.cpp, %.o, $(CXXFILES))
LIBFILE=libsound.dylib

all: $(LIBFILE)

$(LIBFILE): $(CXXOBJECTS) $(COBJECTS)
	$(CXX) $(CFLAGS) -shared -o $(LIBFILE) $(CXXOBJECTS) $(COBJECTS) $(LDFLAGS) $(LDLIBS)

$(COBJECTS): %.o: %.c
	$(CXX) $(CFLAGS) -c $< -o $@

$(CXXOBJECTS): %.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(COBJECTS) $(CXXOBJECTS) $(LIBFILE)
