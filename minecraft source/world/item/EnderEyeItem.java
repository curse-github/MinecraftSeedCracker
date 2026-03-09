/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EyeOfEnder;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EnderEyeItem
/*     */   extends Item
/*     */ {
/*  33 */   public EnderEyeItem(Item.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  38 */     Level level = context.getLevel();
/*  39 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  41 */     BlockState targetState = level.getBlockState(pos);
/*     */     
/*  43 */     if (!targetState.is(Blocks.END_PORTAL_FRAME) || ((Boolean)targetState.getValue(EndPortalFrameBlock.HAS_EYE)).booleanValue()) {
/*  44 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/*  47 */     if (level.isClientSide()) {
/*  48 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  51 */     BlockState newState = (BlockState)targetState.setValue(EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(true));
/*  52 */     Block.pushEntitiesUp(targetState, newState, level, pos);
/*  53 */     level.setBlock(pos, newState, 2);
/*  54 */     level.updateNeighbourForOutputSignal(pos, Blocks.END_PORTAL_FRAME);
/*  55 */     context.getItemInHand().shrink(1);
/*     */     
/*  57 */     level.levelEvent(1503, pos, 0);
/*     */ 
/*     */     
/*  60 */     BlockPattern.BlockPatternMatch match = EndPortalFrameBlock.getOrCreatePortalShape().find(level, pos);
/*  61 */     if (match != null) {
/*  62 */       BlockPos blockPos = match.getFrontTopLeft().offset(-3, 0, -3);
/*  63 */       for (int x = 0; x < 3; x++) {
/*  64 */         for (int z = 0; z < 3; z++) {
/*  65 */           BlockPos portalBlockPos = blockPos.offset(x, 0, z);
/*  66 */           level.destroyBlock(portalBlockPos, true, null);
/*  67 */           level.setBlock(portalBlockPos, Blocks.END_PORTAL.defaultBlockState(), 2);
/*     */         } 
/*     */       } 
/*  70 */       level.globalLevelEvent(1038, blockPos.offset(1, 0, 1), 0);
/*     */     } 
/*     */     
/*  73 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public int getUseDuration(ItemStack itemStack, LivingEntity user) { return 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/*  85 */     ItemStack itemStack = player.getItemInHand(hand);
/*  86 */     BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
/*  87 */     if (hitResult.getType() == HitResult.Type.BLOCK && 
/*  88 */       level.getBlockState(hitResult.getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
/*  89 */       return InteractionResult.PASS;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     player.startUsingItem(hand);
/*  97 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  98 */       BlockPos nearestMapFeature = serverLevel.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, player.blockPosition(), 100, false);
/*  99 */       if (nearestMapFeature == null)
/*     */       {
/* 101 */         return InteractionResult.CONSUME;
/*     */       }
/* 103 */       EyeOfEnder eyeOfEnder = new EyeOfEnder(level, player.getX(), player.getY(0.5D), player.getZ());
/* 104 */       eyeOfEnder.setItem(itemStack);
/* 105 */       eyeOfEnder.signalTo(Vec3.atLowerCornerOf(nearestMapFeature));
/* 106 */       level.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeOfEnder.position(), GameEvent.Context.of(player));
/* 107 */       level.addFreshEntity(eyeOfEnder);
/*     */       
/* 109 */       if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 110 */         CriteriaTriggers.USED_ENDER_EYE.trigger(serverPlayer, nearestMapFeature); }
/*     */ 
/*     */       
/* 113 */       float pitch = Mth.lerp(level.random.nextFloat(), 0.33F, 0.5F);
/* 114 */       level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, pitch);
/* 115 */       itemStack.consume(1, player);
/* 116 */       player.awardStat(Stats.ITEM_USED.get(this)); }
/*     */     
/* 118 */     return InteractionResult.SUCCESS_SERVER;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\EnderEyeItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */