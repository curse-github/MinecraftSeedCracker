/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class BrushItem
/*     */   extends Item
/*     */ {
/*     */   public static final int ANIMATION_DURATION = 10;
/*     */   private static final int USE_DURATION = 200;
/*     */   
/*  37 */   public BrushItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  42 */     Player player = context.getPlayer();
/*  43 */     if (player != null && calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
/*  44 */       player.startUsingItem(context.getHand());
/*     */     }
/*     */     
/*  47 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) { return ItemUseAnimation.BRUSH; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public int getUseDuration(ItemStack itemStack, LivingEntity user) { return 200; }
/*     */ 
/*     */   
/*     */   public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
/*     */     Player player;
/*  62 */     if (ticksRemaining >= 0 && livingEntity instanceof Player) { player = (Player)livingEntity; }
/*  63 */     else { livingEntity.releaseUsingItem();
/*     */       
/*     */       return; }
/*     */     
/*  67 */     HitResult hitResult = calculateHitResult(player);
/*  68 */     if (hitResult instanceof BlockHitResult) { BlockHitResult blockHitResult = (BlockHitResult)hitResult; if (hitResult.getType() == HitResult.Type.BLOCK) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  73 */         int timeElapsed = getUseDuration(itemStack, livingEntity) - ticksRemaining + 1;
/*  74 */         boolean isLastTickBeforeBackswing = (timeElapsed % 10 == 5);
/*     */         
/*  76 */         if (isLastTickBeforeBackswing) {
/*  77 */           SoundEvent brushSound; BlockPos pos = blockHitResult.getBlockPos();
/*  78 */           BlockState state = level.getBlockState(pos);
/*     */ 
/*     */ 
/*     */           
/*  82 */           HumanoidArm brushingArm = (livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND) ? player.getMainArm() : player.getMainArm().getOpposite();
/*     */           
/*  84 */           if (state.shouldSpawnTerrainParticles() && state.getRenderShape() != RenderShape.INVISIBLE) {
/*  85 */             spawnDustParticles(level, blockHitResult, state, livingEntity.getViewVector(0.0F), brushingArm);
/*     */           }
/*     */ 
/*     */           
/*  89 */           Block block = state.getBlock(); if (block instanceof BrushableBlock) { BrushableBlock brushableBlock = (BrushableBlock)block;
/*  90 */             brushSound = brushableBlock.getBrushSound(); }
/*     */           else
/*  92 */           { brushSound = SoundEvents.BRUSH_GENERIC; }
/*     */ 
/*     */           
/*  95 */           level.playSound(player, pos, brushSound, SoundSource.BLOCKS);
/*     */           
/*  97 */           if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  98 */             BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BrushableBlockEntity) { BrushableBlockEntity brushableBlockEntity = (BrushableBlockEntity)blockEntity;
/*  99 */               boolean brushingUpdatedState = brushableBlockEntity.brush(level.getGameTime(), serverLevel, player, blockHitResult.getDirection(), itemStack);
/*     */               
/* 101 */               if (brushingUpdatedState) {
/*     */ 
/*     */                 
/* 104 */                 EquipmentSlot equippedHand = itemStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
/* 105 */                 itemStack.hurtAndBreak(1, player, equippedHand);
/*     */               }  }
/*     */              }
/*     */         
/*     */         }  return;
/*     */       }  }
/*     */     
/* 112 */     livingEntity.releaseUsingItem(); } private HitResult calculateHitResult(Player player) { return ProjectileUtil.getHitResultOnViewVector(player, EntitySelector.CAN_BE_PICKED, player.blockInteractionRange()); }
/*     */ 
/*     */   
/*     */   private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState state, Vec3 viewVector, HumanoidArm brushingArm) {
/* 116 */     double deltaScale = 3.0D;
/* 117 */     int flip = (brushingArm == HumanoidArm.RIGHT) ? 1 : -1;
/* 118 */     int particles = level.getRandom().nextInt(7, 12);
/* 119 */     BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
/*     */     
/* 121 */     Direction hitDirection = hitResult.getDirection();
/* 122 */     DustParticlesDelta dustParticlesDelta = DustParticlesDelta.fromDirection(viewVector, hitDirection);
/* 123 */     Vec3 hitLocation = hitResult.getLocation();
/*     */     
/* 125 */     for (int i = 0; i < particles; i++)
/* 126 */       level.addParticle(particle, hitLocation.x - (
/*     */           
/* 128 */           (hitDirection == Direction.WEST) ? 1.0E-6F : 0.0F), hitLocation.y, hitLocation.z - (
/*     */           
/* 130 */           (hitDirection == Direction.NORTH) ? 1.0E-6F : 0.0F), dustParticlesDelta
/* 131 */           .xd() * flip * 3.0D * level.getRandom().nextDouble(), 0.0D, dustParticlesDelta
/*     */           
/* 133 */           .zd() * flip * 3.0D * level.getRandom().nextDouble()); 
/*     */   }
/*     */   private static final class DustParticlesDelta extends Record { private final double xd; private final double yd; private final double zd; private static final double ALONG_SIDE_DELTA = 1.0D;
/*     */     private static final double OUT_FROM_SIDE_DELTA = 0.1D;
/*     */     
/* 138 */     private DustParticlesDelta(double xd, double yd, double zd) { this.xd = xd; this.yd = yd; this.zd = zd; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/BrushItem$DustParticlesDelta;
/* 138 */       //   0	8	1	o	Ljava/lang/Object; } public double xd() { return this.xd; } public double yd() { return this.yd; } public double zd() { return this.zd; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static DustParticlesDelta fromDirection(Vec3 viewVector, Direction hitDirection) {
/* 144 */       double yd = 0.0D;
/* 145 */       switch (BrushItem.null.$SwitchMap$net$minecraft$core$Direction[hitDirection.ordinal()]) { default: throw new MatchException(null, null);case 1: case 2: case 3: case 4: case 5: case 6: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 150 */         new DustParticlesDelta(0.1D, 0.0D, 1.0D);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BrushItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */