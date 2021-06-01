package com.study.network

import com.nhaarman.mockitokotlin2.doReturn
import com.study.network.response.PhotoItemResponse
import com.study.network.retrofit.RetrofitClient
import com.study.network.retrofit.exception.NetworkException
import com.study.network.retrofit.mock.mockService
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

@RunWith(JUnit4::class)
class RemoteTest {

    private lateinit var imageService: PicsumService

    @Mock
    private lateinit var mockPicsumService: PicsumService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        imageService = RetrofitClient.provideService()

        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = ErrorRequestDispatcher() //PhotoRequestDispatcher()
        mockWebServer.start()

        mockPicsumService = mockService(mockWebServer)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `getPhotos 이미지 리스트 빈값 테스트`() {
        imageService.fetchPhotoList(page = 1)
            .doOnSuccess { println("$it") }
            .test()
            .assertValue { it.isNotEmpty() }
            .assertComplete()
    }

    //@Test
    fun `HttpException 테스트`() {
        val response = MockResponse().apply {
            setResponseCode(404)
            setBody("{}")
        }

        val httpException = HttpException(Response.error<Nothing>(404, "gg".toResponseBody()))
        doReturn(Single.error<Nothing>(httpException))
            .`when`(mockPicsumService)
            .fetchPhotoList(page = 1)

            //.test()
            //.assertError(NetworkException.NotFoundException::class.java)

        mockPicsumService.fetchPhotoList(page = 1)
            .doOnError {
                println("$it")
            }
            .test()

        /*mockPicsumService.fetchPhotoList(page = 1)
            .doOnError {
                println("$it")
            }
            .doOnSuccess {
                println("$it")
            }
            .test()*/
    }
}