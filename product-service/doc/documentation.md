# Сервис по работе с товарами

## Описание
Сервис позволяет добавлять\редактировать\удалять товары в БД

### Основной стек
Java 17   
Spring 3.2.0   
H2

## Описание полей продукта
- id: идентификатор продукта; (Автогенерируется)
- name: наименование продукта (Обязательное поле)   
Наименование продукта - уникальное имя, содержащее в себе латинские символы и символы кириллицы, а так же арабские цифры и дефис;
- price: цена (Обязательное поле)   
Цена является дробным числом, число знаков дробной части - 4 знака после запятой;
- manufacturer: наименование производителя (Обязательное поле)   
Наименование производителя - уникальное имя, содержащее в себе латинские символы и символы кириллицы, а так же дефис;

## Описание REST API
### Получить список продуктов
- Request:   
```
GET: http://<host>:<port>/<context>/products
```
- Response:    
```
Http status: 200   
JSON:
[
    {
        "id":1,
        "name":"test_name1",
        "price":0.005,
        "manufacturer":"test_mfr1",
    },
    {
        "id":2,
        "name":"test_name2",
        "price":200.0256,
        "manufacturer":"test_mfr2"
    },
]
```
### Получить продукт по name
- Request:
```
GET: http://<host>:<port>/<context>/products/{name}   
name = "test_name"
```
- Response:
```
Http status: 200   
JSON:
{
    "id":1,
    "name":"test_name",
    "price":0.005,
    "manufacturer":"test_mfr"
}
```
### Создать продукт
- Request:
```
POST: http://<host>:<port>/<context>/products/create 
name = "test_name"   
JSON:   
{
    "name":"имя продукта",
    "price": цена,
    "manufacturer":"наименование производителя"
}
```
- Response:
```
Http status: 200   
JSON:
{
    "id":1,
    "name":"имя продукта",
    "price":цена,
    "manufacturer":"наименование производителя"
}
```
### Изменить продукт
- Request:
```
PUT: http://<host>:<port>/<context>/products/{name} 
name = "test_name"   
JSON:   
{
    "name":"новое имя продукта",
    "price": цена,
    "manufacturer":"наименование производителя"
}
```
- Response:
```
Http status: 200   
JSON:
{
    "id":1,
    "name":"новое имя продукта",
    "price":новая цена,
    "manufacturer":"наименование производителя"
}
```
### Удалить продукт
- Request:
```
DELETE: http://<host>:<port>/<context>/products/{name} 
name = "test_name"   
JSON:   
{
    "name":"новое имя продукта",
    "price": цена,
    "manufacturer":"наименование производителя"
}
```
- Response:
```
Http status: 200   
```