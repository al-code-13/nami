package com.chefmenu.nami.presenters

import android.content.Context
import com.chefmenu.nami.models.user.UpdateProfileRequest
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

    fun actionUpdateProfile(
        phone: String? = null,
        name: String? = null,
        lastname: String? = null,
        email: String? = null
    ) {
        uiScope.launch {
            try {
                ui.showLoad()
                val newphone: String? = if (phone == "") {
                    null
                } else {
                    phone
                }
                val newname: String? = if (name == "") {
                    null
                } else {
                    name
                }
                val newlastname: String? = if (lastname == "") {
                    null
                } else {
                    lastname
                }
                val newemail: String? = if (email == "") {
                    null
                } else {
                    email
                }

                val user = UpdateProfileRequest(newname, newlastname, newphone, newemail)
                interactor.putMe(user, { data ->
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