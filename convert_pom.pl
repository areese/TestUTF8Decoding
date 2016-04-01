#!/usr/local/bin/perl -w
use strict;

my $inDependencies=0;
my $inDependency=0;
my $inExclusion=0;

my $groupId;
my $artifactId;
my $scope;
my $version;

while (<>) {
	chomp;
	if ($_ =~ /<dependencies>/ ) {
		$inDependencies = 1;
		print "dependencies {\n";
		next;
	}

	if ($_ =~ /<\/dependencies>/ ) {
		$inDependencies = 0;
		print "}\n";
		next;
	}

	if ($_ =~ /<exclusion>/ ) {
		$inExclusion = 1;
		next;
	}

	if ($_ =~ /<\/exclusion>/ ) {
		$inExclusion = 0;
		next;
	}

	if (!$inDependencies) {
		next;
	}

	if ($_ =~ /<dependency>/ ) {
		$inDependency=1;
		next;
	}

	if ($_ =~ /<\/dependency>/ ) {
		if (!defined $scope) {
			$scope="compile";
		}

		print "    ${scope} group: '${groupId}', name: '${artifactId}', version: '${version}'\n";

		$inDependency=0;
		$groupId=undef;
		$artifactId=undef;
		$scope=undef;
		$version=undef;
		$inExclusion=0;
		next;
	}

	if ($inDependency && !$inExclusion) {
		if ($_ =~ /<groupId>(.*)<\/groupId>/) {
			$groupId=$1;
		}

		if ($_ =~ /<artifactId>(.*)<\/artifactId>/) {
			$artifactId=$1;
		}

		if ($_ =~ /<version>(.*)<\/version>/) {
			$version=$1;
		}

		if ($_ =~ /<scope>(.*)<\/scope>/) {
			$scope=$1;
			if ($scope =~ /test/) {
				$scope="testCompile";
			}

			if ($scope =~ /provided/) {
				$scope="runtime";
			}
		}
	}

}