/*    */ package net.minecraft.world.item;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public final class JukeboxPlayable extends Record implements TooltipProvider {
/*    */   private final EitherHolder<JukeboxSong> song;
/*    */   
/* 28 */   public JukeboxPlayable(EitherHolder<JukeboxSong> song) { this.song = song; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/JukeboxPlayable;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 28 */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxPlayable; } public EitherHolder<JukeboxSong> song() { return this.song; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/JukeboxPlayable;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/JukeboxPlayable; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/JukeboxPlayable;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/JukeboxPlayable;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 29 */   public static final Codec<JukeboxPlayable> CODEC = EitherHolder.codec(Registries.JUKEBOX_SONG, JukeboxSong.CODEC).xmap(JukeboxPlayable::new, JukeboxPlayable::song);
/* 30 */   public static final StreamCodec<RegistryFriendlyByteBuf, JukeboxPlayable> STREAM_CODEC = StreamCodec.composite(
/* 31 */       EitherHolder.streamCodec(Registries.JUKEBOX_SONG, JukeboxSong.STREAM_CODEC), JukeboxPlayable::song, JukeboxPlayable::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 37 */     HolderLookup.Provider registries = context.registries();
/* 38 */     if (registries != null) {
/* 39 */       this.song.unwrap(registries).ifPresent(reference -> {
/* 40 */             Component description = ComponentUtils.mergeStyles(((JukeboxSong)reference.value()).description(), Style.EMPTY.withColor(ChatFormatting.GRAY));
/* 41 */             consumer.accept(description);
/*    */           });
/*    */     }
/*    */   }
/*    */   
/*    */   public static InteractionResult tryInsertIntoJukebox(Level level, BlockPos pos, ItemStack toInsert, Player player) {
/* 47 */     JukeboxPlayable jukeboxPlayable = (JukeboxPlayable)toInsert.get(DataComponents.JUKEBOX_PLAYABLE);
/* 48 */     if (jukeboxPlayable == null) {
/* 49 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*    */     }
/*    */     
/* 52 */     BlockState state = level.getBlockState(pos);
/* 53 */     if (!state.is(Blocks.JUKEBOX) || ((Boolean)state.getValue(JukeboxBlock.HAS_RECORD)).booleanValue()) {
/* 54 */       return InteractionResult.TRY_WITH_EMPTY_HAND;
/*    */     }
/*    */     
/* 57 */     if (!level.isClientSide()) {
/* 58 */       ItemStack inserted = toInsert.consumeAndReturn(1, player);
/* 59 */       BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/* 60 */         jukebox.setTheItem(inserted);
/* 61 */         level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state)); }
/*    */ 
/*    */       
/* 64 */       player.awardStat(Stats.PLAY_RECORD);
/*    */     } 
/*    */     
/* 67 */     return InteractionResult.SUCCESS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\JukeboxPlayable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */