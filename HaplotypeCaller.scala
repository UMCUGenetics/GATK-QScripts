package org.broadinstitute.sting.queue.qscripts

import org.broadinstitute.sting.queue.QScript
import org.broadinstitute.sting.queue.extensions.gatk._

class VariantCaller extends QScript {
    // Create an alias 'qscript' to be able to access variables in the VariantCaller.
    // 'qscript' is now the same as 'VariantCaller.this'
    qscript =>

    // Required arguments. All initialized to empty values.
    @Input(doc="The reference file for the bam files.", shortName="R", required=true)
    var referenceFile: File = _

    @Input(doc="One bam files.", shortName="I")
    var bamFile: File = _

    @Argument(doc="Maxmem.", shortName="mem", required=true)
    var maxMem: Int = _

    @Argument(doc="Number of cpu threads per data thread", shortName="nct", required=true)
    var numCPUThreads: Int = _

    @Argument(doc="Number of scatters", shortName="nsc", required=true)
    var numScatters: Int = _

    @Argument(doc="Minimum phred-scaled confidence to call variants", shortName="stand_call_conf", required=true)
    var standCallConf: Int = _ //30 //default: best-practices value

    @Argument(doc="Minimum phred-scaled confidence to emit variants", shortName="stand_emit_conf", required=true)
    var standEmitConf: Int = _ //10 //default: best-practices value

    // The following arguments are all optional.
    @Input(doc="An optional file with known SNP sites.", shortName="D", required=false)
    var dbsnpFile: File = _

    @Input(doc="An optional file with targets intervals.", shortName="L", required=false)
    var targetFile: File = _

    @Argument(doc="Amount of padding (in bp) to add to each interval", shortName="ip", required=false)
    var intervalPadding: Int = 0

    def script() {
	val haplotypeCaller = new HaplotypeCaller

	// All required input
	haplotypeCaller.input_file :+= bamFile
	haplotypeCaller.reference_sequence = referenceFile
	haplotypeCaller.out = swapExt(bamFile, "bam", "genome.vcf")

	haplotypeCaller.scatterCount = numScatters
	haplotypeCaller.memoryLimit = maxMem
	haplotypeCaller.num_cpu_threads_per_data_thread = numCPUThreads

	haplotypeCaller.stand_emit_conf = standEmitConf
	haplotypeCaller.stand_call_conf = standCallConf

	// GATK 3.0 pipeline
	haplotypeCaller.emitRefConfidence = org.broadinstitute.sting.gatk.walkers.haplotypecaller.HaplotypeCaller.ReferenceConfidenceMode.GVCF
	haplotypeCaller.variant_index_type = org.broadinstitute.sting.utils.variant.GATKVCFIndexType.LINEAR
	haplotypeCaller.variant_index_parameter = 128000

	// Optional input
	if (dbsnpFile != null) {
	    haplotypeCaller.D = dbsnpFile
	}
	if (targetFile != null) {
	    haplotypeCaller.L :+= targetFile
	    haplotypeCaller.ip = intervalPadding
	}

	//add function to queue
	add(haplotypeCaller)
    }
}