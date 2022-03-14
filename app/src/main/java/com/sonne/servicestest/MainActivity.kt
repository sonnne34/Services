package com.sonne.servicestest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.sonne.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
//            для остановки сервиса: stopService
            stopService(MyForegroundService.newIntent(this))

            startService(MyService.newIntent(this, 25))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this)
            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
//            какой сервис нужен
            val componentName = ComponentName(this, MyJobService::class.java)

//            какие ограничения работы сервиса
//            например:
//            .setRequiresCharging(true) - только когда заряжается,
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) - только когда подключен к WiFi
//            .setPersisted(true) - выполняться даже после перезагрузки устройства

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
//                при работе с методом .schedule получаем параметры из Bundle
//                .setExtras(MyJobService.newBundle(page++))
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setPersisted(true)
                .build()

//            запускаем на выполнение
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
//             .schedule - сервис не будет добавлен в очередь, он отменит предыдущий сервис
//            jobScheduler.schedule(jobInfo)

//            .enqueue - сервис будет добавлен в очередь
            val intent = MyJobService.newIntent(page++)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentService2.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }
    }
}