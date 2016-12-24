LDLIBS=-lsndfile -lrtaudio

CFILES=$(wildcard *.c)
CXXFILES=$(wildcard *.cpp)
COBJECTS=$(patsubst %.c, %.o, $(CFILES))
CXXOBJECTS=$(patsubst %.cpp, %.o, $(CXXFILES))

ifeq ($(OS),Windows_NT)
        LIBFILE=soundhelpers.dll
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Linux)
        LIBFILE=libsoundhelpers.so
    endif
    ifeq ($(UNAME_S),Darwin)
        LIBFILE=libsoundhelpers.dylib
    endif
endif

all: $(LIBFILE)

$(LIBFILE): $(CXXOBJECTS) $(COBJECTS)
	$(CXX) $(CFLAGS) -shared -o $(LIBFILE) $(CXXOBJECTS) $(COBJECTS) $(LDFLAGS) $(LDLIBS)

$(COBJECTS): %.o: %.c
	$(CXX) $(CFLAGS) -c $< -o $@

$(CXXOBJECTS): %.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(COBJECTS) $(CXXOBJECTS) $(LIBFILE)
