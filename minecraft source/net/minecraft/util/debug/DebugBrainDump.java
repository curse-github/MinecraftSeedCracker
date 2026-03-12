/*     */ package net.minecraft.util.debug;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipType;
/*     */ import net.minecraft.world.entity.ai.memory.ExpirableValue;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ 
/*     */ public final class DebugBrainDump extends Record {
/*     */   private final String name;
/*     */   private final String profession;
/*     */   private final int xp;
/*     */   private final float health;
/*     */   private final float maxHealth;
/*     */   private final String inventory;
/*     */   private final boolean wantsGolem;
/*     */   
/*  38 */   public DebugBrainDump(String name, String profession, int xp, float health, float maxHealth, String inventory, boolean wantsGolem, int angerLevel, List<String> activities, List<String> behaviors, List<String> memories, List<String> gossips, Set<BlockPos> pois, Set<BlockPos> potentialPois) { this.name = name; this.profession = profession; this.xp = xp; this.health = health; this.maxHealth = maxHealth; this.inventory = inventory; this.wantsGolem = wantsGolem; this.angerLevel = angerLevel; this.activities = activities; this.behaviors = behaviors; this.memories = memories; this.gossips = gossips; this.pois = pois; this.potentialPois = potentialPois; } private final int angerLevel; private final List<String> activities; private final List<String> behaviors; private final List<String> memories; private final List<String> gossips; private final Set<BlockPos> pois; private final Set<BlockPos> potentialPois; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBrainDump;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBrainDump; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBrainDump;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBrainDump; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBrainDump;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #38	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBrainDump;
/*  38 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public String profession() { return this.profession; } public int xp() { return this.xp; } public float health() { return this.health; } public float maxHealth() { return this.maxHealth; } public String inventory() { return this.inventory; } public boolean wantsGolem() { return this.wantsGolem; } public int angerLevel() { return this.angerLevel; } public List<String> activities() { return this.activities; } public List<String> behaviors() { return this.behaviors; } public List<String> memories() { return this.memories; } public List<String> gossips() { return this.gossips; } public Set<BlockPos> pois() { return this.pois; } public Set<BlockPos> potentialPois() { return this.potentialPois; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final StreamCodec<FriendlyByteBuf, DebugBrainDump> STREAM_CODEC = StreamCodec.of((output, value) -> value.write(output), DebugBrainDump::new);
/*     */   
/*     */   public DebugBrainDump(FriendlyByteBuf input) {
/*  57 */     this(input
/*  58 */         .readUtf(), input
/*  59 */         .readUtf(), input
/*  60 */         .readInt(), input
/*  61 */         .readFloat(), input
/*  62 */         .readFloat(), input
/*  63 */         .readUtf(), input
/*  64 */         .readBoolean(), input
/*  65 */         .readInt(), input
/*  66 */         .readList(FriendlyByteBuf::readUtf), input
/*  67 */         .readList(FriendlyByteBuf::readUtf), input
/*  68 */         .readList(FriendlyByteBuf::readUtf), input
/*  69 */         .readList(FriendlyByteBuf::readUtf), (Set)input
/*  70 */         .readCollection(java.util.HashSet::new, BlockPos.STREAM_CODEC), (Set)input
/*  71 */         .readCollection(java.util.HashSet::new, BlockPos.STREAM_CODEC));
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/*  76 */     output.writeUtf(this.name);
/*  77 */     output.writeUtf(this.profession);
/*  78 */     output.writeInt(this.xp);
/*  79 */     output.writeFloat(this.health);
/*  80 */     output.writeFloat(this.maxHealth);
/*  81 */     output.writeUtf(this.inventory);
/*  82 */     output.writeBoolean(this.wantsGolem);
/*  83 */     output.writeInt(this.angerLevel);
/*  84 */     output.writeCollection(this.activities, FriendlyByteBuf::writeUtf);
/*  85 */     output.writeCollection(this.behaviors, FriendlyByteBuf::writeUtf);
/*  86 */     output.writeCollection(this.memories, FriendlyByteBuf::writeUtf);
/*  87 */     output.writeCollection(this.gossips, FriendlyByteBuf::writeUtf);
/*  88 */     output.writeCollection(this.pois, BlockPos.STREAM_CODEC);
/*  89 */     output.writeCollection(this.potentialPois, BlockPos.STREAM_CODEC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DebugBrainDump takeBrainDump(ServerLevel serverLevel, LivingEntity entity) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic getEntityName : (Lnet/minecraft/world/entity/Entity;)Ljava/lang/String;
/*     */     //   4: astore_2
/*     */     //   5: aload_1
/*     */     //   6: instanceof net/minecraft/world/entity/npc/villager/Villager
/*     */     //   9: ifeq -> 42
/*     */     //   12: aload_1
/*     */     //   13: checkcast net/minecraft/world/entity/npc/villager/Villager
/*     */     //   16: astore #5
/*     */     //   18: aload #5
/*     */     //   20: invokevirtual getVillagerData : ()Lnet/minecraft/world/entity/npc/villager/VillagerData;
/*     */     //   23: invokevirtual profession : ()Lnet/minecraft/core/Holder;
/*     */     //   26: invokeinterface getRegisteredName : ()Ljava/lang/String;
/*     */     //   31: astore_3
/*     */     //   32: aload #5
/*     */     //   34: invokevirtual getVillagerXp : ()I
/*     */     //   37: istore #4
/*     */     //   39: goto -> 48
/*     */     //   42: ldc ''
/*     */     //   44: astore_3
/*     */     //   45: iconst_0
/*     */     //   46: istore #4
/*     */     //   48: aload_1
/*     */     //   49: invokevirtual getHealth : ()F
/*     */     //   52: fstore #5
/*     */     //   54: aload_1
/*     */     //   55: invokevirtual getMaxHealth : ()F
/*     */     //   58: fstore #6
/*     */     //   60: aload_1
/*     */     //   61: invokevirtual getBrain : ()Lnet/minecraft/world/entity/ai/Brain;
/*     */     //   64: astore #7
/*     */     //   66: aload_1
/*     */     //   67: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   70: invokevirtual getGameTime : ()J
/*     */     //   73: lstore #8
/*     */     //   75: aload_1
/*     */     //   76: instanceof net/minecraft/world/entity/npc/InventoryCarrier
/*     */     //   79: ifeq -> 124
/*     */     //   82: aload_1
/*     */     //   83: checkcast net/minecraft/world/entity/npc/InventoryCarrier
/*     */     //   86: astore #11
/*     */     //   88: aload #11
/*     */     //   90: invokeinterface getInventory : ()Lnet/minecraft/world/SimpleContainer;
/*     */     //   95: astore #12
/*     */     //   97: aload #12
/*     */     //   99: invokeinterface isEmpty : ()Z
/*     */     //   104: ifeq -> 112
/*     */     //   107: ldc ''
/*     */     //   109: goto -> 119
/*     */     //   112: aload #12
/*     */     //   114: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   119: astore #10
/*     */     //   121: goto -> 128
/*     */     //   124: ldc ''
/*     */     //   126: astore #10
/*     */     //   128: aload_1
/*     */     //   129: instanceof net/minecraft/world/entity/npc/villager/Villager
/*     */     //   132: ifeq -> 155
/*     */     //   135: aload_1
/*     */     //   136: checkcast net/minecraft/world/entity/npc/villager/Villager
/*     */     //   139: astore #12
/*     */     //   141: aload #12
/*     */     //   143: lload #8
/*     */     //   145: invokevirtual wantsToSpawnGolem : (J)Z
/*     */     //   148: ifeq -> 155
/*     */     //   151: iconst_1
/*     */     //   152: goto -> 156
/*     */     //   155: iconst_0
/*     */     //   156: istore #11
/*     */     //   158: aload_1
/*     */     //   159: instanceof net/minecraft/world/entity/monster/warden/Warden
/*     */     //   162: ifeq -> 179
/*     */     //   165: aload_1
/*     */     //   166: checkcast net/minecraft/world/entity/monster/warden/Warden
/*     */     //   169: astore #13
/*     */     //   171: aload #13
/*     */     //   173: invokevirtual getClientAngerLevel : ()I
/*     */     //   176: goto -> 180
/*     */     //   179: iconst_m1
/*     */     //   180: istore #12
/*     */     //   182: aload #7
/*     */     //   184: invokevirtual getActiveActivities : ()Ljava/util/Set;
/*     */     //   187: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   192: <illegal opcode> apply : ()Ljava/util/function/Function;
/*     */     //   197: invokeinterface map : (Ljava/util/function/Function;)Ljava/util/stream/Stream;
/*     */     //   202: invokeinterface toList : ()Ljava/util/List;
/*     */     //   207: astore #13
/*     */     //   209: aload #7
/*     */     //   211: invokevirtual getRunningBehaviors : ()Ljava/util/List;
/*     */     //   214: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   219: <illegal opcode> apply : ()Ljava/util/function/Function;
/*     */     //   224: invokeinterface map : (Ljava/util/function/Function;)Ljava/util/stream/Stream;
/*     */     //   229: invokeinterface toList : ()Ljava/util/List;
/*     */     //   234: astore #14
/*     */     //   236: aload_0
/*     */     //   237: aload_1
/*     */     //   238: lload #8
/*     */     //   240: invokestatic getMemoryDescriptions : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;J)Ljava/util/stream/Stream;
/*     */     //   243: <illegal opcode> apply : ()Ljava/util/function/Function;
/*     */     //   248: invokeinterface map : (Ljava/util/function/Function;)Ljava/util/stream/Stream;
/*     */     //   253: invokeinterface toList : ()Ljava/util/List;
/*     */     //   258: astore #15
/*     */     //   260: aload #7
/*     */     //   262: iconst_3
/*     */     //   263: anewarray net/minecraft/world/entity/ai/memory/MemoryModuleType
/*     */     //   266: dup
/*     */     //   267: iconst_0
/*     */     //   268: getstatic net/minecraft/world/entity/ai/memory/MemoryModuleType.JOB_SITE : Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;
/*     */     //   271: aastore
/*     */     //   272: dup
/*     */     //   273: iconst_1
/*     */     //   274: getstatic net/minecraft/world/entity/ai/memory/MemoryModuleType.HOME : Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;
/*     */     //   277: aastore
/*     */     //   278: dup
/*     */     //   279: iconst_2
/*     */     //   280: getstatic net/minecraft/world/entity/ai/memory/MemoryModuleType.MEETING_POINT : Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;
/*     */     //   283: aastore
/*     */     //   284: invokestatic getKnownBlockPositions : (Lnet/minecraft/world/entity/ai/Brain;[Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)Ljava/util/Set;
/*     */     //   287: astore #16
/*     */     //   289: aload #7
/*     */     //   291: iconst_1
/*     */     //   292: anewarray net/minecraft/world/entity/ai/memory/MemoryModuleType
/*     */     //   295: dup
/*     */     //   296: iconst_0
/*     */     //   297: getstatic net/minecraft/world/entity/ai/memory/MemoryModuleType.POTENTIAL_JOB_SITE : Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;
/*     */     //   300: aastore
/*     */     //   301: invokestatic getKnownBlockPositions : (Lnet/minecraft/world/entity/ai/Brain;[Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)Ljava/util/Set;
/*     */     //   304: astore #17
/*     */     //   306: aload_1
/*     */     //   307: instanceof net/minecraft/world/entity/npc/villager/Villager
/*     */     //   310: ifeq -> 327
/*     */     //   313: aload_1
/*     */     //   314: checkcast net/minecraft/world/entity/npc/villager/Villager
/*     */     //   317: astore #19
/*     */     //   319: aload #19
/*     */     //   321: invokestatic getVillagerGossips : (Lnet/minecraft/world/entity/npc/villager/Villager;)Ljava/util/List;
/*     */     //   324: goto -> 330
/*     */     //   327: invokestatic of : ()Ljava/util/List;
/*     */     //   330: astore #18
/*     */     //   332: new net/minecraft/util/debug/DebugBrainDump
/*     */     //   335: dup
/*     */     //   336: aload_2
/*     */     //   337: aload_3
/*     */     //   338: iload #4
/*     */     //   340: fload #5
/*     */     //   342: fload #6
/*     */     //   344: aload #10
/*     */     //   346: iload #11
/*     */     //   348: iload #12
/*     */     //   350: aload #13
/*     */     //   352: aload #14
/*     */     //   354: aload #15
/*     */     //   356: aload #18
/*     */     //   358: aload #16
/*     */     //   360: aload #17
/*     */     //   362: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;IFFLjava/lang/String;ZILjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/Set;Ljava/util/Set;)V
/*     */     //   365: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     //   #97	-> 5
/*     */     //   #98	-> 18
/*     */     //   #99	-> 32
/*     */     //   #101	-> 42
/*     */     //   #102	-> 45
/*     */     //   #104	-> 48
/*     */     //   #105	-> 54
/*     */     //   #107	-> 60
/*     */     //   #108	-> 66
/*     */     //   #111	-> 75
/*     */     //   #112	-> 88
/*     */     //   #113	-> 97
/*     */     //   #114	-> 121
/*     */     //   #115	-> 124
/*     */     //   #118	-> 128
/*     */     //   #119	-> 158
/*     */     //   #121	-> 182
/*     */     //   #122	-> 209
/*     */     //   #124	-> 236
/*     */     //   #125	-> 248
/*     */     //   #126	-> 253
/*     */     //   #128	-> 260
/*     */     //   #129	-> 289
/*     */     //   #131	-> 306
/*     */     //   #133	-> 332
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   32	10	3	profession	Ljava/lang/String;
/*     */     //   39	3	4	xp	I
/*     */     //   18	24	5	villager	Lnet/minecraft/world/entity/npc/villager/Villager;
/*     */     //   97	24	12	inventory	Lnet/minecraft/world/Container;
/*     */     //   121	3	10	inventoryStr	Ljava/lang/String;
/*     */     //   88	36	11	inventoryCarrier	Lnet/minecraft/world/entity/npc/InventoryCarrier;
/*     */     //   141	14	12	villager	Lnet/minecraft/world/entity/npc/villager/Villager;
/*     */     //   171	8	13	warden	Lnet/minecraft/world/entity/monster/warden/Warden;
/*     */     //   319	8	19	villager	Lnet/minecraft/world/entity/npc/villager/Villager;
/*     */     //   0	366	0	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	366	1	entity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   5	361	2	name	Ljava/lang/String;
/*     */     //   45	321	3	profession	Ljava/lang/String;
/*     */     //   48	318	4	xp	I
/*     */     //   54	312	5	health	F
/*     */     //   60	306	6	maxHealth	F
/*     */     //   66	300	7	brain	Lnet/minecraft/world/entity/ai/Brain;
/*     */     //   75	291	8	gameTime	J
/*     */     //   128	238	10	inventoryStr	Ljava/lang/String;
/*     */     //   158	208	11	wantsGolem	Z
/*     */     //   182	184	12	angerLevel	I
/*     */     //   209	157	13	activities	Ljava/util/List;
/*     */     //   236	130	14	behaviors	Ljava/util/List;
/*     */     //   260	106	15	memories	Ljava/util/List;
/*     */     //   289	77	16	pois	Ljava/util/Set;
/*     */     //   306	60	17	potentialPois	Ljava/util/Set;
/*     */     //   332	34	18	gossips	Ljava/util/List;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   66	300	7	brain	Lnet/minecraft/world/entity/ai/Brain<*>;
/*     */     //   209	157	13	activities	Ljava/util/List<Ljava/lang/String;>;
/*     */     //   236	130	14	behaviors	Ljava/util/List<Ljava/lang/String;>;
/*     */     //   260	106	15	memories	Ljava/util/List<Ljava/lang/String;>;
/*     */     //   289	77	16	pois	Ljava/util/Set<Lnet/minecraft/core/BlockPos;>;
/*     */     //   306	60	17	potentialPois	Ljava/util/Set<Lnet/minecraft/core/BlockPos;>;
/*     */     //   332	34	18	gossips	Ljava/util/List<Ljava/lang/String;>; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   private static Set<BlockPos> getKnownBlockPositions(Brain<?> brain, MemoryModuleType... memories) {
/* 154 */     Objects.requireNonNull(brain);
/* 155 */     Objects.requireNonNull(brain); return (Set)Stream.of(memories).filter(brain::hasMemoryValue).map(brain::getMemory)
/* 156 */       .flatMap(Optional::stream)
/* 157 */       .map(GlobalPos::pos)
/* 158 */       .collect(Collectors.toSet());
/*     */   }
/*     */   
/*     */   private static List<String> getVillagerGossips(Villager villager) {
/* 162 */     List<String> gossips = new ArrayList<String>();
/* 163 */     villager.getGossips().getGossipEntries().forEach((uuid, entries) -> {
/* 164 */           String gossipeeName = DebugEntityNameGenerator.getEntityName(uuid);
/* 165 */           entries.forEach(());
/*     */         });
/*     */ 
/*     */     
/* 169 */     return gossips;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   private static Stream<String> getMemoryDescriptions(ServerLevel level, LivingEntity body, long timestamp) { return body.getBrain().getMemories().entrySet().stream()
/* 177 */       .map(entry -> {
/* 178 */           MemoryModuleType<?> memoryType = (MemoryModuleType)entry.getKey();
/* 179 */           Optional<? extends ExpirableValue<?>> optionalExpirableValue = (Optional)entry.getValue();
/* 180 */           return getMemoryDescription(level, timestamp, memoryType, optionalExpirableValue);
/*     */         
/* 182 */         }).sorted(); }
/*     */ 
/*     */   
/*     */   private static String getMemoryDescription(ServerLevel level, long timestamp, MemoryModuleType<?> memoryType, Optional<? extends ExpirableValue<?>> maybeValue) {
/*     */     String description;
/* 187 */     if (maybeValue.isPresent()) {
/* 188 */       ExpirableValue<?> expirableValue = (ExpirableValue)maybeValue.get();
/* 189 */       Object value = expirableValue.getValue();
/* 190 */       if (memoryType == MemoryModuleType.HEARD_BELL_TIME) {
/* 191 */         long timeSince = timestamp - ((Long)value).longValue();
/* 192 */         description = "" + timeSince + " ticks ago";
/* 193 */       } else if (expirableValue.canExpire()) {
/* 194 */         description = getShortDescription(level, value) + " (ttl: " + getShortDescription(level, value) + ")";
/*     */       } else {
/* 196 */         description = getShortDescription(level, value);
/*     */       } 
/*     */     } else {
/* 199 */       description = "-";
/*     */     } 
/* 201 */     return BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(memoryType).getPath() + ": " + BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(memoryType).getPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getShortDescription(ServerLevel level, Object obj) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: astore_2
/*     */     //   2: iconst_0
/*     */     //   3: istore_3
/*     */     //   4: aload_2
/*     */     //   5: iload_3
/*     */     //   6: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   11: tableswitch default -> 251, -1 -> 60, 0 -> 66, 1 -> 85, 2 -> 99, 3 -> 117, 4 -> 135, 5 -> 153, 6 -> 171, 7 -> 205
/*     */     //   60: ldc_w '-'
/*     */     //   63: goto -> 255
/*     */     //   66: aload_2
/*     */     //   67: checkcast java/util/UUID
/*     */     //   70: astore #4
/*     */     //   72: aload_0
/*     */     //   73: aload_0
/*     */     //   74: aload #4
/*     */     //   76: invokevirtual getEntity : (Ljava/util/UUID;)Lnet/minecraft/world/entity/Entity;
/*     */     //   79: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   82: goto -> 255
/*     */     //   85: aload_2
/*     */     //   86: checkcast net/minecraft/world/entity/Entity
/*     */     //   89: astore #5
/*     */     //   91: aload #5
/*     */     //   93: invokestatic getEntityName : (Lnet/minecraft/world/entity/Entity;)Ljava/lang/String;
/*     */     //   96: goto -> 255
/*     */     //   99: aload_2
/*     */     //   100: checkcast net/minecraft/world/entity/ai/memory/WalkTarget
/*     */     //   103: astore #6
/*     */     //   105: aload_0
/*     */     //   106: aload #6
/*     */     //   108: invokevirtual getTarget : ()Lnet/minecraft/world/entity/ai/behavior/PositionTracker;
/*     */     //   111: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   114: goto -> 255
/*     */     //   117: aload_2
/*     */     //   118: checkcast net/minecraft/world/entity/ai/behavior/EntityTracker
/*     */     //   121: astore #7
/*     */     //   123: aload_0
/*     */     //   124: aload #7
/*     */     //   126: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   129: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   132: goto -> 255
/*     */     //   135: aload_2
/*     */     //   136: checkcast net/minecraft/core/GlobalPos
/*     */     //   139: astore #8
/*     */     //   141: aload_0
/*     */     //   142: aload #8
/*     */     //   144: invokevirtual pos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   147: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   150: goto -> 255
/*     */     //   153: aload_2
/*     */     //   154: checkcast net/minecraft/world/entity/ai/behavior/BlockPosTracker
/*     */     //   157: astore #9
/*     */     //   159: aload_0
/*     */     //   160: aload #9
/*     */     //   162: invokevirtual currentBlockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   165: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   168: goto -> 255
/*     */     //   171: aload_2
/*     */     //   172: checkcast net/minecraft/world/damagesource/DamageSource
/*     */     //   175: astore #10
/*     */     //   177: aload #10
/*     */     //   179: invokevirtual getEntity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   182: astore #11
/*     */     //   184: aload #11
/*     */     //   186: ifnonnull -> 196
/*     */     //   189: aload_1
/*     */     //   190: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   193: goto -> 255
/*     */     //   196: aload_0
/*     */     //   197: aload #11
/*     */     //   199: invokestatic getShortDescription : (Lnet/minecraft/server/level/ServerLevel;Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   202: goto -> 255
/*     */     //   205: aload_2
/*     */     //   206: checkcast java/util/Collection
/*     */     //   209: astore #11
/*     */     //   211: aload #11
/*     */     //   213: invokeinterface stream : ()Ljava/util/stream/Stream;
/*     */     //   218: aload_0
/*     */     //   219: <illegal opcode> apply : (Lnet/minecraft/server/level/ServerLevel;)Ljava/util/function/Function;
/*     */     //   224: invokeinterface map : (Ljava/util/function/Function;)Ljava/util/stream/Stream;
/*     */     //   229: ldc_w ', '
/*     */     //   232: invokestatic joining : (Ljava/lang/CharSequence;)Ljava/util/stream/Collector;
/*     */     //   235: invokeinterface collect : (Ljava/util/stream/Collector;)Ljava/lang/Object;
/*     */     //   240: checkcast java/lang/String
/*     */     //   243: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   248: goto -> 255
/*     */     //   251: aload_1
/*     */     //   252: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   255: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #209	-> 0
/*     */     //   #210	-> 60
/*     */     //   #211	-> 66
/*     */     //   #212	-> 85
/*     */     //   #213	-> 99
/*     */     //   #214	-> 117
/*     */     //   #215	-> 135
/*     */     //   #216	-> 153
/*     */     //   #217	-> 171
/*     */     //   #218	-> 177
/*     */     //   #219	-> 184
/*     */     //   #221	-> 205
/*     */     //   #222	-> 224
/*     */     //   #223	-> 232
/*     */     //   #225	-> 251
/*     */     //   #209	-> 255
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   72	13	4	uuid	Ljava/util/UUID;
/*     */     //   91	8	5	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   105	12	6	walkTarget	Lnet/minecraft/world/entity/ai/memory/WalkTarget;
/*     */     //   123	12	7	entityTracker	Lnet/minecraft/world/entity/ai/behavior/EntityTracker;
/*     */     //   141	12	8	globalPos	Lnet/minecraft/core/GlobalPos;
/*     */     //   159	12	9	tracker	Lnet/minecraft/world/entity/ai/behavior/BlockPosTracker;
/*     */     //   184	21	11	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   177	28	10	damageSource	Lnet/minecraft/world/damagesource/DamageSource;
/*     */     //   211	40	11	collection	Ljava/util/Collection;
/*     */     //   0	256	0	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	256	1	obj	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   211	40	11	collection	Ljava/util/Collection<*>; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 230 */   public boolean hasPoi(BlockPos poiPos) { return this.pois.contains(poiPos); }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public boolean hasPotentialPoi(BlockPos poiPos) { return this.potentialPois.contains(poiPos); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugBrainDump.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */