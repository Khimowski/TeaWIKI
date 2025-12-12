# TeaWIKI - 教师评价系统

此部分为后端部分，前端部分请查看我的存储仓库(目前还未创建)

接口使用标准RESTful API

---
## ⚙️系统及其功能

- [x] 👤用户系统
    - [x] 创建，删除，修改和查询
    - [x] 访问他人界面
- [x] 🏷️认证系统
    - [x] 注册和登录
    - [x] 持久化认证
- [ ] 👥教师系统
    - [x] 创建，删除，修改和查询
    - [x] 教师搜索系统
    - [ ] 根据课程推荐教师
- [x] 📝评论系统
    - [x] 创建，删除，修改和查询
    - [x] 教师页面排序和显示
    - [x] 点赞和点踩
- [x] 📌课程系统
    - [x] 创建，删除，修改和查询
    - [x] 链接到教师

---
## 📚技术栈
### 🎯核心框架
- Java 21 - 编程语言
- Spring Boot 3.1.4 - 现代化应用框架

### 🌐Web层
- Spring MVC - Web请求处理
- Spring Validation - 参数校验

### 💾数据层
- MySQL 8.0.33 - 关系型数据库
- MyBatis-Plus 3.5.5 - 增强型ORM框架
- Redis - 缓存与session存储

### 🔐安全认证
- JWT - 无状态身份认证

### 🛠️开发及部署工具
- Maven - 项目构建工具
- Hutool & Lombok - 开发效率工具
- JUnit 5 - 单元测试框架
- OpenAPI 3 - 接口文档

---
## 🚀快速开始
### 📋环境要求
- JDK 21+
- MySQL 8.0.33+
- Redis 7.0+
- Maven 3.6+

### 📦克隆项目
```BASH
git clone git@github.com:ZRedTea/TeaWIKI.git
cd TeaWIKI
```