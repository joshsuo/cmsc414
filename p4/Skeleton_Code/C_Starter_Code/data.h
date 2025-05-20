#ifndef _BUFFR_H
#define _BUFFR_H

typedef struct _Buffer {
  unsigned char *Buf;
  unsigned long Length;
} Buffer;

typedef enum _ActionType {
  add_assignment,
  delete_assignment,
  add_student,
  delete_student,
  add_grade
} ActionType;

typedef struct _Gradebook {
  //put some things here
} Gradebook;

typedef struct _Assignment {
  //put some things here
} Assignment;


Buffer read_from_path(char *path, unsigned char *key);
void write_to_path(char *path, Buffer *B, unsigned char *key);
Buffer concat_buffs(Buffer *A, Buffer *B);
Buffer print_record(Gradebook *R);
void dump_record(Gradebook *R);

int read_records_from_path(char *path, unsigned char *key, Gradebook **, unsigned int *);

#endif
