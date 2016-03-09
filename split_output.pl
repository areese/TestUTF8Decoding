$a=<STDIN>; 
@b=split(//,$a);
for (my $i=0;$i<=$#b;$i+=2) {
	print $b[$i];
	print $b[$i+1];
	print "\n";
}