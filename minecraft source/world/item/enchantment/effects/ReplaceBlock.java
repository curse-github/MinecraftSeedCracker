/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class ReplaceBlock extends Record implements EnchantmentEntityEffect {
/*    */   private final Vec3i offset;
/*    */   private final Optional<BlockPredicate> predicate;
/*    */   private final BlockStateProvider blockState;
/*    */   private final Optional<Holder<GameEvent>> triggerGameEvent;
/*    */   
/* 18 */   public ReplaceBlock(Vec3i offset, Optional<BlockPredicate> predicate, BlockStateProvider blockState, Optional<Holder<GameEvent>> triggerGameEvent) { this.offset = offset; this.predicate = predicate; this.blockState = blockState; this.triggerGameEvent = triggerGameEvent; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock; } public Vec3i offset() { return this.offset; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/ReplaceBlock;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<BlockPredicate> predicate() { return this.predicate; } public BlockStateProvider blockState() { return this.blockState; } public Optional<Holder<GameEvent>> triggerGameEvent() { return this.triggerGameEvent; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final MapCodec<ReplaceBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Vec3i.CODEC
/* 25 */         .optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceBlock::offset), BlockPredicate.CODEC
/* 26 */         .optionalFieldOf("predicate").forGetter(ReplaceBlock::predicate), BlockStateProvider.CODEC
/* 27 */         .fieldOf("block_state").forGetter(ReplaceBlock::blockState), GameEvent.CODEC
/* 28 */         .optionalFieldOf("trigger_game_event").forGetter(ReplaceBlock::triggerGameEvent))
/* 29 */       .apply(i, ReplaceBlock::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 33 */     BlockPos pos = BlockPos.containing(position).offset(this.offset);
/* 34 */     if (((Boolean)this.predicate.map(p -> Boolean.valueOf(p.test(serverLevel, pos))).orElse(Boolean.valueOf(true))).booleanValue() && 
/* 35 */       serverLevel.setBlockAndUpdate(pos, this.blockState.getState(entity.getRandom(), pos))) {
/* 36 */       this.triggerGameEvent.ifPresent(event -> serverLevel.gameEvent(entity, event, pos));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public MapCodec<ReplaceBlock> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\ReplaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */