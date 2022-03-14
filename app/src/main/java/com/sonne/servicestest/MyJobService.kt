package com.sonne.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    //    возвращаем Boolean: если работа ещё выполняется (асинхронность), то return true
//    если работа закончена, как только закончен onStartJob (линейность), то return false
    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartCommand")

//      .enqueue - сервис будет добавлен в очередь
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
            var workItem = p0?.dequeueWork()
//            при работе с методом .schedule (сервис не будет добавлен в очередь)
//            достаём параметр - номер страницы (Bundle):
//            val page = p0?.extras?.getInt(PAGE) ?: 0

//            при работе с методом .enqueue (сервис будет добавлен в очередь)
//            достаём номер страницы из интента
            while (workItem != null) {
                val page = workItem.intent?.getIntExtra(PAGE, 0)
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i $page")
                    }
//                  .completeWork - сообщает, что данная работа была закончена
//                  и можно перейти к сл., не уничтожая весь сервис и следуя по очереди
                    p0?.completeWork(workItem)
                    workItem = p0?.dequeueWork()
                }
//            когда закончить выполнение:
//            true - если нужно будет возобновить работу через время
                jobFinished(p0, false)
            }
        }
        return true
    }

    //    если хочу запланировать выполнение сервиса заново
//    после того, как сервис будет убит
//    то return true
    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {

        const val JOB_ID = 111
        private const val PAGE = "page"

        fun newBundle(page: Int): PersistableBundle {
            return PersistableBundle().apply {
                putInt(PAGE, page)
            }
        }

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}
