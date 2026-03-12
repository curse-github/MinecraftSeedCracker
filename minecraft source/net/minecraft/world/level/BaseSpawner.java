/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
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
/*     */ public abstract class BaseSpawner
/*     */ {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String SPAWN_DATA_TAG = "SpawnData";
/*     */   
/*     */   private static final int EVENT_SPAWN = 1;
/*     */   
/*     */   private static final int DEFAULT_SPAWN_DELAY = 20;
/*     */   
/*     */   private static final int DEFAULT_MIN_SPAWN_DELAY = 200;
/*     */   private static final int DEFAULT_MAX_SPAWN_DELAY = 800;
/*     */   private static final int DEFAULT_SPAWN_COUNT = 4;
/*     */   private static final int DEFAULT_MAX_NEARBY_ENTITIES = 6;
/*     */   private static final int DEFAULT_REQUIRED_PLAYER_RANGE = 16;
/*     */   private static final int DEFAULT_SPAWN_RANGE = 4;
/*  49 */   private int spawnDelay = 20;
/*  50 */   private WeightedList<SpawnData> spawnPotentials = WeightedList.of();
/*     */   private SpawnData nextSpawnData;
/*     */   private double spin;
/*     */   private double oSpin;
/*  54 */   private int minSpawnDelay = 200;
/*  55 */   private int maxSpawnDelay = 800;
/*  56 */   private int spawnCount = 4;
/*     */   private Entity displayEntity;
/*  58 */   private int maxNearbyEntities = 6;
/*  59 */   private int requiredPlayerRange = 16;
/*  60 */   private int spawnRange = 4;
/*     */ 
/*     */   
/*  63 */   public void setEntityId(EntityType<?> type, Level level, RandomSource random, BlockPos pos) { getOrCreateNextSpawnData(level, random, pos).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   private boolean isNearPlayer(Level level, BlockPos pos) { return level.hasNearbyAlivePlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, this.requiredPlayerRange); }
/*     */ 
/*     */   
/*     */   public void clientTick(Level level, BlockPos pos) {
/*  71 */     if (!isNearPlayer(level, pos)) {
/*  72 */       this.oSpin = this.spin;
/*  73 */     } else if (this.displayEntity != null) {
/*  74 */       RandomSource random = level.getRandom();
/*  75 */       double xP = pos.getX() + random.nextDouble();
/*  76 */       double yP = pos.getY() + random.nextDouble();
/*  77 */       double zP = pos.getZ() + random.nextDouble();
/*  78 */       level.addParticle(ParticleTypes.SMOKE, xP, yP, zP, 0.0D, 0.0D, 0.0D);
/*  79 */       level.addParticle(ParticleTypes.FLAME, xP, yP, zP, 0.0D, 0.0D, 0.0D);
/*     */       
/*  81 */       if (this.spawnDelay > 0) {
/*  82 */         this.spawnDelay--;
/*     */       }
/*  84 */       this.oSpin = this.spin;
/*  85 */       this.spin = (this.spin + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
/*     */     } 
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
/*     */   public void serverTick(ServerLevel level, BlockPos pos) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: invokevirtual isNearPlayer : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z
/*     */     //   6: ifeq -> 16
/*     */     //   9: aload_1
/*     */     //   10: invokevirtual isSpawnerBlockEnabled : ()Z
/*     */     //   13: ifne -> 17
/*     */     //   16: return
/*     */     //   17: aload_0
/*     */     //   18: getfield spawnDelay : I
/*     */     //   21: iconst_m1
/*     */     //   22: if_icmpne -> 31
/*     */     //   25: aload_0
/*     */     //   26: aload_1
/*     */     //   27: aload_2
/*     */     //   28: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   31: aload_0
/*     */     //   32: getfield spawnDelay : I
/*     */     //   35: ifle -> 49
/*     */     //   38: aload_0
/*     */     //   39: dup
/*     */     //   40: getfield spawnDelay : I
/*     */     //   43: iconst_1
/*     */     //   44: isub
/*     */     //   45: putfield spawnDelay : I
/*     */     //   48: return
/*     */     //   49: iconst_0
/*     */     //   50: istore_3
/*     */     //   51: aload_1
/*     */     //   52: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   55: astore #4
/*     */     //   57: aload_0
/*     */     //   58: aload_1
/*     */     //   59: aload #4
/*     */     //   61: aload_2
/*     */     //   62: invokevirtual getOrCreateNextSpawnData : (Lnet/minecraft/world/level/Level;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/SpawnData;
/*     */     //   65: astore #5
/*     */     //   67: iconst_0
/*     */     //   68: istore #6
/*     */     //   70: iload #6
/*     */     //   72: aload_0
/*     */     //   73: getfield spawnCount : I
/*     */     //   76: if_icmpge -> 732
/*     */     //   79: new net/minecraft/util/ProblemReporter$ScopedCollector
/*     */     //   82: dup
/*     */     //   83: aload_0
/*     */     //   84: <illegal opcode> get : (Lnet/minecraft/world/level/BaseSpawner;)Lnet/minecraft/util/ProblemReporter$PathElement;
/*     */     //   89: getstatic net/minecraft/world/level/BaseSpawner.LOGGER : Lorg/slf4j/Logger;
/*     */     //   92: invokespecial <init> : (Lnet/minecraft/util/ProblemReporter$PathElement;Lorg/slf4j/Logger;)V
/*     */     //   95: astore #7
/*     */     //   97: aload #7
/*     */     //   99: aload_1
/*     */     //   100: invokevirtual registryAccess : ()Lnet/minecraft/core/RegistryAccess;
/*     */     //   103: aload #5
/*     */     //   105: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   108: invokestatic create : (Lnet/minecraft/util/ProblemReporter;Lnet/minecraft/core/HolderLookup$Provider;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/storage/ValueInput;
/*     */     //   111: astore #8
/*     */     //   113: aload #8
/*     */     //   115: invokestatic by : (Lnet/minecraft/world/level/storage/ValueInput;)Ljava/util/Optional;
/*     */     //   118: astore #9
/*     */     //   120: aload #9
/*     */     //   122: invokevirtual isEmpty : ()Z
/*     */     //   125: ifeq -> 140
/*     */     //   128: aload_0
/*     */     //   129: aload_1
/*     */     //   130: aload_2
/*     */     //   131: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   134: aload #7
/*     */     //   136: invokevirtual close : ()V
/*     */     //   139: return
/*     */     //   140: aload #8
/*     */     //   142: ldc 'Pos'
/*     */     //   144: getstatic net/minecraft/world/phys/Vec3.CODEC : Lcom/mojang/serialization/Codec;
/*     */     //   147: invokeinterface read : (Ljava/lang/String;Lcom/mojang/serialization/Codec;)Ljava/util/Optional;
/*     */     //   152: aload_0
/*     */     //   153: aload_2
/*     */     //   154: aload #4
/*     */     //   156: <illegal opcode> get : (Lnet/minecraft/world/level/BaseSpawner;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Ljava/util/function/Supplier;
/*     */     //   161: invokevirtual orElseGet : (Ljava/util/function/Supplier;)Ljava/lang/Object;
/*     */     //   164: checkcast net/minecraft/world/phys/Vec3
/*     */     //   167: astore #10
/*     */     //   169: aload_1
/*     */     //   170: aload #9
/*     */     //   172: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   175: checkcast net/minecraft/world/entity/EntityType
/*     */     //   178: aload #10
/*     */     //   180: getfield x : D
/*     */     //   183: aload #10
/*     */     //   185: getfield y : D
/*     */     //   188: aload #10
/*     */     //   190: getfield z : D
/*     */     //   193: invokevirtual getSpawnAABB : (DDD)Lnet/minecraft/world/phys/AABB;
/*     */     //   196: invokevirtual noCollision : (Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   199: ifne -> 210
/*     */     //   202: aload #7
/*     */     //   204: invokevirtual close : ()V
/*     */     //   207: goto -> 726
/*     */     //   210: aload #10
/*     */     //   212: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   215: astore #11
/*     */     //   217: aload #5
/*     */     //   219: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   222: invokevirtual isPresent : ()Z
/*     */     //   225: ifeq -> 298
/*     */     //   228: aload #9
/*     */     //   230: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   233: checkcast net/minecraft/world/entity/EntityType
/*     */     //   236: invokevirtual getCategory : ()Lnet/minecraft/world/entity/MobCategory;
/*     */     //   239: invokevirtual isFriendly : ()Z
/*     */     //   242: ifne -> 263
/*     */     //   245: aload_1
/*     */     //   246: invokevirtual getDifficulty : ()Lnet/minecraft/world/Difficulty;
/*     */     //   249: getstatic net/minecraft/world/Difficulty.PEACEFUL : Lnet/minecraft/world/Difficulty;
/*     */     //   252: if_acmpne -> 263
/*     */     //   255: aload #7
/*     */     //   257: invokevirtual close : ()V
/*     */     //   260: goto -> 726
/*     */     //   263: aload #5
/*     */     //   265: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   268: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   271: checkcast net/minecraft/world/level/SpawnData$CustomSpawnRules
/*     */     //   274: astore #12
/*     */     //   276: aload #12
/*     */     //   278: aload #11
/*     */     //   280: aload_1
/*     */     //   281: invokevirtual isValidPosition : (Lnet/minecraft/core/BlockPos;Lnet/minecraft/server/level/ServerLevel;)Z
/*     */     //   284: ifne -> 295
/*     */     //   287: aload #7
/*     */     //   289: invokevirtual close : ()V
/*     */     //   292: goto -> 726
/*     */     //   295: goto -> 330
/*     */     //   298: aload #9
/*     */     //   300: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   303: checkcast net/minecraft/world/entity/EntityType
/*     */     //   306: aload_1
/*     */     //   307: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   310: aload #11
/*     */     //   312: aload_1
/*     */     //   313: invokevirtual getRandom : ()Lnet/minecraft/util/RandomSource;
/*     */     //   316: invokestatic checkSpawnRules : (Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z
/*     */     //   319: ifne -> 330
/*     */     //   322: aload #7
/*     */     //   324: invokevirtual close : ()V
/*     */     //   327: goto -> 726
/*     */     //   330: aload #8
/*     */     //   332: aload_1
/*     */     //   333: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   336: aload #10
/*     */     //   338: <illegal opcode> process : (Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/entity/EntityProcessor;
/*     */     //   343: invokestatic loadEntityRecursive : (Lnet/minecraft/world/level/storage/ValueInput;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/EntityProcessor;)Lnet/minecraft/world/entity/Entity;
/*     */     //   346: astore #12
/*     */     //   348: aload #12
/*     */     //   350: ifnonnull -> 365
/*     */     //   353: aload_0
/*     */     //   354: aload_1
/*     */     //   355: aload_2
/*     */     //   356: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   359: aload #7
/*     */     //   361: invokevirtual close : ()V
/*     */     //   364: return
/*     */     //   365: aload_1
/*     */     //   366: aload #12
/*     */     //   368: invokevirtual getClass : ()Ljava/lang/Class;
/*     */     //   371: invokestatic forExactClass : (Ljava/lang/Class;)Lnet/minecraft/world/level/entity/EntityTypeTest;
/*     */     //   374: new net/minecraft/world/phys/AABB
/*     */     //   377: dup
/*     */     //   378: aload_2
/*     */     //   379: invokevirtual getX : ()I
/*     */     //   382: i2d
/*     */     //   383: aload_2
/*     */     //   384: invokevirtual getY : ()I
/*     */     //   387: i2d
/*     */     //   388: aload_2
/*     */     //   389: invokevirtual getZ : ()I
/*     */     //   392: i2d
/*     */     //   393: aload_2
/*     */     //   394: invokevirtual getX : ()I
/*     */     //   397: iconst_1
/*     */     //   398: iadd
/*     */     //   399: i2d
/*     */     //   400: aload_2
/*     */     //   401: invokevirtual getY : ()I
/*     */     //   404: iconst_1
/*     */     //   405: iadd
/*     */     //   406: i2d
/*     */     //   407: aload_2
/*     */     //   408: invokevirtual getZ : ()I
/*     */     //   411: iconst_1
/*     */     //   412: iadd
/*     */     //   413: i2d
/*     */     //   414: invokespecial <init> : (DDDDDD)V
/*     */     //   417: aload_0
/*     */     //   418: getfield spawnRange : I
/*     */     //   421: i2d
/*     */     //   422: invokevirtual inflate : (D)Lnet/minecraft/world/phys/AABB;
/*     */     //   425: getstatic net/minecraft/world/entity/EntitySelector.NO_SPECTATORS : Ljava/util/function/Predicate;
/*     */     //   428: invokevirtual getEntities : (Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;
/*     */     //   431: invokeinterface size : ()I
/*     */     //   436: istore #13
/*     */     //   438: iload #13
/*     */     //   440: aload_0
/*     */     //   441: getfield maxNearbyEntities : I
/*     */     //   444: if_icmplt -> 459
/*     */     //   447: aload_0
/*     */     //   448: aload_1
/*     */     //   449: aload_2
/*     */     //   450: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   453: aload #7
/*     */     //   455: invokevirtual close : ()V
/*     */     //   458: return
/*     */     //   459: aload #12
/*     */     //   461: aload #12
/*     */     //   463: invokevirtual getX : ()D
/*     */     //   466: aload #12
/*     */     //   468: invokevirtual getY : ()D
/*     */     //   471: aload #12
/*     */     //   473: invokevirtual getZ : ()D
/*     */     //   476: aload #4
/*     */     //   478: invokeinterface nextFloat : ()F
/*     */     //   483: ldc_w 360.0
/*     */     //   486: fmul
/*     */     //   487: fconst_0
/*     */     //   488: invokevirtual snapTo : (DDDFF)V
/*     */     //   491: aload #12
/*     */     //   493: instanceof net/minecraft/world/entity/Mob
/*     */     //   496: ifeq -> 637
/*     */     //   499: aload #12
/*     */     //   501: checkcast net/minecraft/world/entity/Mob
/*     */     //   504: astore #14
/*     */     //   506: aload #5
/*     */     //   508: invokevirtual getCustomSpawnRules : ()Ljava/util/Optional;
/*     */     //   511: invokevirtual isEmpty : ()Z
/*     */     //   514: ifeq -> 537
/*     */     //   517: aload #14
/*     */     //   519: aload_1
/*     */     //   520: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   523: invokevirtual checkSpawnRules : (Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;)Z
/*     */     //   526: ifne -> 537
/*     */     //   529: aload #7
/*     */     //   531: invokevirtual close : ()V
/*     */     //   534: goto -> 726
/*     */     //   537: aload #14
/*     */     //   539: aload_1
/*     */     //   540: invokevirtual checkSpawnObstruction : (Lnet/minecraft/world/level/LevelReader;)Z
/*     */     //   543: ifne -> 554
/*     */     //   546: aload #7
/*     */     //   548: invokevirtual close : ()V
/*     */     //   551: goto -> 726
/*     */     //   554: aload #5
/*     */     //   556: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   559: invokevirtual size : ()I
/*     */     //   562: iconst_1
/*     */     //   563: if_icmpne -> 586
/*     */     //   566: aload #5
/*     */     //   568: invokevirtual getEntityToSpawn : ()Lnet/minecraft/nbt/CompoundTag;
/*     */     //   571: ldc 'id'
/*     */     //   573: invokevirtual getString : (Ljava/lang/String;)Ljava/util/Optional;
/*     */     //   576: invokevirtual isPresent : ()Z
/*     */     //   579: ifeq -> 586
/*     */     //   582: iconst_1
/*     */     //   583: goto -> 587
/*     */     //   586: iconst_0
/*     */     //   587: istore #15
/*     */     //   589: iload #15
/*     */     //   591: ifeq -> 617
/*     */     //   594: aload #12
/*     */     //   596: checkcast net/minecraft/world/entity/Mob
/*     */     //   599: aload_1
/*     */     //   600: aload_1
/*     */     //   601: aload #12
/*     */     //   603: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*     */     //   606: invokevirtual getCurrentDifficultyAt : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/DifficultyInstance;
/*     */     //   609: getstatic net/minecraft/world/entity/EntitySpawnReason.SPAWNER : Lnet/minecraft/world/entity/EntitySpawnReason;
/*     */     //   612: aconst_null
/*     */     //   613: invokevirtual finalizeSpawn : (Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;
/*     */     //   616: pop
/*     */     //   617: aload #5
/*     */     //   619: invokevirtual getEquipment : ()Ljava/util/Optional;
/*     */     //   622: aload #14
/*     */     //   624: dup
/*     */     //   625: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   628: pop
/*     */     //   629: <illegal opcode> accept : (Lnet/minecraft/world/entity/Mob;)Ljava/util/function/Consumer;
/*     */     //   634: invokevirtual ifPresent : (Ljava/util/function/Consumer;)V
/*     */     //   637: aload_1
/*     */     //   638: aload #12
/*     */     //   640: invokevirtual tryAddFreshEntityWithPassengers : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   643: ifne -> 658
/*     */     //   646: aload_0
/*     */     //   647: aload_1
/*     */     //   648: aload_2
/*     */     //   649: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   652: aload #7
/*     */     //   654: invokevirtual close : ()V
/*     */     //   657: return
/*     */     //   658: aload_1
/*     */     //   659: sipush #2004
/*     */     //   662: aload_2
/*     */     //   663: iconst_0
/*     */     //   664: invokevirtual levelEvent : (ILnet/minecraft/core/BlockPos;I)V
/*     */     //   667: aload_1
/*     */     //   668: aload #12
/*     */     //   670: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_PLACE : Lnet/minecraft/core/Holder$Reference;
/*     */     //   673: aload #11
/*     */     //   675: invokevirtual gameEvent : (Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V
/*     */     //   678: aload #12
/*     */     //   680: instanceof net/minecraft/world/entity/Mob
/*     */     //   683: ifeq -> 694
/*     */     //   686: aload #12
/*     */     //   688: checkcast net/minecraft/world/entity/Mob
/*     */     //   691: invokevirtual spawnAnim : ()V
/*     */     //   694: iconst_1
/*     */     //   695: istore_3
/*     */     //   696: aload #7
/*     */     //   698: invokevirtual close : ()V
/*     */     //   701: goto -> 726
/*     */     //   704: astore #8
/*     */     //   706: aload #7
/*     */     //   708: invokevirtual close : ()V
/*     */     //   711: goto -> 723
/*     */     //   714: astore #9
/*     */     //   716: aload #8
/*     */     //   718: aload #9
/*     */     //   720: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   723: aload #8
/*     */     //   725: athrow
/*     */     //   726: iinc #6, 1
/*     */     //   729: goto -> 70
/*     */     //   732: iload_3
/*     */     //   733: ifeq -> 742
/*     */     //   736: aload_0
/*     */     //   737: aload_1
/*     */     //   738: aload_2
/*     */     //   739: invokevirtual delay : (Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V
/*     */     //   742: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #90	-> 0
/*     */     //   #91	-> 16
/*     */     //   #94	-> 17
/*     */     //   #95	-> 25
/*     */     //   #98	-> 31
/*     */     //   #99	-> 38
/*     */     //   #100	-> 48
/*     */     //   #103	-> 49
/*     */     //   #105	-> 51
/*     */     //   #106	-> 57
/*     */     //   #107	-> 67
/*     */     //   #108	-> 79
/*     */     //   #109	-> 97
/*     */     //   #110	-> 113
/*     */     //   #111	-> 120
/*     */     //   #112	-> 128
/*     */     //   #185	-> 134
/*     */     //   #113	-> 139
/*     */     //   #116	-> 140
/*     */     //   #122	-> 169
/*     */     //   #185	-> 202
/*     */     //   #123	-> 207
/*     */     //   #126	-> 210
/*     */     //   #127	-> 217
/*     */     //   #128	-> 228
/*     */     //   #185	-> 255
/*     */     //   #129	-> 260
/*     */     //   #132	-> 263
/*     */     //   #133	-> 276
/*     */     //   #185	-> 287
/*     */     //   #134	-> 292
/*     */     //   #136	-> 295
/*     */     //   #137	-> 298
/*     */     //   #185	-> 322
/*     */     //   #138	-> 327
/*     */     //   #142	-> 330
/*     */     //   #146	-> 348
/*     */     //   #147	-> 353
/*     */     //   #185	-> 359
/*     */     //   #148	-> 364
/*     */     //   #151	-> 365
/*     */     //   #152	-> 438
/*     */     //   #153	-> 447
/*     */     //   #185	-> 453
/*     */     //   #154	-> 458
/*     */     //   #157	-> 459
/*     */     //   #158	-> 491
/*     */     //   #159	-> 506
/*     */     //   #185	-> 529
/*     */     //   #160	-> 534
/*     */     //   #162	-> 537
/*     */     //   #185	-> 546
/*     */     //   #163	-> 551
/*     */     //   #166	-> 554
/*     */     //   #167	-> 589
/*     */     //   #168	-> 594
/*     */     //   #171	-> 617
/*     */     //   #174	-> 637
/*     */     //   #175	-> 646
/*     */     //   #185	-> 652
/*     */     //   #176	-> 657
/*     */     //   #179	-> 658
/*     */     //   #180	-> 667
/*     */     //   #181	-> 678
/*     */     //   #182	-> 686
/*     */     //   #184	-> 694
/*     */     //   #185	-> 696
/*     */     //   #108	-> 704
/*     */     //   #107	-> 726
/*     */     //   #188	-> 732
/*     */     //   #189	-> 736
/*     */     //   #191	-> 742
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   276	19	12	customSpawnRules	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/*     */     //   589	48	15	hasNoConfiguration	Z
/*     */     //   506	131	14	mob	Lnet/minecraft/world/entity/Mob;
/*     */     //   113	583	8	input	Lnet/minecraft/world/level/storage/ValueInput;
/*     */     //   120	576	9	entityType	Ljava/util/Optional;
/*     */     //   169	527	10	spawnPos	Lnet/minecraft/world/phys/Vec3;
/*     */     //   217	479	11	spawnBlockPos	Lnet/minecraft/core/BlockPos;
/*     */     //   348	348	12	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   438	258	13	nearBy	I
/*     */     //   97	629	7	reporter	Lnet/minecraft/util/ProblemReporter$ScopedCollector;
/*     */     //   70	662	6	c	I
/*     */     //   0	743	0	this	Lnet/minecraft/world/level/BaseSpawner;
/*     */     //   0	743	1	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	743	2	pos	Lnet/minecraft/core/BlockPos;
/*     */     //   51	692	3	delay	Z
/*     */     //   57	686	4	random	Lnet/minecraft/util/RandomSource;
/*     */     //   67	676	5	nextSpawnData	Lnet/minecraft/world/level/SpawnData;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   120	576	9	entityType	Ljava/util/Optional<Lnet/minecraft/world/entity/EntityType<*>;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   97	134	704	java/lang/Throwable
/*     */     //   140	202	704	java/lang/Throwable
/*     */     //   210	255	704	java/lang/Throwable
/*     */     //   263	287	704	java/lang/Throwable
/*     */     //   295	322	704	java/lang/Throwable
/*     */     //   330	359	704	java/lang/Throwable
/*     */     //   365	453	704	java/lang/Throwable
/*     */     //   459	529	704	java/lang/Throwable
/*     */     //   537	546	704	java/lang/Throwable
/*     */     //   554	652	704	java/lang/Throwable
/*     */     //   658	696	704	java/lang/Throwable
/*     */     //   706	711	714	java/lang/Throwable }
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
/*     */   private void delay(Level level, BlockPos pos) {
/* 194 */     RandomSource random = level.random;
/* 195 */     if (this.maxSpawnDelay <= this.minSpawnDelay) {
/* 196 */       this.spawnDelay = this.minSpawnDelay;
/*     */     } else {
/* 198 */       this.spawnDelay = this.minSpawnDelay + random.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
/*     */     } 
/*     */     
/* 201 */     this.spawnPotentials.getRandom(random).ifPresent(entry -> setNextSpawnData(level, pos, entry));
/*     */     
/* 203 */     broadcastEvent(level, pos, 1);
/*     */   }
/*     */   
/*     */   public void load(Level level, BlockPos pos, ValueInput input) {
/* 207 */     this.spawnDelay = input.getShortOr("Delay", (short)20);
/*     */     
/* 209 */     input.read("SpawnData", SpawnData.CODEC)
/* 210 */       .ifPresent(nextSpawnData -> setNextSpawnData(level, pos, nextSpawnData));
/*     */     
/* 212 */     this
/* 213 */       .spawnPotentials = (WeightedList)input.read("SpawnPotentials", SpawnData.LIST_CODEC).orElseGet(() -> WeightedList.of((this.nextSpawnData != null) ? this.nextSpawnData : new SpawnData()));
/*     */     
/* 215 */     this.minSpawnDelay = input.getIntOr("MinSpawnDelay", 200);
/* 216 */     this.maxSpawnDelay = input.getIntOr("MaxSpawnDelay", 800);
/* 217 */     this.spawnCount = input.getIntOr("SpawnCount", 4);
/*     */     
/* 219 */     this.maxNearbyEntities = input.getIntOr("MaxNearbyEntities", 6);
/* 220 */     this.requiredPlayerRange = input.getIntOr("RequiredPlayerRange", 16);
/*     */     
/* 222 */     this.spawnRange = input.getIntOr("SpawnRange", 4);
/*     */     
/* 224 */     this.displayEntity = null;
/*     */   }
/*     */   
/*     */   public void save(ValueOutput output) {
/* 228 */     output.putShort("Delay", (short)this.spawnDelay);
/* 229 */     output.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
/* 230 */     output.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
/* 231 */     output.putShort("SpawnCount", (short)this.spawnCount);
/* 232 */     output.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
/* 233 */     output.putShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
/* 234 */     output.putShort("SpawnRange", (short)this.spawnRange);
/* 235 */     output.storeNullable("SpawnData", SpawnData.CODEC, this.nextSpawnData);
/* 236 */     output.store("SpawnPotentials", SpawnData.LIST_CODEC, this.spawnPotentials);
/*     */   }
/*     */   
/*     */   public Entity getOrCreateDisplayEntity(Level level, BlockPos pos) {
/* 240 */     if (this.displayEntity == null) {
/* 241 */       CompoundTag entityToSpawn = getOrCreateNextSpawnData(level, level.getRandom(), pos).getEntityToSpawn();
/* 242 */       if (entityToSpawn.getString("id").isEmpty()) {
/* 243 */         return null;
/*     */       }
/* 245 */       this.displayEntity = EntityType.loadEntityRecursive(entityToSpawn, level, EntitySpawnReason.SPAWNER, EntityProcessor.NOP);
/* 246 */       if (entityToSpawn.size() != 1 || this.displayEntity instanceof net.minecraft.world.entity.Mob);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     return this.displayEntity;
/*     */   }
/*     */   
/*     */   public boolean onEventTriggered(Level level, int id) {
/* 256 */     if (id == 1) {
/* 257 */       if (level.isClientSide()) {
/* 258 */         this.spawnDelay = this.minSpawnDelay;
/*     */       }
/* 260 */       return true;
/*     */     } 
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 266 */   protected void setNextSpawnData(Level level, BlockPos pos, SpawnData nextSpawnData) { this.nextSpawnData = nextSpawnData; }
/*     */ 
/*     */   
/*     */   private SpawnData getOrCreateNextSpawnData(Level level, RandomSource random, BlockPos pos) {
/* 270 */     if (this.nextSpawnData != null) {
/* 271 */       return this.nextSpawnData;
/*     */     }
/* 273 */     setNextSpawnData(level, pos, (SpawnData)this.spawnPotentials.getRandom(random).orElseGet(SpawnData::new));
/* 274 */     return this.nextSpawnData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   public double getSpin() { return this.spin; }
/*     */ 
/*     */ 
/*     */   
/* 284 */   public double getOSpin() { return this.oSpin; }
/*     */   
/*     */   public abstract void broadcastEvent(Level paramLevel, BlockPos paramBlockPos, int paramInt);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\BaseSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */