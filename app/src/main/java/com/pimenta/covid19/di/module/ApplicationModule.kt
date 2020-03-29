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

package com.pimenta.covid19.di.module

import android.app.Application
import com.pimenta.covid19.network.di.module.RemoteDateSourceConfigurationModule
import com.pimenta.covid19.presentation.scheduler.RxScheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by marcus on 29-03-2020.
 */
@Module(
    includes = [
        RemoteDateSourceConfigurationModule::class
    ]
)
class ApplicationModule(
    private val application: Application
) {

    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    @Singleton
    fun providesSchedulers() = RxScheduler()
}