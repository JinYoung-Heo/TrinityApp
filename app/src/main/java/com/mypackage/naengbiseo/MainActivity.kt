package com.mypackage.naengbiseo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mypackage.naengbiseo.room.*
import com.mypackage.naengbiseo.viewmodel.MainViewModel
import com.mypackage.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.host_activity.*
import kotlinx.android.synthetic.main.layout_main_drawer.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity(),
    PopupMenu.OnMenuItemClickListener {
    lateinit var dao1: FoodDao
    lateinit var dao2: ExcelDao
    lateinit var repository1: FoodDataRepository
    lateinit var repository2: ExcelDataRepository
    lateinit var factory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    lateinit var imm: InputMethodManager
    companion object {
        lateinit var pref_hour : MySharedPreferences
        lateinit var pref_minute : MySharedPreferences
        lateinit var pref_am_or_pm : MySharedPreferences
        lateinit var pref_user_name : MySharedPreferences
        lateinit var pref_dDay : MySharedPreferences
        lateinit var pref_alarm_state : MySharedPreferences
    }
    private val ALARM_ACTIVATE = "1"
    private val ALARM_DEACTIVATE = "0"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref_hour = MySharedPreferences(applicationContext, "hour", "8")
        pref_minute = MySharedPreferences(applicationContext, "minute", "0")
        pref_am_or_pm = MySharedPreferences(applicationContext, "am_pm", "오전")
        pref_user_name = MySharedPreferences(applicationContext, "userName", "주인")
        pref_dDay = MySharedPreferences(applicationContext, "d-day", "5")
        pref_alarm_state = MySharedPreferences(applicationContext, "alarmState", "1")
        val assetManager: AssetManager = resources.assets
        val inputStream: InputStream = assetManager.open("excelData2.txt")

        setContentView(R.layout.host_activity)

        dao1 = AppDatabase.getInstance(this).foodDao()
        dao2 = AppDatabase.getInstance(this).excelDao()
        repository1 = FoodDataRepository.getInstance(dao1)
        repository2 = ExcelDataRepository.getInstance(dao2)
        factory = MainViewModelFactory(repository1, repository2)
        viewModel = ViewModelProviders.of(this, factory).get(
            MainViewModel::class.java
        )
        //searchIconEditText.focus



        viewModel.allFoodData.observe(this, Observer {
            if(viewModel.alarmIn()){
                red_spot1.visibility = View.VISIBLE
                //go_to_alarm_button.setImageResource(R.drawable.bell_spot)
            }
            else if(!viewModel.alarmIn()){
                red_spot1.visibility = View.INVISIBLE
                //go_to_alarm_button.setImageResource(R.drawable.bell)
            }

            if(viewModel.basketIn()){
                red_spot2.visibility = View.VISIBLE
                //go_to_basket_button.setImageResource(R.drawable.basket_spot)
            }
            else if(!viewModel.basketIn()){
                red_spot2.visibility = View.INVISIBLE
                //go_to_basket_button.setImageResource(R.drawable.basket)
            }
        })

        viewModel.allExcelData.observe(this, Observer {
            if (it.isEmpty()) {
                Log.d("empty", "비었다@@@@")

                inputStream.bufferedReader().readLines().forEach {
                    var token = it.split("\t")
                    Log.d("empty", token.toString())
                    var data = ExcelData(
                        iconName = token[0],
                        storeWay = token[1],
                        useDate = token[3],
                        treatWay = token[2]
                    )
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.insertExcelData(data)
                    }
                }
            }
            else{
                Log.d("empty", "안비었다@@@@")
                Log.d("empty", it.toString())
            }
        })

        viewModel.initSortData()
        sort_button.text = "항목별"
        buttonEvent()
        initViewFinal()

        if(pref_alarm_state.myEditText == ALARM_ACTIVATE) {
            setAlarm(pref_hour.myEditText.toInt(), pref_minute.myEditText.toInt())
        } else if (pref_alarm_state.myEditText == ALARM_DEACTIVATE) {
            cancelAlarm()
        }
    }

    fun cancelAlarm() {
        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, BroadcastD::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        if (pendingIntent != null) {
            manager.cancel(pendingIntent)
            pendingIntent.cancel()
//            Toast.makeText(applicationContext, "알람을 취소했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun setAlarm(hour: Int, minute: Int) {
        val manager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, BroadcastD::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val calendar: Calendar = Calendar.getInstance()

        //알람시간 calendar에 set해주기
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정 - 이렇게 안하면 이 함수가 호출될때마다 지나갈 알림도 뜸
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        //알람 예약 - 이미 예약된 경우 새로 덮어씀
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
//        Toast.makeText(applicationContext, hour.toString() + ":" + minute + "에 알람을 설정하였습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    /*override fun onTouchEvent(event: MotionEvent?): Boolean {
        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager // as로 타입 캐스팅
        imm.hideSoftInputFromWindow(searchIconEditText.windowToken,0)
        return true
    }*/
    //모듈화를 위해 분리 , 클래스안에 함수 매소드.
    fun initViewFinal() {
        setSupportActionBar(main_toolbar) // 전체화면에 메인 툴바를 넣겠다.

        val host =
            nav_host_fragment as NavHostFragment //우리가 만든것(nav_host_fragment)과 이미 있는것을 결합.nav_host_fragment 는 view,xml
        //NavHostFragment 는 클래스
        val navController = host.navController // 바로 윗줄 포함 두줄 필수. 네비게이션.xml에 접근위해.

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 화면이 바뀔때 키보드 무조건 숨김
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Exception) {
                return@addOnDestinationChangedListener
            }
            handleToolbar(destination)
        } // 원랜 ({})

    }


    /*override fun onSupportNavigateUp(): Boolean {

        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }*/

    private fun handleToolbar(destination: NavDestination) { //툴바 표시할지 안할지
        supportActionBar?.setDisplayShowTitleEnabled(false) //타이틀 제목 없애기
        when (destination.id) { // 스위치 문과 동일.

            R.id.mainFragment -> {
                //이게 드로우어를 락 언락 정하는거
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                // 드로우어 툴바 쉽게 꺼내오게 서포트툴바
                supportActionBar?.show()
            }
            else -> {
                //이게 드로우어를 락 언락 정하는거
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                // 드로우어 툴바 쉽게 꺼내오게 서포트툴바
                supportActionBar?.hide() // 툴바 숨기기

            }

        }

    }

    private fun buttonEvent() {

        search_button.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment)
        }
        sort_button.setOnClickListener {

            /*val popup = PopupMenu(this, sort_button)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.sort_menu, popup.menu)
            popup.show()*/

            PopupMenu(this, sort_button).apply {

                setOnMenuItemClickListener(this@MainActivity)
                inflate(R.menu.sort_menu)
                show()
            }
        }
        x_button.setOnClickListener {
            main_drawer_layout.closeDrawers()
        }

        go_to_basket_button.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.basketFragment)
        }

        go_to_alarm_button.setOnClickListener{
            findNavController(R.id.nav_host_fragment).navigate(R.id.alarmFragment)
        }
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.category_sort -> {
                Log.d("s", "클릭")
                viewModel.setSortData(0)
                sort_button.text = "항목별"
            }
            R.id.buy_sort -> {
                Log.d("s", "클릭")
                viewModel.setSortData(1)
                sort_button.text = "구매순"
            }
            R.id.expiration_sort -> {
                Log.d("s", "클릭")
                viewModel.setSortData(2)
                sort_button.text = "유통기한 임박순"
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
