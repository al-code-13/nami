package com.example.nami.presenters

import com.example.nami.controllers.services.ServiceInteractor
import com.example.nami.models.sections.SectionsResponse

interface SectionsUI {
    fun showSection(data: SectionsResponse)
    fun showError(error: String)
}

class SectionsPresenter(private val ui: SectionsUI) {
    private val interactor = ServiceInteractor()
    fun actionSections() {
        interactor.getSections(

            { data ->
                interactor.getReasons({
                    ui.showSection(data)
                }, { error ->
                    ui.showError(error)
                })
            },
            { error ->
                ui.showError(error)
            })
    }
}