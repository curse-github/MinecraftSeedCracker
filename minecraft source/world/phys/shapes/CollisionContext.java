/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CollisionContext
/*    */ {
/* 17 */   static CollisionContext empty() { return EntityCollisionContext.Empty.WITHOUT_FLUID_COLLISIONS; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   static CollisionContext emptyWithFluidCollisions() { return EntityCollisionContext.Empty.WITH_FLUID_COLLISIONS; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity entity) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: dup
/*    */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   5: pop
/*    */     //   6: astore_1
/*    */     //   7: iconst_0
/*    */     //   8: istore_2
/*    */     //   9: aload_1
/*    */     //   10: iload_2
/*    */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   16: lookupswitch default -> 76, 0 -> 36
/*    */     //   36: aload_1
/*    */     //   37: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*    */     //   40: astore_3
/*    */     //   41: aload_3
/*    */     //   42: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*    */     //   45: invokestatic useExperimentalMovement : (Lnet/minecraft/world/level/Level;)Z
/*    */     //   48: ifeq -> 63
/*    */     //   51: new net/minecraft/world/phys/shapes/MinecartCollisionContext
/*    */     //   54: dup
/*    */     //   55: aload_3
/*    */     //   56: iconst_0
/*    */     //   57: invokespecial <init> : (Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;Z)V
/*    */     //   60: goto -> 86
/*    */     //   63: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   66: dup
/*    */     //   67: aload_0
/*    */     //   68: iconst_0
/*    */     //   69: iconst_0
/*    */     //   70: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   73: goto -> 86
/*    */     //   76: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   79: dup
/*    */     //   80: aload_0
/*    */     //   81: iconst_0
/*    */     //   82: iconst_0
/*    */     //   83: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   86: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     //   #27	-> 36
/*    */     //   #28	-> 41
/*    */     //   #29	-> 51
/*    */     //   #31	-> 63
/*    */     //   #33	-> 76
/*    */     //   #26	-> 86
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   41	35	3	minecart	Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;
/*    */     //   0	87	0	entity	Lnet/minecraft/world/entity/Entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   static CollisionContext of(Entity entity, boolean alwaysCollideWithFluid) { return new EntityCollisionContext(entity, alwaysCollideWithFluid, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext placementContext(Player player) {
/* 47 */     Player player1 = player; return new EntityCollisionContext((player != null) ? player.isDescending() : 0, true, (player != null) ? player.getY() : -1.7976931348623157E308D, (player instanceof LivingEntity) ? player1.getMainHandItem() : ItemStack.EMPTY, false, player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext withPosition(Entity entity, double position) {
/* 58 */     LivingEntity livingEntity = (LivingEntity)entity; return new EntityCollisionContext((entity != null) ? entity.isDescending() : 0, true, (entity != null) ? position : -1.7976931348623157E308D, (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY, false, entity);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isDescending();
/*    */ 
/*    */   
/*    */   boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   boolean isHoldingItem(Item paramItem);
/*    */ 
/*    */   
/*    */   boolean alwaysCollideWithFluid();
/*    */   
/*    */   boolean canStandOnFluid(FluidState paramFluidState1, FluidState paramFluidState2);
/*    */   
/*    */   VoxelShape getCollisionShape(BlockState paramBlockState, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos);
/*    */   
/* 77 */   default boolean isPlacement() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\phys\shapes\CollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */