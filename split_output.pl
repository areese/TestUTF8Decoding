# Copyright 2016 Yahoo Inc.
# Licensed under the terms of the New-BSD license. Please see LICENSE file in the project root for terms.

$a=<STDIN>; 
@b=split(//,$a);
for (my $i=0;$i<=$#b;$i+=2) {
	print $b[$i];
	print $b[$i+1];
	print "\n";
}