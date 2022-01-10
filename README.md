
## 네트워크 체킹 항목
 - useTraffic Manifest


# Retrofit
## Retrofit 겪은 이슈
- Reseponse Content가 Empty일 때 에러르 마주했는데 서비스엣 Any로 리턴하지않고 Unit을 리턴으로 해결

 ## okHttp

 ### Interceptor
 <img src="https://github.com/KennethSS/android-study-network/blob/master/resource/okhttp_core.png" align="start"></img>
 
 - Application Interceptor: 응답 받은 콘텐트 내용이나 Request 콘텐츠 관련
 - Network Interceptor: 네트워크 상태 및 서버 값의 따라 retry


### Exception

NetworkException
- BadRequestException
- UnauthorizedException
- ForbiddenException
- NotFoundException
- UnknownException



## Trouble Shooting
- Parameter type must not include a type variable or wildcard: java.util.Map.....
  해결 방법은 Map부분을 MutableMap이나 HashMap로 바꿔주면 된다.
  before
  fun request(@PartMap params: Map<String, RequestBody>): Flowable<Result>
  after
  fun request(@PartMap params: HashMap<String,  RequestBody>): Flowable<Result>
- 
  
