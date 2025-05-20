#!/usr/bin/env perl

use warnings;
use strict;

use autodie;
use Cwd;
use Data::Dumper;

my $dir;
if ($#ARGV >= 0) {
	$dir = $ARGV[0];
} else {
	$dir = getcwd;
}

chdir($dir);

my @required = qw(task1/exploit1.c
                  task1/exploit2.c
                  task1/exploit3.c
                  task1/exploit4.c);


my @found = grep {! -f $_;} @required;

if ($#found != -1) {
	print "the following file(s) are missing: @found\n";
} else {
	print "Prepare your submission tarball with the following command:\n";
	print "tar -C $dir -czvf <lastname>.<firstname>.tgz @required\n";
}
