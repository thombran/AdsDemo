package cis.gvsu.edu.admobdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedActivity : AppCompatActivity() {

    private var rewardedAd: RewardedAd? = null
    private final var TAG = "RewardedActivity"
    private var rewardText: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded)

        MobileAds.initialize(this)
        loadRewardAd()

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

        val rewardIncButton = findViewById<Button>(R.id.rewardIncButton)
        rewardText = findViewById(R.id.rewardText)
        rewardText?.tag = 0
        rewardIncButton.setOnClickListener {
            rewardText?.tag = rewardText?.tag.toString().toInt() + 1
            rewardText?.text = "Current count: ${rewardText?.tag}"
        }

        val rewardButton = findViewById<Button>(R.id.rewardButton)
        rewardButton.setOnClickListener {
            showRewardAd()
        }
    }

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

    @SuppressLint("SetTextI18n")
    private fun showRewardAd() {
        if (rewardedAd != null) {
            rewardedAd?.show(this, OnUserEarnedRewardListener() {
                Log.d(TAG, "User reward earned")
                rewardText?.tag = rewardText?.tag.toString().toInt() + 100
                rewardText?.text = "Current count: ${rewardText?.tag}"
                Toast.makeText(this, "Your reward has been given",
                    Toast.LENGTH_SHORT).show()
            })
        }
    }
}