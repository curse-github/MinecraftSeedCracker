/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class TntBlock extends Block {
/*  33 */   public static final MapCodec<TntBlock> CODEC = simpleCodec(TntBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  37 */   public MapCodec<TntBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  40 */   public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
/*     */   
/*     */   public TntBlock(BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     registerDefaultState((BlockState)defaultBlockState().setValue(UNSTABLE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  49 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/*  52 */     if (level.hasNeighborSignal(pos) && 
/*  53 */       prime(level, pos)) {
/*  54 */       level.removeBlock(pos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  61 */     if (level.hasNeighborSignal(pos) && 
/*  62 */       prime(level, pos)) {
/*  63 */       level.removeBlock(pos, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/*  70 */     if (!level.isClientSide() && !(player.getAbilities()).instabuild && ((Boolean)state.getValue(UNSTABLE)).booleanValue()) {
/*  71 */       prime(level, pos);
/*     */     }
/*     */     
/*  74 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
/*  79 */     if (!((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     PrimedTnt primed = new PrimedTnt(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
/*  84 */     int fuse = primed.getFuse();
/*  85 */     primed.setFuse((short)(level.random.nextInt(fuse / 4) + fuse / 8));
/*  86 */     level.addFreshEntity(primed);
/*     */   }
/*     */ 
/*     */   
/*  90 */   public static boolean prime(Level level, BlockPos pos) { return prime(level, pos, null); }
/*     */ 
/*     */   
/*     */   private static boolean prime(Level level, BlockPos pos, LivingEntity source) {
/*  94 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/*     */ 
/*     */ 
/*     */         
/*  98 */         PrimedTnt tnt = new PrimedTnt(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, source);
/*  99 */         level.addFreshEntity(tnt);
/* 100 */         level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 101 */         level.gameEvent(source, GameEvent.PRIME_FUSE, pos);
/* 102 */         return true;
/*     */       }  }
/*     */     
/*     */     return false;
/*     */   } protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 107 */     if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
/* 108 */       return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
/*     */     }
/*     */     
/* 111 */     if (prime(level, pos, player))
/* 112 */     { level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/*     */       
/* 114 */       Item item = itemStack.getItem();
/* 115 */       if (itemStack.is(Items.FLINT_AND_STEEL)) {
/* 116 */         itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
/*     */       } else {
/* 118 */         itemStack.consume(1, player);
/*     */       } 
/* 120 */       player.awardStat(Stats.ITEM_USED.get(item)); }
/* 121 */     else if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (!((Boolean)serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 122 */         player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
/* 123 */         return InteractionResult.PASS;
/*     */       }  }
/* 125 */      return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) {
/* 130 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 131 */       BlockPos pos = blockHit.getBlockPos();
/* 132 */       Entity owner = projectile.getOwner();
/* 133 */       if (projectile.isOnFire() && projectile.mayInteract(serverLevel, pos) && 
/* 134 */         prime(level, pos, (owner instanceof LivingEntity) ? (LivingEntity)owner : null)) {
/* 135 */         level.removeBlock(pos, false);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public boolean dropFromExplosion(Explosion explosion) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { UNSTABLE }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TntBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */