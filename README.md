# CommediaDellarte

Bukkit/Paper 기반 Minecraft 플러그인 개발 프레임워크. 데이터 저장, 엔티티 래핑, 상호작용 시스템, 영역 기반 이벤트, 커스텀 GUI 등을 제공한다.

---

## 모듈 구조

| 모듈 | 설명 |
|------|------|
| `library` | 공개 API. 다른 플러그인이 의존성으로 참조하는 인터페이스·추상 클래스 모음 |
| `main` | library의 구현체. 플러그인 본체(`CommediaDellartePlugin`) 포함 |
| `project` | 샘플 프로젝트 |
| `addon` | 레벨 시스템 등 선택적 애드온 |

의존 플러그인은 `library` 모듈만 컴파일 의존성으로 추가하면 된다. `main`은 서버에 설치되는 jar다.

---

## 요구사항

- Java 17+
- Spigot / Paper 1.17.1+
- (선택) Vault, PlaceholderAPI

---

## 진입점 — CommediaDellarte

모든 기능은 `CommediaDellarte` 정적 클래스를 통해 접근한다.

```java
// 플레이어 래퍼 획득 (UUID 캐싱)
A_Player aPlayer = CommediaDellarte.getAPlayer(player);
A_Player byUUID  = CommediaDellarte.getAPlayer(uuid);
A_Player byName  = CommediaDellarte.getAPlayer("PlayerName");

// 오프라인 플레이어
A_OfflinePlayer aOffline = CommediaDellarte.getAOfflinePlayer(offlinePlayer);

// 엔티티·월드 래퍼
A_Entity       aEntity  = CommediaDellarte.getAEntity(entity);
A_LivingEntity aLiving  = CommediaDellarte.getALivingEntity(livingEntity);
A_World        aWorld   = CommediaDellarte.getAWorld(world);

// 데이터 스토리지
IDataStorage storage = CommediaDellarte.getStorage(new NamespacedKey("MyPlugin", "player"));
boolean exists       = CommediaDellarte.containStorage(key);

// 상호작용 매니저
InteractiveManager<ItemStack> itemMgr = CommediaDellarte.getInteractiveManager(ItemStack.class);
InteractiveManager<TileState> tileMgr = CommediaDellarte.getInteractiveManager(TileState.class);

// 타이머
Timer        timer   = CommediaDellarte.createTimer(key, maxTimeTicks, runnable);
BossBarTimer bbTimer = CommediaDellarte.createBossBarTimer(key, maxTimeTicks, runnable, bossBar);

// 직렬화 클래스 등록
CommediaDellarte.registerSerializableClass(myRegisterSerializable);
```

---

## 데이터 시스템

### IDataStorage

`config.yml`의 `data-storage` 섹션에 선언한 스토리지를 런타임에 가져온다.

```java
IDataStorage storage = CommediaDellarte.getStorage(new NamespacedKey("MyPlugin", "player"));

A_DataMap data = storage.getDataMap("playerName");   // 없으면 새로 생성
storage.saveData("playerName");
storage.loadData("playerName");
storage.deleteData("playerName");
storage.saveAll();
storage.loadAll();

boolean loaded = storage.loadedData("playerName");   // 메모리에 올라와 있는지
boolean exists = storage.containData("playerName");  // 저장된 데이터가 존재하는지
```

### A_DataMap

플러그인 데이터의 Key→Value 컨테이너. `ConfigurationSerializable`을 구현하므로 파일·DB에 직렬화된다.

```java
A_DataMap data = aPlayer.getDataMap(plugin);

// 읽기 (없으면 기본값을 저장한 뒤 반환)
int       level  = data.getInt("level", 0);
double    money  = data.getDouble("money", 0.0);
String    name   = data.getString("name", "");
boolean   flag   = data.getBoolean("flag", false);
UUID      uuid   = data.getUUID("uuid", UUID.randomUUID());
ItemStack item   = data.getItemStack("item");
Location  loc    = data.getLocation("loc");
Vector    vec    = data.getVector("vec");
List<String> list = data.getList("list");
A_DataMap sub    = data.getDataMap("sub");
CoolTimeMap ct   = data.getCoolTimeMap("cooltime");

// 쓰기
data.put("level", 10);
data.set("level", 10);    // 체이닝 가능 (A_DataMap 반환)
data.addInt("level", 1);
data.addDouble("money", 100.0);

// 중첩 경로 탐색 ("/" 구분자, List는 인덱스 숫자)
Object val = data.finder("sub/key/0");

// 삭제·초기화
data.remove("key");
data.clear();
```

키에 `.`(점)을 포함할 수 없다.

### A_DataHolder

`A_Entity`와 `A_World`가 구현하는 인터페이스. 플러그인별로 독립적인 DataMap을 제공한다.

```java
A_DataMap playerData = aPlayer.getDataMap(plugin);
A_DataMap worldData  = aWorld.getDataMap(plugin);
A_DataMap entityData = aEntity.getDataMap(plugin);
```

---

## 엔티티 래퍼

### A_Player

Bukkit `Player`를 감싸는 래퍼. `A_DataHolder`를 구현한다.

```java
A_Player ap = CommediaDellarte.getAPlayer(player);

// 비교
ap.comparePlayer(name);
ap.comparePlayer(uuid);
ap.comparePlayer(player);
ap.comparePlayer(aPlayer);

// 인벤토리 열기
ap.openInventory(customGui);
ap.delayOpenInventory(customGui);           // 1틱 후
ap.delayOpenInventory(customGui, 5);        // 5틱 후
ap.delayOpenInventory(customGui, true);     // ignoreInvCloseEvent
ap.closeInventory();
ap.closeInventory(true);                    // ignoreInventoryEvent

// 아이템
ap.addItem(itemStack);
ap.addItemNature(itemStack);                // 꽉 차면 바닥에 드롭
ap.addItemNature(itemStack, amount);

// 스킨 변경
ap.setSkin("textureValue", "signatureValue");
ap.setSkin(offlinePlayer);
ap.resetSkin();
boolean changed = ap.isChangedSkin();

// 플레이어 상태 (A_Player.PlayerStatus)
ap.getPlayerStatus(A_Player.PlayerStatus.IgnoreInvClose);
ap.setPlayerStatus(A_Player.PlayerStatus.IgnoreInvClose, true);
ap.switchPlayerStatus(A_Player.PlayerStatus.IgnoreInvClose);
// PlayerStatus: IgnoreInvClose, ChatEvent

// 예약 Runnable
ap.addPlayerRunnable(key, runnable, delayTicks);
ap.removePlayerRunnable(key);
ap.hasPlayerRunnable(key);

// 채팅 콜백
ap.setPlayerChatRunnable(event -> { /* 처리 */ }, key);
ap.setPlayerChatRunnable(event -> { /* 처리 */ }, key, true); // sync

// 데이터 저장·로드
ap.saveData();
ap.loadData();

// 기타
ap.sendActionBar("메시지");
ap.getPlayerSkull();
ap.isNPC();
ap.hideEntity(entity);
ap.showEntity(entity);
ap.isHiddenEntity(entity);
ap.lastBreakBlock();   // 마지막으로 파괴한 블록의 BlockState
ap.lastPlaceBlock();   // 마지막으로 설치한 블록의 BlockState
```

---

## 월드 / 영역

### A_World

```java
A_World aw = CommediaDellarte.getAWorld(world);

// 비교
aw.compareWorld(worldName);
aw.compareWorld(worldUUID);
aw.compareWorld(world);
aw.compareWorld(location);

// 엔티티
List<Entity>       entities = aw.getEntities();
List<LivingEntity> living   = aw.getLivingEntities();
List<Player>       players  = aw.getPlayers();

// 영역 관리
aw.putArea(plugin, area);
aw.removeArea(plugin, area);
aw.containArea(plugin, area);
aw.getAllArea(plugin);
aw.getAreas(plugin, location);

// 데이터
A_DataMap worldData = aw.getDataMap(plugin);
```

### InstanceArea

BoundingBox 기반 영역. A_World에 등록하면 영역 내 이벤트가 Area*Event로 발행된다.

```java
InstanceArea area = new InstanceArea(
    "myArea",
    plugin,
    world,
    new Vector(x1, y1, z1),
    new Vector(x2, y2, z2)
);

CommediaDellarte.getAWorld(world).putArea(plugin, area);

area.contain(vector);
area.contain(location);
area.contain(anotherArea);
area.overlap(vector1, vector2);
area.overlap(anotherArea);

area.getEntities();
area.getLivingEntities();

A_DataMap areaData = area.getDataMap(); // 월드 DataMap 하위에 저장됨
```

---

## 영역 이벤트 (Area Events)

A_World에 영역을 등록하면 영역 내 Bukkit 이벤트가 Area*Event로 래핑되어 발행된다.

```java
@EventHandler
public void onAreaPlayerJoin(AreaPlayerJoinEvent event) {
    Area area      = event.getArea();
    A_Player player = event.getAPlayer();
}

@EventHandler
public void onAreaEntityDeath(AreaEntityDeathEvent event) {
    Area area              = event.getArea();
    EntityDeathEvent orig  = event.getEvent();
}

@EventHandler
public void onAreaBlockBreak(AreaBlockBreakEvent event) {
    Area area = event.getArea();
}
```

이벤트 패키지: `org.red.minecraft.dellarte.library.event.area.{player|entity|block}`

- **Player** (50+개): AreaPlayerJoinEvent, AreaPlayerQuitEvent, AreaPlayerMoveEvent, AreaPlayerInteractEvent, AreaPlayerDeathEvent 등
- **Entity** (70+개): AreaEntityDeathEvent, AreaEntityDamageEvent, AreaEntityDamageByEntityEvent, AreaEntitySpawnEvent, AreaCreatureSpawnEvent 등
- **Block** (30+개): AreaBlockBreakEvent, AreaBlockPlaceEvent, AreaBlockBurnEvent, AreaBlockGrowEvent, AreaSignChangeEvent 등

`config.yml`의 `area-event` 섹션에서 이벤트별 활성/비활성 가능 (재시작 필요).

---

## 상호작용 시스템 (Interactive)

PersistentData를 이용해 아이템·블록에 동작을 바인딩한다.

### InteractiveItem (아이템)

지원 액션: `LEFT_CLICK_AIR`, `RIGHT_CLICK_AIR`, `LEFT_CLICK_BLOCK`, `RIGHT_CLICK_BLOCK`, `PHYSICAL`, `BREAK`, `FISH`, `HIT`, `DAMAGED`, `DROP`, `SWAP_HAND`, `DEATH`

```java
public class MySword implements InteractiveItem {
    private final Plugin plugin;
    public MySword(Plugin plugin) { this.plugin = plugin; }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(plugin, "mysword");
    }

    // InteractiveManager가 리플렉션으로 호출 — 첫 파라미터가 액션 타입
    public void run(InteractiveItem.LEFT_CLICK_AIR act, ItemStack item, A_Player player, PlayerInteractEvent event) {
        player.sendMessage("왼쪽 클릭!");
    }
}

// 등록
CommediaDellarte.getInteractiveManager(ItemStack.class).register(mySword);

// 아이템에 바인딩
ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
mySword.setInteractiveInObj(sword);

// 확인·제거
mySword.isHasInteractive(sword);
mySword.removeInteractive(sword);
```

### InteractiveTile (블록 TileState)

지원 액션: `BREAK`, `LEFT_CLICK_BLOCK`, `RIGHT_CLICK_BLOCK`

```java
public class MyChest implements InteractiveTile {
    @Override
    public NamespacedKey getKey() { return new NamespacedKey(plugin, "mychest"); }

    public void run(InteractiveTile.RIGHT_CLICK_BLOCK act, TileState tile, A_Player player, PlayerInteractEvent event) {
        player.sendMessage("상자 클릭!");
    }
}

// TileState에 바인딩 후 반드시 update() 호출
TileState tile = (TileState) block.getState();
myChest.setInteractiveInObj(tile);
tile.update();
```

---

## 커스텀 GUI

### CustomGui

```java
// 생성
CustomGui gui = new CustomGui(54, "내 GUI");
CustomGui gui2 = new CustomGui(InventoryType.HOPPER, "GUI");

// 버튼 (클릭 핸들러 포함)
gui.setItem(10, new ItemStack(Material.DIAMOND), event -> {
    event.getWhoClicked().sendMessage("클릭!");
});
gui.setButton(15, event -> event.setCancelled(true));

// 전체 클릭 취소
gui.setAllClickCancel(true);

// 범위 채우기
gui.fillItem(0, 8, borderItem);
gui.setItems(item, 0, 8, 17, 18);

// 이벤트 훅 오버라이드
CustomGui gui = new CustomGui(54, "GUI") {
    @Override public void onClick(InventoryClickEvent event) { }
    @Override public void onOpen(InventoryOpenEvent event) { }
    @Override public void onClose(InventoryCloseEvent event) { }
};

// 열기
aPlayer.openInventory(gui);
aPlayer.delayOpenInventory(gui);
aPlayer.delayOpenInventory(gui, 3);
```

### CustomGuiBuilder

```java
CustomGui gui = new CustomGuiBuilder(54, "내 GUI")
    .setAllClickCancel(true)
    .setItem(4, titleItem)
    .setItem(10, diamond, event -> player.sendMessage("클릭"))
    .build();
```

---

## 쿨타임 (CoolTimeMap)

```java
CoolTimeMap cooltime = data.getCoolTimeMap("cooltime");

// 설정
cooltime.setCoolTime("ability", 5.0);                                    // 5초
cooltime.setCoolTime("ability", 500, CoolTimeMap.TimeType.MILLISECOND);

// 확인 (완료 시 true 반환 + 자동 제거)
if (cooltime.checkCoolTime("ability")) {
    executeSkill();
    cooltime.setCoolTime("ability", 5.0);
}

// 남은 시간
double remainSec = cooltime.getLessCoolTime("ability");
double remainMs  = cooltime.getLessCoolTime("ability", CoolTimeMap.TimeType.MILLISECOND);

// 단축·제거
cooltime.reduceCoolTime("ability", 1.0);
cooltime.removeCoolTime("ability");
```

TimeType: `MILLISECOND`, `SECOND`(기본), `MINUTE`, `HOUR`, `DAY`, `WEEK`, `YEAR`

---

## 타이머

```java
// 단순 타이머 (틱 단위)
Timer timer = CommediaDellarte.createTimer(
    new NamespacedKey(plugin, "mytimer"),
    100,                               // maxTime (틱)
    () -> player.sendMessage("완료!")
);
timer.start();
timer.stop();
timer.addTime(20);
timer.addMaxTime(40);
timer.getTime();
timer.getMaxTime();
timer.isRunning();

// BossBar 타이머
BossBarTimer bb = CommediaDellarte.createBossBarTimer(key, 200, callback, bossBar);
bb.start();
```

---

## 아이템 빌더 (ItemBuilder)

```java
ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
    .setDisplayName("§6전설의 검")
    .setLore("§7공격력 +100", "§7전설 등급")
    .setCustomModelData(1001)
    .setUnbreakable(true)
    .addEnchantment(Enchantment.DAMAGE_ALL, 5)
    .addItemFlags(ItemFlag.HIDE_ENCHANTS)
    .setPersistentDataContainer(key, PersistentDataType.STRING, "value")
    .build();

// 정적 팩토리
ItemBuilder.createItem("이름", Material.STICK, "설명");
ItemBuilder.createItem("이름", Material.STICK, List.of("줄1", "줄2"), 1001);
ItemBuilder.getSkull(offlinePlayer);
ItemBuilder.getSkullByUrl("https://textures.minecraft.net/...");
ItemBuilder.air();
```

---

## 커맨드

```java
public class MyCommand extends AbstractCommand {
    @Override public String getName() { return "mycommand"; }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        sender.sendMessage("실행됨");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
        return List.of("option1", "option2"); // 현재 입력 기준 자동 필터링됨
    }
}

plugin.getCommand("mycommand").setExecutor(cmd);
plugin.getCommand("mycommand").setTabCompleter(cmd);
```

`AbstractPlayerCommand`는 sender가 Player일 때만 실행된다.

---

## 커스텀 직렬화 (RegisterSerializable)

커스텀 객체를 A_DataMap에 저장·복원하려면 등록이 필요하다.

```java
public class MyDataSerializer implements RegisterSerializable<MyData> {
    @Override public Class<MyData> getType() { return MyData.class; }

    @Override
    public A_DataMap serialize(MyData data) {
        return new A_DataMap().set("value", data.getValue());
    }

    @Override
    public MyData deserialize(A_DataMap map) {
        return new MyData(map.getInt("value", 0));
    }
}

// onEnable에서 등록
CommediaDellarte.registerSerializableClass(new MyDataSerializer());
```

---

## 이벤트 리스너 (A_Listener)

```java
public class MyListener extends A_Listener {
    @EventHandler
    public void onAreaPlayerJoin(AreaPlayerJoinEvent event) { ... }
}

new MyListener().register(plugin);
```

---

## 플러그인 초기화 순서

`FirstLoadEvent`가 발행된 뒤에 데이터를 안전하게 사용할 수 있다.

```java
@EventHandler
public void onFirstLoad(FirstLoadEvent event) {
    // 모든 DataStorage 준비 완료
    IDataStorage storage = CommediaDellarte.getStorage(...);
}
```

---

## config.yml 데이터 저장 설정

```yaml
data-storage:
  MyPlugin:                         # 플러그인 이름 (대소문자 정확히)
    player:                         # player/world/entity는 A_Player 등과 자동 연결
      autoSaveEnable: true
      autoSaveTime: 300             # 초
      saveType: "file"              # file | mysql | none
      config:
        directory: "%plugin_name%/%type%"

    custom_type:
      saveType: "mysql"
      config:
        host: "127.0.0.1"
        port: 3306
        database: "mydb"
        username: "user"
        password: "pass"
        table: "%plugin_name%_%type%"
```

- 파일 저장 위치: `plugins/CommediaDellarte/{directory}/{key}.yml`
- `player/world/entity` 미선언 시 `getDataMap(plugin)` 호출 시 저장 안 되는 임시 스토리지 반환

---

## Vault 설정

```yaml
vault:
  enable: true
  format: "원"
  fractional-digits: -1   # -1: 자동, 0 이상: 고정 소수점 자리수
```