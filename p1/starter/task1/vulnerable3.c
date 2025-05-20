/*
 * CMSC 414
 * Spring 2022
 * Homework 1 | task1
 *
 * Build instructions:
 *   We will be building this with the Makefile provided; you may not make
 *   any changes to the Makefile.
 *
 * Submission instructions:
 *   You may not make changes to this file; only to exploit3.c
 *   You do not need to submit this file.
 */

#include <stdio.h>
#include <stdlib.h>

/* this header exports functions to execute the exploit and read/write
 * to/from it */
#include "comms.h"

void sensitive_function(unsigned int dummy_arg, unsigned int arg)
{
    if (arg == 0x54455250) {
        puts("Full points! (Hey wait a minute.. How did you get here?)");
        exit(EXIT_SUCCESS);
    }
}

void a_dummy() {

}

void z_dummy() {

}

void buffer_overflow()
{
    char buffer[16];
    read_from_exploit(buffer, 48);
}

static char greeting[128];

int main()
{
    int local = 5;

    exec_exploit("./exploit3.x");

    read_from_exploit(greeting, sizeof(greeting)-1);

    write_to_exploit(greeting);

    puts("Waiting for input...");
    buffer_overflow();
    
    puts("Zero points. (Program terminated successfully; overflow failed.)");

    return EXIT_SUCCESS;
}
