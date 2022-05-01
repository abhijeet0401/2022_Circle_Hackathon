package com.android.deliveryapp.rider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.deliveryapp.R
import com.android.deliveryapp.databinding.ActivityManagerFeedbackBinding
import com.android.deliveryapp.databinding.ActivityRiderFeedbackBinding
import com.android.deliveryapp.manager.adapters.ManagerFeedbackAdapter
import com.android.deliveryapp.util.FeedbackReviewItem
import com.android.deliveryapp.util.Keys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RiderFeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiderFeedbackBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var orderList: Array<FeedbackReviewItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiderFeedbackBinding.inflate(layoutInflater)

        setContentView(binding.root)
        orderList = emptyArray()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {
            firestore.collection(Keys.riders).document(user.email!!)
                .collection(Keys.review)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result.documents) {
                        orderList = orderList.plus(
                            FeedbackReviewItem(
                                document.getString("review") as String

                            )
                        )
                    }

                    orderList.reverse() // order by the last order

                    updateView(orderList)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        baseContext,
                        getString(R.string.error_user_data),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.w("FIREBASE_FIRESTORE", "Error fetching documents", e)
                }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun updateView(orderList: Array<FeedbackReviewItem>) {
        // client has orders
        if (orderList.isNotEmpty()) {
            binding.ordersList.visibility = View.VISIBLE
            binding.emptyOrdersLabel.visibility = View.INVISIBLE

            binding.ordersList.adapter = ManagerFeedbackAdapter(
                this,
                R.layout.list_element_order,
                orderList
            )
        } else {
            binding.emptyOrdersLabel.visibility = View.VISIBLE
            binding.ordersList.visibility = View.INVISIBLE
        }
    }
    // when the back button is pressed in actionbar, finish this activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}