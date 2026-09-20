# Bai tap thuc hanh 1 - Fallback Pattern bao ve giao dien trang chu

## 1. Boi canh

Trang chu StoreX goi Marketing-Service de lay danh sach "Flash Voucher". Neu Marketing-Service dang bao tri ma StoreX-BFF khong co fallback, loi ket noi se day len controller va frontend co the nhan HTTP 500.

Giai phap la dung Circuit Breaker kem fallback. Khi Marketing-Service loi hoac circuit breaker mo mach, StoreX-BFF tra ve danh sach voucher mac dinh de trang chu van hien thi duoc.

## 2. Quy tac chu ky fallback

Neu method goc la:

```java
public List<VoucherResponse> getFlashVouchers()
```

fallback hop le phai:

- Cung kieu tra ve: `List<VoucherResponse>`.
- Cung tham so voi method goc. Method goc khong co tham so nen fallback cung khong co tham so nghiep vu.
- Them tham so cuoi cung la `Throwable` de bat moi loi.

Chu ky dung:

```java
public List<VoucherResponse> fallbackFlashVouchers(Throwable throwable)
```

Neu method goc co tham so, vi du `getFlashVouchers(String userId)`, fallback phai la:

```java
public List<VoucherResponse> fallbackFlashVouchers(String userId, Throwable throwable)
```

## 3. Code da thuc hien

File chinh:

- `src/main/java/com/example/b1ss13/service/VoucherService.java`
- `src/main/java/com/example/b1ss13/controller/HomeController.java`
- `src/main/java/com/example/b1ss13/config/RestTemplateConfig.java`

Annotation:

```java
@CircuitBreaker(name = "voucherCircuitBreaker", fallbackMethod = "fallbackFlashVouchers")
public List<VoucherResponse> getFlashVouchers() {
    ...
}
```

Fallback:

```java
public List<VoucherResponse> fallbackFlashVouchers(Throwable throwable) {
    log.warn("Khong lay duoc Flash Voucher tu Marketing-Service, dung voucher mac dinh. Nguyen nhan: {}",
            throwable.toString());

    return List.of(new VoucherResponse("DEFAULT_FREESHIP", "Ma Freeship 15K", 15_000));
}
```

URL Marketing-Service duoc cau hinh sai co chu dich:

```yaml
storex:
  marketing-voucher-url: http://localhost:9999/api/flash-vouchers
```

Vì port `9999` khong co service, request se loi va kich hoat fallback.

## 4. Cach chay

```bash
cd b1ss13
..\gradlew.bat bootRun
```

Goi API:

```bash
curl "http://localhost:8080/home/vouchers"
```

Output ky vong, HTTP Status 200 OK:

```json
[
  {
    "code": "DEFAULT_FREESHIP",
    "title": "Ma Freeship 15K",
    "discountAmount": 15000
  }
]
```

## 5. Checklist

- Fallback co cung kieu tra ve voi method goc: `List<VoucherResponse>`.
- Method goc khong co tham so, fallback chi them tham so cuoi `Throwable`.
- Khi Marketing-Service loi, frontend van nhan `DEFAULT_FREESHIP` voi HTTP 200 OK thay vi loi 500.
