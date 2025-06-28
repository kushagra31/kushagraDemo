package com.example.data.sync

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
//@HiltWorker
//class SyncWorker @AssistedInject constructor(
//    @Assisted private val appContext: Context,
//    @Assisted workerParams: WorkerParameters,
//    private val usersRepository: UsersRepository
//) : CoroutineWorker(appContext, workerParams), Synchronizer {
//
//    override suspend fun getForegroundInfo(): ForegroundInfo =
//        appContext.syncForegroundInfo()
//
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        // First sync the repositories in parallel
//        val syncedSuccessfully = usersRepository.sync()
//
//        if (syncedSuccessfully) {
//            Result.success()
//        } else {
//            Result.retry()
//        }
//    }
//
//    companion object {
//        /**
//         * Expedited one time work to sync data on app startup
//         */
//        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
//            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
//            .setConstraints(SyncConstraints)
//            .setInputData(SyncWorker::class.delegatedData())
//            .build()
//    }
//}

