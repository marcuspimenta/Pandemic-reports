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

package com.pimenta.pandreports.summary.data.repository

import com.pimenta.pandreports.model.data.mapper.toDomainModel
import com.pimenta.pandreports.model.domain.CountryDomainModel
import com.pimenta.pandreports.summary.data.datasouce.remote.datasource.SummaryRemoteDataSourceInterface
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by marcus on 29-03-2020.
 */
class SummaryRepository @Inject constructor(
    private val summaryRemoteDataSource: SummaryRemoteDataSourceInterface
) : SummaryRepositoryInterface {

    override fun getSummary(): Single<List<CountryDomainModel>?> =
        summaryRemoteDataSource.getSummary()
            .map {
                it.countries?.filter { country ->
                    !country.name.isNullOrBlank() && !country.slug.isNullOrBlank() &&
                            country.totalConfirmed ?: 0 > 0
                }
                    ?.sortedBy { country -> country.totalConfirmed }
                    ?.distinctBy { country -> country.slug }
                    ?.map { country -> country.toDomainModel() }
                    ?.reversed()
            }
}