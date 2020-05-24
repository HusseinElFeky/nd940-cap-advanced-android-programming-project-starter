package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.models.ElectionResponse
import com.example.android.politicalpreparedness.models.RepresentativeResponse
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.adapter.ElectionAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

private val moshi = Moshi.Builder()
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */
interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): Response<ElectionResponse>

    @GET("voterInfo")
    suspend fun getVoterInfo(@Query("address") address: String, @Query("electionId") electionId: Int): Response<VoterInfoResponse>

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): Response<RepresentativeResponse>
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}
