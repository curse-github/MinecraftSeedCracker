/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.advancements.CriteriaTriggers;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.item.alchemy.Potions;
/*    */ import net.minecraft.world.level.ClipContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ 
/*    */ public class BottleItem
/*    */   extends Item
/*    */ {
/* 27 */   public BottleItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 32 */     List<AreaEffectCloud> clouds = level.getEntitiesOfClass(AreaEffectCloud.class, player.getBoundingBox().inflate(2.0D), input -> (input.isAlive() && input.getOwner() instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon));
/*    */     
/* 34 */     ItemStack itemStack = player.getItemInHand(hand);
/*    */     
/* 36 */     if (!clouds.isEmpty()) {
/* 37 */       AreaEffectCloud cloud = (AreaEffectCloud)clouds.get(0);
/* 38 */       cloud.setRadius(cloud.getRadius() - 0.5F);
/*    */       
/* 40 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 41 */       level.gameEvent(player, GameEvent.FLUID_PICKUP, player.position());
/* 42 */       if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 43 */         CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, cloud); }
/*    */ 
/*    */       
/* 46 */       return InteractionResult.SUCCESS.heldItemTransformedTo(turnBottleIntoItem(itemStack, player, new ItemStack(Items.DRAGON_BREATH)));
/*    */     } 
/*    */     
/* 49 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
/* 50 */     if (hitResult.getType() == HitResult.Type.MISS) {
/* 51 */       return InteractionResult.PASS;
/*    */     }
/*    */     
/* 54 */     if (hitResult.getType() == HitResult.Type.BLOCK) {
/* 55 */       BlockPos pos = hitResult.getBlockPos();
/*    */       
/* 57 */       if (!level.mayInteract(player, pos)) {
/* 58 */         return InteractionResult.PASS;
/*    */       }
/* 60 */       if (level.getFluidState(pos).is(FluidTags.WATER)) {
/* 61 */         level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 62 */         level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
/* 63 */         return InteractionResult.SUCCESS.heldItemTransformedTo(turnBottleIntoItem(itemStack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
/*    */       } 
/*    */     } 
/*    */     
/* 67 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   protected ItemStack turnBottleIntoItem(ItemStack itemStack, Player player, ItemStack itemStackToTurnInto) {
/* 71 */     player.awardStat(Stats.ITEM_USED.get(this));
/* 72 */     return ItemUtils.createFilledResult(itemStack, player, itemStackToTurnInto);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BottleItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */