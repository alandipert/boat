#!/usr/bin/env perl

use strict;
use warnings;
use Text::CSV::Simple;
use Data::Dumper;

my @nums    = ( "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" );

my %cats    = ( I     => { name=>'Hull structure',      afi=>0, d=>[] },
                II    => { name=>'Interior structure',  afi=>0, d=>[] },
                III   => { name=>'Mechanical',          afi=>0, d=>[] },
                IV    => { name=>'Electrical',          afi=>0, d=>[] },
                V     => { name=>'Piping',              afi=>0, d=>[] },
                VI    => { name=>'Wiring',              afi=>0, d=>[] },
                VII   => { name=>'Hull outfit',         afi=>0, d=>[] },
                VIII  => { name=>'Deck equipment',      afi=>0, d=>[] },
                IX    => { name=>'Interior outfit',     afi=>0, d=>[] },
                X     => { name=>'Payload',             afi=>0, d=>[] } );

my ($start, $W, $L, $V, $T);

my $file    = $ARGV[0] || die "no file specified!\n";
my $p       = Text::CSV::Simple->new;
$p->field_map(qw/name x matl wt lcg tcg vcg y z basis/);
my @data    = $p->read_file($file);

for ($start=0; $data[$start] && ! $data[$start]{matl}; $start++) { };

for (my $i=$start+1; $data[$i] && $data[$i]{name} ne "Totals"; $i++) {
  my %t = %{$data[$i]};
  if ($t{name} =~ /^([IVX]+) (.*)$/) {
    if (! $cats{$1}) {
      die "no such category: ".$1."\n";
    }
    $t{name} = $2;
    push(@{$cats{$1}{d}}, \%t); 
  }
}

print <<PREAMBLE;
\\documentclass{article}
\\newcommand{\\HRule}{\\rule{\\linewidth}{0.5mm}}
\\begin{document}

\\begin{titlepage}
  \\begin{center}
    \\textsc{\\LARGE Landing School}\\\\[1.5cm]

    \\HRule \\\\[0.4cm]
    { \\huge \\bfseries Pilot Boat }\\\\[0.4cm]
    \\HRule \\\\[2cm]

    {\\Large Weight Calculation}\\\\[0.4cm]
    Micha Niskin \\\\[0.4cm]
    \\today \\\\[2cm]

    \\begin{tabular}{l r}
      LOA                       & 11.415 m \\\\
      DWL                       & 10.000 m \\\\
      Beam                      & 3.837 m \\\\
      Draft                     & 0.610 m \\\\
      Displacement              & 10,400 kg \\\\
      Power                     & 2 \$\\times\$ 350 hp \\\\
      Speed (maximum)           & 30 kt \\\\
      Speed (cruise)            & 25 kt \\\\
    \\end{tabular}

  \\end{center}
\\end{titlepage}

\\pagebreak
\\begin{center}
PREAMBLE

foreach my $num (@nums) {
  my ($w, $l, $v, $t);
  my $a   = $cats{$num}{afi};
  my $n   = "AFI ".$a."%";
  my @d   = @{$cats{$num}{d}};
  my %afi;

  $w = $l = $v = $t = 0;

  print "\\flushleft{\\Large \\bfseries ".$cats{$num}{name}."}\\\\[0.25cm]\n";

  print "\\begin{tabular*}{\\textwidth}{\@{\\extracolsep{\\fill}} l r r r r }\n";
  print "Item name & Mass & LCG & VCG & TCG \\\\\n";
  print "\\hline\n";

  foreach my $i (@d) {
    my $nm = $i->{name};
    my $wi = $i->{wt};
    my $li = $i->{lcg};
    my $vi = $i->{vcg};
    my $ti = $i->{tcg};

    $w += $wi;
    $l += $wi * $li;
    $v += $wi * $vi;
    $t += $wi * $ti;

    $li /= 1000;
    $vi /= 1000;
    $ti /= 1000;

    printf("%s & %.0f & %.3f & %.3f & %.3f \\\\\n", $nm, $wi, $li, $vi, $ti);
  }

  if ($w != 0) {
    %afi = (name=>$n, wt=>$a/100*$w, lcg=>$l/$w, vcg=>$v/$w, tcg=>$t/$w);
  } else {
    %afi = (name=>$n, wt=>0, lcg=>0, vcg=>0, tcg=>0);
  }

  $w += $afi{wt};
  $l += $afi{wt}*$afi{lcg};
  $v += $afi{wt}*$afi{vcg};
  $t += $afi{wt}*$afi{tcg};

  if ($w != 0) {
    $cats{$num}{d} = {wt=>$w, lcg=>$l/$w, vcg=>$v/$w, tcg=>$t/$w};
    printf("Total & %.0f & %.3f & %.3f & %.3f \\\\\n", $w, $l/$w, $v/$w, $t/$w);
  } else {
    $cats{$num}{d} = {wt=>0, lcg=>0, vcg=>0, tcg=>0};
    printf("Total & %.0f & %.3f & %.3f & %.3f \\\\\n", 0, 0, 0, 0);
  }

  print "\\end{tabular*}\\\\[0.5cm]\n";
}

print "\\end{center}\n";
print "\\end{document}\n";
