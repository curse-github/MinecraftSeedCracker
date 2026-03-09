/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class PotionItem
/*    */   extends Item {
/* 24 */   public PotionItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack getDefaultInstance() {
/* 29 */     ItemStack itemStack = super.getDefaultInstance();
/* 30 */     itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
/* 31 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 36 */     Level level = context.getLevel();
/* 37 */     BlockPos pos = context.getClickedPos();
/* 38 */     Player player = context.getPlayer();
/* 39 */     ItemStack itemStack = context.getItemInHand();
/* 40 */     PotionContents potionContents = (PotionContents)itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*    */     
/* 42 */     BlockState blockState = level.getBlockState(pos);
/* 43 */     if (context.getClickedFace() != Direction.DOWN && blockState.is(BlockTags.CONVERTABLE_TO_MUD) && potionContents.is(Potions.WATER)) {
/* 44 */       level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
/*    */       
/* 46 */       player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
/*    */       
/* 48 */       if (!level.isClientSide()) {
/* 49 */         ServerLevel serverLevel = (ServerLevel)level;
/* 50 */         for (int i = 0; i < 5; i++) {
/* 51 */           serverLevel.sendParticles(ParticleTypes.SPLASH, pos.getX() + level.random.nextDouble(), (pos.getY() + 1), pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/*    */         }
/*    */       } 
/*    */       
/* 55 */       level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 56 */       level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
/*    */       
/* 58 */       level.setBlockAndUpdate(pos, Blocks.MUD.defaultBlockState());
/* 59 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 62 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getName(ItemStack itemStack) {
/* 67 */     PotionContents potion = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
/* 68 */     return (potion != null) ? potion.getName(this.descriptionId + ".effect.") : super.getName(itemStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\PotionItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */