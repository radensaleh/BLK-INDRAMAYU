package com.proyek2.blkindramayu.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.proyek2.blkindramayu.R
import com.proyek2.blkindramayu.fragment.*
import com.proyek2.blkindramayu.room.AppDataBase
import kotlinx.android.synthetic.main.activity_beranda.*

class BerandaActivity : AppCompatActivity() {

    private lateinit var berandaFragment : BerandaFragment
    private lateinit var pelatihanFragment: PelatihanFragment
    private lateinit var profilLembagaFragment: ProfilLembagaFragment
    private lateinit var akunFragment: AkunFragment
    private lateinit var loginregisFragment : LoginRegisterFragment

    private lateinit var mFragmentManager : FragmentManager
    private lateinit var mFragmentTransaction : FragmentTransaction
    private var appDB : AppDataBase? = null
    private var navTab : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        mFragmentManager    = supportFragmentManager
        berandaFragment     = BerandaFragment()
        pelatihanFragment   = PelatihanFragment()
        profilLembagaFragment = ProfilLembagaFragment()
        loginregisFragment    = LoginRegisterFragment()
        akunFragment          = AkunFragment()

        val intent = intent
        navTab = intent.getIntExtra("navTab", 0)

        when(navTab){
            1 -> navigation.selectedItemId = R.id.navigation_beranda
            2 -> navigation.selectedItemId = R.id.navigation_pelatihan
            3 -> navigation.selectedItemId = R.id.navigation_profil
            4 -> navigation.selectedItemId = R.id.navigation_akun
        }

        appDB = AppDataBase.getInstance(this)

        navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_beranda -> {
                    navTab = 1
                    loadFragment()
                }

                R.id.navigation_pelatihan -> {
                    navTab = 2
                    loadFragment()
                }

                R.id.navigation_profil -> {
                    navTab = 3
                    loadFragment()
                }

                R.id.navigation_akun -> {
                    navTab = 4
                    loadFragment()
                }
            }

            true
        }

        if(savedInstanceState != null){
            when (supportFragmentManager.getFragment(savedInstanceState, "fragment")) {
                is BerandaFragment -> {
                    berandaFragment = supportFragmentManager.getFragment(savedInstanceState, "fragment") as BerandaFragment
                }
                is PelatihanFragment -> {
                    pelatihanFragment = supportFragmentManager.getFragment(savedInstanceState, "fragment") as PelatihanFragment
                }
                is ProfilLembagaFragment -> {
                    profilLembagaFragment = supportFragmentManager.getFragment(savedInstanceState, "fragment") as ProfilLembagaFragment
                }
                is AkunFragment -> {
                    akunFragment = supportFragmentManager.getFragment(savedInstanceState, "fragment") as AkunFragment
                }
                is LoginRegisterFragment -> {
                    loginregisFragment = supportFragmentManager.getFragment(savedInstanceState, "fragment") as LoginRegisterFragment
                }
            }
        }

        loadFragment()

    }

    private fun loadFragment(){
        mFragmentTransaction = mFragmentManager.beginTransaction()
        when(navTab){
            1 -> mFragmentTransaction.replace(R.id.frame_layout, berandaFragment, BerandaFragment::class.java.simpleName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            2 -> mFragmentTransaction.replace(R.id.frame_layout, pelatihanFragment, PelatihanFragment::class.java.simpleName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            3 -> mFragmentTransaction.replace(R.id.frame_layout, profilLembagaFragment, ProfilLembagaFragment::class.java.simpleName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            4 -> if(appDB?.memberDao()?.getMember() == null){
                mFragmentTransaction.replace(R.id.frame_layout, loginregisFragment, LoginRegisterFragment::class.java.simpleName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }else{
                mFragmentTransaction.replace(R.id.frame_layout, akunFragment, AkunFragment::class.java.simpleName).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }
        }
        mFragmentTransaction.commit()
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        when(navTab){
            1 -> supportFragmentManager.putFragment(outState, "fragment", berandaFragment)
            2 -> supportFragmentManager.putFragment(outState, "fragment", pelatihanFragment)
            3 -> supportFragmentManager.putFragment(outState, "fragment", profilLembagaFragment)
            4 -> {
                if(this.appDB?.memberDao()?.getMember() == null){
                    supportFragmentManager.putFragment(outState, "fragment", loginregisFragment)
                }else{
                    supportFragmentManager.putFragment(outState, "fragment", akunFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadFragment()
    }
}
