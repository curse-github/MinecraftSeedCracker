/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.decoration.GlowItemFrame;
/*    */ import net.minecraft.world.entity.decoration.HangingEntity;
/*    */ import net.minecraft.world.entity.decoration.ItemFrame;
/*    */ import net.minecraft.world.entity.decoration.painting.Painting;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.TooltipDisplay;
/*    */ import net.minecraft.world.item.context.UseOnContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class HangingEntityItem
/*    */   extends Item {
/* 26 */   private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
/*    */   
/*    */   private final EntityType<? extends HangingEntity> type;
/*    */   
/*    */   public HangingEntityItem(EntityType<? extends HangingEntity> type, Item.Properties properties) {
/* 31 */     super(properties);
/* 32 */     this.type = type;
/*    */   }
/*    */   
/*    */   public InteractionResult useOn(UseOnContext context) {
/*    */     GlowItemFrame glowItemFrame;
/* 37 */     BlockPos pos = context.getClickedPos();
/* 38 */     Direction clickedFace = context.getClickedFace();
/*    */     
/* 40 */     BlockPos blockPos = pos.relative(clickedFace);
/* 41 */     Player player = context.getPlayer();
/* 42 */     ItemStack itemInHand = context.getItemInHand();
/*    */     
/* 44 */     if (player != null && !mayPlace(player, clickedFace, itemInHand, blockPos)) {
/* 45 */       return InteractionResult.FAIL;
/*    */     }
/*    */     
/* 48 */     Level level = context.getLevel();
/*    */     
/* 50 */     if (this.type == EntityType.PAINTING) {
/* 51 */       Optional<Painting> painting = Painting.create(level, blockPos, clickedFace);
/* 52 */       if (painting.isEmpty()) {
/* 53 */         return InteractionResult.CONSUME;
/*    */       }
/* 55 */       glowItemFrame = (HangingEntity)painting.get();
/* 56 */     } else if (this.type == EntityType.ITEM_FRAME) {
/* 57 */       glowItemFrame = new ItemFrame(level, blockPos, clickedFace);
/* 58 */     } else if (this.type == EntityType.GLOW_ITEM_FRAME) {
/* 59 */       glowItemFrame = new GlowItemFrame(level, blockPos, clickedFace);
/*    */     } else {
/* 61 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 64 */     EntityType.createDefaultStackConfig(level, itemInHand, player).accept(glowItemFrame);
/*    */     
/* 66 */     if (glowItemFrame.survives()) {
/* 67 */       if (!level.isClientSide()) {
/* 68 */         glowItemFrame.playPlacementSound();
/* 69 */         level.gameEvent(player, GameEvent.ENTITY_PLACE, glowItemFrame.position());
/* 70 */         level.addFreshEntity(glowItemFrame);
/*    */       } 
/* 72 */       itemInHand.shrink(1);
/* 73 */       return InteractionResult.SUCCESS;
/*    */     } 
/*    */     
/* 76 */     return InteractionResult.CONSUME;
/*    */   }
/*    */ 
/*    */   
/* 80 */   protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) { return (!direction.getAxis().isVertical() && player.mayUseItemAt(blockPos, direction, itemStack)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
/* 85 */     if (this.type == EntityType.PAINTING && display.shows(DataComponents.PAINTING_VARIANT)) {
/* 86 */       Holder<PaintingVariant> variant = (Holder)itemStack.get(DataComponents.PAINTING_VARIANT);
/* 87 */       if (variant != null) {
/* 88 */         ((PaintingVariant)variant.value()).title().ifPresent(builder);
/* 89 */         ((PaintingVariant)variant.value()).author().ifPresent(builder);
/* 90 */         builder.accept(Component.translatable("painting.dimensions", new Object[] { Integer.valueOf(((PaintingVariant)variant.value()).width()), Integer.valueOf(((PaintingVariant)variant.value()).height()) }));
/* 91 */       } else if (tooltipFlag.isCreative()) {
/* 92 */         builder.accept(TOOLTIP_RANDOM_VARIANT);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\HangingEntityItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */