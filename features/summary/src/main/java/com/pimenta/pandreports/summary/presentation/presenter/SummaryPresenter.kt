/*
 * Copyright (C) 2020 Marcus Pimenta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.pimenta.pandreports.summary.presentation.presenter

import android.util.Log
import com.pimenta.pandreports.model.presentation.mapper.toViewModel
import com.pimenta.pandreports.model.presentation.model.CountryViewModel
import com.pimenta.pandreports.presentation.presenter.BasePresenter
import com.pimenta.pandreports.presentation.scheduler.RxScheduler
import com.pimenta.pandreports.summary.R
import com.pimenta.pandreports.summary.domain.GetSummaryUseCaseInterface
import javax.inject.Inject

/**
 * Created by marcus on 29-03-2020.
 */
class SummaryPresenter @Inject constructor(
    private val view: SummaryContract.View,
    private val getSummaryUseCase: GetSummaryUseCaseInterface,
    private val rxScheduler: RxScheduler
) : BasePresenter(), SummaryContract.Presenter {

    private companion object {
        const val TAG = "SummaryPresenter"
    }

    override fun loadSummary() {
        getSummaryUseCase()
            .subscribeOn(rxScheduler.ioScheduler)
            .observeOn(rxScheduler.computationScheduler)
            .map { it.map { country -> country.toViewModel() } }
            .observeOn(rxScheduler.mainScheduler)
            .doOnSubscribe { view.showProgress() }
            .doFinally { view.hideProgress() }
            .subscribe({ result ->
                view.showCountries(result)
            }, { throwable ->
                Log.e(TAG, "Error while loading the summary" + throwable.message)
                view.showErrorMessage(R.string.error_message)
            }).also { compositeDisposable.add(it) }
    }

    override fun countryClicked(countryViewModel: CountryViewModel) {
        view.openTotalCases(countryViewModel)
    }

    override fun aboutClicked() {
        view.openAbout()
    }

}