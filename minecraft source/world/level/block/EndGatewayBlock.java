/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EndGatewayBlock
/*     */   extends BaseEntityBlock
/*     */   implements Portal {
/*  29 */   public static final MapCodec<EndGatewayBlock> CODEC = simpleCodec(EndGatewayBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  33 */   public MapCodec<EndGatewayBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  37 */   protected EndGatewayBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new TheEndGatewayBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.END_GATEWAY, level.isClientSide() ? TheEndGatewayBlockEntity::beamAnimationTick : TheEndGatewayBlockEntity::portalTick); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  52 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  53 */     if (!(blockEntity instanceof TheEndGatewayBlockEntity)) {
/*     */       return;
/*     */     }
/*  56 */     int particleCount = ((TheEndGatewayBlockEntity)blockEntity).getParticleAmount();
/*  57 */     for (int i = 0; i < particleCount; i++) {
/*  58 */       double x = pos.getX() + random.nextDouble();
/*  59 */       double y = pos.getY() + random.nextDouble();
/*  60 */       double z = pos.getZ() + random.nextDouble();
/*  61 */       double xa = (random.nextDouble() - 0.5D) * 0.5D;
/*  62 */       double ya = (random.nextDouble() - 0.5D) * 0.5D;
/*  63 */       double za = (random.nextDouble() - 0.5D) * 0.5D;
/*     */       
/*  65 */       int flip = random.nextInt(2) * 2 - 1;
/*  66 */       if (random.nextBoolean()) {
/*  67 */         z = pos.getZ() + 0.5D + 0.25D * flip;
/*  68 */         za = (random.nextFloat() * 2.0F * flip);
/*     */       } else {
/*  70 */         x = pos.getX() + 0.5D + 0.25D * flip;
/*  71 */         xa = (random.nextFloat() * 2.0F * flip);
/*     */       } 
/*     */       
/*  74 */       level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) { return ItemStack.EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   protected boolean canBeReplaced(BlockState state, Fluid fluid) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  90 */     if (entity.canUsePortal(false)) {
/*  91 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  92 */       if (!level.isClientSide() && blockEntity instanceof TheEndGatewayBlockEntity) { TheEndGatewayBlockEntity endGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity;
/*  93 */         if (!endGatewayBlockEntity.isCoolingDown()) {
/*  94 */           entity.setAsInsidePortal(this, pos);
/*  95 */           TheEndGatewayBlockEntity.triggerCooldown(level, pos, state, endGatewayBlockEntity);
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public TeleportTransition getPortalDestination(ServerLevel currentLevel, Entity entity, BlockPos portalEntryPos) {
/*     */     TheEndGatewayBlockEntity endGatewayBlockEntity;
/* 103 */     BlockEntity blockEntity = currentLevel.getBlockEntity(portalEntryPos);
/* 104 */     if (blockEntity instanceof TheEndGatewayBlockEntity) { endGatewayBlockEntity = (TheEndGatewayBlockEntity)blockEntity; }
/* 105 */     else { return null; }
/*     */ 
/*     */     
/* 108 */     Vec3 teleportPosition = endGatewayBlockEntity.getPortalPosition(currentLevel, portalEntryPos);
/*     */     
/* 110 */     if (teleportPosition == null) {
/* 111 */       return null;
/*     */     }
/*     */     
/* 114 */     if (entity instanceof net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl)
/*     */     {
/*     */       
/* 117 */       return new TeleportTransition(currentLevel, teleportPosition, Vec3.ZERO, 0.0F, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 123 */           Set.of(), TeleportTransition.PLACE_PORTAL_TICKET);
/*     */     }
/*     */ 
/*     */     
/* 127 */     return new TeleportTransition(currentLevel, teleportPosition, Vec3.ZERO, 0.0F, 0.0F, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         Relative.union(new Set[] { Relative.DELTA, Relative.ROTATION }, ), TeleportTransition.PLACE_PORTAL_TICKET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EndGatewayBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */