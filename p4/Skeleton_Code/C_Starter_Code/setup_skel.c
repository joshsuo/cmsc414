#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <assert.h>

#define DEBUG

/* test whether the file exists */
int file_test(char* filename) {
  struct stat buffer;
  return (stat(filename, &buffer) == 0);
}

int main(int argc, char** argv) {
  FILE *fp;
  char *key = NULL;

  if (argc < 2) {
    printf("Usage: setup <logfile pathname>\n");
    return(255);
  }

  fp = fopen(argv[1], "w");
  if (fp == NULL){
#ifdef DEBUG
    printf("setup: fopen() error could not create file\n");
#endif
    printf("invalid\n");
    return(255);
  }

#ifdef DEBUG
  if (file_test(argv[1]))
    printf("created file named %s\n", argv[1]);
#endif

/* add your code here */

  fclose(fp);

  printf("Key is: %s\n", key);

  return(0);

}
