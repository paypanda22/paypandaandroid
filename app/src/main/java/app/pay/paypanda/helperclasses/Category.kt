package app.pay.paypanda.helperclasses

class Category(
    private var _id: String,
    private var category_name: String,
    private var catID:Int
) {

    companion object {
        private val categories = listOf(
            Category("667e8a8aa0cc9372aaceaffb", "Education",3), // DONE
            Category("667e8a8aa0cc9372aaceaffc", "Housing Society",4),
            Category("667e8a8aa0cc9372aaceaffa", "Loan",2), // DONE
            Category("667e8a8aa0cc9372aaceaffd", "Municipal Taxes",7), // DONE
            Category("667e8a8aa0cc9372aaceaffe", "Clubs and Associations",30), // DONE
            Category("667e8a8aa0cc9372aaceafff", "Broadband",5), // DONE
            Category("667e8a8aa0cc9372aaceb001", "Insurance",8), // DONE
            Category("667e8a8aa0cc9372aaceb002", "DTH",13), // DONE
            Category("667e8a8aa0cc9372aaceb003", "Subscription",6), // DONE
            Category("667e8a8aa0cc9372aaceb006", "Electricity",18), // DONE
            Category("667e8a8aa0cc9372aaceb007", "Landline Postpaid",12), // DONE
            Category("667e8a8aa0cc9372aaceb004", "Fastag",9), // DONE
            Category("667e8a8aa0cc9372aaceb005", "Cable TV",21),
            Category("667e8a8aa0cc9372aaceb008", "Mobile Postpaid",10), // DONE
            Category("667e8a8aa0cc9372aaceb00a", "LPG Gas",16), // DONE
            Category("667e8a8aa0cc9372aaceb00b", "Hospital and Pathology",15),
            Category("667e8a8aa0cc9372aaceb009", "Credit Card",19), // DONE
            Category("667e8a8aa0cc9372aaceb00c", "Hospital",14),
            Category("667e8a8aa0cc9372aaceb00d", "Mobile Prepaid",23), // DONE
            Category("667e8a8aa0cc9372aaceb00f", "Municipal Services",17), // DONE
            Category("667e8a8aa0cc9372aaceb010", "Rental",25), // DONE
            Category("667e8a8aa0cc9372aaceb00e", "Water",28), // DONE
            Category("667e8a8aa0cc9372aaceb014", "B2B",24),
            Category("667e8a8aa0cc9372aaceb012", "Health Insurance",26), // DONE
            Category("667e8a8aa0cc9372aaceb013", "Recurring Deposit",20), // DONE
            Category("667e8a8aa0cc9372aaceb015", "NCMC Recharge",31),
            Category("667e8a8aa0cc9372aaceb016", "Metro Recharge",29), // DONE
            Category("667e8a8aa0cc9372aaceb017", "Donation",27), // DONE
            Category("667e8a8aa0cc9372aaceb000", "Piped Gas",11), // DONE
            Category("667e8a8aa0cc9372aaceb011", "Life Insurance",22) // DONE Remove This category
        )

        fun getAllCategories(): List<Category> {
            return categories
        }

        fun getIdByCategoryName(categoryName: String): String {
            for (category in categories) {
                if (category.category_name == categoryName) {
                    return category._id
                }
            }
            return ""
        }
    }

    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var name: String
        get() = category_name
        set(value) {
            category_name = value
        }
}