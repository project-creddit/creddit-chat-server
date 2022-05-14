# API Guide

## Chatting API
### 모든 채팅방 리스트를 가져오는 API
| 메소드 |                     경로                     |
| :----: | :------------------------------------------: |
| `GET` | `REACT_APP_CHAT_API_BASE_URL/${userId}/chatrooms` |

#### Request Header

|     변수      |  타입  | 설명               |
| :-----------: | :----: | ------------------ |
| Authorization | string | 인증 헤더 (Bearer) |

#### Response
```json
[
    {
        "id": "2ee8f7d7-c589-4f3d-85d7-cf2ec0b90fb3",
        "target": "pp22",
        "users": [
            {
              "nickname": "qq22",
              "introduction": null,
              "image": {
                "imgName": null,
                "imgUrl": null
              }
            },
            {
              "nickname": "qq33",
              "introduction": null,
              "image": {
                "imgName": null,
                "imgUrl": null
              }
            }
        ],
        "leftUsers": [],
        "messages": [
            {
                "sender": "pp22",
                "receiver": "pp11",
                "message": "sdf",
                "createdDate": "1651434316329"
            },
            {
                "sender": "pp11",
                "receiver": "pp22",
                "message": "뭔데",
                "createdDate": "1651434333270"
            },
            {
                "sender": "pp22",
                "receiver": "pp11",
                "message": "그게 뭐",
                "createdDate": "1651434341710"
            },
            {
                "sender": "pp22",
                "receiver": "pp11",
                "message": "zlzl",
                "createdDate": "1651434392891"
            },
            {
                "sender": "pp22",
                "receiver": "pp11",
                "message": "ddd",
                "createdDate": "1651434517168"
            }
        ]
    }
]
```

#### example
```js
axios({
      method: "get",
      url:
        process.env.REACT_APP_CHAT_API_BASE_URL + "/" + userId + "/chatrooms",
    }).then((response) => {
      console.log("response: ", response);
      for (const key in response.data) {
        dispatch(
          createChatRoom({
            targetUser: response.data[key].target,
            messages: [...response.data[key].messages],
          })
        );
      }
    });
```

### 채팅을 할 유저를 등록하고 채팅방 생성 API

|  메소드   |                          경로                          |
|:------:|:----------------------------------------------------:|
| `POST` | `REACT_APP_CHAT_API_BASE_URL/chat/register` |

#### Request Header

|     변수      |  타입  | 설명               |
| :-----------: | :----: | ------------------ |
| Authorization | string | 인증 헤더 (Bearer) |

#### Request Body
```json
{
    "myId": "qq22",
    "userId":"qq33"
}
```

<br />

### ChatRoom 조회 API
| 메소드 |                               경로                                |
| :----: |:---------------------------------------------------------------:|
| `GET` | `REACT_APP_CHAT_API_BASE_URL/${userId}/chatrooms/${chatRoomId}` |

#### Response
```json
{
    "id": "2ee8f7d7-c589-4f3d-85d7-cf2ec0b90fb3",
    "target": "qq33",
    "users": [
        {
            "nickname": "qq22",
            "introduction": null,
            "image": {
                "imgName": null,
                "imgUrl": null
            }
        },
        {
            "nickname": "qq33",
            "introduction": null,
            "image": {
                "imgName": null,
                "imgUrl": null
            }
        }
    ],
    "leftUsers": [],
    "messages": []
}
```

<br />

### ChatRoom 나가기 API
|  메소드   |                             경로                             |
|:------:|:----------------------------------------------------------:|
| `POST` | `REACT_APP_CHAT_API_BASE_URL/chat/${userId}/chatroom/left` |

#### Request Header

|     변수      |  타입  | 설명               |
| :-----------: | :----: | ------------------ |
| Authorization | string | 인증 헤더 (Bearer) |

#### Request Body
```json
{
    "id": "2ee8f7d7-c589-4f3d-85d7-cf2ec0b90fb3",
    "target": "qq33",
    "users": [
        {
            "nickname": "qq22",
            "introduction": null,
            "image": {
                "imgName": null,
                "imgUrl": null
            }
        },
        {
            "nickname": "qq33",
            "introduction": null,
            "image": {
                "imgName": null,
                "imgUrl": null
            }
        }
    ],
    "leftUsers": [
        "qq22"
    ],
    "messages": []
}
```

<br />

## User API
- Deprecated 될 API 입니다! 임시로 유저 생성이 필요하기 때문에 만들어놓은 API 이므로 기존의 main server의 User API를 사용하시면 됩니다!
### Login 및 회원가입 API
| 메소드 |                     경로                     |
| :----: | :------------------------------------------: |
| `POST` | `REACT_APP_USER_BASE_URL/register` |
#### Request Header
|     변수      |  타입  | 설명               |
| :-----------: | :----: | ------------------ |
| Authorization | string | no token |
#### Request Body
```json
{
    "id": "$userName",
    "name": "$userName",
    "password": "$userPassword"
}
```
#### Example
```js
 axios({
    method: "post",
    url: process.env.REACT_APP_USER_BASE_URL + "/register",
    data: dataToSubmit,
  }).then((response) => {
    if (response.data) {
      alert("유저 임시 가입 완료!!");
      window.sessionStorage.setItem("userId", dataToSubmit.id);
      window.sessionStorage.setItem("login", true);
    }
  });
```