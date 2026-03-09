/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.LodestoneTracker;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class CompassItem
/*    */   extends Item
/*    */ {
/* 23 */   private static final Component LODESTONE_COMPASS_NAME = Component.translatable("item.minecraft.lodestone_compass");
/*    */ 
/*    */   
/* 26 */   public CompassItem(Item.Properties properties) { super(properties); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean isFoil(ItemStack itemStack) { return (itemStack.has(DataComponents.LODESTONE_TRACKER) || super.isFoil(itemStack)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot) {
/* 36 */     LodestoneTracker tracker = (LodestoneTracker)itemStack.get(DataComponents.LODESTONE_TRACKER);
/* 37 */     if (tracker != null) {
/* 38 */       LodestoneTracker newTracker = tracker.tick(level);
/* 39 */       if (newTracker != tracker) {
/* 40 */         itemStack.set(DataComponents.LODESTONE_TRACKER, newTracker);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/* 47 */     BlockPos blockPos = context.getClickedPos();
/* 48 */     Level level = context.getLevel();
/*    */     
/* 50 */     if (level.getBlockState(blockPos).is(Blocks.LODESTONE)) {
/* 51 */       level.playSound(null, blockPos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
/*    */       
/* 53 */       Player player = context.getPlayer();
/* 54 */       ItemStack itemStack = context.getItemInHand();
/* 55 */       boolean replaceExistingStack = (!player.hasInfiniteMaterials() && itemStack.getCount() == 1);
/*    */       
/* 57 */       LodestoneTracker target = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockPos)), true);
/* 58 */       if (replaceExistingStack) {
/* 59 */         itemStack.set(DataComponents.LODESTONE_TRACKER, target);
/*    */       } else {
/* 61 */         ItemStack lodestoneCompass = itemStack.transmuteCopy(Items.COMPASS, 1);
/* 62 */         itemStack.consume(1, player);
/* 63 */         lodestoneCompass.set(DataComponents.LODESTONE_TRACKER, target);
/* 64 */         if (!player.getInventory().add(lodestoneCompass)) {
/* 65 */           player.drop(lodestoneCompass, false);
/*    */         }
/*    */       } 
/*    */       
/* 69 */       return InteractionResult.SUCCESS;
/*    */     } 
/* 71 */     return super.useOn(context);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public Component getName(ItemStack itemStack) { return itemStack.has(DataComponents.LODESTONE_TRACKER) ? LODESTONE_COMPASS_NAME : super.getName(itemStack); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\CompassItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */