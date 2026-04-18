# CommediaDellarte

**Bukkit/Paper 기반 Minecraft 플러그인 개발 프레임워크 라이브러리**

플레이어·엔티티·월드 래퍼, 영속 데이터 스토리지, GUI, 인터랙티브 오브젝트, 에리어 이벤트, 타이머 등 반복되는 보일러플레이트를 추상화하여 빠른 플러그인 개발을 지원합니다.

---

## 목차

- [특징](#특징)
- [요구사항](#요구사항)
- [설치](#설치)
- [빠른 시작](#빠른-시작)
- [핵심 API](#핵심-api)
  - [진입점 — CommediaDellarte](#1-진입점--commediadellartejavasingleentry)
  - [엔티티 래퍼 — A_Player / A_Entity](#2-엔티티-래퍼)
  - [데이터 스토리지 — IDataStroage](#3-데이터-스토리지)
  - [데이터 컨테이너 — A_DataMap / CoolTimeMap](#4-데이터-컨테이너)
  - [GUI 시스템 — CustomGui / Button](#5-gui-시스템)
  - [아이템 빌더 — ItemBuilder](#6-아이템-빌더)
  - [인터랙티브 시스템](#7-인터랙티브-시스템)
  - [월드 및 에리어 시스템](#8-월드-및-에리어-시스템)
  - [타이머 — Timer / BossBarTimer](#9-타이머)
  - [커맨드 — AbstractCommand](#10-커맨드)
  - [이벤트 — FirstLoadEvent](#11-이벤트--firstloadevent)
  - [직렬화 — RegisterSerializable](#12-직렬화)
- [패키지 구조](#패키지-구조)
- [라이선스](#라이선스)

---

## 특징

- **단일 진입점** — `CommediaDellarte` 정적 클래스 하나로 모든 기능 접근
- **엔티티/월드 래퍼** — Bukkit 엔티티·플레이어·월드를 플러그인 범위 데이터맵·쿨타임과 함께 래핑
- **플러그인 범위 데이터 스토리지** — 비동기 저장/로드, `NamespacedKey` 기반 격리
- **커스텀 GUI** — `CustomGui` + `Button` 람다로 인벤토리 UI를 선언적으로 구성
- **인터랙티브 시스템** — `PersistentData` 기반으로 아이템/타일(TileState)에 동작(Act)을 바인딩
- **에리어 이벤트** — BoundingBox 영역 내에서 발생하는 Bukkit 이벤트를 래핑하여 ~115종 발행
- **타이머** — 틱 기반 `Timer` / BossBar 자동 연동 `BossBarTimer`
- **쿨타임 관리** — `CoolTimeMap`으로 초·분·시간 단위 쿨타임을 간단하게 처리
- **채팅 입력 훅** — `setPlayerChatRunnable`로 플레이어의 다음 채팅을 일회성 입력으로 캡처
- **직렬화 확장** — `RegisterSerializable`로 커스텀 클래스를 `A_DataMap`에 저장/로드

---

## 요구사항

| 항목 | 버전 |
|------|------|
| Java | 17+ |
| Spigot / Paper API | 1.17+ |
| 선택 의존성 | Vault |

---

## 설치

### Maven

`library` 모듈을 컴파일 의존성으로 추가하고, `main` 모듈(플러그인 jar)은 서버에 설치합니다.

**pom.xml**

```xml
<repositories>
    <repository>
        <id>maven-snapshots</id>
        <url>https://nexus.redkiller.org/repository/maven-snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.red.minecraft.dellarte</groupId>
        <artifactId>library</artifactId>
        <version>3.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**plugin.yml** (소프트 의존성 또는 의존성 선언)

```yaml
depend:
  - CommediaDellarte
```

---

## 빠른 시작

```java
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;

@EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    A_Player aPlayer = CommediaDellarte.getAPlayer(player);

    // 플러그인 범위 쿨타임 체크
    CoolTimeMap coolTime = aPlayer.getCoolTime(plugin);
    if (!coolTime.checkCoolTime("my_skill")) {
        player.sendMessage("쿨타임 " + coolTime.getLessCoolTime("my_skill") + "초 남음");
        return;
    }
    coolTime.setCoolTime("my_skill", 10.0);  // 10초 쿨타임 설정

    // 플러그인 범위 데이터 읽기/쓰기
    A_DataMap data = aPlayer.getDataMap(plugin);
    int count = data.getInt("interact_count");
    data.set("interact_count", count + 1);
}
```

---

## 핵심 API

### 1. 진입점 — `CommediaDellarte` (Static Entry)

`org.red.minecraft.dellarte.library.CommediaDellarte`

모든 기능에 접근하는 단일 정적 진입점입니다.

```java
// 플레이어 래퍼
A_Player aPlayer = CommediaDellarte.getAPlayer(player);
A_Player aPlayer = CommediaDellarte.getAPlayer(uuid);           // @Nullable
A_Player aPlayer = CommediaDellarte.getAPlayer(name);           // @Nullable
A_OfflinePlayer aOffline = CommediaDellarte.getAOfflinePlayer(offlinePlayer);

// 엔티티 래퍼
A_Entity aEntity = CommediaDellarte.getAEntity(entity);
A_LivingEntity aLiving = CommediaDellarte.getALivingEntity(livingEntity);

// 월드 래퍼
A_World aWorld = CommediaDellarte.getAWorld(world);
A_World aWorld = CommediaDellarte.getAWorld(worldName);         // @Nullable
A_World aWorld = CommediaDellarte.getAWorld(worldUUID);         // @Nullable

// 데이터 스토리지
IDataStroage storage = CommediaDellarte.getStorage(namespacedKey);
boolean exists = CommediaDellarte.containStorage(namespacedKey);

// 인터랙티브 매니저
InteractiveManager<ItemStack> itemManager = CommediaDellarte.getInteractiveManager(ItemStack.class);
InteractiveManager<TileState> tileManager = CommediaDellarte.getInteractiveManager(TileState.class);

// 타이머
Timer timer = CommediaDellarte.createTimer(key, maxTimeTicks, onEndRunnable);
BossBarTimer bossTimer = CommediaDellarte.createBossBarTimer(key, maxTimeTicks, runnable, bossBar);

// 직렬화 클래스 등록
CommediaDellarte.registerSerializableClass(MyClass.class, new MySerializable());
```

---

### 2. 엔티티 래퍼

#### 계층 구조

```
A_DataHolder
  └── A_Entity          (Entity 래퍼, 데이터맵·쿨타임 포함)
        └── A_LivingEntity
              └── A_Player     (온라인 플레이어)
                    └── A_NPC  (Citizens NPC — hasMetadata("NPC") 체크)
```

#### `A_Entity` 주요 메서드

```java
Entity getEntity();                       // 원본 Bukkit Entity
A_DataMap getDataMap();                   // 전역 데이터맵
A_DataMap getDataMap(Plugin plugin);      // 플러그인 범위 데이터맵
CoolTimeMap getCoolTime();                // 전역 쿨타임맵
CoolTimeMap getCoolTime(Plugin plugin);   // 플러그인 범위 쿨타임맵
void dropNaturally(ItemStack... items);
String getUniqueIdStr();                  // UUID → String 편의 메서드
```

#### `A_Player` 추가 메서드

```java
// 인벤토리
void delayOpenInventory(CustomGui gui);                         // 1틱 후 열기
InventoryView openInventory(CustomGui gui, boolean ignoreEvent);

// 반복 태스크 등록
void addPlayerRunnable(NamespacedKey key, Runnable runnable, int delay);
void removePlayerRunnable(NamespacedKey key);

// 채팅 입력 캡처 (다음 채팅 한 번을 입력으로 처리)
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key);
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key, boolean sync);

// 상태 플래그
boolean getPlayerStatus(PlayerStatus status);
void setPlayerStatus(PlayerStatus status, boolean bool);
// PlayerStatus: IgnoreInvClose, ChatEvent

// 스킨 변경
void setSkin(String skin, String signature);
void setSkin(OfflinePlayer player);
void resetSkin();

// 아이템 추가 (넘치면 자동 드롭)
void addItemNature(ItemStack... items);

// 기타
ItemStack getPlayerSkull();
boolean isNPC();
BlockState lastBreakBlock();
BlockState lastPlaceBlock();
```

---

### 3. 데이터 스토리지

`org.red.minecraft.dellarte.library.data.IDataStorage`

`config.yml`의 `data-storage` 섹션에 `plugin:type` 키로 등록된 스토리지입니다.
기본으로 `plugin:player`, `plugin:entity`, `plugin:world` 타입이 자동 생성됩니다.

```java
NamespacedKey key = new NamespacedKey("myplugin", "player");
IDataStroage storage = CommediaDellarte.getStorage(key);

String id = player.getUniqueId().toString();   // UUID 문자열을 키로 사용하는 것이 관례

A_DataMap data = storage.getDataMap(id);
CoolTimeMap coolTime = storage.getCoolTimeMap(id);

boolean isLoaded = storage.loadedData(id);    // 메모리에 올라와 있는지
boolean hasSaved = storage.containData(id);   // 저장된 데이터가 있는지

storage.loadData(id);   // 비동기 로드
storage.saveData(id);   // 비동기 저장
storage.deleteData(id); // 삭제
storage.saveAll();
storage.loadAll();
```

---

### 4. 데이터 컨테이너

#### `A_DataMap`

`org.red.minecraft.dellarte.library.util.A_DataMap` — Minecraft 확장 DataMap, 체이닝 API 지원

```java
A_DataMap map = aPlayer.getDataMap(plugin);

// 읽기
String name     = map.getString("key");
int count       = map.getInt("count");
double val      = map.getDouble("val");
boolean flag    = map.getBoolean("flag");
ItemStack item  = map.getItemStack("item");
Location loc    = map.getLocation("loc");
Vector vec      = map.getVector("vec");
BoundingBox box = map.getBoundingBox("box");
A_DataMap sub   = map.getDataMap("nested");

// 쓰기 (체이닝 가능)
map.set("key", value);

// 점 표기법으로 중첩 값 탐색
Object value = map.finder("nested.location.x");
```

#### `CoolTimeMap`

`org.red.minecraft.dellarte.library.util.CoolTimeMap`

```java
CoolTimeMap coolTime = aPlayer.getCoolTime(plugin);

// 설정 (기본 단위: 초)
coolTime.setCoolTime("skill", 5.0);
coolTime.setCoolTime("skill", 3, CoolTimeMap.TimeType.MINUTE);

// 체크: 쿨타임 완료 → true, 남아 있음 → false
boolean ready = coolTime.checkCoolTime("skill");

// 남은 시간 조회
double remaining = coolTime.getLessCoolTime("skill");
double remaining = coolTime.getLessCoolTime("skill", CoolTimeMap.TimeType.MILLISECOND);

// 감소 / 제거
coolTime.reduceCoolTime("skill", 1.0);
coolTime.removeCoolTime("skill");

// TimeType: SECOND, MINUTE, HOUR, DAY, WEEK, YEAR, MILLISECOND
```

---

### 5. GUI 시스템

`org.red.minecraft.dellarte.library.inventory.CustomGui`

```java
// 생성
CustomGui gui = new CustomGui(54, "§8메뉴");
gui.setAllClickCancel(true);  // 모든 클릭 취소 (UI 전용)

// 버튼 등록
ItemStack closeBtn = ItemBuilder.createItem("§c닫기", Material.BARRIER);
gui.setItem(26, closeBtn, event -> {
    event.getWhoClicked().closeInventory();
});

// 여러 슬롯 동시 등록
gui.setItems(itemStack, button, 0, 1, 2);
gui.fillItem(0, 8, itemStack, button);  // 범위 채우기

// 이벤트 훅 오버라이드
CustomGui gui = new CustomGui(54, "제목") {
    @Override public void onClick(InventoryClickEvent e) { }
    @Override public void onClose(InventoryCloseEvent e) { }
    @Override public void onOpen(InventoryOpenEvent e) { }
};

// 열기
aPlayer.openInventory(gui);
aPlayer.openInventory(gui, true);   // InventoryCloseEvent 무시
aPlayer.delayOpenInventory(gui);    // 1틱 후 열기
```

---

### 6. 아이템 빌더

`org.red.minecraft.dellarte.library.item.ItemBuilder`

```java
// 체이닝 빌더
ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
    .setDisplayName("§6검")
    .setLore("첫째 줄", "둘째 줄")
    .setCustomModelData(1001)
    .setUnbreakable(true)
    .addEnchant(Enchantment.DAMAGE_ALL, 5, true)
    .addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 10, Operation.ADD_NUMBER, EquipmentSlot.HAND)
    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    .setPersistentDataContainer(key, PersistentDataType.STRING, "value")
    .build();

// 정적 팩토리
ItemStack item  = ItemBuilder.createItem("이름", Material.STONE);
ItemStack item  = ItemBuilder.createItem("이름", Material.STONE, "로어");
ItemStack item  = ItemBuilder.createItem("이름", Material.STONE, "로어", 1001 /*customModelData*/);
ItemStack air   = ItemBuilder.air();

// 두상 아이템
ItemStack skull = ItemBuilder.getSkull(offlinePlayer);
ItemStack skull = ItemBuilder.getSkullByUrl("https://textures.minecraft.net/...");
```

---

### 7. 인터랙티브 시스템

아이템(`ItemStack`) 또는 타일(`TileState`)에 동작(Act)을 바인딩하는 시스템입니다.
`PersistentData`를 사용해 마킹하므로 서버 재시작 후에도 유지됩니다.

#### `InteractiveItem` 구현

```java
public class MySword implements InteractiveItem {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(plugin, "my_sword");
    }

    public class OnHit implements InteractiveItem.HIT {
        @Override
        public void run(ItemStack item, Event event) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            // 처리
        }
    }
}
```

**지원 Act 종류 (InteractiveItem)**

| 내부 클래스 | 트리거 이벤트 |
|---|---|
| `LEFT_CLICK_AIR` | `PlayerInteractEvent` |
| `RIGHT_CLICK_AIR` | `PlayerInteractEvent` |
| `LEFT_CLICK_BLOCK` | `PlayerInteractEvent` |
| `RIGHT_CLICK_BLOCK` | `PlayerInteractEvent` |
| `PHYSICAL` | `PlayerInteractEvent` |
| `BREAK` | `BlockBreakEvent` |
| `FISH` | `PlayerFishEvent` |
| `HIT` | `EntityDamageByEntityEvent` |
| `DAMAGED` | `EntityDamageByEntityEvent` |
| `DROP` | `PlayerDropItemEvent` |
| `SWAP_HAND` | `PlayerSwapHandItemsEvent` |
| `DEATH` | `PlayerDeathEvent` |

**지원 Act 종류 (InteractiveTile)**

| 내부 클래스 | 트리거 이벤트 |
|---|---|
| `BREAK` | `BlockBreakEvent` |
| `LEFT_CLICK_BLOCK` | `PlayerInteractEvent` |
| `RIGHT_CLICK_BLOCK` | `PlayerInteractEvent` |

#### 매니저 사용

```java
InteractiveManager<ItemStack> itemManager =
    CommediaDellarte.getInteractiveManager(ItemStack.class);

// 등록
itemManager.registerInteractiveObj(mySword);

// 아이템에 마킹 (PersistentData 저장)
mySword.setInteractiveInObj(itemStack);
boolean has = mySword.isHasInteractive(itemStack);
mySword.removeInteractive(itemStack);
```

---

### 8. 월드 및 에리어 시스템

#### `A_World`

```java
A_World aWorld = CommediaDellarte.getAWorld(world);

// 에리어 등록
Area area = new InstanceArea("spawn", plugin, world,
    new Vector(x1, y1, z1), new Vector(x2, y2, z2));
aWorld.putArea(plugin, area);
aWorld.removeArea(plugin, area);

// 특정 좌표의 에리어 조회
List<Area> areas = aWorld.getAreas(plugin, location);
```

#### `InstanceArea`

```java
InstanceArea area = new InstanceArea("my_area", plugin, world,
    new Vector(0, 60, 0), new Vector(100, 80, 100));

area.contain(location);
area.contain(vector);
area.overlap(otherArea);
area.getBoundingBox();
List<Entity> entities = area.getEntities();
```

#### 에리어 이벤트

`aWorld.putArea(plugin, area)` 로 에리어를 등록하면, 해당 영역 내에서 발생하는 Bukkit 이벤트가 에리어 이벤트로 래핑되어 발행됩니다.

```java
@EventHandler
public void onAreaBlockBreak(AreaBlockBreakEvent event) {
    Area area = event.getArea();
    BlockBreakEvent original = event.getEvent();
}
```

**지원 에리어 이벤트 (~115종)**

| 패키지 | 내용 |
|---|---|
| `event.area.block.*` | 블록 관련 이벤트 (~25종) |
| `event.area.player.*` | 플레이어 관련 이벤트 (~40종) |
| `event.area.entity.*` | 엔티티 관련 이벤트 (~50종) |

---

### 9. 타이머

```java
NamespacedKey key = new NamespacedKey(plugin, "my_timer");

// 일반 타이머 (maxTime: 틱 단위, 200틱 = 10초)
Timer timer = CommediaDellarte.createTimer(key, 200, () -> {
    // 타이머 종료 시 실행
});
timer.start();
timer.stop();
timer.getTime();        // 현재 경과 틱
timer.setTime(ticks);
timer.addMaxTime(ticks);

// BossBar 타이머
BossBar bar = Bukkit.createBossBar("타이머", BarColor.GREEN, BarStyle.SOLID);
BossBarTimer bossTimer = CommediaDellarte.createBossBarTimer(key, 200, runnable, bar);
```

---

### 10. 커맨드

```java
public class MyCommand extends AbstractCommand {

    @Override
    public String getName() { return "mycommand"; }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        // 처리
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return List.of("option1", "option2");
        // 입력 중인 문자열로 자동 필터링됨 (A_Util.removeStringNotStartWith)
    }
}

// 등록
plugin.getCommand("mycommand").setExecutor(new MyCommand());
plugin.getCommand("mycommand").setTabCompleter(new MyCommand());
```

플레이어 전용 커맨드는 `AbstractPlayerCommand`를 사용합니다.

```java
public class MyPlayerCommand extends AbstractPlayerCommand {
    @Override
    public boolean onPlayerCommand(Player player, String label, String[] args) {
        return true;
    }
}
```

---

### 11. 이벤트 — `FirstLoadEvent`

플러그인이 모두 로드된 후 1틱 뒤에 발행되는 이벤트입니다.
다른 플러그인 의존성 초기화나 데이터 스토리지 접근은 여기서 수행하는 것이 안전합니다.

```java
@EventHandler
public void onFirstLoad(FirstLoadEvent event) {
    // 이 시점에 데이터 스토리지가 모두 생성되어 있음
    IDataStroage storage = CommediaDellarte.getStorage(myKey);
}
```

---

### 12. 직렬화

커스텀 클래스를 `A_DataMap`에 저장/로드할 수 있도록 등록합니다.
`ItemStack`, `Location`, `BoundingBox`, `Vector`는 기본 등록되어 있습니다.

```java
CommediaDellarte.registerSerializableClass(MyClass.class, new RegisterSerializable<MyClass>() {
    @Override
    public DataMap serialize(MyClass obj) {
        DataMap map = new DataMap();
        map.set("field", obj.getField());
        return map;
    }

    @Override
    public MyClass deserialize(DataMap map) {
        return new MyClass(map.getString("field"));
    }
});
```

---

## 패키지 구조

```
org.red.minecraft.dellarte.library
├── CommediaDellarte          ← 진입점 (정적 API)
├── IDellarteManager          ← 매니저 인터페이스
├── command/
│   ├── AbstractCommand
│   └── AbstractPlayerCommand
├── data/
│   └── IDataStroage
├── entity/
│   ├── A_Entity
│   ├── A_LivingEntity
│   ├── A_NPC
│   └── A_Player
├── event/
│   ├── FirstLoadEvent
│   ├── TimerEndEvent
│   ├── area/block/*          ← 에리어 블록 이벤트 (~25종)
│   ├── area/entity/*         ← 에리어 엔티티 이벤트 (~50종)
│   ├── area/player/*         ← 에리어 플레이어 이벤트 (~40종)
│   ├── listener/A_Listener
│   └── player/               ← PlayerLeftClickEvent, PlayerRightClickEvent 등
├── interactive/
│   ├── InteractiveManager
│   ├── InteractiveItem
│   └── InteractiveTile
├── inventory/
│   ├── CustomGui
│   └── Button
├── item/
│   └── ItemBuilder
├── user/
│   └── A_OfflinePlayer
├── util/
│   ├── A_DataHolder          ← getDataMap() / getCoolTime()
│   ├── A_DataMap
│   ├── A_Util
│   ├── BossBarTimer
│   ├── CoolTimeMap
│   ├── NamespaceMap
│   ├── PairData<T,V>
│   ├── PairKeyMap
│   ├── Timer
│   └── UUIDMap
└── world/
    ├── A_World
    ├── Area
    └── InstanceArea
```

---

## 라이선스

이 프로젝트는 [GNU General Public License v3.0](LICENSE) 하에 배포됩니다.

---

*Authors: Inok, RedKiller, Double*