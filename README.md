### Tổng quan
![microservices.png](microservices.png)
### Công nghệ
1. Phần FE (xem [tại đây](https://github.com/Arkadian404/bookstore-fe))
2. Phần BE
- Ngôn ngữ: Java
- Framework: Spring Boot, Spring Data JPA, Spring Security
- Database: MySQL
- Caching: Redis
- Message Broker: Apache Kafka
- API Gateway: Spring Cloud Gateway
- Service Discovery: Eureka
- Công cụ: Docker, Maven, MailDev
### Chức năng
- User:
    - Đăng ký tài khoản
    - Đăng nhập
    - Quên mật khẩu
    - Đổi mật khẩu
    - Tìm kiếm sản phẩm
    - Xem, lọc sản phẩm
    - Xem chi tiết sản phẩm
    - Quản lý thông tin cá nhân
    - Quản lý giỏ hàng
    - Quản lý đơn hàng
    - Thanh toán đơn hàng (Stripe)
- Admin:
    - Đăng nhập
    - Quản lý sản phẩm và các thông tin liên quan
    - Quản lý đơn hàng
    - Quản lý người dùng