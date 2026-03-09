/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
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
/*    */ public final class BlockResult
/*    */   extends Record
/*    */ {
/*    */   private final BlockState blockState;
/*    */   private final Map<Property<?>, Comparable<?>> properties;
/*    */   private final CompoundTag nbt;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #75	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 75 */   public BlockResult(BlockState blockState, Map<Property<?>, Comparable<?>> properties, CompoundTag nbt) { this.blockState = blockState; this.properties = properties; this.nbt = nbt; } public BlockState blockState() { return this.blockState; } public Map<Property<?>, Comparable<?>> properties() { return this.properties; } public CompoundTag nbt() { return this.nbt; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser$BlockResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */