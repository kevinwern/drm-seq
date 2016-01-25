JFLAGS = -g
JC = javac
JVM = java

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Sequencer.java \
	Metronome.java \
	MetronomeListener.java \
	Preset.java \
	Presets.java \
	Cell.java \
	FileMenu.java \
	Row.java \
	ImageButton.java \
	Staff.java \
	Sound.java

MAIN = Sequencer

all: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class
