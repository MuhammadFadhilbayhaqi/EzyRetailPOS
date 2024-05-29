package org.d3if3102.ezyretail.ui.screen.authentication

import Produk
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.d3if3102.ezyretail.model.User
import org.d3if3102.ezyretail.navigation.Screen

class MainViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        // Check if user is logged in
        auth.currentUser?.let {
            fetchUserData(it.uid)
        }
    }

    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    fun fetchUserData(uid: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users").document(uid).get().await()
                val user = snapshot.toObject(User::class.java)
                _currentUser.value = user
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun logout(navController: NavHostController) {
        auth.signOut()
        _currentUser.value = null
        navController.navigate(Screen.Login.route)
    }

    fun registerUser(email: String, password: String, username: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                if (user != null) {
                    val userData = User(user.uid, email, username)
                    firestore.collection("users").document(user.uid).set(userData).await()
                    _currentUser.value = userData
                    onSuccess()
                } else {
                    onFailure()
                }
            } catch (e: Exception) {
                onFailure()
            }
        }
    }


    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                if (user != null) {
                    fetchUserData(user.uid)
                    onSuccess()
                } else {
                    onFailure()
                }
            } catch (e: Exception) {
                onFailure()
            }
        }
    }

    private val _produkList = MutableStateFlow<List<Produk>>(emptyList())
    val produkList: StateFlow<List<Produk>> = _produkList


    fun getProdukList(userId: String) {
        db.collection("users").document(userId).collection("produk")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    Log.e("FirestoreError", "Error fetching tanaman list: $error")
                    return@addSnapshotListener
                }

                val produkList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Produk::class.java)
                } ?: emptyList()

                _produkList.value = produkList
            }
    }


    fun updateProduk(produk: Produk, onComplete: (Boolean, String?) -> Unit) {
        val userId = _currentUser.value?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("produk")
            .document(produk.id.toString())
            .set(produk)
            .addOnSuccessListener {
                onComplete(true, null)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }


    fun deleteProduk(produk: Produk) {
        val userId = _currentUser.value?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("produk")
            .document(produk.id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Dokumen berhasil dihapus!")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Kesalahan menghapus dokumen: $e")
            }
    }
}
