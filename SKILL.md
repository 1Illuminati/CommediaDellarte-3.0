# CommediaDellarte Library — Agent Skill Reference

이 문서는 **CommediaDellarte** 라이브러리를 코드에서 활용하기 위한 에이전트용 참조 문서입니다.

---

## 개요

CommediaDellarte는 **Bukkit/Paper 기반 Minecraft 플러그인 개발을 위한 프레임워크 라이브러리**입니다.
플레이어·엔티티·월드 래퍼, 데이터 스토리지, GUI, 인터랙티브 오브젝트, 에리어 이벤트, 타이머 등 반복되는 작업을 추상화합니다.

- **library 모듈** — 인터페이스와 공개 API 정의 (컴파일 의존성으로 참조)
- **main 모듈** — 구현체. CommediaDellarte 플러그인 자체

---

## 1. 진입점 — `CommediaDellarte` 정적 클래스

`org.red.minecraft.dellarte.library.CommediaDellarte`

모든 기능에 접근하는 단일 진입점입니다. 내부적으로 `IDellarteManager`에 위임합니다.

```java
// 플레이어 래퍼 획득
A_Player aPlayer = CommediaDellarte.getAPlayer(player);
A_Player aPlayer = CommediaDellarte.getAPlayer(uuid);      // @Nullable
A_Player aPlayer = CommediaDellarte.getAPlayer(name);      // @Nullable
A_Player aPlayer = CommediaDellarte.getAPlayer(offlinePlayer); // @Nullable (온라인만 반환)

// 오프라인 플레이어 래퍼 (항상 반환, UUID 기반 캐싱)
A_OfflinePlayer aOffline = CommediaDellarte.getAOfflinePlayer(offlinePlayer);

// 엔티티 래퍼
A_Entity aEntity = CommediaDellarte.getAEntity(entity);
A_LivingEntity aLiving = CommediaDellarte.getALivingEntity(livingEntity);

// 월드 래퍼
A_World aWorld = CommediaDellarte.getAWorld(world);
A_World aWorld = CommediaDellarte.getAWorld(worldName);    // @Nullable
A_World aWorld = CommediaDellarte.getAWorld(worldUUID);    // @Nullable

// 데이터 스토리지
IDataStroage storage = CommediaDellarte.getStorage(namespacedKey);
boolean exists = CommediaDellarte.containStorage(namespacedKey);

// 인터랙티브 매니저
InteractiveManager<ItemStack> manager = CommediaDellarte.getInteractiveManager(ItemStack.class);
InteractiveManager<TileState> manager = CommediaDellarte.getInteractiveManager(TileState.class);

// 타이머 생성
Timer timer = CommediaDellarte.createTimer(key, maxTimeTicks, onEndRunnable);
BossBarTimer bossBarTimer = CommediaDellarte.createBossBarTimer(key, maxTimeTicks, runnable, bossBar);

// 직렬화 클래스 등록 (플러그인 초기화 시 한 번)
CommediaDellarte.registerSerializableClass(MyClass.class, new MySerializable());
```

---

## 2. 엔티티 시스템

### 2-1. 계층 구조

```
A_DataHolder
  └── A_Entity        (Entity 래퍼, 데이터맵·쿨타임 포함)
        └── A_LivingEntity
              └── A_Player     (온라인 플레이어)
                    └── A_NPC  (Citizen NPC 등, hasMetadata("NPC") 체크)
```

### 2-2. `A_Entity` 주요 메서드

`org.red.minecraft.dellarte.library.entity.A_Entity`

```java
Entity getEntity();                     // 원본 Bukkit Entity
A_DataMap getDataMap();                 // 전역 데이터맵
A_DataMap getDataMap(Plugin plugin);    // 플러그인 범위 데이터맵
CoolTimeMap getCoolTime();              // 전역 쿨타임맵
CoolTimeMap getCoolTime(Plugin plugin); // 플러그인 범위 쿨타임맵
void dropNaturally(ItemStack... items);
@Nullable LivingEntity getLivingEntity();
@Nullable A_LivingEntity getALivingEntity();
String getUniqueIdStr();                // UUID → String 편의 메서드
```

### 2-3. `A_Player` 주요 추가 메서드

`org.red.minecraft.dellarte.library.entity.A_Player`

```java
// 플레이어 비교
boolean comparePlayer(Player player);
boolean comparePlayer(UUID uuid);
boolean comparePlayer(String name);

// 인벤토리 (이벤트 무시 옵션 포함)
void delayOpenInventory(Inventory inv);
void delayOpenInventory(CustomGui gui);
void delayOpenInventory(Inventory inv, boolean ignoreInvCloseEvent);
InventoryView openInventory(CustomGui gui);
InventoryView openInventory(CustomGui gui, boolean ignoreEvent);

// 커스텀 Runnable 관리 (반복 태스크)
void addPlayerRunnable(NamespacedKey key, Runnable runnable, int delay);
void removePlayerRunnable(NamespacedKey key);
boolean hasPlayerRunnable(NamespacedKey key);

// 채팅 이벤트 훅
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key);
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key, boolean sync);
// PlayerChatRunnable: void run(AsyncPlayerChatEvent event)

// 플레이어 상태 플래그
boolean getPlayerStatus(PlayerStatus status);
void setPlayerStatus(PlayerStatus status, boolean bool);
void switchPlayerStatus(PlayerStatus status);
// PlayerStatus enum: IgnoreInvClose, ChatEvent

// 스킨 변경
void setSkin(String skin, String signature);
void setSkin(OfflinePlayer player);
void resetSkin();
boolean isChangedSkin();

// 기타 편의 메서드
ItemStack getPlayerSkull();
void addItemNature(ItemStack... items);  // 자연스러운 아이템 추가 (넘치면 드롭)
boolean isNPC();
A_OfflinePlayer getAOfflinePlayer();
BlockState lastBreakBlock();
BlockState lastPlaceBlock();
```

---

## 3. 데이터 스토리지 — `IDataStroage`

`org.red.minecraft.dellarte.library.data.IDataStorage`

config.yml의 `data-storage` 섹션에서 플러그인:타입 키로 등록된 스토리지.
기본으로 `plugin:player`, `plugin:entity`, `plugin:world` 타입이 자동 생성됩니다.

```java
NamespacedKey key = new NamespacedKey("myplugin", "player");
IDataStroage storage = CommediaDellarte.getStorage(key);

// UUID 문자열을 키로 사용하는 것이 관례
String id = player.getUniqueId().toString();

A_DataMap data = storage.getDataMap(id);
CoolTimeMap coolTime = storage.getCoolTimeMap(id);

boolean isLoaded = storage.loadedData(id);  // 메모리에 올라와 있는지
boolean hasSaved = storage.containData(id); // 저장된 데이터가 있는지

storage.loadData(id);    // 비동기 로드
storage.saveData(id);    // 비동기 저장
storage.deleteData(id);  // 삭제
storage.saveAll();       // 전체 저장
storage.loadAll();       // 전체 로드
```

---

## 4. 데이터 컨테이너 — `A_DataMap` / `CoolTimeMap`

### 4-1. `A_DataMap`

`org.red.minecraft.dellarte.library.util.A_DataMap`
`DataMap`의 Minecraft 확장. 체이닝 API 지원.

```java
A_DataMap map = entity.getDataMap();

// 읽기
String name = map.getString("key");
int count = map.getInt("count");
double val = map.getDouble("val");
boolean flag = map.getBoolean("flag");
ItemStack item = map.getItemStack("item");
Location loc = map.getLocation("loc");
Vector vec = map.getVector("vec");
BoundingBox box = map.getBoundingBox("box");
A_DataMap sub = map.getDataMap("nested");

// 쓰기 (체이닝 가능)
map.set("key", value);

// 점 표기법으로 중첩 값 탐색
Object value = map.finder("nested.location.x");

// DataMap → A_DataMap 변환
A_DataMap converted = A_DataMap.convert(dataMap);
A_DataMap deserialized = A_DataMap.deserialize(dataMap);
```

### 4-2. `CoolTimeMap`

`org.red.minecraft.dellarte.library.util.CoolTimeMap`

```java
CoolTimeMap coolTime = player.getCoolTime();

// 쿨타임 설정 (기본 단위: 초)
coolTime.setCoolTime("skill", 5.0);
coolTime.setCoolTime("skill", 3, CoolTimeMap.TimeType.MINUTE);

// 쿨타임 확인 (완료되었으면 true, 남아있으면 false)
boolean ready = coolTime.checkCoolTime("skill");

// 남은 시간 조회 (초 단위)
double remaining = coolTime.getLessCoolTime("skill");
double remaining = coolTime.getLessCoolTime("skill", CoolTimeMap.TimeType.MILLISECOND);

// 쿨타임 감소
coolTime.reduceCoolTime("skill", 1.0);

// 제거
coolTime.removeCoolTime("skill");

// TimeType: SECOND, MINUTE, HOUR, DAY, WEEK, YEAR, MILLISECOND
```

---

## 5. GUI 시스템 — `CustomGui` / `Button`

`org.red.minecraft.dellarte.library.inventory.CustomGui`

```java
// 생성 (size는 9의 배수)
CustomGui gui = new CustomGui(54, "제목");
CustomGui gui = new CustomGui(InventoryType.CHEST);

// 아이템 + 버튼 설정
Button button = event -> {
    event.setCancelled(true);
    // 클릭 처리
};
gui.setItem(0, itemStack, button);
gui.setItems(itemStack, button, 0, 1, 2); // 여러 슬롯
gui.fillItem(0, 8, itemStack, button);    // 범위 채우기

// 클릭 전체 취소 (UI 전용 인벤토리)
gui.setAllClickCancel(true);

// 이벤트 훅 오버라이드
CustomGui gui = new CustomGui(54, "제목") {
    @Override public void onClick(InventoryClickEvent e) { }
    @Override public void onClose(InventoryCloseEvent e) { }
    @Override public void onOpen(InventoryOpenEvent e) { }
};

// 플레이어에게 열기
aPlayer.openInventory(gui);
aPlayer.openInventory(gui, true);        // InventoryCloseEvent 무시
aPlayer.delayOpenInventory(gui);         // 1틱 후 열기 (다른 인벤토리 닫는 이후)
aPlayer.delayOpenInventory(gui, 5);      // 5틱 후 열기
```

---

## 6. 아이템 빌더 — `ItemBuilder`

`org.red.minecraft.dellarte.library.item.ItemBuilder`

```java
// 체이닝 빌더
ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
    .setDisplayName("§6검")
    .setLore("첫째 줄", "둘째 줄")
    .setCustomModelData(1001)
    .setUnbreakable(true)
    .addEnchant(Enchantment.DAMAGE_ALL, 5, true)
    .addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 10, AttributeModifier.Operation.ADD_NUMBER)
    .addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
    .setPersistentDataContainer(key, PersistentDataType.STRING, "value")
    .build();

// 정적 팩토리
ItemStack item = ItemBuilder.createItem("이름", Material.STONE);
ItemStack item = ItemBuilder.createItem("이름", Material.STONE, "로어");
ItemStack item = ItemBuilder.createItem("이름", Material.STONE, List.of("l1","l2"));
ItemStack item = ItemBuilder.createItem("이름", Material.STONE, "로어", 1001);
ItemStack item = ItemBuilder.createItem("이름", Material.STONE, "로어", 1001, 5);
ItemStack air  = ItemBuilder.air();

// 두상 아이템
ItemStack skull = ItemBuilder.getSkull(offlinePlayer);
ItemStack skull = ItemBuilder.getSkullByUrl("https://textures.minecraft.net/...");
```

---

## 7. 인터랙티브 시스템

아이템이나 타일(블록 TileState)에 동작(Act)을 바인딩하는 시스템입니다.

### 7-1. `InteractiveItem` 구현

`org.red.minecraft.dellarte.library.interactive.InteractiveItem`

```java
public class MySword implements InteractiveItem {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(plugin, "my_sword");
    }

    // Act 클래스를 재정의하여 동작 구현
    public class OnHit implements InteractiveItem.InteractiveItemAct {
        @Override
        public void run(ItemStack item, Event event) {
            EntityDamageByEntityEvent dmg = (EntityDamageByEntityEvent) event;
            // 처리
        }
    }
}
```

**InteractiveItem.Act 종류:**

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

### 7-2. `InteractiveTile` 구현

`org.red.minecraft.dellarte.library.interactive.InteractiveTile`

| 내부 클래스 | 트리거 이벤트 |
|---|---|
| `BREAK` | `BlockBreakEvent` |
| `LEFT_CLICK_BLOCK` | `PlayerInteractEvent` |
| `RIGHT_CLICK_BLOCK` | `PlayerInteractEvent` |

### 7-3. `InteractiveManager` 사용

```java
// 매니저 획득 (플러그인 초기화 필요 없음, 이미 main에서 등록됨)
InteractiveManager<ItemStack> itemManager =
    CommediaDellarte.getInteractiveManager(ItemStack.class);
InteractiveManager<TileState> tileManager =
    CommediaDellarte.getInteractiveManager(TileState.class);

// 인터랙티브 오브젝트 등록
itemManager.registerInteractiveObj(mySword);

// 아이템에 인터랙티브 마킹 (PersistentData 저장)
mySword.setInteractiveInObj(itemStack);
boolean has = mySword.isHasInteractive(itemStack);
mySword.removeInteractive(itemStack);

// 등록된 오브젝트 조회
List<InteractiveObj<ItemStack>> objs = itemManager.getInteractiveObj(itemStack);
```

### 7-4. 커스텀 `InteractiveManager` 등록

```java
// 새로운 타입의 인터랙티브 매니저 등록 (최초 1회)
CommediaDellarte.setInteractiveManager(MyType.class, new InteractiveManagerImpl<>(MyType.class));
```

---

## 8. 월드 및 에리어 시스템

### 8-1. `A_World`

`org.red.minecraft.dellarte.library.world.A_World` — `A_DataHolder` + Bukkit `World` 전체 API 래핑

```java
A_World aWorld = CommediaDellarte.getAWorld(world);

// 월드 비교
aWorld.compareWorld(worldName);
aWorld.compareWorld(location);

// 에리어 관리
Area area = new InstanceArea("spawn", plugin, world, start, end);
aWorld.putArea(plugin, area);
aWorld.removeArea(plugin, area);
List<Area> areas = aWorld.getAllArea(plugin);
List<Area> areasAtLoc = aWorld.getAreas(plugin, location);
```

### 8-2. `InstanceArea`

`org.red.minecraft.dellarte.library.world.InstanceArea`

```java
// BoundingBox 기반 사각형 영역
InstanceArea area = new InstanceArea(
    "my_area",     // 이름 (NamespacedKey key에 사용)
    plugin,
    world,
    new Vector(x1, y1, z1),
    new Vector(x2, y2, z2)
);

// 좌표 포함 여부 확인
area.contain(location);
area.contain(vector);
area.overlap(otherArea);
area.getBoundingBox();
List<Entity> entities = area.getEntities();
List<LivingEntity> living = area.getLivingEntities();
```

### 8-3. 에리어 이벤트

에리어 내에서 발생하는 이벤트를 구독할 수 있습니다.
패키지: `org.red.minecraft.dellarte.library.event.area.*`

```java
// 예시: 특정 에리어 내 블록 파괴 이벤트
@EventHandler
public void onAreaBlockBreak(AreaBlockBreakEvent event) {
    Area area = event.getArea();
    BlockBreakEvent original = event.getEvent();
}

// 에리어 이벤트는 original Bukkit 이벤트를 래핑하며
// getArea()로 발생한 에리어, getEvent()로 원본 이벤트를 반환
```

**지원 에리어 이벤트 종류 (패키지별):**

- `event.area.block.*` — 블록 관련 이벤트 (AreaBlockBreakEvent 등 약 25종)
- `event.area.player.*` — 플레이어 관련 이벤트 (AreaPlayerMoveEvent 등 약 40종)
- `event.area.entity.*` — 엔티티 관련 이벤트 (AreaEntityDamageEvent 등 약 50종)

---

## 9. 타이머 — `Timer` / `BossBarTimer`

`org.red.minecraft.dellarte.library.util.Timer`

```java
NamespacedKey key = new NamespacedKey(plugin, "my_timer");

// 타이머 생성 (maxTime: 틱 단위)
Timer timer = CommediaDellarte.createTimer(key, 200, () -> {
    // 타이머 종료 시 실행
});

timer.start();
timer.stop();
timer.isRunning();
timer.getTime();       // 현재 경과 틱
timer.getMaxTime();    // 최대 틱
timer.setTime(ticks);
timer.addTime(ticks);
timer.setMaxTime(ticks);
timer.addMaxTime(ticks);

// BossBar 타이머 (진행 바 자동 업데이트)
BossBar bar = Bukkit.createBossBar("타이머", BarColor.GREEN, BarStyle.SOLID);
BossBarTimer bossTimer = CommediaDellarte.createBossBarTimer(key, 200, runnable, bar);
```

---

## 10. 커맨드 — `AbstractCommand` / `AbstractPlayerCommand`

`org.red.minecraft.dellarte.library.command.AbstractCommand`

```java
public class MyCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "mycommand";
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        // 처리
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return List.of("option1", "option2");
        // A_Util.removeStringNotStartWith() 자동 처리됨
    }
}

// 등록
plugin.getCommand("mycommand").setExecutor(new MyCommand());
plugin.getCommand("mycommand").setTabCompleter(new MyCommand());
```

---

## 11. 리스너 — `A_Listener`

`org.red.minecraft.dellarte.library.event.listener.A_Listener`

```java
public class MyListener extends A_Listener {
    // Bukkit @EventHandler 메서드 정의
}

// 등록
new MyListener().register(plugin);
```

---

## 12. 유틸리티 클래스

### `PairData<T, V>`

```java
PairData<String, Integer> pair = new PairData<>("key", 42);
pair.dataA();  // "key"
pair.dataB();  // 42
pair.equalsDataA("key"); // true
```

### `NamespaceMap<V>`

`NamespacedKey`를 키로 사용하는 Map 구현체.

### `UUIDMap<V>`

`UUID`를 키로 사용하는 Map 구현체.

### `PairKeyMap<K1, K2, V>`

두 개의 키 조합으로 값을 관리하는 Map.

### `A_Util`

```java
// 탭 완성 필터링 (이미 AbstractCommand에서 자동 처리)
List<String> filtered = A_Util.removeStringNotStartWith(list, prefix);
```

---

## 13. 이벤트 — `FirstLoadEvent`

플러그인이 모두 로드된 후(1틱 후) 발행되는 이벤트.

```java
@EventHandler
public void onFirstLoad(FirstLoadEvent event) {
    // 데이터 스토리지가 생성된 이후 실행됨
    // 다른 플러그인 의존성 초기화 여기서 수행
}
```

---

## 14. 직렬화 — `RegisterSerializable`

커스텀 클래스를 `A_DataMap`에 저장/로드할 수 있도록 등록합니다.

```java
// 기본 등록 클래스 (main 모듈에서 자동 등록):
// ItemStack, Location, BoundingBox, Vector

// 추가 등록
CommediaDellarte.registerSerializableClass(MyClass.class, new RegisterSerializable<MyClass>() {
    @Override
    public DataMap serialize(MyClass obj) { /* ... */ }
    @Override
    public MyClass deserialize(DataMap map) { /* ... */ }
});
```

---

## 15. 패키지 구조 요약

```
org.red.minecraft.dellarte.library
├── CommediaDellarte          ← 진입점 (정적 API)
├── IDellarteManager          ← 매니저 인터페이스
├── command/
│   ├── AbstractCommand       ← 커맨드 베이스
│   └── AbstractPlayerCommand ← 플레이어 전용 커맨드 베이스
├── data/
│   └── IDataStroage          ← 데이터 스토리지 인터페이스
├── entity/
│   ├── A_Entity              ← 엔티티 래퍼 (+ A_DataHolder)
│   ├── A_LivingEntity        ← 살아있는 엔티티 래퍼
│   ├── A_NPC                 ← NPC 래퍼
│   └── A_Player              ← 플레이어 래퍼
├── event/
│   ├── FirstLoadEvent        ← 초기 로드 완료 이벤트
│   ├── TimerEndEvent         ← 타이머 종료 이벤트
│   ├── area/block/*          ← 에리어 블록 이벤트 (~25종)
│   ├── area/entity/*         ← 에리어 엔티티 이벤트 (~50종)
│   ├── area/player/*         ← 에리어 플레이어 이벤트 (~40종)
│   ├── listener/A_Listener   ← 리스너 베이스
│   └── player/               ← PlayerLeftClickEvent, PlayerRightClickEvent 등
├── interactive/
│   ├── InteractiveManager    ← 인터랙티브 매니저 인터페이스
│   ├── InteractiveObj        ← 인터랙티브 오브젝트 인터페이스
│   ├── InteractiveAct        ← 동작 인터페이스 + @ActAnnotation
│   ├── InteractiveItem       ← ItemStack 인터랙티브
│   └── InteractiveTile       ← TileState 인터랙티브
├── inventory/
│   ├── CustomGui             ← 커스텀 GUI 컨테이너
│   └── Button                ← GUI 버튼 (InventoryClickEvent → void)
├── item/
│   └── ItemBuilder           ← 아이템 빌더 (체이닝 API)
├── user/
│   └── A_OfflinePlayer       ← 오프라인 플레이어 래퍼
├── util/
│   ├── A_DataHolder          ← getDataMap() / getCoolTime() 인터페이스
│   ├── A_DataMap             ← DataMap + Minecraft 타입 확장
│   ├── A_Util                ← 정적 유틸리티
│   ├── BossBarTimer          ← BossBar 타이머 인터페이스
│   ├── CoolTimeMap           ← 쿨타임 관리
│   ├── NamespaceMap          ← NamespacedKey → Value Map
│   ├── PairData<T,V>         ← 쌍 데이터 레코드
│   ├── PairKeyMap            ← 2중 키 Map
│   ├── Timer                 ← 타이머 인터페이스
│   └── UUIDMap               ← UUID → Value Map
└── world/
    ├── A_World               ← 월드 래퍼 (+ A_DataHolder + Area 관리)
    ├── Area                  ← 에리어 인터페이스
    └── InstanceArea          ← BoundingBox 기반 에리어 구현체
```

---

## 16. 일반적인 사용 패턴

### 플레이어 데이터 읽기/쓰기

```java
// 플러그인의 player 스토리지에서 플레이어 데이터 조작
NamespacedKey storageKey = new NamespacedKey("myplugin", "player");
IDataStroage storage = CommediaDellarte.getStorage(storageKey);

String playerId = player.getUniqueId().toString();
A_DataMap data = storage.getDataMap(playerId);

int level = data.getInt("level");
data.set("level", level + 1);
storage.saveData(playerId);
```

### 플레이어 래퍼로 쿨타임 처리

```java
A_Player aPlayer = CommediaDellarte.getAPlayer(player);
CoolTimeMap coolTime = aPlayer.getCoolTime(plugin);

if (!coolTime.checkCoolTime("skill")) {
    double left = coolTime.getLessCoolTime("skill");
    player.sendMessage("쿨타임 " + left + "초 남음");
    return;
}
coolTime.setCoolTime("skill", 10.0);
// 스킬 실행
```

### 커스텀 GUI 열기

```java
CustomGui gui = new CustomGui(27, "§8메뉴");
gui.setAllClickCancel(true);

ItemStack closeBtn = ItemBuilder.createItem("§c닫기", Material.BARRIER);
gui.setItem(26, closeBtn, event -> {
    event.getWhoClicked().closeInventory();
});

A_Player aPlayer = CommediaDellarte.getAPlayer(player);
aPlayer.openInventory(gui);
```

### 채팅 입력 대기

```java
A_Player aPlayer = CommediaDellarte.getAPlayer(player);
aPlayer.setPlayerStatus(A_Player.PlayerStatus.ChatEvent, true);
aPlayer.setPlayerChatRunnable(event -> {
    String input = event.getMessage();
    event.setCancelled(true);
    // 입력 처리
    aPlayer.setPlayerStatus(A_Player.PlayerStatus.ChatEvent, false);
}, new NamespacedKey(plugin, "chat_input"));
```