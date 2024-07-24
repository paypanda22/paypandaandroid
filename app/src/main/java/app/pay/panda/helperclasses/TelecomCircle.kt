package app.pay.panda.helperclasses

data class TelecomCircle(
    val name: String, val circle_code: Int
){
    companion object{
        private val telecomCircles = mutableListOf<TelecomCircle>()
        fun getTelecomCircles(): MutableList<TelecomCircle> {
            val telecomCircles = mutableListOf<TelecomCircle>()
            val circleData = listOf(
                TelecomCircle("MUMBAI", 92),
                TelecomCircle("MEGHALAY", 103),
                TelecomCircle("DELHI", 10),
                TelecomCircle("MAHARASHTRA", 90),
                TelecomCircle("BIHAR", 52),
                TelecomCircle("KARNATAKA", 6),
                TelecomCircle("KERALA", 95),
                TelecomCircle("ORISSA", 53),
                TelecomCircle("NESA", 16),
                TelecomCircle("GOA", 102),
                TelecomCircle("UP(East)", 54),
                TelecomCircle("HP", 3),
                TelecomCircle("GUJARAT", 98),
                TelecomCircle("ASSAM", 56),
                TelecomCircle("TAMIL NADU", 94),
                TelecomCircle("JHARKHAND", 105),
                TelecomCircle("HARYANA", 96),
                TelecomCircle("J&K", 55),
                TelecomCircle("SIKKIM", 99),
                TelecomCircle("TRIPURA", 100),
                TelecomCircle("UP(West)", 97),
                TelecomCircle("CHENNAI", 40),
                TelecomCircle("RAJASTHAN", 70),
                TelecomCircle("West Bengal", 51),
                TelecomCircle("PUNJAB", 2),
                TelecomCircle("CHHATISGARH", 101),
                TelecomCircle("MIZZORAM", 104),
                TelecomCircle("MP", 93),
                TelecomCircle("KOLKATTA", 31),
                TelecomCircle("AP", 49)
            )
            telecomCircles.addAll(circleData)
            return telecomCircles
        }
    }

}


