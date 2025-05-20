#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <wordexp.h>

#include "data.h"

typedef struct _CmdLineResult {
  //TODO probably put more things here
  int     good;
} CmdLineResult;

int do_batch(char *);

CmdLineResult parse_cmdline(int argc, char *argv[]) {
  CmdLineResult R = { 0 };
  int opt,r = -1;

  R.good = -1;

    if(argc==1)
        printf("\nNo Extra Command Line Argument Passed Other Than Program Name");
    if(argc>=2)
    {
        printf("\nNumber Of Arguments Passed: %d",argc);
        printf("\n----Following Are The Command Line Arguments Passed----");
        for(counter=0;counter<argc;counter++)
          printf("\nargv[%d]: %s",counter,argv[counter]);
          // Decide what is the setting we are in
    }

  //TODO do stuff
  if(file != NULL) {
    R.good = do_something(file);
  } else {
    //TODO do stuff
    R.good = 0;
  }

  return R;
}

int main(int argc, char *argv[]) {
  int r;
  CmdLineResult R;

  R = parse_cmdline(argc, argv);

  if(R.good == 0) {
    Buffer  B = read_from_path(/** stuff **/);

    //TODO do things here.

    //write the result back out to the file
    write_to_path(/** stuff **/);
  }

  return r;
}
