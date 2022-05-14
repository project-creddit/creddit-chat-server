# Chat Server
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Redis-F80000?style=for-the-badge&logo=Redis&logoColor=white">

## implementation
0. `Docker` 설치 

1. `Redis Server` 시작하기
```
docker run --name myredis -d -p 6379:6379 redis
```

2. 확인하기 -> myredis 있으면 완료
```
docker ps
```

3. ChatService 구동

## API Guide
- [Documentation](https://github.com/project-creddit/creddit-chat-server/blob/feature/profile-api/docs/API_GUIDE.md)