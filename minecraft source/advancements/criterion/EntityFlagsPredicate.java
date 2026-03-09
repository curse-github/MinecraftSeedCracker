/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ 
/*     */ public final class EntityFlagsPredicate extends Record {
/*     */   private final Optional<Boolean> isOnGround;
/*     */   private final Optional<Boolean> isOnFire;
/*     */   private final Optional<Boolean> isCrouching;
/*     */   private final Optional<Boolean> isSprinting;
/*     */   
/*  11 */   public EntityFlagsPredicate(Optional<Boolean> isOnGround, Optional<Boolean> isOnFire, Optional<Boolean> isCrouching, Optional<Boolean> isSprinting, Optional<Boolean> isSwimming, Optional<Boolean> isFlying, Optional<Boolean> isBaby, Optional<Boolean> isInWater, Optional<Boolean> isFallFlying) { this.isOnGround = isOnGround; this.isOnFire = isOnFire; this.isCrouching = isCrouching; this.isSprinting = isSprinting; this.isSwimming = isSwimming; this.isFlying = isFlying; this.isBaby = isBaby; this.isInWater = isInWater; this.isFallFlying = isFallFlying; } private final Optional<Boolean> isSwimming; private final Optional<Boolean> isFlying; private final Optional<Boolean> isBaby; private final Optional<Boolean> isInWater; private final Optional<Boolean> isFallFlying; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityFlagsPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityFlagsPredicate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityFlagsPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityFlagsPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityFlagsPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #11	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityFlagsPredicate;
/*  11 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<Boolean> isOnGround() { return this.isOnGround; } public Optional<Boolean> isOnFire() { return this.isOnFire; } public Optional<Boolean> isCrouching() { return this.isCrouching; } public Optional<Boolean> isSprinting() { return this.isSprinting; } public Optional<Boolean> isSwimming() { return this.isSwimming; } public Optional<Boolean> isFlying() { return this.isFlying; } public Optional<Boolean> isBaby() { return this.isBaby; } public Optional<Boolean> isInWater() { return this.isInWater; } public Optional<Boolean> isFallFlying() { return this.isFallFlying; }
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
/*  22 */   public static final Codec<EntityFlagsPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/*  23 */         .optionalFieldOf("is_on_ground").forGetter(EntityFlagsPredicate::isOnGround), Codec.BOOL
/*  24 */         .optionalFieldOf("is_on_fire").forGetter(EntityFlagsPredicate::isOnFire), Codec.BOOL
/*     */         
/*  26 */         .optionalFieldOf("is_sneaking").forGetter(EntityFlagsPredicate::isCrouching), Codec.BOOL
/*  27 */         .optionalFieldOf("is_sprinting").forGetter(EntityFlagsPredicate::isSprinting), Codec.BOOL
/*  28 */         .optionalFieldOf("is_swimming").forGetter(EntityFlagsPredicate::isSwimming), Codec.BOOL
/*  29 */         .optionalFieldOf("is_flying").forGetter(EntityFlagsPredicate::isFlying), Codec.BOOL
/*  30 */         .optionalFieldOf("is_baby").forGetter(EntityFlagsPredicate::isBaby), Codec.BOOL
/*  31 */         .optionalFieldOf("is_in_water").forGetter(EntityFlagsPredicate::isInWater), Codec.BOOL
/*  32 */         .optionalFieldOf("is_fall_flying").forGetter(EntityFlagsPredicate::isFallFlying))
/*  33 */       .apply(i, EntityFlagsPredicate::new));
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
/*     */   public boolean matches(Entity entity) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield isOnGround : Ljava/util/Optional;
/*     */     //   4: invokevirtual isPresent : ()Z
/*     */     //   7: ifeq -> 32
/*     */     //   10: aload_1
/*     */     //   11: invokevirtual onGround : ()Z
/*     */     //   14: aload_0
/*     */     //   15: getfield isOnGround : Ljava/util/Optional;
/*     */     //   18: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   21: checkcast java/lang/Boolean
/*     */     //   24: invokevirtual booleanValue : ()Z
/*     */     //   27: if_icmpeq -> 32
/*     */     //   30: iconst_0
/*     */     //   31: ireturn
/*     */     //   32: aload_0
/*     */     //   33: getfield isOnFire : Ljava/util/Optional;
/*     */     //   36: invokevirtual isPresent : ()Z
/*     */     //   39: ifeq -> 64
/*     */     //   42: aload_1
/*     */     //   43: invokevirtual isOnFire : ()Z
/*     */     //   46: aload_0
/*     */     //   47: getfield isOnFire : Ljava/util/Optional;
/*     */     //   50: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   53: checkcast java/lang/Boolean
/*     */     //   56: invokevirtual booleanValue : ()Z
/*     */     //   59: if_icmpeq -> 64
/*     */     //   62: iconst_0
/*     */     //   63: ireturn
/*     */     //   64: aload_0
/*     */     //   65: getfield isCrouching : Ljava/util/Optional;
/*     */     //   68: invokevirtual isPresent : ()Z
/*     */     //   71: ifeq -> 96
/*     */     //   74: aload_1
/*     */     //   75: invokevirtual isCrouching : ()Z
/*     */     //   78: aload_0
/*     */     //   79: getfield isCrouching : Ljava/util/Optional;
/*     */     //   82: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   85: checkcast java/lang/Boolean
/*     */     //   88: invokevirtual booleanValue : ()Z
/*     */     //   91: if_icmpeq -> 96
/*     */     //   94: iconst_0
/*     */     //   95: ireturn
/*     */     //   96: aload_0
/*     */     //   97: getfield isSprinting : Ljava/util/Optional;
/*     */     //   100: invokevirtual isPresent : ()Z
/*     */     //   103: ifeq -> 128
/*     */     //   106: aload_1
/*     */     //   107: invokevirtual isSprinting : ()Z
/*     */     //   110: aload_0
/*     */     //   111: getfield isSprinting : Ljava/util/Optional;
/*     */     //   114: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   117: checkcast java/lang/Boolean
/*     */     //   120: invokevirtual booleanValue : ()Z
/*     */     //   123: if_icmpeq -> 128
/*     */     //   126: iconst_0
/*     */     //   127: ireturn
/*     */     //   128: aload_0
/*     */     //   129: getfield isSwimming : Ljava/util/Optional;
/*     */     //   132: invokevirtual isPresent : ()Z
/*     */     //   135: ifeq -> 160
/*     */     //   138: aload_1
/*     */     //   139: invokevirtual isSwimming : ()Z
/*     */     //   142: aload_0
/*     */     //   143: getfield isSwimming : Ljava/util/Optional;
/*     */     //   146: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   149: checkcast java/lang/Boolean
/*     */     //   152: invokevirtual booleanValue : ()Z
/*     */     //   155: if_icmpeq -> 160
/*     */     //   158: iconst_0
/*     */     //   159: ireturn
/*     */     //   160: aload_0
/*     */     //   161: getfield isFlying : Ljava/util/Optional;
/*     */     //   164: invokevirtual isPresent : ()Z
/*     */     //   167: ifeq -> 240
/*     */     //   170: aload_1
/*     */     //   171: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   174: ifeq -> 219
/*     */     //   177: aload_1
/*     */     //   178: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   181: astore #4
/*     */     //   183: aload #4
/*     */     //   185: invokevirtual isFallFlying : ()Z
/*     */     //   188: ifne -> 215
/*     */     //   191: aload #4
/*     */     //   193: instanceof net/minecraft/world/entity/player/Player
/*     */     //   196: ifeq -> 219
/*     */     //   199: aload #4
/*     */     //   201: checkcast net/minecraft/world/entity/player/Player
/*     */     //   204: astore_3
/*     */     //   205: aload_3
/*     */     //   206: invokevirtual getAbilities : ()Lnet/minecraft/world/entity/player/Abilities;
/*     */     //   209: getfield flying : Z
/*     */     //   212: ifeq -> 219
/*     */     //   215: iconst_1
/*     */     //   216: goto -> 220
/*     */     //   219: iconst_0
/*     */     //   220: istore_2
/*     */     //   221: iload_2
/*     */     //   222: aload_0
/*     */     //   223: getfield isFlying : Ljava/util/Optional;
/*     */     //   226: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   229: checkcast java/lang/Boolean
/*     */     //   232: invokevirtual booleanValue : ()Z
/*     */     //   235: if_icmpeq -> 240
/*     */     //   238: iconst_0
/*     */     //   239: ireturn
/*     */     //   240: aload_0
/*     */     //   241: getfield isInWater : Ljava/util/Optional;
/*     */     //   244: invokevirtual isPresent : ()Z
/*     */     //   247: ifeq -> 272
/*     */     //   250: aload_1
/*     */     //   251: invokevirtual isInWater : ()Z
/*     */     //   254: aload_0
/*     */     //   255: getfield isInWater : Ljava/util/Optional;
/*     */     //   258: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   261: checkcast java/lang/Boolean
/*     */     //   264: invokevirtual booleanValue : ()Z
/*     */     //   267: if_icmpeq -> 272
/*     */     //   270: iconst_0
/*     */     //   271: ireturn
/*     */     //   272: aload_0
/*     */     //   273: getfield isFallFlying : Ljava/util/Optional;
/*     */     //   276: invokevirtual isPresent : ()Z
/*     */     //   279: ifeq -> 316
/*     */     //   282: aload_1
/*     */     //   283: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   286: ifeq -> 316
/*     */     //   289: aload_1
/*     */     //   290: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   293: astore_2
/*     */     //   294: aload_2
/*     */     //   295: invokevirtual isFallFlying : ()Z
/*     */     //   298: aload_0
/*     */     //   299: getfield isFallFlying : Ljava/util/Optional;
/*     */     //   302: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   305: checkcast java/lang/Boolean
/*     */     //   308: invokevirtual booleanValue : ()Z
/*     */     //   311: if_icmpeq -> 316
/*     */     //   314: iconst_0
/*     */     //   315: ireturn
/*     */     //   316: aload_0
/*     */     //   317: getfield isBaby : Ljava/util/Optional;
/*     */     //   320: invokevirtual isPresent : ()Z
/*     */     //   323: ifeq -> 360
/*     */     //   326: aload_1
/*     */     //   327: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   330: ifeq -> 360
/*     */     //   333: aload_1
/*     */     //   334: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   337: astore_2
/*     */     //   338: aload_2
/*     */     //   339: invokevirtual isBaby : ()Z
/*     */     //   342: aload_0
/*     */     //   343: getfield isBaby : Ljava/util/Optional;
/*     */     //   346: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   349: checkcast java/lang/Boolean
/*     */     //   352: invokevirtual booleanValue : ()Z
/*     */     //   355: if_icmpeq -> 360
/*     */     //   358: iconst_0
/*     */     //   359: ireturn
/*     */     //   360: iconst_1
/*     */     //   361: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #36	-> 0
/*     */     //   #37	-> 30
/*     */     //   #40	-> 32
/*     */     //   #41	-> 62
/*     */     //   #44	-> 64
/*     */     //   #45	-> 94
/*     */     //   #48	-> 96
/*     */     //   #49	-> 126
/*     */     //   #52	-> 128
/*     */     //   #53	-> 158
/*     */     //   #56	-> 160
/*     */     //   #57	-> 170
/*     */     //   #58	-> 221
/*     */     //   #59	-> 238
/*     */     //   #63	-> 240
/*     */     //   #64	-> 270
/*     */     //   #67	-> 272
/*     */     //   #68	-> 314
/*     */     //   #71	-> 316
/*     */     //   #72	-> 358
/*     */     //   #75	-> 360
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   205	10	3	player	Lnet/minecraft/world/entity/player/Player;
/*     */     //   183	36	4	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   221	19	2	entityIsFlying	Z
/*     */     //   294	22	2	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   338	22	2	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	362	0	this	Lnet/minecraft/advancements/criterion/EntityFlagsPredicate;
/*     */     //   0	362	1	entity	Lnet/minecraft/world/entity/Entity; }
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
/*     */   public static class Builder
/*     */   {
/*  79 */     private Optional<Boolean> isOnGround = Optional.empty();
/*  80 */     private Optional<Boolean> isOnFire = Optional.empty();
/*  81 */     private Optional<Boolean> isCrouching = Optional.empty();
/*  82 */     private Optional<Boolean> isSprinting = Optional.empty();
/*  83 */     private Optional<Boolean> isSwimming = Optional.empty();
/*  84 */     private Optional<Boolean> isFlying = Optional.empty();
/*  85 */     private Optional<Boolean> isBaby = Optional.empty();
/*  86 */     private Optional<Boolean> isInWater = Optional.empty();
/*  87 */     private Optional<Boolean> isFallFlying = Optional.empty();
/*     */ 
/*     */     
/*  90 */     public static Builder flags() { return new Builder(); }
/*     */ 
/*     */     
/*     */     public Builder setOnGround(Boolean onGround) {
/*  94 */       this.isOnGround = Optional.of(onGround);
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setOnFire(Boolean onFire) {
/*  99 */       this.isOnFire = Optional.of(onFire);
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCrouching(Boolean crouching) {
/* 104 */       this.isCrouching = Optional.of(crouching);
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSprinting(Boolean sprinting) {
/* 109 */       this.isSprinting = Optional.of(sprinting);
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSwimming(Boolean swimming) {
/* 114 */       this.isSwimming = Optional.of(swimming);
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsFlying(Boolean flying) {
/* 119 */       this.isFlying = Optional.of(flying);
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsBaby(Boolean baby) {
/* 124 */       this.isBaby = Optional.of(baby);
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsInWater(Boolean inWater) {
/* 129 */       this.isInWater = Optional.of(inWater);
/* 130 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIsFallFlying(Boolean fallFlying) {
/* 134 */       this.isFallFlying = Optional.of(fallFlying);
/* 135 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 139 */     public EntityFlagsPredicate build() { return new EntityFlagsPredicate(this.isOnGround, this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isFlying, this.isBaby, this.isInWater, this.isFallFlying); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\EntityFlagsPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */