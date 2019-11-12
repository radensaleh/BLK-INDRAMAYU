package com.proyek2.blkindramayu.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.fragment.AkunFragment
import com.proyek2.blkindramayu.fragment.BerandaFragment
import com.proyek2.blkindramayu.fragment.PelatihanFragment
import com.proyek2.blkindramayu.fragment.ProfilLembagaFragment
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity() {

    lateinit var berandaFragment : BerandaFragment
    lateinit var pelatihanFragment: PelatihanFragment
    lateinit var profilLembagaFragment: ProfilLembagaFragment
    lateinit var akunFragment: AkunFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        berandaFragment = BerandaFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, berandaFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

//        val bottomNav : BottomNavigationView = findViewById(R.id.navigation)

        navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_beranda -> {
                    berandaFragment = BerandaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, berandaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_pelatihan -> {
                    pelatihanFragment = PelatihanFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, pelatihanFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_profil -> {
                    profilLembagaFragment = ProfilLembagaFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, profilLembagaFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

                R.id.navigation_akun -> {
                    akunFragment = AkunFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, akunFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }

            true
        }

    }
}
