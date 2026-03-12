/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ButtonBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  44 */   public static final MapCodec<ButtonBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/*  45 */         .fieldOf("block_set_type").forGetter(()), 
/*  46 */         Codec.intRange(1, 1024).fieldOf("ticks_to_stay_pressed").forGetter(()), 
/*  47 */         propertiesCodec())
/*  48 */       .apply(i, ButtonBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  52 */   public MapCodec<ButtonBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  55 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   private final int ticksToStayPressed;
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected ButtonBlock(BlockSetType type, int ticksToStayPressed, BlockBehaviour.Properties properties) {
/*  63 */     super(properties.sound(type.soundType()));
/*     */     
/*  65 */     this.type = type;
/*  66 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, Boolean.valueOf(false))).setValue(FACE, AttachFace.WALL));
/*  67 */     this.ticksToStayPressed = ticksToStayPressed;
/*  68 */     this.shapes = makeShapes();
/*     */   }
/*     */ 
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  73 */     VoxelShape pressedShaper = Block.cube(14.0D);
/*  74 */     VoxelShape unpressedShaper = Block.cube(12.0D);
/*     */ 
/*     */     
/*  77 */     Map<AttachFace, Map<Direction, VoxelShape>> attachFace = Shapes.rotateAttachFace(Block.boxZ(6.0D, 4.0D, 8.0D, 16.0D));
/*     */     
/*  79 */     return getShapeForEachState(state -> Shapes.join((VoxelShape)((Map)attachFace
/*  80 */           .get(state.getValue(FACE))).get(state.getValue(FACING)), 
/*  81 */           ((Boolean)state.getValue(POWERED)).booleanValue() ? pressedShaper : unpressedShaper, BooleanOp.ONLY_FIRST));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  93 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/*  94 */       return InteractionResult.CONSUME;
/*     */     }
/*  96 */     press(state, level, pos, player);
/*  97 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 102 */     if (explosion.canTriggerBlocks() && !((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 103 */       press(state, level, pos, null);
/*     */     }
/* 105 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */   
/*     */   public void press(BlockState state, Level level, BlockPos pos, Player player) {
/* 109 */     level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(true)), 3);
/* 110 */     updateNeighbours(state, level, pos);
/* 111 */     level.scheduleTick(pos, this, this.ticksToStayPressed);
/* 112 */     playSound(player, level, pos, true);
/* 113 */     level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
/*     */   }
/*     */ 
/*     */   
/* 117 */   protected void playSound(Player player, LevelAccessor level, BlockPos pos, boolean pressed) { level.playSound(pressed ? player : null, pos, getSound(pressed), SoundSource.BLOCKS); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   protected SoundEvent getSound(boolean pressed) { return pressed ? this.type.buttonClickOn() : this.type.buttonClickOff(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 126 */     if (!movedByPiston && ((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 127 */       updateNeighbours(state, level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 133 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 138 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && getConnectedDirection(state) == direction) {
/* 139 */       return 15;
/*     */     }
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 151 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     checkPressed(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 160 */     if (level.isClientSide() || !this.type.canButtonBeActivatedByArrows() || ((Boolean)state.getValue(POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     checkPressed(state, level, pos);
/*     */   }
/*     */   
/*     */   protected void checkPressed(BlockState state, Level level, BlockPos pos) {
/* 168 */     AbstractArrow firstArrow = this.type.canButtonBeActivatedByArrows() ? (AbstractArrow)level.getEntitiesOfClass(AbstractArrow.class, state.getShape(level, pos).bounds().move(pos)).stream().findFirst().orElse(null) : null;
/*     */     
/* 170 */     boolean shouldBePressed = (firstArrow != null);
/* 171 */     boolean wasPressed = ((Boolean)state.getValue(POWERED)).booleanValue();
/*     */     
/* 173 */     if (shouldBePressed != wasPressed) {
/* 174 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(shouldBePressed)), 3);
/* 175 */       updateNeighbours(state, level, pos);
/* 176 */       playSound(null, level, pos, shouldBePressed);
/* 177 */       level.gameEvent(firstArrow, shouldBePressed ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
/*     */     } 
/*     */     
/* 180 */     if (shouldBePressed) {
/* 181 */       level.scheduleTick(new BlockPos(pos), this, this.ticksToStayPressed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/* 187 */     Direction front = getConnectedDirection(state).getOpposite();
/* 188 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, front.getAxis().isHorizontal() ? Direction.UP : (Direction)state.getValue(FACING));
/* 189 */     level.updateNeighborsAt(pos, this, orientation);
/* 190 */     level.updateNeighborsAt(pos.relative(front), this, orientation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 195 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, POWERED, FACE }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ButtonBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */