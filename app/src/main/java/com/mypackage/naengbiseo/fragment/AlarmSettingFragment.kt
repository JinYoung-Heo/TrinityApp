package com.mypackage.naengbiseo.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.viewmodel.AlarmViewModel
import kotlinx.android.synthetic.main.alarm_d_day_dialog.view.*
import kotlinx.android.synthetic.main.alarm_setting_fragment.*
import kotlinx.android.synthetic.main.alarm_setting_name_dialog.view.*
import kotlinx.android.synthetic.main.alarm_time_picker.view.*

class AlarmSettingFragment : Fragment(){
    private val ALARM_ACTIVATE = "1"
    private val ALARM_DEACTIVATE = "0"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alarm_setting_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as MainActivity
        val viewModel = ViewModelProvider(requireParentFragment()).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            AlarmViewModel::class.java)

        viewModel.setUserName(MainActivity.pref_user_name.myEditText)
        viewModel.setHour(MainActivity.pref_hour.myEditText)
        viewModel.setMinute(MainActivity.pref_minute.myEditText)
        viewModel.set_d_day(MainActivity.pref_dDay.myEditText.toInt())
        viewModel.setAlarmState(MainActivity.pref_alarm_state.myEditText)

        viewModel.alarmState.observe(viewLifecycleOwner, Observer{
            // 알람 활성화 되면 setAlarm()
            if (it == ALARM_ACTIVATE) {
                alarmSwitch.isChecked = true
                ac.setAlarm(MainActivity.pref_hour.myEditText.toInt(), MainActivity.pref_minute.myEditText.toInt())
            }
            // 알람 비활성화 되면 cancelAlarm()
            else if (it == ALARM_DEACTIVATE) {
                alarmSwitch.isChecked = false
                ac.cancelAlarm()
            }
        })
        viewModel.alarm_am_or_pm.observe(viewLifecycleOwner, Observer{
            am_or_pm.text = it
        })
        viewModel.alarmHour.observe(viewLifecycleOwner, Observer{
            alarmHourView.text = it
        })
        viewModel.alarmMinute.observe(viewLifecycleOwner, Observer{
            alarmMinuteView.text = it
        })
        viewModel.userName.observe(viewLifecycleOwner, Observer{
            userName.text = it
        })
        viewModel.dDay.observe(viewLifecycleOwner, Observer{
            d_dayTextView.text = it.toString() + "일전"
        })
        viewModel.alarmState.observe(viewLifecycleOwner, Observer{
            Log.d("MSG", "alarm state: " + it)
        })
        alarmTimeSettingView.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.alarm_time_picker, null)
            //AlertDialogBuilder
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
            // 시침 분침을 이전에 저장한 값으로 설정
            dialogView.myTimePicker.hour = MainActivity.pref_hour.myEditText.toInt()
            dialogView.myTimePicker.minute = MainActivity.pref_minute.myEditText.toInt()
            //show dialog
            val alertDialog = builder.show()
            dialogView.dialogCancelButton.setOnClickListener {
                //dismiss dialog
                alertDialog.dismiss()
            }
            dialogView.dialogSaveButton.setOnClickListener {
                val currentHourText: String = dialogView.myTimePicker.hour.toString()
                val currentMinuteText: String = dialogView.myTimePicker.minute.toString()

                // 알람 활성화 상태에서 시간을 수정할 경우 setAlarm()
                if (MainActivity.pref_alarm_state.myEditText == ALARM_ACTIVATE) {
                    ac.setAlarm(currentHourText.toInt(), currentMinuteText.toInt())
                }
                viewModel.setHour(currentHourText)
                viewModel.setMinute(currentMinuteText)

                //dismiss dialog
                alertDialog.dismiss()
            }
        }
        nameSettingView.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.alarm_setting_name_dialog, null)
            //AlertDialogBuilder
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
            //show dialog
            val alertDialog = builder.show()
            dialogView.nameSaveButton.setOnClickListener {
                val userName = dialogView.userNameEditText.text.toString()
                viewModel.setUserName(userName)
                alertDialog.dismiss()
            }
        }
        dDaySettingView.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.alarm_d_day_dialog, null)
            //AlertDialogBuilder
            val builder = AlertDialog.Builder(context)
                .setView(dialogView)
            dialogView.dDayNumberPicker.maxValue = 30
            dialogView.dDayNumberPicker.minValue = 1
            dialogView.dDayNumberPicker.wrapSelectorWheel = true
            dialogView.dDayNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //show dialog
            val alertDialog = builder.show()
            dialogView.dDaySaveButton.setOnClickListener {
                val d_day = dialogView.dDayNumberPicker.value
                viewModel.set_d_day(d_day)
                alertDialog.dismiss()
            }
        }
        alarmSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.setAlarmState(ALARM_ACTIVATE)
            } else {
                viewModel.setAlarmState(ALARM_DEACTIVATE)
            }
        }
        backButtonInSettingFragment.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}