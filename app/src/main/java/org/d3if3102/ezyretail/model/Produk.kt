import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Produk(
    var id: String? = null,
    var namaProduk: String? = null,
    var hargaBeli: Int? = null,
    var hargaJual: Int? = null,
    var deskripsi: String? = null,
    var stok: Int? = null
) {
    // Konstruktor tanpa argumen (diperlukan untuk deserialisasi oleh Firestore)
    constructor() : this(null, null, null, null, null, null)
}
