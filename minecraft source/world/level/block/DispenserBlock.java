/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.dispenser.BlockSource;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DispenserBlock extends BaseEntityBlock {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  48 */   public static final MapCodec<DispenserBlock> CODEC = simpleCodec(DispenserBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  52 */   public MapCodec<? extends DispenserBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*  56 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*     */   
/*  58 */   private static final DefaultDispenseItemBehavior DEFAULT_BEHAVIOR = new DefaultDispenseItemBehavior();
/*  59 */   public static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY = new IdentityHashMap();
/*     */   
/*     */   private static final int TRIGGER_DURATION = 4;
/*     */   
/*  63 */   public static void registerBehavior(ItemLike item, DispenseItemBehavior behavior) { DISPENSER_REGISTRY.put(item.asItem(), behavior); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static void registerProjectileBehavior(ItemLike item) { DISPENSER_REGISTRY.put(item.asItem(), new ProjectileDispenseBehavior(item.asItem())); }
/*     */ 
/*     */   
/*     */   protected DispenserBlock(BlockBehaviour.Properties properties) {
/*  71 */     super(properties);
/*  72 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  77 */     if (!level.isClientSide()) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof DispenserBlockEntity) { DispenserBlockEntity dispenser = (DispenserBlockEntity)blockEntity;
/*  78 */         player.openMenu(dispenser);
/*  79 */         player.awardStat((dispenser instanceof net.minecraft.world.level.block.entity.DropperBlockEntity) ? Stats.INSPECT_DROPPER : Stats.INSPECT_DISPENSER); }
/*     */        }
/*  81 */      return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
/*  85 */     DispenserBlockEntity blockEntity = (DispenserBlockEntity)level.getBlockEntity(pos, BlockEntityType.DISPENSER).orElse(null);
/*  86 */     if (blockEntity == null) {
/*  87 */       LOGGER.warn("Ignoring dispensing attempt for Dispenser without matching block entity at {}", pos);
/*     */       return;
/*     */     } 
/*  90 */     BlockSource source = new BlockSource(level, pos, state, blockEntity);
/*     */     
/*  92 */     int slot = blockEntity.getRandomSlot(level.random);
/*  93 */     if (slot < 0) {
/*  94 */       level.levelEvent(1001, pos, 0);
/*  95 */       level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(blockEntity.getBlockState()));
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     ItemStack itemStack = blockEntity.getItem(slot);
/* 100 */     DispenseItemBehavior behavior = getDispenseMethod(level, itemStack);
/*     */     
/* 102 */     if (behavior != DispenseItemBehavior.NOOP) {
/* 103 */       blockEntity.setItem(slot, behavior.dispense(source, itemStack));
/*     */     }
/*     */   }
/*     */   
/*     */   protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack itemStack) {
/* 108 */     if (!itemStack.isItemEnabled(level.enabledFeatures())) {
/* 109 */       return DEFAULT_BEHAVIOR;
/*     */     }
/* 111 */     DispenseItemBehavior behavior = (DispenseItemBehavior)DISPENSER_REGISTRY.get(itemStack.getItem());
/* 112 */     if (behavior != null) {
/* 113 */       return behavior;
/*     */     }
/* 115 */     return getDefaultDispenseMethod(itemStack);
/*     */   }
/*     */   
/*     */   private static DispenseItemBehavior getDefaultDispenseMethod(ItemStack itemStack) {
/* 119 */     if (itemStack.has(DataComponents.EQUIPPABLE)) {
/* 120 */       return EquipmentDispenseItemBehavior.INSTANCE;
/*     */     }
/* 122 */     return DEFAULT_BEHAVIOR;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 127 */     boolean shouldTrigger = (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()));
/* 128 */     boolean isTriggered = ((Boolean)state.getValue(TRIGGERED)).booleanValue();
/*     */     
/* 130 */     if (shouldTrigger && !isTriggered) {
/* 131 */       level.scheduleTick(pos, this, 4);
/* 132 */       level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, Boolean.valueOf(true)), 2);
/* 133 */     } else if (!shouldTrigger && isTriggered) {
/* 134 */       level.setBlock(pos, (BlockState)state.setValue(TRIGGERED, Boolean.valueOf(false)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 140 */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { dispenseFrom(level, state, pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new DispenserBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static Position getDispensePosition(BlockSource source) { return getDispensePosition(source, 0.7D, Vec3.ZERO); }
/*     */ 
/*     */   
/*     */   public static Position getDispensePosition(BlockSource source, double scale, Vec3 offset) {
/* 163 */     Direction direction = (Direction)source.state().getValue(FACING);
/*     */     
/* 165 */     return source.center().add(scale * direction
/* 166 */         .getStepX() + offset.x(), scale * direction
/* 167 */         .getStepY() + offset.y(), scale * direction
/* 168 */         .getStepZ() + offset.z());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, TRIGGERED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DispenserBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */