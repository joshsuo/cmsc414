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
 *   You may not make changes to this file; only to exploit2.c
 *   You do not need to submit this file.
 */

#include <stdio.h>
#include <stdlib.h>

/* this header exports functions to execute the exploit and read/write
 * to/from it */
#include "comms.h"

void sensitive_function()
{
    puts("Full points! (Hey wait a minute.. How did you get here?)");
    exit(EXIT_SUCCESS);
}

void a_dummy()
{

}

void b_dummy()
{

}

void buffer_overflow()
{
    char buffer[12];
    read_from_exploit(buffer, 32);
}

static char greeting[128];

int main()
{
    int local = 5;

    exec_exploit("./exploit2.x");

    read_from_exploit(greeting, sizeof(greeting)-1);

    write_to_exploit(greeting);

    puts("Waiting for input...");
    buffer_overflow();
    
    puts("Zero points. (Program terminated successfully; overflow failed.)");

    return EXIT_SUCCESS;
}
