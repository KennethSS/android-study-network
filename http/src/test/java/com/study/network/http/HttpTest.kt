package com.study.network.http

import com.study.network.http.placeholder.PlaceHolderGetResponseItem
import com.study.network.http.placeholder.PlaceHolderPostResponse
import com.study.network.http.placeholder.PlaceHolderPutResponse
import com.study.network.http.request.Request
import com.study.network.http.request.RequestMethod
import org.junit.Before
import org.junit.Test

class HttpTest {

    lateinit var executor: NetworkExecutor

    @Before
    fun init() {
        executor = NetworkExecutor(PLACE_HOLDER)
    }

    @Test
    fun testGet() {
        val request = Request(
            method = RequestMethod.GET,
            endPoint = "posts"
        )

        executor.request(request, Array<PlaceHolderGetResponseItem>::class.java) { state ->
            when(state) {
                is NetworkState.Success -> println("Get Total Count: ${state.item.size}")
                is NetworkState.Error -> println("Error: ${state.throwable?.message}")
            }
        }
    }

    @Test
    fun testPost() {
        val request = Request(
            method = RequestMethod.POST,
            endPoint = "posts"
        )

        executor.request(request, PlaceHolderPostResponse::class.java) { state ->
            when(state) {
                is NetworkState.Success -> println("Idt: ${state.item.id}")
                is NetworkState.Error -> println("Error: ${state.throwable?.message}")
            }
        }
    }

    @Test
    fun testPut() {
        val request = Request(
            method = RequestMethod.PUT,
            endPoint = "posts/1"
        )

        executor.request(request, PlaceHolderPutResponse::class.java) { state ->
            when(state) {
                is NetworkState.Success -> println("Put Success Id: ${state.item.id}")
                is NetworkState.Error -> println("Error: ${state.throwable?.message}")
            }
        }
    }

    @Test
    fun testDelete() {
        val request = Request(
            method = RequestMethod.PUT,
            endPoint = "posts/1"
        )

        executor.request(request, Any::class.java) { state ->
            when(state) {
                is NetworkState.Success -> println("Delete Success")
                is NetworkState.Error -> println("Error: ${state.throwable?.message}")
            }
        }
    }

    fun testHttpsGetMethod() {
        val searchText = "gmail"

        val request = Request(
            method = RequestMethod.GET,
            endPoint = "api/rest_v1/page/summary/$searchText"
        )

        executor.request(request, ToggleResponse::class.java) { state ->

            when(state) {
                is NetworkState.Success -> {
                    println("Success: ${state.item}")
                }
                is NetworkState.Error -> {
                    println("Error: ${state.throwable?.message}")
                }
            }
        }
    }

    fun testHttp() {
        val request = Request(
            method = RequestMethod.GET,
            endPoint = "api/v1/spot/mpi/",
            params = hashMapOf("spot_id" to 7274)
        )

        executor.request(request, SpotMiniPiResponse::class.java) { state ->

            when(state) {
                is NetworkState.Success -> {
                    println("Success: ${state.item}")
                }
                is NetworkState.Error -> {
                    println("Error: ${state.throwable?.message}")
                }
            }
        }
    }

    //@Test
    fun testToggleFavorite() {
        val request = Request(
            method = RequestMethod.POST,
            endPoint = "api/course/spot/favorite/toggle/",
            params = hashMapOf("user_id" to 988344, "spot_id" to 10169),
            paramType = ParameterType.MULTI_PART
        )

        executor.request(request, ToggleResponse::class.java) { state ->
            when(state) {
                is NetworkState.Success -> {
                    println("Success: ${state.item}")
                }
                is NetworkState.Error -> {
                    println("Error: ${state.throwable?.message}")
                }
            }
        }
    }

    /*fun testDelete() {
        val request = Request(
            method = RequestMethod.DELETE,
            endPoint = "api/v1/user/favorite/spot/10693/",
            headers = hashMapOf("Authorization" to "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwidXNlcl9pZCI6IjEwNTUyNDMiLCJqdGkiOiIyNGQ3MWFjNDAxZDQ0MTQxOGFmYjc2NGUwN2ZkMGI1YSIsImV4cCI6MTYxNzg2MzAwN30.qbOYeM0PQDt-5cuNBnhGLfghsCqsUVx9M2y7-WkPdvc")
        )

        executor.request(request, Any::class.java) { state ->

            when(state) {
                is NetworkState.Success -> {
                    println("Success: ${state.item}")
                }
                is NetworkState.Error -> {
                    println("Error: ${state.throwable?.message}")
                }
            }
        }
    }

    fun testPost() {
        val request = Request(
            method = RequestMethod.POST,
            endPoint = "api/v1/user/favorite/spot/",
            headers = hashMapOf("Authorization" to "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwidXNlcl9pZCI6IjEwNTUyNDMiLCJqdGkiOiIyNGQ3MWFjNDAxZDQ0MTQxOGFmYjc2NGUwN2ZkMGI1YSIsImV4cCI6MTYxNzg2MzAwN30.qbOYeM0PQDt-5cuNBnhGLfghsCqsUVx9M2y7-WkPdvc"),
            params = hashMapOf("spot" to 10693),
            paramType = ParameterType.JSON
        )

        executor.request(request, FavoriteResponse::class.java) { state ->

            when(state) {
                is NetworkState.Success -> {
                    println("Success: ${state.item}")
                }
                is NetworkState.Error -> {
                    println("Error: ${state.throwable?.message}")
                }
            }
        }
    }*/

    companion object {
        const val WIKI_HOST = "https://en.wikipedia.org/"
        const val DATE_POP = "https://staging.datepop.co.kr/"
        const val PLACE_HOLDER = "https://jsonplaceholder.typicode.com/"
    }
}