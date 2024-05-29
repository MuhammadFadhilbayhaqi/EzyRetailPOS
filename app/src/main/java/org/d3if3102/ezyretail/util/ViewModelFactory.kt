//package org.d3if3102.ezyretail.util
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import org.d3if3102.ezyretail.database.ProdukDao
//import org.d3if3102.ezyretail.ui.screen.produck.AddProduckViewModel
//import org.d3if3102.ezyretail.ui.screen.produck.ProdukViewModel
//import java.lang.IllegalArgumentException
//
//class ViewModelFactory(
//    private val produkDao: ProdukDao,
//) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(ProdukViewModel::class.java) -> {
//                ProdukViewModel(produkDao) as T
//            }
//            modelClass.isAssignableFrom(AddProduckViewModel::class.java) -> {
//                AddProduckViewModel(produkDao) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}
