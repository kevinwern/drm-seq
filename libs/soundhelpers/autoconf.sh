AC_PREREQ([2.69])
AC_INIT(libsoundhelpers, 0.0, kevin.m.wern@gmail.com)

AC_PROG_CC_C_O
AC_PROG_CPP

AC_LANG_PUSH([C++])
AC_CHECK_HEADERS([RtAudio.h])
AC_CHECK_LIB([rtaudio], [main])
AC_CHECK_HEADERS([sndfile.hh])
AC_CHECK_LIB([sndfile], [sf_open])
AC_LANG_POP([C++])
