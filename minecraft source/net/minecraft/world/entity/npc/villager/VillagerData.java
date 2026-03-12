/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class VillagerData extends Record {
/*    */   private final Holder<VillagerType> type;
/*    */   private final Holder<VillagerProfession> profession;
/*    */   private final int level;
/*    */   public static final int MIN_VILLAGER_LEVEL = 1;
/*    */   
/* 14 */   public Holder<VillagerType> type() { return this.type; } public static final int MAX_VILLAGER_LEVEL = 5; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<VillagerProfession> profession() { return this.profession; } public int level() { return this.level; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   private static final int[] NEXT_LEVEL_XP_THRESHOLDS = { 0, 10, 70, 150, 250 };
/*    */   
/* 23 */   public static final Codec<VillagerData> CODEC = RecordCodecBuilder.create(i -> i.group(BuiltInRegistries.VILLAGER_TYPE
/* 24 */         .holderByNameCodec().fieldOf("type").orElseGet(()).forGetter(()), BuiltInRegistries.VILLAGER_PROFESSION
/* 25 */         .holderByNameCodec().fieldOf("profession").orElseGet(()).forGetter(()), Codec.INT
/* 26 */         .fieldOf("level").orElse(Integer.valueOf(1)).forGetter(()))
/* 27 */       .apply(i, VillagerData::new));
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, VillagerData> STREAM_CODEC = StreamCodec.composite(
/* 30 */       ByteBufCodecs.holderRegistry(Registries.VILLAGER_TYPE), VillagerData::type, 
/* 31 */       ByteBufCodecs.holderRegistry(Registries.VILLAGER_PROFESSION), VillagerData::profession, ByteBufCodecs.VAR_INT, VillagerData::level, VillagerData::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VillagerData(Holder<VillagerType> type, Holder<VillagerProfession> profession, int level) {
/* 37 */     level = Math.max(1, level);
/*    */     this.type = type;
/*    */     this.profession = profession;
/*    */     this.level = level;
/* 41 */   } public VillagerData withType(Holder<VillagerType> type) { return new VillagerData(type, this.profession, this.level); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public VillagerData withType(HolderGetter.Provider registries, ResourceKey<VillagerType> type) { return withType(registries.getOrThrow(type)); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public VillagerData withProfession(Holder<VillagerProfession> profession) { return new VillagerData(this.type, profession, this.level); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public VillagerData withProfession(HolderGetter.Provider registries, ResourceKey<VillagerProfession> profession) { return withProfession(registries.getOrThrow(profession)); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public VillagerData withLevel(int level) { return new VillagerData(this.type, this.profession, level); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static int getMinXpPerLevel(int level) { return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level - 1] : 0; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public static int getMaxXpPerLevel(int level) { return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level] : 0; }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public static boolean canLevelUp(int currentLevel) { return (currentLevel >= 1 && currentLevel < 5); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\villager\VillagerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */