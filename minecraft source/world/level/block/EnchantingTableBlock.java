/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.EnchantmentMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnchantingTableBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<EnchantingTableBlock> CODEC = simpleCodec(EnchantingTableBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  35 */   public MapCodec<EnchantingTableBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  38 */   public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2)
/*  39 */     .filter(pos -> (Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2))
/*  40 */     .map(BlockPos::immutable)
/*  41 */     .toList();
/*     */   
/*  43 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 12.0D);
/*     */ 
/*     */   
/*  46 */   protected EnchantingTableBlock(BlockBehaviour.Properties properties) { super(properties); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static boolean isValidBookShelf(Level level, BlockPos pos, BlockPos offset) { return (level.getBlockState(pos.offset(offset)).is(BlockTags.ENCHANTMENT_POWER_PROVIDER) && level.getBlockState(pos.offset(offset.getX() / 2, offset.getY(), offset.getZ() / 2)).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  65 */     super.animateTick(state, level, pos, random);
/*     */     
/*  67 */     for (BlockPos offset : BOOKSHELF_OFFSETS) {
/*  68 */       if (random.nextInt(16) == 0 && isValidBookShelf(level, pos, offset)) {
/*  69 */         level.addParticle(ParticleTypes.ENCHANT, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, (offset.getX() + random.nextFloat()) - 0.5D, (offset.getY() - random.nextFloat() - 1.0F), (offset.getZ() + random.nextFloat()) - 0.5D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new EnchantingTableBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return level.isClientSide() ? createTickerHelper(type, BlockEntityType.ENCHANTING_TABLE, EnchantingTableBlockEntity::bookAnimationTick) : null; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  86 */     if (!level.isClientSide()) {
/*  87 */       player.openMenu(state.getMenuProvider(level, pos));
/*     */     }
/*  89 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
/*  94 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  95 */     if (blockEntity instanceof EnchantingTableBlockEntity) { EnchantingTableBlockEntity enchantingTable = (EnchantingTableBlockEntity)blockEntity;
/*  96 */       Component title = enchantingTable.getDisplayName();
/*     */       
/*  98 */       return new SimpleMenuProvider((containerId, inventory, player) -> new EnchantmentMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), title); }
/*     */     
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EnchantingTableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */