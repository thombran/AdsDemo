package cis.gvsu.edu.admobdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

const val AD_UNIT_ID = "ca-app-pub-3940256099942544/8691691433"

class InterstitialActivity : AppCompatActivity() {
    private var interstitialAd: InterstitialAd? = null
    private final var TAG = "InterstitialActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial)

        MobileAds.initialize(this) {}
        loadAd()

        val incButton = findViewById<Button>(R.id.incButton)
        val incText = findViewById<TextView>(R.id.incrementText)
        val subtext = findViewById<TextView>(R.id.titleSubtext)

        incButton.setOnClickListener {
            var newNum = incText.text.toString().toInt()
            newNum++
            incText.text = newNum.toString()

            if (newNum % 10 == 0) {
                showInterstitial()
                subtext.visibility = VISIBLE
            }
        }

        val nextScreen = findViewById<ImageView>(R.id.interstitial2rewarded)
        nextScreen.setOnClickListener {
            val intent = Intent(this, RewardedActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
                this, AD_UNIT_ID, adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        interstitialAd = null
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        super.onAdLoaded(p0)
                        interstitialAd = p0
                    }
                }
        )
    }

    private fun showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    interstitialAd = null
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    interstitialAd = null
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }
            }
            interstitialAd?.show(this)
        }
    }
}