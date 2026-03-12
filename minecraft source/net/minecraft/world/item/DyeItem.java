/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignText;
/*    */ 
/*    */ public class DyeItem extends Item implements SignApplicator {
/* 17 */   private static final Map<DyeColor, DyeItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);
/*    */   
/*    */   private final DyeColor dyeColor;
/*    */   
/*    */   public DyeItem(DyeColor dyeColor, Item.Properties properties) {
/* 22 */     super(properties);
/* 23 */     this.dyeColor = dyeColor;
/* 24 */     ITEM_BY_COLOR.put(dyeColor, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
/* 29 */     if (target instanceof Sheep) { Sheep sheep = (Sheep)target;
/* 30 */       if (sheep.isAlive() && !sheep.isSheared() && sheep.getColor() != this.dyeColor) {
/* 31 */         sheep.level().playSound(player, sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
/* 32 */         if (!player.level().isClientSide()) {
/* 33 */           sheep.setColor(this.dyeColor);
/* 34 */           itemStack.shrink(1);
/*    */         } 
/* 36 */         return InteractionResult.SUCCESS;
/*    */       }  }
/*    */     
/* 39 */     return InteractionResult.PASS;
/*    */   }
/*    */ 
/*    */   
/* 43 */   public DyeColor getDyeColor() { return this.dyeColor; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static DyeItem byColor(DyeColor color) { return (DyeItem)ITEM_BY_COLOR.get(color); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, Player player) {
/* 52 */     if (sign.updateText(text -> text.setColor(getDyeColor()), isFrontText)) {
/* 53 */       level.playSound(null, sign.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 54 */       return true;
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\DyeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */