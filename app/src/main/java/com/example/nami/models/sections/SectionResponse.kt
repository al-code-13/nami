
data class SectionResponse(
    val orders: List<OrdersList>,
    val message: String?
)


data class OrdersList(
    val id: Int,
    val name: String,
    val lastname: String,
    val address: String,
    val value: String,
    val phoneClient: String,
    val date: String,
    val origin: String,
    val idCodBranch: Int,
    val hour: String,
    val idState: Int,
    val observations: String,
    val methodPay: MethodPay,
    val pickingOrder: PickingOrder,
    val detailOrder: DetailOrder,
    val behavior: Int
)

data class DetailOrder(
    val totalItems: Int
)

data class MethodPay(
    val id: Int,
    val name: String
)

data class PickingOrder(
    val list: List<Any?>
)
