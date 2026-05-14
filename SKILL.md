# CommediaDellarte Library — Claude Code Skill Reference

이 문서는 CommediaDellarte 라이브러리를 사용하는 Minecraft 플러그인 코드를 작성할 때 Claude Code가 참조하는 에이전트용 문서다.

---

## 패키지 구조

```
org.red.minecraft.dellarte.library
├── CommediaDellarte          (정적 진입점, 모든 기능의 시작점)
├── IDellarteManager          (CommediaDellarte 백엔드 인터페이스)
├── command/
│   ├── AbstractCommand        (CommandExecutor + TabCompleter)
│   └── AbstractPlayerCommand  (Player에게만 실행)
├── data/
│   ├── IDataStorage           (데이터 저장소 인터페이스)
│   ├── IDataAdapter           (실제 저장 어댑터)
│   ├── AdapterFactory         (어댑터 팩토리)
│   └── SaveConfig             (스토리지 설정 record)
├── data/serializable/
│   ├── RegisterSerializable<T>
│   └── RegisterConfigSerializable<T>
├── entity/
│   ├── A_Entity               (Entity 래퍼, A_DataHolder 구현)
│   ├── A_LivingEntity         (LivingEntity 래퍼)
│   ├── A_Player               (Player 래퍼)
│   └── A_NPC                  (NPC 래퍼)
├── event/
│   ├── FirstLoadEvent         (플러그인 초기화 완료 이벤트)
│   ├── TimerEndEvent          (타이머 종료 이벤트)
│   ├── area/block/            (30+개 블록 Area 이벤트)
│   ├── area/entity/           (70+개 엔티티 Area 이벤트)
│   ├── area/player/           (50+개 플레이어 Area 이벤트)
│   └── listener/A_Listener    (이벤트 리스너 베이스 클래스)
├── interactive/
│   ├── InteractiveItem        (아이템 상호작용 바인딩)
│   ├── InteractiveTile        (블록 TileState 상호작용 바인딩)
│   ├── InteractiveObj<T>      (상호작용 객체 베이스)
│   ├── InteractiveAct<T>      (액션 인터페이스)
│   └── InteractiveManager<T>  (매니저)
├── inventory/
│   ├── CustomGui              (커스텀 인벤토리 GUI)
│   ├── CustomGuiBuilder       (빌더 패턴)
│   └── Button                 (클릭 핸들러 인터페이스)
├── item/
│   └── ItemBuilder            (ItemStack 빌더)
├── user/
│   └── A_OfflinePlayer        (OfflinePlayer 래퍼)
├── util/
│   ├── A_DataHolder           (데이터 보유자 인터페이스)
│   ├── A_DataMap              (Key→Value 데이터 컨테이너)
│   ├── Timer                  (틱 기반 타이머)
│   ├── BossBarTimer           (BossBar 연동 타이머)
│   └── map/CoolTimeMap        (쿨타임 관리)
└── world/
    ├── A_World                (World 래퍼, A_DataHolder 구현)
    ├── Area                   (영역 인터페이스)
    └── InstanceArea           (BoundingBox 기반 영역 구현체)
```

---

## CommediaDellarte 정적 API (전체)

```java
// 직렬화 등록
CommediaDellarte.registerSerializableClass(RegisterSerializable<T>)

// 스토리지
@Nullable IDataStorage CommediaDellarte.getStorage(NamespacedKey key)
boolean               CommediaDellarte.containStorage(NamespacedKey key)

// 플레이어
A_Player              CommediaDellarte.getAPlayer(@NotNull Player player)
@Nullable A_Player    CommediaDellarte.getAPlayer(UUID uuid)
@Nullable A_Player    CommediaDellarte.getAPlayer(String name)
@Nullable A_Player    CommediaDellarte.getAPlayer(@NotNull OfflinePlayer player)
A_OfflinePlayer       CommediaDellarte.getAOfflinePlayer(@NotNull OfflinePlayer player)

// 엔티티·월드
A_Entity              CommediaDellarte.getAEntity(@NotNull Entity entity)
A_LivingEntity        CommediaDellarte.getALivingEntity(@NotNull LivingEntity entity)
A_World               CommediaDellarte.getAWorld(@NotNull World world)
@Nullable A_World     CommediaDellarte.getAWorld(String worldName)
@Nullable A_World     CommediaDellarte.getAWorld(UUID worldUUID)

// 상호작용 매니저
<T> InteractiveManager<T>  CommediaDellarte.getInteractiveManager(@NotNull Class<T> managerType)
<T> boolean                CommediaDellarte.setInteractiveManager(@NotNull Class<T> clazz, @NotNull InteractiveManager<T> manager)

// 타이머
Timer        CommediaDellarte.createTimer(@NotNull NamespacedKey key, int maxTime, @Nullable Runnable runnable)
BossBarTimer CommediaDellarte.createBossBarTimer(NamespacedKey key, int maxTime, @Nullable Runnable runnable, BossBar... bossBars)
```

---

## A_DataMap 전체 API

```java
// 생성
new A_DataMap()
new A_DataMap(Map<String, Object> map)

// 읽기 — 없으면 defaultValue를 저장한 뒤 반환
int          getInt(String key)                              // 기본값 0
int          getInt(String key, int nullValue)
double       getDouble(String key)                           // 기본값 0.0
double       getDouble(String key, double nullValue)
String       getString(String key)                           // 기본값 ""
String       getString(String key, String nullValue)
boolean      getBoolean(String key)                          // 기본값 false
boolean      getBoolean(String key, boolean nullValue)
Object       get(String key)                                 // 기본값 null
Object       get(String key, Object nullValue)
<T> T        getClass(String key, Class<T> clazz)
<T> T        getClass(String key, Class<T> clazz, Object nullValue)
<T> List<T>  getList(String key)
<T> List<T>  getList(String key, List<T> nullValue)
UUID         getUUID(String key)
UUID         getUUID(String key, UUID nullValue)
ItemStack    getItemStack(String key)
ItemStack    getItemStack(String key, ItemStack nullValue)
Location     getLocation(String key)
Location     getLocation(String key, Location nullValue)
Vector       getVector(String key)
Vector       getVector(String key, Vector nullValue)
BoundingBox  getBoundingBox(String key)
BoundingBox  getBoundingBox(String key, BoundingBox nullValue)
CoolTimeMap  getCoolTimeMap(String key)
CoolTimeMap  getCoolTimeMap(String key, CoolTimeMap nullValue)
A_DataMap    getDataMap(String key)
A_DataMap    getDataMap(String key, A_DataMap nullValue)
@Nullable Object finder(String path)                         // "/" 구분 중첩 경로

// 쓰기
void      put(String key, Object value)
A_DataMap set(String key, Object value)   // 체이닝 가능
void      addInt(String key, int value)
void      addDouble(String key, double value)

// 상태
boolean              containsKey(String key)
Set<String>          keySet()
Collection<Object>   values()
Set<Map.Entry<...>>  entrySet()
Map<String, Object>  getMap()

// 관리
void remove(String key)
void clear()
void copy(A_DataMap dataMap)
void copy(Map<String, Object> map)
```

키에 `.`(점) 사용 불가. `finder()`는 `/` 구분자 사용.

---

## IDataStorage API

```java
A_DataMap getDataMap(String key)       // 없으면 새 빈 맵 생성
boolean   loadedData(String key)       // 메모리에 로드 여부
boolean   containData(String key)      // 저장된 데이터 존재 여부
void      saveData(String key)
void      loadData(String key)
void      deleteData(String key)
void      saveAll()
void      loadAll()
SaveConfig config()
```

---

## A_Player API (주요)

```java
// 비교
boolean comparePlayer(String name)
boolean comparePlayer(UUID uuid)
boolean comparePlayer(Player player)
boolean comparePlayer(OfflinePlayer player)
boolean comparePlayer(A_Player player)
boolean comparePlayer(A_OfflinePlayer player)

// 데이터 (A_DataHolder)
A_DataMap getDataMap(Plugin plugin)
void      saveData()
void      loadData()

// 인벤토리
InventoryView openInventory(@NotNull CustomGui var1)
InventoryView openInventory(@NotNull CustomGui var1, boolean ignoreEvent)
InventoryView openInventory(@NotNull Inventory var1)
InventoryView openInventory(@NotNull Inventory var1, boolean ignoreEvent)
void          closeInventory()
void          closeInventory(boolean ignoreInventoryEvent)
void          delayOpenInventory(Inventory inv)
void          delayOpenInventory(Inventory inv, int delay)
void          delayOpenInventory(CustomGui inv)
void          delayOpenInventory(CustomGui inv, int delay)
void          delayOpenInventory(Inventory inv, boolean ignoreInvCloseEvent)
void          delayOpenInventory(CustomGui inv, boolean ignoreInvCloseEvent)
void          delayOpenInventory(CustomGui inv, int delay, boolean ignoreInvCloseEvent)

// 아이템
HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks)
void addItemNature(ItemStack... itemStacks)   // 넘치면 바닥에 드롭
void addItemNature(ItemStack itemStack, int amount)

// 스킨
void    setSkin(String skin, String signature)
void    setSkin(OfflinePlayer player)
void    resetSkin()
boolean isChangedSkin()

// 플레이어 상태
boolean getPlayerStatus(A_Player.PlayerStatus status)
void    setPlayerStatus(A_Player.PlayerStatus status, boolean bool)
void    switchPlayerStatus(A_Player.PlayerStatus status)
// PlayerStatus enum: IgnoreInvClose, ChatEvent

// 예약 Runnable
void    addPlayerRunnable(NamespacedKey key, Runnable runnable, int delay)
void    removePlayerRunnable(NamespacedKey key)
boolean hasPlayerRunnable(NamespacedKey key)

// 채팅 콜백
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key)
void setPlayerChatRunnable(PlayerChatRunnable runnable, NamespacedKey key, boolean sync)

// 엔티티 가시성
void    hideEntity(Entity entity)
void    showEntity(Entity entity)
boolean isHiddenEntity(Entity entity)

// 기타
ItemStack  getPlayerSkull()
void       sendActionBar(@NotNull String message)
boolean    isNPC()
boolean    isOnline()
boolean    isBanned()
boolean    isWhitelisted()
BlockState lastBreakBlock()
BlockState lastPlaceBlock()
Player     getEntity()      // 원본 Bukkit Player
```

---

## A_World API (주요)

```java
// 비교
boolean compareWorld(@NotNull String world)
boolean compareWorld(@NotNull UUID worldUUID)
boolean compareWorld(@NotNull World world)
boolean compareWorld(@NotNull Location location)
boolean compareWorld(@NotNull A_World world)

// 데이터 (A_DataHolder)
A_DataMap getDataMap(Plugin plugin)

// 엔티티
List<Entity>       getEntities()
List<LivingEntity> getLivingEntities()
List<Player>       getPlayers()
<T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> var1)

// 영역
boolean     putArea(Plugin plugin, Area area)
boolean     containArea(Plugin plugin, Area area)
boolean     removeArea(Plugin plugin, Area area)
List<Area>  getAllArea(Plugin plugin)
List<Area>  getAreas(Plugin plugin, Location location)

// 원본
World getWorld()
String getName()
UUID  getUID()
```

---

## InstanceArea API

```java
// 생성자
new InstanceArea(String name, Plugin plugin, World world, Vector start, Vector end)

// 포함 여부
boolean contain(Vector vec)
boolean contain(Vector start, Vector end)
boolean contain(Location vec)
boolean contain(Location start, Location end)
boolean contain(Area area)
boolean contain(BoundingBox boundingBox)

// 겹침 여부
boolean overlap(Vector start, Vector end)
boolean overlap(Location start, Location end)
boolean overlap(Area area)
boolean overlap(BoundingBox boundingBox)

// 엔티티
@NotNull List<Entity>       getEntities()
@NotNull List<LivingEntity> getLivingEntities()

// 데이터 (월드 DataMap 하위에 "area--name" 키로 저장됨)
A_DataMap getDataMap()

// 기타
@NotNull World   getWorld()
@NotNull A_World getAWorld()
@NotNull NamespacedKey getKey()
@Nullable BoundingBox  getBoundingBox()
boolean isEventEnable()   // 항상 true
```

---

## CoolTimeMap API

```java
// 설정
void setCoolTime(String name, double time)                           // 초 단위
void setCoolTime(String name, double time, TimeType type)

// 확인 (완료 시 true + 자동 제거)
boolean checkCoolTime(String name)

// 조회
long   getCoolTime(String name)                                      // 만료 절대 시간 (ms)
double getLessCoolTime(String name)                                  // 남은 시간 (초)
double getLessCoolTime(String name, TimeType type)

// 수정
void reduceCoolTime(String name, double reduceSecond)
void reduceCoolTime(String name, double reduceSecond, TimeType type)
void removeCoolTime(String name)
void clear()

// TimeType enum
MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK, YEAR
```

---

## InteractiveItem 구현 패턴

```java
public class MyItem implements InteractiveItem {
    private final Plugin plugin;
    public MyItem(Plugin plugin) { this.plugin = plugin; }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(plugin, "unique_key");
    }

    // 각 액션별 메서드 — InteractiveManager가 리플렉션으로 호출
    // 첫 번째 파라미터가 액션 타입, 나머지는 컨텍스트
    public void run(InteractiveItem.LEFT_CLICK_AIR     act, ItemStack item, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveItem.RIGHT_CLICK_AIR    act, ItemStack item, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveItem.LEFT_CLICK_BLOCK   act, ItemStack item, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveItem.RIGHT_CLICK_BLOCK  act, ItemStack item, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveItem.PHYSICAL           act, ItemStack item, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveItem.BREAK              act, ItemStack item, A_Player player, BlockBreakEvent event)     { }
    public void run(InteractiveItem.FISH               act, ItemStack item, A_Player player, PlayerFishEvent event)     { }
    public void run(InteractiveItem.HIT                act, ItemStack item, A_Player player, EntityDamageByEntityEvent event) { }
    public void run(InteractiveItem.DAMAGED            act, ItemStack item, A_Player player, EntityDamageByEntityEvent event) { }
    public void run(InteractiveItem.DROP               act, ItemStack item, A_Player player, PlayerDropItemEvent event) { }
    public void run(InteractiveItem.SWAP_HAND          act, ItemStack item, A_Player player, PlayerSwapHandItemsEvent event) { }
    public void run(InteractiveItem.DEATH              act, ItemStack item, A_Player player, PlayerDeathEvent event)    { }
}

// 등록
CommediaDellarte.getInteractiveManager(ItemStack.class).register(myItem);

// 아이템에 바인딩
myItem.setInteractiveInObj(itemStack);    // AIR이면 RuntimeException

// 상태 확인·제거
myItem.isHasInteractive(itemStack);
myItem.removeInteractive(itemStack);
```

---

## InteractiveTile 구현 패턴

```java
public class MyTile implements InteractiveTile {
    @Override
    public NamespacedKey getKey() { return new NamespacedKey(plugin, "unique_key"); }

    public void run(InteractiveTile.BREAK             act, TileState tile, A_Player player, BlockBreakEvent event)   { }
    public void run(InteractiveTile.LEFT_CLICK_BLOCK  act, TileState tile, A_Player player, PlayerInteractEvent event) { }
    public void run(InteractiveTile.RIGHT_CLICK_BLOCK act, TileState tile, A_Player player, PlayerInteractEvent event) { }
}

// 등록
CommediaDellarte.getInteractiveManager(TileState.class).register(myTile);

// 바인딩 (Chest, Furnace 등 TileEntity 블록만 가능)
TileState tile = (TileState) block.getState();
myTile.setInteractiveInObj(tile);
tile.update();  // 필수
```

---

## CustomGui API

```java
// 생성자
new CustomGui(int size)
new CustomGui(int size, @NotNull String title)
new CustomGui(@NotNull InventoryType type)
new CustomGui(@NotNull InventoryType type, @NotNull String title)

// 버튼
void   setButton(int slot, Button button)
Button getButton(int slot)        // 없으면 null
boolean hasButton(int slot)
void   removeButton(int slot)

// 아이템
ItemStack setItem(int i, ItemStack itemStack)
ItemStack setItem(int i, ItemStack itemStack, Button button)
ItemStack fillItem(int startSlot, int endSlot, ItemStack itemStack)
ItemStack fillItem(int startSlot, int endSlot, ItemStack itemStack, Button button)
Map<Integer, ItemStack> setItems(ItemStack itemStack, Integer... slots)
Map<Integer, ItemStack> setItems(ItemStack itemStack, Button button, Integer... slots)
ItemStack getItem(int i)

// 설정
void    setAllClickCancel(boolean allClickCancel)
boolean getAllClickCancel()

// 이벤트 훅 (오버라이드)
void onClick(InventoryClickEvent event)
void onClose(InventoryCloseEvent event)
void onOpen(InventoryOpenEvent event)

// Inventory 위임
int             getSize()
Inventory       getInventory()
void            clear()
List<HumanEntity> getViewers()
```

Button 인터페이스: `void run(InventoryClickEvent event)` — 람다로 구현 가능.

---

## Timer / BossBarTimer API

```java
// Timer
void      start()
void      stop()
boolean   isRunning()
void      addTime(int time)
void      addMaxTime(int maxTime)
void      setTime(int time)
void      setMaxTime(int maxTime)
int       getTime()
int       getMaxTime()
NamespacedKey getKey()
@Nullable Runnable getRunnable()

// BossBarTimer — Timer를 상속하며 BossBar 진행률 연동
```

---

## ItemBuilder API

```java
// 생성자
new ItemBuilder(Material material)
new ItemBuilder(ItemStack itemStack)

// 메서드 체이닝 (모두 ItemBuilder 반환)
setDisplayName(String arg0)
setLore(List<String> arg0)
setLore(String... lore)
setCustomModelData(Integer data)
setUnbreakable(boolean arg0)
setAmount(int amount)
setType(Material type)
setDurability(short durability)
addEnchantment(Enchantment ench, int level)
addEnchant(Enchantment arg0, int arg1, boolean arg2)
addUnsafeEnchantment(Enchantment ench, int level)
addItemFlags(ItemFlag... arg0)
addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation)
addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot)
addAttribute(Attribute attribute, AttributeModifier attributeModifier)
setPersistentDataContainer(NamespacedKey key, PersistentDataType<T,Z> type, Z value)
removePersistentDataContainer(NamespacedKey key)

// 빌드
ItemStack build()

// 정적 팩토리
static ItemStack createItem(String display, Material material)
static ItemStack createItem(String display, Material material, String lore)
static ItemStack createItem(String display, Material material, List<String> lore)
static ItemStack createItem(String display, Material material, String lore, int customModelData)
static ItemStack createItem(String display, Material material, String lore, int customModelData, int amount)
static ItemStack createItem(String display, Material material, List<String> lore, int customModelData)
static ItemStack createItem(String display, Material material, List<String> lore, int customModelData, int amount)
static ItemStack getSkull(OfflinePlayer player)
static ItemStack getSkullByUrl(String url)
static ItemStack air()
```

---

## RegisterSerializable 구현 패턴

```java
public class MySerializer implements RegisterSerializable<MyData> {
    @Override public Class<MyData> getType() { return MyData.class; }

    @Override
    public A_DataMap serialize(MyData data) {
        A_DataMap map = new A_DataMap();
        map.put("hp", data.getHp());
        map.put("name", data.getName());
        return map;
    }

    @Override
    public MyData deserialize(A_DataMap map) {
        return new MyData(map.getInt("hp", 100), map.getString("name", ""));
    }
}

// onEnable에서 반드시 호출
CommediaDellarte.registerSerializableClass(new MySerializer());
```

---

## AbstractCommand 구현 패턴

```java
public class MyCommand extends AbstractCommand {
    @Override
    public String getName() { return "mycommand"; }   // plugin.yml command 이름과 일치

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        // 처리 후 true 반환
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        // 반환 목록은 자동으로 현재 입력 기준 필터링됨
        return List.of("sub1", "sub2");
    }
}

// 등록
MyCommand cmd = new MyCommand();
plugin.getCommand(cmd.getName()).setExecutor(cmd);
plugin.getCommand(cmd.getName()).setTabCompleter(cmd);
```

---

## A_Listener 사용

```java
public class MyListener extends A_Listener {
    @EventHandler
    public void onAreaPlayerJoin(AreaPlayerJoinEvent event) {
        Area area      = event.getArea();
        A_Player player = event.getAPlayer();
    }
}

new MyListener().register(plugin);
```

---

## config.yml 데이터 저장 설정 방법

다른 플러그인이 데이터를 영속 저장하려면 CommediaDellarte의 `config.yml`에 항목을 추가해야 한다.

```yaml
data-storage:
  PluginName:           # 사용할 플러그인 이름 (정확한 대소문자)
    player:             # 타입 이름 — player/world/entity는 A_Player/A_World/A_Entity와 자동 연결
      autoSaveEnable: true
      autoSaveTime: 300
      saveType: "file"  # file | mysql | none
      config:
        directory: "%plugin_name%/%type%"
    custom:
      saveType: "none"
```

### 저장소 키 (NamespacedKey) 규칙

```java
// player/world/entity 타입은 NamespacedKey(pluginName, typeName) 형태
NamespacedKey playerKey = new NamespacedKey("MyPlugin", "player");
IDataStorage storage = CommediaDellarte.getStorage(playerKey);
```

---

## 자주 쓰는 패턴

### 플레이어 데이터 읽고 저장하기

```java
A_Player ap = CommediaDellarte.getAPlayer(player);
A_DataMap data = ap.getDataMap(plugin);

int level = data.getInt("level", 1);
data.put("level", level + 1);
ap.saveData();  // 즉시 저장 (자동저장과 별개)
```

### 영역 등록 및 이벤트 처리

```java
// onEnable
InstanceArea zone = new InstanceArea("combat_zone", plugin, world,
    new Vector(0, 60, 0), new Vector(100, 120, 100));
CommediaDellarte.getAWorld(world).putArea(plugin, zone);

// @EventHandler
public void onAreaDeath(AreaEntityDeathEvent event) {
    if (event.getArea().getKey().equals(new NamespacedKey(plugin, "combat_zone"))) {
        // 전투 구역 내 사망 처리
    }
}
```

### 쿨타임이 있는 스킬 아이템

```java
// InteractiveItem 구현
public void run(InteractiveItem.RIGHT_CLICK_AIR act, ItemStack item, A_Player player, PlayerInteractEvent event) {
    A_DataMap data = player.getDataMap(plugin);
    CoolTimeMap ct = data.getCoolTimeMap("cooltime");

    if (!ct.checkCoolTime("skill")) {
        player.sendMessage("쿨타임: " + String.format("%.1f", ct.getLessCoolTime("skill")) + "초");
        return;
    }

    // 스킬 실행
    player.sendMessage("스킬 발동!");
    ct.setCoolTime("skill", 10.0);  // 10초 쿨타임
}
```

### GUI에서 데이터 편집

```java
CustomGui gui = new CustomGui(27, "§6설정");
gui.setAllClickCancel(true);

A_DataMap data = aPlayer.getDataMap(plugin);
int level = data.getInt("level", 1);

gui.setItem(13,
    new ItemBuilder(Material.EXPERIENCE_BOTTLE)
        .setDisplayName("§e레벨: " + level)
        .setLore("§7클릭하면 레벨 증가")
        .build(),
    event -> {
        data.addInt("level", 1);
        aPlayer.saveData();
        aPlayer.closeInventory();
        aPlayer.sendMessage("레벨 업!");
    }
);

aPlayer.openInventory(gui);
```

---

## 주의사항

1. **A_DataMap 키에 `.` 불가** — 점이 들어간 키는 `IllegalArgumentException` 발생
2. **InteractiveTile 바인딩 후 `tile.update()` 필수** — 호출하지 않으면 PersistentData가 반영되지 않음
3. **InteractiveItem에 AIR 바인딩 불가** — `setInteractiveInObj(air item)`은 RuntimeException
4. **스토리지 미설정 시 임시 저장소 반환** — `getDataMap(plugin)` 호출은 가능하지만 서버 재시작 시 사라짐
5. **FirstLoadEvent 이후 스토리지 사용** — `onEnable`에서 즉시 `getStorage()`를 호출하면 null 가능성 있음
6. **area-event 설정 변경은 서버 재시작 필요** — 런타임 중 변경 불가
7. **CoolTimeMap은 A_DataMap에 저장하면 자동 직렬화됨** — 별도 RegisterSerializable 불필요