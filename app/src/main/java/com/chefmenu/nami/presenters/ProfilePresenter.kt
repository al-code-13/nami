package com.chefmenu.nami.presenters

import android.content.Context
import com.chefmenu.nami.models.user.UserResponse
import kotlinx.coroutines.*

interface ProfileUI {
    fun showProfile(data: UserResponse)
    fun showSuccess(message: String)
    fun showError(error: String)
    fun showLoad()
}

class ProfilePresenter(val context: Context, private val ui: ProfileUI) : BasePresenter() {
    private var viewModelJob: Job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    fun getUser() {
        uiScope.launch {
            try {
                interactor.getMe({ data ->
                    ui.showLoad()
                    addDataToDB(data)
                    ui.showProfile(data)
                }, { error ->
                    ui.showError(error)
                })
            } catch (e: Exception) {
            }

        }
    }

    fun actionUpdateProfile(phone: String) {
        uiScope.launch {
            try {
                ui.showLoad()
                interactor.putMe(phone, { data ->
                    ui.showSuccess(data.message)
                }, { error ->
                    ui.showError(error)
                })
            } catch (e: Exception) {
            }

        }
    }

    private fun addDataToDB(data: UserResponse) = runBlocking {
        launch(Dispatchers.Main) {
            try {
                realm!!.executeTransaction {
                    it.copyToRealmOrUpdate(data)
                }
            } catch (e: Exception) {
            }
        }
    }

}