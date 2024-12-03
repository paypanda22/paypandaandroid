package app.pay.retailers.helperclasses

class FingerPrintScanner(
    private val deviceName: String,
    private val packageName: String,
    private val imageURL: String
) {
    fun getDeviceName(): String {
        return deviceName
    }

    fun getPackageName(): String {
        return packageName
    }

    fun getImageURL(): String {
        return imageURL
    }
    companion object {
        fun getScannerList(): List<FingerPrintScanner> {
            val scanners = mutableListOf<FingerPrintScanner>()
            scanners.add(FingerPrintScanner("Mantra FingerPrint Scanner", "com.mantra.rdservice", "oplogo/mantra.png"))
            scanners.add(FingerPrintScanner("Morpho FingerPrint Scanner", "com.scl.rdservice", "oplogo/morpho.png"))
            scanners.add(FingerPrintScanner("Sequgen FingerPrint Scanner", "com.secugen.rdservice", "oplogo/sequgen.png"))
            scanners.add(FingerPrintScanner("Startek FingerPrint Scanner", "com.acpl.registersdk", "oplogo/startrek.png"))
            scanners.add(FingerPrintScanner("Precision FingerPrint Scanner", "com.precision.pb510.rdservice", "oplogo/precision.png"))
            scanners.add(FingerPrintScanner("Mantra MFS110L1 Scanner", "com.mantra.mfs110.rdservice", "oplogo/mantral1.png"))
            scanners.add(FingerPrintScanner("IDEMIA L1 RD Service", "com.idemia.l1rdservice", "oplogo/morphol1.png"))
            scanners.add(FingerPrintScanner("MIS100V2 RDService", "com.mantra.mis100v2.rdservice", "oplogo/iris.png"))
            return scanners
        }
    }
}