![logo](https://static.wanted.co.kr/images/events/3178/58ac3248.jpg)

# 지리 기반 맛집 추천 웹 서비스
본 서비스는 공공데이터를 활용하여, 지역 음식점 목록을 자동으로 업데이트 하고 이를 활용한다. 사용자 위치에맞게 맛집 및 메뉴를 추천하여 더 나은 다양한 음식 경험을 제공하고, 음식을 좋아하는 사람들 간의 소통과 공유를 촉진하려 합니다.
<br/>

## Table of Contents
- [개요](#개요)
- [Skils](#skils)
- [Teams](#teams)
- [Running Tests](#running-tests)
- [API Reference](#api-reference)
- [프로젝트 진행 및 이슈 관리](#프로젝트-진행-및-이슈-관리)
- [구현과정(설계 및 의도)](#구현과정(설계-및-의도))
- [TIL 및 회고](#til-및-회고)
- [References](#references)

<br/>


## 개요
본인의 위치 또는 특정 위치에서 반경 이내에 있는 맛집을 추천해주는 서비스
<br/>


## Skils
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/JAVA-17-blue) ![Static Badge](https://img.shields.io/badge/SpringBoot-3.1.5-green)<br/>
데이터베이스: ![Static Badge](https://img.shields.io/badge/MySQL--red) ![Static Badge](https://img.shields.io/badge/Redis--red)<br/>
테스트 데이터베이스: ![Static Badge](https://img.shields.io/badge/H2--red)
<br/>

## Teams

| 팀원      | 담당                                              |
|---------|-------------------------------------------------|
| 신민석(팀장) | 사용자 회원가입, 로그인, 설정 업데이트, 조회, 시군구 목록, 맛집 상세 조회 캐싱 |
| 김나윤     | 맛집 평가 생성, 점심 추천 Discord Webhook                 |
| 남세원     | 시군구 목록 조회, 맛집 목록, 상세 조회                         |
| 원정연     | 공공 데이터 파이프라인 (수집, 전처리, 저장, 스케쥴링)                |


<br/>

## Running Tests

> ![Static Badge](https://img.shields.io/badge/Test_Passed-51/53-green) <br/>
![test](https://github.com/wanted-preonboarding-backend-teamV/Restaurant-Recommendation/assets/83534757/96e99a9f-e2f4-41e4-a34f-3e8310c700f9)
> - API 테스트는 성공했으나 Github Actions CI에서 Redis 연결을 하지 못해 Redis 연결된 2개의 테스트만 실패
> - 추후 Docker 활용해서 CI에도 Redis 구현 예정
> 
<br/>


## API Reference
[API 명세서](https://wonwonjung.notion.site/API-2f04e83f36e349159ccc476d3ea869f3?pvs=4)

<details>
<summary>Member</summary>

#### 회원가입

POST /members

| 바디       | 타입     | 설명   |
|:---------|:-------|:-----|
| account  | string | 계정   |
| password | string | 비밀번호 |

#### Response

    HTTP/1.1 200
    Content-Type: application/json

#### 로그인

POST /members/login

| 바디     | 타입     | 설명   |
|:---------|:-------|:-----|
| account  | string | 계정   |
| password | string | 비밀번호 |

#### Response

    HTTP/1.1 200
    Content-Type: application/json

    {
        "accessToken": "12412fd12fdksr.142fdadafs.rea2r23r23f"
    }

#### 설정 업데이트

PATCH /members

| 바디      | 타입      | 설명       |
|:----------|:--------|:---------|
| lat       | double  | 위도       |
| lon       | double  | 경도       |
| recommend | boolean | 점심 추천 여부 |

#### Response

    HTTP/1.1 204
    Content-Type: application/json

   ``` json
   {
	"id": 1,
	"account": "test1234",
	"lat": 35.123251,
	"lon": 127.231049,
	"recommend": true
}
   ```

#### 정보 조회

GET /members

#### Response

    HTTP/1.1 204
    Content-Type: application/json

   ``` json
   {
	"id": 1,
	"account": "test1234",
	"lat": 35.123251,
	"lon": 127.231049,
	"recommend": true
}
   ```

</details>
<details>
<summary>Restaurant</summary>

#### 시군구 목록 조회

GET /restaurants/district

#### Response

    HTTP/1.1 204
    Content-Type: application/json

``` json
[
  {
    "do-si": "경기",
    "sgg": "광명시",
    "lat": 35.123412,
    "lon": 127.123451
  },
  {
    "do-si": "경기",
    "sgg": "수원시",
    "lat": 35.123512,
    "lon": 127.122351
  },
  {
    "do-si": "경기",
    "sgg": "가평군",
    "lat": 35.126512,
    "lon": 127.133251
  },
  ...
]
```

#### 맛집 목록 조회

GET /restaurants

**Query Paramter**

| 파라미터   | 타입     | 설명                                                         |
|:-------|:-------|:-----------------------------------------------------------|
| lat    | string | default : 필수값, 지구 y축 원점 기준 거리                              |
| lon    | string | default : 필수값, 주기 x축 원점 기준 거리                              |
| range  | double | default : 필수값, km 를 소숫점으로 나타냅니다. 0.5 = 500m / 1.0 = 1000km |
| order  | string | default : distance, distance(거리순) 또는 rating(평점순)으로 정렬      |
| page   | int    | default : 0                                                |
| size   | int    | default : 10                                               |
| search | string | 사업자명 내에 키워드가 포함되면 목록에 포함                                   |
| filter | string | 위생업태명에 따른 필터링 (패스트푸드, 김밥(도시락), 중국식, 일식)                    |


#### Response

    HTTP/1.1 204
    Content-Type: application/json

``` json
[
	{
		"restaurantId": 10,
		"name": "대박김밥",
		"roadnameAddress": "경기도 용인시 처인구 양지면 양지리 633-1",
		"zipCode": "17158",
		"avgRating": 3.4,
		"distance": 0.5 // 단위 : km
		},
 	{
		"restaurantId": 11,
		"name": "김밥",
		"roadnameAddress": "경기도 용인시 처인구 양지면 양지리 633-2",
		"zipCode": "17159",
		"avgRating": 3.4,
		"distance": 0.5 // 단위 : km
		},
    ...
]
```

#### 맛집 상세 조회

GET /restaurants/{restaurant_id}

#### Request

| 파라미터   | 타입     | 설명                                                        |
|:--------------|:-----|:----------------------------------------------------------|
| restaurant_id | long | 맛집 ID                                                     |
 

#### Response

```json
  {
	"restaurantId": 10,
	"name": "대박김밥",
	"sigun": "용인시",
	"type": "김밥(도시락)",
	"roadnameAddress": "경기도 용인시 처인구 양지면 양지리 633-1",
	"lotAddress": "경기도 용인시 처인구 양지면 양지로 111",
	"zipCode": "17158",
	"lat": 37.2346508041,
	"lon": 127.2805685812,
	"avgRating": 3.4,
	"ratings": [
		{
			"memberId": 1,
			"memberAccount": "hello",
			"score": 5,
			"content": "맛있어요."
		},
		...
	]
  }
```

</details>

<details>

<summary>Rating</summary>

#### 맛집 평가 생성

POST /ratings

#### Request

```
  {
    "restaurantId": 11404,
    "score": 4,
    "content": "맛있어요!"
  }
```

#### Response

    HTTP/1.1 204

</details>

<br/>


## 프로젝트 진행 및 이슈 관리

![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
<img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
<img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white">

<br/>


## 구현과정(설계 및 의도)

[요구사항 분석 및 일정 관리](https://wonwonjung.notion.site/7169b5be3652485b82df0c1a2b639788?pvs=4)

<details>

<summary>MySQL Datetime vs Timestamp</summary>

- Timestamp는 인덱스가 더 빠르게 생성되는 대신, 날짜 범위가 1970년~2038년 이내라서 더 이전 또는 이후의 데이터를 저장할 수 없다.
- 인허가일자 컬럼의 날짜가 1970년 이전인 경우가 있기 때문에 Datetime을 사용하였다.

</details>

<details>

<summary>맛집 테이블 인덱스 추가</summary>

- 데이터 파이프라인을 통해 데이터를 저장하기 전에 (사업장명, 도로명주소)로 중복을 확인한다. 이 과정에서 두 컬럼에 대한 검색이 매우 많이 발생하기 때문에 UNIQUE INDEX를 추가해주었다.

</details>

<details>

<summary>bulk update 시 중복 처리 문제</summary>

- bulk 데이터 내에 중복이 있는 경우에는 JPA 등을 통해 체크할 수 없고 로직적으로 처리하거나 중복 되고나서 예외 처리 해야한다.
- 따라서 INSERT IGNORE INTO … 구문을 사용하여 중복된 row는 무시되도록 했다.

</details>

<details>

<summary>Redis 캐싱</summary>

- 복잡한 내용을 저장하는 것이 아닌 기존에 있던 목록 또는 정보를 저장
- RedisTemplate 또는 RedisRepository를 사용하지 않고 @Cacheable 어노테이션으로 캐싱 구현

</details>

<details>

<summary>평균 평점 저장</summary>

- 처음 작성한 코드는 사용자가 새로운 평가를 생성할 때마다 평균 평점을 계산하여 Restaurant 테이블에 저장했다.
- 평점의 경우 실시간 성이 아주 중요한 부분이 아니기 때문에 비동기적으로 처리하여 사용자가 평가 API를 사용할 때 평점이 계산되는 시간을 기다리지 않을 수 있게 하는 것이 더 좋겠다고 생각했다.
- 그래서 RatingAsyncService, ScoreCalculationQueue 클래스를 생성하여 로직을 분리했다.
  - ScoreCalculationQueue : 평가가 생성되면 큐에 저장되고, RatingAsyncService의 메서드가 작동하면 하나씩 제거한다.
  - RatingAsyncService : Queue에 들어있는 평가들을 탐색하여 평균 평점을 30초마다 비동기적으로 처리한다.

</details>

<br/>

## TIL 및 회고

- [연관 데이터 없을 때 조회하기 (Join vs Left Join)](https://wonwonjung.notion.site/Join-vs-Left-Join-0df2958dbbd7488a99ba1ddf2ac6c7bc?pvs=4)
- [@Modifying 어노테이션](https://wonwonjung.notion.site/Modifying-bb318f67700a4260be07bbd68a8c83cb?pvs=4)
- [JPA의 IDENTITY 전략과 Bulk Insert](https://wonwonjung.notion.site/JPA-IDENTITY-Bulk-Insert-8c3f6783defb4371be28accca35e5227?pvs=4)
- [Redis 시작하기](https://wonwonjung.notion.site/Redis-ad1cc11ef6ca46b5a6c13bc877552a12?pvs=4)

<br/>

## References

- [[Redis] Windows 환경에서 Redis 설치 및 실행](https://velog.io/@jinyngg/Redis-%EC%9C%88%EB%8F%84%EC%9A%B0-Redis-%EC%84%A4%EC%B9%98)
- [[Redis] SpringBoot Data Redis 로컬/통합 테스트 환경 구축하기](https://jojoldu.tistory.com/297)
- [JPA의 Batch Insert](https://jaehun2841.github.io/2020/11/22/2020-11-22-spring-data-jpa-batch-insert)
- [WebClient로 OpenApi 데이터 수집하기](https://velog.io/@jmjmjmz732002/Spring-외부-API-통신-리팩토링을-하게-된-이유#webclient-기본-이용법)
- [Spring Scheduler 테스트하기](https://silvergoni.tistory.com/entry/use-awaitility를-사용하여-딜레이-테스트하기)
- [스프링에서 @Async로 비동기처리하기](https://springboot.tistory.com/38)



