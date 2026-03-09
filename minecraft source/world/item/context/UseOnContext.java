/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UseOnContext
/*    */ {
/*    */   private final Player player;
/*    */   private final InteractionHand hand;
/*    */   private final BlockHitResult hitResult;
/*    */   private final Level level;
/*    */   private final ItemStack itemStack;
/*    */   
/* 22 */   public UseOnContext(Player player, InteractionHand hand, BlockHitResult hitResult) { this(player.level(), player, hand, player.getItemInHand(hand), hitResult); }
/*    */ 
/*    */   
/*    */   protected UseOnContext(Level level, Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
/* 26 */     this.player = player;
/* 27 */     this.hand = hand;
/* 28 */     this.hitResult = hitResult;
/*    */     
/* 30 */     this.itemStack = itemStack;
/* 31 */     this.level = level;
/*    */   }
/*    */ 
/*    */   
/* 35 */   protected final BlockHitResult getHitResult() { return this.hitResult; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public BlockPos getClickedPos() { return this.hitResult.getBlockPos(); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Direction getClickedFace() { return this.hitResult.getDirection(); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Vec3 getClickLocation() { return this.hitResult.getLocation(); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean isInside() { return this.hitResult.isInside(); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public ItemStack getItemInHand() { return this.itemStack; }
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Player getPlayer() { return this.player; }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public InteractionHand getHand() { return this.hand; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Level getLevel() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public Direction getHorizontalDirection() { return (this.player == null) ? Direction.NORTH : this.player.getDirection(); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public boolean isSecondaryUseActive() { return (this.player != null && this.player.isSecondaryUseActive()); }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public float getRotation() { return (this.player == null) ? 0.0F : this.player.getYRot(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\context\UseOnContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */