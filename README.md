# Main dosyası 'Soru5againApplication' sprinbootapp olarak çalıştırıp uygulamayı test edebilirsiniz.

## Kullanılan Teknolojiler
- **Java:** Programlama dili olarak kullanılmıştır.
- **Spring Boot:** Uygulama çatısı olarak tercih edilmiştir.
- **Hibernate/JPA:** Veritabanı işlemleri için ORM kullanılmıştır.
- **H2:** Geliştirme ve test aşamaları için hafif bellek içi veritabanı olarak kullanılmıştır.
- **Maven:** Proje yönetimi ve bağımlılık yönetimi için kullanılmıştır.

## Model:
- **BaseEntity:** Temel varlık sınıfı.
- **Order:** Siparişleri temsil eden sınıf.
- **OrderItem:** Sipariş kalemlerini temsil eden sınıf.
- **Product:** Ürünleri temsil eden sınıf.
- **Cart:** Sepeti temsil eden sınıf.
- **CartItem:** Sepet öğelerini temsil eden sınıf.

## Repository:
- **OrderRepository:** Siparişler için veritabanı işlemleri.
- **OrderItemRepository:** Sipariş kalemleri için veritabanı işlemleri.
- **ProductRepository:** Ürünler için veritabanı işlemleri.
- **OrderItemPriceRepository:** Sipariş kalemi fiyatları için veritabanı işlemleri.
- **CartRepository:** Sepetler için veritabanı işlemleri.
- **CustomerRepository:** Müşteriler için veritabanı işlemleri.

## Service:
- **OrderService:** Siparişlerle ilgili iş mantığını içeren sınıf.
- **CartService:** Sepetlerle ilgili iş mantığını içeren sınıf.
- **CustomerService:** Müşterilerle ilgili iş mantığını içeren sınıf.
- **ProductService:** Ürünlerle ilgili iş mantığını içeren sınıf.

## Controller:
- **OrderController:** Siparişlerle ilgili REST API uç noktalarını yöneten sınıf.
- **CartController:** Sepetlerle ilgili REST API uç noktalarını yöneten sınıf.
- **CustomerController:** Müşterilerle ilgili REST API uç noktalarını yöneten sınıf.
- **ProductController:** Ürünlerle ilgili REST API uç noktalarını yöneten sınıf.

## Kullanım Talimatları
### Uygulamanın Başlatılması:
Proje, Spring Boot uygulaması olarak başlatılır. Gerekli bağımlılıklar ve yapılandırmalar Maven kullanılarak yapılır.

### H2 Veritabanı Yapılandırması:
H2 veritabanı, application.properties dosyasında yapılandırılmıştır. Veritabanı bağlantı bilgileri ve konsol erişimi burada belirlenmiştir.

### H2 Konsolu Kullanımı:
Veritabanı yapılarını ve verileri incelemek için H2 Konsolu kullanılabilir. Konsola erişim [http://localhost:8080/h2-console](http://localhost:8080/h2-console) adresinden sağlanır. Varsayılan URL jdbc:h2:mem:testdb, kullanıcı adı sa, şifre: password

### API Kullanımı:
API uç noktaları, sipariş oluşturma, güncelleme, silme ve getirme işlemleri için kullanılabilir.

