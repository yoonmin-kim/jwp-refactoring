# 키친포스

## 요구 사항

### 상품
-[ ] `상품`을 등록할 수 있다
  -[ ] `상품`의 가격은 0원 이상이어야 한다
-[ ] `상품`의 목록을 조회할 수 있다

### 메뉴 그룹
-[ ] `메뉴 그룹`을 등록할 수 있다
-[ ] `메뉴 그룹`의 목록을 조회할 수 있다

### 메뉴
-[ ] `메뉴`를 등록할 수 있다
  -[ ] `메뉴`에는 여러 `메뉴 상품`을 등록할 수 있다
  -[ ] 등록한 `메뉴 상품`은 존재해야 한다
  -[ ] `메뉴`의 가격은 0원 이상이어야 한다
  -[ ] `메뉴`의 가격은 `메뉴 상품`의 `금액`의 합을 초과할 수 없다
  -[ ] `메뉴 그룹`에 속해야 한다
-[ ] `메뉴` 목록을 조회할 수 있다

### 주문
-[ ] `주문`을 생성할 수 있다
  -[ ] `주문 항목`은 필수이다
  -[ ] `주문 항목`에는 `메뉴`와 수량이 속해있다
  -[ ] `주문 항목`에 속한 `메뉴`는 존재해야 한다
  -[ ] `주문 테이블`이 지정되어야 한다
  -[ ] 지정된 `주문 테이블`은 존재해야 한다
  -[ ] `주문 테이블`이 `빈 테이블`이면 주문할 수 없다
  -[ ] `주문 상태`는 `조리`로 생성한다
-[ ] `주문`의 목록을 조회할 수 있다
-[ ] `주문`의 `주문 상태`를 변경할 수 있다
  -[ ] 변경하려는 `주문`은 존재해야 한다
  -[ ] `주문 상태`가 `계산 완료` 인 `주문`은 상태를 변경할 수 없다

### 주문 테이블
-[ ] 개별 `주문 테이블`을 생성할 수 있다
-[ ] `주문 테이블`의 목록을 조회할 수 있다
-[ ] `주문 테이블`을 `빈 테이블`로 변경할 수 있다
  -[ ] 변경하려는 `주문 테이블`은 존재해야 한다
  -[ ] `단체 지정`된 `주문 테이블`은 변경할 수 없다
  -[ ] 등록된 `주문`의 `주문 상태`가 `계산 완료`인 경우에만 변경할 수 있다
-[ ] `주문 테이블`에 `방문한 손님 수`를 변경할 수 있다
  -[ ] `방문한 손님 수`는 0명 이상이어야 한다
  -[ ] 변경하려는 `주문 테이블`은 존재해야 한다
  -[ ] `주문 테이블`이 `빈 테이블`이면 변경할 수 없다

### 단체 지정
-[ ] 여러 `주문 테이블`을 `단체 지정`할 수 있다
  -[ ] `주문 테이블`은 2개 이상이어야 한다
  -[ ] `단체 지정`하려는 `주문 테이블`은 존재해야 한다
  -[ ] `주문 테이블`이 `빈 테이블`이 아니거나 이미 `단체 지정`되었다면 `단체 지정`할 수 없다
  -[ ] `주문 테이블`은 `단체 지정`되면 `빈 테이블`이 아니게 된다
-[ ] `단체 지정`을 해제할 수 있다
  -[ ] `단체 지정`된 `주문 테이블`에 등록된 `주문 상태`가 `계산 완료`인 경우에만 해제할 수 있다

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
