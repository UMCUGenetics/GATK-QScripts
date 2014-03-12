# GATK QScripts
Various QScripts used in our illumina variant calling pipeline.

### Usage
Basic example:
    
    java -jar Queue.jar -S <script>.scala

IndelRealigner example:
    
    java -Xmx4G -jar Queue.jar -jobQueue veryshort -jobEnv "threaded 1" -jobRunner GridEngine -S IndelRealigner.scala -R Homo_sapiens.GRCh37.GATK.illumina.fasta -nt 1 -mem 4 -nsc 500 -mode single -known 1000G_phase1.indels.b37.vcf -known Mills_and_1000G_gold_standard.indels.b37.vcf -I SAMPLE_1_dedup.bam -run

HaplotypeCaller example:
    
    java -Xmx16G -Xms4G -jar Queue.jar -jobQueue veryshort -jobEnv "threaded 4" -jobRunner GridEngine -jobReport logs/variantCaller.jobReport.txt -S HaplotypeCaller.scala -R Homo_sapiens.GRCh37.GATK.illumina.fasta -O testHaplotypeCaller -mem 4 -nct 4 -nsc 1000 -stand_call_conf 30 -stand_emit_conf 15 -I SAMPLE_1_dedup_realigned_recalibrated.bam -I SAMPLE_2_dedup_realigned_recalibrated.bam -D dbsnp_137.b37.vcf -L exome.interval_list -run

More information about Queue can be found here: http://gatkforums.broadinstitute.org/categories/queue

### Copyright

Copyright (c) 2014 Cuppen Group. See LICENSE for further details.
