/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Data
/*    */   extends Record
/*    */ {
/*    */   private final boolean needsStateScanning;
/*    */   private final boolean dragonKilled;
/*    */   private final boolean previouslyKilled;
/*    */   private final boolean isRespawning;
/*    */   private final Optional<UUID> dragonUUID;
/*    */   private final Optional<BlockPos> exitPortalLocation;
/*    */   private final Optional<List<Integer>> gateways;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/end/EndDragonFight$Data;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 68 */   public Data(boolean needsStateScanning, boolean dragonKilled, boolean previouslyKilled, boolean isRespawning, Optional<UUID> dragonUUID, Optional<BlockPos> exitPortalLocation, Optional<List<Integer>> gateways) { this.needsStateScanning = needsStateScanning; this.dragonKilled = dragonKilled; this.previouslyKilled = previouslyKilled; this.isRespawning = isRespawning; this.dragonUUID = dragonUUID; this.exitPortalLocation = exitPortalLocation; this.gateways = gateways; } public boolean needsStateScanning() { return this.needsStateScanning; } public boolean dragonKilled() { return this.dragonKilled; } public boolean previouslyKilled() { return this.previouslyKilled; } public boolean isRespawning() { return this.isRespawning; } public Optional<UUID> dragonUUID() { return this.dragonUUID; } public Optional<BlockPos> exitPortalLocation() { return this.exitPortalLocation; } public Optional<List<Integer>> gateways() { return this.gateways; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 78 */         .fieldOf("NeedsStateScanning").orElse(Boolean.valueOf(true)).forGetter(Data::needsStateScanning), Codec.BOOL
/* 79 */         .fieldOf("DragonKilled").orElse(Boolean.valueOf(false)).forGetter(Data::dragonKilled), Codec.BOOL
/* 80 */         .fieldOf("PreviouslyKilled").orElse(Boolean.valueOf(false)).forGetter(Data::previouslyKilled), Codec.BOOL
/*    */         
/* 82 */         .lenientOptionalFieldOf("IsRespawning", Boolean.valueOf(false)).forGetter(Data::isRespawning), UUIDUtil.CODEC
/* 83 */         .lenientOptionalFieldOf("Dragon").forGetter(Data::dragonUUID), BlockPos.CODEC
/* 84 */         .lenientOptionalFieldOf("ExitPortalLocation").forGetter(Data::exitPortalLocation), 
/* 85 */         Codec.list(Codec.INT).lenientOptionalFieldOf("Gateways").forGetter(Data::gateways))
/* 86 */       .apply(i, Data::new));
/*    */   
/* 88 */   public static final Data DEFAULT = new Data(true, false, false, false, Optional.empty(), Optional.empty(), Optional.empty());
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\end\EndDragonFight$Data.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */