package com.example.memoi

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.memoi.databinding.ActivityMainBinding
import com.example.memoi.todo.Todo
import com.example.memoi.viewmodel.TodoListViewModel
import java.time.LocalDate


import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.logging.Handler



class MainActivity : AppCompatActivity() {

    enum class FragmentType {
        MainToday,
        MainTodo,
        AddNew
    }

    lateinit var binding: ActivityMainBinding
    val vm : TodoListViewModel by viewModels()

    lateinit var navcon: NavController

    // which fragment (in frgNav) is being shown to user?
    // has type of FragmentType (enum)
    var currentFragment = FragmentType.MainToday


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddNew.setOnClickListener { view ->
            val navHostFragment = binding.frgNav.getFragment<NavHostFragment>()

            // check the current fragment, and then choose action
            if (currentFragment == FragmentType.MainToday)
                navcon.navigate(R.id.action_mainTodayFragment_to_addNewFragment)
            else
                navcon.navigate(R.id.action_mainTodoFragment_to_addNewFragment)
        }


        //절전모드예외 앱으로 해재하는 권한 얻는 코드() -> 없다면 절전모드로 인한 1분마다의 체크 불가.
        val pm:PowerManager = getApplicationContext().getSystemService(POWER_SERVICE) as PowerManager
        var isWhiteListing = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName())
        }
        if (!isWhiteListing) {
            var intent = Intent()
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()))
            startActivity(intent)
        }

        navcon = binding.frgNav.getFragment<NavHostFragment>().navController
        binding.bottomNav.setupWithNavController(navcon)

        //핸들러를 통한 10초 딜레이 후 실행,viewmodel이 firebase로부터 객체를 로딩완료후 실행가능.
        Handler(Looper.getMainLooper()).postDelayed({

            //실행시점 기준 미래에 설정된 모든 알람 설정, 아직 기능 온전하지 않음.
            val todolist = vm.getList()

            for (i in 0 until todolist.size) {
                val target = todolist[i]
                if(target.localTime != null){

                    if (target.localDate == null) {
                        val newTarget = target.deepCopy()
                        newTarget.setNewDate(LocalDate.now())
                        setAlarm(newTarget)
                    }
                    else if(target.localDate.isAfter(LocalDate.now())){
                        setAlarm(target)
                    }
                    else if(target.localDate.isEqual(LocalDate.now())){
                        //.truncatedTo(ChronoUnit.MINUTES)를 통한 분 이후의 값 제거
                        if(target.localTime.isAfter(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))){
                            setAlarm(target)
                        }
                    }

                }
            }
           /* for(i in 0 .. todoTodaylist.size-1){
                if(todoTodaylist[i].localTime!=null){
                    //.truncatedTo(ChronoUnit.MINUTES)를 통한 분 이후의 값 제거
                    if(todoTodaylist[i].localTime.isAfter(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))){
                        setAlarm(todoTodaylist[i])
                    }
                }
            }*/

            //알람삭제.. 구현실패
            //cancelAlarm(checkAlarm())
            //checkAlarm은 앱 실행시점기준 가장 근미래의 todo를 가져옴
            //setAlarm(checkAlarm()) //임시코드, 가까운 todo 재실행시 잡아줌.
        }, 10000)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navcon.navigateUp() || super.onSupportNavigateUp()
    }


    // hide bottomNavigation & addNewButton
    fun hideButtons() {
        binding.bottomNav.visibility = View.INVISIBLE
        binding.btnAddNew.visibility = View.INVISIBLE
    }

    // show bottomNavigation &...
    fun showButtons() {
        binding.bottomNav.visibility = View.VISIBLE
        binding.btnAddNew.visibility = View.VISIBLE
    }
    
    
    //알람 울릴떄마다 삭제하고 알람 생성할떄 호출해서 써먹으려하였지만 삭재가 안되여 사용 안할 예정.
    /*fun checkAlarm():Todo?{
        val todolist = vm.getTodayList()
        var num=1000
        if(todolist.size!=0){
            var near = 1000000000
            for(i:Int in 0..todolist.size-1){
                //null가능성 체크
                if(todolist[i].localTime!=null) {
                    val tmp=(todolist[i].localTime.hour*60+todolist[i].localTime.minute
                            -(LocalTime.now().hour*60+LocalTime.now().minute))
                    if(tmp<0)
                        continue
                    if (near>tmp) {
                        near=tmp
                        num=i

                    }
                }
            }
            if(near==1000000000)
                return null
            //제대로 된 근미래값 todo return 확인 완료
            return todolist[num]
        }
        return null
    }*/
    
    //알람 설정
    fun setAlarm(todoNear: Todo) {
        println("setting alarm for : \n$todoNear")
        println("\n>>>\nunique Id : ${todoNear.uniqueId}\n==========\n")

        //intent를 통한 todo값 보내주기
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("title", todoNear.title)
        intent.putExtra("description",todoNear.description)
        intent.putExtra("url",todoNear.url)
        intent.putExtra("num", todoNear.uniqueId)

        //알람시간이 현재 기준으로 얼마만큼 뒤에 있는지 계산
        val time = (todoNear.localDate.year*365*24*60+todoNear.localDate.dayOfYear*24*60+todoNear.localTime.hour*60+todoNear.localTime.minute
                -LocalDate.now().year*365*24*60-LocalDate.now().dayOfYear*24*60-LocalTime.now().hour*60-LocalTime.now().minute)

        //바로 실행하지 않기에 pending
        val pendingIntent = PendingIntent.getBroadcast(
            this, todoNear.uniqueId, intent, PendingIntent.FLAG_IMMUTABLE
        )

        getSystemService(AlarmManager::class.java).setExact(
            //기기시간 기준으로 실행
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            ( (SystemClock.elapsedRealtime()/60000 + time ) * 60000 ),
            pendingIntent
        )

        //cancelAlarm(todoNear)

    }
    /*알람 삭제함수.. 실패
    fun cancelAlarm(todoNear: Todo?) {
        if(todoNear!=null) {
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("title", todoNear.title)
            intent.putExtra("description",todoNear.description)
            intent.putExtra("url",todoNear.url)
            val pendingIntent = PendingIntent.getBroadcast(
            this,0,intent,PendingIntent.FLAG_IMMUTABLE)
            getSystemService(AlarmManager::class.java).cancel(pendingIntent)
        }
    }*/


    /* todo: make it be able to be used generally, 안씀.

    fun notificate(todo: Todo) {

        val builder = NotificationCompat.Builder(this, "test_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("${todo.title}")
            .setContentText("${todo.description}\n${todo.url} ")
            .setDefaults(Notification.DEFAULT_VIBRATE)// 알림 진동기능
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.calendar))//알림창 큰 아이콘
            //.setAutoCancel(true)// 알람터치시 삭제... 작동 안하는 것으로 보임

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 이후에는 알림을 받을 때 채널이 필요
            // gradle에서 SDK 26 이상이 보장되므로 위 조건이 필요하지는 않음. 그래도 놔둡시다.
            val channel_id = "test_channel" // 알림을 받을 채널 id 설정
            val channel_name = "채널이름" // 채널 이름 설정
            val descriptionText = "설명글" // 채널 설명글 설정
            val importance = NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위 설정
            val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                description = descriptionText
            }

            // 만든 채널 정보를 시스템에 등록
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // 알림 표시: 알림의 고유 ID(ex: 1002), 알림 결과
            notificationManager.notify(1002, builder.build())
        }
    }*/
}
