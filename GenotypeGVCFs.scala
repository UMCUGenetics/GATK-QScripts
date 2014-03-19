package org.broadinstitute.sting.queue.qscripts

import org.broadinstitute.sting.queue.QScript
import org.broadinstitute.sting.queue.extensions.gatk._

class Genotyper extends QScript {
    // Create an alias 'qscript' to be able to access variables in the Genotyper.
    // 'qscript' is now the same as 'Genetyper.this'
    qscript =>

    // Required arguments. All initialized to empty values.
    @Input(doc="The reference file for the bam files.", shortName="R", required=true)
    var referenceFile: File = _

    @Input(doc="One or more .genome.vcf files.", shortName="I")
    var gvcfFiles: List[File] = Nil

    @Input(doc="Output core filename.", shortName="O", required=true)
    var out: File = _

    @Argument(doc="Maxmem.", shortName="mem", required=true)
    var maxMem: Int = _

    @Argument(doc="Number of threads per data thread", shortName="nt", required=true)
    var numThreads: Int = _

    @Argument(doc="Number of scatters", shortName="nsc", required=true)
    var numScatters: Int = _

    // The following arguments are all optional.
    @Input(doc="An optional file with known SNP sites.", shortName="D", required=false)
    var dbsnpFile: File = _

    @Argument(doc="An optional file with targets intervals.", shortName="inv", required=false)
    var nonVariants: Boolean = false

    def script() {
	val genotyper = new GenotypeGVCFs

	// All required input
	genotyper.variant = gvcfFiles
	genotyper.reference_sequence = referenceFile
	genotyper.out = qscript.out + ".raw_variants.vcf"

	genotyper.scatterCount = numScatters
	genotyper.memoryLimit = maxMem
	genotyper.num_threads = numThreads

	// Optional input
	genotyper.includeNonVariants = nonVariants
	if (dbsnpFile != null) {
	    genotyper.D = dbsnpFile
	}
	//add function to queue
	add(genotyper)
    }
}