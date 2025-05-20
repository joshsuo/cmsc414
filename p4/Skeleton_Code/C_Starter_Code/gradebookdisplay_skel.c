#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/queue.h>

#include "data.h"

int verbose = 0;

void print_Gradebook(Gradebook *gradebook) {
  unsigned int i;

  for(i = 0; i < num_assigment; i++) {
    dump_assignment(&gradebook[i]);
    printf("----------------\n");
  }

  return;
}

struct Student {
  //TODO put some other stuff here
  SLIST_ENTRY(Student)   link;
};

SLIST_HEAD(slisthead, Student);

void print_Assignment(Assignment *assignment) {

  return;
}

void print_Student(struct slisthead *head, ...) {

  return;
}

void print_Final(Gradebook *gradebook){

  return;
}


int main(int argc, char *argv[]) {
  int   opt,len;
  char  *logpath = NULL;

  //TODO Code this
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

}
