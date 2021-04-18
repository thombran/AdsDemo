package cis.gvsu.edu.admobdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

/**
 * This is our demo rewarded ad activity which demonstrates a simple implementation/functionality
 * of a rewarded ad
 * @author Brandon Thomas
 * @version 0.1.0
 */
class RewardedActivity : AppCompatActivity() {

    private var rewardedAd: RewardedAd? = null
    private final var TAG = "RewardedActivity" // Used for logging
    private var rewardText: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded)

        MobileAds.initialize(this) // Start up mobile ad service for this activity
        loadRewardAd() // Load a reward ad before user beings interaction

        // Setup the callback for loading a rewarded ad
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.d(TAG, "Ad failed to load with error: $p0")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad loaded successfully")
            }
        }

        // When increment button is pressed, simply increase count by 1
        val rewardIncButton = findViewById<Button>(R.id.rewardIncButton)
        rewardText = findViewById(R.id.rewardText)
        rewardText?.tag = 0
        rewardIncButton.setOnClickListener {
            rewardText?.tag = rewardText?.tag.toString().toInt() + 1
            rewardText?.text = "Current count: ${rewardText?.tag}"
        }

        // When reward button is pressed, display rewarded ad to user
        val rewardButton = findViewById<Button>(R.id.rewardButton)
        rewardButton.setOnClickListener {
            showRewardAd()
        }
    }

    /**
     * Helper function to load rewarded ads, which includes basic error handling.
     * If ad is successfully loaded from Google servers, rewardedAd variable is set accordingly
     */
    private fun loadRewardAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object :
                RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.d(TAG, "Ad failed to load with error: $p0")
                rewardedAd = null
            }

            override fun onAdLoaded(p0: RewardedAd) {
                Log.d(TAG, "Ad loaded successfully")
                rewardedAd = p0
            }
        })
    }

    /**
     * Function to actually display the rewarded ad when appropriate. Rewards user with a small
     * amount of extra click to their total amount displayed on the screen
     */
    @SuppressLint("SetTextI18n")
    private fun showRewardAd() {
        if (rewardedAd != null) {
            rewardedAd?.show(this, OnUserEarnedRewardListener() {
                Log.d(TAG, "User reward earned")
                rewardText?.tag = rewardText?.tag.toString().toInt() + 100 // This is the reward
                rewardText?.text = "Current count: ${rewardText?.tag}"
                Toast.makeText(this, "Your reward has been given",
                    Toast.LENGTH_SHORT).show()
            })
        }
    }
}