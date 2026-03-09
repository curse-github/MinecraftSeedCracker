/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
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
/*    */ final class BlockEntitySource
/*    */   extends Record
/*    */   implements LootContextArg.Getter<BlockEntity, Tag>
/*    */ {
/*    */   private final ContextKey<? extends BlockEntity> contextParam;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource; }
/*    */   
/* 54 */   private BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) { this.contextParam = contextParam; } public ContextKey<? extends BlockEntity> contextParam() { return this.contextParam; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #54	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 59 */   public Tag get(BlockEntity blockEntity) { return blockEntity.saveWithFullMetadata(blockEntity.getLevel().registryAccess()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\nbt\ContextNbtProvider$BlockEntitySource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */