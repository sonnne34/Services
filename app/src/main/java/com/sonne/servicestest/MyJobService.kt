package com.sonne.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
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
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log("Timer $i")
            }
//            когда закончить выполнение:
//            true - если нужно будет возобновить работу через время
            jobFinished(p0, true)
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
    }
}
